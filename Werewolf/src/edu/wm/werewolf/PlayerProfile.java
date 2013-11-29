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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayerProfile extends Activity {
    JSONObject response;
    String username;
    String password;
    Constants c = new Constants();
    OnClickListener l;
    boolean clicked = false;
    
    
    
    
	private class DownloadWebPageTask extends WebPageTask {

	    public DownloadWebPageTask(boolean hasPairs, String username,
				String password, List<NameValuePair> pairs, boolean isPost) {
			super(hasPairs, username, password, pairs, isPost);
		}

		@Override
	    protected void onPostExecute(String result) {
	    	clicked = false;
	    	Log.v(null, "Post executed");
	    	Log.v(null, "RESULT VAL:" + result);
			Intent i = new Intent(getApplicationContext(), PlayerProfile.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	i.putExtra("playerinfo", result);
	    	i.putExtra("username", username);
	    	i.putExtra("password", password);
//	    	intent.putExtra("isWerewolf", isWerewolf);
	    	startActivity(i);
	    }
	  }
    
    
    View me;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_playerprofile);
        me = findViewById(R.id.gamestatus);
        me.setBackgroundColor(Color.BLACK);
        TextView playerName = (TextView) findViewById(R.id.playername);
//        TextView 
        TextView playerStatus = (TextView) findViewById(R.id.player_status);
        playerStatus.setTextColor(Color.WHITE);
        Button playerButton = (Button) findViewById(R.id.profile_button);
        Intent i = getIntent();
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");
        // getting attached intent data
        try {
			response = new JSONObject(i.getStringExtra("playerinfo"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(response);
        try {
			response = new JSONObject(getIntent().getStringExtra("playerinfo"));
			playerName.setText(response.getString("id"));
			if(response.getBoolean("dead")){
				playerStatus.setText("Dead");
			} else{
				playerStatus.setText("Alive");
			}
	        if(response.getBoolean("werewolf")){
	        	playerButton.setText("Kill");
	        	l = new KillListener(username, password, response.getString("id"));
	        	playerButton.setOnClickListener(l);
	        } else {
	        	playerButton.setText("Vote");
	        	playerButton.setOnClickListener(l);
	        }
//			response = new JSONObject(getIntent().getStringExtra))
		} catch (JSONException e) {
			e.printStackTrace();
		}

//        // displaying selected product name
        
         
    }
	
	public class KillListener implements OnClickListener {
		String username;
		String password;
		String victim;
		
		
		
		KillListener(String username, String password, String victim){
			this.username = username;
			this.password = password;
			this.victim = victim;
		}
		
		@Override
		public void onClick(View v) {
			if(clicked){
				clicked = true;
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    			try {
					pairs.add(new BasicNameValuePair("victim", response.getString("id")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DownloadWebPageTask task = new DownloadWebPageTask(true, username, password, pairs, true);
				task.execute(new String[] { c.kill() });
			}
			
			
		}

	}
}