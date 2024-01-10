package com.dynamite.heaterrc;
/*
commonActivity.java

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class commonActivity extends Activity{
    public static final String ACTION_SMS_SENT = "com.example.android.apis.os.SMS_SENT_ACTION";
	public static final String PREFS_NAME = "MyPrefsFile";
	public BroadcastReceiver bcr;
	// private static final String DEBUG_TAG = "commonActivity";

	// Methods
	public void onDestroy(){
		super.onDestroy();
		try {
			unregisterReceiver(bcr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getApplicationContext();
	        /*
	        try {
		        // Register broadcast receivers for SMS sent and delivered intents
		        registerReceiver(bcr = new BroadcastReceiver() {

		        	@Override
		            public void onReceive(Context context, Intent intent) {
		                String message = null;
		                boolean error = true;
		                final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		                final SharedPreferences.Editor SPeditor = settings.edit();
		                switch (getResultCode()) {
		                case Activity.RESULT_OK:
		                    message = getString(R.string.com_message_ok);
		                    error = false;
		                    break;
		                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
		                    message = "Error.";
		                    break;
		                case SmsManager.RESULT_ERROR_NO_SERVICE:
		                    message = "Error: No service.";
		                    break;
		                case SmsManager.RESULT_ERROR_NULL_PDU:
		                    message = "Error: Null PDU.";
		                    break;
		                case SmsManager.RESULT_ERROR_RADIO_OFF:
		                    message = "Error: Radio off.";
		                    break;
		                default:
		                    message = "Unknown Error.";
		                }
		                // Log.d(DEBUG_TAG, "Broadcast received="+message);
		                String temp = settings.getString(getString(R.string.sp_lastAlarm), "-");
		                // In case of error and if not already reported, add error to the lastAlarm string
		                if ((error)&&(!temp.contains("Error"))){
		                	// Log.d(DEBUG_TAG, "Entered IF statement.");
		                	Toast toast = Toast.makeText(context,
			        				getString(R.string.app_name)+"-"+message, Toast.LENGTH_LONG);
			        		toast.show();
			        		SPeditor.putString(getString(R.string.sp_lastAlarm), settings.getString(getString(R.string.sp_lastAlarm), "-")+" - "+message).commit();
		                }

		                try{
			                // Counter to avoid appearance of multiple popups
			                if (!appState.getSmsReportState()){
				                if (error)
				                	showMsgPopUp("Error",message);
				                else
				                	showMsgPopUp("Information",message);

				                appState.setSmsReportState(true);  // set WAIT_SMS_REPORT

			                }
		                } catch (Exception e){
		                	e.printStackTrace();
		                	// Log.w("commonActivity:", "Exception thrown=" + e.toString());
		                }
		            }
		        }, new IntentFilter(ACTION_SMS_SENT));
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        }*/
	 }

	public abstract void onResume(Bundle savedInstaceState);

	public void showMsgPopUp(String s_title, String s_msg){
		// build dialog box to display message
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    String posBtnTxt = getString(R.string.com_pos_btn);
	    builder.setMessage(s_msg)
	        .setCancelable(false)
	        .setTitle(s_title)
	        .setPositiveButton(posBtnTxt, (dialog, id) -> dialog.dismiss());
	    final AlertDialog alert = builder.create();
	    alert.show();
	}

	public void sendSMS(String s_msg){
		String PREFS_NAME = "MyPrefsFile";
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    	sendSMS2numb(settings.getString(getString(R.string.sp_destNumb), "0"), s_msg, false);
    }

	public void sendSMS2numb (String s_destNumb, String s_msg, boolean GPStracker){
		//SmsManager sms = SmsManager.getDefault();
    	//myApp appState = ((myApp)getApplicationContext());
    	String PREFS_NAME = "MyPrefsFile";
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor SPeditor = settings.edit();
        SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), true).commit();

        // Log.d("commonActivity:", "Destination: " + SMS_DEST_NUMBER);
        // Log.d("commonActivity:","Message: " + s_msg);

        int myNum = 0;
        try {
    	    myNum = Integer.parseInt(settings.getString(getString(R.string.sp_maxSMScount), getString(R.string.cfg_maxSMScount)));
    	} catch(NumberFormatException nfe) {
    		// Log.w("ConfigActivity:", "NumberFormatException: " + nfe.getMessage());
    		nfe.printStackTrace();
        }
        try {
	        if(s_destNumb.contentEquals("0")){
	        	// Log.d("myApp:", "Config Error, destination is empty");
	        	showMsgPopUp(getString(R.string.com_cnfgErrTitle), getString(R.string.com_cnfgErrTxt));
	        	if (GPStracker)
	        		SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), false);
	        	else
	        		SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false);
	        	SPeditor.commit();
	        }
	        else if ((settings.getInt(getString(R.string.sp_smsCounter), 0) >= myNum)&&(myNum!=0)) {
	        	String error_msg=getString(R.string.com_smsCounterExceed)+" "+settings.getString(getString(R.string.sp_maxSMScount), getString(R.string.cfg_maxSMScount));
	        	showMsgPopUp(getString(R.string.com_cnfgErrTitle), error_msg);
	        	if (GPStracker)
	        		SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), false);
	        	else
	        		SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false);
	        	SPeditor.commit();
	        }
	        else {  // Send SMS only if destination number has been entered



	        	// Increment SMS counter
	            int counter = settings.getInt(getString(R.string.sp_smsCounter), 0);
	            counter++;

	            SPeditor.putInt(getString(R.string.sp_smsCounter), counter);
	            SPeditor.commit();
	            /*
	        	appState.setSmsReportState(false); // reset flag WAIT_SMS_REPORT
		        try {
		        	sms.sendTextMessage(SMS_DEST_NUMBER, null, s_msg, PendingIntent.getBroadcast(
		                    commonActivity.this, 0, new Intent(ACTION_SMS_SENT), 0), null);
		        }catch(IllegalArgumentException iae){
		        	// Log.w("StandHeizungActivity:", "Exception thrown=" + iae.toString());
		        	iae.printStackTrace();
		        	if (GPStracker)
		        		SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), false);
		        	else
		        		SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false);
		        	SPeditor.commit();
		        	showMsgPopUp(getString(R.string.com_cnfgErrTitle), getString(R.string.com_cnfgErrTxt));
		       	}catch(NullPointerException npe){
		        	// Log.w("StandHeizungActivity:", "Exception thrown=" + npe.toString());
		        	npe.printStackTrace();
		        	if (GPStracker)
		        		SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), false);
		        	else
		        		SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false);
		        	SPeditor.commit();
		        	showMsgPopUp(getString(R.string.com_cnfgErrTitle), getString(R.string.com_cnfgErrTxt));
		        }    */
	        }
        } catch (Exception e){
        	e.printStackTrace();
        }
	}

	public void showInfo(PackageInfo pInfo){
		final String PREFS_NAME = "MyPrefsFile";
	    final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
	    final SharedPreferences.Editor SPeditor = settings.edit();

		String s_appname = getString(R.string.app_name);
		String s_versionPre = getString(R.string.com_versionPre);
    	String s_version = pInfo.versionName;
    	String s_authortitle = getString(R.string.authortitle);
    	String s_authorname = getString(R.string.authorname);
    	String s_smsCounter = getString(R.string.com_smsCounter);
    	int i_counter = settings.getInt(getString(R.string.sp_smsCounter), 0);
    	String s_prepaidCreditTitle = getString(R.string.com_PrepaidCreditTitle);
    	String s_prepaidCredit = settings.getString(getString(R.string.sp_prepaidCredit), getString(R.string.cfg_prepaidCredit));

    	// showMsgPopUp(s_appname, s_versionPre+s_version+"\n"+s_authortitle+s_authorname+"\n"+s_smsCounter+i_counter);

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    String posBtnTxt = getString(R.string.com_pos_btn);
	    builder.setMessage(s_versionPre+s_version+"\n"+s_authortitle+s_authorname+"\n"+s_smsCounter+i_counter+"\n"+s_prepaidCreditTitle+s_prepaidCredit)
	        .setCancelable(false)
	        .setTitle(s_appname)
	        .setNegativeButton(getString(R.string.com_reset_btn), (dialog, id) -> {
                // Check phone number and enable buttons
                if (isPhoneNumberCorrect(settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber)))){
                    SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), true);
                }
                // Reset the SMS counter
                SPeditor.putInt(getString(R.string.sp_smsCounter), 0).commit();
                dialog.dismiss();
                Toast toast = Toast.makeText(getBaseContext(),getString(R.string.com_reset_txt), Toast.LENGTH_LONG);
                        toast.show();
            })
	        .setPositiveButton(posBtnTxt, (dialog, id) -> dialog.dismiss());
	    final AlertDialog alert = builder.create();
	    alert.show();

	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	PackageInfo pInfo = null;

	     // Make Package Info available
	    	try {
	    		pInfo = getPackageManager().getPackageInfo("com.dynamite.heaterrc",
	    		PackageManager.GET_META_DATA);

	    	} catch (NameNotFoundException e) {
	    		// Log.w("StandHeizungActivity:", "NameNotFound: " + e.getMessage());
	    		e.printStackTrace();
	    	}

    	// Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                System.exit(0);
                return true;
            case R.id.info:
                assert pInfo != null;
                showInfo(pInfo);
                return true;
            case R.id.restore:
            	restoreDefaults();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Method is creating a popup dialog asking whether the restore shall really be executed.
     */
    public void restoreDefaults(){
    	// build dialog box to display message
    	String s_title = getString(R.string.com_warning);
    	String s_msg = getString(R.string.com_restore_warning);
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(s_msg)
	        .setCancelable(true)
	        .setTitle(s_title)
	        .setNegativeButton(getString(R.string.com_cancelbutton), (dialog, which) -> dialog.dismiss())
	        .setPositiveButton(getString(R.string.com_restorebutton), (dialog, id) -> {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor SPeditor = settings.edit();
                int counter = settings.getInt(getString(R.string.sp_smsCounter), 0);
                SPeditor.clear().commit();
                SPeditor.putInt(getString(R.string.sp_smsCounter), counter);
                SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false);
                SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabledGPS), false);
                SPeditor.commit();
                dialog.dismiss();
            });
	    final AlertDialog alert = builder.create();
	    alert.show();
    }


    /*
     * Method is creating a popup dialog asking whether the restore shall really be executed.
     */
    public void showPopupPlus(String s_title, String s_msg, String s_negBtntxt, String s_posBtntxt, final String s_prefs){
    	final String PREFS_NAME = "MyPrefsFile";
	    final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
	    final SharedPreferences.Editor SPeditor = settings.edit();
    	// build dialog box to display message
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(s_msg)
	        .setCancelable(true)
	        .setTitle(s_title)
	        .setNegativeButton(s_negBtntxt, (dialog, which) -> {
                SPeditor.putInt(s_prefs, -1).commit();
                dialog.dismiss();
            })
	        .setPositiveButton(s_posBtntxt, (dialog, id) -> {
                SPeditor.putInt(s_prefs, 1).commit();
                dialog.dismiss();
            });
	    final AlertDialog alert = builder.create();
	    alert.show();
    }

    /*
	 * Method is checking weather the given phone number is correct
	 *
	 * Returns: false if value is not ok
	 * 			true if value is ok
	 */
	public boolean isPhoneNumberCorrect(String number){
		if (number.compareTo("0") == 0)	return false;
		if (number.compareTo("") == 0) return false;
		if (number.contains(";")) return false;
		if (number.contains(",")) return false;
		if (number.contains(".")) return false;
		if (number.contains(":")) return false;
		if (number.contains("/")) return false;
        if (number.contains("(")) return false;
        return !number.contains(")");

		// else return true
    }

	public void showHelp(){
		String helpText = getString(R.string.helptext);
		showMsgPopUp(getString(R.string.sh_helpbutton), helpText);
	}

	public void showHelpGPS(){
		String helpText = getString(R.string.helptextGPS);
		showMsgPopUp(getString(R.string.sh_helpbutton), helpText);
	}

	public String int2time(int hour, int minute){
		String sHour = String.valueOf(hour);
		String sMinute = String.valueOf(minute);

		if (sHour.length() < 2)
			sHour = "0"+sHour;
		if (sMinute.length() < 2)
			sMinute = "0"+sMinute;

		return sHour+":"+sMinute;
	}
}
