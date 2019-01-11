package com.dawson.fesamm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class AboutActivity extends MenuActivity {

    public static final String TAG = "ABOUT-DEBUG";
    boolean isAboutActive;

    public static Activity aboutActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a reference to the About Activity so it can be later closed from another Activity
        aboutActivity = this;

        setContentView(R.layout.activity_about);

        // Indicate that an About activity has been opened. Save this information in the SharedPreferences
        isAboutActive = true;
        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isAboutActive", isAboutActive);
        editor.commit();

    }

    /**
     * @author Sonya Rabhi
     * Display a dialog with a short description of the team
     * @param v
     */
    public void showAboutTeam(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialogText).setTitle(R.string.dialogTitle);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * @author Sonya Rabhi
     * Indicate that the Activity is not active anymore and save it in the SharedPrefences
     */
    public void onBackPressed(){
        isAboutActive = false;
        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isAboutActive", isAboutActive);
        editor.commit();

        finish();
    }

}
