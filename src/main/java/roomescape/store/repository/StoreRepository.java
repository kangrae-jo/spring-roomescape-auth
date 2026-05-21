package roomescape.store.repository;

import java.util.List;
import java.util.Optional;
import roomescape.store.entity.Store;

public interface StoreRepository {

    boolean existsById(Long id);

    Store save(Store store);

    Optional<Store> findById(Long id);

    List<Store> findAll();

    int deleteById(Long id);

}
