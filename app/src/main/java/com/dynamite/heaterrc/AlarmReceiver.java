package com.dynamite.heaterrc;
/*
AlarmReceiver.java

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

public class AlarmReceiver {
    //private static final String DEBUG_TAG = "AlarmReceiver";
/*    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String ACTION_SMS_SENT = "com.example.android.apis.os.SMS_SENT_ACTION";

    public void onReceive(Context context, Intent intent) {
        // Log.d(DEBUG_TAG, "Recurring alarm; requesting an action.");
        final SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor SPeditor = settings.edit();
        final myApp appState = ((myApp)context.getApplicationContext());

        Date d = new Date();
        String weekDay = (String) android.text.format.DateFormat.format("EEEE", d);
        // Get the current time
        final Calendar cal = Calendar.getInstance();
        int iDay = cal.get(Calendar.DAY_OF_WEEK);
        int iHour = cal.get(Calendar.HOUR_OF_DAY);
        int iMinute = cal.get(Calendar.MINUTE);

        if(settings.getBoolean(context.getString(R.string.sp_schedule_active), false)){
			//Log.d(DEBUG_TAG,"Schedule is active");
		} else {
			//Log.d(DEBUG_TAG,"Schedule is not active. Exit.");
			System.exit(0);
		}


        String lastAlarm = weekDay + " " + int2time(iHour,iMinute);
        SPeditor.putString(context.getString(R.string.sp_lastAlarm), lastAlarm)
        .apply();

        int myNum = 0;
        try {
    	    myNum = Integer.parseInt(settings.getString(context.getString(R.string.sp_maxSMScount), context.getString(R.string.cfg_maxSMScount)));
    	} catch(NumberFormatException nfe) {
    		// Log.w("ConfigActivity:", "NumberFormatException: " + nfe.getMessage());
    		nfe.printStackTrace();
        }
        // Uncheck the happened recurring Event
        SPeditor.putBoolean(appState.int2spWeekDay(iDay), false).commit();

        int smsCounter=settings.getInt(context.getString(R.string.sp_smsCounter), 0);
        if ((smsCounter < myNum)||(myNum == 0)){
        	 // Send Start SMS
            sendSMS2numb(context, settings.getString(context.getString(R.string.sp_destNumb), "0"),
            		settings.getString(context.getString(R.string.sp_startCmd),
            				context.getString(R.string.cfg_startcmd)), false);

	        // set the next recurring event.
	        myApp appState2 = ((myApp)context.getApplicationContext());
	        appState2.setRecurringAlarm(context.getApplicationContext());
        } else {
        	String error_msg=context.getString(R.string.com_smsCounterExceed)+" "+settings.getString(context.getString(R.string.sp_maxSMScount), context.getString(R.string.cfg_maxSMScount));
        	// Log.d(DEBUG_TAG,"error_msg");
        	Toast toast = Toast.makeText(context,
    				context.getString(R.string.app_name)+"-"+error_msg, Toast.LENGTH_LONG);
    		toast.show();
        	String message="Error: Max SMS counter reached!";
        	SPeditor.putString(context.getString(R.string.sp_lastAlarm), settings.getString(context.getString(R.string.sp_lastAlarm), "-")+" - "+message);
        	SPeditor.putBoolean(context.getString(R.string.sp_schedule_active), false);
            SPeditor.putString(context.getString(R.string.sp_nextAlarm), "-");
            SPeditor.commit();
        }
    }

    public void sendSMS2numb (Context context, String s_destNumb, String s_msg, boolean GPStracker){
		SmsManager sms = SmsManager.getDefault();
    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor SPeditor = settings.edit();

        // Log.d(DEBUG_TAG, "Destination: " + SMS_DEST_NUMBER);
        // Log.d(DEBUG_TAG,"Message: " + s_msg);

        if(s_destNumb.contentEquals("0")){
        	// Log.d(DEBUG_TAG, "Config Error, destination is empty");
        	try {
				Toast toast = Toast.makeText(context, context.getString(R.string.app_name)+
						context.getString(R.string.com_SMSsendingErr), Toast.LENGTH_LONG);
				toast.show();
				// Add info to taskbar
			} catch (Resources.NotFoundException rnfe) {
        		rnfe.printStackTrace();
        		//Log.d(DEBUG_TAG, "catched exception: " + e.toString());
			}
        }
        else {  // Send SMS only if destination number has been entered
        	// Increment SMS counter

            int counter = settings.getInt(context.getString(R.string.sp_smsCounter), 0);
            counter++;

            SPeditor.putInt(context.getString(R.string.sp_smsCounter), counter);
            SPeditor.apply();
            final myApp appState = ((myApp)context.getApplicationContext());
            appState.setSmsReportState(false); // reset flag WAIT_SMS_REPORT

	        try {
	        	sms.sendTextMessage(SMS_DEST_NUMBER, null, s_msg, PendingIntent.getBroadcast(
	                    context, 0, new Intent(ACTION_SMS_SENT), 0), null);
	        	Toast toast = Toast.makeText(context, context.getString(R.string.app_name)+
	        			context.getString(R.string.com_SMSsending), Toast.LENGTH_LONG);
	        	toast.show();
	        	appState.setSmsReportState(true);  // set WAIT_SMS_REPORT to avoid pop-up window
	        }catch(IllegalArgumentException iae){
	        	// Log.w("StandHeizungActivity:", "Exception thrown=" + iae.toString());
	        	iae.printStackTrace();
	        	Toast toast = Toast.makeText(context, context.getString(R.string.app_name)+
	        			context.getString(R.string.com_cnfgErrTxt), Toast.LENGTH_LONG);
	    	    toast.show();
	        	// showMsgPopUp(context.getString(R.string.com_cnfgErrTitle), context.getString(R.string.com_cnfgErrTxt));

	       	}catch(NullPointerException npe){
	        	// Log.w("StandHeizungActivity:", "Exception thrown=" + npe.toString());
	        	npe.printStackTrace();
	        	Toast toast = Toast.makeText(context, context.getString(R.string.app_name)+
	        			context.getString(R.string.com_cnfgErrTxt), Toast.LENGTH_LONG);
	    	    toast.show();
	        	// showMsgPopUp(context.getString(R.string.com_cnfgErrTitle), context.getString(R.string.com_cnfgErrTxt));
	        }
        }
	}

    private String int2time(int hour, int minute){
		String sHour = String.valueOf(hour);
		String sMinute = String.valueOf(minute);

		if (sHour.length() < 2)
			sHour = "0"+sHour;
		if (sMinute.length() < 2)
			sMinute = "0"+sMinute;

		return sHour+":"+sMinute;
	}
*/

}
