package com.dawson.fesamm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dawson.fesamm.Data.FetchStock;
import com.dawson.fesamm.Data.Stock;

import java.util.ArrayList;
import java.util.List;

public class ChosenStockActivity extends MenuActivity {

    private ArrayList<String> myArrayList;
    private ArrayList<String> myArrayList2 = new ArrayList<>();
    private ListView chosenTickersList;
    private ArrayAdapter<String> myArrayAdapter;
    private TextView foundTikcerText;
    private List<Stock> stocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_stock);

        //Get layout elements
        chosenTickersList = (ListView) findViewById(R.id.chosenTickersList);
        foundTikcerText = (TextView) findViewById(R.id.stocksFound);

        //Get the List of selected tickers
        myArrayList = getIntent().getExtras().getStringArrayList("tickerList");

        myArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myArrayList2);
        chosenTickersList.setAdapter(myArrayAdapter);

        //When the user clicks on a ticker, create an intent to the StockInformationActivity passing
        //to it the stock object which contains all the necessary information about the clicked ticker
        chosenTickersList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ChosenStockActivity.this, StockInformationActivity.class);
                        intent.putExtra("info", findClickedTikcerInformation(String.valueOf(parent.getItemAtPosition(position))));
                        startActivity(intent);
                    }
                }
        );

        //Check for network connection
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            //Creating a FetchStock object by passing to it the TextView, an array adapter,
            //an ArrayList of String, a List of Stock
            FetchStock fetch = new FetchStock(foundTikcerText, myArrayAdapter, myArrayList2, stocks);
            fetch.execute(myArrayList.toArray(new String[myArrayList.size()]));
        }else{
            foundTikcerText.setText("No Network Connection Available!");
        }

    } //onCreate()

    /**
     * This method search inside the List of Stock to find the selected ticker and return the Stock
     * @param selected
     * @return
     */
    private Stock findClickedTikcerInformation(String selected){
        for(int i=0; i<stocks.size(); i++){
            if(selected.equalsIgnoreCase(stocks.get(i).getTicker()))
                return stocks.get(i);
        }
        return null;
    } //findClickedTikcerInformation()

} //ChosenStockActivity class
