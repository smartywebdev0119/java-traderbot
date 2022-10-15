package com.tecknobit.traderbot.traders.autonomous.android;

import com.tecknobit.coinbasemanager.Managers.ExchangePro.CoinbaseManager;
import com.tecknobit.traderbot.orders.MarketOrder;
import com.tecknobit.traderbot.records.account.BotDetails;
import com.tecknobit.traderbot.records.account.TraderAccount;
import com.tecknobit.traderbot.records.portfolio.Asset;
import com.tecknobit.traderbot.records.portfolio.Cryptocurrency;
import com.tecknobit.traderbot.records.portfolio.Transaction;
import com.tecknobit.traderbot.routines.android.AndroidBotController;
import com.tecknobit.traderbot.routines.android.AndroidCoreRoutines;
import com.tecknobit.traderbot.routines.android.AndroidWorkflow;
import com.tecknobit.traderbot.routines.android.AndroidWorkflow.Credentials;
import com.tecknobit.traderbot.routines.android.ServerRequest;
import com.tecknobit.traderbot.routines.autonomous.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.traders.autonomous.interfaces.CoinbaseAutoTraderBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.apimanager.Tools.Trading.TradingTools.roundValue;
import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.BUY_SIDE;
import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.SELL_SIDE;
import static com.tecknobit.traderbot.records.portfolio.Cryptocurrency.TradingConfig;
import static com.tecknobit.traderbot.records.portfolio.Cryptocurrency.TradingConfig.MODEL_ID_KEY;
import static com.tecknobit.traderbot.routines.android.ServerRequest.HOST;
import static com.tecknobit.traderbot.routines.android.ServerRequest.PORT;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.*;
import static java.lang.System.currentTimeMillis;
import static java.text.DateFormat.getDateTimeInstance;

/**
 * The {@code AndroidCoinbaseAutoTrader} class is trader for {@link CoinbaseManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Coinbase's account autonomously.<br>
 * Is derived class of {@link CoinbaseAutoTraderBot} class from inherit all core routines methods and instances.
 *
 * @author Tecknobit N7ghtm4r3
 * @implNote for autonomous operations uses {@link AutoTraderCoreRoutines} and {@link MarketOrder} routines.
 * @implSpec {@link TraderAccount} and {@link #walletList} are objects that will automatically restore, but if
 * you change trader's platform all assets will be deleted from the managing of the trader.
 **/
public class AndroidCoinbaseAutoTrader extends CoinbaseAutoTraderBot implements AndroidCoreRoutines, AndroidBotController {

    /**
     * {@code transactionDateFormat} is instance helpful to format transaction date
     **/
    private final DateFormat transactionDateFormat;

    /**
     * {@code botDetails} is instance helpful to manage trader details
     *
     * @implNote will be instantiated with default values by {@link AndroidCoinbaseAutoTrader}
     **/
    private static final BotDetails botDetails;

    static {
        long timestamp = System.currentTimeMillis();
        botDetails = new BotDetails(timestamp, BOT_TYPE_AUTONOMOUS, RUNNING_BOT_STATUS, COINBASE_PLATFORM,
                10000, timestamp);
    }

