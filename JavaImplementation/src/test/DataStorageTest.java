import main.data.DataStorage;
import main.model.SavingsAccount;
import main.model.Account;
import org.junit.juniper.api.Test;
import static org.junit.juniper.api.Assertions.*;

public class DataStorageTest {
    @Test
    public void testSaveAccount() {
        DataStorage = storage = new DataStorage();
        storage.saveAccount(new SavingsAccount("ACC001", 1000.0, "Savings"));

    }
}
