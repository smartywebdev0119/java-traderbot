package com.tecknobit.traderbot.exceptions;

import org.json.JSONObject;

/**
 * The {@code SaveData} is an {@link Exception} that is trowed when an Android's trader is started for the first time
 * without have a Tecknobit's account.
 * @author Tecknobit N7ghtm4r3
 * **/
public class SaveData extends Exception{

    /** Constructor to init {@link SaveData}
     * @param credentials: generated for your Tecknobit's account that you must save anywhere you retain safe to salve.
     * **/
    public SaveData(JSONObject credentials) {
        super("Note: is not an error, but is an alert!\n" +
              "Before restart service save credentials data of Tecknobit's account anywhere that you retain safe,\n" +
              "they will be need in login operations here for traders and also for your Android's device.\n" +
              "Next restart you will have to insert these credentials in Credentials object to start \n" +
              "normal flow of trader.\n" +
              "################################### TECKNOBIT'S CREDENTIALS ######################################\n" +
              credentials.toString(4));
    }

}
