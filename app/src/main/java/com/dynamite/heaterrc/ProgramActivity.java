package com.dynamite.heaterrc;
/*
ProgramActivity.java

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
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import java.util.Calendar;

public class ProgramActivity extends commonActivity {
	// private static final String DEBUG_TAG = "ProgramActivity";
    private int pHour;
    private int pMinute;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID = 0;

	/** Called when the activity is first created. */
	public static final String PREFS_NAME = "MyPrefsFile";

    //public static final String ACTION_SMS_SENT = "com.example.android.apis.os.SMS_SENT_ACTION";
    //public int SMS_RESULT_COUNTER=0;
	SharedPreferences.OnSharedPreferenceChangeListener listener;
	private final int IPC_ID = 1122;

	Button exitBtn;
	Button setBtn;
	Button helpschedBtn;
	ProgressBar sendingPB;
	TimePicker setTime;
    //RadioButton RB0;
    //RadioButton RB1;
    ViewFlipper VF;
    Button sunday;
    Button monday;
    Button tuesday;
    Button wednesday;
    Button thursday;
    Button friday;
    Button saturday;
    Button showDetails;
    TextView tvprogram;
    ToggleButton scheduleToggle;



    @Override
    public void onResume(Bundle savedInstaceState){
    	// Log.d(DEBUG_TAG, "onResume has been called");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program);

        // Log.d(DEBUG_TAG, "onCreate has been called");

     // Restore preferences
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor SPeditor = settings.edit();

        // Button initialization
        exitBtn = findViewById(R.id.exitbutton);
        setBtn = findViewById(R.id.setbutton);
        helpschedBtn = findViewById(R.id.btnhelpsched);
        setBtn.setEnabled(settings.getBoolean(getString(R.string.sp_sendBtnEnabled), false));

        // initialize TextView
        tvprogram = findViewById(R.id.tvprogram);

        // initialize ProgressBar
        sendingPB = findViewById(R.id.programPB);
        sendingPB.setVisibility(View.INVISIBLE);

        // initialize ToggleButton
        scheduleToggle = findViewById(R.id.scheduletoggle);
        scheduleToggle.setChecked(settings.getBoolean(getString(R.string.sp_schedule_active), false));

        // initialize timepicker
        setTime = findViewById(R.id.settime);
        setTime.setIs24HourView(true);
        setTime.setCurrentHour(settings.getInt(getString(R.string.sp_TimePickerHour), 12));
        setTime.setCurrentMinute(settings.getInt(getString(R.string.sp_TimePickerMin), 30));

        // initialize Spinner
        final Spinner spinner = findViewById(R.id.spinner1);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.selection_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(settings.getInt(getString(R.string.sp_progSpinnerPos), 0));

        // initialize ViewFlipper
        VF = findViewById(R.id.ViewFlipper01);

        // initialize Weekdays buttons
        sunday = findViewById(R.id.btnsunday);
        monday = findViewById(R.id.btnmonday);
        tuesday = findViewById(R.id.btntuesday);
        wednesday = findViewById(R.id.btnwednesday);
        thursday = findViewById(R.id.btnthursday);
        friday = findViewById(R.id.btnfriday);
        saturday = findViewById(R.id.btnsaturday);

        showDetails = findViewById(R.id.btnshowdetail);

        sunday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_sunday), false)));
        monday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_monday), false)));
        tuesday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_tuesday), false)));
        wednesday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_wednesday), false)));
        thursday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_thursday), false)));
        friday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_friday), false)));
        saturday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_saturday), false)));


        /* Get the current time */
        final Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);
        //pHour = settings.getInt(getString(R.string.sp_TimePickerHour), 12);
        //pMinute = settings.getInt(getString(R.string.sp_TimePickerMin), 30);


     // set click listener on the exitBtn
        exitBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "Exit Button pressed");
            System.exit(0);
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent,
    	        View view, int pos, long id) {
				// select the view based on spinner position
                VF.setDisplayedChild(pos);
                if (pos>0){
		           // Obtain MotionEvent object
		        	tvprogram.setText(getString(R.string.prog_tvprogram_schedule));
		        } else {
		        	tvprogram.setText(getString(R.string.prog_tvprogram));
		        }
                SPeditor.putInt(getString(R.string.sp_progSpinnerPos), pos).apply();
    	    }

    	    public void onNothingSelected(AdapterView parent) {
    	      // Select the default position
    	    	// Log.d(DEBUG_TAG, "Nothing selected on Spinner. Position 0 selected.");
    	    	spinner.setSelection(0);
    	    }
		});

        helpschedBtn.setOnClickListener(v -> showMsgPopUp(getString(R.string.prog_helpTitle), getString(R.string.prog_helpText)));

        // set click listener on the setBtn
        setBtn.setOnClickListener(v -> {
            // Log.d(DEBUG_TAG, "Set Button pressed");
            sendingPB.setVisibility(View.VISIBLE);
            SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), false).commit();

            // Read Time
            String hour;
            String minute;
            hour = setTime.getCurrentHour().toString();
            minute = setTime.getCurrentMinute().toString();
            // Add leading zero if less than two digit
            if(hour.length() < 2)
                hour = "0" + hour;
            if(minute.length() < 2)
                minute = "0" + minute;

            // Log.d(DEBUG_TAG,"Time set=" + hour + minute);
            String SMS_START_COMMAND = settings.getString(getString(R.string.sp_startCmd), getString(R.string.cfg_startcmd));

            // send SMS with Start command followed by time
            sendSMS(SMS_START_COMMAND+hour+minute);

            // Store hour and time to settings
            if (setTime.getCurrentHour()!=settings.getInt(getString(R.string.sp_TimePickerHour), 12))
                SPeditor.putInt(getString(R.string.sp_TimePickerHour), setTime.getCurrentHour());
            if (setTime.getCurrentMinute()!=settings.getInt(getString(R.string.sp_TimePickerMin), 30))
                SPeditor.putInt(getString(R.string.sp_TimePickerMin), setTime.getCurrentMinute());
            // Commit the edits!
            SPeditor.commit();
        });

        scheduleToggle.setOnClickListener(v -> {
            boolean on = scheduleToggle.isChecked();
if (on) {
SPeditor.putBoolean(getString(R.string.sp_schedule_active), true).commit();
} else {
                SPeditor.putBoolean(getString(R.string.sp_schedule_active), false).commit();
}
        });

        sunday.setOnTouchListener((v, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
return true;
}
            if(event.getAction()!=MotionEvent.ACTION_UP) return false;

            dayPressed(getString(R.string.sp_sunday));
            return false;
        });

        monday.setOnTouchListener((v, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
return true;
}
if(event.getAction()!=MotionEvent.ACTION_UP) return false;

dayPressed(getString(R.string.sp_monday));
return false;
        });

        tuesday.setOnTouchListener((v, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN) return true;
if(event.getAction()!=MotionEvent.ACTION_UP) return false;

dayPressed(getString(R.string.sp_tuesday));
            return false;
        });

        wednesday.setOnTouchListener((v, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN) return true;
if(event.getAction()!=MotionEvent.ACTION_UP) return false;

dayPressed(getString(R.string.sp_wednesday));
            return false;
        });

        thursday.setOnTouchListener((v, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN) return true;
if(event.getAction()!=MotionEvent.ACTION_UP) return false;

dayPressed(getString(R.string.sp_thursday));
            return false;
        });

        friday.setOnTouchListener((v, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN) return true;
if(event.getAction()!=MotionEvent.ACTION_UP) return false;

dayPressed(getString(R.string.sp_friday));
            return false;
        });

        saturday.setOnTouchListener((v, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN) return true;
if(event.getAction()!=MotionEvent.ACTION_UP) return false;

dayPressed(getString(R.string.sp_saturday));
            return false;
        });

        showDetails.setOnClickListener(v -> {
            try {
                Intent myIntentA1A2 = new Intent(ProgramActivity.this,
                        AlarmDetails.class);

                Bundle myData = new Bundle();
                myData.putString("myString1", "Hello Android");
                myData.putDouble("myDouble1", 3.141592);
                int[] myLittleArray = { 1, 2, 3 };
                myData.putIntArray("myIntArray1", myLittleArray);

                myIntentA1A2.putExtras(myData);

                startActivityForResult(myIntentA1A2,IPC_ID);
            } catch (Exception e) {
                e.printStackTrace();
                // Log.d(DEBUG_TAG, "showDetails: "+e.getMessage());
            }
        });
        /*
     // Register broadcast receivers for SMS sent and delivered intents
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	if (isPhoneNumberCorrect(settings.getString(getString(R.string.sp_destNumb), getString(R.string.cfg_phonenumber)))){
		        	SPeditor.putBoolean(getString(R.string.sp_sendBtnEnabled), true).commit();
		        }
            }
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
              setBtn.setEnabled(SEND_BTN_ENABLED);
              if(SEND_BTN_ENABLED)
                  sendingPB.setVisibility(View.INVISIBLE);
          }
          else if (key.compareTo(getString(R.string.sp_scheduledSend))==0){
              if (settings.getBoolean(getString(R.string.sp_scheduledSend), false)){
                  // Log.d(DEBUG_TAG, "scheduledSend");
                  String SMS_START_COMMAND = settings.getString(getString(R.string.sp_startCmd), getString(R.string.cfg_startcmd));
                  sendSMS(SMS_START_COMMAND);
                  SPeditor.putBoolean(getString(R.string.sp_scheduledSend), false).commit();
              }

          }
          else if (key.compareTo(getString(R.string.sp_schedule_active))==0){
              myApp appStates = ((myApp)getApplicationContext());
              if (settings.getBoolean(getString(R.string.sp_schedule_active), false)){
                  // Log.d(DEBUG_TAG, "Schedule has been activated");
                  // appStates.setRecurringAlarm(getApplicationContext());
                  Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.app_name)+
                          getString(R.string.prog_schedule_activated), Toast.LENGTH_SHORT);
                  toast.show();
              } else {
                  // Log.d(DEBUG_TAG, "Schedule has been deactivated");
                  delAlarm(appStates.getApplicationContext());
                  // When Schedule has been deactivated, there is no next alarm
                  SPeditor.putString(getString(R.string.sp_nextAlarm), "-").commit();
                  if (scheduleToggle.isChecked()){
                      Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.app_name)+
                              getString(R.string.prog_schedule_deactivated), Toast.LENGTH_SHORT);
                      toast.show();
                  }
              }
              // synchronize toggle button with settings
              scheduleToggle.setChecked(settings.getBoolean(getString(R.string.sp_schedule_active), false));
          }
          // Update the weekday buttons, especially after that an SMS has been sent...
          updateWeekBtn();
     };
     settings.registerOnSharedPreferenceChangeListener(listener);
    }

    private void delAlarm(Context context){
    	// Log.d(DEBUG_TAG, "delAlarm called");
/*    	Intent downloader = new Intent(context, AlarmReceiver.class);
    	PendingIntent recurringSendSMS = PendingIntent.getBroadcast(context,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getSystemService(
                Context.ALARM_SERVICE);
        try {
            assert alarms != null;
            alarms.cancel(recurringSendSMS);
		} catch (NullPointerException npe){
        	npe.printStackTrace();
			// Log.d(DEBUG_TAG, "Alarm could not be cancelled!");
		}*/
    }

    private void updateWeekBtn(String weekday, boolean btnset){
    	if (weekday.compareTo(getString(R.string.sp_sunday))==0)
    		sunday.setBackgroundColor(btnColor(btnset));
    	else if (weekday.compareTo(getString(R.string.sp_monday))==0)
    		monday.setBackgroundColor(btnColor(btnset));
    	else if (weekday.compareTo(getString(R.string.sp_tuesday))==0)
    		tuesday.setBackgroundColor(btnColor(btnset));
    	else if (weekday.compareTo(getString(R.string.sp_wednesday))==0)
    		wednesday.setBackgroundColor(btnColor(btnset));
    	else if (weekday.compareTo(getString(R.string.sp_thursday))==0)
    		thursday.setBackgroundColor(btnColor(btnset));
    	else if (weekday.compareTo(getString(R.string.sp_friday))==0)
    		friday.setBackgroundColor(btnColor(btnset));
    	else if (weekday.compareTo(getString(R.string.sp_saturday))==0)
    		saturday.setBackgroundColor(btnColor(btnset));
    	else {
    		// Log.d(DEBUG_TAG, "updateWeekBtn: No weekday found!");
    	}
    }

    private void updateWeekBtn(){
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        sunday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_sunday), false)));
        monday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_monday), false)));
        tuesday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_tuesday), false)));
        wednesday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_wednesday), false)));
        thursday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_thursday), false)));
        friday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_friday), false)));
        saturday.setBackgroundColor(btnColor(settings.getBoolean(getString(R.string.sp_saturday), false)));
    }

    private int btnColor(boolean btnset){
    	if (btnset)
    		return 0xFF00FF00; // return green
    	else
    		return 0xFFFF0000; // return red
    }

    private void dayPressed(String weekday){
    	final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor SPeditor = settings.edit();

    	SPeditor.putString(getString(R.string.sp_lastTouched), weekday);
    	SPeditor.putBoolean(getString(R.string.sp_schedule_active), false);
    	SPeditor.apply();
        if (settings.getBoolean(weekday, false)){
			SPeditor.putBoolean(weekday, false).commit();
			updateWeekBtn(weekday, false);
        } else {
			pHour = settings.getInt(weekday+"Hour", pHour);
			pMinute = settings.getInt(weekday+"Minute", pMinute);
			// Log.d(DEBUG_TAG, "dayPressed: "+weekday+" "+int2time(pHour,pMinute));
			int timeDialogId = StringWeekDay2int(weekday);
			if (timeDialogId > 0)
				showDialog(timeDialogId);
			else
				showDialog(TIME_DIALOG_ID);
        }

    }

    /** Callback received when the user "picks" a time in the dialog */
    private final TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                pHour = hourOfDay;
                pMinute = minute;
                displayToast();
            }
        };

    /** Displays a notification when the time is updated */
    private void displayToast() {
    	final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor SPeditor = settings.edit();
        String lastTouched = settings.getString(getString(R.string.sp_lastTouched), "-");
    	Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(int2time(pHour,pMinute)),   Toast.LENGTH_SHORT).show();
    	// Log.d(DEBUG_TAG, "displayToast: lastTouched="+lastTouched+" "+int2time(pHour,pMinute));
    	if (lastTouched.compareTo("-")!=0){
    		SPeditor.putBoolean(lastTouched, true);
            SPeditor.putInt(lastTouched+"Hour", pHour);
            SPeditor.putInt(lastTouched+"Minute", pMinute).apply();
            updateWeekBtn(lastTouched, true);
            if (settings.getBoolean(getString(R.string.sp_schedule_active), false)){
	            myApp appStates = ((myApp)getApplicationContext());
	    		// appStates.setRecurringAlarm(getApplicationContext());
    		}
        } else {
        	// Log.d(DEBUG_TAG, "displayToast: Could not find lastTouched");
        }
    }

    /** Create a new dialog for time picker */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case TIME_DIALOG_ID:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
            return new TimePickerDialog(this,
                    mTimeSetListener, pHour, pMinute, true);
        }
        return null;
    }

    private int StringWeekDay2int (String weekDay){
    	int cWeekDay;
    		if (weekDay.compareTo(getString(R.string.sp_sunday))==0)
    			cWeekDay=Calendar.SUNDAY;
    		else if (weekDay.compareTo(getString(R.string.sp_monday))==0)
				cWeekDay=Calendar.MONDAY;
    		else if (weekDay.compareTo(getString(R.string.sp_tuesday))==0)
				cWeekDay=Calendar.TUESDAY;
			else if (weekDay.compareTo(getString(R.string.sp_wednesday))==0)
				cWeekDay=Calendar.WEDNESDAY;
			else if (weekDay.compareTo(getString(R.string.sp_thursday))==0)
				cWeekDay=Calendar.THURSDAY;
			else if (weekDay.compareTo(getString(R.string.sp_friday))==0)
				cWeekDay=Calendar.FRIDAY;
			else if (weekDay.compareTo(getString(R.string.sp_saturday))==0)
				cWeekDay=Calendar.SATURDAY;
			else
				// Nothing selected
				cWeekDay=-1;
    	return cWeekDay;
    }
}

