package com.dawson.fesamm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This class is a Fragment which will display a random financial hint
 * @author Alpha Kabine Kaba and Mohamed Hamza
 */
public class FinancialHintsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_financial_hints, container, false);
        TextView hint = inflatedView.findViewById(R.id.hint);

        // Set the text to the financial hint saved in the FinancialHintsFragment arguments
        if(this.getArguments() != null)
            hint.setText(this.getArguments().getString("randomHint"));
        else
            hint.setText("");

        return inflatedView;
    }

    /**
     * This method is used to create a new instance of FinancialHintsFragment which will have the financial hint as argument
     * @param randomHint - The financial hint to save in the arguments
     * @return myFragment - FinancialHintsFragment object
     */
    public static FinancialHintsFragment newInstance(String randomHint){
        FinancialHintsFragment myFragment = new FinancialHintsFragment();
        Bundle args = new Bundle();
        args.putString("randomHint", randomHint);
        myFragment.setArguments(args);

        return myFragment;
    }
}