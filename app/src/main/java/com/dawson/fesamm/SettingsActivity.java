package com.dawson.fesamm;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class SettingsActivity extends MenuActivity {
    private String fName;
    private String lName;
    private String email;
    private String password;
    private String currency;
    private String stock;
    TextView inputErrorTextView;
    EditText fNameView,lNameView,emailView,passwordView,currencyView,stockView;

    private boolean isSettingsActive;
    public static Activity settingsActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.inputErrorTextView = (TextView)findViewById(R.id.inputError);

        // Create a reference to this activity so it can be later on closed from outside, if necessary
        settingsActivity = this;
        // Indicate a Settings Activity has been opened and is therefore active
        isSettingsActive = true;
        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isSettingsActive", isSettingsActive);
        editor.commit();

        fNameView = (EditText)findViewById(R.id.fnameInput);

        lNameView = (EditText)findViewById(R.id.lnameInput);

        emailView = (EditText)findViewById(R.id.emailInput);

        passwordView = (EditText)findViewById(R.id.passwordInput);
        passwordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        currencyView = (EditText)findViewById(R.id.currencyInput);

        stockView = (EditText)findViewById(R.id.stockInput);


        if (prefs != null) {
            this.fName = prefs.getString("fName", "");
            fNameView.setText(this.fName);

            this.lName = prefs.getString("lName", "");
            lNameView.setText(this.lName);

            this.email = prefs.getString("email", "");
            emailView.setText(this.email);

            this.password = prefs.getString("password", "");
            passwordView.setText(this.password);

            this.currency = prefs.getString("currency", "");
            currencyView.setText(this.currency);

            this.stock = prefs.getString("stock", "");
            stockView.setText(this.stock);
        }
    }

    /**
     * Saves the user input from the EditViews into SharedPreferences when the button is pressed
     * @author Sonya Rabhi
     * @param v
     */
    public void saveUserSettings(View v) {
        saveChanges();
    }

    @Override
    public void onBackPressed(){

        // Indicate the activity is about to be closed and save this info in the SharedPreferences
        isSettingsActive = false;
        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isSettingsActive", isSettingsActive);
        editor.commit();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.backPressed).setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SettingsActivity.this.finish();
            }
        }).setNegativeButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveChanges();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     *
     * Save the input of the use from the EditText into SharedPreferences and finish the activity.
     * @author Sonya Rabhi
     */
    public void saveChanges() {
        this.fName = fNameView.getText().toString();

        this.lName = lNameView.getText().toString();

        this.email = emailView.getText().toString();

        this.password = passwordView.getText().toString();

        this.currency = currencyView.getText().toString();

        this.stock = stockView.getText().toString();

        if (!isValidEmail(email)){
            this.inputErrorTextView.setText("Email is not valid. Please enter again!");
            this.emailView.setText("");
            return;
        }
        if (password.length() < 6 ){
            this.inputErrorTextView.setText("The password must contain at least 6 characters!");
            return;
        }

        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("fName", fName);
        editor.putString("lName", lName);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("currency", currency);
        editor.putString("stock", stock);

        // Save the date of the changes made to the Settings
        Date date = new Date();
        String dateStr = date.toString();
        editor.putString("dateStamp", dateStr);

        editor.commit();

        finish();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
