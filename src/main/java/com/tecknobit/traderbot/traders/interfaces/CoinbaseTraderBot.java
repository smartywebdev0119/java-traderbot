package com.tecknobit.traderbot.traders.interfaces;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.coinbasemanager.exchangepro.CoinbaseManager;
import com.tecknobit.coinbasemanager.exchangepro.account.CoinbaseAccountManager;
import com.tecknobit.coinbasemanager.exchangepro.account.records.CoinbaseAccount;
import com.tecknobit.coinbasemanager.exchangepro.currencies.CoinbaseCurrenciesManager;
import com.tecknobit.coinbasemanager.exchangepro.currencies.records.Currency;
import com.tecknobit.coinbasemanager.exchangepro.orders.CoinbaseOrdersManager;
import com.tecknobit.coinbasemanager.exchangepro.orders.records.Order;
import com.tecknobit.coinbasemanager.exchangepro.products.CoinbaseProductsManager;
import com.tecknobit.coinbasemanager.exchangepro.products.records.Ticker;
import com.tecknobit.coinbasemanager.exchangepro.products.records.TradingPair;
import com.tecknobit.traderbot.records.portfolio.Asset;
import com.tecknobit.traderbot.records.portfolio.Coin;
import com.tecknobit.traderbot.records.portfolio.MarketCoin;
import com.tecknobit.traderbot.records.portfolio.Transaction;
import com.tecknobit.traderbot.routines.interfaces.TraderBotConstants;
import com.tecknobit.traderbot.routines.interfaces.TraderCoreRoutines;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.apimanager.trading.CryptocurrencyTool.getCryptocurrencyName;
import static com.tecknobit.apimanager.trading.TradingTools.roundValue;
import static com.tecknobit.coinbasemanager.exchangepro.CoinbaseManager.ReturnFormat.STRING;
import static com.tecknobit.coinbasemanager.exchangepro.orders.records.Order.Sorter.created_at;
import static com.tecknobit.coinbasemanager.exchangepro.orders.records.Order.SortingOrder.asc;
import static com.tecknobit.coinbasemanager.exchangepro.orders.records.Order.Status.done;
import static com.tecknobit.coinbasemanager.exchangepro.orders.records.OrderDetails.Side.valueOf;
import static com.tecknobit.traderbot.records.portfolio.Transaction.getDate;
import static com.tecknobit.traderbot.records.portfolio.Transaction.getDateTimestamp;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.*;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.Side.BUY;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.Side.SELL;
import static java.lang.Math.ceil;

/**
 * The {@code CoinbaseTraderBot} class is trader for {@link CoinbaseManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Coinbase's account.<br>
 * Is derived class of {@code TraderCoreRoutines} class from inherit all core routines methods and instances.
 * @author Tecknobit N7ghtm4r3
 * **/
public class CoinbaseTraderBot extends TraderCoreRoutines {

    /**
     * {@code coinbaseAccountManager} is instance of {@link CoinbaseAccountManager} helpful to account operations
     * **/
    protected final CoinbaseAccountManager coinbaseAccountManager;

    /**
     * {@code coinbaseProductsManager} is instance of {@link CoinbaseProductsManager} helpful to products requests
     * **/
    protected final CoinbaseProductsManager coinbaseProductsManager;

    /**
     * {@code coinbaseOrdersManager} is instance of {@link CoinbaseOrdersManager} helpful to order operations
     * **/
    protected final CoinbaseOrdersManager coinbaseOrdersManager;

    /**
     * {@code tradingPairsList} is a map that contains symbols of trading pair list
     * @implSpec this map has as key the asset index (es. BTC) as {@link String} and has as value custom object
     * {@link TradingPair} give by {@link CoinbaseManager} library.
     * **/
    protected HashMap<String, TradingPair> tradingPairsList;

    /**
     * {@code lastPrices} is a map that contains asset index (es. BTC) as key {@link String} and last ticker as {@link Ticker}
     * @apiNote values inserted in this map are only tickers of coins inserted in {@link #coins} list
     * @implNote refresh of last prices, by default, is every 10 seconds, but you can set programmatically
     * {@link #refreshTime} to customize refresh time.
     * **/
    protected HashMap<String, Ticker> lastPrices;

    /**
     * {@code coinbaseCurrenciesManager} is instance of {@link CoinbaseCurrenciesManager} helpful to fetch details about
     * cryptocurrencies
     * **/
    protected final CoinbaseCurrenciesManager coinbaseCurrenciesManager;

