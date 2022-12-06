package com.tecknobit.traderbot.routines.interfaces;

import static com.tecknobit.traderbot.routines.interfaces.RoutineMessages.ANSI_RED;
import static com.tecknobit.traderbot.routines.interfaces.RoutineMessages.ANSI_RESET;

/**
 * The {@code TraderBotConstants} interface defines all constants used in TraderBot library that are useful also
 * for a custom easy use.
 *
 * @author Tecknobit N7ghtm4r3
 **/
public class TraderBotConstants {

    /**
     * {@code USD_CURRENCY} is the identifier of USD currency used by traders to get default prices
     **/
    public static final String USD_CURRENCY = "USD";

    /**
     * {@code USDT_CURRENCY} is the identifier of USDT cryptocurrency used by traders to get default prices
     **/
    public static final String USDT_CURRENCY = "USDT";

    /**
     * {@code CUSTOM_DEF_COLOR} is instance that memorizes default color to use for example when asset is at pair
     *
     * @implSpec not available for console color
     **/
    public static final String CUSTOM_DEF_COLOR = "#A8A8A8";

    /**
     * {@code CUSTOM_RED_COLOR} is instance that memorizes red color to use for example when asset is at loss
     *
     * @implSpec not available for console color
     **/
    public static final String CUSTOM_RED_COLOR = "#A80202";

    /**
     * {@code CUSTOM_GREEN_COLOR} is instance that memorizes green color to use for example when asset is at gain
     *
     * @implSpec not available for console color
     **/
    public static final String CUSTOM_GREEN_COLOR = "#079A44";

    /**
     * {@code BINANCE_COLOR} is instance that memorizes Binance official color
     *
     * @implSpec not available for console color
     **/
    public static final String BINANCE_COLOR = "#F3BA2F";

    /**
     * {@code COINBASE_COLOR} is instance that memorizes Coinbase official color
     *
     * @implSpec not available for console color
     **/
    public static final String COINBASE_COLOR = "#1652f0";

    /**
     * {@code BOT_TYPE_AUTONOMOUS} is instance that memorizes AUTONOMOUS bot type in use
     **/
    public static final String BOT_TYPE_AUTONOMOUS = "AUTONOMOUS";

    /**
     * {@code BOT_TYPE_MANUAL} is instance that memorizes MANUAL bot type in use
     **/
    public static final String BOT_TYPE_MANUAL = "MANUAL";

    /**
     * {@code BINANCE_PLATFORM} is instance that memorizes BINANCE bot platform in use
     **/
    public static final String BINANCE_PLATFORM = "BINANCE";

    /**
     * {@code COINBASE_PLATFORM} is instance that memorizes COINBASE bot platform in use
     **/
    public static final String COINBASE_PLATFORM = "COINBASE";

    /**
     * {@code RUNNING_BOT_STATUS} is instance that memorizes RUNNING bot current status
     **/
    public static final String RUNNING_BOT_STATUS = "RUNNING";

    /**
     * {@code STOPPED_BOT_STATUS} is instance that memorizes STOPPED bot current status
     **/
    public static final String STOPPED_BOT_STATUS = "STOPPED";

    /**
     * {@code BOT_STATUS_KEY} is instance that memorizes bot status key
     **/
    public static final String BOT_STATUS_KEY = "bot_status";

    /**
     * {@code BOT_PLATFORM_KEY} is instance that memorizes bot platform key
     **/
    public static final String BOT_PLATFORM_KEY = "bot_platform";

    /**
     * {@code BOT_TYPE_KEY} is instance that memorizes bot type key
     **/
    public static final String BOT_TYPE_KEY = "bot_type";

    /**
     * {@code REFRESH_TIME_KEY} is instance that memorizes refresh time key
     **/
    public static final String REFRESH_TIME_KEY = "refresh_time";

    /**
     * {@code LAST_BOT_ACTIVITY_KEY} is instance that memorizes last bot activity key
     **/
    public static final String LAST_BOT_ACTIVITY_KEY = "last_bot_activity";

    /**
     * {@code RUNNING_FROM_DATE_KEY} is instance that memorizes running from date key
     **/
    public static final String RUNNING_FROM_DATE_KEY = "running_from_date";

    /**
     * {@code ACTIVATION_DATE_KEY} is instance that memorizes activation date key
     **/
    public static final String ACTIVATION_DATE_KEY = "activation_date";

