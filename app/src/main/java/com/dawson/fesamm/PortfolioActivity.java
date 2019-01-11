package com.dawson.fesamm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.dawson.fesamm.Data.ApiCallAsyncTask;
import com.dawson.fesamm.Data.Stock;

public class PortfolioActivity extends MenuActivity {
    //private RecyclerView recyclerView;

    private List<Stock> stocks;
    private String email;
    private String password;

    public static Activity portfolioActivity;

    public static final String TAG = "PORTFOLIO-DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        portfolioActivity = this;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_portfolio);
        this.stocks = new ArrayList<>();



    }

    /**
     *  This method is getting information from settings (shared preferences) and sending it to GetTokenAsyncTask
     */
    private void connect(){
        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        email = prefs.getString("email", "nothing :(");
        password = prefs.getString("password", "nothing :(");
        // Check if the network is available. Otherwise, show a Toast prompting a user to check the connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            // Create an AsyncTask to get the token
            /*ApiCallAsyncTask tokenTask = new ApiCallAsyncTask(email, password, this);
            tokenTask.execute();*/
            RecyclerView recyclerView = findViewById(R.id.recyclerStocks);

            TextView balanceTextView = findViewById(R.id.cashUser);


            ApiCallAsyncTask task = new ApiCallAsyncTask(this, email, password, recyclerView, true, null, null, null, null,balanceTextView);

            task.execute();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please check your internet connection",
                    Toast.LENGTH_SHORT);

            toast.show();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        connect();
    }



}
