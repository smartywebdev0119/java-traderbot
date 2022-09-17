package com.tecknobit.traderbot.Routines.Android;

import com.tecknobit.apimanager.Tools.Formatters.JsonHelper;
import com.tecknobit.traderbot.Exceptions.SaveData;
import com.tecknobit.traderbot.Records.Account.BotDetails;
import com.tecknobit.traderbot.Records.Account.TraderAccount;
import com.tecknobit.traderbot.Records.Android.Routine;
import com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency;
import com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.TradingConfig;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages;
import com.tecknobit.traderbot.Routines.Interfaces.TraderCoreRoutines;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.traderbot.Records.Account.BotDetails.*;
import static com.tecknobit.traderbot.Records.Account.Trader.TraderManager.*;
import static com.tecknobit.traderbot.Records.Account.TraderAccount.TOTAL_INCOME_KEY;
import static com.tecknobit.traderbot.Records.Android.Routine.*;
import static com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.CRYPTOCURRENCY_KEY;
import static com.tecknobit.traderbot.Records.Portfolio.Transaction.TRANSACTION_KEY;
import static com.tecknobit.traderbot.Records.Portfolio.Transaction.getDate;
import static com.tecknobit.traderbot.Routines.Android.ServerRequest.*;
import static java.lang.Integer.parseInt;
import static java.lang.System.*;
import static org.apache.commons.validator.routines.EmailValidator.getInstance;

/**
 * The {@code AndroidWorkflow} class is useful to manage Android's bots workflow<br>
 * Is useful for Android's type bots.
 *
 * @author Tecknobit N7ghtm4r3
 **/

public class AndroidWorkflow implements RoutineMessages {

    /**
     * {@code alreadyInstantiated} flag to lock multiple instantiations of {@link AndroidWorkflow} object
     **/
    protected static boolean alreadyInstantiated = false;

    /**
     * {@code serverRequest} instance to make server request for Android's bots
     * **/
    protected final ServerRequest serverRequest;

    /**
     * {@code trader} instance of Android's traders used
     * **/
    protected final TraderCoreRoutines trader;

    /**
     * {@code credentials} instance that contains your Tecknobit's account credentials, not your private exchange keys
     * **/
    protected final Credentials credentials;

    /**
     * {@code printRoutineMessages} flag to insert to print or not routine messages
     * **/
    protected boolean printRoutineMessages;

    /**
     * {@code workflowStarted} flag to indicate if Android's workflow has been started
     * **/
    protected boolean workflowStarted;

    /** Constructor to init {@link AndroidWorkflow}
     * @param serverRequest: instance to make server request for Android's traders
     * @param trader: instance of Android's traders used
     * @param credentials : instance that contains your Tecknobit's account credentials, not your private exchange keys
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * **/
    public AndroidWorkflow(ServerRequest serverRequest, TraderCoreRoutines trader, Credentials credentials,
                           boolean printRoutineMessages) {
        if(!alreadyInstantiated)
            alreadyInstantiated = true;
        else {
            out.println(ANSI_RED + "AndroidWorkflow object is already instantiated you cannot have multiple AndroidWorkflow " +
                    "objects in same session" + ANSI_RESET);
            exit(1);
        }
        this.serverRequest = serverRequest;
        this.printRoutineMessages = printRoutineMessages;
        this.credentials = credentials;
        this.trader = trader;
        workflowStarted = false;
    }

