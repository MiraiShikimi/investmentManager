package com.csgoinvestmentmanager.investmentManager.security;

import com.csgoinvestmentmanager.investmentManager.filer.CustomAuthFilter;
import com.csgoinvestmentmanager.investmentManager.filer.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${key}")
    private String key;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable();
        http.authorizeRequests().antMatchers("/api/auth/**","/swagger-ui/**").permitAll();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/login/**","api/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui",
                "/webjars/**").permitAll();
        http.authorizeRequests().antMatchers(GET,"/api/user/**","/api/list").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/api/role/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/csgoitem/save","/csgoitem/refresh/all").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/useritem/**").hasAnyAuthority("ROLE_USER");


        http.authorizeRequests().anyRequest().authenticated();
      //  http.addFilter(new CustomAuthFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(key), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }




}
