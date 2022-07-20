package com.csgoinvestmentmanager.investmentManager.timedTaks;

import com.csgoinvestmentmanager.investmentManager.randomFuncitions.ValveApi;
import com.csgoinvestmentmanager.investmentManager.repository.CSGOItemRepository;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.CSGOItemServiceImplementation;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.UserInventoryValueService;
import com.csgoinvestmentmanager.investmentManager.service.intefaces.CSGOItemService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.TimerTask;


@RequiredArgsConstructor
@Slf4j
@Component
public class PriceUpdateTask extends TimerTask {

    private final CSGOItemServiceImplementation csgoItemService;
    private final UserInventoryValueService userInventoryValueService;



    @Override
    public void run() {
        log.info("refreshing csgo prices at: " + LocalDateTime.now());

        csgoItemService.refreshAllPrices();

        userInventoryValueService.calculateUserInventoryValues();

        System.out.println("its running wohoo");
    }
}
