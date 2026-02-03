package service;

import domain.ConversionRequest;
import domain.ConversionResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class CurrencyConverter {

    private final ExchangeRateClient rateClient;

    public CurrencyConverter(ExchangeRateClient rateClient) {
        this.rateClient = rateClient;
    }

    public ConversionResult convert(ConversionRequest req) {
        BigDecimal rate = rateClient.getRate(req.getFrom(), req.getTo());
        BigDecimal converted = req.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP);

        return new ConversionResult(
                req.getFrom(),
                req.getTo(),
                req.getAmount(),
                rate,
                converted,
                Instant.now()
        );
    }
}
