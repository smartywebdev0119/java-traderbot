package com.tecknobit.traderbot.routines.interfaces;

import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.traderbot.records.portfolio.Asset;
import com.tecknobit.traderbot.records.portfolio.Coin;
import com.tecknobit.traderbot.records.portfolio.MarketCoin;
import com.tecknobit.traderbot.records.portfolio.Transaction;
import com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.Side;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.traderbot.routines.interfaces.TraderCoreRoutines.FormatResponse.*;

/**
 * The {@code TraderCoreRoutines} class defines base routines methods for all traders. <br>
 * These routines allow the different traders to do some operations about wallet info and make trading operations.
 * @author Tecknobit N7ghtm4r3
 * **/
public abstract class TraderCoreRoutines {

    /**
     * {@code transactions} is a list of transactions made by user account plus transactions made by a
     * trader on a single symbol.
     *
     * @implNote every time that is call {@link #getTransactionsList(java.lang.String, boolean)}
     * or {@link #getTransactionsList(java.lang.String, java.lang.String, boolean)} {@code transactions}
     * instance will be clear only if currency used is different from previous time or is time to refresh different
     * data or user force refresh of this list. <br>
     * This list is {@link ArrayList} of custom object {@link Transaction} give by {@code TraderBot} library.
     **/
    protected ArrayList<Transaction> transactions;

    /**
     * {@code allTransactions} is a list of all transactions made by user account plus transactions made by a
     * trader.
     * @implNote every time that is call {@link #getAllTransactions(boolean)} <br>
     * or {@link #getTransactionsList(java.lang.String, boolean)} {@code allTransactions}
     * instance will be clear only if currency used is different from previous time or is time to refresh different
     * data or user force refresh of this list. <br>
     * This list is {@link ArrayList} of custom object {@link Transaction} give by {@code TraderBot} library.
     * **/
    protected ArrayList<Transaction> allTransactions;

    /**
     * {@code coins} is a map that contains assets of base user wallet and all assets that a trader will trade
     * during his operation.
     * @implSpec this map has as key the asset index (es. BTC) as {@link String} and has as value custom object
     * {@link Coin} give by {@code TraderBot} library.
     * **/
    protected HashMap<String, Coin> coins;

    /**
     * {@code quoteCurrencies} is a list of quote currencies used in past orders and current active
     * user wallets ready to make order es (USD or EUR).
     * @implNote methods that allow to operate on list are: {@link #setQuoteCurrencies(ArrayList)},
     * {@link #insertQuoteCurrency(java.lang.String)}, {@link #removeQuoteCurrency(java.lang.String)} and
     * {@link #insertQuoteCurrency(java.lang.String)}
     * **/
    protected ArrayList<String> quoteCurrencies;

    /**
     * {@code lastTransactionCurrency} is instance that memorizes currency used in transactions list methods. <br>
     * Every time that those methods are called if is different from new currency inserted will be replaced with that new currency.
     * **/
    protected String lastTransactionCurrency;

    /**
     * {@code assets} is a list of custom object {@link Asset} give by {@code TraderBot} library.
     * @implNote is used for fetch user wallet and its cryptocurrencies.
     * **/
    protected ArrayList<Asset> assets;

    /**
     * {@code lastBalanceCurrency} is instance that memorizes currency used in balance methods. <br>
     * Every time that those methods are called if is different from new currency inserted will be replaced with that new currency.
     * **/
    protected String lastBalanceCurrency;

    /**
     * {@code lastAssetCurrency} is instance that memorizes currency used in asset methods. <br>
     * Every time that those methods are called if is different from new currency inserted will be replaced with that new currency.
     * **/
    protected String lastAssetCurrency;

    /**
     * {@code refreshTime} is instance that memorizes time to refresh last prices.
     *
     * @implNote this param can customize with {@link #setRefreshTime(int)}
     * @implSpec valid values are from 5 second to 3600 seconds other will generate an {@link Exception}
     **/
    protected int refreshTime;

    /**
     * {@code lastPricesRefresh} is instance that memorizes last time that prices are updated.
     * @implNote when is called {@link #isRefreshTime()} if timestamp of the call minus
     * {@code lastPricesRefresh} is bigger or equal than {@link #refreshTime} execute refresh.
     * **/
    protected long lastPricesRefresh;

    /**
     * {@code balance} is instance that memorizes balance of all cryptocurrencies of the user and all cryptocurrencies
     * traded by bot.
     * **/
    protected double balance;

    /**
     * {@code orderStatus} is instance that memorizes response and result of an order.
     *
     * @implNote it is obtained calling {@link #getOrderStatus(FormatResponse)} method
     **/
    protected String orderStatus;

