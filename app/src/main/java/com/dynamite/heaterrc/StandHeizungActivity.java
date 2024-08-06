package com.dynamite.heaterrc;
/*
StandHeizungActivity.java

Copyright (C) 2024  dynamitetuning

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class StandHeizungActivity extends commonActivity {
    /** Called when the activity is first created.  */
	// private static final String DEBUG_TAG = "StandHeizungActivity";
	SharedPreferences.OnSharedPreferenceChangeListener listener;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.standheizung);

        // Log.d(DEBUG_TAG, "onCreate has been called");

    	 // Restore preferences
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor SPeditor = settings.edit();

        // Button initialization
        final Button exitBtn = findViewById(R.id.exitbutton);
        final Button startBtn = findViewById(R.id.startbutton);
        startBtn.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false));
        final Button stopBtn = findViewById(R.id.stopbutton);
        stopBtn.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false));
        final Button tempBtn = findViewById(R.id.tempbutton);
        tempBtn.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false));
        final Button summerBtn = findViewById(R.id.summerbutton);
        summerBtn.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false));
        final Button winterBtn = findViewById(R.id.winterbutton);
        winterBtn.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false));
        final Button statusBtn = findViewById(R.id.statusbutton);
        statusBtn.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false));
        final Button infoBtn = findViewById(R.id.helpbutton);

        // initialize ProgressBar
        final ProgressBar sendingPB;
        sendingPB = findViewById(R.id.mainPB);
        sendingPB.setVisibility(View.INVISIBLE);

     // set click listener on the exitBtn
        exitBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "Exit Button pressed");
            System.exit(0);
        });

     // set click listener on the startBtn
        startBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "Start Button pressed");
            SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false).apply();
            sendingPB.setVisibility(View.VISIBLE);

            String SMS_START_COMMAND = settings.getString(getString(R.string.sp_startCmd), getString(R.string.cfg_startcmd));
            sendSMS(SMS_START_COMMAND);
        });

        // set click listener on the stopBtn
        stopBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "Stop Button pressed");
            SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false).commit();
            sendingPB.setVisibility(View.VISIBLE);

            String SMS_STOP_COMMAND = settings.getString(getString(R.string.sp_stopCmd), getString(R.string.cfg_stopcmd));

            sendSMS(SMS_STOP_COMMAND);
        });

     // set click listener on the tempBtn
        tempBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "Temperature Button pressed");
            SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false).commit();
            sendingPB.setVisibility(View.VISIBLE);

            String SMS_TEMP_COMMAND = settings.getString(getString(R.string.sp_tempCmd), getString(R.string.cfg_tempcmd));

            sendSMS(SMS_TEMP_COMMAND);
        });

     // set click listener on the summerBtn
        summerBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "summer Button pressed");
            SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false).commit();
            sendingPB.setVisibility(View.VISIBLE);

            String SMS_SUMMER_COMMAND = settings.getString(getString(R.string.sp_summerCmd), getString(R.string.cfg_summercmd));

            sendSMS(SMS_SUMMER_COMMAND);
        });

     // set click listener on the winterBtn
        winterBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "winter Button pressed");
            SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false).commit();
            sendingPB.setVisibility(View.VISIBLE);

            String SMS_WINTER_COMMAND = settings.getString(getString(R.string.sp_winterCmd), getString(R.string.cfg_wintercmd));

            sendSMS(SMS_WINTER_COMMAND);
        });

     // set click listener on the statusBtn
        statusBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "status Button pressed");
            SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false).commit();
            sendingPB.setVisibility(View.VISIBLE);

            String SMS_STATUS_COMMAND = settings.getString(getString(R.string.sp_statusCmd), getString(R.string.cfg_statuscmd));

            sendSMS(SMS_STATUS_COMMAND);
        });

        // set click listener on the exitBtn
        infoBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "Info Button pressed");
            //showHelp();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.helpLink)));
            startActivity(browserIntent);
        });
 /*
     // Register broadcast receivers for SMS sent and delivered intents
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	if (isPhoneNumberCorrect(settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber)))){
		        	SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), true).commit();
		        }            }
        }, new IntentFilter(ACTION_SMS_SENT));
   */
     // Use instance field for listener
     // It will not be gc'd as long as this instance is kept referenced
        listener = (prefs, key) -> {
            // Log.d(DEBUG_TAG, "Change on shared preference: "+key);
            // reload values for textviews
            boolean SEND_BTN_ENABLED = settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false);
            if (key.compareTo(getString(R.string.sp_sendBtnEnabled))==0){
                // Log.d(DEBUG_TAG, "sendBtnEnable="+SEND_BTN_ENABLED);
                startBtn.setEnabled(SEND_BTN_ENABLED);
                stopBtn.setEnabled(SEND_BTN_ENABLED);
                tempBtn.setEnabled(SEND_BTN_ENABLED);
                summerBtn.setEnabled(SEND_BTN_ENABLED);
                winterBtn.setEnabled(SEND_BTN_ENABLED);
                statusBtn.setEnabled(SEND_BTN_ENABLED);
                if (SEND_BTN_ENABLED)
                    sendingPB.setVisibility(View.INVISIBLE);
            }
};
     settings.registerOnSharedPreferenceChangeListener(listener);

        String SMS_DEST_NUMBER = settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber));

        if(SMS_DEST_NUMBER.contentEquals("0")){
        	showHelp();
        	SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false);
        	SPeditor.commit();
        }

    }

	public void onResume(Bundle savedInstaceState){
		// nothing to do
	}

}

