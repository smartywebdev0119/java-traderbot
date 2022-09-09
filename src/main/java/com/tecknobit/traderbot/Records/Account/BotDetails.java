package com.tecknobit.traderbot.Records.Account;

import java.util.Date;
import java.util.HashMap;

/**
 * The {@code BotDetails} class is useful to contains details of trader used. <br>
 * Is useful for Android's type traders.
 *
 * @author Tecknobit N7ghtm4r3
 **/

public class BotDetails extends Trader {

    /**
     * {@code CUSTOM_DEF_COLOR} is instance that memorizes default color to use for example when asset is at pair
     * @implSpec not available for console color
     * **/
    public static final String CUSTOM_DEF_COLOR = "#A8A8A8";

    /**
     * {@code CUSTOM_RED_COLOR} is instance that memorizes red color to use for example when asset is at loss
     * @implSpec not available for console color
     * **/
    public static final String CUSTOM_RED_COLOR = "#C3100C";

    /**
     * {@code CUSTOM_GREEN_COLOR} is instance that memorizes green color to use for example when asset is at gain
     * @implSpec not available for console color
     * **/
    public static final String CUSTOM_GREEN_COLOR = "#24B82B";

    /**
     * {@code BINANCE_COLOR} is instance that memorizes Binance official color
     * @implSpec not available for console color
     * **/
    public static final String BINANCE_COLOR = "#F3BA2F";

    /**
     * {@code COINBASE_COLOR} is instance that memorizes Coinbase official color
     * @implSpec not available for console color
     * **/
    public static final String COINBASE_COLOR = "#1652f0";

    /**
     * {@code TRADER_TYPE_AUTONOMOUS} is instance that memorizes AUTONOMOUS trader type in use
     * **/
    public static final String TRADER_TYPE_AUTONOMOUS = "AUTONOMOUS";

    /**
     * {@code TRADER_TYPE_MANUAL} is instance that memorizes MANUAL trader type in use
     * **/
    public static final String TRADER_TYPE_MANUAL = "MANUAL";

    /**
     * {@code BINANCE_TRADER_PLATFORM} is instance that memorizes BINANCE trader platform in use
     * **/
    public static final String BINANCE_TRADER_PLATFORM = "BINANCE";

    /**
     * {@code COINBASE_TRADER_PLATFORM} is instance that memorizes COINBASE trader platform in use
     * **/
    public static final String COINBASE_TRADER_PLATFORM = "COINBASE";

    /**
     * {@code RUNNING_TRADER_STATUS} is instance that memorizes RUNNING trader current status
     * **/
    public static final String RUNNING_TRADER_STATUS = "RUNNING";

    /**
     * {@code STOPPED_TRADER_STATUS} is instance that memorizes STOPPED trader current status
     * **/
    public static final String STOPPED_TRADER_STATUS = "STOPPED";

    /**
     * {@code TRADER_STATUS_KEY} is instance that memorizes trader status key
     * **/
    public static final String TRADER_STATUS_KEY = "trader_status";

    /**
     * {@code TRADER_PLATFORM_KEY} is instance that memorizes trader platform key
     * **/
    public static final String TRADER_PLATFORM_KEY = "trader_platform";

    /**
     * {@code TRADER_TYPE_KEY} is instance that memorizes trader type key
     * **/
    public static final String TRADER_TYPE_KEY = "trader_type";

    /**
     * {@code REFRESH_TIME_KEY} is instance that memorizes refresh time key
     **/
    public static final String REFRESH_TIME_KEY = "refresh_time";

    /**
     * {@code LAST_TRADER_ACTIVITY_KEY} is instance that memorizes last trader activity key
     * **/
    public static final String LAST_TRADER_ACTIVITY_KEY = "last_trader_activity";

    /**
     * {@code RUNNING_FROM_DATE_KEY} is instance that memorizes running from date key
     * **/
    public static final String RUNNING_FROM_DATE_KEY = "running_from_date";

    /**
     * {@code lastTraderActivity} is instance that memorizes last time when bot contact server
     * **/
    private String lastTraderActivity;

    /**
     * {@code lastTraderActivityTimestamp} is instance that memorizes last time when bot contact server in timestamp format
     * **/
    private long lastTraderActivityTimestamp;

