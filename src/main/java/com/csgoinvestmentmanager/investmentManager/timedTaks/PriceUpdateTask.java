package com.csgoinvestmentmanager.investmentManager.timedTaks;

import com.csgoinvestmentmanager.investmentManager.service.Implementation.CSGOItemServiceImplementation;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.UserInventoryValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceUpdateTask {

    private final CSGOItemServiceImplementation csgoItemService;
    private final UserInventoryValueService userInventoryValueService;

    // Initial delay: 10 min, then every 4 hours
    @Scheduled(initialDelay = 10 * 60 * 1000, fixedDelay = 4 * 60 * 60 * 1000)
    public void run() {
        log.info("refreshing csgo prices at: {}", LocalDateTime.now());
        csgoItemService.refreshAllPrices();
        userInventoryValueService.calculateUserInventoryValues();
    }
}
