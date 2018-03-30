package com.dynamite.heaterrc;
/* 
ConfigActivity.java

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ConfigActivity extends commonActivity {
    /** Called when the activity is first created. */
	// private static final String DEBUG_TAG = "ConfigActivity"
	// Restore preferences
	SharedPreferences.OnSharedPreferenceChangeListener listener;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        
        // Log.d(DEBUG_TAG, "onCreate has been called");

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor SPeditor = settings.edit();
        
        // Button initialization
        Button exitBtn;
        exitBtn = (Button)findViewById(R.id.exitbutton);
        Button saveBtn;
        saveBtn = (Button)findViewById(R.id.savebutton);
        final ToggleButton smsFeedback = (ToggleButton)findViewById(R.id.SMSfeedbackbutton);
        smsFeedback.setChecked(settings.getBoolean(getString(R.string.sp_smsFeedback), true));
        final CheckBox smsSendWarning = (CheckBox)findViewById(R.id.ShowSMSwarning);
        smsSendWarning.setChecked(settings.getBoolean(getString(R.string.sp_sendSMSwarning), false));
        smsSendWarning.setVisibility(View.INVISIBLE); // make this option invisible for the time being.
        final CheckBox GPStracker = (CheckBox)findViewById(R.id.EnableGPStracker);
        GPStracker.setChecked(settings.getBoolean(getString(R.string.sp_GPStrackerTab), false));
        GPStracker.setVisibility(View.VISIBLE); // make this option invisible for the time being (0xFF=invisible).
        

        // initialize EditText
        final EditText tPhoneNumber;
        tPhoneNumber = (EditText)findViewById(R.id.phonenumber);
        tPhoneNumber.setText(settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber)));
        final EditText tStartCmd;
        tStartCmd = (EditText)findViewById(R.id.startcmd);
        tStartCmd.setText(settings.getString(getString(R.string.sp_startCmd), getString(R.string.cfg_startcmd)));
        final EditText tStopCmd;
        tStopCmd = (EditText)findViewById(R.id.stopcmd);
        tStopCmd.setText(settings.getString(getString(R.string.sp_stopCmd), getString(R.string.cfg_stopcmd)));
        final EditText tTempCmd;
        tTempCmd = (EditText)findViewById(R.id.tempcmd);
        tTempCmd.setText(settings.getString(getString(R.string.sp_tempCmd), getString(R.string.cfg_tempcmd)));
        final EditText tSummerCmd = (EditText)findViewById(R.id.summercmd);
        tSummerCmd.setText(settings.getString(getString(R.string.sp_summerCmd), getString(R.string.cfg_summercmd)));
        final EditText tWinterCmd = (EditText)findViewById(R.id.wintercmd);
        tWinterCmd.setText(settings.getString(getString(R.string.sp_winterCmd), getString(R.string.cfg_wintercmd)));
        final EditText tStatusCmd = (EditText)findViewById(R.id.statuscmd);
        tStatusCmd.setText(settings.getString(getString(R.string.sp_statusCmd), getString(R.string.cfg_statuscmd)));
        final EditText tHeatDurationCmd = (EditText)findViewById(R.id.heatdurationcmd);
        tHeatDurationCmd.setText(settings.getString(getString(R.string.sp_heatDurationCmd), getString(R.string.cfg_heattimercmd)));
        final EditText tHeatDuration = (EditText)findViewById(R.id.heatduration);
        tHeatDuration.setText(settings.getString(getString(R.string.sp_heatDuration), getString(R.string.cfg_heatduration)));
        final EditText tSMSfeedbackCmd = (EditText)findViewById(R.id.SMSfeedbackcmd);
        tSMSfeedbackCmd.setText(settings.getString(getString(R.string.sp_smsFeedbackCmd), getString(R.string.cfg_SMSfeedbackcmd)));
        final EditText tmaxSMScount = (EditText)findViewById(R.id.maxSMScount);
        tmaxSMScount.setText(settings.getString(getString(R.string.sp_maxSMScount), getString(R.string.cfg_maxSMScount)));
        final EditText tPrepaidCredit = (EditText)findViewById(R.id.prepaidCredit);
        tPrepaidCredit.setText(settings.getString(getString(R.string.sp_prepaidCredit), getString(R.string.cfg_prepaidCredit)));
        final EditText tSMScost = (EditText)findViewById(R.id.SMScost);
        tSMScost.setText(settings.getString(getString(R.string.sp_SMScost), getString(R.string.cfg_SMScost)));
        
     // check actual configuration and enable buttons
        if (isPhoneNumberCorrect(settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber)))){
        	// Log.d(DEBUG_TAG, "Destination number should be ok, enable buttons.");
        	
        	SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), true);
        	SPeditor.commit();
        }
        
     // set click listener on the exitBtn 
        exitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Log.d(DEBUG_TAG, "Exit Button pressed");
                System.exit(0);
            }
            });
        
        // set click listener on the saveBtn 
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Log.d(DEBUG_TAG, "Save Button pressed");
            	 // We need an Editor object to make preference changes.
                // All objects are from android.context.Context
            	//TODO Remove saveBtn and save the changes automatically
            	// Compare and change the values only if different
                if (settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber)).compareTo(tPhoneNumber.getText().toString()) != 0){
                	// Log.d(DEBUG_TAG, "dest_numb not match");
                	if (isPhoneNumberCorrect(tPhoneNumber.getText().toString()))
                		SPeditor.putString(getString(R.string.sp_destNumb), tPhoneNumber.getText().toString());
                	else
                		showMsgPopUp(getString(R.string.com_warning), getString(R.string.cfg_checknumbertext)); 
                }
                if (settings.getString(getString(R.string.sp_startCmd), getString(R.string.cfg_startcmd)).compareTo(tStartCmd.getText().toString()) != 0){
            		// Log.d(DEBUG_TAG, "start_cmd not match");
            		SPeditor.putString(getString(R.string.sp_startCmd), tStartCmd.getText().toString());
            	}
                if (settings.getString(getString(R.string.sp_stopCmd), getString(R.string.cfg_stopcmd)).compareTo(tStopCmd.getText().toString()) != 0){
                	// Log.d(DEBUG_TAG, "stop_cmd not match");
                	SPeditor.putString(getString(R.string.sp_stopCmd), tStopCmd.getText().toString());
                }
                if (settings.getString(getString(R.string.sp_tempCmd), getString(R.string.cfg_tempcmd)).compareTo(tTempCmd.getText().toString()) != 0){
                	// Log.d(DEBUG_TAG, "temp_cmd not match");
                	SPeditor.putString(getString(R.string.sp_tempCmd), tTempCmd.getText().toString());
                }
                if (settings.getString(getString(R.string.sp_summerCmd), getString(R.string.cfg_summercmd)).compareTo(tSummerCmd.getText().toString()) != 0){
                	SPeditor.putString(getString(R.string.sp_summerCmd), tSummerCmd.getText().toString());
                }
                if (settings.getString(getString(R.string.sp_winterCmd), getString(R.string.cfg_wintercmd)).compareTo(tWinterCmd.getText().toString()) != 0){
                	SPeditor.putString(getString(R.string.sp_winterCmd), tWinterCmd.getText().toString());
                }
                if (settings.getString(getString(R.string.sp_statusCmd), getString(R.string.cfg_statuscmd)).compareTo(tStatusCmd.getText().toString()) != 0){
                	SPeditor.putString(getString(R.string.sp_statusCmd), tStatusCmd.getText().toString());
                }
                if (settings.getString(getString(R.string.sp_heatDurationCmd), getString(R.string.cfg_heattimercmd)).compareTo(tHeatDurationCmd.getText().toString()) != 0){
                	SPeditor.putString(getString(R.string.sp_heatDurationCmd), tHeatDurationCmd.getText().toString());
                }
                if (settings.getString(getString(R.string.sp_smsFeedbackCmd), getString(R.string.cfg_SMSfeedbackcmd)).compareTo(tSMSfeedbackCmd.getText().toString()) != 0){
                	SPeditor.putString(getString(R.string.sp_smsFeedbackCmd), tSMSfeedbackCmd.getText().toString());
                }
                
                if (settings.getString(getString(R.string.sp_prepaidCredit), getString(R.string.cfg_prepaidCredit)).compareTo(tPrepaidCredit.getText().toString()) != 0){
                	String newCredit = tPrepaidCredit.getText().toString();
                	float myNum = 0;

                	try {
                	    myNum = Float.parseFloat(newCredit);
                	} catch(NumberFormatException nfe) {
                		// Log.w(DEBUG_TAG, "NumberFormatException: " + nfe.getMessage());
        	    		nfe.printStackTrace();
        	    		myNum = 0;
                	}
                	if ((myNum > 0)&&(myNum < Float.MAX_VALUE)){
                		SPeditor.putString(getString(R.string.sp_prepaidCredit), newCredit);
                	}else{
                		tSMScost.setText(settings.getString(getString(R.string.sp_prepaidCredit), getString(R.string.cfg_prepaidCredit)));
                	}
                }
                
                if (settings.getString(getString(R.string.sp_SMScost), getString(R.string.cfg_SMScost)).compareTo(tSMScost.getText().toString()) != 0){
                	String newSMScost = tSMScost.getText().toString();
                	float myNum = 0;

                	try {
                	    myNum = Float.parseFloat(newSMScost);
                	} catch(NumberFormatException nfe) {
                		// Log.w(DEBUG_TAG, "NumberFormatException: " + nfe.getMessage());
        	    		nfe.printStackTrace();
        	    		myNum = 0;
                	}
                	if ((myNum > 0)&&(myNum < Float.MAX_VALUE)){
                		SPeditor.putString(getString(R.string.sp_SMScost), newSMScost);
                	}else{
                		tSMScost.setText(settings.getString(getString(R.string.sp_SMScost), getString(R.string.cfg_SMScost)));
                	}
                }
                
                if (settings.getString(getString(R.string.sp_maxSMScount), getString(R.string.cfg_maxSMScount)).compareTo(tmaxSMScount.getText().toString()) != 0){
                	String newMaxSMScount = tmaxSMScount.getText().toString();
                	int iSMScount = 0;
                	
                	try {
                		iSMScount = Integer.parseInt(newMaxSMScount);
                	} catch(NumberFormatException nfe) {
                		// Log.w(DEBUG_TAG, "NumberFormatException: " + nfe.getMessage());
        	    		nfe.printStackTrace();
        	    		iSMScount = 0;
                	}
                	
                	if ((iSMScount > 0) && (iSMScount < 16000) && (newMaxSMScount.length() <= 5)){
                		SPeditor.putString(getString(R.string.sp_maxSMScount), newMaxSMScount);
                	} else {
                		tmaxSMScount.setText(settings.getString(getString(R.string.sp_maxSMScount), getString(R.string.cfg_maxSMScount)));
                	}
                }
                
                if (settings.getString(getString(R.string.sp_heatDuration), getString(R.string.cfg_heatduration)).compareTo(tHeatDuration.getText().toString()) != 0){
                	String new_duration = tHeatDuration.getText().toString();
                	int myNum = 0;

                	try {
                	    myNum = Integer.parseInt(new_duration);
                	} catch(NumberFormatException nfe) {
                		// Log.w(DEBUG_TAG, "NumberFormatException: " + nfe.getMessage());
        	    		nfe.printStackTrace();
        	    		myNum = 0;
                	}
                	
                	if ((myNum > 0) && (myNum < 999) && (new_duration.length() <= 3)){
                		SPeditor.putInt(getString(R.string.sp_heatDurationresult), 0);
                		SPeditor.putString(getString(R.string.sp_heatDuration), new_duration);
                		showPopupPlus(getString(R.string.com_warning), getString(R.string.com_SMSsendwarning), getString(R.string.com_cancelbutton), getString(R.string.com_pos_btn), getString(R.string.sp_heatDurationresult));
                	} else	// if value is out of range, load stored prefs value
                		tHeatDuration.setText(settings.getString(getString(R.string.sp_heatDuration), getString(R.string.cfg_heatduration)));
                }
                // Commit the edits!
                SPeditor.commit();  
                
                Toast toast = Toast.makeText(getBaseContext(),
        				getString(R.string.cfg_configsaved), Toast.LENGTH_SHORT);
        				toast.show();
            }
            });
        
     // set click listener on the smsFeedback toggle 
        smsFeedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Log.d(DEBUG_TAG, "smsFeedback Button pressed");
            	// Reset the result code to zero
            	SPeditor.putInt(getString(R.string.sp_SMSfeedbackresult), 0).commit();
            	showPopupPlus(getString(R.string.com_warning), getString(R.string.com_SMSsendwarning), getString(R.string.com_cancelbutton), getString(R.string.com_pos_btn), getString(R.string.sp_SMSfeedbackresult));
            }
            });
     
        smsSendWarning.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
            	// Log.d(DEBUG_TAG, "smsSendWarning Button pressed");
				SPeditor.putBoolean(getString(R.string.sp_sendSMSwarning), smsSendWarning.isChecked()).commit();
			}
		});
        
        GPStracker.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Log.d(DEBUG_TAG, "GPStracker Checkbox Button pressed");
				SPeditor.putBoolean(getString(R.string.sp_GPStrackerTab), GPStracker.isChecked()).commit();
				showMsgPopUp(getString(R.string.com_warning), getString(R.string.com_Restartwarning)); 
			}
		});
        
     // Use instance field for listener 
     // It will not be gc'd as long as this instance is kept referenced
     listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
       public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
    	   // Log.d(DEBUG_TAG, "Change on shared preference: "+key); 
			
	        if(key.compareTo(getString(R.string.sp_destNumb)) == 0){
	        	SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), isPhoneNumberCorrect(settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber))));
	        	SPeditor.commit(); 	
	        }
	        if(key.compareTo(getString(R.string.sp_receivedSMSfb)) == 0){
	        	boolean b_receivedSMS = settings.getBoolean(getString(R.string.sp_receivedSMSfb), false);
	        	if (b_receivedSMS){
	        		//smsFeedback.setChecked(true);
	        		SPeditor.putBoolean(getString(R.string.sp_smsFeedback), true);
	        		SPeditor.putBoolean(getString(R.string.sp_receivedSMSfb), false);
	        		SPeditor.commit();
	        	}
	        }
	        if(key.compareTo(getString(R.string.sp_SMSfeedbackresult)) == 0){
	        	int result = settings.getInt(getString(R.string.sp_SMSfeedbackresult), 0);
	        	boolean b_smsFeedback = settings.getBoolean(getString(R.string.sp_smsFeedback), true);
	        	switch (result){
	        	case -1:  // negative result code, do not send SMS
	        		smsFeedback.setChecked(b_smsFeedback);
	        		break;    	
	        	case 1:  // positive result code, send SMS
	        		SPeditor.putBoolean(getString(R.string.sp_smsFeedback), !b_smsFeedback).commit();
	        		sendSMSfeedbackMsg(settings.getString(getString(R.string.sp_smsFeedbackCmd), getString(R.string.cfg_SMSfeedbackcmd)), !b_smsFeedback);
	        		break;
	        	case 0:
	        	default:
	        			// nothing to be done
	        	}
	        }
	        if(key.compareTo(getString(R.string.sp_heatDurationresult)) == 0){
	        	String old_duration = settings.getString(getString(R.string.sp_heatDuration), getString(R.string.cfg_heatduration));
	        	String new_duration = tHeatDuration.getText().toString();
	        	int result = settings.getInt(getString(R.string.sp_heatDurationresult), 0);
	        	switch (result){
	        	case -1:  // negative result code, do not send SMS
	        		tHeatDuration.setText(old_duration);
	        		break;
	        	case 1:
	        		if(new_duration.length() < 2)
	                	new_duration = "0" + new_duration;
	        		if(new_duration.length() < 3)
	                	new_duration = "0" + new_duration;
	        		SPeditor.putString(getString(R.string.sp_heatDuration), new_duration).commit();
	        		sendSMS(settings.getString(getString(R.string.sp_heatDurationCmd), getString(R.string.cfg_heattimercmd)) + new_duration);
	        		break;
	        	case 0:
	        		default:
	        		// nothing to be done
	        	}
	        }
	     // reload values for textviews
	        tPhoneNumber.setText(settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber)));
	        tStartCmd.setText(settings.getString(getString(R.string.sp_startCmd), getString(R.string.cfg_startcmd)));
	        tStopCmd.setText(settings.getString(getString(R.string.sp_stopCmd), getString(R.string.cfg_stopcmd)));
	        tTempCmd.setText(settings.getString(getString(R.string.sp_tempCmd), getString(R.string.cfg_tempcmd)));
	        tSummerCmd.setText(settings.getString(getString(R.string.sp_summerCmd), getString(R.string.cfg_summercmd)));
	        tWinterCmd.setText(settings.getString(getString(R.string.sp_winterCmd), getString(R.string.cfg_wintercmd)));
	        tStatusCmd.setText(settings.getString(getString(R.string.sp_statusCmd), getString(R.string.cfg_statuscmd)));
	        tHeatDurationCmd.setText(settings.getString(getString(R.string.sp_heatDurationCmd), getString(R.string.cfg_heattimercmd)));
	        tHeatDuration.setText(settings.getString(getString(R.string.sp_heatDuration), getString(R.string.cfg_heatduration)));
	        tSMSfeedbackCmd.setText(settings.getString(getString(R.string.sp_smsFeedbackCmd), getString(R.string.cfg_SMSfeedbackcmd)));
	        tmaxSMScount.setText(settings.getString(getString(R.string.sp_maxSMScount), getString(R.string.cfg_maxSMScount)));
	        tPrepaidCredit.setText(settings.getString(getString(R.string.sp_prepaidCredit), getString(R.string.cfg_prepaidCredit)));
	        tSMScost.setText(settings.getString(getString(R.string.sp_SMScost), getString(R.string.cfg_SMScost)));
	        smsFeedback.setChecked(settings.getBoolean(getString(R.string.sp_smsFeedback), true));
    		smsFeedback.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false));
    		GPStracker.setChecked(settings.getBoolean(getString(R.string.sp_GPStrackerTab), false));
       }
     };        
     settings.registerOnSharedPreferenceChangeListener(listener);
    }
	
	public void onResume(Bundle savedInstaceState){
    	// nothing to do
    }
	
	public void sendSMSfeedbackMsg(String msgtxt, boolean response){
		String s_msg_text = msgtxt;
		if (response){
			// response ON
			s_msg_text = s_msg_text + "ON";
		} else {
			// response OFF
			s_msg_text = s_msg_text + "OFF";
		}
		sendSMS(s_msg_text);
	}
		
	
}

