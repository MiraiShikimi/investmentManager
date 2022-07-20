package com.csgoinvestmentmanager.investmentManager.dto;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UserItemDto implements Serializable {

    private Long id;
    private Long quantity;
    private CSGOItem csgoItem;

}
