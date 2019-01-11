package com.dawson.fesamm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dawson.fesamm.Data.Stock;

public class StockInformationActivity extends MenuActivity {
    private TextView tickerText;
    private TextView companyText;
    private TextView priceText;
    private TextView currencyText;
    private TextView exchangeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_information);

        //Get layout elements
        tickerText = (TextView) findViewById(R.id.tickerText);
        companyText = (TextView) findViewById(R.id.companyText);
        priceText = (TextView) findViewById(R.id.priceText);
        currencyText = (TextView) findViewById(R.id.currencyText);
        exchangeText = (TextView) findViewById(R.id.exchangeText);

        //Get the Stock object sent by the ChosenStockActivity and display its information
        showInformation((Stock)getIntent().getSerializableExtra("info"));
    }

    /**
     * This method shows the information of the passed Stock
     * @param stock
     */
    private void showInformation(Stock stock){
        tickerText.setText(stock.getTicker());
        companyText.setText(stock.getCompanyName());
        priceText.setText(stock.getPrice());
        currencyText.setText(stock.getCurrencyType());
        exchangeText.setText(stock.getStockExchange());
    }
}
