package com.tecknobit.traderbot.traders.autonomous.interfaces;

import com.tecknobit.coinbasemanager.exchangepro.CoinbaseManager;
import com.tecknobit.coinbasemanager.exchangepro.products.records.Candle.Granularity;
import com.tecknobit.coinbasemanager.exchangepro.products.records.Ticker;
import com.tecknobit.traderbot.orders.MarketOrder;
import com.tecknobit.traderbot.records.account.TraderAccount;
import com.tecknobit.traderbot.records.portfolio.Coin;
import com.tecknobit.traderbot.records.portfolio.Cryptocurrency;
import com.tecknobit.traderbot.records.portfolio.Cryptocurrency.TradingConfig;
import com.tecknobit.traderbot.records.portfolio.Transaction;
import com.tecknobit.traderbot.routines.autonomous.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.traders.interfaces.CoinbaseTraderBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.apimanager.trading.TradingTools.roundValue;
import static com.tecknobit.coinbasemanager.exchangepro.products.records.Candle.Granularity._1d;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.USD_CURRENCY;
import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;

/**
 * The {@code CoinbaseAutoTraderBot} class is trader for {@link CoinbaseManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Coinbase's account autonomously.<br>
 * Is derived class of {@link CoinbaseTraderBot} class from inherit all core routines methods and instances.
 * @implNote for autonomous operations uses {@link AutoTraderCoreRoutines} and {@link MarketOrder} routines.
 * @implSpec {@link TraderAccount} and {@link #walletList} are objects that you have to save their data like losses, gains,
 * total income or cryptocurrencies that trader has bought, by yourself if not their data
 * will be not fetch autonomously, and they will be not more available if not restored by you.
 * @author Tecknobit N7ghtm4r3
 * **/
public class CoinbaseAutoTraderBot extends CoinbaseTraderBot implements AutoTraderCoreRoutines, MarketOrder {

    /**
     * {@code TraderAccount} is instance that memorizes and manage account information and trading reports of auto trader
     * account
     **/
    protected TraderAccount traderAccount;

    /**
     * {@code MIN_NOTIONAL_FILTER} is instance that contains key for {@code MIN_NOTIONAL} filter
     * **/
    public static final String MIN_NOTIONAL_FILTER = "MIN_NOTIONAL";

    /**
     * {@code LOT_SIZE_FILTER} is instance that contains key for {@code LOT_SIZE} filter
     * **/
    public static final String LOT_SIZE_FILTER = "LOT_SIZE";

    /**
     * {@code walletList} is a map that contains wallet list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * @implNote is used in {@link #buyCryptocurrencies()} and {@link #updateWallet()} routines
     * **/
    protected ConcurrentHashMap<String, Cryptocurrency> walletList;

    /**
     * {@code checkingList} is a map that contains checking list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * @implNote is used in {@link #checkCryptocurrencies()} routine
     * **/
    protected final HashMap<String, Cryptocurrency> checkingList;

    /**
     * {@code tradingConfig} is instance that memorizes model of trading to use for trading routines
     * **/
    protected TradingConfig tradingConfig;

    /**
     * {@code sendStatsReport} is instance that memorizes flag to insert to send or not reports
     * **/
    protected boolean sendStatsReport;

    /**
     * {@code printRoutineMessages} is instance that memorizes flag to insert to print or not routine messages
     * **/
    protected boolean printRoutineMessages;

    /**
     * {@code runningTrader} is instance that memorizes flag that indicates if the bot is running
     * **/
    protected boolean runningTrader;

    /**
     * {@code previousChecking} is instance that memorizes previous timestamp when {@link #checkCryptocurrencies()} is called
     * **/
    private long previousChecking;

    /**
     * {@code previousBuying} is instance that memorizes previous timestamp when {@link #buyCryptocurrencies()} is called
     * **/
    private long previousBuying;

    /**
     * {@code previousUpdating} is instance that memorizes previous timestamp when {@link #updateWallet()} is called
     * **/
    private long previousUpdating;

