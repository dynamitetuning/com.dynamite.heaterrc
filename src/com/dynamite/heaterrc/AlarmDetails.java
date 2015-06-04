package com.dynamite.heaterrc;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
 
public class AlarmDetails extends commonActivity {
	public static final String PREFS_NAME = "MyPrefsFile";
	TextView tLastAlarm;
	TextView tNextAlarm;
	Button bExit;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmdetails);  
        
      //---look up the notification manager service---
        NotificationManager nm = (NotificationManager) 
            getSystemService(NOTIFICATION_SERVICE);
 
        //---cancel the notification---
        nm.cancel(getIntent().getExtras().getInt("NotifID"));
        
        // Restore preferences
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 4);
        // final SharedPreferences.Editor SPeditor = settings.edit();
        
        // initialize TextViews
        tLastAlarm = (TextView)findViewById(R.id.tvalarmdetails);
        tNextAlarm = (TextView)findViewById(R.id.tvnextschedule);
        
        tLastAlarm.setText(settings.getString(getString(R.string.sp_lastAlarm), "-"));
        tNextAlarm.setText(settings.getString(getString(R.string.sp_nextAlarm), "-"));
        
        bExit = (Button)findViewById(R.id.btnExit);
        
        // set click listener on the exitBtn 
        bExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Log.d(DEBUG_TAG, "Exit Button pressed");
            	finish();
            }
            });
        
    }
    
    public void onResume(Bundle savedInstaceState){
    	// nothing to do
    }
}
