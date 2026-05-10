package com.csgoinvestmentmanager.investmentManager.resurce;

import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.Response;
import com.csgoinvestmentmanager.investmentManager.model.Role;
import com.csgoinvestmentmanager.investmentManager.service.intefaces.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserResource {

    private final AppUserService appUserService;

    @GetMapping("/list")
    public ResponseEntity<?>getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return ResponseEntity.ok().body(appUserService.getUsers(PageRequest.of(page, size)));
    }
    /*
    @PostMapping("/user/save")
    public ResponseEntity<AppUser>saveUser(@RequestBody AppUser appUser){
        return ResponseEntity.ok().body(appUserService.saveUser(appUser));
    }
    */



    @PostMapping("/role/save")
    public ResponseEntity<Role>saverole(@RequestBody Role role ){
        return ResponseEntity.ok().body(appUserService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<Role>saverole(@RequestBody RoleToUserForm form){
        appUserService.addRoleToUser(form.getUserName(), form.getRoleName());
        return ResponseEntity.ok().build();
    }



    /*
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = appUserService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
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
    }

     */

}

@Data
class RoleToUserForm{
    private String userName;
    private String roleName;
}
