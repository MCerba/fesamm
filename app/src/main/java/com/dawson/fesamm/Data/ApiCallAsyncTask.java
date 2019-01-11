package com.dawson.fesamm.Data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dawson.fesamm.BuySellTask;
import com.dawson.fesamm.ForeignExchangeActivity;
import com.dawson.fesamm.GetMoneyLeftAsincTask;
import com.dawson.fesamm.GetStocksAsyncTask;
import com.dawson.fesamm.PortfolioActivity;
import com.dawson.fesamm.R;
import com.dawson.fesamm.SettingsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An AsyncTask that is in charge of retrieving the JWT token of an authenticated user
 * @author Sonya Rabhi
 */
public class ApiCallAsyncTask extends AsyncTask<Void, Void, String> {

    public static final String TAG = "DEBUG-ASYNCTASK";
    private Context context;
    private String email;
    private String password;
    private List<Stock> stocks;
    private RecyclerView recyclerView;
    private boolean showStocks;
    private TextView oldQuantity;
    String typeOfTransaction;
    String quantity;
    String ticker;
    private TextView balanceTextView;

    public ApiCallAsyncTask(Context context, String email, String password, RecyclerView recyclerView, boolean showStocks, TextView oldQuantity, String typeOfTransaction,
                            String quantity, String ticker,TextView  balanceTextView){
        this.context = context;
        this.email = email;
        this.password = password;
        this.recyclerView = recyclerView;
        this.showStocks = showStocks;
        this.oldQuantity = oldQuantity;
        this.typeOfTransaction = typeOfTransaction;
        this.quantity = quantity;
        this.ticker = ticker;
        this.balanceTextView = balanceTextView;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String token = "";

        try {
            URL url = new URL("http://fesamm.herokuapp.com/api/auth/login");

            JSONObject postParameters = new JSONObject();

            postParameters.put("email", this.email);
            postParameters.put("password", this.password);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream outputStream = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(getPostDataString(postParameters));
            writer.flush();
            writer.close();
            outputStream.close();

            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while((line = input.readLine()) != null){
                response.append(line);
            }
            input.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            token = jsonResponse.getString("access_token");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            token = "Invalid credentials";
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return token;
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
                e.printStackTrace();
            }

        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String token){
        if(token.equals("Invalid credentials")) {
            // Do something when the credentials are invalid/ when the credentials cannot be used to generate a token
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.invailidCredMsg).setPositiveButton(R.string.goSettings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PortfolioActivity.portfolioActivity.finish();
                    Intent intent = new Intent(context, SettingsActivity.class);
                    context.startActivity(intent);
                }
            }).setNegativeButton(R.string.leavePortfolio, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PortfolioActivity.portfolioActivity.finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            SharedPreferences prefs = this.context.getSharedPreferences("com.dawson.fesamm.preference", this.context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("token", token);

            editor.commit();

            List<Stock> stocks = new ArrayList<>();

            if(showStocks) {
                GetStocksAsyncTask stocksTask = new GetStocksAsyncTask(token, stocks, context, this.recyclerView);
                stocksTask.execute();
                GetMoneyLeftAsincTask GetMoneyLeftAsincTask = new GetMoneyLeftAsincTask(token, context, this.balanceTextView);
                GetMoneyLeftAsincTask.execute();

            }
            else{
                BuySellTask task = new BuySellTask(oldQuantity, context);
                task.execute(this.typeOfTransaction, this.quantity, this.ticker, token);
            }

        }
    }
}
