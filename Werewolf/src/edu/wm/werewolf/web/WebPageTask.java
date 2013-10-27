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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class WebPageTask extends AsyncTask<String, Void, String>{
	
	List<NameValuePair> pairs;
	boolean hasPairs;
	String username;
	String password;
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
	      for (String url : urls) {
	        DefaultHttpClient client = new DefaultHttpClient();
	        URI uri = null;
			try {
				uri = new URI(url);
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
	        HttpGet httpPost= new HttpGet(url);
//			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			client.getCredentialsProvider().setCredentials(
					new AuthScope(uri.getHost(), uri.getPort(),AuthScope.ANY_SCHEME),
					new UsernamePasswordCredentials("admin", "admin"));
	        try {
//	          httpPost.setEntity(new UrlEncodedFormEntity(pairs));
	          HttpResponse execute = client.execute(httpPost);
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

}
