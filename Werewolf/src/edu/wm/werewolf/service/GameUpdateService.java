package edu.wm.werewolf.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.wm.werewolf.PlayerProfile;
import edu.wm.werewolf.web.WebPageTask;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GameUpdateService extends Service {
	String username;
	String password;
	LocationManager locationManager;
	String provider;
	Location l;
	
	private class DownloadWebPageTask extends WebPageTask {

	    public DownloadWebPageTask(boolean hasPairs, String username,
				String password, List<NameValuePair> pairs, boolean isPost) {
			super(hasPairs, username, password, pairs, isPost);
		}

		@Override
	    protected void onPostExecute(String result) {
	    	
	    }
	  }

    @Override
   public IBinder onBind(Intent intent) {
          // TODO: Return the communication channel to the service.
          throw new UnsupportedOperationException("Not yet implemented");
   }

   @Override
   public void onCreate() {
          // TODO Auto-generated method stub
	   	  Log.v(null, "Service created!  Hurray!");
          Toast.makeText(getApplicationContext(), "Service Created", 1).show();
          locationManager = (LocationManager) 
        		  getSystemService(Context.LOCATION_SERVICE);
          super.onCreate();
   }

   @Override
   public void onDestroy() {
          // TODO Auto-generated method stub
	   	  Log.v(null, "Service murdered!  Hurray!");
          Toast.makeText(getApplicationContext(), "Service Destroy", 1).show();
          super.onDestroy();
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
          // TODO Auto-generated method stub
	      Log.v(null, "Run Service run!");
	      username = (String)intent.getExtras().get("username");
	      password = (String)intent.getExtras().get("password");
          Toast.makeText(getApplicationContext(), "Service Running ", 1).show();
//          Criteria c = new Criteria();
//          mCurrentLocation = mLocationClient.getLastLocation();
//          provider = locationManager.getBestProvider(c, false);
////          l = locationManager.getLastKnownLocation(provider);
//          double lng=l.getLongitude();
//          double lat=l.getLatitude();
//          System.out.println("Longitude: " + lng);
//          System.out.println("Latitude: " + lat);
//          List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//          pairs.add(new BasicNameValuePair("lng", lng+""));
//          pairs.add(new BasicNameValuePair("lat", lat+""));
//          DownloadWebPageTask task = new DownloadWebPageTask(true, username, password, pairs, true);
          return super.onStartCommand(intent, flags, startId);
   }
}
