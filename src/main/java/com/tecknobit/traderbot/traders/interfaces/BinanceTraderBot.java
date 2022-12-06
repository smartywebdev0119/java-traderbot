package com.tecknobit.traderbot.traders.interfaces;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.binancemanager.managers.BinanceManager;
import com.tecknobit.binancemanager.managers.BinanceManager.BinanceEndpoint;
import com.tecknobit.binancemanager.managers.market.BinanceMarketManager;
import com.tecknobit.binancemanager.managers.market.records.stats.ExchangeInformation.Filter;
import com.tecknobit.binancemanager.managers.market.records.stats.ExchangeInformation.Filter.FilterDetails;
import com.tecknobit.binancemanager.managers.market.records.stats.ExchangeInformation.Symbol;
import com.tecknobit.binancemanager.managers.market.records.tickers.TickerPriceChange;
import com.tecknobit.binancemanager.managers.signedmanagers.trade.commons.Order;
import com.tecknobit.binancemanager.managers.signedmanagers.trade.spot.BinanceSpotManager;
import com.tecknobit.binancemanager.managers.signedmanagers.trade.spot.records.orders.response.SpotOrderStatus;
import com.tecknobit.binancemanager.managers.signedmanagers.wallet.BinanceWalletManager;
import com.tecknobit.binancemanager.managers.signedmanagers.wallet.records.asset.CoinInformation;
import com.tecknobit.traderbot.records.portfolio.Asset;
import com.tecknobit.traderbot.records.portfolio.Coin;
import com.tecknobit.traderbot.records.portfolio.MarketCoin;
import com.tecknobit.traderbot.records.portfolio.Transaction;
import com.tecknobit.traderbot.routines.interfaces.TraderCoreRoutines;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.tecknobit.apimanager.trading.TradingTools.roundValue;
import static com.tecknobit.binancemanager.managers.BinanceManager.ReturnFormat.STRING;
import static com.tecknobit.binancemanager.managers.market.records.stats.ExchangeInformation.Filter.FilterType.LOT_SIZE;
import static com.tecknobit.binancemanager.managers.market.records.stats.ExchangeInformation.Filter.FilterType.MIN_NOTIONAL;
import static com.tecknobit.binancemanager.managers.signedmanagers.trade.commons.Order.Status.FILLED;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.*;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.Side.BUY;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.Side.SELL;
import static java.lang.Math.ceil;

/**
 * The {@code BinanceTraderBot} class is trader for {@link BinanceManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Binance's account.<br>
 * Is derived class of {@link TraderCoreRoutines} class from inherit all core routines methods and instances.
 *
 * @author Tecknobit N7ghtm4r3
 **/
public class BinanceTraderBot extends TraderCoreRoutines {

    /**
     * {@code BINANCE_BASE_ENDPOINTS} is a list constant that contains list of Binance's main endpoints
     **/
    public static final String[] BINANCE_BASE_ENDPOINTS;

    static {
        BinanceEndpoint[] binanceEndpoints = BinanceEndpoint.values();
        BINANCE_BASE_ENDPOINTS = new String[binanceEndpoints.length];
        for (int j = 0; j < binanceEndpoints.length; j++)
            BINANCE_BASE_ENDPOINTS[j] = binanceEndpoints[j].toString();
    }

    /**
     * {@code BUSD_CURRENCY} is the identifier of BUSD currency used by Binance's traders
     **/
    public static final String BUSD_CURRENCY = "BUSD";

    /**
     * {@code binanceWalletManager} is instance of {@link BinanceWalletManager} helpful to wallet operations
     **/
    protected final BinanceWalletManager binanceWalletManager;

    /**
     * {@code binanceMarketManager} is instance of {@link BinanceMarketManager} helpful to market requests
     * **/
    protected final BinanceMarketManager binanceMarketManager;

    /**
     * {@code binanceSpotManager} is instance of {@link BinanceSpotManager} helpful to spot operations
     * **/
    protected final BinanceSpotManager binanceSpotManager;

