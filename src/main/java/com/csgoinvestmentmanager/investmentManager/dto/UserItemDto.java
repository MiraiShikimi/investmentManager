package com.csgoinvestmentmanager.investmentManager.dto;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserItemDto implements Serializable {

    private CSGOItem csgoItem;
    private Long id;
    private Long quantity;


}
