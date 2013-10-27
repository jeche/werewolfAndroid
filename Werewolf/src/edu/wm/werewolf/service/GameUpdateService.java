package edu.wm.werewolf.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GameUpdateService extends Service{

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
          Toast.makeText(getApplicationContext(), "Service Running ", 1).show();
          return super.onStartCommand(intent, flags, startId);
   }
}