package com.tecknobit.traderbot.routines.android;

import com.tecknobit.apimanager.apis.encryption.aes.ClientCipher;
import com.tecknobit.traderbot.routines.android.AndroidWorkflow.Credentials;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import static com.tecknobit.apimanager.apis.encryption.aes.ClientCipher.Algorithm.CBC_ALGORITHM;
import static com.tecknobit.traderbot.routines.interfaces.TraderBotConstants.*;

/**
 * The {@code ServerRequest} class is useful to make server request for Android's bots<br>
 * Is useful for Android's type bots.
 *
 * @author Tecknobit N7ghtm4r3
 **/
public class ServerRequest {

    /**
     * {@code HOST} host value
     **/
    public static final String HOST = "217.160.205.94";

    /**
     * {@code PORT} port value
     **/
    public static final int PORT = 7898;

    /**
     * {@code response} object for requests response
     **/
    public static JSONObject response;

    /**
     * {@code clientCipher} object to cipher requests
     **/
    protected final ClientCipher clientCipher;

    /**
     * {@code ciphered} flag to cipher or not requests
     **/
    protected final boolean ciphered;

    /**
     * {@code port} is instance that memorizes host value
     **/
    protected final String host;

    /**
     * {@code port} is instance that memorizes port value
     **/
    protected final int port;

    /**
     * {@code clientCipher} object to write requests
     **/
    protected PrintWriter printWriter;

    /**
     * {@code socket} object to transport request
     **/
    protected volatile Socket socket;

    /**
     * {@code authToken} is instance that memorizes identifier of server trader to log in and requests operations
     **/
    protected String authToken;

    /**
     * {@code token} is instance that memorizes identifier of user to log in and requests operations
     **/
    protected String token;

    /**
     * Constructor to init {@link ServerRequest}
     *
     * @param ivSpec    : initialization vector used in server requests
     * @param secretKey : secret key used in server requests
     * @param authToken : identifier of server trader to log in and requests operations
     * @param token     : identifier of user to log in and requests operations
     * @param host:     host value
     * @param port:     port value
     **/
    public ServerRequest(String ivSpec, String secretKey, String authToken, String token, String host,
                         int port) throws Exception {
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
                messageToSent = clientCipher.encrypt(messageToSent);
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
            String messageToSent = clientCipher.encrypt(message.toString()) + "#" + token;
            printWriter.println(messageToSent);
            printWriter.flush();
        } catch (NullPointerException ignored) {
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

    /**
     * This method is used fetch public keys for public requests operation
     *
     * @param host: host value
     * @param port: port value
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
                    response = clientCipher.decrypt(response);
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

}
