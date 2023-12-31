package com.tecknobit.traderbot.records.account;

import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.traderbot.records.portfolio.Cryptocurrency;
import com.tecknobit.traderbot.records.portfolio.Cryptocurrency.TradingConfig;
import com.tecknobit.traderbot.routines.android.ServerRequest;
import com.tecknobit.traderbot.routines.interfaces.RecordDetails;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.apimanager.trading.TradingTools.roundValue;
import static com.tecknobit.apimanager.trading.TradingTools.textualizeAssetPercent;
import static com.tecknobit.traderbot.records.portfolio.Cryptocurrency.TradingConfig.*;
import static com.tecknobit.traderbot.routines.android.AndroidWorkflow.*;
import static com.tecknobit.traderbot.routines.android.ServerRequest.*;
import static com.tecknobit.traderbot.routines.autonomous.AutoTraderCoreRoutines.ASSET_NOT_TRADABLE;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.*;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

/**
 * The {@code TraderAccount} class is useful to contains reports of trading. <br>
 * Is useful for autonomous traders and Android's traders.
 *
 * @author Tecknobit N7ghtm4r3
 **/
public final class TraderAccount extends TecknobitBot implements RecordDetails {

    /**
     * {@code salesAtLoss} is instance that memorizes sales at loss for account
     * **/
    private int salesAtLoss;

    /**
     * {@code salesAtGain} is instance that memorizes sales at gain for account
     * **/
    private int salesAtGain;

    /**
     * {@code salesAtPair} is instance that memorizes sales at pair for account
     * **/
    private int salesAtPair;

    /**
     * {@code activationDate} is instance that memorizes activation date for account
     * **/
    private long activationDate;

    /**
     * {@code incomes} is instance that memorizes list of incomes from orders
     * **/
    private ArrayList<Double> incomes;

    /**
     * {@code cryptocurrencies} is instance that memorizes list of cryptocurrencies in your waller
     * **/
    private final ConcurrentHashMap<String, Cryptocurrency> cryptocurrencies;

    /**
     * {@code totalIncome} is instance that memorizes total income for account
     * **/
    private double totalIncome;

    /** Constructor to init {@link TraderAccount}
     * @param salesAtLoss: sales at loss for account
     * @param salesAtGain: sales at gain for account
     * @param salesAtPair: sales at pair for account
     * @param activationDate: date when trader has been activated
     * @param totalIncome: total income gained for account
     * **/
    public TraderAccount(int salesAtLoss, int salesAtGain, int salesAtPair, long activationDate, double totalIncome) {
        this.salesAtLoss = salesAtLoss;
        this.salesAtGain = salesAtGain;
        this.salesAtPair = salesAtPair;
        this.activationDate = activationDate;
        this.totalIncome = totalIncome;
        cryptocurrencies = null;
        initTimeFormatters();
    }

    /** Constructor to init {@link TraderAccount}
     * @param salesAtLoss: sales at loss for account
     * @param salesAtGain: sales at gain for account
     * @param salesAtPair: sales at pair for account
     * @param activationDate: date when trader has been activated
     * @param incomes: list of past incomes
     * **/
    public TraderAccount(int salesAtLoss, int salesAtGain, int salesAtPair, long activationDate,
                         ArrayList<Double> incomes) {
        this.salesAtLoss = salesAtLoss;
        this.salesAtGain = salesAtGain;
        this.salesAtPair = salesAtPair;
        this.activationDate = activationDate;
        this.incomes = incomes;
        cryptocurrencies = null;
        initTimeFormatters();
    }

