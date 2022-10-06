package com.tecknobit.traderbot.Routines.Interfaces;

import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.BUY_SIDE;
import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.SELL_SIDE;
import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.ANSI_RED;
import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.ANSI_RESET;

/**
 * The {@code TraderBotConstants} interface defines all constants used in TraderBot library that are useful also
 * for a custom easy use.
 *
 * @author Tecknobit N7ghtm4r3
 **/

public interface TraderBotConstants {

    /**
     * {@code USD_CURRENCY} is the identifier of USD currency used by traders to get default prices
     **/
    String USD_CURRENCY = "USD";

    /**
     * {@code USDT_CURRENCY} is the identifier of USDT cryptocurrency used by traders to get default prices
     **/
    String USDT_CURRENCY = "USDT";

    /**
     * {@code BUY} is constant for buy side
     **/
    String BUY = BUY_SIDE.toUpperCase();

    /**
     * {@code SELL} is constant for buy side
     **/
    String SELL = SELL_SIDE.toUpperCase();

    /**
     * {@code CUSTOM_DEF_COLOR} is instance that memorizes default color to use for example when asset is at pair
     *
     * @implSpec not available for console color
     **/
    String CUSTOM_DEF_COLOR = "#A8A8A8";

    /**
     * {@code CUSTOM_RED_COLOR} is instance that memorizes red color to use for example when asset is at loss
     *
     * @implSpec not available for console color
     **/
    String CUSTOM_RED_COLOR = "#A80202";

    /**
     * {@code CUSTOM_GREEN_COLOR} is instance that memorizes green color to use for example when asset is at gain
     *
     * @implSpec not available for console color
     **/
    String CUSTOM_GREEN_COLOR = "#079A44";

    /**
     * {@code BINANCE_COLOR} is instance that memorizes Binance official color
     *
     * @implSpec not available for console color
     **/
    String BINANCE_COLOR = "#F3BA2F";

    /**
     * {@code COINBASE_COLOR} is instance that memorizes Coinbase official color
     *
     * @implSpec not available for console color
     **/
    String COINBASE_COLOR = "#1652f0";

    /**
     * {@code BOT_TYPE_AUTONOMOUS} is instance that memorizes AUTONOMOUS bot type in use
     **/
    String BOT_TYPE_AUTONOMOUS = "AUTONOMOUS";

    /**
     * {@code BOT_TYPE_MANUAL} is instance that memorizes MANUAL bot type in use
     **/
    String BOT_TYPE_MANUAL = "MANUAL";

    /**
     * {@code BINANCE_PLATFORM} is instance that memorizes BINANCE bot platform in use
     **/
    String BINANCE_PLATFORM = "BINANCE";

    /**
     * {@code COINBASE_PLATFORM} is instance that memorizes COINBASE bot platform in use
     **/
    String COINBASE_PLATFORM = "COINBASE";

    /**
     * {@code RUNNING_BOT_STATUS} is instance that memorizes RUNNING bot current status
     **/
    String RUNNING_BOT_STATUS = "RUNNING";

    /**
     * {@code STOPPED_BOT_STATUS} is instance that memorizes STOPPED bot current status
     **/
    String STOPPED_BOT_STATUS = "STOPPED";

    /**
     * {@code BOT_STATUS_KEY} is instance that memorizes bot status key
     **/
    String BOT_STATUS_KEY = "bot_status";

    /**
     * {@code BOT_PLATFORM_KEY} is instance that memorizes bot platform key
     **/
    String BOT_PLATFORM_KEY = "bot_platform";

    /**
     * {@code BOT_TYPE_KEY} is instance that memorizes bot type key
     **/
    String BOT_TYPE_KEY = "bot_type";

    /**
     * {@code REFRESH_TIME_KEY} is instance that memorizes refresh time key
     **/
    String REFRESH_TIME_KEY = "refresh_time";

    /**
     * {@code LAST_BOT_ACTIVITY_KEY} is instance that memorizes last bot activity key
     **/
    String LAST_BOT_ACTIVITY_KEY = "last_bot_activity";

