package com.dawson.fesamm;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * This class is used for the currencies exchange component of the app
 * @author Alpha Kabine Kaba
 */
public class ForeignExchangeActivity extends MenuActivity {

    TextView one;
    TextView two;
    TextView three;
    TextView four;
    TextView five;
    TextView six;
    TextView seven;
    TextView eight;
    TextView nine;
    TextView zero;
    ImageButton delete;
    TextView dot;

    TextView fromCurrency;
    TextView fromCurrencyName;
    TextView toCurrency;
    TextView toCurrencyName;

    EditText fromAmount;
    EditText toAmount;

    ImageButton switchBtn;

    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_exchange);
        initializeViews();
        setNumbersClickListeners();

        if(savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            setBaseCurrency();
        }
    }

    /**
     * This method adds a digit to the amount to be converted
     * @param lastNumber - last digit entered
     */
    private void addNumber(String lastNumber){
        String fromAmountText = fromAmount.getText().toString();
        if(fromAmountText.length() <= 11) {
            if (fromAmountText.contains(".")) {
                if (!lastNumber.equals(".")) {
                    int dotPosition = fromAmountText.indexOf('.');
                    if (fromAmountText.length() <= dotPosition + 2) {
                        fromAmount.setText(fromAmountText + lastNumber);
                    }
                }
            }
            else{
                if (fromAmountText.isEmpty()) {
                    if (!lastNumber.equals("0")) {
                        if (lastNumber.equals("."))
                            fromAmount.setText("0" + lastNumber);
                        else
                            fromAmount.setText(fromAmountText + lastNumber);
                    }
                } else {
                    fromAmount.setText(fromAmountText + lastNumber);
                }
            }
        }

        fromAmountText = fromAmount.getText().toString();

        convert(fromAmountText, fromCurrency.getText().toString(), toCurrency.getText().toString());
    }

    /**
     * This method removes the last digit from the amount entered
     */
    private void removeLastNumber(){
        String fromAmountText = fromAmount.getText().toString();
        if(!fromAmountText.isEmpty()) {
            if(fromAmountText.length() == 1){
                fromAmount.setText("");
                toAmount.setText("");
            }
            else {
                fromAmount.setText(fromAmountText.substring(0, fromAmountText.length() - 1));
                fromAmountText = fromAmount.getText().toString();

                convert(fromAmountText, fromCurrency.getText().toString(), toCurrency.getText().toString());
            }
        }
    }

    /**
     * This method executes an AsyncTask that converts currencies
     * @param fromAmountText - amount to be converted
     * @param fromCurrencyText - currency to convert from
     * @param toCurrencyText - currency to convert to
     */
    private void convert(String fromAmountText, String fromCurrencyText, String toCurrencyText){
        if(!fromAmountText.isEmpty()) {
            Convert task = new Convert(toAmount);
            task.execute(fromAmountText, fromCurrencyText, toCurrencyText);
        }
    }

    /**
     * Set onClick listeners for the numbers
     */
    private void setNumbersClickListeners(){
        TextView[] numbers = {one, two, three, four, five, six, seven, eight, nine, zero, dot};

        for(TextView number : numbers){
            number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNumber(((TextView) v).getText().toString());
                }
            });
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLastNumber();
            }
        });
    }

    /**
     * Initialize all the views
     */
    private void initializeViews(){
        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.two);
        three = (TextView) findViewById(R.id.three);
        four = (TextView) findViewById(R.id.four);
        five = (TextView) findViewById(R.id.five);
        six = (TextView) findViewById(R.id.six);
        seven = (TextView) findViewById(R.id.seven);
        eight = (TextView) findViewById(R.id.eight);
        nine = (TextView) findViewById(R.id.nine);
        zero = (TextView) findViewById(R.id.zero);
        delete = (ImageButton) findViewById(R.id.delete);
        dot = (TextView) findViewById(R.id.dot);
        fromCurrency = (TextView) findViewById(R.id.fromCurrency);
        fromCurrencyName = (TextView) findViewById(R.id.fromCurrencyName);
        toCurrency = (TextView) findViewById(R.id.toCurrency);
        toCurrencyName = (TextView) findViewById(R.id.toCurrencyName);
        fromAmount = (EditText) findViewById(R.id.fromAmount);
        toAmount = (EditText) findViewById(R.id.toAmount);
        switchBtn = (ImageButton) findViewById(R.id.switchBtn);
    }

    /**
     * Calls a method to change the 'from' currency
     * @param v
     */
    public void currencyFromChange(View v){
        currencyChange(true);
    }

    /**
     * Calls a method to change the 'to' currency
     * @param v
     */
    public void currencyToChange(View v){
        currencyChange(false);
    }

    /**
     * This method executes an AsyncTask to change a currency
     * @param from
     */
    private void currencyChange(final boolean from){
        DisplayCurrencies task = new DisplayCurrencies(getApplicationContext(), ForeignExchangeActivity.this, from, fromCurrency,
                toCurrency, fromAmount, toAmount, fromCurrencyName, toCurrencyName);
        task.execute();
        if(from)
            updateCurrenciesNames(fromCurrency.getText().toString(), true);
        else
            updateCurrenciesNames(toCurrency.getText().toString(), false);
    }

    /**
     * This method retrieves the base currency from the SharedPreferences and then executes an AsyncTask to set the base currency
     */
    private void setBaseCurrency(){
        SharedPreferences prefs = getSharedPreferences("com.dawson.fesamm.preference", MODE_PRIVATE);
        String currency = (prefs.getString("currency", "").isEmpty()) ? "CAD" : prefs.getString("currency", "").toUpperCase().trim();
        RetrieveBaseCurrency task = new RetrieveBaseCurrency(fromCurrency, fromCurrencyName, toCurrencyName);
        task.execute(currency);
    }

    /**
     * This method executes an AsyncTask to update the currencies' names (i.e. United States Dollar / Guinean Franc)
     * @param currencySymbol - currency from which we want to retrieve the name
     * @param from - boolean to indicate if it is the 'from' or the 'to' currency
     */
    private void updateCurrenciesNames(String currencySymbol, boolean from){
        DisplayCurrencyName task = new DisplayCurrencyName(from, fromCurrencyName, toCurrencyName);
        task.execute(currencySymbol);
    }

    /**
     * This method switch the 2 currencies
     * @param v
     */
    public void switchCurrencies(View v){
        String tempFromCurrency = fromCurrency.getText().toString();
        String tempFromCurrencyName = fromCurrencyName.getText().toString();
        String tempFromAmount = fromAmount.getText().toString();

        fromCurrency.setText(toCurrency.getText().toString());
        fromCurrencyName.setText(toCurrencyName.getText().toString());
        fromAmount.setText(toAmount.getText().toString());

        toCurrency.setText(tempFromCurrency);
        toCurrencyName.setText(tempFromCurrencyName);
        toAmount.setText(tempFromAmount);

        convert(fromAmount.getText().toString(), fromCurrency.getText().toString(), toCurrency.getText().toString());
    }

    /**
     * Saves instance state
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("fromCurrency", fromCurrency.getText().toString());
        outState.putString("fromCurrencyName", fromCurrencyName.getText().toString());
        outState.putString("toCurrency", toCurrency.getText().toString());
        outState.putString("toCurrencyName", toCurrencyName.getText().toString());
        outState.putString("fromAmount", fromAmount.getText().toString());
        outState.putString("toAmount", toAmount.getText().toString());
    }

    /**
     * Restores instance state
     * @param outState
     */
    private void restoreState(Bundle outState){
        fromCurrency.setText(outState.getString("fromCurrency"));
        fromCurrencyName.setText(outState.getString("fromCurrencyName"));
        toCurrency.setText(outState.getString("toCurrency"));
        toCurrencyName.setText(outState.getString("toCurrencyName"));
        fromAmount.setText(outState.getString("fromAmount"));
        toAmount.setText(outState.getString("toAmount"));
    }
}