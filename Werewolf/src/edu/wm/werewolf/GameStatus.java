package edu.wm.werewolf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import edu.wm.werewolf.service.GameUpdateService;
import edu.wm.werewolf.web.Constants;
import edu.wm.werewolf.web.WebPageTask;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

public class GameStatus extends Activity{
	
	JSONObject response;
	JSONArray responseArray;
	boolean isNight;
	String username;
	String password;
	TextView currStats;
	List<String>changedD;
	int col;
	int col2;
	long color = 0;
	int deadCount = 0;
	int aliveCount = 0;
	boolean isDead;
	int n;
	int d;
	private final static String TAG = "GameStatus";
	private class DownloadWebPageTask extends WebPageTask{

	    public DownloadWebPageTask(boolean hasPairs, String username,
				String password, List<NameValuePair> pairs, boolean isPost) {
			super(hasPairs, username, password, pairs, isPost);
		}

		@Override
	    protected void onPostExecute(String result) {
//	    	clicked = false;
//	    	Log.v(TAG, "Post executed");
//	    	Log.v(TAG, "RESULT VAL:" + result);
//	    	Intent intent = new Intent(getApplicationContext(), PlayerList.class);
//	    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	    	intent.putExtra("playerList", result);
//	    	intent.putExtra(c.isWerewolf(), isWerewolf);
//	    	intent.putExtra(c.createTime(), created);
//	    	intent.putExtra(c.nightFreq(), freq);
//	    	intent.putExtra("username", username);
//	    	intent.putExtra("password", password);
//	    	startActivity(intent);
	    	if(clicked){
	    		clicked = false;
	    		Log.v(TAG, "Post executed");
	    		Log.v(TAG, "RESULT VAL:" + result);
				Intent i = new Intent(getApplicationContext(), PlayerProfile.class);
	    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		i.putExtra("playerinfo", result);
	    		i.putExtra(c.isWerewolf(), isWerewolf);
	    		i.putExtra(c.isDead(), isDead);
	    		i.putExtra(c.createTime(), created);
	    		i.putExtra(c.nightFreq(), freq);
	    		i.putExtra("username", username);
	    		i.putExtra("password", password);
//	    		intent.putExtra("isWerewolf", isWerewolf);
	    		startActivity(i);
	    	}
	    	else{
	    		try {
					JSONObject resp = new JSONObject(result);
					response = resp;
//					isDead = resp.getBoolean(c.isDead());
					isWerewolf = resp.getBoolean(c.isWerewolf());
					JSONArray respAr = resp.getJSONArray("players");
					updateListInfo(resp.toString());
					if(isDead){
						v.vibrate(200);
						Thread.currentThread().sleep(200);
						v.vibrate(200);
//						Toast.makeText(context, text, duration)
					}

				if(wolves != response.getInt("numWolf") || peeps != response.getInt("numPeep")){
					wolves = response.getInt("numWolf");
					peeps = response.getInt("numPeep");
				}
				updateProgressBars();
				updateListInfo(resp.toString());
				if(isDead){
					v.vibrate(200);
					Thread.currentThread().sleep(200);
					v.vibrate(200);
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	  }
	
	private boolean clicked = false;
	private Constants c = new Constants();
	private ViewFlipper flippy;
	private TextView timerValue;
	private TextView playerStatus;
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	boolean isWerewolf;
	long created = 0L;
	long freq = 0L;
	View me;
	private ArrayList<String> scentList;
	private ArrayList<String> deadList;
	private ArrayList<String> killList;
	private ListView list;
	private Vibrator v;
	private ProgressBar wolfProgress;
	private int wolves;
	private int peeps;
	private ImageView image;
	private ProgressBar lifeProgress;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gamestatus);
		currStats = (TextView) findViewById(R.id.textView5);
		// Get instance of Vibrator from current Context
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// Vibrate for 400 milliseconds
		
		flippy = (ViewFlipper) findViewById(R.id.flippy);
		flippy.showNext();
		n = getResources().getColor(R.color.night);
		d = getResources().getColor(R.color.day);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);
		
		Intent intent = new Intent(this, GameUpdateService.class);
		me = findViewById(R.id.flippy);
		username = getIntent().getExtras().getString("username");
		intent.putExtra("username", username);
		password = getIntent().getExtras().getString("password");
		intent.putExtra("password", password);
		playerStatus = (TextView) findViewById(R.id.player_status);
		playerStatus.setTextColor(Color.WHITE);
		PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0); // Used for background service
		isWerewolf = getIntent().getExtras().getBoolean(c.isWerewolf());
		isNight = getIntent().getExtras().getString(c.getGameStatus()).contains("true");
		if(isNight){
			col = R.color.night;
			col2 = R.color.day;
		}else{
			col = R.color.day;
			col2 = R.color.night;
		}
		created = getIntent().getExtras().getLong(c.createTime());
		freq = getIntent().getExtras().getLong(c.nightFreq());
		timerValue = (TextView) findViewById(R.id.timerValue);
		if(getIntent().getExtras().getString(c.getGameStatus()).equals("isOver")){
			timerValue.setText("No game currently running.");
			timerValue.setTextSize(20);
		}
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    60*1000, pintent);
		Intent serviceIntent = new Intent(getBaseContext(), GameUpdateService.class);
		serviceIntent.putExtra("username", getIntent().getExtras().getString("username"));
		serviceIntent.putExtra("password", getIntent().getExtras().getString("password"));
//		startService(serviceIntent);
		customHandler.postDelayed(updateTimerThread, 0);
		customHandler.postDelayed(updateGameStatus, 30000);
		
