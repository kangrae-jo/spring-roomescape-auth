package roomescape.store.payload;

import roomescape.store.entity.Store;

public record StoreResponse(
        Long id,
        Long memberId,
        String managerName
) {

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getManager().getId(),
                store.getManager().getName()
        );
    }

}
