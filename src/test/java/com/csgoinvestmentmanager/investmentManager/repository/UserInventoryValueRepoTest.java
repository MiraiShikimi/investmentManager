package com.csgoinvestmentmanager.investmentManager.repository;

import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.UserInvenoryValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserInventoryValueRepoTest {

    @Autowired
    private UserInventoryValueRepo userInventoryValueRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    AppUser appuser = new AppUser();
    UserInvenoryValue userInvenoryValue = new UserInvenoryValue();

    @BeforeEach
    void setUp() {

        appuser.setEmail("testEmail@test.com");
        appuser.setUsername("tester");
        appuser.setPassword("tester");
        appUserRepo.save(appuser);

        userInvenoryValue.setAppUser(appuser);
        userInvenoryValue.setDateOfValue(LocalDateTime.now());
        userInvenoryValue.setInventoryValue(BigDecimal.ONE);
        userInventoryValueRepo.save(userInvenoryValue);

        userInvenoryValue.setAppUser(appuser);
        userInvenoryValue.setDateOfValue(LocalDateTime.now());
        userInvenoryValue.setInventoryValue(BigDecimal.TEN);
        userInventoryValueRepo.save(userInvenoryValue);


    }

    @Test
    void findAllByAppUser() {
        //given
        //when
        List<UserInvenoryValue> list = userInventoryValueRepo.findAllByAppUser(appuser).stream().toList();
        //then
        for (UserInvenoryValue value: list
             ) {
            assertThat(value.getAppUser()).isEqualTo(appuser);

        }


    }
}