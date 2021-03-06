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
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
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
//			System.out.println(username + " " + password);

			
	        try {
	        	if(isPost){
					HttpPost httpPost = new HttpPost(url);
			          if(hasPairs){
				          	httpPost.setEntity(new UrlEncodedFormEntity(pairs));
				          }
			          if(!username.equals("")){
			          httpPost.addHeader(BasicScheme.authenticate(
			        		  new UsernamePasswordCredentials(username, password),
			        		  "UTF-8", false));
			          }
			          execute = client.execute(httpPost);
				}
	        	else{
			          if(hasPairs){
		        		  if(!url.endsWith("/"))
		        		        url += "/";
//		        		  pairs.g
//			        	  String paramString = URLEncodedUtils.format(pairs, "utf-8");
//			        	  url = url + paramString;
	
//			        	  uril = new URIBuilder(httpPost.getURI()).addParameter("q",
//			        		        "That was easy!").build();

//			        		((HttpRequestBase) httpPost).setURI(uril);//			        	  HttpGet httpGet = new HttpGet(url);
//			        	  System.out.println(url);
				      }
	        		  HttpGet httpPost= new HttpGet(url);
//	        		  URI uril;
//	        		  if(!url.endsWith("?"))
//	        		        url += "?";
//			          if(hasPairs){
//
//			        	  String paramString = URLEncodedUtils.format(pairs, "utf-8");
//			        	  url = url + paramString;
//	
////			        	  uril = new URIBuilder(httpPost.getURI()).addParameter("q",
////			        		        "That was easy!").build();
//
////			        		((HttpRequestBase) httpPost).setURI(uril);//			        	  HttpGet httpGet = new HttpGet(url);
//				      }
			          
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
//	      Log.v(null, resp);
	      
	      return resp;
	}
	
    @Override
    protected void onPostExecute(String result) {}

}
