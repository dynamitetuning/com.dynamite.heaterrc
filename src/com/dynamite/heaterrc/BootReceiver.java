package com.dynamite.heaterrc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	private static final String DEBUG_TAG = "BootReceiver";
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	public void onReceive(Context context, Intent intent) {
		final SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 4);
        // final SharedPreferences.Editor SPeditor = settings.edit();
		// TODO 
		Log.d(DEBUG_TAG, "onReceive methode entered");
		if (settings.getBoolean(context.getString(R.string.sp_schedule_active), false)){
			myApp appStates = ((myApp)context.getApplicationContext());
			appStates.setRecurringAlarm(context.getApplicationContext());
			Log.d(DEBUG_TAG, "Schedule has been activated");
		}
	}
}