package com.csgoinvestmentmanager.investmentManager.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class UserInventoryValueDto implements Serializable {

    private Long id;
    private BigDecimal inventoryValue;
    private BigDecimal inventoryValueTaxed;
    private LocalDateTime dateOfValue;
  //  private Long appUserId;
}
