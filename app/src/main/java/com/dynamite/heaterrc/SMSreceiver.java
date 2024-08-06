package com.dynamite.heaterrc;
/*
SMSreceiver.java

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

public class SMSreceiver{
	// private static final String DEBUG_TAG = "SMSreceiver";
	public static final String PREFS_NAME = "MyPrefsFile";
	/*
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor SPeditor = settings.edit();

		// Log.d(DEBUG_TAG, "Entered onReceive method...");

		try {
			Object messages[] = (Object[]) bundle.get("pdus");
			SmsMessage smsMessage[] = new SmsMessage[messages.length];
			for (int n = 0; n < messages.length; n++) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
			}

			// String smsText = smsMessage[0].getMessageBody();
			String originNumber = smsMessage[0].getDisplayOriginatingAddress();
			// Log.d(DEBUG_TAG, "Received SMS Text:" + smsText);
			// Log.d(DEBUG_TAG, "Received SMS from number: " + originNumber);
			String heaterNumber = settings.getString(context.getString(R.string.sp_destNumb), "0");
			String GPStrackNumber = settings.getString(context.getString(R.string.sp_destNumbGPS), "0");
			if (heaterNumber.length() > 2){
				heaterNumber = heaterNumber.substring(2, heaterNumber.length());

				if (originNumber.contains(heaterNumber)){
					// originating number corresponds to heater number
					Toast toast = Toast.makeText(context,
					context.getString(R.string.com_SMSreceived) + originNumber, Toast.LENGTH_LONG);
					toast.show();
					SPeditor.putBoolean(context.getString(R.string.sp_receivedSMSfb), true).commit();
					// Calculate remaining prepaid credit
			    	try {
			        	BigDecimal prepaidCredit = new BigDecimal(settings.getString(context.getString(R.string.sp_prepaidCredit), context.getString(R.string.cfg_prepaidCredit)));
			        	BigDecimal SMScost = new BigDecimal(settings.getString(context.getString(R.string.sp_SMScost), context.getString(R.string.cfg_SMScost)));
			        	BigDecimal defaultValue = new BigDecimal(context.getString(R.string.cfg_prepaidCredit));
			        	int costcompare = prepaidCredit.compareTo(SMScost);
			        	boolean showNotify = false;
			        	if (costcompare>0){
			            	String result = prepaidCredit.subtract(SMScost).toPlainString();

			            	SPeditor.putString(context.getString(R.string.sp_prepaidCredit), result);
			            	SPeditor.commit();
			        	} else if (costcompare < 0) {
			        		// Recharge your prepaid credit
			        		showNotify = true;
			        	} else if ((costcompare == 0)&&(prepaidCredit.compareTo(defaultValue)!=0)){
			        		showNotify = true;
			        	}
			        	if (showNotify){
			        		Toast toast2 = Toast.makeText(context,
			        				context.getString(R.string.com_prepaidRecharge), Toast.LENGTH_LONG);
			    			toast2.show();
			    			toast2.show(); // show twice to increase duration
			        	}

			        	// update the string equivalent
			        }catch(Exception e) {
			        	e.printStackTrace();
			        }
				}
			}
			if (GPStrackNumber.length() > 2){
				GPStrackNumber = GPStrackNumber.substring(2, GPStrackNumber.length());
				if (originNumber.contains(GPStrackNumber)){
					Toast toast = Toast.makeText(context,
					context.getString(R.string.com_SMSreceived) + originNumber, Toast.LENGTH_LONG);
					toast.show();
					// SPeditor.putBoolean(context.getString(R.string.sp_receivedSMSfb), true).commit();
				}
			}
		} catch(IndexOutOfBoundsException iobe){
			// Log.w(DEBUG_TAG, "IooBException thrown=" + iobe.toString());
			iobe.printStackTrace();
		} catch(Exception e){
			// Log.w(DEBUG_TAG, "Exception thrown=" + e.toString());
			e.printStackTrace();
		}
	}*/
}
