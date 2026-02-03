package domain;

import java.math.BigDecimal;
import java.time.Instant;

public class ConversionResult {
    private final String from;
    private final String to;
    private final BigDecimal amount;
    private final BigDecimal rate;
    private final BigDecimal converted;
    private final Instant timestamp;

    public ConversionResult(String from, String to, BigDecimal amount,
                            BigDecimal rate, BigDecimal converted, Instant timestamp) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.rate = rate;
        this.converted = converted;
        this.timestamp = timestamp;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getRate() { return rate; }
    public BigDecimal getConverted() { return converted; }
    public Instant getTimestamp() { return timestamp; }
}

