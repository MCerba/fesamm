package com.dawson.fesamm;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class LoanCalcActivity extends MenuActivity {
    TextView finalCreditAmountTextView, monthsPaidTextView, totalInterestTextView;
    EditText loanAmountEditText, yearlyRateEditText, minimumMonthlyPaymentEditText;
    Spinner termOfLoanSpinner;
    FloatingActionButton fab;
    RecyclerView contactRecyclerView;
    String emailContent = "Calculation was not done.";
    Boolean alreadyCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calc);
        contactRecyclerView = (RecyclerView) findViewById(R.id.contacts_view);
        this.fab = findViewById(R.id.fab);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResultByEmail();
            }
        });

        loanAmountEditText = (EditText) findViewById(R.id.loanAmount);
        yearlyRateEditText = (EditText) findViewById(R.id.yearlyRate);
        minimumMonthlyPaymentEditText = (EditText) findViewById(R.id.minimum_monthly_amount);


        finalCreditAmountTextView = (TextView) findViewById(R.id.finalCreditAmount);
        monthsPaidTextView = (TextView) findViewById(R.id.totalPayment);
        totalInterestTextView = (TextView) findViewById(R.id.totoalInterst);

        termOfLoanSpinner = (Spinner) findViewById(R.id.termOfLoan);
        changeFloatingButton(false);
        if (savedInstanceState != null) restoreState(savedInstanceState);
    }


    /**
     * This method create a dialogFragment and pass email to it
     */
    private void sendResultByEmail() {
        Bundle data = new Bundle();//create bundle instance
        data.putString("emailContent", this.emailContent);//put string to pass with a key value
        final ContactsDialogFragment contactsDialogFragment = new ContactsDialogFragment();
        contactsDialogFragment.setArguments(data);//Set bundle data to fragment
        contactsDialogFragment.show(getFragmentManager(), "dialog");

    }

    /**
     * This method make calculation depending on intering data
     * It calculates Loan amount after intered time (in years)
     * end how many months do we need to pay all loan
     * This method change the message which can be sended
     *
     * @param v View
     */
    public void calculate(View v) {
        if (isValidData()) {
            double loanAmount = Double.parseDouble(loanAmountEditText.getText().toString());
            int termOfLoan = parseTermOfLoanSpinner();
            double yearlyRate = Double.parseDouble(yearlyRateEditText.getText().toString());
            double minimumMonthlyPayment = Double.parseDouble(minimumMonthlyPaymentEditText.getText().toString());

            LoanCalculator lc = new LoanCalculator(loanAmount, termOfLoan, minimumMonthlyPayment, yearlyRate);
            lc.calculate();
            finalCreditAmountTextView.setText(String.format("$%.2f", lc.getLoanAmount()));
            monthsPaidTextView.setText(String.format("%d months", (int) lc.getMonthspayed()));
            totalInterestTextView.setText(String.format("$%.2f", lc.getTotalInterest()));
            if (lc.getLoanAmount() > 0) {
                this.emailContent = "Initial amount of credit is $" + loanAmountEditText.getText().toString() + ". After "
                        + lc.getNumberOfYears() + "years, if you will pay monthly $" + minimumMonthlyPaymentEditText.getText().toString()
                        + " your credit amount will be $" + finalCreditAmountTextView.getText().toString() + ".";
            } else {
                this.emailContent = "Initial amount of credit is $" + loanAmountEditText.getText().toString() + ". After "
                        + monthsPaidTextView.getText().toString() + " months, if you will pay monthly $" + minimumMonthlyPaymentEditText.getText().toString()
                        + " your will pay all your credit.";
            }
            changeFloatingButton(true);
            this.alreadyCalculated = true;
        }
    }  //calculate()

    /**
     * This method clear all entered data an results
     *
     * @param v View
     */
    public void clear(View v) {
        loanAmountEditText.setText("");
        yearlyRateEditText.setText("");
        finalCreditAmountTextView.setText("");
        monthsPaidTextView.setText("");
        totalInterestTextView.setText("");
        minimumMonthlyPaymentEditText.setText("");
        changeFloatingButton(false);
        this.alreadyCalculated = false;
        termOfLoanSpinner.setSelection(0);

    }  //clear()

    /**
     * This method do the data validation
     *
     * @return thrue if data is valid
     * false if not
     */
    public boolean isValidData() {
        if (loanAmountEditText.getText().toString().isEmpty()
                || yearlyRateEditText.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * This methos get number of years from termOfLoanSpinner
     *
     * @return Integer number of years
     */
    private Integer parseTermOfLoanSpinner() {
        Integer retVal;
        try {
            retVal = Integer.parseInt(termOfLoanSpinner.getSelectedItem().toString().substring(0, 2));
        } catch (NumberFormatException nfe) {
            retVal = Integer.parseInt(termOfLoanSpinner.getSelectedItem().toString().substring(0, 1));
        }
        return retVal;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save UI state from to the savedInstanceState Bundle.
        savedInstanceState.putString("monthlyPayment", finalCreditAmountTextView.getText().toString());
        savedInstanceState.putString("monthsPaid", monthsPaidTextView.getText().toString());
        savedInstanceState.putString("totalInterest", totalInterestTextView.getText().toString());
        savedInstanceState.putBoolean("alreadyCalculated", this.alreadyCalculated);


    }

    /**
     * This method restore state from given bundle
     * state = value of finalCreditAmountTextView, monthsPaidTextView, totalInterestTextView
     * and alreadyCalculated
     *
     * @param savedInstanceState - bundle for restoring state of current activity
     */
    public void restoreState(Bundle savedInstanceState) {
        // Restore UI state from the savedInstanceState.
        String monthlyPayment = savedInstanceState.getString("monthlyPayment");
        String monthsPaid = savedInstanceState.getString("monthsPaid");
        String totalInterest = savedInstanceState.getString("totalInterest");
        this.alreadyCalculated = savedInstanceState.getBoolean("alreadyCalculated");
        changeFloatingButton(this.alreadyCalculated);
        if (!monthlyPayment.equals("")) finalCreditAmountTextView.setText(monthlyPayment);
        if (!monthsPaid.equals("")) monthsPaidTextView.setText(monthsPaid);
        if (!totalInterest.equals("")) totalInterestTextView.setText(totalInterest);

    }

    /**
     * Yhis method change state of floating action button
     *
     * @param enable - boolean
     *               if true - make button enable
     *               if false - make button disable
     */
    public void changeFloatingButton(boolean enable) {
        if (enable == true) {
            fab.setEnabled(true);
            fab.setClickable(true);
            fab.setAlpha(1f);
        } else {
            fab.setEnabled(false);
            fab.setClickable(false);
            fab.setAlpha(0.3f);
        }
    }
}