    /**
     * Constructor to init {@link TraderAccount}
     *
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @implNote is useful for Android's use
     **/
    public TraderAccount(Credentials credentials) throws Exception {
        cryptocurrencies = new ConcurrentHashMap<>();
        ServerRequest serverRequest = new ServerRequest(credentials, HOST, PORT);
        serverRequest.sendTokenRequest(new JSONObject(), GET_ACCOUNT_OPE);
        response = serverRequest.readResponse();
        if (response != null) {
            if (response.getInt(STATUS_CODE) == SUCCESSFUL_RESPONSE) {
                salesAtLoss = response.getInt(LOSSES_KEY);
                salesAtGain = response.getInt(GAINS_KEY);
                salesAtPair = response.getInt(PAIRS_KEY);
                activationDate = response.getLong(ACTIVATION_DATE_KEY);
                incomes = new ArrayList<>();
                JSONArray incomesList = response.getJSONArray(INCOMES_KEY);
                for (int j=0; j < incomesList.length(); j++)
                    incomes.add(incomesList.getDouble(j));
                JSONArray cryptocurrencies = response.getJSONArray(CRYPTOCURRENCY_KEY);
                for (int j=0; j < cryptocurrencies.length(); j++){
                    JSONObject crypto = cryptocurrencies.getJSONObject(j);
                    JsonHelper jsonHelper = new JsonHelper(crypto);
                    TradingConfig tradingConfig = null;
                    JSONObject config = jsonHelper.getJSONObject(TRADING_CONFIG_KEY);
                    if(config != null){
                        tradingConfig = new TradingConfig(config.getLong(MODEL_ID_KEY),
                                config.getDouble(MARKET_PHASE_KEY),
                                config.getDouble(WASTE_RANGE_KEY),
                                config.getInt(DAIS_GAP_KEY),
                                config.getDouble(MIN_GAIN_FOR_ORDER_KEY),
                                config.getDouble(MAX_LOSS_KEY),
                                config.getDouble(MAX_GAIN_KEY)
                        );
                    }
                    String index = crypto.getString(BASE_ASSET_KEY);
                    this.cryptocurrencies.put(index, new Cryptocurrency(index,
                            crypto.getString(ASSET_NAME_KEY),
                            crypto.getDouble(QUANTITY_KEY),
                            tradingConfig,
                            crypto.getDouble(TPTOP_INDEX_KEY),
                            jsonHelper.get(CANDLE_GAP_KEY),
                            crypto.getString(QUOTE_ASSET_KEY),
                            crypto.getDouble(FIRST_PRICES_SUM_KEY),
                            crypto.getInt(FIRST_PRICES_SIZE_KEY)
                    ));
                }
                totalIncome = ASSET_NOT_TRADABLE;
                initTimeFormatters();
            }else
                throw new IllegalAccessException("Operation failed");
        }
    }

    /** Constructor to init {@link TraderAccount} <br>
     * @implNote this constructor must call to init autonomous traders at their first start, in next starts you will
     * have to call {@link #TraderAccount(int salesAtLoss, int salesAtGain, int salesAtPair, long activationDate, ArrayList incomes)}
     * constructor with your data from you stored to start normal flow of autonomous traders
     * **/
    public TraderAccount() {
        salesAtLoss = 0;
        salesAtGain = 0;
        salesAtPair = 0;
        activationDate = currentTimeMillis();
        totalIncome = 0;
        incomes = new ArrayList<>();
        cryptocurrencies = null;
        initTimeFormatters();
    }

    /**
     * This method is used to get sales at loss
     * @return sales at loss
     * **/
    public int getSalesAtLoss() {
        return salesAtLoss;
    }

    /**
     * This method is used to increment sales at loss<br>
     * Any params required
     * **/
    public void addLoss(){
        salesAtLoss++;
    }

    /**
     * This method is used to get sales at gain
     * @return sales at gain
     * **/
    public int getSalesAtGain() {
        return salesAtGain;
    }

    /**
     * This method is used to add increment sales at gain <br>
     * Any params required
     * **/
    public void addGain(){
        salesAtGain++;
    }

    /**
     * This method is used to get sales at pair
     * @return sales at loss
     * **/
    public int getSalesAtPair() {
        return salesAtPair;
    }

    /**
     * This method is used to increment sales at pair <br>
     * Any params required
     * **/
    public void addPair(){
        salesAtPair++;
    }

    /**
     * This method is used to get total sales
     * @return total sales
     * **/
    public int getTotalSales(){
        return salesAtLoss + salesAtGain + salesAtPair;
    }

    /**
     * This method is used to get activation date of account <br>
     * Any params required
     *
     * @return {@link #activationDate} as activation date of account as long
     **/
    public long getActivationDateTimestamp() {
        return activationDate;
    }

    /**
     * This method is used to get activation date of account <br>
     * Any params required
     * @return {@link #activationDate} as activation date of account as {@link String}
     * **/
    public String getActivationDate() {
        return timeFormat.format(new Date(activationDate));
    }

    public ArrayList<Double> getIncomes() {
        return incomes;
    }

    /**
     * This method is used to add increment in {@link #incomes} list
     * @param newIncome: new income to insert
     * **/
    public void addIncome(double newIncome){
        if(newIncome < -100)
            throw new IllegalArgumentException("Income value cannot be less than -100");
        incomes.add(newIncome);
    }

