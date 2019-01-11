package com.dawson.fesamm;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * AsyncTask that takes care of displaying all the currencies
 */
public class DisplayCurrencies extends AsyncTask<Void, Void, List<String>> {

    Context context;
    ForeignExchangeActivity foreignExchangeActivity;
    boolean from;
    TextView fromCurrency;
    TextView toCurrency;
    TextView fromAmount;
    TextView toAmount;
    TextView fromCurrencyName;
    TextView toCurrencyName;

    public DisplayCurrencies(Context context, ForeignExchangeActivity foreignExchangeActivity, boolean from, TextView fromCurrency, TextView toCurrency,
                             TextView fromAmount, TextView toAmount, TextView fromCurrencyName, TextView toCurrencyName){
        super();
        this.context = context;
        this.foreignExchangeActivity = foreignExchangeActivity;
        this.from = from;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.fromCurrencyName = fromCurrencyName;
        this.toCurrencyName = toCurrencyName;
    }

    /**
     * This method returns a list of all the currencies
     * @param voids
     * @return
     */
    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> currenciesList = new ArrayList<>();
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

            SortedSet<String> currencies = new TreeSet<>();
            Iterator<String> keys = rates.keys();
            while(keys.hasNext()){
                currencies.add(keys.next());
            }

            currenciesList.add("Choose a currency symbol ...");

            for(String currency : currencies){
                currenciesList.add(currency);
            }
        }
        catch(IOException ie){
        }
        catch(JSONException jse){
        }

        return currenciesList;
    }

    /**
     * This method displays an alert box that contains a spinner from which a currency can be chosen
     * @param currencies - list of all the currencies
     */
    @Override
    protected void onPostExecute(List<String> currencies){
        String[] currenciesSymbols = new String[currencies.size()];
        currencies.toArray(currenciesSymbols);

        AlertDialog.Builder alert = new AlertDialog.Builder(foreignExchangeActivity);
        View currenciesDialog = LayoutInflater.from(context).inflate(R.layout.currencies_dialog_spinner, null);
        alert.setTitle("Currencies");
        final Spinner spinner = (Spinner) currenciesDialog.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(foreignExchangeActivity, android.R.layout.simple_spinner_item, currenciesSymbols);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        alert.setPositiveButton("Change currency", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!spinner.getSelectedItem().toString().equals("Choose a currency symbol ...")){
                    Toast.makeText(foreignExchangeActivity, "Currency changed to " + spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    if(from) {
                        fromCurrency.setText(spinner.getSelectedItem().toString());
                        DisplayCurrencyName task = new DisplayCurrencyName(from, fromCurrencyName, toCurrencyName);
                        task.execute(fromCurrency.getText().toString());
                    }
                    else {
                        toCurrency.setText(spinner.getSelectedItem().toString());
                        DisplayCurrencyName task = new DisplayCurrencyName(from, fromCurrencyName, toCurrencyName);
                        task.execute(toCurrency.getText().toString());
                    }
                    fromAmount.setText("");
                    toAmount.setText("");
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setView(currenciesDialog);
        alert.create().show();
    }
}
