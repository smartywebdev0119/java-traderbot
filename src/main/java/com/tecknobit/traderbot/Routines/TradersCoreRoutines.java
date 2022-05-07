package com.tecknobit.traderbot.Routines;

import java.util.ArrayList;

public abstract class TradersCoreRoutines {

    public abstract double getWalletBalance();

    public abstract <T> ArrayList<T> getAssetsList() throws Exception;

    public abstract <T> ArrayList<T> getTransactionsList();

    public abstract <T> T getAccountDetails();

    public abstract String getErrorResponse();

}
