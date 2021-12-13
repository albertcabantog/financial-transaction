package util;

import domain.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionLoaderTest {

    @Test
    public void givenCSV_whenSystemInitialise_thenTransactionListLoaded() {
        TransactionLoader loader = new TransactionLoader();
        List<TransactionRecord> list = loader.getTransactions();
        assertTrue(list != null && list.isEmpty() == false);
    }

}