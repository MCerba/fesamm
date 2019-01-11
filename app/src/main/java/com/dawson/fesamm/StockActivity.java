package com.dawson.fesamm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dawson.fesamm.Data.FetchStock;
import com.dawson.fesamm.Data.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohamed Hamza
 */
public class StockActivity extends MenuActivity {

    private EditText inputStock;
    private ListView tickerList;
    private ArrayList<String> myArrayList = new ArrayList<>();
    private ArrayAdapter<String> myArrayAdapter;
    private Button addBtn;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        //Get layout elements
        nextBtn = (Button) findViewById(R.id.nextBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        inputStock = (EditText) findViewById(R.id.inputStock);
        tickerList = (ListView) findViewById(R.id.tickerList);

        //Restore the ArrayList of added Tickers
        if(savedInstanceState != null){
            myArrayList = savedInstanceState.getStringArrayList("tickerList");

            //By default the 'next' button is disabled. So if the user rotates the device, check if
            //the ArrayList is not empty, if it's not empty (the user have selected tickers) enable
            //the 'next' button
            if(!myArrayList.isEmpty())
                nextBtn.setEnabled(true);
        }

        //Check the input, if the input is not empty enable the add button
        checkForInput();

        //Create an ArrayAdapter to list the chosen tickers
        myArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myArrayList);
        tickerList.setAdapter(myArrayAdapter);

        //Show an Alert Dialog when the user clicks on a ticker to be able to delete it
        tickerList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        showAlertDialog(String.valueOf(parent.getItemAtPosition(position)));
                    }
                }
        );

    } //onCreate()

    /**
     * Get the input (ticker) and add it to the ListView
     * @param v
     */
    public void addTicker(View v){
        //Get the input (ticker)
        String ticker = inputStock.getText().toString().trim();

        //Add the ticker to the ArrayList and notify the ArrayAdapter to show the ticker
        myArrayList.add(ticker);
        myArrayAdapter.notifyDataSetChanged();

        //Clear the input
        inputStock.getText().clear();

        //Enable the next button when a ticker is added
        nextBtn.setEnabled(true);

    } //addTicker()

    /**
     * This is a handler of the next button. When the user clicks the button 'next', open the ChosenStockActivity
     * @param v
     */
    public void showChosenTickers(View v){
        //Clear the input
        inputStock.getText().clear();

        //Create an intent to the ChosenStockActivity passing to it the List of selected tickers
        Intent intent = new Intent(StockActivity.this, ChosenStockActivity.class);
        intent.putExtra("tickerList", myArrayList);
        startActivity(intent);

    } //showChosenTickers()

    /**
     * This method adds a listener to the EditText that checks if the text is empty or not. If the
     * input is not empty enable the add button. Otherwise keep the add button disabled
     */
    private void checkForInput(){
        inputStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //If the user chose less than 5 tickers, than check if the input is empty of not.
                //If the input is not empty than enable the add button
                if(myArrayList.size() != 5) {
                    String input = inputStock.getText().toString().trim();
                    addBtn.setEnabled(!input.isEmpty());
                }else
                    addBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    } //checkForInput()

    /**
     * When the user clicks on a chosen ticker, this method creates an AlertDialog giving the user
     * the option to either keep the ticker or remove the ticker
     * @param selectedTicker
     */
    private void showAlertDialog(final String selectedTicker){
        AlertDialog.Builder builder= new AlertDialog.Builder(StockActivity.this);
        builder.setMessage("Are you sure you want to remove "+selectedTicker+ "?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myArrayList.remove(selectedTicker);
                        myArrayAdapter.notifyDataSetChanged();

                        //If the last ticker is removed, disable the next button
                        if(myArrayList.size()==0)
                            nextBtn.setEnabled(false);
                    }
                }).setNegativeButton("Cancel", null);
        AlertDialog alert = builder.create();
        alert.show();

    } //showAlertDialog()

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        //Save the ArrayList of tickers
        savedInstanceState.putStringArrayList("tickerList", myArrayList);

    } //onSaveInstanceState()

} //StockActivity class
