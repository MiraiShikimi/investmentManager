package com.csgoinvestmentmanager.investmentManager.repository;

import com.csgoinvestmentmanager.investmentManager.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Long> {

    Optional<Object> findByToken(String token);

    void deleteByToken(String token);
}
