package com.csgoinvestmentmanager.investmentManager.controller;

import com.csgoinvestmentmanager.investmentManager.dto.AuthenticationResponse;
import com.csgoinvestmentmanager.investmentManager.dto.LoginRequest;
import com.csgoinvestmentmanager.investmentManager.dto.RefreshTokenRequest;
import com.csgoinvestmentmanager.investmentManager.dto.RegisterRequest;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody  RegisterRequest registerRequest){
            authService.signup(registerRequest);
            return new ResponseEntity<>("User Registration succsesful ", HttpStatus.OK);

    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }


    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);

    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("this is happening");
        return authService.refreshToken(request,response);

    }

}
