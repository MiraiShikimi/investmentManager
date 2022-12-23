package com.csgoinvestmentmanager.investmentManager.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appuser")
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "id")
    private Long id;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false, unique = true, name = "user_name")
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    private boolean enabled;
    @Column(name = "profile_picture")
    private String pictureUrl;



}
