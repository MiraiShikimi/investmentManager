package com.csgoinvestmentmanager.investmentManager.resurce;

import com.csgoinvestmentmanager.investmentManager.model.CSGOItem;
import com.csgoinvestmentmanager.investmentManager.model.Response;
import com.csgoinvestmentmanager.investmentManager.service.Implementation.CSGOItemServiceImplementation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/csgoitem")
@RequiredArgsConstructor
@Slf4j
public class CSGOItemResource {
    private final CSGOItemServiceImplementation csgoItemService;

    @GetMapping("/list")
    public ResponseEntity<Response> getCSGOItems() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("csgoItems", csgoItemService.list(50)))
                        .message("csgo items retrived")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()

        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getCSGOItem(@PathVariable("id") Long id) {
        System.out.println("NOT WORKING");
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("csgoItem", csgoItemService.get(id)))
                        .message("csgo Item retried")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid CSGOItem csgoItem) {

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("csgoItem", csgoItemService.create(csgoItem)))
                        .message("CSGO Item created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/updateprice/{id}")
    public ResponseEntity<Response> refreshCSGOItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("csgoItem", csgoItemService.refresh(id)))
                        .message("CSGO Item created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/updateprice/all")
    public ResponseEntity<Response> refreshAllCSGOItem() {
        System.out.println("refreshing items");
        log.info("refreshing items");
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("csgoItem", csgoItemService.refreshAllPrices()))
                        .message("all prices have been updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/save")
    public ResponseEntity<Response> updateCSGOItem(@RequestBody @Valid CSGOItem csgoItem) {

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("csgoItem", csgoItemService.update(csgoItem)))
                        .message("CSGO Item created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("deleted",csgoItemService.delete(id)))
                        .message("item deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}
