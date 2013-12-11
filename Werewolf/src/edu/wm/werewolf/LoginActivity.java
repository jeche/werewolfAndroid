package edu.wm.werewolf;

import java.io.IOException;
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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	Button registerButton;
	Button loginButton;
	EditText usernameText;
	EditText passwordText;
	boolean clicked = false;
	private JSONObject response;
	Constants c= new Constants();
	private static final String TAG = "LoginActivity";
	
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
					Log.v(null, "going to gamestatus");
					Context context3 = getApplicationContext();
					CharSequence text3 = "Going to Game Status";
					int duration3 = Toast.LENGTH_SHORT;
					Toast toast3 = Toast.makeText(context3, text3, duration3);
					toast3.show();
					Intent intent2 = new Intent(context3, GameStatus.class);
					intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent2.putExtra("username", usernameText.getText().toString());
					intent2.putExtra("password", passwordText.getText().toString());
					if(response.getString(c.isWerewolf())!=null){
						intent2.putExtra(c.isWerewolf(), response.getBoolean(c.isWerewolf()));
					}else{
						intent2.putExtra(c.isWerewolf(), false);
					}
					intent2.putExtra(c.getGameStatus(), response.getString(c.getGameStatus()));
					intent2.putExtra(c.createTime(), response.getLong(c.createTime()));
					intent2.putExtra(c.nightFreq(), response.getLong(c.nightFreq()));
					intent2.putExtra(c.allPlayers(), response.toString());
					Log.v(TAG, response.toString());
					Log.v(TAG, response.get("players").toString());
					startActivity(intent2);
					
			  }else{
		
				  	Context context3 = getApplicationContext();
					CharSequence text3 = "Authentication Failed.";
					int duration3 = Toast.LENGTH_SHORT;
					Toast toast3 = Toast.makeText(context3, text3, duration3);
					toast3.show();
			  }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		
			Context context3 = getApplicationContext();
			CharSequence text3 = "Authentication Failed.";
			int duration3 = Toast.LENGTH_SHORT;
			Toast toast3 = Toast.makeText(context3, text3, duration3);
			if(response != null){
			System.out.println(response.toString());
			}
			toast3.show();
			e.printStackTrace();
		}
	      clicked = false;
	    }
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeruser);
		usernameText = (EditText) findViewById(R.id.editText2);
		passwordText = (EditText) findViewById(R.id.editText1);
		loginButton = (Button) findViewById(R.id.button2);
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(!clicked){
					DownloadWebPageTask task = new DownloadWebPageTask(false, usernameText.getText().toString(), passwordText.getText().toString(), null, false);
					task.execute(new String[] {c.statusURL()});
					clicked = true;
				}
			}
			});
		registerButton = (Button) findViewById(R.id.profile_button);
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
		MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.wolf_howl);
//		try {
//			mediaPlayer.prepare();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		mediaPlayer.start(); // no need to call prepare(); create() does that for you
		registerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(!clicked){
					clicked = true;
					Log.v(null, "going to registration");
				
					Context context3 = getApplicationContext();
					Intent intent2 = new Intent(context3, RegisterActivity.class);
					intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent2);
				}
			}
			});
	}
}