    /**
     * {@code previousTradingConfigFetching} is instance that memorizes previous trading confing fetching
     * **/
    protected long previousTradingConfigFetching;

    /**
     * {@code baseCurrency} is instance that memorizes base currency to get all amount value of traders routine es. EUR
     * **/
    protected String baseCurrency;

    /**
     * {@code cryptocurrencySold} is instance that memorizes cryptocurrency that is being sold
     * **/
    protected Cryptocurrency cryptocurrencySold;

    /**
     * {@code USING_WALLET_LIST} is instance that memorizes if wallet list is being using
     * **/
    protected volatile boolean USING_WALLET_LIST = false;

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, TraderAccount traderAccount, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        this.traderAccount = traderAccount;
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, int timeout, TraderAccount traderAccount,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout);
        this.traderAccount = traderAccount;
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 TraderAccount traderAccount, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage);
        this.traderAccount = traderAccount;
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, TraderAccount traderAccount,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase);
        this.traderAccount = traderAccount;
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, ArrayList<String> quoteCurrencies, TraderAccount traderAccount,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, traderAccount, sendStatsReport, printRoutineMessages,
                baseCurrency);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, int timeout, ArrayList<String> quoteCurrencies,
                                 TraderAccount traderAccount, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, timeout, traderAccount, sendStatsReport, printRoutineMessages, baseCurrency);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency : base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 ArrayList<String> quoteCurrencies, TraderAccount traderAccount, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, traderAccount, sendStatsReport, printRoutineMessages,
                baseCurrency);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies,
                                 TraderAccount traderAccount, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, traderAccount, sendStatsReport, printRoutineMessages, baseCurrency);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     *
     * @param apiKey:               your Coinbase's api key
     * @param apiSecret:            your Coinbase's secret key
     * @param passphrase:           your Coinbase's api passphrase
     * @param defaultErrorMessage:  custom error to show when is not a request error
     * @param timeout:              custom timeout for request
     * @param refreshTime:          is time in seconds to set to refresh data
     * @param traderAccount:        manage account information and trading reports of auto trader account
     * @param sendStatsReport:      flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency:         base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, int refreshTime, TraderAccount traderAccount, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, traderAccount, sendStatsReport, printRoutineMessages,
                baseCurrency);
        setRefreshTime(refreshTime);
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     *
     * @param apiKey:               your Coinbase's api key
     * @param apiSecret:            your Coinbase's secret key
     * @param passphrase:           your Coinbase's api passphrase
     * @param timeout:              custom timeout for request
     * @param refreshTime:          is time in seconds to set to refresh data
     * @param traderAccount:        manage account information and trading reports of auto trader account
     * @param sendStatsReport:      flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency:         base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, int timeout, int refreshTime,
                                 TraderAccount traderAccount, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, timeout, traderAccount, sendStatsReport, printRoutineMessages, baseCurrency);
        setRefreshTime(refreshTime);
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param refreshTime: is time in seconds to set to refresh data
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 TraderAccount traderAccount, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency, int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, traderAccount, sendStatsReport, printRoutineMessages,
                baseCurrency);
        setRefreshTime(refreshTime);
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     *
     * @param apiKey:               your Coinbase's api key
     * @param apiSecret:            your Coinbase's secret key
     * @param passphrase:           your Coinbase's api passphrase
     * @param refreshTime:          is time in seconds to set to refresh data
     * @param traderAccount:        manage account information and trading reports of auto trader account
     * @param sendStatsReport:      flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency:         base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, TraderAccount traderAccount,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                 int refreshTime) throws Exception {
        this(apiKey, apiSecret, passphrase, traderAccount, sendStatsReport, printRoutineMessages, baseCurrency);
        setRefreshTime(refreshTime);
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param refreshTime: is time in seconds to set to refresh data.
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, ArrayList<String> quoteCurrencies, int refreshTime,
                                 TraderAccount traderAccount, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, refreshTime, traderAccount, sendStatsReport,
                printRoutineMessages, baseCurrency);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param refreshTime: is time in seconds to set to refresh data.
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                                 ArrayList<String> quoteCurrencies, int refreshTime, TraderAccount traderAccount,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, timeout, refreshTime, traderAccount, sendStatsReport, printRoutineMessages,
                baseCurrency);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param refreshTime: is time in seconds to set to refresh data
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 ArrayList<String> quoteCurrencies, int refreshTime, TraderAccount traderAccount,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, defaultErrorMessage, traderAccount, sendStatsReport, printRoutineMessages, baseCurrency,
                refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link CoinbaseAutoTraderBot}
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param refreshTime: is time in seconds to set to refresh data
     * @param traderAccount: manage account information and trading reports of auto trader account
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies,
                                 int refreshTime, TraderAccount traderAccount, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        this(apiKey, apiSecret, passphrase, traderAccount, sendStatsReport, printRoutineMessages, baseCurrency,
                refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * This method is used to init wallet list when autonomous trader is started from second time onwards<br>
     * @param walletList: wallet list of your cryptocurrencies, stored by you <br>
     * Any return
     * **/
    @Override
    public void setWallet(ConcurrentHashMap<String, Cryptocurrency> walletList) {
        if(walletList == null)
            throw new IllegalArgumentException("Wallet cannot be null");
        this.walletList = walletList;
    }

    /**
     * This method is used to start {@link CoinbaseAutoTraderBot}<br>
     * Any params required
     * @implNote the running mode if is disabled (using {@link #disableBot()}) trader will not do more trading operations,
     * but trader will continue to listen for reactivation of the running mode
     * (using {@link #enableBot()}) and trading operations will start again.
     * **/
    @Override
    public void start() throws Exception {
        runningTrader = true;
        printDisclaimer();
        previousBuying = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        while (runningTrader){
                            if(makeRoutine(previousChecking, CHECKING_GAP_TIME)){
                                previousChecking = System.currentTimeMillis();
                                checkCryptocurrencies();
                            }
                            if(makeRoutine(previousBuying, BUYING_GAP_TIME)){
                                previousBuying = System.currentTimeMillis();
                                buyCryptocurrencies();
                            }
                        }
                        System.out.println("Bot is stopped, waiting for reactivation");
                        Thread.sleep(5000);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        updateWallet();
    }

    /**
     * This method is used to check list of possible new cryptocurrencies to buy using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    @Override
    public void checkCryptocurrencies() throws Exception {
        System.out.println("## CHECKING NEW CRYPTOCURRENCIES");
        if (makeRoutine(previousTradingConfigFetching, BUYING_GAP_TIME * 2)) {
            previousTradingConfigFetching = currentTimeMillis();
            tradingConfig = fetchTradingConfig(tradingConfig);
        }
        int daysGap = tradingConfig.getDaysGap();
        for (Ticker ticker : coinbaseProductsManager.getAllTickers()) {
            String symbol = ticker.getProductId();
            String quoteAsset = ticker.getQuoteAsset();
            if (quoteCurrencies.isEmpty() || quoteContained(quoteAsset)) {
                String baseAsset = ticker.getBaseAsset();
                Coin coin = coins.get(baseAsset);
                if (coin != null && !walletList.containsKey(baseAsset)) {
                    double lastPrice = ticker.getPrice();
                    double priceChangePercent = 0;
                    Cryptocurrency cryptocurrency = checkingList.get(baseAsset);
                    if (cryptocurrency != null) {
                        priceChangePercent = coinbaseProductsManager.getTrendPercent(cryptocurrency.getLastPrice(),
                                lastPrice, 8);
                    }
                    double tptop = isTradable(symbol, tradingConfig, _1d, priceChangePercent);
                    if (tptop != ASSET_NOT_TRADABLE) {
                        checkingList.put(baseAsset, new Cryptocurrency(baseAsset,
                                coin.getAssetName(),
                                0,
                                symbol,
                                lastPrice,
                                tptop,
                                _1d,
                                priceChangePercent,
                                quoteAsset,
                                tradingConfig
                        ));
                    }else
                        checkingList.remove(baseAsset);
                }
            }
        }
    }

    /**
     * This method is used to check if a {@link Cryptocurrency} when this method is called is respecting correct range gap
     * to be bought using {@link TradingConfig} model.
     * @param symbol: symbol used in checking phase es. BTCBUSD or BTC-USD
     * @param tradingConfig: model of trading to use as {@link TradingConfig}
     * @param candleInterval: interval gap to make forecast
     * @param priceChangePercent: percent gap of the symbol from previous day and day when the symbol is checked
     * @return value of {@link #computeTPTOPIndex(String, TradingConfig, Object, double)} if is correct and return
     * {@link #ASSET_NOT_TRADABLE} if is not respect {@link TradingConfig} model.
     * **/
    @Override
    public double isTradable(String symbol, TradingConfig tradingConfig, Object candleInterval,
                             double priceChangePercent) throws Exception {
        double wasteRange = tradingConfig.getWasteRange();
        if((abs(priceChangePercent - tradingConfig.getMarketPhase()) <= wasteRange) &&
                ((priceChangePercent >= tradingConfig.getMaxLoss()) && (priceChangePercent <= tradingConfig.getMaxGain()))){
            double tptop = computeTPTOPIndex(symbol, tradingConfig, candleInterval, wasteRange);
            if(tptop >= tradingConfig.getMinGainForOrder())
                return tptop;
        }
        return ASSET_NOT_TRADABLE;
    }

    /**
     * This method is used to check if a {@link Cryptocurrency} when this method is called is respecting correct range gap
     * to be bought using {@link TradingConfig} model.
     * @param symbol: symbol used in checking phase es. BTCBUSD or BTC-USD
     * @param tradingConfig: model of trading to use as {@link TradingConfig}
     * @param candleInterval: interval gap to make forecast
     * @return value of tptop index if is correct and return {@link #ASSET_NOT_TRADABLE} if is not respect {@link TradingConfig} model.
     * **/
    @Override
    public double computeTPTOPIndex(String symbol, TradingConfig tradingConfig, Object candleInterval,
                                    double wasteRange) throws Exception {
        return coinbaseProductsManager.getSymbolForecast(symbol, tradingConfig.getDaysGap(), (Granularity) candleInterval,
                tradingConfig.getWasteRange());
    }

    /**
     * This method is used to buy new cryptocurrencies from list loaded from {@link #checkCryptocurrencies()} routine
     * using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    @Override
    public void buyCryptocurrencies() throws Exception {
        while (USING_WALLET_LIST);
        USING_WALLET_LIST = true;
        System.out.println("## BUYING NEW CRYPTOCURRENCIES");
        for (Cryptocurrency cryptocurrency : checkingList.values()){
            String symbol = cryptocurrency.getSymbol();
            double quantity = getMarketOrderQuantity(cryptocurrency);
            if(quantity != -1) {
                try {
                    buyMarket(symbol, quantity);
                    cryptocurrency.setQuantity(quantity);
                    cryptocurrency.addFirstPrice(cryptocurrency.getLastPrice());
                    walletList.put(cryptocurrency.getAssetIndex(), cryptocurrency);
                    if(printRoutineMessages)
                        System.out.println("Buying [" + symbol + "], quantity: " + quantity);
                }catch (Exception e){
                    USING_WALLET_LIST = false;
                    printError(symbol, e);
                }
            }
        }
        USING_WALLET_LIST = false;
        checkingList.clear();
        if(printRoutineMessages) {
            printRed("### Transactions");
            for (Transaction transaction : getAllTransactions(true))
                transaction.printDetails();
        }
    }

    /**
     * This method is used to routine of update wallet of cryptocurrencies bought by auto trader. If {@link Cryptocurrency}
     * respect {@link TradingConfig} model that {@link Cryptocurrency} will be sold. <br>
     * Any params required
     * **/
    @Override
    public void updateWallet() throws Exception {
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    while (runningTrader){
                        if(makeRoutine(previousUpdating, UPDATING_GAP_TIME)){
                            previousUpdating = System.currentTimeMillis();
                            while (USING_WALLET_LIST);
                            USING_WALLET_LIST = true;
                            try {
                                if(walletList.size() > 0) {
                                    System.out.println("## UPDATING WALLET CRYPTOCURRENCIES");
                                    if(isRefreshTime())
                                        refreshLatestPrice();
                                    for (Cryptocurrency cryptocurrency : walletList.values()){
                                        String symbol = cryptocurrency.getSymbol();
                                        if(symbol == null)
                                            symbol = cryptocurrency.getAssetIndex() + USD_CURRENCY;
                                        TradingConfig tradingConfig = cryptocurrency.getTradingConfig();
                                        Ticker ticker = lastPrices.get(symbol);
                                        double lastPrice = ticker.getPrice();
                                        double incomePercent = coinbaseProductsManager.getTrendPercent(
                                                cryptocurrency.getFirstPrice(), lastPrice);
                                        double minGainOrder = tradingConfig.getMinGainForOrder();
                                        double tptopIndex = cryptocurrency.getTptopIndex();
                                        refreshCryptoDetails(cryptocurrency, incomePercent, lastPrice, ticker.getPriceChangePercent());
                                        if(incomePercent < tradingConfig.getMinGainForOrder() && incomePercent < tptopIndex){
                                            if(printRoutineMessages)
                                                System.out.println("Refreshing [" + symbol + "]");
                                        }else if(incomePercent <= tradingConfig.getMaxLoss())
                                            incrementSalesSale(cryptocurrency, LOSS_SELL);
                                        else if(incomePercent >= minGainOrder || incomePercent >= tptopIndex)
                                            incrementSalesSale(cryptocurrency, GAIN_SELL);
                                        else
                                            incrementSalesSale(cryptocurrency, PAIR_SELL);
                                    }
                                }
                                USING_WALLET_LIST = false;
                                if(printRoutineMessages){
                                    printRed("### Wallet");
                                    for (Cryptocurrency cryptocurrency : walletList.values())
                                        cryptocurrency.printDetails();
                                    System.out.println("## Balance amount: " + getWalletBalance(baseCurrency,
                                            false, 2) + " " + baseCurrency);
                                }
                            }catch (Exception e){
                                USING_WALLET_LIST = false;
                                printRed("Error during wallet updating");
                            }
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        printRed("Error during wallet updating");
                    }
                }
            }
        }.start();
    }

    /**
     * This method is used to increment sales detail
     * @param cryptocurrency: cryptocurrency used in the order
     * @param codeOpe: code of type of sell to increment
     * **/
    @Override
    public void incrementSalesSale(Cryptocurrency cryptocurrency, String codeOpe) throws Exception {
        cryptocurrencySold = cryptocurrency;
        sellMarket(cryptocurrency.getSymbol(), cryptocurrency.getQuantity());
        walletList.remove(cryptocurrency.getAssetIndex());
        switch (codeOpe){
            case LOSS_SELL:
                traderAccount.addLoss();
                if(printRoutineMessages) {
                    printRed("## Selling at loss [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextIncomePercent(2) + "]");
                }
                break;
            case GAIN_SELL:
                traderAccount.addGain();
                if(printRoutineMessages) {
                    printGreen("## Selling at gain [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextIncomePercent(2) +  "]");
                }
                break;
            default:
                traderAccount.addPair();
                if(printRoutineMessages) {
                    System.out.println("## Selling at pair [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextIncomePercent() +  "]");
                }
        }
        traderAccount.addIncome(cryptocurrency.getIncomePercent(2));
        if(printRoutineMessages)
            traderAccount.printDetails();
    }

    /**
     * This method is used to send a sell market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        super.sellMarket(symbol, quantity);
        if(sendStatsReport)
            sendStatsReport(cryptocurrencySold.getTradingConfig().getModelId(), cryptocurrencySold.getIncomePercent());
    }

    /**
     * This method is used to set flag to send stats report with {@link #sendStatsReport(long, double)} method
     * @param sendStatsReport: flag to insert to send or not reports
     * **/
    @Override
    public void setSendStatsReport(boolean sendStatsReport) {
        this.sendStatsReport = sendStatsReport;
    }

    /**
     * This method is used to get flag to send stats report with {@link #sendStatsReport(long, double)} method <br>
     * @return flag that indicates the possibility or not to send stats reports
     * **/
    @Override
    public boolean canSendStatsReport() {
        return sendStatsReport;
    }

    /**
     * This method is used to set flag to print routine messages
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * **/
    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        this.printRoutineMessages = printRoutineMessages;
    }

    /**
     * This method is used to get flag to print or not routine messages
     * @return flag that indicates the possibility or not to print or not routine messages
     * **/
    @Override
    public boolean canPrintRoutineMessages() {
        return printRoutineMessages;
    }

    /**
     * This method is used to get if bot is in running mode
     * @return flag that indicates if the bot is running
     * **/
    @Override
    public boolean isBotRunning() {
        return runningTrader;
    }

    /**
     * This method is used to disable running mode of bot
     * **/
    @Override
    public void disableBot() {
        runningTrader = false;
    }

    /**
     * This method is used to enable running mode of bot
     * **/
    @Override
    public void enableBot() {
        runningTrader = true;
    }

    /**
     * This method is used to get quantity for market order type
     * @param cryptocurrency: cryptocurrency as {@link Cryptocurrency} used in the order
     * @return quantity for the market order es. 1
     * **/
    @Override
    public double getMarketOrderQuantity(Cryptocurrency cryptocurrency) throws Exception {
        return getSuggestedOrderQuantity(cryptocurrency.getSymbol(),
                roundValue(getCoinBalance(cryptocurrency.getQuoteAsset()) * cryptocurrency.getTptopIndex() / 100,
                        6));
    }

    /**
     * This method is used to get sales at loss
     * @return sales at loss
     * **/
    @Override
    public double getSalesAtLoss() {
        return traderAccount.getSalesAtLoss();
    }

    /**
     * This method is used to get sales at gain
     * @return sales at gain
     * **/
    @Override
    public double getSalesAtGain() {
        return traderAccount.getSalesAtGain();
    }

    /**
     * This method is used to get sales at pair
     * @return sales at pair
     * **/
    @Override
    public double getSalesInPair() {
        return traderAccount.getSalesAtPair();
    }

    /**
     * This method is used to get total sales
     * @return total sales
     * **/
    @Override
    public double getTotalSales() {
        return traderAccount.getTotalSales();
    }

    /**
     * This method is used to set base currency for change amount value
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * **/
    @Override
    public void setBaseCurrency(String baseCurrency) {
        if(baseCurrency == null || baseCurrency.isEmpty())
            throw new IllegalArgumentException("Currency cannot be null or empty, but for example EUR or USD");
        this.baseCurrency = baseCurrency;
    }

    /**
     * This method is used to get base currency for change amount value <br>
     * Any params required
     * **/
    @Override
    public String getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * This method is used to get trader account object
     * @return trader account as {@link TraderAccount} object
     * **/
    @Override
    public TraderAccount getTraderAccount() {
        return traderAccount;
    }

    /**
     * This method is used to get wallet list
     * @return wallet list as {@link ConcurrentHashMap} object of {@link Cryptocurrency}
     * **/
    @Override
    public ConcurrentHashMap<String, Cryptocurrency> getWallet() {
        return walletList;
    }

}
