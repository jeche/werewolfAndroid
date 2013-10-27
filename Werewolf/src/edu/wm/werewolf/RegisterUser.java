package edu.wm.werewolf;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wm.werewolf.web.Constants;
import edu.wm.werewolf.web.WebPageTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUser extends Activity {
	Button registerButton;
	Button loginButton;
	private JSONObject response;
	Constants c= new Constants();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeruser);
		
		loginButton = (Button) findViewById(R.id.button2);
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
			}
			});
		registerButton = (Button) findViewById(R.id.button1);
		registerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
			}
			});
	}
	
	private class DownloadWebPageTask extends WebPageTask {

	    public DownloadWebPageTask(boolean hasPairs, String username,
				String password, List<NameValuePair> pairs, boolean isPost) {
			super(hasPairs, username, password, pairs, isPost);
		}

		@Override
	    protected void onPostExecute(String result) {
	      try {
			if(response.getString("status").equals(c.success())){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	  }

	  public void myClickHandler(View view) {
	    DownloadWebPageTask task = new DownloadWebPageTask(true, null, null, null, true);
	    task.execute(new String[] { c.getBaseUrl()+"addUser" });

	  }
}
