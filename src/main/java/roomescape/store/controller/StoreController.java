package roomescape.store.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.store.payload.StoreResponse;
import roomescape.store.service.StoreService;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> storeResponses = storeService.findAll().stream()
                .map(StoreResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(storeResponses);
    }

}
