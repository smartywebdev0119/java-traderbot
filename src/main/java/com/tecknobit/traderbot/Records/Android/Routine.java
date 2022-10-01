package com.tecknobit.traderbot.Records.Android;

import com.tecknobit.traderbot.Routines.Interfaces.TraderBotConstants;

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
     * {@code ROUTINES_KEY} is instance that memorizes routines key
     *
     * @see TraderBotConstants
     * @deprecated use {@link TraderBotConstants} instead to use constants
     **/
    @Deprecated
    public static final String ROUTINES_KEY = "routines";

    /**
     * {@code ROUTINE_KEY} is instance that memorizes routine key
     *
     * @see TraderBotConstants
     * @deprecated use {@link TraderBotConstants} instead to use constants
     **/
    @Deprecated
    public static final String ROUTINE_KEY = "routine";

    /**
     * {@code ROUTINE_EXTRA_VALUE_KEY} is instance that memorizes extra value of routine key
     *
     * @see TraderBotConstants
     * @deprecated use {@link TraderBotConstants} instead to use constants
     **/
    @Deprecated
    public static final String ROUTINE_EXTRA_VALUE_KEY = "extra_value";

    /**
     * {@code routine} is instance that memorizes routine that trader has to do
     * **/
    private final String routine;

    /**
     * {@code extraValue} is instance that memorizes extra value that trader has to compute
     * **/
    private final String extraValue;

    /** Constructor to init {@link Routine}
     * @param routine: routine that trader has to do
     * @param extraValue: extra value that trader has to compute
     * **/
    public Routine(String routine, String extraValue) {
        this.routine = routine;
        this.extraValue = extraValue;
    }

    public String getRoutine() {
        return routine;
    }

    public String getExtraValue() {
        return extraValue;
    }

    @Override
    public String toString() {
        return "Routine{" +
                "routine='" + routine + '\'' +
                ", extraValue='" + extraValue + '\'' +
                '}';
    }

}
