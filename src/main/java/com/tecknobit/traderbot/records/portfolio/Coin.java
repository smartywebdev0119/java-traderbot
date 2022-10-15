package com.tecknobit.traderbot.records.portfolio;

import org.json.JSONObject;

/**
 * The {@code Coin} class defines Coin object. <br>
 * This object is useful for all traders routines and also useful as asset container
 *
 * @author Tecknobit N7ghtm4r3
 **/
public final class Coin extends Token {

    /**
     * {@code tradingEnabled} is instance that memorizes if this coin is available or not for trading or if is not bought yet.
     * **/
    private boolean tradingEnabled;

    /** Constructor to init {@link Asset}
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @param tradingEnabled: available or not for trading or if is not bought yet
     * **/
    public Coin(String assetIndex, String assetName, double quantity, boolean tradingEnabled) {
        super(assetIndex, assetName, quantity);
        this.tradingEnabled = tradingEnabled;
    }

    /**
     * Method to get {@link #tradingEnabled} instance <br>
     * Any params required
     *
     * @return {@link #tradingEnabled} instance as boolean
     **/
    public boolean isTradingEnabled() {
        return tradingEnabled;
    }

    /**
     * Method to set {@link #tradingEnabled} instance
     *
     * @param tradingEnabled: available or not for trading or if is not bought yet
     **/
    public void setTradingEnabled(boolean tradingEnabled) {
        this.tradingEnabled = tradingEnabled;
    }

    /**
     * Returns a string representation of the object <br>
     * Any params required
     *
     * @return a string representation of the object as {@link String}
     */
    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
