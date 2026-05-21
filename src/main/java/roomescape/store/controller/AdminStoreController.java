package roomescape.store.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.store.entity.Store;
import roomescape.store.payload.StoreRequest;
import roomescape.store.payload.StoreResponse;
import roomescape.store.service.StoreService;

@RestController
@RequestMapping("/admin/stores")
public class AdminStoreController {

    private final StoreService storeService;

    public AdminStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<StoreResponse> postStore(
            @Valid @RequestBody StoreRequest request
    ) {
        Store store = storeService.save(request);
        URI location = URI.create("/stores/" + store.getId());

        return ResponseEntity.created(location)
                .body(StoreResponse.from(store));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable Long id
    ) {
        storeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
