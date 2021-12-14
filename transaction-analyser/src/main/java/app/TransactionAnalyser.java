package app;

import domain.QueryRequest;
import domain.QueryResponse;
import domain.TransactionRecord;
import util.TransactionLoader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionAnalyser {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final String REVERSAL = "REVERSAL";

    public static void main(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Missing parameter passed to the system");
        }

        var analyser = new TransactionAnalyser();
        String accountId = args[0];
        if (!analyser.isValidAccountId(accountId)) {
            throw new IllegalArgumentException("Invalid account ID");
        }
        String from = args[1];
        String to = args[2];
        var request = QueryRequest.builder()
                .accountId(accountId)
                .from(analyser.convert(from))
                .to(analyser.convert(to))
                .build();

        var response = analyser.getQueryResponse(request);
        System.out.println("Input arguments:");
        System.out.printf("accountId: %s\nfrom: %s\nto: %s", request.getAccountId(), from, to);
        System.out.println();
        System.out.println("Output:");
        System.out.printf("Relative balance for the period is: %s\nNumber of transactions included is: %s\n", response.getBalance().toString(), response.getTransactionCount());
    }

    private LocalDateTime convert(String date) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(date, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date");
        }

        return localDateTime;
    }

    public boolean isValidAccountId(String accountId) {
        var loader = new TransactionLoader();
        var transactions = loader.getTransactions(accountId);

        return Objects.nonNull(transactions) && !transactions.isEmpty();
    }

    public boolean isValidTransactionDateRange(LocalDateTime from, LocalDateTime to) {
        return from.isBefore(to);
    }

    public QueryResponse getQueryResponse(QueryRequest request) {
        boolean datesValid = isValidTransactionDateRange(request.getFrom(), request.getTo());
        if (!datesValid) {
            throw new IllegalArgumentException("Date from should be before Date to");
        }

        var loader = new TransactionLoader();
        var transactions = loader.getTransactions();
        var filteredData = transactions.stream().filter(transactionRecord ->
                transactionRecord.getFromAccountId().trim().equals(request.getAccountId()) &&
                        ((transactionRecord.getCreateAt().isAfter(request.getFrom()) && transactionRecord.getCreateAt().isBefore(request.getTo())) ||
                                (transactionRecord.getCreateAt().isEqual(request.getFrom()) || transactionRecord.getCreateAt().isEqual(request.getTo())) ||
                                transactionRecord.getTransactionType().trim().equals(REVERSAL))

        ).collect(Collectors.toList());
        BigDecimal balance = BigDecimal.ZERO;
        int transactionCounter = 0;
        for (TransactionRecord filteredTransaction : filteredData) {
            if (filteredTransaction.getTransactionType().trim().equals("REVERSAL")) {
                balance = balance.add(filteredTransaction.getAmount());
                transactionCounter--;
            } else {
                balance = balance.subtract(filteredTransaction.getAmount());
                transactionCounter++;
            }
        }

        return QueryResponse.builder().balance(balance).transactionCount(transactionCounter).build();
    }
}
