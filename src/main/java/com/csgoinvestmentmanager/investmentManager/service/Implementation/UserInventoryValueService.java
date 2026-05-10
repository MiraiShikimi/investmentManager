package com.csgoinvestmentmanager.investmentManager.service.Implementation;

import com.csgoinvestmentmanager.investmentManager.Mappers.UserInventoryValueMapper;
import com.csgoinvestmentmanager.investmentManager.dto.UserInventoryValueDto;
import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.UserInvenoryValue;
import com.csgoinvestmentmanager.investmentManager.model.UserItem;
import com.csgoinvestmentmanager.investmentManager.repository.UserInventoryValueRepo;
import com.csgoinvestmentmanager.investmentManager.repository.UserItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.BigDecimal.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserInventoryValueService {
    private final UserInventoryValueRepo userInventoryValueRepo;
    private final UserItemRepository userItemRepo;
    private final AuthService authService;



    public void calculateUserInventoryValues(){
        Map<AppUser, List<UserItem>> itemsByUser = userItemRepo.findAllWithUsersAndItems()
                .stream()
                .collect(Collectors.groupingBy(UserItem::getAppUser));

        for (Map.Entry<AppUser, List<UserItem>> entry : itemsByUser.entrySet()) {
            AppUser appUser = entry.getKey();
            BigDecimal inventoryValue = ZERO;
            BigDecimal inventoryValueTaxed = ZERO;

            log.info("running calculations for {}", appUser.getUsername());

            for (UserItem userItem : entry.getValue()) {
                BigDecimal itemPrice = userItem.getCsgoItem().getLowestPrice();
                BigDecimal taxedItemValue = (0 > itemPrice.compareTo(new BigDecimal("0.22")))
                        ? itemPrice.subtract(new BigDecimal("0.02"))
                        : itemPrice.divide(new BigDecimal("1.15"), 2, RoundingMode.HALF_UP);
                BigDecimal qty = valueOf(userItem.getQuantity());
                inventoryValue = inventoryValue.add(qty.multiply(itemPrice));
                inventoryValueTaxed = inventoryValueTaxed.add(qty.multiply(taxedItemValue));
            }

            UserInvenoryValue userInvenoryValue = new UserInvenoryValue();
            userInvenoryValue.setInventoryValue(inventoryValue);
            userInvenoryValue.setInventoryValueTaxed(inventoryValueTaxed);
            userInvenoryValue.setDateOfValue(LocalDateTime.now(ZoneOffset.UTC));
            userInvenoryValue.setAppUser(appUser);
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
