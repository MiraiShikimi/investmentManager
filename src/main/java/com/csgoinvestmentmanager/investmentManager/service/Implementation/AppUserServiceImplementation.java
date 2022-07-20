package com.csgoinvestmentmanager.investmentManager.service.Implementation;

import com.csgoinvestmentmanager.investmentManager.Exeptions.SpringRedditException;
import com.csgoinvestmentmanager.investmentManager.model.Role;
import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.repository.AppUserRepo;
import com.csgoinvestmentmanager.investmentManager.repository.RoleRepo;
import com.csgoinvestmentmanager.investmentManager.service.intefaces.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImplementation implements AppUserService, UserDetailsService {
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;



    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("saving user");
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepo.save(appUser);

    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving Role");
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("adding role to user");
        AppUser appUser = appUserRepo.findByUsername(username).orElseThrow(() -> new SpringRedditException("failed to add the role to the user"));
        Role role = roleRepo.findByName(roleName);
        appUser.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String userName) {
        log.info("getting user");
        return appUserRepo.findByUsername(userName).orElseThrow(() -> new SpringRedditException("no user found with username - " + userName));
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("getting users");
        return appUserRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appuser = appUserRepo.findByUsername(username).orElseThrow(() -> new SpringRedditException(" no user found with that username"));
        if(appuser == null){
            log.error("user not found");
            throw new UsernameNotFoundException("user not found");
        } else{
            log.info("user found in the database {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appuser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        authorities.forEach(role -> log.info("the Users authorities" + role.toString()));
        return new User(appuser.getUsername(),appuser.getPassword(),authorities);
    }
}
