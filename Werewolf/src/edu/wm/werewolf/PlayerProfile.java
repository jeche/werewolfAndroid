package edu.wm.werewolf;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PlayerProfile extends Activity {
    JSONObject response;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_playerprofile);
         
        TextView txtProduct = (TextView) findViewById(R.id.playername_label);
         
        Intent i = getIntent();
        // getting attached intent data
        try {
			response = new JSONObject(i.getStringExtra("playerinfo"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(response);
//        try {
//			response = new JSONObject(getIntent().getStringExtra("playerList"));
//			txtProduct.setText(response.getString("id"));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//        // displaying selected product name
        
         
    }
}