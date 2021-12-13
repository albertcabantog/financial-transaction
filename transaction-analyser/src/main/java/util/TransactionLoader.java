package util;

import com.opencsv.bean.CsvToBeanBuilder;
import domain.TransactionRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionLoader {
    private static final String DEFAULT_FILE_NAME = "transaction_list.csv";

    public List<TransactionRecord> getTransactions() {
        InputStream initialStream = getFileFromResourceAsStream(DEFAULT_FILE_NAME);
        Reader fileReader = new InputStreamReader(initialStream);
        List<TransactionRecord> transactionRecords = new CsvToBeanBuilder<TransactionRecord>(fileReader)
                .withType(TransactionRecord.class)
                .withIgnoreLeadingWhiteSpace(false)
                .build()
                .parse();
        return transactionRecords;
    }

    public List<TransactionRecord> getTransactions(String filterByAccountId) {
        var transactionRecords = getTransactions().stream()
                .filter(transactionRecord ->
                        transactionRecord.getFromAccountId().trim().equals(filterByAccountId))
                .collect(Collectors.toList());
        return transactionRecords;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
