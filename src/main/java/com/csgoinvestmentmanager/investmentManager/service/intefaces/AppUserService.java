package com.csgoinvestmentmanager.investmentManager.service.intefaces;

import com.csgoinvestmentmanager.investmentManager.model.Role;
import com.csgoinvestmentmanager.investmentManager.model.AppUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppUserService {
    AppUser saveUser(AppUser appUser);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String userName);
    Page<AppUser> getUsers(Pageable pageable);
}
