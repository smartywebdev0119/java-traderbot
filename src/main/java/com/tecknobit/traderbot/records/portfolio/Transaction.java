package com.tecknobit.traderbot.records.portfolio;

import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.traderbot.routines.interfaces.RecordDetails;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;

import static com.tecknobit.apimanager.trading.TradingTools.roundValue;
import static com.tecknobit.apimanager.trading.TradingTools.textualizeAssetPercent;
import static com.tecknobit.traderbot.routines.autonomous.AutoTraderCoreRoutines.*;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.*;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.Side.BUY;
import static java.lang.System.out;

/**
 * The {@code Transaction} class defines Transaction object. <br>
 * This object is useful for fetch and format transactions list details.
 *
 * @author Tecknobit N7ghtm4r3
 **/
public class Transaction implements RecordDetails {

    /**
     * {@code symbol} is instance that memorizes symbol of transaction es. BTCBUSD or BTC-USD
     **/
    protected final String symbol;

    /**
     * {@code side} is instance that memorizes side of transaction BUY or SELL
     **/
    protected final Side side;

    /**
     * {@code transactionDate} is instance that memorizes date of that transaction es 21:08:22 30/06/2022
     **/
    protected final String transactionDate;

    /**
     * {@code transactionDateTimestamp} is instance that memorizes date of that transaction es 1045868400000
     **/
    protected final long transactionDateTimestamp;

    /**
     * {@code value} is instance that memorizes value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     **/
    protected final double value;

    /**
     * {@code quantity} is instance that memorizes value of quantity transfered in that transaction es. 1 BTC
     **/
    protected final double quantity;

    /**
     * {@code quoteAsset} is instance that memorizes quote asset used in that transaction es. EUR
     * **/
    protected final String quoteAsset;

    /**
     * {@code baseAsset} is instance that memorizes base asset used in that transaction es. BTC
     * **/
    protected final String baseAsset;

    /**
     * {@code incomePercent} is instance that memorizes income of that transaction es. +1.5%
     * **/
    protected double incomePercent;

    /**
     * {@code transactionType} is instance that memorizes type of that transaction es. sold at gain, at loss or at pair
     * **/
    protected String transactionType;