    /**
     * This method is used fetch details of an order request<br>
     *
     * @param formatResponseType: this indicates the format of order status that have to return.
     * @implNote you must call it when is placed an order before, so when {@link #buyMarket(java.lang.String, double)}
     * or {@link #sellMarket(java.lang.String, double)} is being called.
     * @implSpec if {@code formatResponseType} is equal to {@code STRING} order status will be returned as {@link String} <br>
     * if {@code formatResponseType} is equal to {@code JSON} order status will be returned as {@link JSONObject} or {@link JSONArray} <br>
     * if {@code formatResponseType} is equal to {@code CustomObject} order status will be returned as custom object given by libraries<br>
     **/
    public <T> T getOrderStatus(FormatResponse formatResponseType) {
        if (formatResponseType.equals(STRING) || formatResponseType.equals(LIBRARY_OBJECT)) {
            return (T) orderStatus;
        } else if (formatResponseType.equals(JSON)) {
            try {
                return (T) new JSONObject(orderStatus);
            } catch (JSONException e) {
                try {
                    return (T) new JSONArray(orderStatus);
                } catch (JSONException requestError) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * This method is used by traders to init all instances that routine need. <br>
     * Any params required
     * **/
    protected abstract void initTrader() throws Exception;

    /**
     * This method is used by traders to get user wallet balance. <br>
     *
     * @param currency:     currency of balance value es. EUR will return balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @param decimals:     this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @return wallet balance in currency value
     **/
    public abstract double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception;

    /**
     * This method is used by traders to get user wallet balance. <br>
     *
     * @param currency:     currency of balance value es. EUR will return balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return wallet balance in currency value
     **/
    public abstract double getWalletBalance(String currency, boolean forceRefresh) throws Exception;

    /**
     * This method is used to get asset list of user wallet.<br>
     *
     * @param currency:     currency of asset balance value es. EUR will return asset balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Asset} as {@link ArrayList}
     **/
    public abstract ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception;

    /**
     * This method is used to get all transactions from all {@link #quoteCurrencies} inserted.<br>
     *
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Wrapper
    public abstract ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception;

    /**
     * This method is used to get all transactions from all {@link #quoteCurrencies} inserted.<br>
     *
     * @param dateFormat:   this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    public abstract ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception;

    /**
     * This method is used to get all transactions from a single symbol<br>
     *
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh:  this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Wrapper
    public abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception;

    /**
     * This method is used to get all transactions from a single symbol<br>
     *
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param dateFormat:    this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh:  this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    public abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat,
                                                               boolean forceRefresh) throws Exception;

    /**
     * This method is used to send a buy market order<br>
     *
     * @param symbol:   this indicates the symbol for the order es. (BTCBUSD or BTC-USDT)
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     **/
    public abstract void buyMarket(String symbol, double quantity) throws Exception;

    /**
     * This method is used to send a sell market order<br>
     *
     * @param symbol:   this indicates the symbol for the order es. (BTCBUSD or BTC-USDT)
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     **/
    public abstract void sellMarket(String symbol, double quantity) throws Exception;

    /**
     * This method is used to place an order<br>
     *
     * @param symbol:   this indicates the symbol for the order es. (BTCBUSD or BTC-USDT)
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * @param side:     this indicates the side of the order (BUY or SELL)
     **/
    protected abstract void placeAnOrder(String symbol, double quantity, Side side) throws Exception;

    /**
     * This method is used to insert or update a coin in {@link #coins} list.
     * @param index: index of the coin es. BTC
     * @param name: name of the coin es Bitcoin
     * @param quantity: quantity of that coin es. 0.28
     * **/
    protected void insertCoin(String index, String name, double quantity){
        coins.put(index, new Coin(index,
                name,
                quantity,
                quantity > 0
        ));
    }

    /**
     * This method is used to convert all values from {@code "usd"} value
     *
     * @param currency       : currency to convert from {@code "usd"}
     * @param valueToConvert : value to convert from {@code "usd"}
     * @param decimals:      number of digits to round final value
     * @return value converted from {@code "usd"} as double
     * @throws IllegalArgumentException if decimal digits are negative
     **/
    protected abstract double convertFromUsd(String currency, double valueToConvert, int decimals);

    /**
     * This method is used to get error of any requests<br>
     * Any params required
     **/
    public abstract String getErrorResponse();

    /**
     * Method to print error response <br>
     * Any params required
     **/
    public abstract void printErrorMessage();

    /**
     * This method is used to refresh latest prices<br>
     * Any params required
     **/
    public abstract void refreshLatestPrice() throws Exception;

    /**
     * This method is used to check if any quote is inserted in {@link #quoteCurrencies} list
     *
     * @param compareQuote: quote to compare and check if is inserted
     * @return true if is inserted o false if not
     **/
    public boolean quoteContained(String compareQuote) {
        for (String quote : quoteCurrencies)
            if (compareQuote.equals(quote))
                return true;
        return false;
    }

    /**
     * This method is used to set new list of {@link #quoteCurrencies} overwritten the past list<br>
     * @param quoteCurrencies: list of quote currencies to insert.
     * **/
    public void setQuoteCurrencies(ArrayList<String> quoteCurrencies) {
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * This method is used to insert a new quote currency in {@link #quoteCurrencies} list<br>
     * If this value is already inserted in list will be not inserted to avoid duplicate values.
     * @param newQuote: quote currency to insert es. SOL
     * **/
    public void insertQuoteCurrency(String newQuote){
        if(!quoteCurrencies.contains(newQuote))
            quoteCurrencies.add(newQuote);
    }

    /**
     * This method is used to remove a quote currency from {@link #quoteCurrencies} list<br>
     * If this value is not inserted in list will be not removed and will be returned false.
     * @param quoteToRemove: quote currency to remove es. SOL
     * @return status of deletion for {@code quoteToRemove}, will be true only if that value exists in list and can
     * be removed
     * **/
    public boolean removeQuoteCurrency(String quoteToRemove){
        return quoteCurrencies.remove(quoteToRemove);
    }

    /**
     * This method is used to get {@link #quoteCurrencies} list<br>
     * Any params required.
     * @return {@link #quoteCurrencies} list as {@link ArrayList} of {@link String}
     * **/
    public ArrayList<String> getQuoteCurrencies() {
        return quoteCurrencies;
    }

    /**
     * This method is used check if is refreshing time <br>
     * Any params required
     *
     * @return true only if current timestamp when this method is called minus last timestamp when this method is called,
     * it is memorized in {@link #lastPricesRefresh}, is bigger or equal than {@link #refreshTime}
     **/
    public boolean isRefreshTime() {
        return (System.currentTimeMillis() - lastPricesRefresh) >= refreshTime;
    }

    /**
     * This method is used to get {@link #refreshTime}<br>
     * Any params required.
     *
     * @return {@link #refreshTime} as int
     **/
    public int getRefreshTime() {
        return refreshTime;
    }

    /**
     * Method to get {@link #refreshTime} in seconds (s) format <br>
     * Any params required
     *
     * @return {@link #refreshTime} in seconds (s) format as int
     **/
    public int getRefreshTimeSeconds() {
        return refreshTime / 1000;
    }

    /**
     * This method is used to set time to refresh data
     *
     * @param refreshTime: is time in seconds to set to refresh data.
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     **/
    public void setRefreshTime(int refreshTime) {
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * This method is used print error when request is made, if error is not in request
     * will print {@link Exception} error message
     **/
    protected abstract void printError(String symbol, Exception e);

    /**
     * This method is to compute suggested quantity for an order
     *
     * @param symbol:       symbol of cryptocurrency for the order
     * @param testQuantity: quantity to test
     * @return suggested quantity value computed from exchange's limits as double
     **/
    public abstract double getSuggestedOrderQuantity(String symbol, double testQuantity) throws Exception;

    /**
     * This method is to get list of the latest prices
     *
     * @param decimals: number of digits to round final value
     * @return last prices as {@link ArrayList} of {@link MarketCoin} custom object
     * @throws IllegalArgumentException if decimal digits are negative
     **/
    public abstract ArrayList<MarketCoin> getLatestPrices(int decimals);

    /**
     * This method is to get list of the latest prices <br>
     * Any params required
     *
     * @return last prices as {@link ArrayList} of {@link MarketCoin} custom object
     **/
    public abstract ArrayList<MarketCoin> getLatestPrices();

    /**
     * This method is to get last price of a symbol
     *
     * @param symbol:   symbol from fetch last price
     * @param decimals: number of digits to round final value
     * @return last price as {@link MarketCoin} custom object
     * @throws IllegalArgumentException if decimal digits are negative
     **/
    public abstract MarketCoin getLastPrice(String symbol, int decimals);

    /**
     * This method is to get last price of a symbol <br>
     *
     * @param symbol: symbol from fetch last price
     * @return last price as {@link MarketCoin} custom object
     **/
    public abstract MarketCoin getLastPrice(String symbol);

    /**
     * This method is used to get coin balance
     *
     * @param quote: string of quote currency to return amount value of balance
     * @return balance of coin inserted
     **/
    public abstract double getCoinBalance(String quote);

    /**
     * {@code ReturnFormat} is the instance to pass in {@link Returner} methods to format as you want the response by
     * {@code "Coinbase"}
     *
     * @apiNote you can choose between:
     * <ul>
     * <li>
     * {@link Returner.ReturnFormat#STRING} -> returns the response formatted as {@link String}
     * </li>
     * <li>
     * {@link Returner.ReturnFormat#JSON} -> returns the response formatted as {@code "JSON"}
     * </li>
     * <li>
     * {@link Returner.ReturnFormat#LIBRARY_OBJECT} -> returns the response formatted as custom object offered by library that uses this list
     * </li>
     * </ul>
     **/
    public enum FormatResponse {

        STRING,
        JSON,
        LIBRARY_OBJECT

    }

    /**
     * This method is used to print disclaimer alert to warn user of responsibility of storage and manage
     * api keys create on exchange platforms. <br>
     * It is printed in all traders. <br>
     * Any params required
     **/
    protected void printCredentialsDisclaimer() {
        System.out.println("############################### DISCLAIMER ALERT ################################# \n" +
                "## Note: You are solely responsible for storing and using your api keys created ## \n" +
                "## on the exchange platforms, this is because they will never be saved in our   ## \n" +
                "## infrastructures. Never share these personal credentials with anyone,         ## \n" +
                "## Tecknobit will never ask you for any of the credentials external to our      ## \n" +
                "## service outside of starting the interfaces, therefore not by email, messages,## \n" +
                "## forums or social networks.                                                   ## \n" +
                "##################################################################################\n");
    }

}
