package domain;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionRecord {

    @CsvBindByPosition(position = 0)
    private String transactionId;

    @CsvBindByPosition(position = 1)
    private String fromAccountId;

    @CsvBindByPosition(position = 2)
    private String toAccountId;

    @CsvDate(value = " dd/MM/yyyy HH:mm:ss")
    @CsvBindByPosition(position = 3)
    private LocalDateTime createAt;

    @CsvBindByPosition(position = 4)
    private BigDecimal amount;

    @CsvBindByPosition(position = 5)
    private String transactionType;

    @CsvBindByPosition(position = 6)
    private String relatedTransaction;
}
