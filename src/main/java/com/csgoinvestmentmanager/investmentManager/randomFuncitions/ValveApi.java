package com.csgoinvestmentmanager.investmentManager.randomFuncitions;

import com.csgoinvestmentmanager.investmentManager.Exeptions.Http429Expection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
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
            log.warn("Rate limited by Steam API for item '{}', sleeping {}s before retry", itemName, RATE_LIMIT_SLEEP_MS / 1000);
            try { Thread.sleep(RATE_LIMIT_SLEEP_MS); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            try {
                return fetchPrice(itemName, currentPrice);
            } catch (Http429Expection e2) {
                log.error("Still rate limited after retry for item '{}', keeping existing price {}", itemName, currentPrice);
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
                log.warn("Unexpected response code {} for item '{}', keeping existing price", responseCode, itemName);
                return currentPrice;
            }

            String body;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                body = reader.readLine();
            }

            JsonNode data = objectMapper.readTree(body);
            String raw = data.get("lowest_price").asText().substring(0, 4).replace(',', '.').replaceAll("-", "0");
            BigDecimal newPrice = new BigDecimal(raw);
            log.debug("Updated price for '{}': {} -> {}", itemName, currentPrice, newPrice);
            return newPrice;

        } catch (Http429Expection e) {
            throw e;
        } catch (IOException e) {
            log.error("IO error fetching price for item '{}': {}", itemName, e.getMessage());
            return currentPrice;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return currentPrice;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
