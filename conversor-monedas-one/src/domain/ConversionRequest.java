package domain;

import java.math.BigDecimal;
import java.util.Objects;

public class ConversionRequest {
    private final String from;
    private final String to;
    private final BigDecimal amount;

    public ConversionRequest(String from, String to, BigDecimal amount) {
        this.from = Objects.requireNonNull(from).toUpperCase();
        this.to = Objects.requireNonNull(to).toUpperCase();
        this.amount = Objects.requireNonNull(amount);
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public BigDecimal getAmount() { return amount; }
}
