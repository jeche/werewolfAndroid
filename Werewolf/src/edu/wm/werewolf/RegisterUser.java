package edu.wm.werewolf;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterUser extends Activity {
	Button registerButton;
	Button loginButton;
	
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
}
