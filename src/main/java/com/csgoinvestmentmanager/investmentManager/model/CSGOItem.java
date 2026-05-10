package com.csgoinvestmentmanager.investmentManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "csgoitem")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CSGOItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "hash_name", unique = true, nullable = false, length = 175)
    private String hashName;
    @Column(name = "display_name", length = 100)
    private String displayName;
    @Column(name = "lowest_price", length = 20)
    private BigDecimal lowestPrice;
    @Column(name = "image_url")
    private String imageURL;

    public CSGOItem(String hashName, String imageURL) {
        this.hashName = hashName;
        this.imageURL = imageURL;
    }
}
