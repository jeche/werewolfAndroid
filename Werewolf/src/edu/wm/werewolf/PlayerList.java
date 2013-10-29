package edu.wm.werewolf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlayerList extends Activity {
	ListView list;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playerlist);
		list = (ListView) findViewById(R.id.listView1);
		
		try {
			JSONArray array = new JSONArray(getIntent().getStringExtra("playerList"));
			JSONObject obj;
			boolean isDead;
			String[] stringarray = new String[array.length()];
	        for (int i = 0; i < array.length(); i++) {
	            obj = (JSONObject) array.get(i);
	            isDead = obj.getBoolean("dead");
	            if(!isDead){
	            	stringarray[i] = array.getString(i);
	            }
	        	
	        }
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray); 
	        list.setAdapter(adapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