		list = (ListView) findViewById(R.id.listView1);
		scentList = new ArrayList<String>();
		killList = new ArrayList<String>();
		deadList = new ArrayList<String>();
		wolfProgress = (ProgressBar) findViewById(R.id.balance_bar);
		lifeProgress = (ProgressBar) findViewById(R.id.life_bar);
		try {
			response = new JSONObject(getIntent().getExtras().getString((c.allPlayers())));
			
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
//		progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
		wolves = 0;
		try {
			wolves = response.getInt("numWolf");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		peeps = 0;
		try {
			peeps = response.getInt("numPeep");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        image = (ImageView) findViewById(R.id.imageView1);
        updateListInfo(getIntent().getExtras().getString((c.allPlayers())));

	}
	private Runnable updateGameStatus = new Runnable(){

		@Override
		public void run() {
			DownloadWebPageTask task = new DownloadWebPageTask(false, username, password, null, false);
			task.execute(new String[] {c.statusURL()});
		}
		
	};
	private Runnable updateTimerThread = new Runnable() {

		public void run() {
//			long color;
			long newCol;
			int change;
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			newCol = timeInMilliseconds;
			long color2;
			color2 = newCol;
			timeInMilliseconds = timeInMilliseconds % freq;
			timeInMilliseconds = freq - timeInMilliseconds;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			newCol = newCol % freq;
			newCol = freq - newCol;
			
			newCol = (newCol * 100) / freq;
			if(newCol < 15 && newCol > 0 && newCol != color){
//				Log.v("color", newCol + " Color fade" + color);
				int red = Color.red(col);
				int red2 = Color.red(col2);
				int blue = Color.blue(col);
				int blue2 = Color.blue(col2);
				int green = Color.green(col);
				int green2 = Color.green(col2);
				color = newCol;
				int newRed;
				int newGreen;
				int newBlue;
				newCol = 15 - newCol;
				if(col == d){
					 newRed = (int) (red - 51 * newCol / 15);
					if(newRed < 0){
						newRed = 0;
					}
					newGreen = (int) (green - 25 * newCol / 15);
					if(newGreen < 25){
						newGreen = 25;
					}
					if(newGreen <= 102){
						newBlue = (int) (blue - 51 * newCol / 15);
						if(newBlue < 51){
							newBlue = 51;
						}
					}else{
						newBlue = 255;
					}
				}else{
					newGreen = (int) (green + 25 * newCol / 15);
					if(newGreen > 178){
						newGreen = 178;
					}
					newBlue = (int) (blue + 51 * newCol / 15);
					if(newBlue > 255 || newGreen >= 128){
						newBlue = 255;
					}
					if(newGreen >= 153){
						newRed = (int) (red + 51 * newCol / 15);
						if(newRed > 102){
							newRed = 102;
						}
					}else{
						newRed = 0;
					}
				}
				int goTo;
				float[] hsv = new float[3];
				Color.RGBToHSV(red, green, blue, hsv );
//				newCol = 15 - newCol;
				goTo = Color.rgb(newRed, newGreen, newBlue);
//				col = goTo;
//				goTo = Color.rgb((int) (red + color * (red - red2) / 15), green + (int)color *(green -green2)/15, blue + (int)color *(blue - blue2)/15);
				
//				Color.red();
//				col = col + change / 100;
				me.setBackgroundColor(goTo);
			} else if((color2 / freq) % 2 == 0 && col != n){
				me.setBackgroundColor(n);
				Log.v("color", color2 + " night");
				col = n;
				col2 = d;
			} else if(col != d && (color2 / freq) % 2 == 1){
				me.setBackgroundColor(d);
				Log.v("color", color2 + " day");
				col = d;
				col2 = n;
			}
			
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			if(secs == 0 && mins == 0){
				v.vibrate(400);
			}
			timerValue.setText("" + mins + ":"
					+ String.format("%02d", secs)); // + ":"
//					+ String.format("%03d", milliseconds));
			customHandler.postDelayed(this, 0);
		}
		};
	private float initialX = 0f;
	
	
	@Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
        case MotionEvent.ACTION_DOWN:
            initialX = touchevent.getX();
            break;
        case MotionEvent.ACTION_UP:
            float finalX = touchevent.getX();
            if (initialX  > finalX) {
                if (flippy.getDisplayedChild() == 2)
                    break;
 
                flippy.setInAnimation(this, R.anim.in_right);
                flippy.setOutAnimation(this, R.anim.out_left);
 
                flippy.showNext();
            } else {
                if (flippy.getDisplayedChild() == 0)
                    break;
 
                flippy.setInAnimation(this, R.anim.in_left);
                flippy.setOutAnimation(this, R.anim.out_right);
 
                flippy.showPrevious();
            }
            break;
        }
        return false;
    }