    /**
     * {@code GAINS_KEY} is instance that memorizes sales at gain key
     **/
    public static final String GAINS_KEY = "sales_at_gain";

    /**
     * {@code LOSSES_KEY} is instance that memorizes sales at loss key
     **/
    public static final String LOSSES_KEY = "sales_at_loss";

    /**
     * {@code PAIRS_KEY} is instance that memorizes sales at pair key
     **/
    public static final String PAIRS_KEY = "sales_at_pair";

    /**
     * {@code TOTAL_INCOME_KEY} is instance that memorizes total income key
     **/
    public static final String TOTAL_INCOME_KEY = "total_income";

    /**
     * {@code INCOMES_KEY} is instance that memorizes incomes key
     **/
    public static final String INCOMES_KEY = "incomes";

    /**
     * {@code ROUTINES_KEY} is instance that memorizes routines key
     **/
    public static final String ROUTINES_KEY = "routines";

    /**
     * {@code ROUTINE_KEY} is instance that memorizes routine key
     **/
    public static final String ROUTINE_KEY = "routine";

    /**
     * {@code ROUTINE_EXTRA_VALUE_KEY} is instance that memorizes extra value of routine key
     **/
    public static final String ROUTINE_EXTRA_VALUE_KEY = "extra_value";

    /**
     * {@code CRYPTOCURRENCY_KEY} is instance that memorizes cryptocurrency key
     **/
    public static final String CRYPTOCURRENCY_KEY = "cryptocurrency";

    /**
     * {@code SYMBOL_KEY} is instance that memorizes symbol key
     **/
    public static final String SYMBOL_KEY = "symbol";

    /**
     * {@code LAST_PRICE_KEY} is instance that memorizes last price key
     **/
    public static final String LAST_PRICE_KEY = "last_price";

    /**
     * {@code FIRST_PRICE_KEY} is instance that memorizes last price key
     **/
    public static final String FIRST_PRICE_KEY = "first_price";

    /**
     * {@code TPTOP_INDEX_KEY} is instance that memorizes tptop index key
     **/
    public static final String TPTOP_INDEX_KEY = "tptop_index";

    /**
     * {@code CANDLE_GAP_KEY} is instance that memorizes candle gap key
     **/
    public static final String CANDLE_GAP_KEY = "candle_gap";

    /**
     * {@code PRICE_CHANGE_PERCENT_KEY} is instance that memorizes price change percent key
     **/
    public static final String PRICE_CHANGE_PERCENT_KEY = "price_change_percent";

    /**
     * {@code QUOTE_ASSET_KEY} is instance that memorizes quote asset key
     **/
    public static final String QUOTE_ASSET_KEY = "quote_asset";

    /**
     * {@code INCOME_PERCENT_KEY} is instance that memorizes income percent key
     **/
    public static final String INCOME_PERCENT_KEY = "income_percent";

    /**
     * {@code TRADING_CONFIG_KEY} is instance that memorizes trading config key
     **/
    public static final String TRADING_CONFIG_KEY = "trading_config";

    /**
     * {@code FIRST_PRICES_SUM_KEY} is instance that memorizes first prices size key
     **/
    public static final String FIRST_PRICES_SUM_KEY = "first_prices_sum";

    /**
     * {@code FIRST_PRICES_SIZE_KEY} is instance that memorizes first prices size key
     **/
    public static final String FIRST_PRICES_SIZE_KEY = "first_prices_size";

    /**
     * {@code BASE_ASSET_KEY} is instance that memorizes base asset key
     **/
    public static final String BASE_ASSET_KEY = "base_asset";

    /**
     * {@code ASSET_NAME_KEY} is instance that memorizes asset name key
     **/
    public static final String ASSET_NAME_KEY = "asset_name";

    /**
     * {@code QUANTITY_KEY} is instance that memorizes quantity key
     **/
    public static final String QUANTITY_KEY = "quantity";

    /**
     * {@code TRANSACTIONS_KEY} is instance that memorizes transactions key
     **/
    public static final String TRANSACTIONS_KEY = "transactions";

    /**
     * {@code TRANSACTION_KEY} is instance that memorizes transaction key
     **/
    public static final String TRANSACTION_KEY = "transaction";

    /**
     * {@code SIDE_KEY} is instance that memorizes side key
     **/
    public static final String SIDE_KEY = "side";

