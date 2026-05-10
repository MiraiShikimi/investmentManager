package com.csgoinvestmentmanager.investmentManager.randomFuncitions;

import com.csgoinvestmentmanager.investmentManager.Exeptions.Http429Expection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ValveApi {

    private static final int CONNECT_TIMEOUT_MS  = 10_000;
    private static final int READ_TIMEOUT_MS     = 10_000;
    private static final int RATE_LIMIT_SLEEP_MS = 65_000;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Gets the lowest selling price from the Steam Community market.
     * On HTTP 429 the call sleeps 65 s then retries once.
     */
    public BigDecimal getItemPriceFromValveApi(String itemName, BigDecimal currentPrice) {
        try {
            return fetchPrice(itemName, currentPrice);
        } catch (Http429Expection e) {
            // First 429: already slept inside fetchPrice; sleep again before the retry
            // so the retry doesn't immediately hit the rate limit a second time.
            try { Thread.sleep(RATE_LIMIT_SLEEP_MS); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            try {
                return fetchPrice(itemName, currentPrice);
            } catch (Http429Expection e2) {
                // Still rate-limited after retry — skip this item, keep existing price
                return currentPrice;
            }
        }
    }

    private BigDecimal fetchPrice(String itemName, BigDecimal currentPrice) throws Http429Expection {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://steamcommunity.com/market/priceoverview/?currency=3&country=DE&appid=730&market_hash_name=" + itemName);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);

            int responseCode = conn.getResponseCode();

            if (responseCode == 429) {
                Thread.sleep(RATE_LIMIT_SLEEP_MS);
                throw new Http429Expection("Too many requests");
            }
            if (responseCode != 200) {
                return currentPrice;
            }

            String body;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                body = reader.readLine();
            }

            JsonNode data = objectMapper.readTree(body);
            String raw = data.get("lowest_price").asText().substring(0, 4).replace(',', '.').replaceAll("-", "0");
            return new BigDecimal(raw);

        } catch (Http429Expection e) {
            throw e;
        } catch (IOException e) {
            return currentPrice;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return currentPrice;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
