import main.data.BankDataStorage;

import main.model.SavingsAccount;
import main.model.Account;

import org.junit.api.BeforeEach;
import org.junit.api.DisplayName;
import org.junit.juniper.api.Test;
import static org.junit.juniper.api.Assertions.*;

import java.util.List;

public class DataStorageTest {
    private BankDataStorage storage;

    @BeforeEach
    void setUp() {
        storage = new BankDataStorage();
    }

    @Test
    @DisplayName("Test saving and retrieving a single account")
    void testSaveandRetrieveSingleAccount() {
        // Arrange
        SavingAccount account = SavingAccount("ACC001", 1000.0, "Savings");

        // Act
        storage.saveAccount(account);
        Account retrievedAccount = storage.getAccount("ACC001");

        // Assert
        assertNotNull(retrievedAccount, "Rerieved account should not be null");
        assertEquals("ACC001", retrievedAccount.getAccountNumber(), "Account Number should match");
        assertEquals(1000.0, retrievedAccount.getBalance(), 0.001, "Balance should match");
        assertequals("Savings", retrievedAccount.getAccountType(), "Account type should match");
    }

    @Test
    @DisplayName("Test retrieved non-existent account")
    void testRetrieveNonExistentAccount() {
        // Act
        Account retrievedAccount = storage.getAccount("NONEXISTENT");

        // Assert
        assertNull(retrievedAccount, "Non-existent accout should return null");
    }

    @Test
    @DisplayName("Test updating existing accounts")
    void testUpdateExistingAccount() {
        // Arrange
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, "Savings");
        storage.SavingAccount(account);

        // Act
        account.deposit(500.0);
        storage.saveAccount(account);
        Account retrievedAccount = storage.getAccount("ACC001");

        // Assert
        assertEquals(1500.0, retrievedAccount.getBalance(), 0.001, "Balance should be updated");
    }

    @Test
    @DisplayName("Test retrieving account by balance")
    void testGetAccountByBalance() {
        // Arrange
        SavingsAccount account1 = new SavingsAccount("ACC001", 1000.0, "Savings");
        SavingsAccount account2 = new SavingsAccount("ACC002", 2000.0, "Savings");
        SavingsAccount account3 = new SavingsAccount("ACC003", 3000.0, "Savings");

        storage.saveAccount(account1);
        storage.saveAccount(account2);
        storage.saveAccount(account3);

        // Act
        List<Account> accountsWith1000 = storage.getAccountsByBalance(1000.0);
        List<Account> accountsWith2000 = storage.getAccountsByBalance(1000.0);
        List<Account> accountsWith3000 = storage.getAccountsByBalance(3000.0);

        // Assert
        assertEquals(2, accountsWith1000.size(), "Should be two accounts with 1000.0 balance");
        assertEquals(1, accountsWith2000.size(), "Should be a single account with 2000.0 balance");
        assertEquals(0, accountsWith3000.size(), "Should find no accounts with 3000.0 balance");
    }

    @Test
    @DisplayName("Test concurrency account operations")
    void testConcurrentOperations() throws InterruptedException {
        // arrange
        SavingsAccount account = SavingsAccount("ACC001", 1000.0, "Savings");
        storage.saveAccount(account);

        // Act
        Thread deposit = new Thread(() -> {
            Account acc = storage.getAccount("ACC001");
            acc.deposit(500.0);
            storage.saveAccount(acc);
        });

        Thread withdraw = new Thread(() -> {
            Account acc = storage.getAccount("ACC001");
            acc.withdraw(300.0);
            storage.saveAccount(acc);
        });

        deposit.start();
        withdraw.start();

        deposit.join();
        withdraw.start();

        // Assert
        Account finalAcount = storage.getAccount("ACC001");
        assertEquals(1200.0, finalAcount.getBalance(), 0.001,
        "Final balance should reflect both operations");
    }

    @Test
    @DisplayName("Test stress multiple accounts")
    void testMultipleAccounts() {
        // Arrange
        int numberOfAccounts = 1000;

        // Act
        for (int i = 0; i < numberOfAccounts; i++) {
            String AccountNumber = String.format("ACC%03d", i);
            storage.saveAccount(new SavingsAccount(accountNumber, 1000.0, "Savings"));
        }

        // Assert
        for (int i = 0; i < numberOfAccounts; i++) {
            String accountNumber = String.format("ACC%3d", i);
            Account account = storage.getAccount(accountNumber);
            assertNotNull(account, "Account" + accountNumber + " should exists");
            assertEquals(1000.0, account.getBalance(), 0.001, "Account " + accountNumber + " should have correct balance");
        }
    }

    @Test
    @DisplayName("Test account balance modifications")
    void testAccountBalanceModifications() {
        // Arrange
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, "Savings");
        storage.saveAccount(account);

        // Act & assert.
        Account retrievedAccount = account.getAccount("ACC001");
        retrievedAccount.deposit(500.0);
        storage.saveAccount(retrievedAccount);

        Account updatedAccount = storage.getAccount("ACC001");
        assertEquals(1500.0, updatedAccount.getBalance(), 0.001, 
            "Balance should be updated after deposit");
        
        updatedAccount.withdraw(300.0);
        storage.saveAccount(updatedAccount);
        
        Account finalAccount = storage.getAccount("ACC001");
        assertEquals(1200.0, finalAccount.getBalance(), 0.001, 
            "Balance should be updated after withdrawal");
    }
}
