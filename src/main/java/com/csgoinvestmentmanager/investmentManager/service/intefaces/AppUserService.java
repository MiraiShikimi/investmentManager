package com.csgoinvestmentmanager.investmentManager.service.intefaces;

import com.csgoinvestmentmanager.investmentManager.model.Role;
import com.csgoinvestmentmanager.investmentManager.model.AppUser;

import java.util.List;



public interface AppUserService {
    AppUser saveUser(AppUser appUser);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String userName);
    List<AppUser> getUsers();
}
