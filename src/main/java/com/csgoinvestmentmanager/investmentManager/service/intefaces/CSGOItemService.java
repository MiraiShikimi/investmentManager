package com.csgoinvestmentmanager.investmentManager.service.intefaces;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;

import java.util.Collection;

public interface CSGOItemService {

    CSGOItem create(CSGOItem csgoItem);
    CSGOItem get(Long id);
    Boolean delete(Long id);
    CSGOItem update(CSGOItem csgoItem);
    Collection<CSGOItem> list(int limit);
    CSGOItem refresh(Long id);
    CSGOItem refresh(CSGOItem tempItem);
    String refreshAllPrices();

}
