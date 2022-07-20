package com.csgoinvestmentmanager.investmentManager.resurce;

import com.csgoinvestmentmanager.investmentManager.model.Response;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.UserInventoryValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;

@RequestMapping("/userinventoryvalue")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserInventoryValueResource {
    private final UserInventoryValueService userInventoryValueService;

    @GetMapping("/list")
    public ResponseEntity<Response> getUserInventoryValue(){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(of("userInventoryValues", userInventoryValueService.getUserInventoryValueForCurrentUser()))
                        .message("retried Users Inventory Values")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}
