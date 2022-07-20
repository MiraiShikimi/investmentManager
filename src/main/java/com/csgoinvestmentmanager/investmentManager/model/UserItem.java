package com.csgoinvestmentmanager.investmentManager.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_item", uniqueConstraints = { @UniqueConstraint(name = "UniqueNumberAndStatus", columnNames = { "csgo_item_id","user_Id" }) })
public class UserItem implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name ="csgo_item_id",nullable = false)
    private CSGOItem csgoItem;
    @Column(nullable = false)
    private Long quantity;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_Id", referencedColumnName = "id",nullable = false)
    private AppUser appUser;

}
