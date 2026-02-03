package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.ApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpExchangeRateClient implements ExchangeRateClient {

    private final Gson gson = new Gson();
    private final String endpointTemplate;
    private final String apiKey;

    // endpointTemplate ejemplo:
    // https://v6.exchangerate-api.com/v6/%s/latest/%s
    public HttpExchangeRateClient(String endpointTemplate, String apiKey) {
        this.endpointTemplate = endpointTemplate;
        this.apiKey = apiKey;
    }

    @Override
    public BigDecimal getRate(String baseCurrency, String targetCurrency) {
        try {
            String urlStr = String.format(endpointTemplate, apiKey, baseCurrency.toUpperCase());
            URL url = new URL(urlStr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);

            int status = con.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new ApiException("Error HTTP: " + status);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            con.disconnect();

            JsonObject root = gson.fromJson(response.toString(), JsonObject.class);

            if (root.has("result") && !"success".equalsIgnoreCase(root.get("result").getAsString())) {
                throw new ApiException("Error de API: " + root);
            }

            JsonObject rates = root.getAsJsonObject("conversion_rates");
            if (rates == null || !rates.has(targetCurrency.toUpperCase())) {
                throw new ApiException("No existe tasa para " + targetCurrency);
            }

            return rates.get(targetCurrency.toUpperCase()).getAsBigDecimal();

        } catch (Exception e) {
            throw new ApiException("Error consultando API", e);
        }
    }
}

