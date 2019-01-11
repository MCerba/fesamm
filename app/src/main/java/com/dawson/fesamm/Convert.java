package com.dawson.fesamm;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * AsyncTask that takes care of converting currencies
 * @author Alpha Kabine Kaba
 */
public class Convert extends AsyncTask<String, Void, Double> {
    private TextView toAmount;
    private DecimalFormat df = new DecimalFormat("#.00");

    public Convert(TextView toAmount){
        super();
        this.toAmount = toAmount;
    }

    /**
     * This method returns the result of the conversion
     * @param symbols
     * @return amountConverted - result of the conversion
     */
    @Override
    protected Double doInBackground(String... symbols) {
        Double amountConverted = -1.0;

        try{
            String urlString = "https://openexchangerates.org/api/latest.json?app_id=311254c97fc142ca9b97b967fbbe2c06";
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
            JSONObject rates = new JSONObject(jsonResponse.getJSONObject("rates").toString());

            Double from = rates.getDouble(symbols[1]);
            Double to = rates.getDouble(symbols[2]);
            Double multiplier = to/from;

            amountConverted = Double.parseDouble(symbols[0]) *  multiplier;
        }
        catch(IOException ie){
        }
        catch(JSONException jse){
        }

        return amountConverted;
    }

    /**
     * This method displays the amount converted
     * @param amountConverted
     */
    @Override
    protected void onPostExecute(Double amountConverted){
        if(amountConverted != -1.0)
            toAmount.setText(this.df.format(amountConverted).toString());
        else
            toAmount.setText("Could not retrieve data online, check your connection");
    }
}
