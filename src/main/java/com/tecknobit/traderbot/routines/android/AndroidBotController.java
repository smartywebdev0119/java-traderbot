package com.tecknobit.traderbot.routines.android;

/**
 * The {@code RoutineMessages} interface defines base methods to manage Android's bot
 *
 * @author Tecknobit N7ghtm4r3
 **/
public interface AndroidBotController {

    /**
     * This method is used to run the bot
     **/
    void runBot();

    /**
     * This method is used to stop the bot
     **/
    void stopBot();

    /**
     * This method is used to set time change refresh data
     *
     * @param refreshTime: is time in seconds to set to refresh data
     **/
    void changeRefreshTime(int refreshTime);

    /**
     * This method is used to set base currency for change amount value
     *
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     **/
    void changeBaseCurrency(String baseCurrency);

}
