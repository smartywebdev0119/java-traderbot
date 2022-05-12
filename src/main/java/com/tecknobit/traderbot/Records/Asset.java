package com.tecknobit.traderbot.Records;

public final class Asset extends Token {
    private double balance;
    private final String currency;

    public Asset(String assetIndex, String assetName, double quantity, double balance, String currency) {
        super(assetIndex, assetName, quantity);
        if(balance < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        this.balance = balance;
        if(currency == null || currency.isBlank())
            throw new IllegalArgumentException("Currency must contains characters");
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if(balance < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

}
