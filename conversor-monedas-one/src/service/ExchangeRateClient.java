package service;

import java.math.BigDecimal;

public interface ExchangeRateClient {
    BigDecimal getRate(String baseCurrency, String targetCurrency);
}