    /**
     * {@code RUNNING_FROM_DATE_KEY} is instance that memorizes running from date key
     **/
    String RUNNING_FROM_DATE_KEY = "running_from_date";

    /**
     * {@code ACTIVATION_DATE_KEY} is instance that memorizes activation date key
     **/
    String ACTIVATION_DATE_KEY = "activation_date";

    /**
     * {@code GAINS_KEY} is instance that memorizes sales at gain key
     **/
    String GAINS_KEY = "sales_at_gain";

    /**
     * {@code LOSSES_KEY} is instance that memorizes sales at loss key
     **/
    String LOSSES_KEY = "sales_at_loss";

    /**
     * {@code PAIRS_KEY} is instance that memorizes sales at pair key
     **/
    String PAIRS_KEY = "sales_at_pair";

    /**
     * {@code TOTAL_INCOME_KEY} is instance that memorizes total income key
     **/
    String TOTAL_INCOME_KEY = "total_income";

    /**
     * {@code INCOMES_KEY} is instance that memorizes incomes key
     **/
    String INCOMES_KEY = "incomes";

    /**
     * {@code ROUTINES_KEY} is instance that memorizes routines key
     **/
    String ROUTINES_KEY = "routines";

    /**
     * {@code ROUTINE_KEY} is instance that memorizes routine key
     **/
    String ROUTINE_KEY = "routine";

    /**
     * {@code ROUTINE_EXTRA_VALUE_KEY} is instance that memorizes extra value of routine key
     **/
    String ROUTINE_EXTRA_VALUE_KEY = "extra_value";

    /**
     * {@code CRYPTOCURRENCY_KEY} is instance that memorizes cryptocurrency key
     **/
    String CRYPTOCURRENCY_KEY = "cryptocurrency";

    /**
     * {@code SYMBOL_KEY} is instance that memorizes symbol key
     **/
    String SYMBOL_KEY = "symbol";

    /**
     * {@code LAST_PRICE_KEY} is instance that memorizes last price key
     **/
    String LAST_PRICE_KEY = "last_price";

    /**
     * {@code FIRST_PRICE_KEY} is instance that memorizes last price key
     **/
    String FIRST_PRICE_KEY = "first_price";

    /**
     * {@code TPTOP_INDEX_KEY} is instance that memorizes tptop index key
     **/
    String TPTOP_INDEX_KEY = "tptop_index";

    /**
     * {@code CANDLE_GAP_KEY} is instance that memorizes candle gap key
     **/
    String CANDLE_GAP_KEY = "candle_gap";

    /**
     * {@code PRICE_CHANGE_PERCENT_KEY} is instance that memorizes price change percent key
     **/
    String PRICE_CHANGE_PERCENT_KEY = "price_change_percent";

    /**
     * {@code QUOTE_ASSET_KEY} is instance that memorizes quote asset key
     **/
    String QUOTE_ASSET_KEY = "quote_asset";

    /**
     * {@code INCOME_PERCENT_KEY} is instance that memorizes income percent key
     **/
    String INCOME_PERCENT_KEY = "income_percent";

    /**
     * {@code TRADING_CONFIG_KEY} is instance that memorizes trading config key
     **/
    String TRADING_CONFIG_KEY = "trading_config";

    /**
     * {@code FIRST_PRICES_SUM_KEY} is instance that memorizes first prices size key
     **/
    String FIRST_PRICES_SUM_KEY = "first_prices_sum";

    /**
     * {@code FIRST_PRICES_SIZE_KEY} is instance that memorizes first prices size key
     **/
    String FIRST_PRICES_SIZE_KEY = "first_prices_size";

    /**
     * {@code BASE_ASSET_KEY} is instance that memorizes base asset key
     **/
    String BASE_ASSET_KEY = "base_asset";

    /**
     * {@code ASSET_NAME_KEY} is instance that memorizes asset name key
     **/
    String ASSET_NAME_KEY = "asset_name";

    /**
     * {@code QUANTITY_KEY} is instance that memorizes quantity key
     **/
    String QUANTITY_KEY = "quantity";

    /**
     * {@code TRANSACTIONS_KEY} is instance that memorizes transactions key
     **/
    String TRANSACTIONS_KEY = "transactions";

