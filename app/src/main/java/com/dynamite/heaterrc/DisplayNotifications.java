package com.dynamite.heaterrc;
/*
DisplayNotifications.java

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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class DisplayNotifications extends commonActivity {
    /** Called when the activity is first created. */
	// private static final String DEBUG_TAG = "DisplayNotifications";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor SPeditor = settings.edit();

        try {
	        // Register broadcast receivers for SMS sent and delivered intents
	        registerReceiver(new BroadcastReceiver() {

	        	@Override
	            public void onReceive(Context context, Intent intent) {
	                String message = null;
	                boolean error = true;
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
	            }
	        }, new IntentFilter(ACTION_SMS_SENT));
        } catch (Exception ex) {
        	ex.printStackTrace();
        }*/

        //---get the notification ID for the notification;
        // passed in by the MainActivity---
		int notifID = 0;
		try {
			notifID = getIntent().getExtras().getInt("NotifID");
		} catch (NullPointerException npe){
        	npe.printStackTrace();
		}
        //---PendingIntent to launch activity if the user selects
        // the notification---
        Intent i = new Intent("com.dynamite.heaterrc.AlarmDetails");
        i.putExtra("NotifID", notifID);

        PendingIntent detailsIntent =
            PendingIntent.getActivity(this, 0, i, 0);

        NotificationManager nm = (NotificationManager)
            getSystemService(NOTIFICATION_SERVICE);
        Notification notif = new Notification(
            R.drawable.ic_launcher,
            getString(R.string.dn_notificationTitle),
            System.currentTimeMillis());

        CharSequence from = getString(R.string.dn_notificationTitle);
        CharSequence message = getString(R.string.dn_notificationText);
        notif.setLatestEventInfo(this, from, message, detailsIntent);

        //---100ms delay, vibrate for 250ms, pause for 100 ms and
        // then vibrate for 500ms---
        // notif.vibrate = new long[] { 100, 250, 100, 500};
        assert nm != null;
        nm.notify(notifID, notif);
        //---destroy the activity---

        finish();
    }

    public void onResume(Bundle savedInstaceState){
    	// nothing to do
    }
}
