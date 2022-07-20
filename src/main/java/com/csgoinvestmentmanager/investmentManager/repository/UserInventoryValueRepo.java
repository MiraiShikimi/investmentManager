package com.csgoinvestmentmanager.investmentManager.repository;

import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.UserInvenoryValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserInventoryValueRepo extends JpaRepository<UserInvenoryValue,Long> {

    Collection<UserInvenoryValue> findAllByAppUser(AppUser appuser);
}
