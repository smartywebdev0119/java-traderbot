package com.tecknobit.traderbot.traders.interfaces.android;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.binancemanager.managers.BinanceManager;
import com.tecknobit.binancemanager.managers.market.records.tickers.TickerPriceChange;
import com.tecknobit.traderbot.records.account.BotDetails;
import com.tecknobit.traderbot.records.account.TraderAccount;
import com.tecknobit.traderbot.records.portfolio.Asset;
import com.tecknobit.traderbot.records.portfolio.Coin;
import com.tecknobit.traderbot.records.portfolio.Cryptocurrency;
import com.tecknobit.traderbot.records.portfolio.Transaction;
import com.tecknobit.traderbot.routines.android.AndroidBotController;
import com.tecknobit.traderbot.routines.android.AndroidCoreRoutines;
import com.tecknobit.traderbot.routines.android.AndroidWorkflow;
import com.tecknobit.traderbot.routines.android.AndroidWorkflow.Credentials;
import com.tecknobit.traderbot.routines.android.ServerRequest;
import com.tecknobit.traderbot.traders.interfaces.BinanceTraderBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.apimanager.trading.CryptocurrencyTool.getCryptocurrencyName;
import static com.tecknobit.traderbot.routines.android.ServerRequest.HOST;
import static com.tecknobit.traderbot.routines.android.ServerRequest.PORT;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.*;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.Side.SELL;
import static java.lang.System.currentTimeMillis;
import static java.text.DateFormat.getDateTimeInstance;

/**
 * The {@code AndroidBinanceTrader} class is trader for {@link BinanceManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Binance's account.<br>
 * Is derived class of {@link BinanceTraderBot} class from inherit all core routines methods and instances.
 *
 * @author Tecknobit N7ghtm4r3
 * @implNote This is an Android's interface you can
 * find the apk source at <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>
 **/
public class AndroidBinanceTrader extends BinanceTraderBot implements AndroidCoreRoutines, AndroidBotController {

    /**
     * {@code transactionDateFormat} is instance helpful to format transaction date
     **/
    private final DateFormat transactionDateFormat;

    /**
     * {@code botDetails} is instance helpful to manage trader details
     *
     * @implNote will be instantiated with default values by {@link AndroidBinanceTrader}
     **/
    private static final BotDetails botDetails;

    static {
        long timestamp = System.currentTimeMillis();
        botDetails = new BotDetails(timestamp, BOT_TYPE_MANUAL, RUNNING_BOT_STATUS, BINANCE_PLATFORM,
                10000, timestamp);
    }

    /**
     * {@code traderAccount} is instance helpful to manage trader account stats
     *
     * @implNote will be instantiated with account values with server request
     **/
    private final TraderAccount traderAccount;

    /**
     * {@code androidWorkflow} is instance helpful to manage Android's workflow
     **/
    private final AndroidWorkflow androidWorkflow;

    /**
     * {@code symbol} is instance that memorizes symbol of cryptocurrency es. BTCBUSD or BTC-USD
     * **/
    private String symbol;

    /**
     * {@code side} is instance that memorizes side of order BUY or SELL
     **/
    private Side side;

    /**
     * {@code walletList} is a map that contains wallet list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * **/
    private final ConcurrentHashMap<String, Cryptocurrency> walletList;

    /**
     * {@code runningTrader} is instance that memorizes flag that indicates if the trader is running
     * **/
    private boolean runningTrader;

    /**
     * {@code baseCurrency} is instance that memorizes base currency to get all amount value of traders routine es. EUR
     * **/
    private String baseCurrency;