    /**
     * This method is used to print all income from {@link #incomes} list <br>
     * Any params required
     * **/
    public void printIncomes() {
        for (double income : incomes)
            out.print(getIncomeColorLine("## Income: ", income));
    }

    /**
     * This method is used to calculate general profit of account
     * Any params required
     * @return general profit of account
     * **/
    public double computeTotalIncome(){
        if(totalIncome == ASSET_NOT_TRADABLE || incomes != null){
            double totalIncome = 0;
            for (double income : incomes)
                totalIncome += income;
            return totalIncome / incomes.size();
        }
        return totalIncome;
    }

    /**
     * This method is used to calculate general profit of account
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted with decimals digits
     * **/
    public double computeTotalIncome(int decimals){
        return roundValue(computeTotalIncome(), decimals);
    }

    /**
     * This method is used to format like a {@link String} general income from {@link #computeTotalIncome()}
     * Any params required
     * @return general income of account formatted as {@link String} es. +1.674% or -1.756%
     * **/
    public String textualizeTotalIncome(){
        return textualizeAssetPercent(computeTotalIncome());
    }

    /**
     * This method is used to format like a {@link String} general income from {@link #computeTotalIncome()}
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted as {@link String} es. +1.67% or -1.75%
     * **/
    public String textualizeTotalIncome(int decimals){
        return textualizeAssetPercent(computeTotalIncome(decimals));
    }

    /**
     * This method is used to get total income for account <br>
     * Any params required
     * @return general profit of account
     * **/
    public double getTotalIncome() {
        return computeTotalIncome();
    }

    /**
     * This method is used to get total income for account
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted with decimals digits
     * **/
    public double getTotalIncome(int decimals) {
        return computeTotalIncome(2);
    }

    /**
     * This method is used to get wallet list of cryptocurrencies <br>
     * Any params required
     *
     * @return wallet list as {@link ConcurrentHashMap} of {@link Cryptocurrency}
     * @implNote is useful and able to use only in Android's interfaces
     **/
    public ConcurrentHashMap<String, Cryptocurrency> getWalletCryptocurrencies() {
        return cryptocurrencies;
    }

    /**
     * This method is used to get account details <br>
     * Any params required
     *
     * @return account details as {@link HashMap} of {@link Object}
     **/
    public HashMap<String, Object> getAccount() {
        HashMap<String, Object> trader = new HashMap<>();
        trader.put(ACTIVATION_DATE_KEY, activationDate);
        trader.put(GAINS_KEY, salesAtGain);
        trader.put(LOSSES_KEY, salesAtLoss);
        trader.put(PAIRS_KEY, salesAtPair);
        trader.put(TOTAL_INCOME_KEY, totalIncome);
        return trader;
    }

    /**
     * Returns a string pretty printed of the representation of the object <br>
     * Any params required
     *
     * @return a string representation of the object as {@link String}
     */
    public String prettyPrint() {
        return new JSONObject(this).toString();
    }

    /**
     * This method is used to print details of {@link TraderAccount} object <br>
     * Any params required
     **/
    @Override
    public void printDetails() {
        out.println(this);
    }

    /**
     * Returns a string representation of the object <br>
     * Any params required
     *
     * @return a string representation of the object as {@link String}
     */
    @Override
    public String toString() {
        return "### [ ACCOUNT STATUS ]\n" +
                "## Total: " + getTotalSales() + "\n" +
                "## At loss: " + ANSI_RED + salesAtLoss + ANSI_RESET + "\n" +
                "## At gain: " + ANSI_GREEN + salesAtGain + ANSI_RESET + "\n" +
                "## At pair: " + salesAtPair + "\n" +
                getIncomeColorLine("## Total income: ", computeTotalIncome(2)) +
                "######################";
    }

    /**
     * This method is used to format colored string for income detail line.<br>
     * @param tail: text to indicate the info es. Income:
     * @param income: value of income es. +8%
     * @return percent string colored and formatted as {@link String}
     * **/
    private String getIncomeColorLine(String tail, double income){
        String textIncome = textualizeAssetPercent(income);
        if(textIncome.contains("+"))
            return tail + ANSI_GREEN + textIncome + ANSI_RESET + "\n";
        else if(textIncome.contains("-"))
            return tail + ANSI_RED + textIncome + ANSI_RESET + "\n";
        else
            return tail + textIncome + "\n";
    }

}
