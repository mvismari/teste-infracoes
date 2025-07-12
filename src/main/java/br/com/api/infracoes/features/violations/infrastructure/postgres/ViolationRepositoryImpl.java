package br.com.api.infracoes.features.violations.infrastructure.postgres;

import br.com.api.infracoes.shared.domain.entities.Violation;
import br.com.api.infracoes.shared.domain.repositories.ViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ViolationRepositoryImpl implements ViolationRepository {

    private final ViolationJpaRepository violationJpaRepository;
    private final ViolationMapperJpa violationMapperJpa;

    @Override
    public Long save(Violation violation) {
        ViolationEntity entity = violationMapperJpa.toEntity(violation);
        ViolationEntity saved = violationJpaRepository.save(entity);
        return saved.getId();
    }

    @Override
    public Violation findById(Long id) {
        return violationJpaRepository
                .findById(id)
                .map(violationMapperJpa::toDomain)
                .orElse(null);
    }

    /*
    @Override
    public Equipment findBySerial(String serial) {
        return equipmentJpaRepository
                .findById(serial)
                .map(mapperJpa::toDomain)
                .orElse(null);
    }

    @Override
    public Page<Equipment> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return equipmentJpaRepository.findAll(pageable).map(mapperJpa::toDomain);
    }

*/
}
