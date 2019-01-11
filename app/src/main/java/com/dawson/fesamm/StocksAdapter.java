package com.dawson.fesamm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dawson.fesamm.Data.Stock;

import java.util.List;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.StocksViewHolder> {
    private List<Stock> stocks;
    private Context context;

    private LayoutInflater mInflater;

    class StocksViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView stockView;
        public StocksViewHolder(View v) {
            super(v);
            stockView = v.findViewById(R.id.stockView);
        }
    }

    public StocksAdapter (List<Stock> stocks, Context context) {
        this.stocks = stocks;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public StocksViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View v = mInflater.inflate(R.layout.stock_item, parent, false);

        StocksViewHolder vh = new StocksViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder (final StocksViewHolder holder, final int position) {
        if (stocks.size() > 0 && stocks != null) {

            Stock currentStock = stocks.get(position);

            final String compName = currentStock.getCompanyName();
            final String ticker = currentStock.getTicker();
            final int quantity = currentStock.getQuantity();
            final String currency = currentStock.getCurrencyType();
            final String price = currentStock.getPrice();

            //final String name = currentStock.getCompanyName();

            holder.stockView.setText(currentStock.getCompanyName());

            holder.stockView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewStockActivity.class);
                    intent.putExtra("companyName", compName);
                    intent.putExtra("tickerSymbol", ticker);
                    intent.putExtra("price", price);
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("currency", currency);

                    context.startActivity(intent);
                }
            });
        } else {
            holder.stockView.setText("No stocks");
        }





    }

    @Override
    public int getItemCount() {
        return this.stocks.size();
    }
}
