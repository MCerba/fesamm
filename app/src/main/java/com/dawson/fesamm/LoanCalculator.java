package com.dawson.fesamm;

import android.util.Log;

public class LoanCalculator {
    private static final String TAG = "LoanCalculator";
    private double loanAmount;
    private int numberOfYears;
    private double yearlyInterestRate;
    private double minimumMonthlyPayment;
    private double totalInterest;
    private double monthspayed;


    public LoanCalculator(double loanAmount,
                          int numberOfYears,
                          double minimumMonthlyPayment,
                          double yearlyInterestRate) {
        this.loanAmount = loanAmount;
        this.numberOfYears = numberOfYears;
        this.minimumMonthlyPayment = minimumMonthlyPayment;
        this.yearlyInterestRate = yearlyInterestRate;
        this.totalInterest = 0;
    }


    public double getLoanAmount() {
        return loanAmount;
    }

    /**
     * This method make calculation depending on the private data of the class
     */
    public void calculate() {

        double dailyInterestRate;
        int numberOfPayments;
        int totalDays;
        double dailyAmountCharged;
        double currentLoanAmount;
        //calculate the monthly payment
        dailyInterestRate = this.yearlyInterestRate / 36500;
        totalDays = this.numberOfYears * 365;

        int monthsToPay = this.numberOfYears * 12;
        currentLoanAmount = this.loanAmount;
        int monthsAlreadyPayed = 0;
        Log.d(TAG, "currentLoanAmount: " + currentLoanAmount);
        Log.d(TAG, "monthsToPay: " + monthsToPay);
        while (currentLoanAmount > this.minimumMonthlyPayment) {
            currentLoanAmount = passMonth(currentLoanAmount, dailyInterestRate, 30);
            currentLoanAmount -= this.minimumMonthlyPayment;
            monthsAlreadyPayed++;
            if (monthsAlreadyPayed == monthsToPay) {
                this.monthspayed = monthsAlreadyPayed;
                break;
            }
            Log.d(TAG, "currentLoanAmount: " + currentLoanAmount);
            Log.d(TAG, "monthsAlreadyPayed: " + monthsAlreadyPayed);
        }
        if (currentLoanAmount < this.minimumMonthlyPayment) {
            currentLoanAmount = 0;
            this.monthspayed = monthsAlreadyPayed + 1;
        }
        this.loanAmount = currentLoanAmount;
    }

    /**
     * This function calculate loan amount after a month
     *
     * @param initialLoan       amount in the beginning of the month
     * @param dailyInterestRate daily Interest Rate
     * @param totalDays         days in the month
     * @return double - loan amount after a month
     */
    private double passMonth(double initialLoan, double dailyInterestRate, double totalDays) {
        for (int i = 0; i < totalDays; i++) {
            initialLoan = initialLoan + initialLoan * dailyInterestRate;
            this.totalInterest += initialLoan * dailyInterestRate;
        }
        Log.d(TAG, "initialLoan: " + initialLoan);
        return initialLoan;
    }


    public int getNumberOfYears() {
        return numberOfYears;
    }

    public double getYearlyInterestRate() {
        return yearlyInterestRate;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setNumberOfYears(int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    public void setYearlyInterestRate(double yearlyInterestRate) {
        this.yearlyInterestRate = yearlyInterestRate;
    }

    public String toString() {
        return getLoanAmount() + "," + getNumberOfYears() + "," +
                getYearlyInterestRate();
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public double getMonthspayed() {
        return monthspayed;
    }
}