    /**
     * {@code traderType} is instance that memorizes type of trader in use {@link #TRADER_TYPE_AUTONOMOUS} or {@link #TRADER_TYPE_MANUAL}
     * **/
    private final String traderType;

    /**
     * {@code traderStatus} is instance that memorizes current status of trader {@link #RUNNING_TRADER_STATUS} or {@link #STOPPED_TRADER_STATUS}
     * **/
    private String traderStatus;

    /**
     * {@code traderPlatform} is instance that memorizes platform of trader in use {@link #BINANCE_TRADER_PLATFORM} or {@link #COINBASE_TRADER_PLATFORM}
     * **/
    private final String traderPlatform;

    /**
     * {@code refreshTime} is instance that memorizes time to refresh last prices
     * @implNote this param can customize with {@link #setRefreshTime(int)}
     * @implSpec valid values are from 5 second to 3600 seconds other will generate an {@link Exception}
     * **/
    private long refreshTime;

    /**
     * {@code traderPlatform} is instance that memorizes timestamp of date when trader has been started
     * **/
    private long runningFromDate;

    /** Constructor to init {@link BotDetails}
     * @param traderType: type of trader in use {@link #TRADER_TYPE_AUTONOMOUS} or {@link #TRADER_TYPE_MANUAL}
     * @param traderPlatform: platform of trader in use {@link #BINANCE_TRADER_PLATFORM} or {@link #COINBASE_TRADER_PLATFORM}
     * @param runningFromDate: time stamp of date when trader has been started
     * @throws IllegalArgumentException if parameters range is not respected
     * **/
    public BotDetails(String traderType, String traderPlatform, long runningFromDate) {
        initTimeFormatters();
        if (!traderType.equals(TRADER_TYPE_AUTONOMOUS) && !traderType.equals(TRADER_TYPE_MANUAL))
            throw new IllegalArgumentException("Trader type inserted is wrong value, can be AUTONOMOUS or MANUAL type");
        else
            this.traderType = traderType;
        if (!traderPlatform.equals(BINANCE_TRADER_PLATFORM) && !traderPlatform.equals(COINBASE_TRADER_PLATFORM))
            throw new IllegalArgumentException("Trader platform inserted is not supported yet or is a wrong value");
        else
            this.traderPlatform = traderPlatform;
        if (runningFromDate < 0)
            throw new IllegalArgumentException("Running from date timestamp cannot be less than 0");
        else
            this.runningFromDate = runningFromDate;
    }

