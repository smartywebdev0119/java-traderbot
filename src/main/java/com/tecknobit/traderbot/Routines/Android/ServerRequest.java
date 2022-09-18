package com.tecknobit.traderbot.Routines.Android;

import com.tecknobit.aesHelper.ClientCipher;
import com.tecknobit.traderbot.Routines.Android.AndroidWorkflow.Credentials;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import static com.tecknobit.aesHelper.ClientCipher.CBC_ALGORITHM;
import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.ANSI_RED;
import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.ANSI_RESET;

/**
 * The {@code ServerRequest} class is useful to make server request for Android's bots<br>
 * Is useful for Android's type bots.
 *
 * @author Tecknobit N7ghtm4r3
 **/

public class ServerRequest {

    /**
     * {@code SERVICE_UNAVAILABLE} is instance to indicate that service is not available to perform requests
     * **/
    public static final String SERVICE_UNAVAILABLE = ANSI_RED + "Service is not available for serve your request, wait"
            + ANSI_RESET;

    /**
     * {@code GET_KEYS_OPE} request
     * **/
    public static final String GET_KEYS_OPE = "get_keys_ope";

    /**
     * {@code REGISTRATION_OPE} request
     * **/
    public static final String REGISTRATION_OPE = "registration_ope";

    /**
     * {@code LOGIN_OPE} request
     * **/
    public static final String LOGIN_OPE = "login_ope";

    /**
     * {@code GET_ACCOUNT_OPE} request
     **/
    public static final String GET_ACCOUNT_OPE = "get_account_ope";

    /**
     * {@code GET_ROUTINES_OPE} request
     * **/
    public static final String GET_ROUTINES_OPE = "get_routines_ope";

    /**
     * {@code CHANGE_EMAIL_OPE} request
     * **/
    public static final String CHANGE_EMAIL_OPE = "change_email_ope";

    /**
     * {@code CHANGE_PASSWORD_OPE} request
     * **/
    public static final String CHANGE_PASSWORD_OPE = "change_password_ope";

    /**
     * {@code CHANGE_REFRESH_TIME_OPE} request
     **/
    public static final String CHANGE_REFRESH_TIME_OPE = "change_refresh_time_ope";

    /**
     * {@code CHANGE_BOT_STATUS_OPE} request
     * **/
    public static final String CHANGE_BOT_STATUS_OPE = "change_bot_status_ope";

    /**
     * {@code CHANGE_LANGUAGE_OPE} request
     * **/
    public static final String CHANGE_LANGUAGE_OPE = "change_language_ope";

    /**
     * {@code CHANGE_CURRENCY_OPE} request
     * **/
    public static final String CHANGE_CURRENCY_OPE = "change_currency_ope";

    /**
     * {@code CHANGE_DELETION_ACCOUNT_TIME_OPE} request
     * **/
    public static final String CHANGE_DELETION_ACCOUNT_TIME_OPE = "change_deletion_account_time_ope";

    /**
     * {@code INSERT_WALLET_BALANCE_OPE} request
     * **/
    public static final String INSERT_WALLET_BALANCE_OPE = "insert_wallet_balance_ope";

    /**
     * {@code INSERT_CRYPTOCURRENCY_OPE} request
     * **/
    public static final String INSERT_CRYPTOCURRENCY_OPE = "insert_cryptocurrency_ope";

    /**
     * {@code DELETE_CRYPTOCURRENCY_OPE} request
     * **/
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
     * **/
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
     * **/
    public static final int CREDENTIALS_ERROR_RESPONSE = 400;

    /**
     * {@code GENERIC_ERROR_RESPONSE} response
     * **/
    public static final int GENERIC_ERROR_RESPONSE = 300;

    /**
     * {@code SUCCESSFUL_RESPONSE} response
     * **/
    public static final int SUCCESSFUL_RESPONSE = 200;

    /**
     * {@code ERROR_RESPONSE} response
     * **/
    public static final int ERROR_RESPONSE = 500;

    /**
     * {@code STATUS_CODE} key
     * **/
    public static final String STATUS_CODE = "status_code";

    /**
     * {@code TOKEN_KEY} key
     * **/
    public static final String TOKEN_KEY = "token";

    /**
     * {@code EMAIL_KEY} key
     * **/
    public static final String EMAIL_KEY = "email";

    /**
     * {@code NEW_EMAIL_KEY} key
     * **/
    public static final String NEW_EMAIL_KEY = "new_email";

    /**
     * {@code PASSWORD_KEY} key
     * **/
    public static final String PASSWORD_KEY = "password";

    /**
     * {@code NEW_PASSWORD_KEY} key
     * **/
    public static final String NEW_PASSWORD_KEY = "new_password";

    /**
     * {@code IV_SPEC_KEY} key
     * **/
    public static final String IV_SPEC_KEY = "iv_spec";

    /**
     * {@code SECRET_KEY} key
     * **/
    public static final String SECRET_KEY = "secret_key";

    /**
     * {@code DEVICE_TOKEN_KEY} key
     * **/
    public static final String DEVICE_TOKEN_KEY = "device_token";

    /**
     * {@code AUTH_TOKEN_KEY} key
     * **/
    public static final String AUTH_TOKEN_KEY = "auth_token";

    /**
     * {@code LANGUAGE_KEY} key
     * **/
    public static final String LANGUAGE_KEY = "language";

    /**
     * {@code CURRENCY_KEY} key
     * **/
    public static final String CURRENCY_KEY = "currency";

    /**
     * {@code DELETION_TIME_KEY} key
     * **/
    public static final String DELETION_TIME_KEY = "deletion_time";