    /**
     * Constructor to init {@link AndroidBinanceTrader}
     *
     * @param apiKey:              your Binance's api key
     * @param secretKey:           your Binance's secret key
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param refreshTime          : is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidBinanceTrader(String apiKey, String secretKey, Credentials credentials, boolean printRoutineMessages,
                                String baseCurrency, int refreshTime) throws Exception {
        this(apiKey, secretKey, null, refreshTime, credentials, printRoutineMessages, baseCurrency);
    }

    /**
     * Constructor to init {@link AndroidBinanceTrader}
     *
     * @param apiKey:              your Binance's api key
     * @param secretKey:           your Binance's secret key
     * @param refreshTime:         is time in seconds to set to refresh data
     * @param quoteCurrencies:     is a list of quote currencies used in past orders es (USD or EUR)
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, int refreshTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshTime);
        this.baseCurrency = baseCurrency;
        setRefreshTime(refreshTime);
        initCredentials(credentials, botDetails, baseCurrency, quoteCurrencies);
        androidWorkflow = new AndroidWorkflow(new ServerRequest(credentials, HOST, PORT), this, credentials,
                printRoutineMessages);
        traderAccount = new TraderAccount(credentials);
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidBinanceTrader}
     *
     * @param apiKey:              your Binance's api key
     * @param secretKey:           your Binance's secret key
     * @param baseEndpoint:        base endpoint choose from BinanceTraderBot.BINANCE_BASE_ENDPOINTS array
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param refreshTime          : is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, Credentials credentials,
                                boolean printRoutineMessages, String baseCurrency, int refreshTime) throws Exception {
        this(apiKey, secretKey, baseEndpoint, null, credentials, printRoutineMessages, baseCurrency, refreshTime);
    }

    /**
     * Constructor to init {@link AndroidBinanceTrader}
     *
     * @param apiKey:              your Binance's api key
     * @param secretKey:           your Binance's secret key
     * @param refreshTime:         is time in seconds to set to refresh data
     * @param baseEndpoint:        base endpoint choose from BinanceTraderBot.BINANCE_BASE_ENDPOINTS array
     * @param quoteCurrencies:     is a list of quote currencies used in past orders es (USD or EUR)
     * @param credentials:         is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                Credentials credentials, boolean printRoutineMessages,
                                String baseCurrency, int refreshTime) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshTime);
        this.baseCurrency = baseCurrency;
        setRefreshTime(refreshTime);
        initCredentials(credentials, botDetails, baseCurrency, quoteCurrencies);
        androidWorkflow = new AndroidWorkflow(new ServerRequest(credentials, HOST, PORT), this, credentials,
                printRoutineMessages);
        traderAccount = new TraderAccount(credentials);
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * This method init all instances that {@link AndroidBinanceTrader}'s routine need. <br>
     * Any params required <br>
     * **/
    @Override
    protected void initTrader() throws Exception {
        printAndroidDisclaimer();
        super.initTrader();
    }

    /**
     * This method is used to handle {@link AndroidWorkflow} <br>
     * Any params required
     * **/
    @Override
    public void workflowHandler() {
        enableBot();
        refreshWalletList();
        androidWorkflow.startWorkflow();
    }

