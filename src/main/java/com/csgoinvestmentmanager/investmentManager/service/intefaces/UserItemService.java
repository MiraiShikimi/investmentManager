package com.csgoinvestmentmanager.investmentManager.service.intefaces;

import com.csgoinvestmentmanager.investmentManager.dto.UserItemDto;
import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import com.csgoinvestmentmanager.investmentManager.model.UserItem;

import java.util.Collection;

public interface UserItemService {

    UserItem addUserItem(UserItem userItem);
    UserItem get(Long id);
    Boolean delete(Long id);

    UserItem update(UserItemDto userItemDto);

    Collection<UserItemDto> list(int limit);
}
