package com.dawson.fesamm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);


        return true;
    }

    /**
     * A method that opens one of the three options in the menu and makes sure to close the previous one.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        boolean isAboutActive = prefs.getBoolean("isAboutActive", false);
        boolean isSettingsActive = prefs.getBoolean("isSettingsActive", false);

        if (item.getItemId() == R.id.about) {

            if (isAboutActive) {
                AboutActivity.aboutActivity.finish();
            }

            if (isSettingsActive) {
                SettingsActivity.settingsActivity.finish();
            }

            Intent intent = new Intent(this, AboutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

            return true;
        } else if (item.getItemId() == R.id.dawson) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

            return true;
        } else if (item.getItemId() == R.id.settings) {

            if (isAboutActive == true && AboutActivity.aboutActivity != null) {
                AboutActivity.aboutActivity.finish();
            }

            if (isSettingsActive == true && SettingsActivity.settingsActivity != null) {
                SettingsActivity.settingsActivity.finish();
            }

            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
