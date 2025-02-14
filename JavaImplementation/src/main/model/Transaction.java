package main.model;

public class Transaction implements Runnable {
    private  Account account;
    private double amount;
    private boolean isDeposit;

    public Transaction(Account account, double amount, boolean isDeposit) {
        this.amount = amount;
        this.account = account;
        this.isDeposit = isDeposit;
    }

    @Override
    public void run() {
        if(isDeposit) {
            account.deposit(amount);
        } else {
            account.withdrawal(amount);
        }
    }
}
