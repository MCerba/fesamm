package com.dawson.fesamm;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dawson.fesamm.Data.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * An AsyncTask that retrieves money left balance of a certain user from the API
 *
 * @author Cerba Mihail
 */
public class GetMoneyLeftAsincTask extends AsyncTask<Void, Void, Void> {

    private HttpURLConnection conn;
    private String token;
    private Context context;
    private TextView balanceTextView;
    private ProgressDialog progressDialog;
    private double cash;
    public static final String TAG = "CASH-DEBUG";

    public GetMoneyLeftAsincTask(String token, Context context, TextView balanceTextView) {
        this.token = token;
        this.context = context;
        this.balanceTextView = balanceTextView;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            sendRequest();
            getStocks();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        //Create a progressDialog letting the user know that we are retrieving his stocks
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting your Stocks ...");
        progressDialog.setTitle("Portfolio");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void v) {

        NumberFormat formatter = new DecimalFormat("#0.00");
        balanceTextView.setText("$" + formatter.format(this.cash));
        progressDialog.dismiss();
    }

    /**
     * A methds that sends the request to the CASH API with a token
     *
     * @throws IOException
     * @author Cerba Mihail
     */
    private void sendRequest() throws IOException {
        URL url = new URL("http://fesamm.herokuapp.com/api/cash");
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setRequestProperty("Authorization", "Bearer " + this.token);
        conn.connect();

        int response = conn.getResponseCode();

        // If the response is anything but 200, throw an exception
        if (response != HttpURLConnection.HTTP_OK) {
            throw new IOException();
        }
    }

    /**
     * A method that retrieves the information from the response and update cash variable
     *
     * @throws IOException
     * @throws JSONException
     * @author Cerba Mihail
     */
    private void getStocks() throws IOException, JSONException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JSONObject jsonObj = new JSONObject(buffer.readLine());
        this.cash = jsonObj.getDouble("cash");

        Log.d(TAG, "cash : " + cash);


    }
}
