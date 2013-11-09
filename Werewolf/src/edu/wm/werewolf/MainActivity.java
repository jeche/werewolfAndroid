package edu.wm.werewolf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wm.werewolf.service.GameUpdateService;
import edu.wm.werewolf.web.Constants;
import edu.wm.werewolf.web.WebPageTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText usernameText;
	private EditText passwordText;
	private EditText verifyPasswordText;
	private EditText firstNameText;
	private EditText lastNameText;
	private Button registerButton;
	private Constants c = new Constants();
	private String username;//do this for all
	private JSONObject response;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		usernameText = (EditText) findViewById(R.id.username);
		passwordText = (EditText) findViewById(R.id.password);
		verifyPasswordText = (EditText) findViewById(R.id.verifyPassword);
		firstNameText = (EditText) findViewById(R.id.firstName);
		lastNameText = (EditText) findViewById(R.id.lastName);
		registerButton = (Button) findViewById(R.id.createButton);
		
		if (savedInstanceState == null){
			username = "";
		}
		else{
			username = savedInstanceState.getString("username");
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("username", username);
	}
	
	private class DownloadWebPageTask extends WebPageTask {

	    public DownloadWebPageTask(boolean hasPairs, String username,
				String password, List<NameValuePair> pairs, boolean isPost) {
			super(hasPairs, username, password, pairs, isPost);
		}

		@Override
	    protected void onPostExecute(String result) {
	      try {
	    	response = new JSONObject(result);
			if(response.getString(c.responseStatus()).equals(c.success())){
					Log.v(null, "going to pref");
					Context context3 = getApplicationContext();
					CharSequence text3 = "Successful Registration!";
					int duration3 = Toast.LENGTH_SHORT;
					Toast toast3 = Toast.makeText(context3, text3, duration3);
					toast3.show();
					Intent intent2 = new Intent(context3, GameStatus.class);
					intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent2);
			  }else{
				  	Context context3 = getApplicationContext();
					CharSequence text3 = "There was an error creating your account.";
					int duration3 = Toast.LENGTH_SHORT;
					Toast toast3 = Toast.makeText(context3, text3, duration3);
					toast3.show();
			  }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    }
	  }

	  public void myClickHandler(View view) {
		if(!passwordText.getText().toString().equals(verifyPasswordText.getText().toString())){
		  	Context context3 = getApplicationContext();
			CharSequence text3 = "Passwords do not match.";
			int duration3 = Toast.LENGTH_SHORT;
			Toast toast3 = Toast.makeText(context3, text3, duration3);
			toast3.show();			
			return;
		}
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("userName", usernameText.getText().toString()));
		pairs.add(new BasicNameValuePair("id", usernameText.getText().toString()));
		pairs.add(new BasicNameValuePair("firstName", firstNameText.getText().toString()));
		pairs.add(new BasicNameValuePair("lastName", lastNameText.getText().toString()));
		pairs.add(new BasicNameValuePair("hashedPassword", passwordText.getText().toString()));
	    DownloadWebPageTask task = new DownloadWebPageTask(true, usernameText.getText().toString(), passwordText.getText().toString(), pairs, true);
	    task.execute(new String[] { c.addUserURL() });

	  }

}
