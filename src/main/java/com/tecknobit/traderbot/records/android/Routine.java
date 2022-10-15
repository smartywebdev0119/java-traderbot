package com.tecknobit.traderbot.records.android;

import org.json.JSONObject;

/**
 * The {@code Routine} class is useful for Android's traders <br>
 * {@code Routine} defines what trader have to change or do when user from
 * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s app make a request
 *
 * @author Tecknobit N7ghtm4r3
 * @implNote this class is developed for Android's traders
 **/
public final class Routine {

    /**
     * {@code routine} is instance that memorizes routine that trader has to do
     * **/
    private final String routine;

    /**
     * {@code extraValue} is instance that memorizes extra value that trader has to compute
     * **/
    private final String extraValue;

    /**
     * Constructor to init {@link Routine}
     *
     * @param routine:    routine that trader has to do
     * @param extraValue: extra value that trader has to compute
     **/
    public Routine(String routine, String extraValue) {
        this.routine = routine;
        this.extraValue = extraValue;
    }

    /**
     * Method to get {@link #routine} instance <br>
     * Any params required
     *
     * @return {@link #routine} instance as {@link String}
     **/
    public String getRoutine() {
        return routine;
    }

    /**
     * Method to get {@link #extraValue} instance <br>
     * Any params required
     *
     * @return {@link #extraValue} instance as {@link String}
     **/
    public String getExtraValue() {
        return extraValue;
    }

    /**
     * Returns a string representation of the object <br>
     * Any params required
     *
     * @return a string representation of the object as {@link String}
     */
    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
