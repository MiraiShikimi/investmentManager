package com.csgoinvestmentmanager.investmentManager.Mappers;

import com.csgoinvestmentmanager.investmentManager.dto.UserItemDto;
import com.csgoinvestmentmanager.investmentManager.model.UserItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserItemMapper {

    UserItemMapper INSTANCE = Mappers.getMapper(UserItemMapper.class);


    UserItemDto toDto(UserItem userItem);

}
