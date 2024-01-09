package com.dynamite.heaterrc;
/*
BootReceiver.java

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

public class BootReceiver {
	private static final String DEBUG_TAG = "BootReceiver";
	public static final String PREFS_NAME = "MyPrefsFile";
/*
	@Override
	public void onReceive(Context context, Intent intent) {
		final SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // final SharedPreferences.Editor SPeditor = settings.edit();
		// TODO
		Log.d(DEBUG_TAG, "onReceive methode entered");
		if (settings.getBoolean(context.getString(R.string.sp_schedule_active), false)){
			myApp appStates = ((myApp)context.getApplicationContext());
			appStates.setRecurringAlarm(context.getApplicationContext());
			Log.d(DEBUG_TAG, "Schedule has been activated");
		}
	} */
}
