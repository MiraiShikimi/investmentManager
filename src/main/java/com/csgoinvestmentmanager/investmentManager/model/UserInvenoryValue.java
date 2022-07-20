package com.csgoinvestmentmanager.investmentManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserInvenoryValue {
    @Id
    @GeneratedValue(strategy = AUTO)
    Long id;

    @Column(name = "inventory_value", nullable = false)
    BigDecimal inventoryValue;

    @Column(name = "date_of_value",nullable = false)
    LocalDateTime dateOfValue;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_Id", referencedColumnName = "id",nullable = false)
    private AppUser appUser;


}