    /**
     * This method is used to update account wallet list <br>
     * Any params required
     * **/
    @Override
    public void refreshWalletList() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                JSONArray wallet = new JSONArray();
                while (true){
                    if(runningTrader){
                        try {
                            if(isRefreshTime())
                                refreshLatestPrice();
                            for (Cryptocurrency cryptocurrency : walletList.values()){
                                String assetIndex = cryptocurrency.getAssetIndex();
                                TickerPriceChange ticker = lastPrices.get(assetIndex + BUSD_CURRENCY);
                                double lastPrice = ticker.getLastPrice();
                                double priceChangePercent = ticker.getPriceChangePercent();
                                cryptocurrency.setLastPrice(lastPrice);
                                cryptocurrency.setPriceChangePercent(priceChangePercent);
                                walletList.put(assetIndex, cryptocurrency);
                                wallet.put(new JSONObject().put(BASE_ASSET_KEY, assetIndex)
                                        .put(LAST_PRICE_KEY, lastPrice)
                                        .put(PRICE_CHANGE_PERCENT_KEY, priceChangePercent)
                                        .put(INCOME_PERCENT_KEY, cryptocurrency.getIncomePercent(2)));
                            }
                            androidWorkflow.insertRefreshedPrices(wallet);
                            wallet.clear();
                        }catch (Exception e){
                            printRed("Error during refreshing wallet list");
                        }finally {
                            try {
                                sleep(refreshTime);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * This method is used by traders to get user Binance's wallet balance. <br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @implNote if {@link #runningTrader} is false will return -1
     * @return wallet balance in currency value
     * **/
    public double getWalletBalance(boolean forceRefresh) throws Exception {
        if(runningTrader) {
            balance = super.getWalletBalance(baseCurrency, forceRefresh);
            androidWorkflow.insertWalletBalance(binanceMarketManager.roundValue(balance, 2));
            return balance;
        }
        return -1;
    }

    /**
     * This method is used by traders to get user Binance's wallet balance. <br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @param decimals: this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @implNote if {@link #runningTrader} is false will return -1
     * @return wallet balance in currency value
     * **/
    public double getWalletBalance(boolean forceRefresh, int decimals) throws Exception {
        if(runningTrader)
            return binanceMarketManager.roundValue(getWalletBalance(forceRefresh), decimals);
        return -1;
    }

    /**
     * This method is used to get asset list of Binance's user wallet.<br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    public ArrayList<Asset> getAssetsList(boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAssetsList(baseCurrency, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Binance's account from all {@link #quoteCurrencies} inserted.<br>
     *
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * @implNote if {@link #runningTrader} is false will return null
     **/
    @Wrapper
    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        return getAllTransactions(null, forceRefresh);
    }

    /**
     * This method is used to get all transactions for a Binance's account from all {@link #quoteCurrencies} inserted.<br>
     *
     * @param dateFormat:   this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * @implNote if {@link #runningTrader} is false will return null
     **/
    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if (runningTrader)
            return super.getAllTransactions(dateFormat, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Binance's account from a single symbol<br>
     *
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh:  this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * @implNote if {@link #runningTrader} is false will return null
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
     * @implNote if {@link #runningTrader} is false will return null
     **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat,
                                                      boolean forceRefresh) throws Exception {
        if (runningTrader)
            return super.getTransactionsList(quoteCurrency, dateFormat, forceRefresh);
        return null;
    }

    /**
     * This method is used to send a buy market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * @implNote if {@link #runningTrader} is false will do not the buy operation
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        if(runningTrader){
            super.buyMarket(symbol, quantity);
            this.symbol = symbol;
        }
    }

    /**
     * This method is used to send a sell market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * @implNote if {@link #runningTrader} is false will do not the sell operation
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        if(runningTrader){
            super.sellMarket(symbol, quantity);
            this.symbol = symbol;
        }
    }

    /**
     * This method is used to place an order<br>
     *
     * @param symbol:   this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to trade es. 10
     * @param side:     this indicates the side of the order (BUY or SELL)
     **/
    @Override
    protected void placeAnOrder(String symbol, double quantity, Side side) throws Exception {
        super.placeAnOrder(symbol, quantity, side);
        this.side = side;
    }

    /**
     * This method is used to insert or update a coin in {@link #coins} list.
     * @param index: index of the coin es. BTC
     * @param name: name of the coin es Bitcoin
     * @param quantity: quantity of that coin es. 0.28
     * @implNote in Android's interfaces this method updates also {@link #traderAccount} instance
     * stats and insert a new transaction
     * **/
    @Override
    protected void insertCoin(String index, String name, double quantity) {
        super.insertCoin(index, name, quantity);
        String quoteAsset = tradingPairsList.get(symbol).getQuoteAsset();
        Cryptocurrency cryptocurrency = walletList.get(index);
        TickerPriceChange ticker = lastPrices.get(symbol);
        double lastPrice = ticker.getLastPrice();
        Coin coin = coins.get(index);
        int sales = 0;
        Transaction transaction = new Transaction(symbol, side, transactionDateFormat.format(new Date(currentTimeMillis())),
                binanceMarketManager.roundValue(quantity * lastPrice, 2), quantity, quoteAsset, index);
        if (side.equals(SELL)) {
            double income = cryptocurrency.getIncomePercent();
            sales = androidWorkflow.getSellSales(transaction, traderAccount, cryptocurrency, getTypeSellCode(income));
            traderAccount.addIncome(income);
        }else{
            if(cryptocurrency == null) {
                cryptocurrency = new Cryptocurrency(index, getCryptocurrencyName(index), quantity,
                    symbol, lastPrice, -1 , null, ticker.getPriceChangePercent(), quoteAsset, null);
                cryptocurrency.addFirstPrice(lastPrice);
            }else {
                cryptocurrency.setQuantity(coin.getQuantity());
                cryptocurrency.addFirstPrice(lastPrice);
            }
            walletList.put(index, cryptocurrency);
        }
        if (coin.isTradingEnabled())
            androidWorkflow.insertCryptocurrency(cryptocurrency, transaction, sales, traderAccount.getTotalIncome(2));
        else {
            androidWorkflow.removeCryptocurrency(index, transaction);
            walletList.remove(index);
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
     * This method is used to get if bot is in running mode
     *
     * @return flag that indicates if the bot is running
     **/
    @Override
    public boolean isBotRunning() {
        return runningTrader;
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
            return traderAccount.getSalesAtLoss();
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
            return traderAccount.getSalesAtGain();
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
            return traderAccount.getSalesAtPair();
        return -1;
    }

    /**
     * This method is used to get total sales
     * @return total sales
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getTotalSales() {
        if(runningTrader)
            return traderAccount.getTotalSales();
        return -1;
    }

    /**
     * This method is used to set base currency for change amount value
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * **/
    @Override
    public void setBaseCurrency(String baseCurrency) {
        if (baseCurrency == null || baseCurrency.isEmpty())
            throw new IllegalArgumentException("Currency cannot be null or empty, but for example EUR or USD");
        this.baseCurrency = baseCurrency;
    }

    /**
     * This method is used to get base currency for change amount value <br>
     * Any params required
     * @implNote if {@link #runningTrader} is false will return null
     * **/
    @Override
    public String getBaseCurrency() {
        if(runningTrader)
            return baseCurrency;
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
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * **/
    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        androidWorkflow.setPrintRoutineMessages(printRoutineMessages);
    }

    /**
     * This method is used to get flag to print or not routine messages
     *
     * @return flag that indicates the possibility or not to print or not routine messages
     **/
    @Override
    public boolean canPrintRoutineMessages() {
        return androidWorkflow.canPrintRoutineMessages();
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
