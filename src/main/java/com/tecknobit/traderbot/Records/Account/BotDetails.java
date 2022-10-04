package com.tecknobit.traderbot.Records.Account;

import com.tecknobit.traderbot.Routines.Interfaces.TraderBotConstants;

import java.util.Date;
import java.util.HashMap;

import static com.tecknobit.traderbot.Routines.Interfaces.TraderBotConstants.*;

/**
 * The {@code BotDetails} class is useful to contains details of bot used. <br>
 * Is useful for Android's type bots.
 *
 * @author Tecknobit N7ghtm4r3
 **/

public class BotDetails extends TecknobitBot {

    /**
     * {@code botType} is instance that memorizes type of bot in use {@link TraderBotConstants#BOT_TYPE_AUTONOMOUS} or {@link TraderBotConstants#BOT_TYPE_MANUAL}
     **/
    private final String botType;

    /**
     * {@code botPlatform} is instance that memorizes platform of bot in use {@link TraderBotConstants#BINANCE_PLATFORM} or {@link TraderBotConstants#COINBASE_PLATFORM}
     **/
    private final String botPlatform;

    /**
     * {@code lastBotActivity} is instance that memorizes last time when bot contact server
     **/
    private String lastBotActivity;

    /**
     * {@code lastBotActivityTimestamp} is instance that memorizes last time when bot contact server in timestamp format
     **/
    private long lastBotActivityTimestamp;

    /**
     * {@code botStatus} is instance that memorizes current status of bot {@link TraderBotConstants#RUNNING_BOT_STATUS} or {@link TraderBotConstants#STOPPED_BOT_STATUS}
     **/
    private String botStatus;

    /**
     * {@code refreshTime} is instance that memorizes time to refresh last prices
     *
     * @implNote this param can customize with {@link #setRefreshTime(int)}
     * @implSpec valid values are from 5 second to 3600 seconds other will generate an {@link Exception}
     **/
    private int refreshTime;

    /**
     * {@code botPlatform} is instance that memorizes timestamp of date when bot has been started
     * **/
    private long runningFromDate;

    /**
     * Constructor to init {@link BotDetails}
     *
     * @param botType:         type of bot in use {@link TraderBotConstants#BOT_TYPE_AUTONOMOUS} or {@link TraderBotConstants#BOT_TYPE_MANUAL}
     * @param botPlatform:     platform of bot in use {@link TraderBotConstants#BINANCE_PLATFORM} or {@link TraderBotConstants#COINBASE_PLATFORM}
     * @param runningFromDate: time stamp of date when bot has been started
     * @throws IllegalArgumentException if parameters range is not respected
     **/
    public BotDetails(String botType, String botPlatform, long runningFromDate) {
        initTimeFormatters();
        if (!botType.equals(BOT_TYPE_AUTONOMOUS) && !botType.equals(BOT_TYPE_MANUAL))
            throw new IllegalArgumentException("bot type inserted is wrong value, can be AUTONOMOUS or MANUAL type");
        else
            this.botType = botType;
        if (!botPlatform.equals(BINANCE_PLATFORM) && !botPlatform.equals(COINBASE_PLATFORM))
            throw new IllegalArgumentException("bot platform inserted is not supported yet or is a wrong value");
        else
            this.botPlatform = botPlatform;
        if (runningFromDate < 0)
            throw new IllegalArgumentException("Running from date timestamp cannot be less than 0");
        else
            this.runningFromDate = runningFromDate;
    }

    /** Constructor to init {@link BotDetails}
     * @param lastBotActivity: last time when bot contact server
     * @param botType: type of bot in use {@link TraderBotConstants#BOT_TYPE_AUTONOMOUS} or {@link TraderBotConstants#BOT_TYPE_MANUAL}
     * @param botStatus: current status of bot {@link TraderBotConstants#RUNNING_BOT_STATUS} or {@link TraderBotConstants#STOPPED_BOT_STATUS}
     * @param botPlatform: platform of bot in use {@link TraderBotConstants#BINANCE_PLATFORM} or {@link TraderBotConstants#COINBASE_PLATFORM}
     * @param refreshTime: time to refresh last prices
     * @param runningFromDate: date when bot has been started
     * @throws IllegalArgumentException if parameters range is not respected
     * **/
    public BotDetails(long lastBotActivity, String botType, String botStatus, String botPlatform,
                      int refreshTime, long runningFromDate) {
        initTimeFormatters();
        boolean isInMillis = refreshTime > 3600;
        if (lastBotActivity < 0)
            throw new IllegalArgumentException("Last bot activity timestamp cannot be less than 0");
        else {
            this.lastBotActivity = timeFormat.format(new Date(lastBotActivity));
            lastBotActivityTimestamp = lastBotActivity;
        }
        if (!botType.equals(BOT_TYPE_AUTONOMOUS) && !botType.equals(BOT_TYPE_MANUAL))
            throw new IllegalArgumentException("bot type inserted is wrong value, can be AUTONOMOUS or MANUAL type");
        else
            this.botType = botType;
        if (!botStatus.equals(RUNNING_BOT_STATUS) && !botStatus.equals(STOPPED_BOT_STATUS))
            throw new IllegalArgumentException("bot status inserted is wrong value, can be RUNNING or STOPPED status");
        else
            this.botStatus = botStatus;
        if (!botPlatform.equals(BINANCE_PLATFORM) && !botPlatform.equals(COINBASE_PLATFORM))
            throw new IllegalArgumentException("bot platform inserted is not supported yet or is a wrong value");
        else
            this.botPlatform = botPlatform;
        if (isInMillis)
            refreshTime /= 1000;
        if (refreshTime < 5 || refreshTime > 3600)
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
        else {
            if (isInMillis)
                refreshTime *= 1000;
            this.refreshTime = refreshTime;
        }
        if (runningFromDate < 0)
            throw new IllegalArgumentException("Running from date timestamp cannot be less than 0");
        else
            this.runningFromDate = runningFromDate;
    }

