package edu.wm.werewolf.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import edu.wm.werewolf.GameStatus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class WebPageTask extends AsyncTask<String, Void, String>{
	
	List<NameValuePair> pairs;
	boolean hasPairs;
	String username = "admin";
	String password = "admin";
	boolean isPost;
	
	public WebPageTask(boolean hasPairs, String username, String password,List<NameValuePair> pairs, boolean isPost){
		this.hasPairs = hasPairs;
		this.username = username;
		this.password = password;
		if(hasPairs){
			this.pairs=pairs;
		}
		this.isPost = isPost;
	}

	@Override
	protected String doInBackground(String... urls) {
	      String resp = "";
	      HttpResponse execute;
	      for (String url : urls) {
	        DefaultHttpClient client = new DefaultHttpClient();
	        URI uri = null;
	        
			try {
				uri = new URI(url);
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
//			client.getCredentialsProvider().setCredentials(
//					new AuthScope(uri.getHost(), uri.getPort(),AuthScope.ANY_SCHEME),
//					new UsernamePasswordCredentials(username, password)); // CHANGE TO USERNAME/PASSWORD
			System.out.println(username + " " + password);

			
	        try {
	        	if(isPost){
					HttpPost httpPost = new HttpPost(url);
			          if(hasPairs){
				          	httpPost.setEntity(new UrlEncodedFormEntity(pairs));
				          }
			          httpPost.addHeader(BasicScheme.authenticate(
			        		  new UsernamePasswordCredentials(username, password),
			        		  "UTF-8", false));
			          execute = client.execute(httpPost);
				}
	        	else{
					HttpGet httpPost= new HttpGet(url);
			          if(hasPairs){
			        	  BasicHttpParams h = new BasicHttpParams();
			        	  for(int i = 0; i < pairs.size(); i++){
			        		  h.setParameter(pairs.get(i).getName(), pairs.get(i).getValue());
			        	  }
				          httpPost.setParams(h);
				      }
			          httpPost.addHeader(BasicScheme.authenticate(
			        		  new UsernamePasswordCredentials(username, password),
			        		  "UTF-8", false));
					execute = client.execute(httpPost);
				}

	          
	          InputStream content = execute.getEntity().getContent();

	          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	          String s = "";
	          while ((s = buffer.readLine()) != null) {
	            resp += s;
	          }

	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	      Log.v(null, resp);
	      
	      return resp;
	}
	
    @Override
    protected void onPostExecute(String result) {
//      Log.v(null, response);
//      
//      try {
//		if(response.getString("status").equals(c.success())){
//				Log.v(null, "going to pref");
//				Context context3 = getApplicationContext();
//				CharSequence text3 = "Successful Registration!";
//				int duration3 = Toast.LENGTH_SHORT;
//				Toast toast3 = Toast.makeText(context3, text3, duration3);
//				toast3.show();
//				Intent intent2 = new Intent(context3, GameStatus.class);
//				intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent2);
//		  }else{
//			  	Context context3 = getApplicationContext();
//				CharSequence text3 = "There was an error creating your account.";
//				int duration3 = Toast.LENGTH_SHORT;
//				Toast toast3 = Toast.makeText(context3, text3, duration3);
//				toast3.show();
//		  }
//	} catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
    }

}
