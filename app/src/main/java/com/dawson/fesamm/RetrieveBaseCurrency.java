package com.dawson.fesamm;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask that takes care of displaying the base currency
 */
public class RetrieveBaseCurrency extends AsyncTask<String, Void, String> {

    TextView fromCurrency;
    TextView fromCurrencyName;
    TextView toCurrencyName;

    public RetrieveBaseCurrency(TextView fromCurrency, TextView fromCurrencyName, TextView toCurrencyName){
        this.fromCurrency = fromCurrency;
        this.fromCurrencyName = fromCurrencyName;
        this.toCurrencyName = toCurrencyName;
    }

    /**
     * Returns the base currency symbol (the one passed in the parameters or 'CAD' if not valid)
     * @param strings
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        String baseCurrency = "CAD";
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
            String currencyName = jsonResponse.getString(strings[0]);
            baseCurrency = strings[0];
        }
        catch(IOException ie){
        }
        catch(JSONException jse){
        }
        catch(Exception e){
        }

        return baseCurrency;
    }

    /**
     * Displays the base currency
     * @param baseCurrency
     */
    @Override
    protected void onPostExecute(String baseCurrency){
        fromCurrency.setText(baseCurrency);
        DisplayCurrencyName task = new DisplayCurrencyName(true, fromCurrencyName, toCurrencyName);
        task.execute(fromCurrency.getText().toString());
    }
}