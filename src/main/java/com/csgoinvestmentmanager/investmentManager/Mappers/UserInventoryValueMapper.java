package com.csgoinvestmentmanager.investmentManager.Mappers;


import com.csgoinvestmentmanager.investmentManager.dto.UserInventoryValueDto;
import com.csgoinvestmentmanager.investmentManager.model.UserInvenoryValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserInventoryValueMapper {

    UserInventoryValueMapper INSTANCE = Mappers.getMapper(UserInventoryValueMapper.class);

    @Mapping(source = "inventoryValueTaxed", target = "inventoryValueTaxed")
    UserInventoryValueDto toDto(UserInvenoryValue userItem);
}