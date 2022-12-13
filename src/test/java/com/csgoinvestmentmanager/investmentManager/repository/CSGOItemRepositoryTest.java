package com.csgoinvestmentmanager.investmentManager.repository;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CSGOItemRepositoryTest {

    @Autowired
    private CSGOItemRepository underTest;
    
    @Test
    void findCSGOItemByHashName() {
        //given
        CSGOItem csgoItem = new CSGOItem(
                "gamma%20case","imageUrl"
        );
        underTest.save(csgoItem);
        //when
       boolean exists = underTest.findCSGOItemByHashName(csgoItem.getHashName()).equals(csgoItem);
        //then
        assertThat(exists).isTrue();
    }
}