    public String getLastBotActivity() {
        if ((System.currentTimeMillis() - lastBotActivityTimestamp) >= ((86400 * 1000) / 2))
            lastBotActivity = dayPassFormat.format(new Date(lastBotActivityTimestamp));
        return lastBotActivity;
    }

    /**
     * Method to set {@link #lastBotActivity}
     *
     * @param lastBotActivity: last time when bot contact server value
     * @throws IllegalArgumentException when time value is less than 0
     **/
    public void setLastBotActivity(long lastBotActivity) {
        if (lastBotActivity < 0)
            throw new IllegalArgumentException("Last bot activity timestamp cannot be less than 0");
        this.lastBotActivity = timeFormat.format(new Date(lastBotActivity));
        lastBotActivityTimestamp = lastBotActivity;
    }

    public String getBotType() {
        return botType;
    }

    public String getBotStatus() {
        return botStatus;
    }

    /**
     * Method to set {@link #botStatus}
     *
     * @param botStatus: current status of bot
     * @throws IllegalArgumentException when time value is different from {@link TraderBotConstants#RUNNING_BOT_STATUS} or
     * {@link TraderBotConstants#STOPPED_BOT_STATUS}
     **/
    public void setBotStatus(String botStatus) {
        if (!botStatus.equals(RUNNING_BOT_STATUS) && !botStatus.equals(STOPPED_BOT_STATUS))
            throw new IllegalArgumentException("bot status inserted is wrong value, can be RUNNING or STOPPED status");
        this.botStatus = botStatus;
    }

    public String getBotPlatform() {
        return botPlatform;
    }

    /**
     * This method is used to get color of platform that bot is using<br>
     * Any params required
     *
     * @return color platform as {@link String}
     **/
    public String getBotPlatformColor() {
        switch (botPlatform) {
            case BINANCE_PLATFORM:
                return BINANCE_COLOR;
            case COINBASE_PLATFORM:
                return COINBASE_COLOR;
            default:
                return TraderBotConstants.CUSTOM_DEF_COLOR;
        }
    }

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
     * Method to set {@link #refreshTime}
     *
     * @param refreshTime: time to refresh last prices
     * @throws IllegalArgumentException when time value is less from 5 second or more 3600 seconds
     **/
    public void setRefreshTime(int refreshTime) {
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime * 1000;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    public long getRunningFromDate() {
        return runningFromDate;
    }

    /** Method to set {@link #runningFromDate}
     * @param runningFromDate: timestamp of date when bot has been started
     * @throws IllegalArgumentException when timestamp value is lesser than 0
     * **/
    public void setRunningFromDate(long runningFromDate){
        if(runningFromDate < 0)
            throw new IllegalArgumentException("Running from date timestamp cannot be less than 0");
        else
            this.runningFromDate = runningFromDate;
    }

    /**
     * This method is used to get date of {@link #runningFromDate} timestamp
     * Any params required
     * @return date from last start as {@link String}
     * **/
    public String getRunningDate(){
        return dayPassFormat.format(runningFromDate);
    }

    /**
     * This method is used to get bot details <br>
     * Any params required
     *
     * @return bot details as {@link HashMap} of {@link Object}
     **/
    public HashMap<String, Object> getBot() {
        HashMap<String, Object> bot = new HashMap<>();
        bot.put(RUNNING_FROM_DATE_KEY, runningFromDate);
        bot.put(BOT_TYPE_KEY, botType);
        bot.put(LAST_BOT_ACTIVITY_KEY, lastBotActivityTimestamp);
        bot.put(REFRESH_TIME_KEY, refreshTime);
        bot.put(BOT_PLATFORM_KEY, botPlatform);
        bot.put(BOT_STATUS_KEY, botStatus);
        return bot;
    }

    @Override
    public String toString() {
        return "BotDetails{" +
                "lastBotActivity='" + lastBotActivity + '\'' +
                ", lastBotActivityTimestamp=" + lastBotActivityTimestamp +
                ", botType='" + botType + '\'' +
                ", botStatus='" + botStatus + '\'' +
                ", botPlatform='" + botPlatform + '\'' +
                ", refreshTime=" + refreshTime +
                ", runningFromDate=" + runningFromDate +
                '}';
    }

}
