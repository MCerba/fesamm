package com.dawson.fesamm;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.dawson.fesamm.Data.Stock;

/**
 * An AsyncTask that retrieves all the stocks of a certain user from the API
 * @author Sonya Rabhi
 */
public class GetStocksAsyncTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private HttpURLConnection conn;
    private String token;
    private List<Stock> stocks;
    private List<Stock> newStocks;
    private StocksAdapter adapter;
    private Context context;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    public static final String TAG = "STOCKS-DEBUG";

    public GetStocksAsyncTask(String token, List<Stock> stocks, Context context, RecyclerView recyclerView) {
        this.token = token;
        this.stocks = stocks;
        this.newStocks = new ArrayList<>();
        this.context = context;
        this.recyclerView = recyclerView;
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
    protected void onPreExecute(){
        //Create a progressDialog letting the user know that we are retrieving his stocks
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting your Stocks ...");
        progressDialog.setTitle("Portfolio");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void v) {

        Log.d(TAG, "Enter post execute");
        for (int i = 0; i < this.newStocks.size(); i++) {
            this.stocks.add(this.newStocks.get(i));
        }

        //RecyclerView recyclerView = context.findViewById(R.id.recyclerStocks);
        final StocksAdapter adapter = new StocksAdapter(this.stocks, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        SeparatorDecoration decoration = new SeparatorDecoration(context, Color.GRAY, 1.5f);
        recyclerView.addItemDecoration(decoration);


        //Close the progressDialog at the end
        progressDialog.dismiss();
    }

    /**
     * A methds that sends the request to the API with a token
     * @author Sonya Rabhi
     * @throws IOException
     */
    private void sendRequest() throws IOException {
        URL url = new URL("http://fesamm.herokuapp.com/api/allstocks");
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
     * A method that retrieves the information from the response and creates a list of Stocks
     * @author Sonya Rabhi
     * @throws IOException
     * @throws JSONException
     */
    private void getStocks() throws IOException, JSONException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        JSONObject jsonObj = new JSONObject(buffer.readLine());
        JSONArray stocks = jsonObj.getJSONArray("stocks");
        JSONArray prices = jsonObj.getJSONArray("prices");

        Log.d(TAG, "stocks array: " + stocks.toString());
        Log.d(TAG, "prices array: " + prices.toString());

        // Create Stocks objects and store them in a List
        for (int i = 0; i < stocks.length(); i++) {
            Stock stock = new Stock();
            JSONObject obj = stocks.getJSONObject(i);
            stock.setCompanyName(stocks.getJSONObject(i).getString("company_name"));
            stock.setTicker(stocks.getJSONObject(i).getString("ticker_symbol"));
            stock.setCurrencyType(stocks.getJSONObject(i).getString("currency"));
            stock.setPrice(prices.getDouble(i)+"");
            stock.setQuantity(stocks.getJSONObject(i).getInt("num_stocks"));

            Log.d(TAG, "last price: " + prices.getDouble(i));
            Log.d(TAG,"Company Name: " + stocks.getJSONObject(i).getString("company_name"));
            this.newStocks.add(stock);
        }

    }
}
