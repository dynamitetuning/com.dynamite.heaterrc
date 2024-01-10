package com.dynamite.heaterrc;
/*
GPStrackerActivity.java

Copyright (C) 2015  dynamitetuning

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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class GPStrackerActivity extends commonActivity {

	/** Called when the activity is first created.  */
	SharedPreferences.OnSharedPreferenceChangeListener listener;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.gpstracker);

        // Log.d("GPStrackerActivity:", "onCreate has been called");

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor SPeditor = settings.edit();

     // Button initialization
        final Button GPSposReqBtn;
        GPSposReqBtn = findViewById(R.id.trackerreqbutton);
        GPSposReqBtn.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabledGPS), false));
        Button exitBtn;
        exitBtn = findViewById(R.id.exitbutton);
        Button helpBtn;
        helpBtn = findViewById(R.id.helpbutton);

     // initialize EditText
        final EditText tPhoneNumberGPS;
        tPhoneNumberGPS = findViewById(R.id.phonenumberGPS);
        tPhoneNumberGPS.setText(settings.getString(getString(R.string.sp_destNumbGPS), getString(R.string.cfg_phonenumber)));
        final EditText tPosReqCmd;
        tPosReqCmd = findViewById(R.id.PosReqCmd);
        tPosReqCmd.setText(settings.getString(getString(R.string.sp_GPSposReqCmd), getString(R.string.cfg_GPSposReqCmd)));

     // initialize ProgressBar
        final ProgressBar sendingPB;
        sendingPB = findViewById(R.id.GPStrackPB);
        sendingPB.setVisibility(View.INVISIBLE);

     // check actual configuration and enable buttons
        if (isPhoneNumberCorrect(settings.getString(getString(R.string.sp_destNumbGPS), getString(R.string.cfg_phonenumber)))){
        	// Log.d("GPStrackerActivity:", "Destination number should be ok, enable buttons.");
        	SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), true).apply();
        }

        tPhoneNumberGPS.setOnFocusChangeListener((v, hasFocus) -> {
            // Log.d("GPStrackerActivity:", "FocusChange event tPhoneNumberGPS:"+hasFocus);
            String phoneNumber = settings.getString(getString(R.string.sp_destNumbGPS), getString(R.string.cfg_phonenumber));
            if (phoneNumber.compareTo(tPhoneNumberGPS.getText().toString())!=0)
                SPeditor.putString(getString(R.string.sp_destNumbGPS), tPhoneNumberGPS.getText().toString()).commit();
            if (isPhoneNumberCorrect(settings.getString(getString(R.string.sp_destNumbGPS), getString(R.string.cfg_phonenumber)))){
                SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), true).commit();
            }
        });

        tPosReqCmd.setOnFocusChangeListener((v, hasFocus) -> {
            // Log.d("GPStrackerActivity:", "FocusChange event tPosReqCmd:"+hasFocus);
            String posReqCmd = settings.getString(getString(R.string.sp_GPSposReqCmd), getString(R.string.cfg_GPSposReqCmd));
            if (posReqCmd.compareTo(tPosReqCmd.getText().toString()) != 0)
                SPeditor.putString(getString(R.string.sp_GPSposReqCmd), tPosReqCmd.getText().toString()).commit();
        });

     // set click listener on the exitBtn
        GPSposReqBtn.setOnClickListener(v -> {
            // Log.d("GPStrackerActivity:", "GPSposReqBtn Button pressed");
            SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), false).commit();
            sendingPB.setVisibility(View.VISIBLE);

            String SMS_START_COMMAND = settings.getString(getString(R.string.sp_GPSposReqCmd), getString(R.string.cfg_GPSposReqCmd));
            String DEST_NUMB = settings.getString(getString(R.string.sp_destNumbGPS), getString(R.string.cfg_phonenumber));
            sendSMS2numb(DEST_NUMB, SMS_START_COMMAND, true);
        });

     // set click listener on the helpBtn
        helpBtn.setOnClickListener(v -> {
            // Log.d("StandHeizungActivity:", "help Button pressed");
            showHelpGPS();
        });

        // set click listener on the exitBtn
        exitBtn.setOnClickListener(v -> {
            // Log.d("GPStrackerActivity:", "Exit Button pressed");
            System.exit(0);
        });
        /*
     // Register broadcast receivers for SMS sent and delivered intents
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	if (isPhoneNumberCorrect(settings.getString(getString(R.string.sp_destNumbGPS), getString(R.string.cfg_phonenumber)))){
		        	SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), true).commit();
		        }
            }
        }, new IntentFilter(ACTION_SMS_SENT));
      */
     // Use instance field for listener
        // It will not be gc'd as long as this instance is kept referenced
        listener = (prefs, key) -> {
            // Log.d("GPStrackerActivity:", "Change on shared preference: "+key);
            // reload values for textviews
            boolean SEND_BTN_ENABLED = settings.getBoolean(getString(R.string.sp_sendBtnEnabledGPS), false);
            if (key.compareTo(getString(R.string.sp_sendBtnEnabledGPS))==0){
                // Log.d("GPStrackerActivity:", "sendBtnEnable="+SEND_BTN_ENABLED);
                GPSposReqBtn.setEnabled(SEND_BTN_ENABLED);
                if (SEND_BTN_ENABLED)
                    sendingPB.setVisibility(View.INVISIBLE);
            }
            tPhoneNumberGPS.setText(settings.getString(getString(R.string.sp_destNumbGPS), getString(R.string.cfg_phonenumber)));
            tPosReqCmd.setText(settings.getString(getString(R.string.sp_GPSposReqCmd), getString(R.string.cfg_GPSposReqCmd)));
        };
        settings.registerOnSharedPreferenceChangeListener(listener);

	}

	public void onResume(Bundle savedInstaceState){
    	// nothing to do
    }
}
