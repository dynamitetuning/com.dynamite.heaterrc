package com.dynamite.heaterrc;
/* 
myApp.java

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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.widget.Toast;

public class myApp extends Application{
	// private static final String DEBUG_TAG = "myApp";
	private boolean WAIT_SMS_REPORT=false;
	public static final String PREFS_NAME = "MyPrefsFile";

	public boolean getSmsReportState(){
		return WAIT_SMS_REPORT;
	}
	
	public void setSmsReportState(boolean b){
		this.WAIT_SMS_REPORT=b;
	}
	
	public void setRecurringAlarm(Context context) {
        // we know mobiletuts updates at right around 1130 GMT.
        // let's grab new stuff at around 11:45 GMT, inexactly
    	// Log.d(DEBUG_TAG, "setRecurringAlarm called ");
    	final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 4);
    	final SharedPreferences.Editor SPeditor = settings.edit();
    	final Calendar cal = Calendar.getInstance();
    	
        Calendar updateTime = Calendar.getInstance();
        //updateTime.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        updateTime.setTimeZone(TimeZone.getDefault());
        int dayOfWeek = getNextWeekDay();
        
        Calendar calendar = Calendar.getInstance();
    	int day = calendar.get(Calendar.DAY_OF_WEEK);        
       
        if (dayOfWeek > 0)
        	updateTime.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        else if (dayOfWeek < 0){
        	Toast toast = Toast.makeText(context,
				"No weekday has been selected!", Toast.LENGTH_LONG);
			toast.show();
			SPeditor.putBoolean(getString(R.string.sp_schedule_active), false);
	        SPeditor.putString(getString(R.string.sp_nextAlarm), "-"); 
	        SPeditor.commit();
        	return;
        } else {
        	dayOfWeek = day;
        }
        int hourOfDay = settings.getInt(int2spWeekDay(dayOfWeek)+"Hour", 0);
        int minute = settings.getInt(int2spWeekDay(dayOfWeek)+"Minute", 0);
        updateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        updateTime.set(Calendar.MINUTE, minute);
        updateTime.set(Calendar.SECOND,0);
        
        // Log.d(DEBUG_TAG, "setRecurringAlarm called for "+int2spWeekDay(dayOfWeek)+" "+int2time(hourOfDay,minute)+" Day No:"+dayOfWeek);
        
        SPeditor.putString(getString(R.string.sp_nextAlarm), int2WeekDay(dayOfWeek)+" "+int2time(hourOfDay,minute))
        	.commit();
         
      //---PendingIntent to launch activity when the alarm triggers---                    
        Intent i = new Intent("com.dynamite.heaterrc.DisplayNotifications");

        //---assign an ID of 1---
        i.putExtra("NotifID", 1);                                

        PendingIntent displayIntent = PendingIntent.getActivity(
            getBaseContext(), 0, i, 0);     
        
        //---PendingIntent to launch activity when the alarm triggers---  
        Intent sendStartSMS = new Intent(context, AlarmReceiver.class);        
        PendingIntent recurringSendSMS = PendingIntent.getBroadcast(context,
                0, sendStartSMS, PendingIntent.FLAG_ONE_SHOT);         
        AlarmManager alarms = (AlarmManager) getSystemService(
                Context.ALARM_SERVICE);
        AlarmManager alarms2 =(AlarmManager) getSystemService(
        		Context.ALARM_SERVICE);
        
        /* setRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation)
        	Schedule a repeating alarm. */
        long checkTime = updateTime.getTimeInMillis()/1000;
        // check if time is in the past and correct it.
        /** Get the current time */
        long actualTime = cal.getTimeInMillis()/1000;
        if (actualTime > checkTime){
        	// Shift for one week
        	// Log.d(DEBUG_TAG, "actualTime="+actualTime+", checkTime="+checkTime);
        	updateTime.setTimeInMillis((checkTime+7*24*60*60)*1000);
        }
        
        alarms.setRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringSendSMS);
        alarms2.setRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, displayIntent);
        // Log.d(DEBUG_TAG, "setRecurringAlarm="+updateTime.getTimeInMillis());
    }
	
	public int getNextWeekDay (){
	    	final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 4);
	    	
	    	Time t = new Time();
	    	int hour = -1;
	    	int minute = -1;
	    	t.setToNow();	    	
	    	
	    	Calendar calendar = Calendar.getInstance();
	    	int day = calendar.get(Calendar.DAY_OF_WEEK);
	    	String weekDay=int2spWeekDay(day);
	    	
	    	// Log.d(DEBUG_TAG, "Todays Weekday is = "+day+"="+int2WeekDay(day)+"="+weekDay);
	    	
	    	boolean isTodayActive = settings.getBoolean(weekDay, false);
	    	if (isTodayActive){
	    		hour = settings.getInt(weekDay+"Hour", -1);
	    		minute = settings.getInt(weekDay+"Minute", -1);
	    		if (t.hour < hour){
	    			// Log.d(DEBUG_TAG, "Today active. HourNow="+t.hour+", setHour="+hour);
	    			return 0;
	    		}
	    		else if ((t.hour == hour ) && (t.minute < minute)){
	    			// Log.d(DEBUG_TAG, "Today active. MinuteNow="+t.minute+", setMin="+minute);
	    			return 0;
	    		}
	    		else if ((hour < 0) || (minute < 0)){
	    			// Log.d(DEBUG_TAG, "Today active, but hour or minute not present.");
	    			return 0;
	    		} else {
	    			// Schedule will happen in a week
	    			// Log.d(DEBUG_TAG, "Today active, but Schedule will happen in a week.");
	    			// return StringWeekDay2int(weekDay);
	    		}
	    	}
	    	
    	// Find out which day is coming next
    	for (int i=day+1; i<8; i++){
    		if (settings.getBoolean(int2spWeekDay(i), false)){
    			return i;
    		}
    	}
    	for (int i=1; i<=day; i++){
    		if (settings.getBoolean(int2spWeekDay(i), false)){
    			return i;
    		}
    	}
    	
    	return -1;
	}
	
	private String int2time(int hour, int minute){
		String sHour = hour+"";
		String sMinute = minute+"";
		
		if (sHour.length() < 2)
			sHour = "0"+sHour;
		if (sMinute.length() < 2)
			sMinute = "0"+sMinute;
		
		return sHour+":"+sMinute;
	}
	
	public String int2WeekDay (int day){
		Date d = new Date();
		if ((day > 0)&&(day < 8))
			d = new Date(113, 8, day); // 1.Sept.2013=Sunday
		else
			return null;	
		String weekDay = (String) android.text.format.DateFormat.format("EEEE", d);
		// Log.d(DEBUG_TAG, "int2WeekDay conversion: "+day+"="+weekDay+d.toString());
		return weekDay;
	}
	
	public String int2spWeekDay (int day){
		String result;
		switch (day){
		case 1:
			result = getString(R.string.sp_sunday);
			break;
		case 2:
			result = getString(R.string.sp_monday);
			break;
		case 3:
			result = getString(R.string.sp_tuesday);
			break;	
		case 4:
			result = getString(R.string.sp_wednesday);
			break;
		case 5:
			result = getString(R.string.sp_thursday);
			break;
		case 6:
			result = getString(R.string.sp_friday);
			break;
		case 7:
			result = getString(R.string.sp_saturday);
			break;
		default:
			result = null;	
		}
		
		return result;
	}
}