    /** Constructor to init {@link BotDetails}
     * @param lastTraderActivity: last time when bot contact server
     * @param traderType: type of trader in use {@link #TRADER_TYPE_AUTONOMOUS} or {@link #TRADER_TYPE_MANUAL}
     * @param traderStatus: current status of trader {@link #RUNNING_TRADER_STATUS} or {@link #STOPPED_TRADER_STATUS}
     * @param traderPlatform: platform of trader in use {@link #BINANCE_TRADER_PLATFORM} or {@link #COINBASE_TRADER_PLATFORM}
     * @param refreshTime: time to refresh last prices
     * @param runningFromDate: date when trader has been started
     * @throws IllegalArgumentException if parameters range is not respected
     * **/
    public BotDetails(long lastTraderActivity, String traderType, String traderStatus, String traderPlatform,
                      int refreshTime, long runningFromDate) {
        initTimeFormatters();
        boolean isInMillis = refreshTime > 3600;
        if (lastTraderActivity < 0)
            throw new IllegalArgumentException("Last trader activity timestamp cannot be less than 0");
        else {
            this.lastTraderActivity = timeFormat.format(new Date(lastTraderActivity));
            lastTraderActivityTimestamp = lastTraderActivity;
        }
        if (!traderType.equals(TRADER_TYPE_AUTONOMOUS) && !traderType.equals(TRADER_TYPE_MANUAL))
            throw new IllegalArgumentException("Trader type inserted is wrong value, can be AUTONOMOUS or MANUAL type");
        else
            this.traderType = traderType;
        if (!traderStatus.equals(RUNNING_TRADER_STATUS) && !traderStatus.equals(STOPPED_TRADER_STATUS))
            throw new IllegalArgumentException("Trader status inserted is wrong value, can be RUNNING or STOPPED status");
        else
            this.traderStatus = traderStatus;
        if (!traderPlatform.equals(BINANCE_TRADER_PLATFORM) && !traderPlatform.equals(COINBASE_TRADER_PLATFORM))
            throw new IllegalArgumentException("Trader platform inserted is not supported yet or is a wrong value");
        else
            this.traderPlatform = traderPlatform;
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

    public String getLastTraderActivity() {
        if((System.currentTimeMillis() - lastTraderActivityTimestamp) >= ((86400 * 1000) / 2))
            lastTraderActivity = dayPassFormat.format(new Date(lastTraderActivityTimestamp));
        return lastTraderActivity;
    }

    /** Method to set {@link #lastTraderActivity}
     * @param lastTraderActivity: last time when bot contact server value
     * @throws IllegalArgumentException when time value is less than 0
     * **/
    public void setLastTraderActivity(long lastTraderActivity) {
        if(lastTraderActivity < 0)
            throw new IllegalArgumentException("Last trader activity timestamp cannot be less than 0");
        this.lastTraderActivity = timeFormat.format(new Date(lastTraderActivity));
        lastTraderActivityTimestamp = lastTraderActivity;
    }

    public String getTraderType() {
        return traderType;
    }

    public String getTraderStatus() {
        return traderStatus;
    }

    /** Method to set {@link #traderStatus}
     * @param traderStatus: current status of trader
     * @throws IllegalArgumentException when time value is different from {@link #RUNNING_TRADER_STATUS} or {@link #STOPPED_TRADER_STATUS}
     * **/
    public void setTraderStatus(String traderStatus) {
        if(!traderStatus.equals(RUNNING_TRADER_STATUS) && !traderStatus.equals(STOPPED_TRADER_STATUS))
            throw new IllegalArgumentException("Trader status inserted is wrong value, can be RUNNING or STOPPED status");
        this.traderStatus = traderStatus;
    }

    public String getTraderPlatform() {
        return traderPlatform;
    }

    /**
     * This method is used to get color of platform that trader is using<br>
     * Any params required
     * @return color platform as {@link String}
     * **/
    public String getTraderPlatformColor() {
        switch (traderPlatform) {
            case BINANCE_TRADER_PLATFORM:
                return BINANCE_COLOR;
            case COINBASE_TRADER_PLATFORM:
                return COINBASE_COLOR;
            default:
                return CUSTOM_DEF_COLOR;
        }
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    /**
     * Method to set {@link #refreshTime}
     *
     * @param refreshTime: time to refresh last prices
     * @throws IllegalArgumentException when time value is less from 5 second or more 3600 seconds
     **/
    public void setRefreshTime(int refreshTime) {
        if (refreshTime >= 5 && refreshTime <= 3600)
            this.refreshTime = refreshTime;
        else
            throw new IllegalArgumentException("Refresh time must be more than 5 (5s) and less than 3600 (1h)");
    }

    public long getRunningFromDate() {
        return runningFromDate;
    }

    /** Method to set {@link #runningFromDate}
     * @param runningFromDate: timestamp of date when trader has been started
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
     * This method is used to get trader details <br>
     * Any params required
     * @return trader details as {@link HashMap} of {@link Object}
     * **/
    public HashMap<String, Object> getTrader() {
        HashMap<String, Object> trader = new HashMap<>();
        trader.put(RUNNING_FROM_DATE_KEY, runningFromDate);
        trader.put(TRADER_TYPE_KEY, traderType);
        trader.put(LAST_TRADER_ACTIVITY_KEY, lastTraderActivityTimestamp);
        trader.put(REFRESH_TIME_KEY, refreshTime);
        trader.put(TRADER_PLATFORM_KEY, traderPlatform);
        trader.put(TRADER_STATUS_KEY, traderStatus);
        return trader;
    }

    @Override
    public String toString() {
        return "BotDetails{" +
                "lastTraderActivity='" + lastTraderActivity + '\'' +
                ", lastTraderActivityTimestamp=" + lastTraderActivityTimestamp +
                ", traderType='" + traderType + '\'' +
                ", traderStatus='" + traderStatus + '\'' +
                ", traderPlatform='" + traderPlatform + '\'' +
                ", refreshTime=" + refreshTime +
                ", runningFromDate=" + runningFromDate +
                '}';
    }

}
