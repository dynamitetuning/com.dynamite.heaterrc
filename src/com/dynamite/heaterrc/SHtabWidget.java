package com.dynamite.heaterrc;
/* 
SHtabWidget.java

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
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class SHtabWidget extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    final String PREFS_NAME = "MyPrefsFile";
	    final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 4);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)	    
	    intent = new Intent().setClass(this, ConfigActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("config").setIndicator(getString(R.string.tab_config_name),
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, ProgramActivity.class);
	    spec = tabHost.newTabSpec("program").setIndicator(getString(R.string.tab_program_name),
	                      res.getDrawable(R.drawable.ic_tab_albums))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, StandHeizungActivity.class);
	    spec = tabHost.newTabSpec("home").setIndicator(getString(R.string.tab_home_name),
	                      res.getDrawable(R.drawable.ic_tab_songs))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, GPStrackerActivity.class);
	    spec = tabHost.newTabSpec("GPS-Tracker").setIndicator(getString(R.string.tab_gpstrack_name),
	                      res.getDrawable(R.drawable.ic_tab_gps))
	                  .setContent(intent);
	    // Enable this tab only if option is activated in the config. 
	    if (settings.getBoolean(getString(R.string.sp_GPStrackerTab), false))
	    	tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(2);
	}

}