package com.csgoinvestmentmanager.investmentManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "csgoitem" )
@Data
@AllArgsConstructor
public class CSGOItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "hash_name", unique = true, nullable = false, length = 100)
    private String hashName;
    @Column(name = "display_name", length = 100)
    private String displayName;
    @Column(name = "lowest_price", length = 20)
    private BigDecimal lowestPrice;
    @Column(name = "image_url")
    private String imageURL;


    public CSGOItem() {
    }

    public CSGOItem(String hashName, String imageURL) {
        this.hashName = hashName;
        this.imageURL = imageURL;
    }

    public String getHashName() {
        return hashName;
    }

    public void setHashName(String hashName) {
        this.hashName = hashName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
