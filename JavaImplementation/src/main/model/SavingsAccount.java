package main.model;

public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.05;

    public SavingsAccount(String accountNumber, double balance) {
        super(accountNumber, balance, "savings");
    }

    @Override
    public void applyInterest() {
        double interest = getBalance() + INTEREST_RATE;
        setBalance(getBalance() + interest);
        System.out.println("Interest applied to account " + getAccountNumber() + " New balance: " + getBalance());
    }
}
