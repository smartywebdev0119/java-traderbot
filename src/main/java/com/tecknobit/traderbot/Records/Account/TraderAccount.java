package com.tecknobit.traderbot.Records.Account;

import com.tecknobit.traderbot.Routines.RoutineMessages;

import java.util.ArrayList;
import java.util.Date;

import static com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines.ASSET_NOT_TRADABLE;
import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

/**
 * The {@code TraderAccount} class is useful to contains reports of trading. <br>
 * Is useful for autonomous traders and Android's traders.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class TraderAccount implements RoutineMessages {

    /**
     * {@code salesAtLoss} is instance that memorize sales at loss for account
     * **/
    private int salesAtLoss;

    /**
     * {@code salesAtGain} is instance that memorize sales at gain for account
     * **/
    private int salesAtGain;

    /**
     * {@code salesAtPair} is instance that memorize sales at pair for account
     * **/
    private int salesAtPair;

    /**
     * {@code activationDate} is instance that memorize activation date for account
     * **/
    private String activationDate;

    /**
     * {@code incomes} is instance that memorize list of incomes from orders
     * **/
    private ArrayList<Double> incomes;

    /**
     * {@code totalIncome} is instance that memorize total income for account
     * **/
    double totalIncome;

    /** Constructor to init {@link TraderAccount}
     * @param salesAtLoss: sales at loss for account
     * @param salesAtGain: sales at gain for account
     * @param salesAtPair: sales at pair for account
     * @param activationDate: date when trader has been activated
     * @param totalIncome: total income gained for account
     * **/
    public TraderAccount(int salesAtLoss, int salesAtGain, int salesAtPair, String activationDate, double totalIncome) {
        this.salesAtLoss = salesAtLoss;
        this.salesAtGain = salesAtGain;
        this.salesAtPair = salesAtPair;
        this.activationDate = activationDate;
        this.totalIncome = totalIncome;
    }

    /** Constructor to init {@link TraderAccount}
     * @param salesAtLoss: sales at loss for account
     * @param salesAtGain: sales at gain for account
     * @param salesAtPair: sales at pair for account
     * @param activationDate: date when trader has been activated
     * @param incomes: list of past incomes
     * **/
    public TraderAccount(int salesAtLoss, int salesAtGain, int salesAtPair, String activationDate,
                         ArrayList<Double> incomes) {
        this.salesAtLoss = salesAtLoss;
        this.salesAtGain = salesAtGain;
        this.salesAtPair = salesAtPair;
        this.activationDate = activationDate;
        this.incomes = incomes;
    }

    /** Constructor to init {@link TraderAccount} <br>
     * Any params required
     * **/
    public TraderAccount() {
        fetchAccountInformation();
    }

    /**
     * This method is used to get sales at loss
     * @return sales at loss
     * **/
    public int getSalesAtLoss() {
        return salesAtLoss;
    }

    /**
     * This method is used to increment sales at loss<br>
     * Any params required
     * **/
    public void addLoss(){
        salesAtLoss++;
    }

    /**
     * This method is used to get sales at gain
     * @return sales at gain
     * **/
    public int getSalesAtGain() {
        return salesAtGain;
    }

    /**
     * This method is used to add increment sales at gain <br>
     * Any params required
     * **/
    public void addGain(){
        salesAtGain++;
    }

    /**
     * This method is used to get sales at pair
     * @return sales at loss
     * **/
    public int getSalesAtPair() {
        return salesAtPair;
    }

    /**
     * This method is used to increment sales at pair <br>
     * Any params required
     * **/
    public void addPair(){
        salesAtPair++;
    }

    /**
     * This method is used to get total sales
     * @return total sales
     * **/
    public int getTotalSales(){
        return salesAtLoss + salesAtGain + salesAtPair;
    }

    /**
     * This method is used to get activation date of account <br>
     * Any params required
     * @return {@link #activationDate} as activation date of account as {@link String}
     * **/
    public String getActivationDate() {
        return activationDate;
    }

    /**
     * This method is used to init account information <br>
     * Any params required
     * **/
    private void fetchAccountInformation(){
        // TODO: 22/05/2022 REQUEST TO GET ACCOUNT INFORMATION
        salesAtGain = 0;
        salesAtLoss = 0;
        salesAtPair = 0;
        activationDate = new Date(System.currentTimeMillis()).toString();
        incomes = new ArrayList<>();
        totalIncome = ASSET_NOT_TRADABLE;
    }

    public ArrayList<Double> getIncomes() {
        return incomes;
    }

    /**
     * This method is used to add increment in {@link #incomes} list
     * @param newIncome: new income to insert
     * **/
    public void addIncome(double newIncome){
        if(newIncome < -100)
            throw new IllegalArgumentException("Income value cannot be less than -100");
        incomes.add(newIncome);
    }

    /**
     * This method is used to print all income from {@link #incomes} list <br>
     * Any params required
     * **/
    public void printIncomes() {
        for (double income : incomes)
            out.print(getIncomeColorLine("## Income: ", income));
    }

    /**
     * This method is used to calculate general profit of account
     * Any params required
     * @return general profit of account
     * **/
    public double computeTotalIncome(){
        if(totalIncome == ASSET_NOT_TRADABLE || incomes != null){
            double totalIncome = 0;
            for (double income : incomes)
                totalIncome += income;
            return tradingTools.roundValue(totalIncome / incomes.size(), 2);
        }
        return totalIncome;
    }

    /**
     * This method is used to calculate general profit of account
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted with decimals digits
     * **/
    public double computeTotalIncome(int decimals){
        return tradingTools.roundValue(computeTotalIncome(), decimals);
    }

    /**
     * This method is used to format like a {@link String} general income from {@link #computeTotalIncome()}
     * Any params required
     * @return general income of account formatted as {@link String} es. +1.674% or -1.756%
     * **/
    public String textualizeTotalIncome(){
        return tradingTools.textualizeAssetPercent(computeTotalIncome());
    }

    /**
     * This method is used to format like a {@link String} general income from {@link #computeTotalIncome()}
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted as {@link String} es. +1.67% or -1.75%
     * **/
    public String textualizeTotalIncome(int decimals){
        return tradingTools.textualizeAssetPercent(computeTotalIncome(decimals));
    }

    /**
     * This method is used to get total income for account <br>
     * Any params required
     * @return general profit of account
     * **/
    public double getTotalIncome() {
        return computeTotalIncome();
    }

    /**
     * This method is used to get total income for account
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted with decimals digits
     * **/
    public double getTotalIncome(int decimals) {
        return computeTotalIncome(2);
    }

    /**
     * This method is used to print details of {@link TraderAccount} object <br>
     * Any params required
     * **/
    @Override
    public void printDetails() {
        out.println("### [ ACCOUNT STATUS ]\n" +
                "## Total: " + getTotalSales() + "\n" +
                "## At loss: " + ANSI_RED + salesAtLoss + ANSI_RESET + "\n" +
                "## At gain: " + ANSI_GREEN + salesAtGain + ANSI_RESET +"\n" +
                "## At pair: " + salesAtPair + "\n" +
                getIncomeColorLine("## Total income: ", computeTotalIncome(2)) +
                "######################");
    }

    /**
     * This method is used to format colored string for income detail line.<br>
     * @param tail: text to indicate the info es. Income:
     * @param income: value of income es. +8%
     * @return percent string colored and formatted as {@link String}
     * **/
    private String getIncomeColorLine(String tail, double income){
        String textIncome = tradingTools.textualizeAssetPercent(income);
        if(textIncome.contains("+"))
            return tail + ANSI_GREEN + textIncome + ANSI_RESET + "\n";
        else if(textIncome.contains("-"))
            return tail + ANSI_RED + textIncome + ANSI_RESET + "\n";
        else
            return tail + textIncome + "\n";
    }

}