    /**
     * Constructor to init {@link Transaction}
     * @param symbol: symbol of transaction es. BTCBUSD or BTC-USD
     * @param side: side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value: value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity: value of quantity transfered in that transaction es. 1 BTC
     * @param quoteAsset: quote asset used in that transaction es. EUR
     * @param baseAsset: base asset used in that transaction es. BTC
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, Side side, String transactionDate, double value, double quantity,
                       String quoteAsset, String baseAsset) {
        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        this.side = side;
        if (transactionDate == null || transactionDate.isEmpty())
            throw new IllegalArgumentException("Transaction date must contains characters");
        else {
            this.transactionDate = transactionDate;
            transactionDateTimestamp = getDateTimestamp(transactionDate);
        }
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
        if(quoteAsset == null || quoteAsset.isEmpty())
            throw new IllegalArgumentException("Quote asset must contains characters");
        else
            this.quoteAsset = quoteAsset;
        if(baseAsset == null || baseAsset.isEmpty())
            throw new IllegalArgumentException("Base asset must contains characters");
        else
            this.baseAsset = baseAsset;
        incomePercent = ASSET_NOT_TRADABLE;
        transactionType = null;
    }

    /**
     * Constructor to init {@link Transaction}
     * @param symbol: symbol of transaction es. BTCBUSD or BTC-USD
     * @param side: side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value: value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity: value of quantity transfered in that transaction es. 1 BTC
     * @param quoteAsset: quote asset used in that transaction es. EUR
     * @param baseAsset: base asset used in that transaction es. BTC
     * @param incomePercent: income percent that transaction has done
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, Side side, String transactionDate, double value, double quantity,
                       String quoteAsset, String baseAsset, double incomePercent) {
        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        this.side = side;
        if (transactionDate == null || transactionDate.isEmpty())
            throw new IllegalArgumentException("Transaction date must contains characters");
        else {
            this.transactionDate = transactionDate;
            transactionDateTimestamp = getDateTimestamp(transactionDate);
        }
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
        if(quoteAsset == null || quoteAsset.isEmpty())
            throw new IllegalArgumentException("Quote asset must contains characters");
        else
            this.quoteAsset = quoteAsset;
        if(baseAsset == null || baseAsset.isEmpty())
            throw new IllegalArgumentException("Base asset must contains characters");
        else
            this.baseAsset = baseAsset;
        if(incomePercent < -100)
            throw new IllegalArgumentException("Income percent must be bigger than -100");
        else
            this.incomePercent = incomePercent;
        transactionType = null;
    }

    /**
     * Constructor to init {@link Transaction}
     *
     * @param symbol:          symbol of transaction es. BTCBUSD or BTC-USD
     * @param side:            side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value:           value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity:        value of quantity transfered in that transaction es. 1 BTC
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, Side side, String transactionDate, double value, double quantity) {
        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        this.side = side;
        if (transactionDate == null || transactionDate.isEmpty())
            throw new IllegalArgumentException("Transaction date must contains characters");
        else {
            this.transactionDate = transactionDate;
            transactionDateTimestamp = getDateTimestamp(transactionDate);
        }
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
        incomePercent = ASSET_NOT_TRADABLE;
        quoteAsset = null;
        baseAsset = null;
        transactionType = null;
    }

    /**
     * Constructor to init {@link Transaction}
     *
     * @param symbol:          symbol of transaction es. BTCBUSD or BTC-USD
     * @param side:            side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value:           value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity:        value of quantity transfered in that transaction es. 1 BTC
     * @param incomePercent:   income percent that transaction has done
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, Side side, String transactionDate, double value, double quantity,
                       double incomePercent) {
        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        this.side = side;
        if (transactionDate == null || transactionDate.isEmpty())
            throw new IllegalArgumentException("Transaction date must contains characters");
        else {
            this.transactionDate = transactionDate;
            transactionDateTimestamp = getDateTimestamp(transactionDate);
        }
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
        if(incomePercent < -100)
            throw new IllegalArgumentException("Income percent must be bigger than -100");
        else
            this.incomePercent = incomePercent;
        quoteAsset = null;
        baseAsset = null;
        transactionType = null;
    }

    /**
     * Constructor to init {@link Transaction}
     *
     * @param symbol:          symbol of transaction es. BTCBUSD or BTC-USD
     * @param side:            side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value:           value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity:        value of quantity transfered in that transaction es. 1 BTC
     * @param quoteAsset:      quote asset used in that transaction es. EUR
     * @param baseAsset:       base asset used in that transaction es. BTC
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, Side side, long transactionDate, double value, double quantity,
                       String quoteAsset, String baseAsset) {
        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        this.side = side;
        transactionDateTimestamp = transactionDate;
        this.transactionDate = getDate(transactionDate);
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
        if (quoteAsset == null || quoteAsset.isEmpty())
            throw new IllegalArgumentException("Quote asset must contains characters");
        else
            this.quoteAsset = quoteAsset;
        if (baseAsset == null || baseAsset.isEmpty())
            throw new IllegalArgumentException("Base asset must contains characters");
        else
            this.baseAsset = baseAsset;
        incomePercent = ASSET_NOT_TRADABLE;
        transactionType = null;
    }

    /**
     * Constructor to init {@link Transaction}
     *
     * @param symbol:          symbol of transaction es. BTCBUSD or BTC-USD
     * @param side:            side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value:           value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity:        value of quantity transfered in that transaction es. 1 BTC
     * @param quoteAsset:      quote asset used in that transaction es. EUR
     * @param baseAsset:       base asset used in that transaction es. BTC
     * @param incomePercent:   income percent that transaction has done
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, Side side, long transactionDate, double value, double quantity,
                       String quoteAsset, String baseAsset, double incomePercent) {
        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        this.side = side;
        transactionDateTimestamp = transactionDate;
        this.transactionDate = getDate(transactionDate);
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
        if (quoteAsset == null || quoteAsset.isEmpty())
            throw new IllegalArgumentException("Quote asset must contains characters");
        else
            this.quoteAsset = quoteAsset;
        if (baseAsset == null || baseAsset.isEmpty())
            throw new IllegalArgumentException("Base asset must contains characters");
        else
            this.baseAsset = baseAsset;
        if(incomePercent < -100)
            throw new IllegalArgumentException("Income percent must be bigger than -100");
        else
            this.incomePercent = incomePercent;
        transactionType = null;
    }

    /**
     * Constructor to init {@link Transaction}
     *
     * @param symbol:          symbol of transaction es. BTCBUSD or BTC-USD
     * @param side:            side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value:           value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity:        value of quantity transfered in that transaction es. 1 BTC
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, Side side, long transactionDate, double value, double quantity) {
        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        this.side = side;
        transactionDateTimestamp = transactionDate;
        this.transactionDate = getDate(transactionDate);
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
        incomePercent = ASSET_NOT_TRADABLE;
        quoteAsset = null;
        baseAsset = null;
        transactionType = null;
    }

    /**
     * Constructor to init {@link Transaction}
     *
     * @param symbol:          symbol of transaction es. BTCBUSD or BTC-USD
     * @param side:            side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value:           value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity:        value of quantity transfered in that transaction es. 1 BTC
     * @param incomePercent:   income percent that transaction has done
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, Side side, long transactionDate, double value, double quantity,
                       double incomePercent) {
        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        this.side = side;
        transactionDateTimestamp = transactionDate;
        this.transactionDate = getDate(transactionDate);
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
        if (incomePercent < -100)
            throw new IllegalArgumentException("Income percent must be bigger than -100");
        else
            this.incomePercent = incomePercent;
        quoteAsset = null;
        baseAsset = null;
        transactionType = null;
    }

    /**
     * Method to get {@link #symbol} instance <br>
     * Any params required
     *
     * @return {@link #symbol} instance as {@link String}
     **/
    public String getSymbol() {
        return symbol;
    }

