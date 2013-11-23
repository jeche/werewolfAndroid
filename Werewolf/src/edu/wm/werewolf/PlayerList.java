package edu.wm.werewolf;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wm.werewolf.web.Constants;
import edu.wm.werewolf.web.WebPageTask;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup;

public class PlayerList extends Activity {
	ListView list;
	String username;
	String password;
	boolean clicked = false;
	List<String> scentList;
	List<String> killList;
	Constants c = new Constants();

	
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
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playerlist);
		list = (ListView) findViewById(R.id.listView1);
		boolean isWerewolf = getIntent().getExtras().getBoolean(c.isWerewolf());
		username = getIntent().getExtras().getString("username");
		password = getIntent().getExtras().getString("password");
		scentList = new ArrayList<String>();
		killList = new ArrayList<String>();
		clicked = false;
		try {
			JSONArray array = new JSONArray(getIntent().getStringExtra("playerList"));
			JSONObject obj;
			boolean isDead;
			int score = 0;
			String[] stringarray = new String[array.length() - 1];
			List<String> stringList = new ArrayList<String>();
	        for (int i = 0; i < array.length(); i++) {
	            obj = (JSONObject) array.get(i);
	            isDead = obj.getBoolean(c.isDead());
	            if(isWerewolf){
	            	score = obj.getInt("score");
	            }
	            
	            if(!obj.getString("id").equals(username)){
	            	System.out.println(obj.getString("id") + " username: " +  username);
	            	stringList.add(obj.getString("id"));
	            	if(isWerewolf && score > 0){
	            		scentList.add(obj.getString("id"));
	            		killList.add(obj.getString("id"));
	            	}
	            	
	            }
	        	
	        }
	        for( int i = 0; i < stringList.size(); i++){
	        	stringarray[i] = stringList.get(i);
	        }
	        
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray){
	        	@Override
	        	public View getView(int position, View convertView, ViewGroup parent) {
		            TextView textView = (TextView) super.getView(position, convertView, parent);
		            if(scentList.contains(textView.getText().toString())){
		            	textView.setTextColor(getResources().getColor(R.color.cyan));
		            }
		            if(killList.contains(textView.getText().toString())){;
		            	textView.setTextColor(getResources().getColor(R.color.red));
		            }
		            return textView;
	        	}
	        }; 
	        list.setAdapter(adapter);
	        list.setOnItemClickListener(new OnItemClickListener() {
	        	@Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        		String player = ((TextView) view).getText().toString();
	                // selected item
	        		if(!clicked){
	        			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	        			pairs.add(new BasicNameValuePair("playername", player));
	        			DownloadWebPageTask task = new DownloadWebPageTask(false, username, password, pairs, false);
	        			task.execute(new String[] { c.getInfoURL() +"/" + player });
	        			clicked = true;
	        		}
	               
	            }

	          });
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
