package com.csgoinvestmentmanager.investmentManager.Threads;

import com.csgoinvestmentmanager.investmentManager.timedTaks.PriceUpdateTask;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Timer;

@AllArgsConstructor
@Component
public class CollectionThread implements Runnable {
    private final PriceUpdateTask priceUpdateTask;

    @Override
    public void run() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(priceUpdateTask,0,20*60*1000);
    }
}