    /**
     * {@code TRANSACTION_DATE_KEY} is instance that memorizes transaction date key
     **/
    public static final String TRANSACTION_DATE_KEY = "transaction_date";

    /**
     * {@code VALUE_KEY} is instance that memorizes value key
     **/
    public static final String VALUE_KEY = "value";

    /**
     * {@code TRANSACTION_TYPE_KEY} is instance that memorizes transaction type key
     **/
    public static final String TRANSACTION_TYPE_KEY = "transaction_type";

    /**
     * {@code SERVICE_UNAVAILABLE} is instance to indicate that service is not available to perform requests
     **/
    public static final String SERVICE_UNAVAILABLE = ANSI_RED + "Service is not available for serve your request, wait" + ANSI_RESET;

    /**
     * {@code GET_KEYS_OPE} request
     **/
    public static final String GET_KEYS_OPE = "get_keys_ope";

    /**
     * {@code REGISTRATION_OPE} request
     **/
    public static final String REGISTRATION_OPE = "registration_ope";

    /**
     * {@code LOGIN_OPE} request
     **/
    public static final String LOGIN_OPE = "login_ope";

    /**
     * {@code GET_ACCOUNT_OPE} request
     **/
    public static final String GET_ACCOUNT_OPE = "get_account_ope";

    /**
     * {@code GET_ROUTINES_OPE} request
     **/
    public static final String GET_ROUTINES_OPE = "get_routines_ope";

    /**
     * {@code CHANGE_EMAIL_OPE} request
     **/
    public static final String CHANGE_EMAIL_OPE = "change_email_ope";

    /**
     * {@code CHANGE_PASSWORD_OPE} request
     **/
    public static final String CHANGE_PASSWORD_OPE = "change_password_ope";

    /**
     * {@code CHANGE_REFRESH_TIME_OPE} request
     **/
    public static final String CHANGE_REFRESH_TIME_OPE = "change_refresh_time_ope";

    /**
     * {@code CHANGE_BOT_STATUS_OPE} request
     **/
    public static final String CHANGE_BOT_STATUS_OPE = "change_bot_status_ope";

    /**
     * {@code CHANGE_LANGUAGE_OPE} request
     **/
    public static final String CHANGE_LANGUAGE_OPE = "change_language_ope";

    /**
     * {@code CHANGE_CURRENCY_OPE} request
     **/
    public static final String CHANGE_CURRENCY_OPE = "change_currency_ope";

    /**
     * {@code CHANGE_DELETION_ACCOUNT_TIME_OPE} request
     **/
    public static final String CHANGE_DELETION_ACCOUNT_TIME_OPE = "change_deletion_account_time_ope";

    /**
     * {@code INSERT_WALLET_BALANCE_OPE} request
     **/
    public static final String INSERT_WALLET_BALANCE_OPE = "insert_wallet_balance_ope";

    /**
     * {@code INSERT_CRYPTOCURRENCY_OPE} request
     **/
    public static final String INSERT_CRYPTOCURRENCY_OPE = "insert_cryptocurrency_ope";

    /**
     * {@code DELETE_CRYPTOCURRENCY_OPE} request
     **/
    public static final String DELETE_CRYPTOCURRENCY_OPE = "delete_cryptocurrency_ope";

    /**
     * {@code INSERT_REFRESHED_PRICES} request
     **/
    public static final String INSERT_REFRESHED_PRICES = "insert_refreshed_prices_ope";

    /**
     * {@code INSERT_QUOTE_OPE} request
     **/
    public static final String INSERT_QUOTE_OPE = "insert_quote_ope";

    /**
     * {@code INSERT_QUOTES_OPE} request
     **/
    public static final String INSERT_QUOTES_OPE = "insert_quotes_ope";

    /**
     * {@code REMOVE_QUOTE_OPE} request
     **/
    public static final String REMOVE_QUOTE_OPE = "remove_quote_ope";

    /**
     * {@code INSERT_CHECKING_LIST_OPE} request
     **/
    public static final String INSERT_CHECKING_LIST_OPE = "insert_checking_list_ope";

    /**
     * {@code INSERT_WALLET_LIST_OPE} request
     **/
    public static final String INSERT_WALLET_LIST_OPE = "insert_wallet_list_ope";

