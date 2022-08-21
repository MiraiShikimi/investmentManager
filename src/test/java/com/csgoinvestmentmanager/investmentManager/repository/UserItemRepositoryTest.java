package com.csgoinvestmentmanager.investmentManager.repository;

import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import com.csgoinvestmentmanager.investmentManager.model.UserItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UserItemRepositoryTest {

    @Autowired
    private UserItemRepository userItemRepository;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private CSGOItemRepository csgoItemRepository;

    CSGOItem csgoItem = new CSGOItem();
    UserItem userItem = new UserItem();
    AppUser appuser = new AppUser();
    Long id = (long)1;

    @BeforeEach
    void setUp() {
        //given
        csgoItem.setHashName("Gamma%20Case");
        csgoItem.setImageURL("ImageUrl");

        appuser.setEmail("testEmail@test.com");
        appuser.setUsername("tester");
        appuser.setPassword("tester");

        appUserRepo.save(appuser);
        csgoItemRepository.save(csgoItem);

        userItem.setAppUser(appuser);
        userItem.setQuantity((long)10);
        userItem.setCsgoItem(csgoItem);

        userItemRepository.save(userItem);

    }

    @Test
    void findAllByAppUser() {
        //given
        //when
        List<UserItem> tempUserItem = userItemRepository.findAllByAppUser(appuser).stream().toList();
        //then
        for (UserItem item: tempUserItem) {
            assertThat(item.getAppUser()).isEqualTo(appuser);

        }

    }

    @Test
    void findDistinctId() {
        //given
        //when
        List<AppUser> list = userItemRepository.findDistinctId();
        //then
        Set<AppUser> userSet = new HashSet<>(list);

        assertThat(list.size()).isEqualTo(userSet.size());
     }

    @Test
    void findAllbyUserId() {
    }
}