    /** Constructor to init {@link CoinbaseTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             int timeout) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager();
        coinbaseOrdersManager = new CoinbaseOrdersManager();
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager();
        initTrader();
    }

    /** Constructor to init {@link CoinbaseTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager();
        coinbaseOrdersManager = new CoinbaseOrdersManager();
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager();
        initTrader();
    }

    /** Constructor to init {@link CoinbaseTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager();
        coinbaseOrdersManager = new CoinbaseOrdersManager();
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager();
        initTrader();
    }

    /** Constructor to init {@link CoinbaseTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
        coinbaseProductsManager = new CoinbaseProductsManager();
        coinbaseOrdersManager = new CoinbaseOrdersManager();
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager();
        initTrader();
    }

    /** Constructor to init {@link CoinbaseTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage, int timeout,
                             ArrayList<String> quoteCurrencies) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        this.quoteCurrencies = quoteCurrencies;
    }

    /** Constructor to init {@link CoinbaseTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                             ArrayList<String> quoteCurrencies) throws Exception {
        this(apiKey, apiSecret, passphrase, timeout);
        this.quoteCurrencies = quoteCurrencies;
    }

    /** Constructor to init {@link CoinbaseTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             ArrayList<String> quoteCurrencies) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage);
        this.quoteCurrencies = quoteCurrencies;
    }

    /** Constructor to init {@link CoinbaseTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase,
                             ArrayList<String> quoteCurrencies) throws Exception {
        this(apiKey, apiSecret, passphrase);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseTraderBot}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret:           your Coinbase's secret key
     * @param passphrase:          your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout:             custom timeout for request
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             int timeout, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * Constructor to init {@link CoinbaseTraderBot}
     *
     * @param apiKey:      your Coinbase's api key
     * @param apiSecret:   your Coinbase's secret key
     * @param passphrase:  your Coinbase's api passphrase
     * @param timeout:     custom timeout for request
     * @param refreshTime: is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, timeout);
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * Constructor to init {@link CoinbaseTraderBot}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret:           your Coinbase's secret key
     * @param passphrase:          your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             short refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage);
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * Constructor to init {@link CoinbaseTraderBot}
     *
     * @param apiKey:      your Coinbase's api key
     * @param apiSecret:   your Coinbase's secret key
     * @param passphrase:  your Coinbase's api passphrase
     * @param refreshTime: is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, short refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase);
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * Constructor to init {@link CoinbaseTraderBot}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret:           your Coinbase's secret key
     * @param passphrase:          your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout:             custom timeout for request
     * @param refreshTime:         is time in seconds to set to refresh data
     * @param quoteCurrencies:     is a list of quote currencies used in past orders es (USD or EUR)
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage, int timeout,
                             ArrayList<String> quoteCurrencies, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseTraderBot}
     *
     * @param apiKey:          your Coinbase's api key
     * @param apiSecret:       your Coinbase's secret key
     * @param passphrase:      your Coinbase's api passphrase
     * @param timeout:         custom timeout for request
     * @param refreshTime:     is time in seconds to set to refresh data
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                             ArrayList<String> quoteCurrencies, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, timeout, refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseTraderBot}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret:           your Coinbase's secret key
     * @param passphrase:          your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param refreshTime:         is time in seconds to set to refresh data
     * @param quoteCurrencies:     is a list of quote currencies used in past orders es (USD or EUR)
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             ArrayList<String> quoteCurrencies, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, quoteCurrencies);
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * Constructor to init {@link CoinbaseTraderBot}
     *
     * @param apiKey:          your Coinbase's api key
     * @param apiSecret:       your Coinbase's secret key
     * @param passphrase:      your Coinbase's api passphrase
     * @param refreshTime:     is time in seconds to set to refresh data
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase,
                             ArrayList<String> quoteCurrencies, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, quoteCurrencies);
        if(refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * This method init all instances that {@link CoinbaseTraderBot}'s routine need. <br>
     * Any params required <br>
     * **/
    @Override
    protected void initTrader() throws Exception {
        printCredentialsDisclaimer();
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        if(quoteCurrencies == null)
            quoteCurrencies = new ArrayList<>();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
        refreshLatestPrice();
    }

