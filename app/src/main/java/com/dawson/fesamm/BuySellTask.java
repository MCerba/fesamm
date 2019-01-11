package com.dawson.fesamm;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import static com.dawson.fesamm.Data.ApiCallAsyncTask.getPostDataString;

public class BuySellTask extends AsyncTask<String, Void, String> {

    private TextView tvQuantity;
    private String typeOfTransaction;
    private Context context;
    private boolean containError;

    public BuySellTask(TextView tvQuantity, Context context){
        this.tvQuantity = tvQuantity;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        this.typeOfTransaction = strings[0];
        String quantity = strings[1];
        String ticker = strings[2];
        String token =  strings[3];

        try {
            URL url = new URL("http://fesamm.herokuapp.com/api/" + typeOfTransaction);

            JSONObject postParameters = new JSONObject();
            postParameters.put("ticker", ticker);
            postParameters.put("quantity", quantity);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Accept", "application/json");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(getPostDataString(postParameters));
            writer.flush();
            writer.close();
            outputStream.close();

            InputStream inputStream;
            containError = false;

            if(conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
                inputStream = conn.getInputStream();
            }
            else{
                inputStream = conn.getErrorStream();
                containError = true;
            }

            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer response = new StringBuffer();
            String line;
            while((line = input.readLine()) != null){
                response.append(line);
            }
            input.close();

            if(containError){
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("error");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return quantity;
    }

    @Override
    protected void onPostExecute(String quantityToSell){
        if(containError){
            AlertDialog.Builder builder= new AlertDialog.Builder(context);
            builder.setMessage("Insufficient cash")
                    .setNegativeButton("OK", null);
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            int quantity = Integer.parseInt(quantityToSell);
            int oldQuantity = Integer.parseInt(tvQuantity.getText().toString());

            if (this.typeOfTransaction.equalsIgnoreCase("sell")) {
                int updatedQuantity = oldQuantity - quantity;
                tvQuantity.setText("" + updatedQuantity);
            } else {
                int updatedQuantity = oldQuantity + quantity;
                tvQuantity.setText("" + updatedQuantity);
            }

            Toast.makeText(context, "Transaction completed", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method taken from https://www.studytutorial.in/android-httpurlconnection-post-and-get-request-tutorial
     * @param params
     * @return result.toString()
     */
    public static String getPostDataString(JSONObject params) {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            try {
                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        System.out.println(result.toString());
        return result.toString();
    }
}