	public void updateListInfo(String vals){
		String[] stringarray = null;
		ArrayList<String>tempListS = scentList;
		ArrayList<String>tempListK = killList;
		ArrayList<String>tempListD = deadList;
		changedD = new ArrayList<String>();
		deadList =  new ArrayList<String>();
		scentList = new ArrayList<String>();
		killList = new ArrayList<String>();
		boolean changed = false;
		boolean dchange = false;
		boolean killChange = false;
		try {
			JSONObject response = new JSONObject(vals);
			JSONArray array = response.getJSONArray("players");
			responseArray = array;
			Log.w(TAG, response.toString());
			JSONObject obj;
			boolean isDead;
			int score = 0;	
			deadCount = 0;
			aliveCount = 0;
			stringarray = new String[array.length() - 1];
			List<String> stringList = new ArrayList<String>();
	        for (int i = 0; i < array.length(); i++) {
	            obj = (JSONObject) array.get(i);
	            isDead = obj.getBoolean(c.isDead());
	            if(isDead){
	            	if(tempListD.contains(obj.getString("id"))){
	            		dchange = true;
	            		changedD.add(obj.getString("id"));
	            	}
	            	deadList.add(obj.getString("id"));
	            	deadCount++;
	            }else{
	            	aliveCount++;
	            }
	            if(isWerewolf){
	            	score = obj.getInt("score");
	            }
	            if(!obj.getString("id").equals(username)){
	            	System.out.println(obj.getString("id") + " username: " +  username);
	            	stringList.add(obj.getString("id"));
	            	if(isWerewolf && score > 0){
	            		if(score == 1){
	            			if(!tempListS.contains(obj.getString("id"))){
	            				changed = true;
	            			}
	            			scentList.add(obj.getString("id"));
	            		}else if(score == 2){
	            			if(!tempListK.contains(obj.getString("id"))){
	            				killChange = true;
	            			}
	            			killList.add(obj.getString("id"));
	            		}
	            	}
	            	
	            }else{
	            	this.isDead = obj.getBoolean(c.isDead());
	            	updateImg(obj);
	            	if(isWerewolf){
	            		updateKills(obj);
	            	}
	            	if(isDead){
//	            		setDeadUI(obj);
	            	}

	            }
	        	
	        }
	        for( int i = 0; i < stringList.size(); i++){
	        	stringarray[i] = stringList.get(i);
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray){
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
	            TextView textView = (TextView) super.getView(position, convertView, parent);
	            textView.setTextColor(Color.WHITE);
	            if(scentList.contains(textView.getText().toString())){
	            	textView.setTextColor(Color.YELLOW);
	            }
	            if(killList.contains(textView.getText().toString())){;
	            	textView.setTextColor(Color.RED);
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
        list.invalidate();
        if(changed || killChange){
        	if(killChange){
        		v.vibrate(1000);
        	}else{
        		v.vibrate(100);
        		v.vibrate(100);
        	}
        }
        if(changedD.size() > 0){
        	String newTest = "Most recent to die: ";
        	for(int i = 0; i < changedD.size(); i ++){
        		newTest = newTest + changedD.get(i);
        		if(i != changedD.size() - 1){
        			newTest = newTest + ", ";
        		}
        	}
        	currStats.setText(newTest);
        }else{
        	currStats.setText("No one has died recently.");
        }
        updateProgressBars();
	}
	private void updateKills(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}

	private void updateImg(JSONObject obj) {
		try {
			String img = obj.getString("imgString");
			if(isDead){
				image.setImageResource(R.drawable.gravestone);
				playerStatus.setText("Dead");
			}else if(isWerewolf && isNight){
		    	image.setImageResource(R.drawable.werewolf);
		    	playerStatus.setText("Alive and hungry");
			}else if(img.equals("M")){
			    image.setImageResource(R.drawable.male_villager);
			    playerStatus.setText("Alive");
			}else{
			    image.setImageResource(R.drawable.female_villager);
			    playerStatus.setText("Alive");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateProgressBars();
	}
	
	private void updateProgressBars() {
		int ptotal = aliveCount + deadCount;
		int apercent = aliveCount * 100 / ptotal;
		int total = wolves + peeps;
		int percent = peeps * 100 / total;
		if(percent < 50){
			wolfProgress.setBackgroundColor(Color.RED);
		}
		wolfProgress.setProgress(percent);
		if(apercent < 33){
			lifeProgress.setBackgroundColor(Color.RED);
		}else if(apercent < 66){
			lifeProgress.setBackgroundColor(Color.MAGENTA);
		}
		lifeProgress.setProgress(apercent);
		
	}
}