    /**
     * This method is used to get from a timestamp a date formatted by Locale
     *
     * @param timestamp: timestamp of the date to get
     * @return date value as {@link String}
     **/
    public static String getDate(long timestamp) {
        return TimeFormatter.getStringDate(timestamp);
    }

    /**
     * Method to get {@link #transactionDate} instance <br>
     * Any params required
     *
     * @return {@link #transactionDate} instance as {@link String}
     **/
    public String getTransactionDate() {
        return transactionDate;
    }

    /**
     * Method to get {@link #value} instance <br>
     * Any params required
     *
     * @return {@link #value} instance as double
     **/
    public double getValue() {
        return value;
    }

    /**
     * This method is used get {@link #value} instance
     *
     * @param decimals: number of decimal digits es. 2
     * @return {@link #value} formatted as 22.21
     **/
    public double getValue(int decimals) {
        return roundValue(value, decimals);
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
     * This method is used get {@link #quantity} instance
     *
     * @param decimals: number of decimal digits es. 2
     * @return {@link #quantity} formatted as 21.22
     **/
    public double getQuantity(int decimals) {
        return roundValue(quantity, decimals);
    }

    /**
     * Method to get {@link #quoteAsset} instance <br>
     * Any params required
     *
     * @return {@link #quoteAsset} instance as {@link String}
     **/
    public String getQuoteAsset() {
        return quoteAsset;
    }

    /**
     * Method to get {@link #baseAsset} instance <br>
     * Any params required
     *
     * @return {@link #baseAsset} instance as {@link String}
     **/
    public String getBaseAsset() {
        return baseAsset;
    }

    /**
     * Method to get {@link #incomePercent} instance <br>
     * Any params required
     *
     * @return {@link #incomePercent} instance as double
     **/
    public double getIncomePercent() {
        return incomePercent;
    }

    /**
     * This method is used get {@link #incomePercent} instance
     *
     * @param decimals: number of decimal digits es. 2
     * @return {@link #incomePercent} formatted as 21.22
     **/
    public double getIncomePercent(int decimals) {
        return roundValue(incomePercent, decimals);
    }

    /**
     * This method is used get {@link #transactionDate} instance formatted as long<br>
     * Any params required
     *
     * @return {@link #transactionDate} timestamp as long
     * @implNote when {@link ParseException} has been thrown return value will be -1 as default
     **/
    public long getTransactionTimestamp() {
        return transactionDateTimestamp;
    }

    /**
     * Method to set {@link #incomePercent}
     *
     * @param incomePercent: income percent of cryptocurrency
     * @throws IllegalArgumentException when income percent value is less than 0
     **/
    public void setIncomePercent(double incomePercent) {
        if (incomePercent < -100)
            throw new IllegalArgumentException("Income percent cannot be less than -100");
        this.incomePercent = incomePercent;
    }

    /** Method to set {@link #transactionType}
     * @param transactionType: transaction es. sold at gain, at loss or at pair
     * @throws IllegalArgumentException when time value is different from sales_at_loss,sales_at_gain and sales_at_pair
     * **/
    public void setTransactionType(String transactionType) {
        if(!transactionType.equals(GAIN_SELL) && !transactionType.equals(LOSS_SELL)
                && !transactionType.equals(PAIR_SELL))
            throw new IllegalArgumentException("Transaction type can be only be GAIN_SELL, LOSS_SELL or PAIR_SELL");
        this.transactionType = transactionType;
    }

    /**
     * This method is used get {@link #incomePercent} instance <br>
     * Any params required
     *
     * @return {@link #incomePercent} as {@link String} es. +2.3812%
     **/
    public String getIncomePercentText() {
        return textualizeAssetPercent(incomePercent);
    }

    /**
     * This method is used get {@link #incomePercent} instance
     *
     * @param decimals: number of decimal digits es. 2
     * @return {@link #incomePercent} as {@link String} es. +2.38%
     * @throws IllegalArgumentException if decimalDigits is negative
     **/
    public String getIncomePercentText(int decimals) {
        return textualizeAssetPercent(incomePercent, decimals);
    }

    /**
     * This method is used to get transaction details <br>
     * Any params required
     *
     * @return transaction details as {@link HashMap} of {@link Object}
     **/
    public HashMap<String, Object> getTransaction() {
        HashMap<String, Object> transaction = new HashMap<>();
        transaction.put(SYMBOL_KEY, symbol);
        transaction.put(SIDE_KEY, side);
        transaction.put(TRANSACTION_DATE_KEY, transactionDate);
        transaction.put(QUANTITY_KEY, quantity);
        transaction.put(QUOTE_ASSET_KEY, quoteAsset);
        transaction.put(VALUE_KEY, value);
        transaction.put(BASE_ASSET_KEY, baseAsset);
        if (incomePercent != ASSET_NOT_TRADABLE)
            transaction.put(INCOME_PERCENT_KEY, incomePercent);
        if (transactionType != null)
            transaction.put(TRANSACTION_TYPE_KEY, transactionType);
        return transaction;
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
     * This method is used to print details of {@link Transaction} object <br>
     * Any params required
     **/
    @Override
    public void printDetails() {
        out.println(this);
    }

    @Override
    public String toString() {
        return "## [" + symbol + "]\n" +
                getSideInfoLine(side) +
                "## Date: " + transactionDate + "\n" +
                "## Amount value: " + value + "\n" +
                "## Quantity: " + quantity + "\n" +
                "## Base asset: " + baseAsset + "\n" +
                "## Quote asset: " + quoteAsset + "\n" +
                "######################";
    }

    /**
     * This method is used to get from a timestamp a date formatted by Locale
     *
     * @param timestamp: timestamp of the date to get
     * @param pattern:   pattern to format return date es. dd/MM/yyyy hh:mm:ss
     * @return date value as {@link String}
     **/
    public static String getDate(long timestamp, String pattern) {
        return TimeFormatter.getStringDate(timestamp, pattern);
    }

    /**
     * This method is used to get from a date its timestamp value
     *
     * @param date: date to get timestamp
     * @return date timestamp value as long
     * @implNote when {@link ParseException} has been thrown return value will be -1 as default
     **/
    public static long getDateTimestamp(String date) {
        return TimeFormatter.getDateTimestamp(date);
    }

    /**
     * This method is used to get from a date its timestamp value
     *
     * @param date:    date to get timestamp
     * @param pattern: pattern to format return date es. dd/MM/yyyy hh:mm:ss
     * @return date timestamp value as long
     * @implNote when {@link ParseException} has been thrown return value will be -1 as default
     **/
    public static long getDateTimestamp(String date, String pattern) {
        return TimeFormatter.getDateTimestamp(date, pattern);
    }

    /**
     * Method to get {@link #side} instance <br>
     * Any params required
     *
     * @return {@link #side} instance as {@link String}
     **/
    public Side getSide() {
        return side;
    }

    /**
     * This method is used to format colored string for side detail line.
     *
     * @param side: side of {@link Transaction}
     * @return side string colored and formatted as {@link String}
     **/
    protected String getSideInfoLine(Side side) {
        if (side.equals(BUY))
            return "## Side: " + ANSI_GREEN + side + ANSI_RESET + "\n";
        return "## Side: " + ANSI_RED + side + ANSI_RESET + "\n";
    }

}

