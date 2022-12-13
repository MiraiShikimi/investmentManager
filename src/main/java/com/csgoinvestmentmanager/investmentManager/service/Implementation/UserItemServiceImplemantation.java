package com.csgoinvestmentmanager.investmentManager.service.Implementation;

import com.csgoinvestmentmanager.investmentManager.Mappers.UserItemMapper;
import com.csgoinvestmentmanager.investmentManager.dto.UserItemDto;
import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.UserItem;
import com.csgoinvestmentmanager.investmentManager.repository.UserItemRepository;
import com.csgoinvestmentmanager.investmentManager.service.intefaces.UserItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserItemServiceImplemantation implements UserItemService {

    private final UserItemRepository userItemRepository;
    private final AuthService authService;

    @Override
    public UserItem addUserItem(UserItem userItem) {
        log.info("adding new UserItem: {}", userItem.getCsgoItem().getDisplayName());
        log.info("username " + authService.getCurrentUser().getUsername());
        userItem.setAppUser(authService.getCurrentUser());
        return userItemRepository.save(userItem);
    }

    @Override
    public UserItem get(Long id) {
        log.info("getting UserItem by id: {}", id);
        return userItemRepository.findById(id).get();
    }

    @Override
    public Boolean delete(Long id) {
        log.info("deleting user item by id: {}", id);
        userItemRepository.deleteById(id);
        return Boolean.TRUE;
    }


    @Override
    public UserItem update(UserItemDto userItemDto) {
        log.info("updating UserItem: {}", userItemDto.getCsgoItem().getDisplayName());
        UserItem theItem = userItemRepository.findById(userItemDto.getId()).get();
        theItem.setQuantity(userItemDto.getQuantity());
        return userItemRepository.save(theItem);
    }

    @Override
    public Collection<UserItemDto> list(int limit) {
        log.info("fetching user items");
        AppUser appuser = authService.getCurrentUser();
        List<UserItem> theList = userItemRepository.findAllByAppUser(appuser).stream().toList();
        List<UserItemDto> dtoList = new ArrayList<>();
        theList.forEach(userItem ->  dtoList.add(UserItemMapper.INSTANCE.toDto(userItem)));
        return dtoList;
    }
}
