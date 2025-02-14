package main.model;

public abstract class Account {
    private String accountNumber;
    private double balance;
    private String accountType;

    // Creating the account class.
    public Account (String accountNumber, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    // getters and setters.
    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    // Abstraction: Abstraction methods for account-specific behavior.
    public abstract void applyInterest();

    // Thread safe deposit method.
    public synchronized void deposit(double amount) {
        if(amount > 0) {
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " deposited " + amount + " int o account " + accountNumber);
        } else {
            System.out.println("Insufficient balance or invalid withdrawal amount");
        }
    }

    // Thread safe withdrawal.
    public synchronized void withdrawal(double amount) {
        if (amount > 0 && amount <= balance) {
            System.out.println(Thread.currentThread().getName() + " withdrawal " + amount + " from account " + accountNumber);
        } else {
            System.out.println("Insufficient balance or invalid withdrawal amount.");
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}

