package com.csgoinvestmentmanager.investmentManager.service.Implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.csgoinvestmentmanager.investmentManager.Exeptions.SpringRedditException;
import com.csgoinvestmentmanager.investmentManager.config.AppConfig;
import com.csgoinvestmentmanager.investmentManager.dto.AccessTokenAndAuth;
import com.csgoinvestmentmanager.investmentManager.dto.AuthenticationResponse;
import com.csgoinvestmentmanager.investmentManager.dto.LoginRequest;
import com.csgoinvestmentmanager.investmentManager.dto.RegisterRequest;
import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.NotificationEmail;
import com.csgoinvestmentmanager.investmentManager.model.Role;
import com.csgoinvestmentmanager.investmentManager.model.VerificationToken;
import com.csgoinvestmentmanager.investmentManager.repository.AppUserRepo;
import com.csgoinvestmentmanager.investmentManager.repository.RoleRepo;
import com.csgoinvestmentmanager.investmentManager.repository.VerificationTokenRepository;
import com.csgoinvestmentmanager.investmentManager.security.JwtProvider;
import com.csgoinvestmentmanager.investmentManager.service.intefaces.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    @Value("${key}")
    private String key;

    private final PasswordEncoder passwordEncoder;
    private final AppUserRepo appUserRepo;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final AppUserService appUserService;
    private final RoleRepo roleRepo;
    private final AppConfig appConfig;



    public void signup(RegisterRequest registerRequest){
        AppUser user = new AppUser();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(false);
        user.setPictureUrl("default");
        addRoleToUser(user,"ROLE_USER");

        appUserRepo.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Plese Activate your account", user.getEmail(), "Thank you for signing up, " +
                "please click on the below url to activate your account : " +
                appConfig.getUrl() +  "/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(AppUser user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;

     }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken =verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        AppUser user = appUserRepo.findByUsername(username).orElseThrow(() -> new SpringRedditException("user not found with name - " + username));
        user.setEnabled(true);
        appUserRepo.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        AccessTokenAndAuth token = jwtProvider.generateToken(authenticate);
        String refreshToken = jwtProvider.generateRefreshToken(authenticate);
        AppUser tempUser =appUserRepo.findByUsername(loginRequest.getUsername()).orElseThrow();
        return AuthenticationResponse.builder()
                .authenticationToken(token.getAccessToken())
                .refreshToken(refreshToken)
                .username(loginRequest.getUsername())
                .roles(token.getAuthorities())
                .pictureUrl(tempUser.getPictureUrl())
                .build();

    }

    @Transactional(readOnly = true)
    public AppUser getCurrentUser() {
        String principal = (String) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        log.info("it breaks before after");
        AppUser tempuser =appUserRepo.findByUsername(principal)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal));
        log.info(tempuser.getEmail());
        log.info("returning");
        return tempuser;
    }



    public void changeProfilePicture(String url) {
        log.info("changing user profile picture");
        AppUser tempUser = getCurrentUser();
        tempUser.setPictureUrl(url);
        appUserRepo.save(tempUser);
    }


    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        log.error("the authorization header is " + authorizationHeader);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                log.info("passed the if statement");
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(key.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = appUserService.getUser(username);
                String authenticationToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("authenticationToken", authenticationToken);
                tokens.put("refresh_token", refresh_token);
                authenticationResponse.setAuthenticationToken(authenticationToken);
                authenticationResponse.setRefreshToken(refresh_token);
                authenticationResponse.setUsername(username);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                log.error("request token timed out " + exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }

        return authenticationResponse;


    }


    public void addRoleToUser(AppUser appUser, String roleName) {
        log.info("adding role to user");
        Role role = roleRepo.findByName(roleName);
        appUser.getRoles().add(role);
    }
}