    /**
     * This method is used by traders to get user Coinbase's wallet balance. <br>
     * @param currency: currency of balance value es. EUR will return balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return wallet balance in currency value
     * **/
    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) {
        if(isRefreshTime() || !lastBalanceCurrency.equals(currency) || forceRefresh) {
            refreshLatestPrice();
            lastBalanceCurrency = currency;
            balance = 0;
            for (Coin coin : coins.values())
                if (coin.isTradingEnabled())
                    balance += coin.getQuantity() * lastPrices.get(coin.getAssetIndex() + "-" + USD_CURRENCY).getPrice();
            if (!currency.contains(USD_CURRENCY))
                balance = convertFromUsd(currency, balance, 8);
        }
        return balance;
    }

    /**
     * This method is used by traders to get user Coinbase's wallet balance. <br>
     * @param currency: currency of balance value es. EUR will return balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @param decimals: this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @return wallet balance in currency value
     * **/
    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) {
        return coinbaseProductsManager.roundValue(getWalletBalance(currency, forceRefresh), decimals);
    }

    /**
     * This method is used to get asset list of Coinbase's user wallet.<br>
     * @param currency: currency of asset balance value es. EUR will return asset balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) {
        if(isRefreshTime() || !lastAssetCurrency.equals(currency) || forceRefresh){
            refreshLatestPrice();
            lastAssetCurrency = currency;
            assets.clear();
            for (Coin coin : coins.values()) {
                if(coin.isTradingEnabled()) {
                    String index = coin.getAssetIndex();
                    double quantity = coin.getQuantity();
                    double balance = quantity * lastPrices.get(index).getPrice();
                    if (!currency.contains(USD_CURRENCY))
                        balance = convertFromUsd(currency, balance, 8);
                    assets.add(new Asset(index,
                            coin.getAssetName(),
                            quantity,
                            balance,
                            currency
                    ));
                }
            }
        }
        return assets;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from all {@link #quoteCurrencies} inserted.<br>
     *
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Wrapper
    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        return getAllTransactions(null, forceRefresh);
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from all {@link #quoteCurrencies} inserted.<br>
     *
     * @param dateFormat:   this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if (isRefreshTime() || forceRefresh) {
            allTransactions.clear();
            for (String quoteCurrency : quoteCurrencies)
                allTransactions.addAll(getTransactionsList(quoteCurrency, dateFormat, forceRefresh));
            transactions.clear();
        }
        return allTransactions;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from a single symbol<br>
     *
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh:  this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Wrapper
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        return getTransactionsList(quoteCurrency, null, forceRefresh);
    }

    /**
     * This method is used to get all transactions for a Binance's account from a single symbol<br>
     *
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param dateFormat:    this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh:  this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat,
                                                      boolean forceRefresh) throws Exception {
        if (isRefreshTime() || !lastTransactionCurrency.equals(quoteCurrency) || forceRefresh) {
            transactions.clear();
            lastTransactionCurrency = quoteCurrency;
            ArrayList<Order> orders = coinbaseOrdersManager.getAllOrders(1000, created_at, asc, done);
            String date;
            for (Coin coin : coins.values()) {
                if (coin.isTradingEnabled()) {
                    String baseAsset = coin.getAssetIndex();
                    String symbol = baseAsset + "-" + lastTransactionCurrency;
                    for (Order order : orders) {
                        if (order.getProductId().equals(symbol)) {
                            String createdAt = order.getCreatedAt();
                            if (dateFormat != null)
                                date = getDate(getDateTimestamp(createdAt, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
                                        dateFormat);
                            else
                                date = createdAt;
                            double size = order.getSize();
                            transactions.add(new Transaction(symbol,
                                    Side.valueOf(order.getSide().name().toUpperCase()),
                                    date,
                                    size * order.getPrice(),
                                    size,
                                    lastTransactionCurrency,
                                    baseAsset
                            ));
                        }
                    }
                }
            }
        }
        return transactions;
    }

    /**
     * This method is used to send a buy market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        if(!symbol.contains("-"))
            symbol = getOrderSymbol(symbol);
        placeAnOrder(symbol, quantity, BUY);
        int statusCode = coinbaseOrdersManager.getStatusResponse();
        if(statusCode == 200){
            String[] index = symbol.split("-");
            Coin coin = coins.get(index[0]);
            insertQuoteCurrency(index[1]);
            insertCoin(index[0], coin.getAssetName(), coin.getQuantity() + quantity);
        }else{
            throw new Exception("Error during buy order status code: [" + statusCode + "]" +
                    " error message: [" + coinbaseOrdersManager.getErrorResponse() + "]");
        }
    }

    /**
     * This method is used to send a sell market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        if (!symbol.contains("-"))
            symbol = getOrderSymbol(symbol);
        String[] index = symbol.split("-");
        Coin coin = coins.get(index[0]);
        if (coin != null) {
            placeAnOrder(symbol, quantity, SELL);
            int statusCode = coinbaseOrdersManager.getStatusResponse();
            if (statusCode == 200)
                insertCoin(index[0], coin.getAssetName(), coin.getQuantity() - quantity);
            else {
                throw new Exception("Error during sell order status code: [" + statusCode + "]" +
                        " error message: [" + coinbaseOrdersManager.getErrorResponse() + "]");
            }
        } else
            throw new Exception("Your wallet doesn't have this coin to sell [" + symbol + "]");
    }

    /**
     * This method is used to check and eventually correct symbol inserted for the order
     * @param actualSymbol: correct form would be base-quote, but if is basequote check algorithm start. es. BTC-USDT
     * @return if is correct return {@code actualSymbol}, if not symbol inserted in the right form es BTCUSDT -> BTC-USDT
     * @implSpec if symbol is wrong and not exists in Coinbase listing order request will throw an {@link Exception}
     * **/
    protected String getOrderSymbol(String actualSymbol){
        for (TradingPair tradingPair : tradingPairsList.values()) {
            String id = tradingPair.getId();
            if(id.replace("-", "").equals(actualSymbol))
                return id;
        }
        return actualSymbol;
    }

    /**
     * This method is used to place an order
     *
     * @param symbol:   this indicates the symbol for the order es. BTC-BUSD
     * @param quantity: this indicates quantity of that symbol is wanted to trade es. 10
     * @param side:     this indicates the side of the order (BUY or SELL)
     **/
    @Override
    protected void placeAnOrder(String symbol, double quantity, Side side) throws Exception {
        orderStatus = coinbaseOrdersManager.createMarketOrderSize(valueOf(side.name().toLowerCase()), symbol, quantity,
                STRING);
    }

    /**
     * This method is used fetch details of a Coinbase's order request<br>
     *
     * @param formatResponseType: this indicates the format of order status that have to return.
     * @implNote you must call it when is placed an order before, so when {@link TraderCoreRoutines#buyMarket(String, double)}
     * or {@link TraderCoreRoutines#sellMarket(String, double)} is being called.
     * @implSpec if {@code formatResponseType} is equal to {@code STRING} order status will be returned as {@link String} <br>
     * if {@code formatResponseType} is equal to {@code JSON} order status will be returned as {@link JSONObject} or {@link JSONArray} <br>
     * if {@code formatResponseType} is equal to {@code CustomObject} order status will be returned as custom object given by libraries<br>
     **/
    @Override
    public <T> T getOrderStatus(FormatResponse formatResponseType) {
        return super.getOrderStatus(formatResponseType);
    }

    /**
     * This method is used to get error of any requests<br>
     * Any params required
     **/
    @Override
    public String getErrorResponse() {
        return coinbaseAccountManager.getErrorResponse();
    }

    /**
     * Method to print error response <br>
     * Any params required
     **/
    @Override
    public void printErrorMessage() {
        coinbaseAccountManager.printErrorResponse();
    }

    /**
     * This method is used to refresh latest prices<br>
     * Any params required
     **/
    @Override
    public synchronized void refreshLatestPrice() {
        lastPricesRefresh = System.currentTimeMillis();
        try {
            for (CoinbaseAccount coin : coinbaseAccountManager.getCoinbaseWallets()) {
                double balance = coin.getBalance();
                String index = coin.getCurrency();
                coins.put(index, new Coin(index,
                        getCryptocurrencyName(index),
                        balance,
                        balance != 0
                ));
            }
            for (TradingPair tradingPair : coinbaseProductsManager.getAllTradingPairs())
                tradingPairsList.put(tradingPair.getId(), tradingPair);
            for (String productId : tradingPairsList.keySet()) {
                try {
                    if (coins.get(productId.split("-")[0]).isTradingEnabled() || productId.endsWith(USD_CURRENCY))
                        lastPrices.put(productId, coinbaseProductsManager.getProductTicker(productId));
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used print error when request is made, if error is not in request
     * will print {@link Exception} error message
     * **/
    @Override
    protected void printError(String symbol, Exception e){
        if (coinbaseOrdersManager.getStatusResponse() != SUCCESSFUL_RESPONSE)
            System.out.println(getErrorResponse() + " on [" + symbol + "]");
        else
            e.printStackTrace();
    }

    /**
     * This method is to compute suggested quantity for an order
     *
     * @param symbol:       symbol of cryptocurrency for the order
     * @param testQuantity: quantity to test
     * @return suggested quantity value computed from exchange's limits as double
     **/
    @Override
    public double getSuggestedOrderQuantity(String symbol, double testQuantity) throws Exception {
        Currency currency = coinbaseCurrenciesManager.getCurrency(symbol);
        if (testQuantity >= currency.getMinSize())
            if (testQuantity % currency.getMaxPrecision() != 0)
                return ceil(testQuantity);
        return -1;
    }

    /**
     * This method is to get list of the latest prices
     *
     * @param decimals: number of digits to round final value
     * @return last prices as {@link ArrayList} of {@link MarketCoin} custom object
     * @throws IllegalArgumentException if decimal digits are negative
     **/
    @Override
    public ArrayList<MarketCoin> getLatestPrices(int decimals) {
        ArrayList<MarketCoin> lastPrices = new ArrayList<>();
        for (Ticker ticker : this.lastPrices.values())
            lastPrices.add(new MarketCoin(ticker.getPrice(decimals), ticker.getPriceChangePercent(decimals)));
        return lastPrices;
    }

    /**
     * This method is to get list of the latest prices <br>
     * Any params required
     *
     * @return last prices as {@link ArrayList} of {@link MarketCoin} custom object
     **/
    @Override
    public ArrayList<MarketCoin> getLatestPrices() {
        ArrayList<MarketCoin> lastPrices = new ArrayList<>();
        for (Ticker ticker : this.lastPrices.values())
            lastPrices.add(new MarketCoin(ticker.getPrice(), ticker.getPriceChangePercent()));
        return lastPrices;
    }

    /**
     * This method is to get last price of a symbol
     *
     * @param symbol:   symbol from fetch last price
     * @param decimals: number of digits to round final value
     * @return last price as {@link MarketCoin} custom object
     * @throws IllegalArgumentException if decimal digits are negative
     * @apiNote if symbol inserted does not exist will be returned a {@link MarketCoin} object with zeros as values
     **/
    @Override
    public MarketCoin getLastPrice(String symbol, int decimals) {
        Ticker ticker = lastPrices.get(symbol);
        if (ticker == null)
            return new MarketCoin(0, 0);
        return new MarketCoin(ticker.getPrice(decimals),
                ticker.getPriceChangePercent(decimals)
        );
    }

    /**
     * This method is to get last price of a symbol <br>
     *
     * @param symbol: symbol from fetch last price
     * @return last price as {@link MarketCoin} custom object
     * @apiNote if symbol inserted does not exist will be returned a {@link MarketCoin} object with zeros as values
     **/
    @Override
    public MarketCoin getLastPrice(String symbol) {
        Ticker ticker = lastPrices.get(symbol);
        if (ticker == null)
            return new MarketCoin(0, 0);
        return new MarketCoin(ticker.getPrice(),
                ticker.getPriceChangePercent()
        );
    }

    /**
     * This method is used to get coin balance
     *
     * @param quote: string of quote currency to return amount value of balance
     * @return balance of coin inserted
     **/
    public double getCoinBalance(String quote) {
        Coin coin = coins.get(quote);
        return coinbaseAccountManager.roundValue(coin.getQuantity() *
                lastPrices.get(coin.getAssetIndex() + "-" + USD_CURRENCY).getPrice(), 8);
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
    @Override
    protected double convertFromUsd(String currency, double valueToConvert, int decimals) {
        double usdValue = lastPrices.get(getUSDTSymbol(currency)).getPrice();
        if (usdValue < 1)
            return roundValue(valueToConvert * usdValue, 8);
        return roundValue(valueToConvert / usdValue, 8);
    }

    /**
     * This method is used to assemble right symbol with {@link TraderBotConstants#USDT_CURRENCY} combination.
     *
     * @param currency: currency for the request es. EUR
     * @return currency inserted es. USDT-EUR.
     **/
    private String getUSDTSymbol(String currency) {
        return USDT_CURRENCY + "-" + currency;
    }

}