    /**
     * {@code TRANSACTION_KEY} is instance that memorizes transaction key
     **/
    String TRANSACTION_KEY = "transaction";

    /**
     * {@code SIDE_KEY} is instance that memorizes side key
     **/
    String SIDE_KEY = "side";

    /**
     * {@code TRANSACTION_DATE_KEY} is instance that memorizes transaction date key
     **/
    String TRANSACTION_DATE_KEY = "transaction_date";

    /**
     * {@code VALUE_KEY} is instance that memorizes value key
     **/
    String VALUE_KEY = "value";

    /**
     * {@code TRANSACTION_TYPE_KEY} is instance that memorizes transaction type key
     **/
    String TRANSACTION_TYPE_KEY = "transaction_type";

    /**
     * {@code SERVICE_UNAVAILABLE} is instance to indicate that service is not available to perform requests
     **/
    String SERVICE_UNAVAILABLE = ANSI_RED + "Service is not available for serve your request, wait" + ANSI_RESET;

    /**
     * {@code GET_KEYS_OPE} request
     **/
    String GET_KEYS_OPE = "get_keys_ope";

    /**
     * {@code REGISTRATION_OPE} request
     **/
    String REGISTRATION_OPE = "registration_ope";

    /**
     * {@code LOGIN_OPE} request
     **/
    String LOGIN_OPE = "login_ope";

    /**
     * {@code GET_ACCOUNT_OPE} request
     **/
    String GET_ACCOUNT_OPE = "get_account_ope";

    /**
     * {@code GET_ROUTINES_OPE} request
     **/
    String GET_ROUTINES_OPE = "get_routines_ope";

    /**
     * {@code CHANGE_EMAIL_OPE} request
     **/
    String CHANGE_EMAIL_OPE = "change_email_ope";

    /**
     * {@code CHANGE_PASSWORD_OPE} request
     **/
    String CHANGE_PASSWORD_OPE = "change_password_ope";

    /**
     * {@code CHANGE_REFRESH_TIME_OPE} request
     **/
    String CHANGE_REFRESH_TIME_OPE = "change_refresh_time_ope";

    /**
     * {@code CHANGE_BOT_STATUS_OPE} request
     **/
    String CHANGE_BOT_STATUS_OPE = "change_bot_status_ope";

    /**
     * {@code CHANGE_LANGUAGE_OPE} request
     **/
    String CHANGE_LANGUAGE_OPE = "change_language_ope";

    /**
     * {@code CHANGE_CURRENCY_OPE} request
     **/
    String CHANGE_CURRENCY_OPE = "change_currency_ope";

    /**
     * {@code CHANGE_DELETION_ACCOUNT_TIME_OPE} request
     **/
    String CHANGE_DELETION_ACCOUNT_TIME_OPE = "change_deletion_account_time_ope";

    /**
     * {@code INSERT_WALLET_BALANCE_OPE} request
     **/
    String INSERT_WALLET_BALANCE_OPE = "insert_wallet_balance_ope";

    /**
     * {@code INSERT_CRYPTOCURRENCY_OPE} request
     **/
    String INSERT_CRYPTOCURRENCY_OPE = "insert_cryptocurrency_ope";

    /**
     * {@code DELETE_CRYPTOCURRENCY_OPE} request
     **/
    String DELETE_CRYPTOCURRENCY_OPE = "delete_cryptocurrency_ope";

    /**
     * {@code INSERT_REFRESHED_PRICES} request
     **/
    String INSERT_REFRESHED_PRICES = "insert_refreshed_prices_ope";

    /**
     * {@code INSERT_QUOTE_OPE} request
     **/
    String INSERT_QUOTE_OPE = "insert_quote_ope";

    /**
     * {@code INSERT_QUOTES_OPE} request
     **/
    String INSERT_QUOTES_OPE = "insert_quotes_ope";

    /**
     * {@code REMOVE_QUOTE_OPE} request
     **/
    String REMOVE_QUOTE_OPE = "remove_quote_ope";

    /**
     * {@code INSERT_CHECKING_LIST_OPE} request
     **/
    String INSERT_CHECKING_LIST_OPE = "insert_checking_list_ope";

