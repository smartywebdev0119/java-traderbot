package com.tecknobit.traderbot.records.portfolio;

import org.json.JSONObject;

import static com.tecknobit.apimanager.Tools.Trading.TradingTools.roundValue;
import static com.tecknobit.apimanager.Tools.Trading.TradingTools.textualizeAssetPercent;

/**
 * The {@code MarketCoin} class defines market coin object
 * This object is useful for format a market coin asset where market values constantly change
 *
 * @author Tecknobit N7ghtm4r3
 **/
public class MarketCoin {

    /**
     * {@code lastPrice} is instance that memorizes last inserted price of cryptocurrency and is constantly refreshed
     **/
    private double lastPrice;

    /**
     * {@code priceChangePercent} is instance that memorizes previous day percent gap of trend of cryptocurrency
     **/
    private double priceChangePercent;

    /**
     * Constructor to init {@link MarketCoin}
     *
     * @param lastPrice:          last inserted price of cryptocurrency
     * @param priceChangePercent: previous day percent gap of trend of cryptocurrency
     **/
    public MarketCoin(double lastPrice, double priceChangePercent) {
        this.lastPrice = lastPrice;
        this.priceChangePercent = priceChangePercent;
    }

    /**
     * Method to get {@link #lastPrice} instance <br>
     * Any params required
     *
     * @return {@link #lastPrice} instance as double
     **/
    public double getLastPrice() {
        return lastPrice;
    }

    /**
     * Method to set {@link #lastPrice}
     *
     * @param lastPrice: last price value
     * @throws IllegalArgumentException when last price value is less than 0
     **/
    public void setLastPrice(double lastPrice) {
        if (lastPrice < 0)
            throw new IllegalArgumentException("Last price cannot be less than 0");
        this.lastPrice = lastPrice;
    }

    /**
     * This method is used get {@link #lastPrice} instance
     *
     * @param decimals: number of decimal digits es. 2
     * @return {@link #lastPrice} instance formatted as 21.22
     * @throws IllegalArgumentException if decimalDigits is negative
     **/
    public double getLastPrice(int decimals) {
        return roundValue(lastPrice, decimals);
    }

    /**
     * Method to get {@link #priceChangePercent} instance <br>
     * Any params required
     *
     * @return {@link #priceChangePercent} instance as double
     **/
    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    /**
     * Method to set {@link #priceChangePercent}
     *
     * @param priceChangePercent: previous day percent gap of trend of cryptocurrency
     * @throws IllegalArgumentException when price change percent value is less than 0
     **/
    public void setPriceChangePercent(double priceChangePercent) {
        if (priceChangePercent < -100)
            throw new IllegalArgumentException("Price change percent cannot be less than -100");
        this.priceChangePercent = priceChangePercent;
    }

    /**
     * This method is used to get {@link #priceChangePercent} instance
     *
     * @param decimals: number of decimal digits es. 2
     * @return {@link #priceChangePercent} formatted as 1.65 or -1.65
     **/
    public double getPriceChangePercent(int decimals) {
        return roundValue(priceChangePercent, decimals);
    }

    /**
     * This method is used to format like a {@link String} object {@link #priceChangePercent}.<br>
     * Any params required
     *
     * @return {@link #priceChangePercent} formatted as +1.653% or -1.6563% as {@link String}
     **/
    public String getTextPriceChangePercent() {
        return textualizeAssetPercent(priceChangePercent);
    }

    /**
     * This method is used to format like a {@link String} object {@link #priceChangePercent}.<br>
     *
     * @param decimals: number of decimal digits es. 2
     * @return {@link #priceChangePercent} formatted as +1.65% or -1.65% as {@link String}
     **/
    public String getTextPriceChangePercent(int decimals) {
        return textualizeAssetPercent(roundValue(priceChangePercent, decimals));
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
