package edu.wm.werewolf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import edu.wm.werewolf.constants.Constants;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText usernameText;
	private EditText passwordText;
	private EditText verifyPasswordText;
	private EditText firstNameText;
	private EditText lastNameText;
	private Button registerButton;
	private Constants c = new Constants();
	private String username;//do this for all
	
	
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
		
		if (savedInstanceState == null)
		{
			username = "";
		}
		else
		{
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
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("username", username);
	}
	
	public void myClickHandler(View view) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(c.getBaseUrl()+"addUser");
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("userName", usernameText.getText().toString()));
		pairs.add(new BasicNameValuePair("id", usernameText.getText().toString()));
		pairs.add(new BasicNameValuePair("firstName", firstNameText.getText().toString()));
		pairs.add(new BasicNameValuePair("lastName", lastNameText.getText().toString()));
		pairs.add(new BasicNameValuePair("hashedPassword", passwordText.getText().toString()));

		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(post);
			String jsonString = EntityUtils.toString(response.getEntity());
			System.out.println(jsonString);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