    /**
     * {@code INSERT_WALLET_LIST_OPE} request
     **/
    String INSERT_WALLET_LIST_OPE = "insert_wallet_list_ope";

    /**
     * {@code GET_TRADING_CONFIGS_OPE} request
     **/
    String GET_TRADING_CONFIGS_OPE = "get_trading_configs_request";

    /**
     * {@code SEND_STATS_REPORT_OPE} request
     **/
    String SEND_STATS_REPORT_OPE = "send_stats_report_ope";

    /**
     * {@code DELETE_TRANSACTION_OPE} request
     *
     * @apiNote base workflow of this operation is to delete a transaction from
     * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s application,
     * but not to delete transaction in server side use. For that use you can see <a href="https://github.com/N7ghtm4r3/TxNotes">TxNotes</a>'s
     * library that allows to delete also in server side use and not only from <a href="https://play.google.com/store/apps/details?id=com.tecknobit.txnotes">TxNotes</a>'s
     * application
     **/
    String DELETE_TRANSACTION_OPE = "delete_transaction_ope";

    /**
     * {@code LOGOUT_ACCOUNT_OPE} request
     **/
    String LOGOUT_ACCOUNT_OPE = "logout_account_ope";

    /**
     * {@code DELETE_ACCOUNT_OPE} request
     **/
    String DELETE_ACCOUNT_OPE = "delete_account_ope";

    /**
     * {@code CREDENTIALS_ERROR_RESPONSE} response
     **/
    int CREDENTIALS_ERROR_RESPONSE = 400;

    /**
     * {@code GENERIC_ERROR_RESPONSE} response
     **/
    int GENERIC_ERROR_RESPONSE = 300;

    /**
     * {@code SUCCESSFUL_RESPONSE} response
     **/
    int SUCCESSFUL_RESPONSE = 200;

    /**
     * {@code ERROR_RESPONSE} response
     **/
    int ERROR_RESPONSE = 500;

    /**
     * {@code STATUS_CODE} key
     **/
    String STATUS_CODE = "status_code";

    /**
     * {@code TOKEN_KEY} key
     **/
    String TOKEN_KEY = "token";

    /**
     * {@code EMAIL_KEY} key
     **/
    String EMAIL_KEY = "email";

    /**
     * {@code NEW_EMAIL_KEY} key
     **/
    String NEW_EMAIL_KEY = "new_email";

    /**
     * {@code PASSWORD_KEY} key
     **/
    String PASSWORD_KEY = "password";

    /**
     * {@code NEW_PASSWORD_KEY} key
     **/
    String NEW_PASSWORD_KEY = "new_password";

    /**
     * {@code IV_SPEC_KEY} key
     **/
    String IV_SPEC_KEY = "iv_spec";

    /**
     * {@code SECRET_KEY} key
     **/
    String SECRET_KEY = "secret_key";

    /**
     * {@code DEVICE_TOKEN_KEY} key
     **/
    String DEVICE_TOKEN_KEY = "device_token";

    /**
     * {@code AUTH_TOKEN_KEY} key
     **/
    String AUTH_TOKEN_KEY = "auth_token";

    /**
     * {@code LANGUAGE_KEY} key
     **/
    String LANGUAGE_KEY = "language";

    /**
     * {@code CURRENCY_KEY} key
     **/
    String CURRENCY_KEY = "currency";

    /**
     * {@code DELETION_TIME_KEY} key
     **/
    String DELETION_TIME_KEY = "deletion_time";

    /**
     * {@code QUOTE_KEY} key
     **/
    String QUOTE_KEY = "quote";

    /**
     * {@code BASE_CURRENCY_KEY} key
     **/
    String BASE_CURRENCY_KEY = "base_currency";

    /**
     * {@code QUOTES_KEY} key
     **/
    String QUOTES_KEY = "quotes";

    /**
     * {@code BALANCE_KEY} key
     **/
    String BALANCE_KEY = "balance";

    /**
     * {@code SERVER_REQUEST_KEY} key
     **/
    String SERVER_REQUEST_KEY = "server_request";

}