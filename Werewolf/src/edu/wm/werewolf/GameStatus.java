package edu.wm.werewolf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wm.werewolf.constants.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameStatus extends Activity {
	
	JSONObject response;
	JSONArray responseArray;
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
	    @Override
	    protected String doInBackground(String... urls) {
	      String resp = "";
	      for (String url : urls) {
	        DefaultHttpClient client = new DefaultHttpClient();
	        URI uri = null;
			try {
				uri = new URI(url);
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
	        HttpGet httpPost= new HttpGet(url);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			client.getCredentialsProvider().setCredentials(
					new AuthScope(uri.getHost(), uri.getPort(),AuthScope.ANY_SCHEME),
					new UsernamePasswordCredentials("admin", "admin"));
	        try {
//	          httpPost.setEntity(new UrlEncodedFormEntity(pairs));
	          HttpResponse execute = client.execute(httpPost);
	          InputStream content = execute.getEntity().getContent();

	          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	          String s = "";
	          while ((s = buffer.readLine()) != null) {
	            resp += s;
	          }

	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	      Log.v(null, resp);
	      
//	      try {
//			responseArray = new JSONArray(resp);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	      return resp;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	Log.v(null, "Post executed");
	    	Intent intent = new Intent(getApplicationContext(), PlayerList.class);
	    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	intent.putExtra("playerList", result);
	    	startActivity(intent);

	    }
	  }
	
	private Button startButton;
	private Button pauseButton;
	private Button refreshButton;
	private Button playerListButton;
	private boolean clicked = false;
	private Constants c = new Constants();

	private TextView timerValue;

	private long startTime = 0L;

	private Handler customHandler = new Handler();

	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gamestatus);
		timerValue = (TextView) findViewById(R.id.timerValue);

		startButton = (Button) findViewById(R.id.startButton);
		playerListButton = (Button) findViewById(R.id.playerlist);
		playerListButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(!clicked){
					DownloadWebPageTask task = new DownloadWebPageTask();
				    task.execute(new String[] { c.getBaseUrl()+"players/alive" });
				}
			}
			});
		
		refreshButton = (Button) findViewById(R.id.refresh);
		refreshButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				startTime = SystemClock.uptimeMillis();
				customHandler.postDelayed(updateTimerThread, 0);

			}
		});
		

		startButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				startTime = SystemClock.uptimeMillis();
				customHandler.postDelayed(updateTimerThread, 0);

			}
		});

		pauseButton = (Button) findViewById(R.id.pauseButton);

		pauseButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);

			}
		});

	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			timerValue.setText("" + mins + ":"
					+ String.format("%02d", secs) + ":"
					+ String.format("%03d", milliseconds));
			customHandler.postDelayed(this, 0);
		}

	};

}
