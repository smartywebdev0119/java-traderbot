package com.tecknobit.traderbot.Records;

import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines.TradingConfig;

public class Cryptocurrency {

    private final String symbol;
    private final double lastPrice;
    private final double tptopIndex;
    private final double quantity;
    private final Object candleGap;
    private final TradingConfig tradingConfig;

    public Cryptocurrency(String symbol, double lastPrice, double tptopIndex, double quantity, Object candleGap,
                          TradingConfig tradingConfig) {
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.tptopIndex = tptopIndex;
        this.quantity = quantity;
        this.candleGap = candleGap;
        this.tradingConfig = tradingConfig;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public double getTptopIndex() {
        return tptopIndex;
    }

    public double getQuantity() {
        return quantity;
    }

    public Object getCandleGap() {
        return candleGap;
    }

    public TradingConfig getTradingConfig() {
        return tradingConfig;
    }

}
