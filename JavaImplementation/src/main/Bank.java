package main;

import main.model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bank {
    public static void main(String[] args) {
        // Create an account
        Account account = new SavingsAccount("12345", 1000);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Simulate multiple transactions.
        executor.submit(new Transaction(account, 500, true));
        executor.submit(new Transaction(account, 200, false));
        executor.submit(new Transaction(account, 1000, true));
        executor.submit(new Transaction(account, 300, false));

        // Shutting down the executor.
        executor.shutdown();

        // apply interest after transactions.
        account.applyInterest();

        // printing the final balance.
        System.out.println("Final balance: " + account.getBalance());
    }
}