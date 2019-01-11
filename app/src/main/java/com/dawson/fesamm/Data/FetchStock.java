package com.dawson.fesamm.Data;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchStock extends AsyncTask<String, Void, String>{

    private List<Stock> stocks;
    private String tickers = "";
    private String foundTicker;
    private TextView foundTikerText;
    private ArrayAdapter<String> myArrayAdapter;
    private ArrayList<String> myArrayList;
    private HttpURLConnection conn;

    /**
     * Constructor
     * @param foundTikerText is the TextView found in the activity_chosen_stock.xml
     * @param myArrayAdapter is the ArrayAdapter passed by the ChosenStockActivity which will allow
     *                       to show the found ticker
     * @param myArrayList is the empty ArrayList that is passed by the ChosenStockActivity which will be
     *                    used in the onPostExecute to add the tickers. The myArrayList works with
     *                    myArrayAdapter to add the tickers to the ArrayList and show them with the
     *                    ArrayAdapter
     * @param stocks is the empty List of Stock passed by the ChosenStockActivity which will contain
     *              Stock objects of the found stocks. It will be used inside ChosenStockActivity
     */
    public FetchStock(TextView foundTikerText, ArrayAdapter<String> myArrayAdapter, ArrayList<String> myArrayList, List<Stock> stocks){
        this.foundTikerText = foundTikerText;
        this.myArrayAdapter = myArrayAdapter;
        this.myArrayList = myArrayList;
        this.stocks = stocks;
    }

    @Override
    protected String doInBackground(String... ticker) {
        try {
            //This method will create a ticker string that will be passed to the url
            createTickerString(ticker);

            //createConnection will create the Url and create a connection to access the ticker information
            createConnection();

            //This method retrieves the necessary information from the api
            readTikcerInformation(conn);

        } catch (MalformedURLException e) {
            return "Unable to Retrieve Data. Please Try Again Later!";
        } catch (IOException e) {
            return "Unable to Retrieve Data. Please Try Again Later!";
        } catch (JSONException e) {
            return "No Stocks Found!";
        } finally {
            if(conn != null){
                try {
                    conn.disconnect();
                }catch (IllegalStateException ignore){}
            }
        }
        return foundTicker + " Found Stocks";
    }

    @Override
    protected void onPostExecute(String str) {

        //Show the number of stocks found
        foundTikerText.setText(str);
        for(int i=0; i<stocks.size(); i++){
            //For every found stock, add the ticker to the ArrayList and update the ArrayAdapter to
            //show the found ticker
            myArrayList.add(stocks.get(i).getTicker());
            myArrayAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This method loops over the var args of passed tickers and creates a String that will be passed
     * to the Url
     * @param ticker
     */
    private void createTickerString(String... ticker){
        //Put all the tickers in a String so it can be added to the url
        for(String temp : ticker){
            tickers += temp+",";
        }

        //Remove the last ',' from the String
        tickers = tickers.substring(0, tickers.length() - 1);
    }

    /**
     * This method creates the Url to retrieve the information from the api and makes a connection
     * @return HttpURLConnection conn
     * @throws IOException
     */
    private void createConnection() throws IOException {
        //Add to the URL the string of the selected tickers
        URL url = new URL("https://www.worldtradingdata.com/api/v1/stock?symbol="+tickers+"&api_token=MvYBAarExo6Nf1oj6q7Ta6KT6mH7nLtgxr4Zw6xkJwhLwslp2DbVZoIBfGqx");

        conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(15000);

        conn.connect();

        int response = conn.getResponseCode();

        if(response != HttpURLConnection.HTTP_OK)
            throw new IOException();
    }

    /**
     * This method retrieves the necessary information (the number of stocks found and the information
     * about each stock found) from the api. It then calls the createStocks method to create a Stock
     * object and add it to the stocks List
     * @param conn
     * @throws IOException
     * @throws JSONException
     */
    private void readTikcerInformation(HttpURLConnection conn) throws IOException, JSONException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        JSONObject jsonObj = new JSONObject(buffer.readLine());

        //Get the number of found stocks
        foundTicker = jsonObj.getString("symbols_returned");

        //Get a Collection of all the stocks found
        JSONArray dataArray = jsonObj.getJSONArray("data");

        //Loop through the Collection a create a Stock Object
        for(int i=0; i<dataArray.length(); i++){
            createStocks(dataArray.getJSONObject(i).getString("symbol"),
                    dataArray.getJSONObject(i).getString("name"),
                    dataArray.getJSONObject(i).getString("currency"),
                    dataArray.getJSONObject(i).getString("price"),
                    dataArray.getJSONObject(i).getString("stock_exchange_short"));
        }
    }

    /**
     * This method creates a Stock object and adds it to the stocks List
     * @param ticker
     * @param name
     * @param currency
     * @param price
     * @param exchange
     */
    private void createStocks(String ticker, String name, String currency, String price, String exchange){
        Stock s = new Stock();
        s.setTicker(ticker);
        s.setCompanyName(name);
        s.setCurrencyType(currency);
        s.setPrice(price);
        s.setStockExchange(exchange);
        stocks.add(s);
    }
}
