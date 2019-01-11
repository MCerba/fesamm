package com.dawson.fesamm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        if (prefs.getString("email", "").equals("")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isAboutActive", false);
        editor.commit();

    }

    public void runForeign_exchange(View view) {
        Intent intent = new Intent(this, ForeignExchangeActivity.class);
        startActivity(intent);
    }

    public void runFinancial_hints(View view) {
        startActivity(new Intent(this, FinancialHintsActivity.class));
    }

    public void runStock_quote(View view) {
        startActivity(new Intent(this, StockActivity.class));
    }

    public void runNotes(View view) {
        Intent intent = new Intent(this,NotesActivity.class);
        startActivity(intent);
    }

    public void runLoan_calculator(View view) {
        Intent intent = new Intent(this,LoanCalcActivity.class);
        startActivity(intent);
    }

    public void runStock_portfolio(View view) {
        Intent intent = new Intent(this,PortfolioActivity.class);
        startActivity(intent);
    }
}
