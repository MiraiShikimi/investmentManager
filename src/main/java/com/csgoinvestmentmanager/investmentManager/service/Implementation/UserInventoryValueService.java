package com.csgoinvestmentmanager.investmentManager.service.Implementation;

import com.csgoinvestmentmanager.investmentManager.Mappers.UserInventoryValueMapper;
import com.csgoinvestmentmanager.investmentManager.Mappers.UserItemMapper;
import com.csgoinvestmentmanager.investmentManager.dto.UserInventoryValueDto;
import com.csgoinvestmentmanager.investmentManager.dto.UserItemDto;
import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.UserInvenoryValue;
import com.csgoinvestmentmanager.investmentManager.model.UserItem;
import com.csgoinvestmentmanager.investmentManager.repository.AppUserRepo;
import com.csgoinvestmentmanager.investmentManager.repository.UserInventoryValueRepo;
import com.csgoinvestmentmanager.investmentManager.repository.UserItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserInventoryValueService {
    private final UserInventoryValueRepo userInventoryValueRepo;
    private final UserItemRepository userItemRepo;
    private final AppUserRepo appUserRepo;
    BigDecimal inventoryValue =  ZERO;
    private final AuthService authService;



    public void calculateUserInventoryValues(){

        for (AppUser appUser: userItemRepo.findDistinctId()) {
            UserInvenoryValue userInvenoryValue = new UserInvenoryValue();
            inventoryValue = ZERO;

            log.info("running calcularions for " + appUser.getUsername());

            for (UserItem userItem: userItemRepo.findAllbyUserId(appUser)
                 ) {
                System.out.println("running " +inventoryValue);

                inventoryValue = inventoryValue.add(valueOf(userItem.getQuantity()).multiply(userItem.getCsgoItem().getLowestPrice()));
            }
            System.out.println(inventoryValue);
            userInvenoryValue.setInventoryValue(inventoryValue);
            userInvenoryValue.setDateOfValue(LocalDateTime.now());
            userInvenoryValue.setAppUser(appUserRepo.findById(appUser.getId()).get());

            userInventoryValueRepo.save(userInvenoryValue);
        }
    }

    public List<UserInventoryValueDto> getUserInventoryValueForCurrentUser(){
        log.info("fetching user inventory value");
        AppUser appuser = authService.getCurrentUser();
        List<UserInvenoryValue> theList = userInventoryValueRepo.findAllByAppUser(appuser).stream().toList();
        List<UserInventoryValueDto> dtoList = new ArrayList<>();
        theList.forEach(userInventoryValue ->  dtoList.add(UserInventoryValueMapper.INSTANCE.toDto(userInventoryValue)));
        return dtoList;
    }
}
