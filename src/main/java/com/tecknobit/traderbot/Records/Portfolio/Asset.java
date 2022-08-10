package com.tecknobit.traderbot.Records.Portfolio;

/**
 * The {@code Asset} class defines Asset object. <br>
 * This object is useful for format user wallet asset.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class Asset extends Token {

    /**
     * {@code balance} is instance that memorizes balance for this asset and is constantly refreshed
     * **/
    private double balance;

    /**
     * {@code currency} is instance that memorizes currency for balance valuating es. EUR
     * **/
    private final String currency;

    /** Constructor to init {@link Asset}
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @param balance: balance for this asset
     * @param currency: currency for balance valuating es. EUR
     * @throws IllegalArgumentException if parameters range is not respected
     * **/
    public Asset(String assetIndex, String assetName, double quantity, double balance, String currency) {
        super(assetIndex, assetName, quantity);
        if(balance < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.balance = balance;
        if(currency == null || currency.isEmpty())
            throw new IllegalArgumentException("Currency must contains characters");
        else
            this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    /** Method to set {@link #balance}
     * @param balance: balance for this asset
     * @throws IllegalArgumentException when balance value is lesser than 0
     * **/
    public void setBalance(double balance) {
        if(balance < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "balance=" + balance +
                ", currency='" + currency + '\'' +
                ", assetIndex='" + assetIndex + '\'' +
                ", assetName='" + assetName + '\'' +
                ", quantity=" + quantity +
                '}';
    }

}