    /**
     * {@code androidWorkflow} is instance helpful to manage Android's workflow
     **/
    private final AndroidWorkflow androidWorkflow;

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     int timeout, boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials, int refreshTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, null, sendStatsReport,
                printRoutineMessages, baseCurrency);
        setRefreshTime(refreshTime);
        initCredentials(credentials);
        traderAccount = new TraderAccount(credentials);
        androidWorkflow = new AndroidWorkflow(new ServerRequest(credentials, HOST, PORT), this, credentials,
                printRoutineMessages);
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, int timeout, boolean sendStatsReport,
                                     boolean printRoutineMessages, String baseCurrency, Credentials credentials,
                                     int refreshTime) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, null, sendStatsReport, printRoutineMessages, baseCurrency);
        setRefreshTime(refreshTime);
        initCredentials(credentials);
        traderAccount = new TraderAccount(credentials);
        androidWorkflow = new AndroidWorkflow(new ServerRequest(credentials, HOST, PORT), this, credentials,
                printRoutineMessages);
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials, int refreshTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, null, sendStatsReport, printRoutineMessages,
                baseCurrency);
        setRefreshTime(refreshTime);
        initCredentials(credentials);
        traderAccount = new TraderAccount(credentials);
        androidWorkflow = new AndroidWorkflow(new ServerRequest(credentials, HOST, PORT), this, credentials,
                printRoutineMessages);
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, boolean sendStatsReport,
                                     boolean printRoutineMessages, String baseCurrency, Credentials credentials,
                                     int refreshTime) throws Exception {
        super(apiKey, apiSecret, passphrase, null, sendStatsReport, printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshTime(refreshTime);
        initCredentials(credentials);
        traderAccount = new TraderAccount(credentials);
        androidWorkflow = new AndroidWorkflow(new ServerRequest(credentials, HOST, PORT), this, credentials,
                printRoutineMessages);
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     int timeout, ArrayList<String> quoteCurrencies, boolean sendStatsReport,
                                     boolean printRoutineMessages, String baseCurrency, Credentials credentials,
                                     int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, sendStatsReport, printRoutineMessages, baseCurrency,
                credentials, refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param timeout              : custom timeout for request
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, int timeout,
                                     ArrayList<String> quoteCurrencies, boolean sendStatsReport, boolean printRoutineMessages,
                                     String baseCurrency, Credentials credentials, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, timeout, sendStatsReport, printRoutineMessages, baseCurrency, credentials,
                refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     ArrayList<String> quoteCurrencies, boolean sendStatsReport, boolean printRoutineMessages,
                                     String baseCurrency, Credentials credentials, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, sendStatsReport, printRoutineMessages, baseCurrency,
                credentials, refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     *
     * @param apiKey:              your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshTime:         is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies,
                                     boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, sendStatsReport, printRoutineMessages, baseCurrency, credentials, refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     *
     * This method init all instances that {@link AndroidBinanceAutoTrader}'s routine need. <br>
     * Any params required <br>
     * **/
    @Override
    protected void initTrader() throws Exception {
        printAndroidDisclaimer();
        super.initTrader();
    }

    /**
     * This method is used to init a {@link Credentials} object to start {@link AndroidWorkflow}
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * **/
    @Override
    public void initCredentials(Credentials credentials) throws Exception {
        checkCredentialsValidity(credentials);
        credentials.setBotDetails(botDetails);
        if(credentials.getToken() == null)
            credentials.sendRegistrationRequest(HOST, PORT);
        else
            credentials.sendLoginRequest(baseCurrency, HOST, PORT, quoteCurrencies);
    }

    /**
     * This method is used to handle {@link AndroidWorkflow} <br>
     * Any params required
     * **/
    @Override
    public void workflowHandler() {
        enableBot();
        androidWorkflow.checkWalletList(walletList);
        androidWorkflow.startWorkflow();
    }

    /**
     * This method is used to check list of possible new cryptocurrencies to buy using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    @Override
    public void checkCryptocurrencies() throws Exception {
        super.checkCryptocurrencies();
        JSONArray checkingList = new JSONArray();
        for (Cryptocurrency cryptocurrency : this.checkingList.values()){
            checkingList.put(new JSONObject().put(BASE_ASSET_KEY, cryptocurrency.getAssetIndex())
                    .put(SYMBOL_KEY, cryptocurrency.getSymbol())
                    .put(ASSET_NAME_KEY, cryptocurrency.getAssetName())
                    .put(TPTOP_INDEX_KEY, cryptocurrency.getTptopIndex(2))
                    .put(LAST_PRICE_KEY, cryptocurrency.getLastPrice(2))
                    .put(PRICE_CHANGE_PERCENT_KEY, cryptocurrency.getPriceChangePercent(2))
                    .put(MODEL_ID_KEY, cryptocurrency.getTradingConfig().getModelId())
                    .put(QUOTE_ASSET_KEY, cryptocurrency.getQuoteAsset())
            );
        }
        androidWorkflow.insertCheckingList(checkingList);
    }

    /**
     * This method is used to buy new cryptocurrencies from list loaded from {@link #checkCryptocurrencies()} routine
     * using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    @Override
    public void buyCryptocurrencies() throws Exception {
        HashMap<String, Cryptocurrency> mCheckingList = new HashMap<>(checkingList);
        super.buyCryptocurrencies();
        JSONArray wallet = new JSONArray();
        for (Cryptocurrency cryptocurrency : walletList.values()){
            if(mCheckingList.containsKey(cryptocurrency.getAssetIndex())){
                wallet.put(new JSONObject(cryptocurrency.getCryptocurrencyJSON())
                        .put(TRANSACTION_KEY, androidWorkflow.assembleTransaction(cryptocurrency, BUY_SIDE,
                                transactionDateFormat).getTransactionJSON()));
            }
        }
        androidWorkflow.insertWalletList(wallet);
    }

    /**
     * This method is used to routine of update wallet of cryptocurrencies bought by auto trader. If {@link Cryptocurrency}
     * respects {@link Cryptocurrency.TradingConfig} model that {@link Cryptocurrency} will be sold. <br>
     * Any params required
     * **/
    @Override
    public void updateWallet() throws Exception {
        super.updateWallet();
        refreshWalletList();
    }

    /**
     * This method is used to update account wallet list <br>
     * Any params required
     * **/
    @Override
    public void refreshWalletList() throws Exception {
        JSONArray wallet = new JSONArray();
        for (Cryptocurrency cryptocurrency : walletList.values()){
            wallet.put(new JSONObject().put(BASE_ASSET_KEY, cryptocurrency.getAssetIndex())
                    .put(LAST_PRICE_KEY, cryptocurrency.getLastPrice())
                    .put(PRICE_CHANGE_PERCENT_KEY, cryptocurrency.getPriceChangePercent(2))
                    .put(INCOME_PERCENT_KEY, cryptocurrency.getIncomePercent(2)));
        }
        androidWorkflow.insertRefreshedPrices(wallet);
    }

    /**
     * This method is used by traders to get user Coinbase's wallet balance. <br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @implNote if {@link #runningTrader} is false will return -1
     * @return wallet balance in currency value
     * **/
    public double getWalletBalance(boolean forceRefresh) {
        if(runningTrader) {
            balance = super.getWalletBalance(baseCurrency, forceRefresh);
            androidWorkflow.insertWalletBalance(roundValue(balance, 2));
            return balance;
        }
        return -1;
    }

    /**
     * This method is used by traders to get user Coinbase's wallet balance. <br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @param decimals: this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @implNote if {@link #runningTrader} is false will return -1
     * @return wallet balance in currency value
     * **/
    public double getWalletBalance(boolean forceRefresh, int decimals) {
        if(runningTrader)
            return roundValue(getWalletBalance(forceRefresh), decimals);
        return -1;
    }

    /**
     * This method is used to get asset list of Coinbase's user wallet.<br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    public ArrayList<Asset> getAssetsList(boolean forceRefresh) {
        if(runningTrader)
            return super.getAssetsList(baseCurrency, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from all {@link #quoteCurrencies} inserted.<br>
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAllTransactions(dateFormat, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from all {@link #quoteCurrencies} inserted.<br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAllTransactions(forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getTransactionsList(quoteCurrency, dateFormat, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getTransactionsList(quoteCurrency, forceRefresh);
        return null;
    }

    /**
     * This method is used to send a buy market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * @implNote if {@link #runningTrader} is false will do not the buy operation
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        if(runningTrader)
            super.buyMarket(symbol, quantity);
    }

    /**
     * This method is used to send a sell market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * @implNote if {@link #runningTrader} is false will do not the sell operation
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        if (runningTrader) {
            super.sellMarket(symbol, quantity);
            androidWorkflow.removeCryptocurrency(cryptocurrencySold.getAssetIndex(),
                    androidWorkflow.assembleTransaction(cryptocurrencySold, SELL_SIDE, transactionDateFormat));
        }
    }

    /**
     * This method is used to set new list of {@link #quoteCurrencies} overwritten the past list
     *
     * @param quoteCurrencies: list of quote currencies to insert
     * @apiNote this method is useful to interact with
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s interface
     **/
    @Override
    public void setQuoteCurrencies(ArrayList<String> quoteCurrencies) {
        if (androidWorkflow.insertQuoteCurrencyList(quoteCurrencies))
            this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * This method is used to disable running mode of trader
     *
     * @implNote in Android's interfaces this method updates also
     * {@link #botDetails} status instance to STOPPED_BOT_STATUS
     **/
    @Override
    public void disableBot() {
        if (runningTrader) {
            runningTrader = false;
            botDetails.setBotStatus(STOPPED_BOT_STATUS);
        }
    }

    /**
     * This method is used to enable running mode of trader
     *
     * @implNote in Android's interfaces this method updates also
     * {@link #botDetails} status instance to RUNNING_BOT_STATUS
     **/
    @Override
    public void enableBot() {
        if (!runningTrader) {
            runningTrader = true;
            botDetails.setBotStatus(RUNNING_BOT_STATUS);
        }
    }

    /**
     * This method is used to get sales at loss
     * @return sales at loss
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getSalesAtLoss() {
        if(runningTrader)
            return super.getSalesAtLoss();
        return -1;
    }

    /**
     * This method is used to get sales at gain
     * @return sales at gain
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getSalesAtGain() {
        if(runningTrader)
            return super.getSalesAtGain();
        return -1;
    }

    /**
     * This method is used to get sales at pair
     * @return sales at pair
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getSalesInPair() {
        if(runningTrader)
            return super.getSalesInPair();
        return -1;
    }

    /**
     * This method is used to get total sales
     * @return total sales
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getTotalSales() {
        if (runningTrader)
            return super.getTotalSales();
        return -1;
    }

    /**
     * This method is used to get base currency for change amount value <br>
     * Any params required
     *
     * @implNote if {@link #runningTrader} is false will return null
     **/
    @Override
    public String getBaseCurrency() {
        if (runningTrader)
            return super.getBaseCurrency();
        return null;
    }

    /**
     * This method is used to get credentials inserted for trader login
     * @return trader credentials as {@link Credentials} object
     * **/
    @Override
    public Credentials getCredentials() {
        return androidWorkflow.getCredentials();
    }

    /**
     * This method is used to set flag to print routine messages
     *
     * @param printRoutineMessages: flag to insert to print or not routine messages
     **/
    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        super.setPrintRoutineMessages(printRoutineMessages);
        androidWorkflow.setPrintRoutineMessages(printRoutineMessages);
    }

    /**
     * This method is used to add a new quote currency in {@link #quoteCurrencies} list<br>
     * If this value is already inserted in list will be not inserted to avoid duplicate values.
     *
     * @param newQuote: quote currency to insert es. SOL
     * @apiNote this method is useful to interact with
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s interface
     **/
    public void addQuoteCurrency(String newQuote) {
        if (!quoteCurrencies.contains(newQuote) && androidWorkflow.insertQuoteCurrency(newQuote))
            quoteCurrencies.add(newQuote);
    }

    /**
     * This method is used to remove a quote currency from {@link #quoteCurrencies} list<br>
     * If this value is not inserted in list will be not removed and will be returned false.
     *
     * @param quoteToRemove: quote currency to remove es. SOL
     * @return status of deletion for {@code quoteToRemove}, will be true only if that value exists in list and can
     * be removed
     * @apiNote this method is useful to interact with
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s interface
     **/
    public boolean deleteQuoteCurrency(String quoteToRemove) {
        if (quoteCurrencies.contains(quoteToRemove) && androidWorkflow.removeQuoteCurrency(quoteToRemove))
            return quoteCurrencies.remove(quoteToRemove);
        return false;
    }

    /**
     * This method is used to run the bot
     *
     * @apiNote this method is useful to interact with
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s interface
     **/
    @Override
    public void runBot() {
        if (!runningTrader) {
            if (androidWorkflow.enableBot()) {
                enableBot();
            }
        }
    }

    /**
     * This method is used to stop the bot
     *
     * @apiNote this method is useful to interact with
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s interface
     **/
    @Override
    public void stopBot() {
        if (runningTrader) {
            if (androidWorkflow.disableBot()) {
                disableBot();
            }
        }
    }

    /**
     * This method is used to set time change refresh data
     *
     * @param refreshTime: is time in seconds to set to refresh data.
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote in Android's interfaces this method updates also {@link #botDetails} instance
     * @apiNote this method is useful to interact with
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s interface
     **/
    @Override
    public void changeRefreshTime(int refreshTime) {
        if (refreshTime >= 5 && refreshTime <= 3600) {
            if (getRefreshTimeSeconds() != refreshTime) {
                if (androidWorkflow != null) {
                    if (androidWorkflow.changeRefreshTime(refreshTime)) {
                        botDetails.setRefreshTime(refreshTime);
                        this.refreshTime = refreshTime * 1000;
                    }
                } else {
                    botDetails.setRefreshTime(refreshTime);
                    this.refreshTime = refreshTime * 1000;
                }
            }
        } else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * This method is used to set base currency for change amount value
     *
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @apiNote this method is useful to interact with
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s interface
     **/
    @Override
    public void changeBaseCurrency(String baseCurrency) {
        if (baseCurrency == null || baseCurrency.isEmpty())
            throw new IllegalArgumentException("Currency cannot be null or empty, but for example EUR or USD");
        if (!this.baseCurrency.equals(baseCurrency)) {
            if (androidWorkflow.changeBaseCurrency(baseCurrency))
                this.baseCurrency = baseCurrency;
        }
    }

}