    /**
     * {@code tradingPairsList} is a map that contains symbols of trading pair list
     * @implSpec this map has as key the asset index (es. BTC) as {@link String} and has as value custom object
     * {@link Symbol} give by {@link BinanceManager} library.
     * **/
    protected HashMap<String, Symbol> tradingPairsList;

    /**
     * {@code lastPrices} is a map that contains asset index (es. BTC) as key {@link String} and last ticker as {@link TickerPriceChange}
     * @apiNote values inserted in this map are only tickers of coins inserted in {@link #coins} list
     * @implNote refresh of last prices, by default, is every 10 seconds, but you can set programmatically
     * {@link #refreshTime} to customize refresh time.
     * **/
    protected HashMap<String, TickerPriceChange> lastPrices;

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey) throws Exception {
        this(apiKey, secretKey, (String) null);
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param baseEndpoint: base endpoint choose from BinanceTraderBot.BINANCE_BASE_ENDPOINTS array
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws Exception {
        binanceWalletManager = new BinanceWalletManager(baseEndpoint, apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager();
        binanceMarketManager = new BinanceMarketManager();
        refreshTime = 10000;
        initTrader();
    }

    /**
     * Constructor to init {@link BinanceTraderBot}
     *
     * @param apiKey:      your Binance's api key
     * @param secretKey:   your Binance's secret key
     * @param refreshTime: is time in seconds to set to refresh data
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public BinanceTraderBot(String apiKey, String secretKey, int refreshTime) throws Exception {
        this(apiKey, secretKey);
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * Constructor to init {@link BinanceTraderBot}
     *
     * @param apiKey:       your Binance's api key
     * @param secretKey:    your Binance's secret key
     * @param refreshTime:  is time in seconds to set to refresh data
     * @param baseEndpoint: base endpoint choose from BinanceTraderBot.BINANCE_BASE_ENDPOINTS array
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint, int refreshTime) throws Exception {
        this(apiKey, secretKey, baseEndpoint);
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * Constructor to init {@link BinanceTraderBot}
     *
     * @param apiKey:          your Binance's api key
     * @param secretKey:       your Binance's secret key
     * @param refreshTime:     is time in seconds to set to refresh data
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public BinanceTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                            int refreshTime) throws Exception {
        this(apiKey, secretKey, refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * Constructor to init {@link BinanceTraderBot}
     *
     * @param apiKey:          your Binance's api key
     * @param secretKey:       your Binance's secret key
     * @param refreshTime:     is time in seconds to set to refresh data
     * @param baseEndpoint:    base endpoint choose from BinanceTraderBot.BINANCE_BASE_ENDPOINTS array
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @throws IllegalArgumentException if {@code refreshTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by library anywhere.
     **/
    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                            int refreshTime) throws Exception {
        this(apiKey, secretKey, baseEndpoint, refreshTime);
        this.quoteCurrencies = quoteCurrencies;
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies) throws Exception {
        this(apiKey, secretKey);
        this.quoteCurrencies = quoteCurrencies;
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param baseEndpoint: base endpoint choose from BinanceTraderBot.BINANCE_BASE_ENDPOINTS array
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @implNote these keys will NOT store by library anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint,
                            ArrayList<String> quoteCurrencies) throws Exception {
        this(apiKey, secretKey, baseEndpoint);
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * This method init all instances that {@link BinanceTraderBot}'s routine need. <br>
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
     * This method is used by traders to get user Binance's wallet balance. <br>
     *
     * @param currency:     currency of balance value es. EUR will return balance in EUR currency
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @param decimals:     this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @return wallet balance in currency value
     **/
    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception {
        return binanceMarketManager.roundValue(getWalletBalance(currency, forceRefresh), decimals);
    }

    /**
     * This method is used by traders to get user Binance's wallet balance. <br>
     *
     * @param currency:     currency of balance value es. EUR will return balance in EUR currency
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @return wallet balance in currency value
     **/
    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws Exception {
        if (isRefreshTime() || !lastBalanceCurrency.equals(currency) || forceRefresh) {
            refreshLatestPrice();
            lastBalanceCurrency = currency;
            balance = 0;
            for (Coin coin : coins.values())
                if(coin.isTradingEnabled()) {
                    String assetIndex = coin.getAssetIndex();
                    double lastPrice;
                    if (assetIndex.equals(BUSD_CURRENCY)) {
                        try {
                            lastPrice = binanceMarketManager.getCurrentAveragePriceValue(BUSD_CURRENCY + USDT_CURRENCY);
                        } catch (IOException e) {
                            return 0;
                        }
                    } else
                        lastPrice = lastPrices.get(assetIndex + BUSD_CURRENCY).getLastPrice();
                    balance += coin.getQuantity() * lastPrice;
                }
            if (!currency.contains(USD_CURRENCY))
                balance = convertFromUsd(currency, balance, 8);
        }
        return balance;
    }

    /**
     * This method is used to get asset list of Binance's user wallet.<br>
     * @param currency: currency of asset balance value es. EUR will return asset balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || !lastAssetCurrency.equals(currency) || forceRefresh){
            refreshLatestPrice();
            assets.clear();
            lastAssetCurrency = currency;
            for (Coin coin : coins.values()){
                if(coin.isTradingEnabled()) {
                    double free = coin.getQuantity();
                    String asset = coin.getAssetIndex();
                    if (asset.equals(BUSD_CURRENCY))
                        asset = BUSD_CURRENCY;
                    double value = free * lastPrices.get(asset + BUSD_CURRENCY).getLastPrice();
                    if (!currency.contains(USD_CURRENCY))
                        value = convertFromUsd(currency, value, 8);
                    assets.add(new Asset(asset,
                            coin.getAssetName(),
                            free,
                            value,
                            currency
                    ));
                }
            }
        }
        return assets;
    }

    /**
     * This method is used to get all transactions for a Binance's account from all {@link #quoteCurrencies} inserted.<br>
     *
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Override
    @Wrapper(wrapper_of = "getAllTransactions(String dateFormat, boolean forceRefresh)")
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        return getAllTransactions(null, forceRefresh);
    }

    /**
     * This method is used to get all transactions for a Binance's account from all {@link #quoteCurrencies} inserted.<br>
     *
     * @param dateFormat:   this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if (isRefreshTime() || allTransactions.isEmpty() || forceRefresh) {
            allTransactions.clear();
            for (String quoteCurrency : quoteCurrencies)
                allTransactions.addAll(getTransactionsList(quoteCurrency, dateFormat, forceRefresh));
            transactions.clear();
        }
        return allTransactions;
    }

    /**
     * This method is used to get all transactions for a Binance's account from a single symbol<br>
     *
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh:  this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     **/
    @Override
    @Wrapper(wrapper_of = "getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh)")
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
            refreshLatestPrice();
            lastTransactionCurrency = quoteCurrency;
            transactions.clear();
            for (Coin coin : coins.values()) {
                if (coin.isTradingEnabled()) {
                    String baseAsset = coin.getAssetIndex();
                    String symbol = baseAsset + lastTransactionCurrency;
                    if (!symbol.startsWith(lastTransactionCurrency)) {
                        try {
                            for (SpotOrderStatus order : binanceSpotManager.getAllOrders(symbol)) {
                                if (order.getStatus().equals(FILLED)) {
                                    String date;
                                    if (dateFormat != null)
                                        date = Transaction.getDate(order.getTime(), dateFormat);
                                    else
                                        date = new Date(order.getTime()).toString();
                                    transactions.add(new Transaction(symbol,
                                            Side.valueOf(order.getSide().name()),
                                            date,
                                            order.getCummulativeQuoteQty(),
                                            order.getOrigQty(),
                                            lastTransactionCurrency,
                                            baseAsset
                                    ));
                                }
                            }
                        }catch (JSONException ignored){
                        }
                    }
                }
            }
        }
        return transactions;
    }

    /**
     * This method is used to send a buy market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        placeAnOrder(symbol, quantity, BUY);
        int statusCode = binanceSpotManager.getStatusResponse();
        if(statusCode == 200) {
            Symbol coinSymbol = tradingPairsList.get(symbol);
            insertQuoteCurrency(coinSymbol.getQuoteAsset());
            String baseAsset = coinSymbol.getBaseAsset();
            Coin coin = coins.get(baseAsset);
            insertCoin(baseAsset, coin.getAssetName(), coin.getQuantity() + quantity);
        }else {
            throw new Exception("Error during buy order status code: [" + statusCode + "]" +
                    " error message: [" + binanceSpotManager.getErrorResponse() + "]");
        }
    }

    /**
     * This method is used to send a sell market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        Symbol coinSymbol = tradingPairsList.get(symbol);
        String baseAsset = coinSymbol.getBaseAsset();
        Coin coin = coins.get(baseAsset);
        if(coin != null && coin.isTradingEnabled()) {
            placeAnOrder(symbol, quantity, SELL);
            int statusCode = binanceSpotManager.getStatusResponse();
            if(statusCode == 200)
                insertCoin(baseAsset, coin.getAssetName(), coin.getQuantity() - quantity);
            else {
                throw new Exception("Error during sell order status code: [" + statusCode + "]" +
                        " error message: [" + binanceSpotManager.getErrorResponse() + "]");
            }
        }else
            throw new Exception("Your wallet doesn't have this coin to sell [" + symbol + "]");
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
        orderStatus = binanceSpotManager.sendMarketOrderQty(symbol, Order.Side.valueOf(side.name()), quantity, null,
                STRING);
    }

    /**
     * This method is used to get error of any requests<br>
     * Any params required
     **/
    @Override
    public String getErrorResponse() {
        return binanceSpotManager.getErrorResponse();
    }

    /**
     * Method to print error response <br>
     * Any params required
     **/
    @Override
    public void printErrorMessage() {
        binanceSpotManager.printErrorResponse();
    }

    /**
     * This method is used to refresh latest prices<br>
     * Any params required
     **/
    @Override
    public synchronized void refreshLatestPrice() throws Exception {
        lastPricesRefresh = System.currentTimeMillis();
        for (CoinInformation coin : binanceWalletManager.getAllCoins()) {
            double free = coin.getFree();
            boolean isTradingEnable = true;
            String index = coin.getCoin();
            if (free == 0 || !coin.canTrading())
                isTradingEnable = false;
            coins.put(index, new Coin(index,
                    coin.getName(),
                    free,
                    isTradingEnable
            ));
        }
        for (Symbol symbol : binanceMarketManager.getExchangeInformation().getSymbols())
            tradingPairsList.put(symbol.getSymbol(), symbol);
        for (TickerPriceChange tickerPriceChange : binanceMarketManager.getTickersPriceChangeList()) {
            String symbol = tickerPriceChange.getSymbol();
            try {
                if (coins.get(tradingPairsList.get(symbol).getBaseAsset()).isTradingEnabled() || symbol.endsWith(BUSD_CURRENCY))
                    lastPrices.put(symbol, tickerPriceChange);
            } catch (NullPointerException ignored) {
            }
        }
    }

    /**
     * This method is used print error when request is made, if error is not in request
     * will print {@link Exception} error message
     * **/
    @Override
    protected void printError(String symbol, Exception e){
        if (binanceSpotManager.getStatusResponse() != SUCCESSFUL_RESPONSE)
            System.out.println(getErrorResponse() + " on [" + symbol + "]");
        else
            e.printStackTrace();
    }

    /**
     * This method is to compute suggested quantity for an order
     * @param symbol: symbol of cryptocurrency for the order
     * @param testQuantity: quantity to test
     * @return suggested quantity value computed from exchange's limits as double
     * **/
    @Override
    public double getSuggestedOrderQuantity(String symbol, double testQuantity) throws Exception {
        Symbol exchangeInformation = tradingPairsList.get(symbol);
        if(exchangeInformation != null){
            double stepSize = 0, maxQty = 0, minQty = 0, minNotional = 0, quantity = -1;
            double lastPrice = lastPrices.get(symbol).getLastPrice();
            double balance = lastPrice * testQuantity;
            for (Filter filter : exchangeInformation.getFiltersList()) {
                ArrayList<FilterDetails> filters = filter.getFilterDetails();
                if (filter.getFilterType().equals(LOT_SIZE)) {
                    JSONObject lotSize = getFilter(filter.getFilterDetails(), LOT_SIZE);
                    stepSize = lotSize.getDouble("stepSize");
                    maxQty = lotSize.getDouble("maxQty");
                    minQty = lotSize.getDouble("minQty");
                } else if (filter.getFilterType().equals(MIN_NOTIONAL)) {
                    minNotional = getFilter(filter.getFilterDetails(), MIN_NOTIONAL).getDouble("minNotional");
                    break;
                }
            }
            double minNotionalQty = minNotional / lastPrice;
            if(balance == minNotional)
                quantity = ceil(minNotional);
            else if(balance > minNotional){
                if(quantity < minQty)
                    quantity = minQty;
                else if(quantity > maxQty)
                    quantity = maxQty;
                else {
                    if ((quantity - minQty) % stepSize != 0)
                        quantity = ceil(quantity);
                    if (quantity < minNotionalQty)
                        quantity = ceil(minNotionalQty);
                }
            }
            return quantity;
        }
        throw new Exception("Symbol does not exist");
    }

    /**
     * Method to get a filter details
     *
     * @param filters: filters list
     * @param type:    type of the filter to fetch
     * @return filter details as {@link JSONObject}
     **/
    // TODO: 06/12/2022 TEST
    private JSONObject getFilter(ArrayList<FilterDetails> filters, Filter.FilterType type) {
        for (FilterDetails filter : filters)
            if (filter.getKey().equals(type.name()))
                return new JSONObject(filter.toString());
        return new JSONObject();
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
        for (TickerPriceChange tickerPriceChange : this.lastPrices.values()) {
            lastPrices.add(new MarketCoin(tickerPriceChange.getLastPrice(decimals),
                    tickerPriceChange.getPriceChangePercent(decimals)));
        }
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
        for (TickerPriceChange tickerPriceChange : this.lastPrices.values())
            lastPrices.add(new MarketCoin(tickerPriceChange.getLastPrice(), tickerPriceChange.getPriceChangePercent()));
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
        TickerPriceChange tickerPriceChange = lastPrices.get(symbol);
        if (tickerPriceChange == null)
            return new MarketCoin(0, 0);
        return new MarketCoin(tickerPriceChange.getLastPrice(decimals),
                tickerPriceChange.getPriceChangePercent(decimals)
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
        TickerPriceChange tickerPriceChange = lastPrices.get(symbol);
        if (tickerPriceChange == null)
            return new MarketCoin(0, 0);
        return new MarketCoin(tickerPriceChange.getLastPrice(),
                tickerPriceChange.getPriceChangePercent()
        );
    }

    /**
     * This method is used to get coin balance
     *
     * @param quote: string of quote currency to return amount value of balance
     * @return balance of coin inserted
     **/
    @Override
    public double getCoinBalance(String quote) {
        Coin coin = coins.get(quote);
        String assetIndex = coin.getAssetIndex();
        double quantity = coin.getQuantity();
        if (assetIndex.equals(BUSD_CURRENCY)) {
            try {
                return roundValue(binanceMarketManager.getCurrentAveragePriceValue(BUSD_CURRENCY + USDT_CURRENCY)
                        * quantity, 8);
            } catch (IOException e) {
                return -1;
            }
        }
        return roundValue(quantity * lastPrices.get(assetIndex + BUSD_CURRENCY).getLastPrice(), 8);
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
        double usdValue = lastPrices.get(currency + BUSD_CURRENCY).getLastPrice();
        if (usdValue < 1)
            return roundValue(valueToConvert * usdValue, 8);
        return roundValue(valueToConvert / usdValue, 8);
    }

}