    /**
     * {@code GET_TRADING_CONFIGS_OPE} request
     **/
    public static final String GET_TRADING_CONFIGS_OPE = "get_trading_configs_request";

    /**
     * {@code SEND_STATS_REPORT_OPE} request
     **/
    public static final String SEND_STATS_REPORT_OPE = "send_stats_report_ope";

    /**
     * {@code DELETE_TRANSACTION_OPE} request
     *
     * @apiNote base workflow of this operation is to delete a transaction from
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s application,
     * but not to delete transaction in server side use. For that use you can see <a href="https://github.com/N7ghtm4r3/TxNotes">TxNotes</a>'s
     * library that allows to delete also in server side use and not only from <a href="https://play.google.com/store/apps/details?id=com.tecknobit.txnotes">TxNotes</a>'s
     * application
     **/
    public static final String DELETE_TRANSACTION_OPE = "delete_transaction_ope";

    /**
     * {@code LOGOUT_ACCOUNT_OPE} request
     **/
    public static final String LOGOUT_ACCOUNT_OPE = "logout_account_ope";

    /**
     * {@code DELETE_ACCOUNT_OPE} request
     **/
    public static final String DELETE_ACCOUNT_OPE = "delete_account_ope";

    /**
     * {@code CREDENTIALS_ERROR_RESPONSE} response
     **/
    public static final int CREDENTIALS_ERROR_RESPONSE = 400;

    /**
     * {@code GENERIC_ERROR_RESPONSE} response
     **/
    public static final int GENERIC_ERROR_RESPONSE = 300;

    /**
     * {@code SUCCESSFUL_RESPONSE} response
     **/
    public static final int SUCCESSFUL_RESPONSE = 200;

    /**
     * {@code ERROR_RESPONSE} response
     **/
    public static final int ERROR_RESPONSE = 500;

    /**
     * {@code STATUS_CODE} key
     **/
    public static final String STATUS_CODE = "status_code";

    /**
     * {@code TOKEN_KEY} key
     **/
    public static final String TOKEN_KEY = "token";

    /**
     * {@code EMAIL_KEY} key
     **/
    public static final String EMAIL_KEY = "email";

    /**
     * {@code NEW_EMAIL_KEY} key
     **/
    public static final String NEW_EMAIL_KEY = "new_email";

    /**
     * {@code PASSWORD_KEY} key
     **/
    public static final String PASSWORD_KEY = "password";

    /**
     * {@code NEW_PASSWORD_KEY} key
     **/
    public static final String NEW_PASSWORD_KEY = "new_password";

    /**
     * {@code IV_SPEC_KEY} key
     **/
    public static final String IV_SPEC_KEY = "iv_spec";

    /**
     * {@code SECRET_KEY} key
     **/
    public static final String SECRET_KEY = "secret_key";

    /**
     * {@code DEVICE_TOKEN_KEY} key
     **/
    public static final String DEVICE_TOKEN_KEY = "device_token";

    /**
     * {@code AUTH_TOKEN_KEY} key
     **/
    public static final String AUTH_TOKEN_KEY = "auth_token";

    /**
     * {@code LANGUAGE_KEY} key
     **/
    public static final String LANGUAGE_KEY = "language";

    /**
     * {@code CURRENCY_KEY} key
     **/
    public static final String CURRENCY_KEY = "currency";

    /**
     * {@code DELETION_TIME_KEY} key
     **/
    public static final String DELETION_TIME_KEY = "deletion_time";

    /**
     * {@code QUOTE_KEY} key
     **/
    public static final String QUOTE_KEY = "quote";

    /**
     * {@code BASE_CURRENCY_KEY} key
     **/
    public static final String BASE_CURRENCY_KEY = "base_currency";

    /**
     * {@code QUOTES_KEY} key
     **/
    public static final String QUOTES_KEY = "quotes";

    /**
     * {@code BALANCE_KEY} key
     **/
    public static final String BALANCE_KEY = "balance";

    /**
     * {@code SERVER_REQUEST_KEY} key
     **/
    public static final String SERVER_REQUEST_KEY = "server_request";

    /**
     * {@code Side} list of available sides
     **/
    public enum Side {

        /**
         * {@code "BUY"} side
         **/
        BUY,

        /**
         * {@code "SELL"} side
         **/
        SELL

    }

    /**
     * Constructor to avoid instantiation
     **/
    private TraderBotConstants() {
    }

}
