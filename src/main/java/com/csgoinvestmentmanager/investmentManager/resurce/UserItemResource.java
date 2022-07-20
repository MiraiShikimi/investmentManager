package com.csgoinvestmentmanager.investmentManager.resurce;

import com.csgoinvestmentmanager.investmentManager.dto.UserItemDto;
import com.csgoinvestmentmanager.investmentManager.model.Response;
import com.csgoinvestmentmanager.investmentManager.model.UserItem;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.UserItemServiceImplemantation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/useritem")
@RestController
@RequiredArgsConstructor
public class UserItemResource {
    private final UserItemServiceImplemantation userItemService;

    @GetMapping("/list")
    public ResponseEntity<Response> getUserItems() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("userItems", userItemService.list(50)))
                        .message("user items retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("userItem", userItemService.get(id)))
                        .message("user item retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUserItem(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("deleted",userItemService.delete(id)))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> addUserItem(@RequestBody @Valid UserItem userItem){
        System.out.println("\n \n processing user item" + userItem.getCsgoItem().getDisplayName());
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("userItem", userItemService.addUserItem(userItem)))
                        .message("user item added")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateUserItem(@RequestBody @Valid UserItemDto userItemDto){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("userItem", userItemService.update(userItemDto).getCsgoItem()))
                        .message("user item updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}
