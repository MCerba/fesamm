package com.dawson.fesamm;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask that takes care of displaying currencies' names
 */
public class DisplayCurrencyName extends AsyncTask<String, Void, String> {
    boolean from;
    TextView fromCurrencyName;
    TextView toCurrencyName;

    public DisplayCurrencyName(boolean from, TextView fromCurrencyName, TextView toCurrencyName){
        this.from = from;
        this.fromCurrencyName = fromCurrencyName;
        this.toCurrencyName = toCurrencyName;
    }

    /**
     * This method returns the currency name for a given currency symbol (i.e United States Dollar for USD)
     * @param strings
     * @return currencyName - currency name for a given currency symbol
     */
    @Override
    protected String doInBackground(String... strings) {
        String currencyName = "";
        try{
            String urlString = "https://openexchangerates.org/api/currencies.json?app_id=311254c97fc142ca9b97b967fbbe2c06";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = input.readLine()) != null){
                response.append(line);
            }
            input.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            currencyName = jsonResponse.getString(strings[0]);
        }
        catch(IOException ie){
        }
        catch(Exception e){
        }

        return currencyName;
    }

    /**
     * Displays the currency name
     * @param currencyName
     */
    @Override
    protected void onPostExecute(String currencyName){
        if(from)
            fromCurrencyName.setText(currencyName);
        else
            toCurrencyName.setText(currencyName);
    }
}
