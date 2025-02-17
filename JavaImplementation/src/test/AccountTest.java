package test;
import main.model.SavingsAccount;
import main.model.Account;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    @Test
    public void testDeposit() {
        Account account= new SavingsAccount("1234", 1000);
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    public void testWithdraw() {
        Account account = new SavingsAccount("1234", 1000);
        account.withdraw(200);
        assertEquals(800, account.getBalance());
    }

    @Test
    public void testInsufficientBalance() {
        Account account = new SavingsAccount("1234", 1000);
        account.withdraw(1500);
        // Balance should remain unchanged.
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void testApplyInterest() {
        SavingsAccount account = new SavingsAccount("1234", 1000);
        account.applyInterest();
        assertEquals(1050, account.getBalance());
    }
}
