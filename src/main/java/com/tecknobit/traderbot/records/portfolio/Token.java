package com.tecknobit.traderbot.records.portfolio;

import org.json.JSONObject;

/**
 * The {@code Token} class defines base methods for asset and coin objects. <br>
 * These methods allow to use inheritance object more efficiently.
 *
 * @author Tecknobit N7ghtm4r3
 **/
public class Token {

    /**
     * {@code assetIndex} is instance that memorizes index of token es. BTC
     * **/
    protected final String assetIndex;

    /**
     * {@code assetName} is instance that memorizes full name of token es Bitcoin
     * **/
    protected final String assetName;

    /**
     * {@code quantity} is instance that memorizes value of quantity bought for this token es 1
     * **/
    protected double quantity;

    /** Constructor to init {@link Cryptocurrency}
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     * **/
    public Token(String assetIndex, String assetName, double quantity) {
        if(assetIndex == null || assetIndex.isEmpty())
            throw new IllegalArgumentException("Asset index must contains characters");
        this.assetIndex = assetIndex;
        if (assetName == null || assetName.isEmpty())
            throw new IllegalArgumentException("Asset name must contains characters");
        this.assetName = assetName;
        if (quantity < 0)
            throw new IllegalArgumentException("Balance value cannot be less than 0");
        this.quantity = quantity;
    }

    /**
     * Method to get {@link #assetIndex} instance <br>
     * Any params required
     *
     * @return {@link #assetIndex} instance as {@link String}
     **/
    public String getAssetIndex() {
        return assetIndex;
    }

    /**
     * Method to get {@link #assetName} instance <br>
     * Any params required
     *
     * @return {@link #assetName} instance as {@link String}
     **/
    public String getAssetName() {
        return assetName;
    }

    /**
     * Method to get {@link #quantity} instance <br>
     * Any params required
     *
     * @return {@link #quantity} instance as double
     **/
    public double getQuantity() {
        return quantity;
    }

    /**
     * Method to set {@link #quantity}
     *
     * @param quantity: value of quantity bought for this token
     * @throws IllegalArgumentException when quantity value is less than 0
     **/
    public void setQuantity(double quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Balance value cannot be less than 0");
        this.quantity = quantity;
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
