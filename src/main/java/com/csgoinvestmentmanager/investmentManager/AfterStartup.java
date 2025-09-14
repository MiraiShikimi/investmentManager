package com.csgoinvestmentmanager.investmentManager;

import com.csgoinvestmentmanager.investmentManager.Threads.CollectionThread;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AfterStartup {

    private final CollectionThread collectionThread;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {

        System.out.println("\n \n \n hello world, I have just started up \n \n \n  \n \n \n ");

        //Thread thread = new Thread(collectionThread);
        //thread.start();
    }

}