    /**
     * {@code QUOTE_KEY} key
     * **/
    public static final String QUOTE_KEY = "quote";

    /**
     * {@code BASE_CURRENCY_KEY} key
     * **/
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
     * {@code response} object for requests response
     **/
    public static JSONObject response;

    /**
     * {@code HOST} host value
     **/
    // TODO: 17/09/2022 CHANGE HOST
    public static final String HOST = "localhost";

    /**
     * {@code PORT} port value
     **/
    public static final int PORT = 7898;

    /**
     * {@code clientCipher} object to cipher requests
     **/
    private final ClientCipher clientCipher;

    /**
     * {@code clientCipher} object to write requests
     **/
    private PrintWriter printWriter;

    /**
     * {@code ciphered} flag to cipher or not requests
     * **/
    private final boolean ciphered;

    /**
     * {@code socket} object to transport request
     * **/
    private volatile Socket socket;

    /**
     * {@code authToken} is instance that memorizes identifier of server trader to log in and requests operations
     * **/
    private String authToken;

    /**
     * {@code token} is instance that memorizes identifier of user to log in and requests operations
     * **/
    private String token;

    /**
     * {@code port} is instance that memorizes host value
     * **/
    private final String host;

    /**
     * {@code port} is instance that memorizes port value
     * **/
    private final int port;

    /**
     * Constructor to init {@link ServerRequest}
     *
     * @param ivSpec    : initialization vector used in server requests
     * @param secretKey : secret key used in server requests
     * @param authToken : identifier of server trader to log in and requests operations
     * @param token     : identifier of user to log in and requests operations
     * @param host: host value
     * @param port: port value
     **/
    public ServerRequest(String ivSpec, String secretKey, String authToken, String token, String host, int port) throws Exception {
        this(ivSpec, secretKey, host, port);
        this.authToken = authToken;
        this.token = token;
    }

    /**
     * Constructor to init {@link ServerRequest}
     *
     * @param ivSpec    : initialization vector used in server requests
     * @param secretKey : secret key used in server requests
     * @param host: host value
     * @param port: port value
     **/
    public ServerRequest(String ivSpec, String secretKey, String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        clientCipher = new ClientCipher(ivSpec, secretKey, CBC_ALGORITHM);
        ciphered = true;
        authToken = null;
        token = null;
    }

    /**
     * Constructor to init {@link ServerRequest}
     *
     * @param credentials : instance that contains your Tecknobit's account credentials, not your private exchange keys
     * @param host:       host value
     * @param port:       port value
     **/
    public ServerRequest(Credentials credentials, String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        clientCipher = new ClientCipher(credentials.getIvSpec(), credentials.getSecretKey(), CBC_ALGORITHM);
        ciphered = true;
    }

    /**
     * Constructor to init {@link ServerRequest}
     *
     * @param host: host value
     * @param port: port value
     **/
    public ServerRequest(String host, int port) {
        this.host = host;
        this.port = port;
        clientCipher = null;
        ciphered = false;
    }

    /**
     * This method is used to send request without tokens
     * @param message: message for the request
     * @param operation: operation for the server
     * **/
    public void sendRequest(JSONObject message, String operation) throws Exception {
        try {
            createSocket();
            message.put("ope", operation);
            String messageToSent = message.toString();
            if (ciphered) {
                assert clientCipher != null;
                messageToSent = clientCipher.encryptRequest(messageToSent);
            }
            printWriter.println(messageToSent);
            printWriter.flush();
        }catch (NullPointerException ignored){
        }
    }

    /**
     * This method is used to send request with tokens
     * @param message: message for the request
     * @param operation: operation for the server
     * **/
    public void sendTokenRequest(JSONObject message, String operation) throws Exception {
        try {
            createSocket();
            message.put("ope", operation);
            message.put(AUTH_TOKEN_KEY, authToken);
            assert clientCipher != null;
            String messageToSent = clientCipher.encryptRequest(message.toString()) + "#" + token;
            printWriter.println(messageToSent);
            printWriter.flush();
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * This method is used fetch public keys for public requests operation<br>
     * Any params required
     *
     * @return new request object as {@link ServerRequest}
     **/
    public static ServerRequest getPublicRequest(String host, int port) {
        try {
            ServerRequest serverRequest = new ServerRequest(host, port);
            serverRequest.sendRequest(new JSONObject(), GET_KEYS_OPE);
            response = serverRequest.readResponse();
            return new ServerRequest(response.getString(IV_SPEC_KEY), response.getString(SECRET_KEY), host, port);
        } catch (Exception e) {
            throw new IllegalStateException(SERVICE_UNAVAILABLE);
        }
    }

    /**
     * This method is used to read request <br>
     * Any params required
     *
     * @return response of the server as {@link JSONObject}
     **/
    public JSONObject readResponse() {
        try {
            String response = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            if (ciphered) {
                assert clientCipher != null;
                try{
                    response = clientCipher.decryptResponse(response);
                }catch (BadPaddingException e){
                    response = "{\"status_code\": -1}";
                }
            }
            socket = null;
            return new JSONObject(response);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * This method is used to create or recreate {@link #socket} for requests <br>
     * Any params required
     * **/
    private void createSocket() throws IOException {
        try {
            while (socket != null);
            socket = new Socket(host, port);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        }catch (ConnectException ignored) {
        }
    }

    /**
     * This method is used to send request with tokens
     *
     * @param message:   message for the request
     * @param operation: operation for the server
     **/
    public void sendServerRequest(JSONObject message, String operation) throws Exception {
        message.put(SERVER_REQUEST_KEY, true);
        sendTokenRequest(message, operation);
    }

}
