package com.csgoinvestmentmanager.investmentManager.repository;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSGOItemRepository extends JpaRepository<CSGOItem, Long> {



    void deleteByhashName(String hashName);

    CSGOItem findCSGOItemByHashName(String hashName);
}
