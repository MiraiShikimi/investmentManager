package com.csgoinvestmentmanager.investmentManager.service.Implementation;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import com.csgoinvestmentmanager.investmentManager.randomFuncitions.ValveApi;
import com.csgoinvestmentmanager.investmentManager.repository.CSGOItemRepository;
import com.csgoinvestmentmanager.investmentManager.service.intefaces.CSGOItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CSGOItemServiceImplementation implements CSGOItemService {
    private final CSGOItemRepository csgoItemRepository;
    private final ValveApi valveApi;


    @Override
    public CSGOItem create(CSGOItem csgoItem) {
        csgoItem.setDisplayName(csgoItem.getHashName()
                .replaceAll("%20"," ")
                .replaceAll("%7C","|")
                .replaceAll("%28","(")
                .replaceAll("%29",")")
                .replaceAll("%E2%98%85","★")
                .replaceAll("%26","&"));
        csgoItem.setLowestPrice(valveApi.getItemPriceFromValveApi(csgoItem.getHashName(),csgoItem.getLowestPrice()));
        log.info("saving new server: {}", csgoItem.getDisplayName());
        return csgoItemRepository.save(csgoItem);
    }

    @Override
    public CSGOItem get(Long id) {
        log.info("Fetching csgo item by id  {}", id);

        return csgoItemRepository.findById(id).orElseThrow(RuntimeException::new);

    }

    @Override
    public Boolean delete(Long id) {
        log.info("deleting csgo item by hash name {}",id);
        csgoItemRepository.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public CSGOItem update(CSGOItem csgoItem) {
        log.info("updating Item");
        csgoItem.setDisplayName(csgoItem.getHashName()
                .replaceAll("%20"," ")
                .replaceAll("%7C","|")
                .replaceAll("%28","(")
                .replaceAll("%29",")")
                .replaceAll("%E2%98%85","★")
                .replaceAll("%26","&"));

        BigDecimal price = valveApi.getItemPriceFromValveApi(csgoItem.getHashName(), csgoItem.getLowestPrice());
        csgoItem.setLowestPrice(price);
        return csgoItemRepository.save(csgoItem);
    }

    @Override
    public Collection<CSGOItem> list(int limit) {
        log.info("fetching csgo items");


        return csgoItemRepository.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public CSGOItem refresh(Long id) {
        log.info("updating Item price");
        CSGOItem tempItem = csgoItemRepository.findById(id).orElseThrow(RuntimeException::new);
        tempItem.setLowestPrice(valveApi.getItemPriceFromValveApi(tempItem.getHashName(), tempItem.getLowestPrice()));
        return csgoItemRepository.save(tempItem);
    }

    @Override
    public CSGOItem refresh(CSGOItem tempItem) {
        log.info("updating Item price");
        tempItem.setLowestPrice(valveApi.getItemPriceFromValveApi(tempItem.getHashName(), tempItem.getLowestPrice()));
        return csgoItemRepository.save(tempItem);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String refreshAllPrices() {
        int page = 0;
        final int pageSize = 50;
        List<CSGOItem> batch;
        do {
            batch = csgoItemRepository.findAll(PageRequest.of(page++, pageSize)).getContent();
            for (CSGOItem item : batch) {
                refresh(item);
            }
        } while (batch.size() == pageSize);
        return "done";
    }
}
