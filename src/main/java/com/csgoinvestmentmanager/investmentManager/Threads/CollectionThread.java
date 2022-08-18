package com.csgoinvestmentmanager.investmentManager.Threads;

import com.csgoinvestmentmanager.investmentManager.timedTaks.PriceUpdateTask;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Timer;

@AllArgsConstructor
@Component
public class CollectionThread implements Runnable {

    private final PriceUpdateTask priceUpdateTask;

    @Override
    public void run() {

        System.out.println("running running as the yeah smehting");

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(priceUpdateTask,0,4*60*60*1000);
    }
}
