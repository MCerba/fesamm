package com.dawson.fesamm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dawson.fesamm.Data.ApiCallAsyncTask;
import com.dawson.fesamm.Data.Stock;

public class ViewStockActivity extends AppCompatActivity {
    private TextView companyTextPortfolio;
    private TextView tickerTextPortfolio;
    private TextView priceTextPortfolio;
    private TextView currencyTextPortfolio;
    private TextView quantityTextPortfolio;
    private String email;
    private String password;
    private EditText quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stock);

        getTextViews();
        showInformation();
        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        this.email = prefs.getString("email", "nothing");
        this.password = prefs.getString("password", "nothing");
        quantity = (EditText) findViewById(R.id.quantity);
    }

    public void buyStocks(View v){
        ApiCallAsyncTask task = new ApiCallAsyncTask(this, email, password, null, false, quantityTextPortfolio, "buy", quantity.getText().toString(),
                tickerTextPortfolio.getText().toString(),null);
        task.execute();
    }

    public void sellStocks(View v){
        int quantityAvailable = Integer.parseInt(quantityTextPortfolio.getText().toString());
        int quantityToSell = Integer.parseInt(quantity.getText().toString());

        if(quantityToSell > quantityAvailable){
            AlertDialog.Builder builder= new AlertDialog.Builder(ViewStockActivity.this);
            builder.setMessage("Invalid quantity")
                    .setNegativeButton("OK", null);
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            ApiCallAsyncTask task = new ApiCallAsyncTask(this, email, password, null, false, quantityTextPortfolio, "sell", quantity.getText().toString(),
                    tickerTextPortfolio.getText().toString(),null);
            task.execute();
        }
    }

    private void getTextViews(){
        companyTextPortfolio = (TextView) findViewById(R.id.companyTextPortfolio);
        tickerTextPortfolio = (TextView) findViewById(R.id.tickerTextPortfolio);
        priceTextPortfolio = (TextView) findViewById(R.id.priceTextPortfolio);
        currencyTextPortfolio = (TextView) findViewById(R.id.currencyTextPortfolio);
        quantityTextPortfolio = (TextView) findViewById(R.id.quantityTextPortfolio);
    }

    private void showInformation(){
        companyTextPortfolio.setText(getIntent().getExtras().getString("companyName"));
        tickerTextPortfolio.setText(getIntent().getExtras().getString("tickerSymbol"));
        priceTextPortfolio.setText(getIntent().getExtras().getString("price"));
        currencyTextPortfolio.setText(getIntent().getExtras().getString("currency"));
        quantityTextPortfolio.setText(getIntent().getExtras().getInt("quantity") + "");
    }
}