    /**
     * This method is used to start Android's workflow <br>
     * Any params required
     * **/
    public void startWorkflow(){
        if(!workflowStarted) {
            workflowStarted = true;
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        while (true){
                            performRoutines();
                            sleep(5000);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();
        }
    }

    /**
     * This method is used to perform routines of Android's workflow <br>
     * Any params required
     * **/
    protected void performRoutines() {
        for (Routine routine : getRoutines()) {
            switch (routine.getRoutine()) {
                case CHANGE_EMAIL_OPE:
                    credentials.setEmail(routine.getExtraValue());
                    printOperationStatus("[" + CHANGE_EMAIL_OPE + "] Email successfully changed", true);
                    break;
                case CHANGE_PASSWORD_OPE:
                    credentials.setPassword(routine.getExtraValue());
                    printOperationStatus("[" + CHANGE_PASSWORD_OPE + "] Password successfully changed", true);
                    break;
                case CHANGE_REFRESH_TIME_OPE:
                    trader.setRefreshTime(parseInt(routine.getExtraValue()));
                    printOperationStatus("[" + CHANGE_REFRESH_TIME_OPE + "] Refresh prices time successfully changed",
                            true);
                    break;
                case CHANGE_BOT_STATUS_OPE:
                    printOperationStatus("[" + CHANGE_BOT_STATUS_OPE + "] Bot status successfully changed",
                            true);
                    if (routine.getExtraValue().equals(STOPPED_BOT_STATUS)) {
                        if (trader instanceof AndroidCoreRoutines) {
                            ((AndroidCoreRoutines) trader).disableTrader();
                            printOperationStatus("Bot status: [" + STOPPED_BOT_STATUS + "]", false);
                        }
                    } else {
                        if (trader instanceof AndroidCoreRoutines) {
                            ((AndroidCoreRoutines) trader).enableTrader();
                            printOperationStatus("Bot status: [" + RUNNING_BOT_STATUS + "]", true);
                        }
                    }
                    break;
                case CHANGE_CURRENCY_OPE:
                    if (trader instanceof AndroidCoreRoutines) {
                        ((AndroidCoreRoutines) trader).setBaseCurrency(routine.getExtraValue());
                        printOperationStatus("[" + CHANGE_CURRENCY_OPE + "] Base currency successfully changed", true);
                    }
                    break;
                case INSERT_QUOTE_OPE:
                    String quoteAdd = routine.getExtraValue();
                    trader.insertQuoteCurrency(quoteAdd);
                    printOperationStatus("[" + INSERT_QUOTE_OPE + "] Quote currency [" + quoteAdd + "] successfully inserted",
                            true);
                    break;
                case REMOVE_QUOTE_OPE:
                    String quoteRemove = routine.getExtraValue();
                    trader.removeQuoteCurrency(quoteRemove);
                    printOperationStatus("[" + REMOVE_QUOTE_OPE + "] Quote currency [" + quoteRemove + "] successfully removed",
                            true);
            }
        }
    }

    /**
     * This method is used to fetch routines of Android's workflow <br>
     * Any params required
     * **/
    protected ArrayList<Routine> getRoutines() {
        ArrayList<Routine> routines = new ArrayList<>();
        try {
            serverRequest.sendTokenRequest(new JSONObject(), GET_ROUTINES_OPE);
            response = serverRequest.readResponse();
            if (response != null) {
                if (response.getInt(STATUS_CODE) != -1) {
                    JSONArray jsonRoutines = JsonHelper.getJSONArray(response, ROUTINES_KEY, new JSONArray());
                    for (int j = 0; j < jsonRoutines.length(); j++) {
                        JSONObject routine = jsonRoutines.getJSONObject(j);
                        routines.add(new Routine(routine.getString(ROUTINE_KEY), routine.getString(ROUTINE_EXTRA_VALUE_KEY)));
                    }
                }else{
                    printRed("[ACCOUNT DELETED] You deleted account for the bot, we hope to see you again soon!");
                    exit(0);
                }
            }
        } catch (Exception e) {
            printOperationFailed(GET_ROUTINES_OPE);
        }
        return routines;
    }

    /**
     * This method is used to insert wallet balance <br>
     * @param balance: value of balance to insert
     * **/
    public void insertWalletBalance(double balance) {
        try {
            serverRequest.sendTokenRequest(new JSONObject().put(BALANCE_KEY, balance), INSERT_WALLET_BALANCE_OPE);
            response = serverRequest.readResponse();
            if(response != null){
                switch (response.getInt(STATUS_CODE)){
                    case SUCCESSFUL_RESPONSE:
                        if(printRoutineMessages)
                            printOperationSuccess(INSERT_WALLET_BALANCE_OPE);
                        break;
                    case GENERIC_ERROR_RESPONSE:
                        printOperationStatus("[" + INSERT_WALLET_BALANCE_OPE + "] Wallet balance cannot be less than 0",
                                false);
                        break;
                    default:
                        printOperationFailed(INSERT_WALLET_BALANCE_OPE);
                }
            }else
                printOperationFailed(INSERT_WALLET_BALANCE_OPE);
        } catch (Exception e) {
            printOperationFailed(INSERT_WALLET_BALANCE_OPE);
        }
    }

    /**
     * This method is used to clear from wallet list {@link Cryptocurrency} that has not got {@link TradingConfig}
     * @param wallet: wallet of user trader
     * @implSpec is useful for Android auto traders
     * **/
    public void checkWalletList(ConcurrentHashMap<String, Cryptocurrency> wallet){
        for (Cryptocurrency cryptocurrency : wallet.values())
            if(cryptocurrency.getTradingConfig() == null)
                wallet.remove(cryptocurrency.getAssetIndex());
    }

    /**
     * This method is used to add and get sales to increment
     * @param cryptocurrency: cryptocurrency of order
     * @implSpec is useful for Android auto traders
     * @return sales to increment as int
     * **/
    public int getSellSales(Transaction transaction, TraderAccount traderAccount, Cryptocurrency cryptocurrency,
                            String sellCode){
        int sales;
        transaction.setIncomePercent(cryptocurrency.getIncomePercent(2));
        transaction.setTransactionType(sellCode);
        switch (sellCode){
            case LOSS_SELL:
                traderAccount.addLoss();
                sales = traderAccount.getSalesAtLoss();
                break;
            case GAIN_SELL:
                traderAccount.addGain();
                sales = traderAccount.getSalesAtGain();
                break;
            default:
                traderAccount.addPair();
                sales = traderAccount.getSalesAtPair();
        }
        return sales;
    }

    /**
     * This method is used to assemble a {@link Transaction} object
     * @param cryptocurrency: cryptocurrency of order
     * @param side: side of order
     * @param dateFormat: date formatter for transaction
     * @implSpec is useful for Android auto traders
     * @return transaction as {@link Transaction} object
     * **/
    public Transaction assembleTransaction(Cryptocurrency cryptocurrency, String side, DateFormat dateFormat){
        return new Transaction(cryptocurrency.getSymbol(), side, dateFormat.format(new Date(currentTimeMillis())),
                cryptocurrency.getCryptocurrencyBalance(2), cryptocurrency.getQuantity(),
                cryptocurrency.getQuoteAsset(), cryptocurrency.getAssetIndex());
    }

    /**
     * This method is used to insert cryptocurrency in the user wallet<br>
     * @param cryptocurrency: cryptocurrency to insert
     * @param transaction: transaction to insert
     * @param sales: sales to insert
     * @param totalIncome: total income of trader to insert
     * **/
    public void insertCryptocurrency(Cryptocurrency cryptocurrency, Transaction transaction, int sales, double totalIncome) {
        try {
            JSONObject request = new JSONObject().put(CRYPTOCURRENCY_KEY, cryptocurrency.getCryptocurrency())
                    .put(TRANSACTION_KEY, transaction.getTransaction());
            if(sales > 0) {
                request.put(SELL_KEY, sales);
                request.put(TOTAL_INCOME_KEY, totalIncome);
            }
            serverRequest.sendTokenRequest(request, INSERT_CRYPTOCURRENCY_OPE);
            response = serverRequest.readResponse();
            if(response != null){
                switch (response.getInt(STATUS_CODE)){
                    case SUCCESSFUL_RESPONSE:
                        printOperationSuccess(INSERT_CRYPTOCURRENCY_OPE);
                        break;
                    case GENERIC_ERROR_RESPONSE:
                        printOperationStatus("[" + INSERT_WALLET_BALANCE_OPE + "] Insert a valid cryptocurrency value",
                                false);
                        break;
                    default:
                        printOperationFailed(INSERT_CRYPTOCURRENCY_OPE);
                }
            }else
                printOperationFailed(INSERT_CRYPTOCURRENCY_OPE);
        }catch (Exception e){
            printOperationFailed(INSERT_CRYPTOCURRENCY_OPE);
        }
    }

    /**
     * This method is used to remove cryptocurrency from the user wallet<br>
     * @param assetIndex: cryptocurrency to insert
     * @param transaction: transaction to insert
     * **/
    public void removeCryptocurrency(String assetIndex, Transaction transaction){
        try {
            serverRequest.sendTokenRequest(new JSONObject().put(CRYPTOCURRENCY_KEY, assetIndex)
                            .put(TRANSACTION_KEY, transaction.getTransaction()), DELETE_CRYPTOCURRENCY_OPE);
            response = serverRequest.readResponse();
            if(response != null){
                switch (response.getInt(STATUS_CODE)){
                    case SUCCESSFUL_RESPONSE:
                        printOperationSuccess(DELETE_CRYPTOCURRENCY_OPE);
                        break;
                    case GENERIC_ERROR_RESPONSE:
                        printOperationStatus("[" + DELETE_CRYPTOCURRENCY_OPE + "] Insert a valid cryptocurrency index",
                                false);
                        break;
                    default:
                        printOperationFailed(DELETE_CRYPTOCURRENCY_OPE);
                }
            }else
                printOperationFailed(DELETE_CRYPTOCURRENCY_OPE);
        }catch (Exception e){
            printOperationFailed(DELETE_CRYPTOCURRENCY_OPE);
        }
    }

    /**
     * This method is used to refresh details of cryptocurrency of the user wallet<br>
     * @param wallet: wallet with updated values of a cryptocurrency
     * **/
    public void insertRefreshedPrices(JSONArray wallet) throws Exception {
        if(wallet.length() > 0){
            serverRequest.sendTokenRequest(new JSONObject().put(CRYPTOCURRENCY_KEY, wallet), INSERT_REFRESHED_PRICES);
            response = serverRequest.readResponse();
            if(response != null){
                switch (response.getInt(STATUS_CODE)){
                    case SUCCESSFUL_RESPONSE:
                        printOperationSuccess(INSERT_REFRESHED_PRICES);
                        break;
                    case GENERIC_ERROR_RESPONSE:
                        printOperationStatus("[" + INSERT_REFRESHED_PRICES + "] Insert a valid wallet list",
                                false);
                        break;
                    default: printOperationFailed(INSERT_REFRESHED_PRICES);
                }
            }else
                printOperationFailed(INSERT_REFRESHED_PRICES);
        }
    }

    /**
     * This method is used to insert checking list of cryptocurrencies<br>
     * @param checkingList: list of cryptocurrencies
     * @implSpec is useful for Android auto traders
     * **/
    public void insertCheckingList(JSONArray checkingList) throws Exception {
        serverRequest.sendTokenRequest(new JSONObject().put(CRYPTOCURRENCY_KEY, checkingList), INSERT_CHECKING_LIST_OPE);
        response = serverRequest.readResponse();
        if(response != null){
            switch (response.getInt(STATUS_CODE)){
                case SUCCESSFUL_RESPONSE:
                    printOperationSuccess(INSERT_CHECKING_LIST_OPE);
                    break;
                case GENERIC_ERROR_RESPONSE:
                    printOperationStatus("[" + INSERT_CHECKING_LIST_OPE + "] Insert a valid checking list",
                            false);
                    break;
                default: printOperationFailed(INSERT_CHECKING_LIST_OPE);
            }
        }else
            printOperationFailed(INSERT_CHECKING_LIST_OPE);
    }

    /**
     * This method is used to insert wallet list of cryptocurrencies<br>
     * @param walletList: list of cryptocurrencies
     * @implSpec is useful for Android auto traders
     * **/
    public void insertWalletList(JSONArray walletList) throws Exception {
        serverRequest.sendTokenRequest(new JSONObject().put(CRYPTOCURRENCY_KEY, walletList), INSERT_WALLET_LIST_OPE);
        response = serverRequest.readResponse();
        if(response != null){
            switch (response.getInt(STATUS_CODE)){
                case SUCCESSFUL_RESPONSE:
                    printOperationSuccess(INSERT_WALLET_LIST_OPE);
                    break;
                case GENERIC_ERROR_RESPONSE:
                    printOperationStatus("[" + INSERT_WALLET_LIST_OPE + "] Insert a valid checking list",
                            false);
                    break;
                default: printOperationFailed(INSERT_WALLET_LIST_OPE);
            }
        }else
            printOperationFailed(INSERT_WALLET_LIST_OPE);
    }

    /**
     * This method is used to print message about an operation made<br>
     * @param msg: message to print out
     * @param greenPrint: flag to print green or red
     * **/
    protected void printOperationStatus(String msg, boolean greenPrint) {
        if (printRoutineMessages) {
            if (greenPrint) {
                printGreen(getDate(currentTimeMillis()) + " -> " + msg);
            } else
                printRed(getDate(currentTimeMillis()) + " -> " + msg);
        }
    }

    /**
     * This method is used to print message about a failed operation<br>
     * @param ope: message to print out
     * **/
    protected void printOperationFailed(String ope) {
        if (printRoutineMessages)
            printRed(getDate(currentTimeMillis()) + " -> [" + ope + "] Operation failed");
    }

    /**
     * This method is used to print message about a successful operation <br>
     * @param ope: message to print out
     * **/
    protected void printOperationSuccess(String ope) {
        if (printRoutineMessages)
            printGreen(getDate(currentTimeMillis()) + " -> [" + ope + "] Operation ended successfully");
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

    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public String toString() {
        return "AndroidWorkflow{" +
                "serverRequest=" + serverRequest +
                ", trader=" + trader +
                ", credentials=" + credentials +
                ", printRoutineMessages=" + printRoutineMessages +
                ", workflowStarted=" + workflowStarted +
                '}';
    }

    /**
     * The {@code Credentials} class is object for Tecknobit's account credentials
     * @implNote it not saves your exchange keys
     * Is useful for Android's type traders.
     * @author Tecknobit N7ghtm4r3
     * **/

    public static final class Credentials{

        /**
         * {@code alreadyInstantiated} flag to lock multiple instantiations of {@link Credentials} object
         * **/
        private static boolean alreadyInstantiated = false;

        /**
         * {@code MAX_TOKEN_LENGTH} max number for password length
         * **/
        public static final int MAX_TOKEN_LENGTH = 32;

        /**
         * {@code MIN_TOKEN_LENGTH} min number for password length
         * **/
        public static final int MIN_TOKEN_LENGTH = 8;

        /**
         * {@code serverRequest} instance to make server request for Android's bots
         * **/
        private ServerRequest serverRequest;

        /**
         * {@code authToken} is instance that memorizes identifier of server trader to log in and requests operations
         * **/
        private final String authToken;

        /**
         * {@code token} is instance that memorizes identifier of user to log in and requests operations
         * **/
        private final String token;

        /**
         * {@code email} is instance that memorizes email of user
         * **/
        private String email;

        /**
         * {@code password} is instance that memorizes password of user
         * **/
        private String password;

        /**
         * {@code ivSpec} is instance initialization vector used in server requests
         * **/
        private final String ivSpec;

        /**
         * {@code secretKey} is instance secret key used in server requests
         * **/
        private final String secretKey;

        /**
         * {@code botDetails} is instance helpful to manage bot details
         **/
        private BotDetails botDetails;

        /** Constructor to init {@link Credentials}
         * @param email: is instance that memorizes email of user
         * @param password: is instance that memorizes password of user
         * @implNote this constructor must call to register a new account
         * **/
        public Credentials(String email, String password) {
            if (!alreadyInstantiated)
                alreadyInstantiated = true;
            else
                exitWithError();
            if (!getInstance().isValid(email))
                throw new IllegalArgumentException("Email must be a valid email");
            else
                this.email = email;
            if (wrongPasswordValidity(password))
                throw new IllegalArgumentException("Password must be 8 - 32 characters length");
            else
                this.password = password;
            authToken = null;
            token = null;
            ivSpec = null;
            secretKey = null;
        }

        /**
         * Constructor to init {@link Credentials}
         *
         * @param authToken: is instance that memorizes identifier of server trader to log in and requests operations
         * @param email:     is instance that memorizes email of user
         * @param password:  is instance that memorizes password of user
         * @param token:     instance that memorizes identifier of user to log in and requests operations
         * @param ivSpec:    instance initialization vector used in server requests
         * @param secretKey: is instance secret key used in server requests
         * @implNote this constructor must call to log in
         **/
        public Credentials(String authToken, String email, String password, String token, String ivSpec, String secretKey) {
            if (!alreadyInstantiated)
                alreadyInstantiated = true;
            else
                exitWithError();
            this.authToken = authToken;
            this.email = email;
            this.password = password;
            this.token = token;
            this.ivSpec = ivSpec;
            this.secretKey = secretKey;
        }

        /**
         * This method is used to register a new Tecknobit's account <br>
         * Any params required
         **/
        public void sendRegistrationRequest(String host, int port) throws Exception {
            if (botDetails != null && token == null) {
                serverRequest = getPublicRequest(host, port);
                assert serverRequest != null;
                serverRequest.sendRequest(new JSONObject().put(EMAIL_KEY, email).put(PASSWORD_KEY, password)
                        .put(BOT_STATUS_KEY, botDetails.getBotStatus())
                        .put(REFRESH_TIME_KEY, botDetails.getRefreshTime())
                        .put(BOT_PLATFORM_KEY, botDetails.getBotPlatform())
                        .put(LAST_BOT_ACTIVITY_KEY, botDetails.getLastBotActivity())
                        .put(RUNNING_FROM_DATE_KEY, botDetails.getRunningFromDate())
                        .put(BOT_TYPE_KEY, botDetails.getBotType()), REGISTRATION_OPE);
                response = serverRequest.readResponse();
                if (response != null) {
                    switch (response.getInt(STATUS_CODE)) {
                        case SUCCESSFUL_RESPONSE:
                            throw new SaveData(new JSONObject().put(TOKEN_KEY, response.getString(TOKEN_KEY))
                                    .put(AUTH_TOKEN_KEY, response.getString(AUTH_TOKEN_KEY))
                                    .put(IV_SPEC_KEY, response.getString(IV_SPEC_KEY))
                                    .put(SECRET_KEY, response.getString(SECRET_KEY))
                                    .put(EMAIL_KEY, email)
                                    .put(PASSWORD_KEY, password));
                        case GENERIC_ERROR_RESPONSE:
                            throw new IllegalAccessException("Email not available");
                        default:
                            throw new IllegalAccessException("Operation failed");
                    }
                }else
                    throw new IllegalStateException(ANSI_RED + "Service is not available for serve your request, wait" + ANSI_RESET);
            }
        }

        /**
         * This method is used to log in a Tecknobit's account <br>
         * Any params required
         **/
        public void sendLoginRequest(String baseCurrency, String host, int port, ArrayList<String> quoteCurrencies) throws Exception {
            serverRequest = getPublicRequest(host, port);
            assert serverRequest != null;
            serverRequest.sendRequest(new JSONObject().put(EMAIL_KEY, email).put(PASSWORD_KEY, password)
                    .put(AUTH_TOKEN_KEY, authToken)
                    .put(BOT_STATUS_KEY, botDetails.getBotStatus())
                    .put(REFRESH_TIME_KEY, botDetails.getRefreshTime())
                    .put(BOT_PLATFORM_KEY, botDetails.getBotPlatform())
                    .put(LAST_BOT_ACTIVITY_KEY, botDetails.getLastBotActivity())
                    .put(RUNNING_FROM_DATE_KEY, botDetails.getRunningFromDate())
                    .put(BOT_TYPE_KEY, botDetails.getBotType())
                    .put(BASE_CURRENCY_KEY, baseCurrency)
                    .put(QUOTES_KEY, new JSONArray(quoteCurrencies)), LOGIN_OPE);
            response = serverRequest.readResponse();
            if(response != null) {
                switch (response.getInt(STATUS_CODE)){
                    case SUCCESSFUL_RESPONSE:
                        if(!token.equals(response.getString(TOKEN_KEY)) || !ivSpec.equals(response.getString(IV_SPEC_KEY))
                                || !secretKey.equals(response.getString(SECRET_KEY))) {
                            throw new IllegalAccessException("Wrong credentials inserted");
                        }
                        break;
                    case GENERIC_ERROR_RESPONSE: throw new IllegalAccessException("Wrong credentials inserted");
                    default: throw new IllegalAccessException("Operation failed");
                }
            }else
                throw new IllegalStateException(SERVICE_UNAVAILABLE);
        }

        /**
         * This method is used to exit when a multiple instantiation of {@link Credentials} object is made <br>
         * Any params required
         * **/
        private void exitWithError() {
            out.println(ANSI_RED + "Credentials object is already instantiated you cannot have multiple Credentials objects in same session"
            + ANSI_RESET);
            exit(1);
        }

        /**
         * This method is used to check validity of password inserted
         * @param password: password of user to check
         * @return is password inserted is valid
         * **/
        private boolean wrongPasswordValidity(String password){
            if(password == null)
                return true;
            int length = password.length();
            return length < MIN_TOKEN_LENGTH || length > MAX_TOKEN_LENGTH;
        }

        public ServerRequest getServerRequest() {
            return serverRequest;
        }

        public String getAuthToken() {
            return authToken;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            if (!getInstance().isValid(email))
                throw new IllegalArgumentException("Email must be a valid email");
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            if (wrongPasswordValidity(password))
                throw new IllegalArgumentException("Password must be 8 - 32 characters length");
            this.password = password;
        }

        public String getToken() {
            return token;
        }

        public String getIvSpec() {
            return ivSpec;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setBotDetails(BotDetails botDetails) {
            this.botDetails = botDetails;
        }

        @Override
        public String toString() {
            return "Credentials{" +
                    "serverRequest=" + serverRequest +
                    ", authToken='" + authToken + '\'' +
                    ", token='" + token + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", ivSpec='" + ivSpec + '\'' +
                    ", secretKey='" + secretKey + '\'' +
                    ", botDetails=" + botDetails +
                    '}';
        }

    }

}
