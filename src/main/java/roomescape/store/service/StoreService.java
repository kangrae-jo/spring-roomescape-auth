package roomescape.store.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.NotFoundException;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.store.entity.Store;
import roomescape.store.payload.StoreRequest;
import roomescape.store.repository.StoreRepository;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public StoreService(StoreRepository storeRepository, MemberRepository memberRepository) {
        this.storeRepository = storeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Store save(StoreRequest request) {
        Member manager = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotFoundException(DomainType.MEMBER, request.memberId()));

        return storeRepository.save(Store.create(manager));
    }

    @Transactional(readOnly = true)
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        int affected = storeRepository.deleteById(id);
        if (affected == 0) {
            throw new NotFoundException(DomainType.STORE, id);
        }
    }

}
