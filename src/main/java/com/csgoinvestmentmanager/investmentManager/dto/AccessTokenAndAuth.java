package com.csgoinvestmentmanager.investmentManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessTokenAndAuth {
    private String accessToken;
    private List<String> authorities;
}
