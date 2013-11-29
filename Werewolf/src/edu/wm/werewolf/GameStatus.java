package edu.wm.werewolf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
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

import edu.wm.werewolf.service.GameUpdateService;
import edu.wm.werewolf.web.Constants;
import edu.wm.werewolf.web.WebPageTask;
import android.R.color;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GameStatus extends Activity {
	
	JSONObject response;
	JSONArray responseArray;
	boolean isNight;
	String username;
	String password;
	int col;
	int col2;
	long color = 0;
	int n;
	int d;
	
	private class DownloadWebPageTask extends WebPageTask {

	    public DownloadWebPageTask(boolean hasPairs, String username,
				String password, List<NameValuePair> pairs, boolean isPost) {
			super(hasPairs, username, password, pairs, isPost);
			System.out.println("");
		}

		@Override
	    protected void onPostExecute(String result) {
	    	clicked = false;
	    	Log.v(null, "Post executed");
	    	Log.v(null, "RESULT VAL:" + result);
	    	Intent intent = new Intent(getApplicationContext(), PlayerList.class);
	    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	intent.putExtra("playerList", result);
	    	intent.putExtra(c.isWerewolf(), isWerewolf);
	    	intent.putExtra(c.createTime(), created);
	    	intent.putExtra(c.nightFreq(), freq);
	    	intent.putExtra("username", username);
	    	intent.putExtra("password", password);
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
	boolean isWerewolf;
	long created = 0L;
	long freq = 0L;
	View me;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gamestatus);
		n = getResources().getColor(R.color.night);
		d = getResources().getColor(R.color.day);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);
		
//		Intent intent = new Intent(this, GameUpdateService.class);
		me = findViewById(R.id.gamestatus);
		username = getIntent().getExtras().getString("username") ;
//		intent.putExtra("username", username);
		password = getIntent().getExtras().getString("password");
//		intent.putExtra("password", password);
//		PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0); // Used for background service
		isWerewolf = getIntent().getExtras().getBoolean(c.isWerewolf());
		isNight = getIntent().getExtras().getString(c.getGameStatus()).contains("true");
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.balance_bar);
		progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
		progressBar.setProgress(50);
		if(isNight){
			col = R.color.night;
			col2 = R.color.day;
		}else{
			col = R.color.day;
			col2 = R.color.night;
		}
		created = getIntent().getExtras().getLong(c.createTime());
		freq = getIntent().getExtras().getLong(c.nightFreq());
		timerValue = (TextView) findViewById(R.id.timerValue);
		System.out.println("FREQUENCY" + freq);
		playerListButton = (Button) findViewById(R.id.playerlist);
		playerListButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(!clicked){
					clicked = true;
					DownloadWebPageTask task = new DownloadWebPageTask(false, username, password, null, false);
					if(!isWerewolf || !isNight){				    
						task.execute(new String[] {c.aliveURL() });
					}
					else{
						task.execute(new String[] { c.scentURL()});
					}
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
		if(getIntent().getExtras().getString(c.getGameStatus()).equals("isOver")){
			timerValue.setText("No game currently running.");
			timerValue.setTextSize(20);
		}
//		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//                    60*1000, pintent);
//		Intent serviceIntent = new Intent(getBaseContext(), GameUpdateService.class);
//		serviceIntent.putExtra("username", getIntent().getExtras().getString("username"));
//		serviceIntent.putExtra("password", getIntent().getExtras().getString("password"));
//		startService(serviceIntent);
		customHandler.postDelayed(updateTimerThread, 0);
	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {
//			long color;
			long newCol;
			int change;
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			newCol = timeInMilliseconds;
			long color2;
			color2 = newCol;
			timeInMilliseconds = timeInMilliseconds % freq;
			timeInMilliseconds = freq - timeInMilliseconds;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			newCol = newCol % freq;
			newCol = freq - newCol;
			
			newCol = (newCol * 100) / freq;
			if(newCol < 15 && newCol > 0 && newCol != color){
				Log.v("color", newCol + " Color fade" + color);
				int red = Color.red(col);
				int red2 = Color.red(col2);
				int blue = Color.blue(col);
				int blue2 = Color.blue(col2);
				int green = Color.green(col);
				int green2 = Color.green(col2);
				color = newCol;
				int newRed;
				int newGreen;
				int newBlue;
				newCol = 15 - newCol;
				if(col == d){
					 newRed = (int) (red - 51 * newCol / 15);
					if(newRed < 0){
						newRed = 0;
					}
					newGreen = (int) (green - 25 * newCol / 15);
					if(newGreen < 25){
						newGreen = 25;
					}
					if(newGreen <= 102){
						newBlue = (int) (blue - 51 * newCol / 15);
						if(newBlue < 51){
							newBlue = 51;
						}
					}else{
						newBlue = 255;
					}
				}else{
					newGreen = (int) (green + 25 * newCol / 15);
					if(newGreen > 178){
						newGreen = 178;
					}
					newBlue = (int) (blue + 51 * newCol / 15);
					if(newBlue > 255 || newGreen >= 128){
						newBlue = 255;
					}
					if(newGreen >= 153){
						newRed = (int) (red + 51 * newCol / 15);
						if(newRed > 102){
							newRed = 102;
						}
					}else{
						newRed = 0;
					}
				}
				int goTo;
				float[] hsv = new float[3];
				Color.RGBToHSV(red, green, blue, hsv );
//				newCol = 15 - newCol;
				goTo = Color.rgb(newRed, newGreen, newBlue);
//				col = goTo;
//				goTo = Color.rgb((int) (red + color * (red - red2) / 15), green + (int)color *(green -green2)/15, blue + (int)color *(blue - blue2)/15);
				
//				Color.red();
//				col = col + change / 100;
				me.setBackgroundColor(goTo);
			} else if((color2 / freq) % 2 == 0 && col != n){
				me.setBackgroundColor(n);
				Log.v("color", color2 + " night");
				col = n;
				col2 = d;
			} else if(col != d && (color2 / freq) % 2 == 1){
				me.setBackgroundColor(d);
				Log.v("color", color2 + " day");
				col = d;
				col2 = n;
			}
			
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			timerValue.setText("" + mins + ":"
					+ String.format("%02d", secs)); // + ":"
//					+ String.format("%03d", milliseconds));
			customHandler.postDelayed(this, 0);
		}

	};

}
