package com.csgoinvestmentmanager.investmentManager.repository;

import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    Collection<UserItem> findAllByAppUser(AppUser appuser);

    @Query("SELECT DISTINCT appUser FROM UserItem ")
    List<AppUser> findDistinctId();

    @Query("FROM UserItem WHERE appUser = ?1")
    List<UserItem> findAllbyUserId(AppUser appUser);


}
