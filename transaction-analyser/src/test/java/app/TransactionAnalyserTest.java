package app;

import domain.QueryRequest;
import domain.QueryResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionAnalyserTest {

    @Test
    public void givenAccountIdNotExist_whenQueryTransaction_thenCheckFail() {
        TransactionAnalyser analyser = new TransactionAnalyser();
        String testAccountId = "NOT-A-VALID-ACCOUNT";
        assertFalse(analyser.isValidAccountId(testAccountId));
    }

    @Test
    public void givenValidAccountId_whenQueryTransaction_thenPass() {
        TransactionAnalyser analyser = new TransactionAnalyser();
        String testAccountId = "ACC334455";
        assertTrue(analyser.isValidAccountId(testAccountId));
    }

    @Test
    public void givenValidTransactionDateRange_whenQueryTransaction_thenCheckPass() {
        TransactionAnalyser analyser = new TransactionAnalyser();
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusDays(2);

        assertTrue(analyser.isValidTransactionDateRange(from, to));
    }

    @Test
    public void givenInvalidTransactionDateRange_whenQueryTransaction_thenCheckFail() {
        TransactionAnalyser analyser = new TransactionAnalyser();
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusDays(2);

        assertFalse(analyser.isValidTransactionDateRange(to, from));
    }

    @Test
    public void givenInvalidTransactionDateTimeRange_whenQueryTransaction_thenCheckFail() {
        TransactionAnalyser analyser = new TransactionAnalyser();
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plus(5, ChronoUnit.MINUTES);

        assertFalse(analyser.isValidTransactionDateRange(to, from));
    }

    @Test
    public void givenQueryForPaymentTransaction_whenQueryTransaction_thenReturnBalanceAndTransactionCount() {
        TransactionAnalyser analyser = new TransactionAnalyser();
        QueryRequest request = QueryRequest.builder()
                .accountId("ACC998877")
                .from(LocalDateTime.now())
                .to(LocalDateTime.parse("20/10/2018 18:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .build();
        QueryResponse response = analyser.getQueryResponse(request);
        assertTrue(Objects.nonNull(response));
        assertEquals(BigDecimal.valueOf(-5.00).doubleValue(), response.getBalance().doubleValue());
        assertEquals(1, response.getTransactionCount());
    }

    @Test
    public void givenQueryForAccountWithoutTransaction_whenQueryTransaction_thenReturnZeroTransactionCount() {
        TransactionAnalyser analyser = new TransactionAnalyser();
        QueryRequest request = QueryRequest.builder()
                .accountId("ACC998877")
                .from(LocalDateTime.now())
                .to(LocalDateTime.now())
                .build();
        QueryResponse response = analyser.getQueryResponse(request);
        assertTrue(Objects.nonNull(response));
        assertEquals(0, response.getTransactionCount());
    }

    @Test
    public void givenQueryForAccountWithReversalTransaction_whenQueryTransaction_thenReturnBalanceAndTransactionCount() {
        TransactionAnalyser analyser = new TransactionAnalyser();
        QueryRequest request = QueryRequest.builder()
                .accountId("ACC334455")
                .from(LocalDateTime.parse("20/10/2018 12:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .to(LocalDateTime.parse("20/10/2018 19:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .build();
        QueryResponse response = analyser.getQueryResponse(request);
        assertTrue(Objects.nonNull(response));
        assertEquals(BigDecimal.valueOf(-25.00).doubleValue(), response.getBalance().doubleValue());
        assertEquals(1, response.getTransactionCount());
    }
}
