package br.com.api.infracoes.features.violations.infrastructure.postgres;

import br.com.api.infracoes.features.violations.dto.ViolationFiltersRequestDto;
import br.com.api.infracoes.shared.domain.entities.Violation;
import br.com.api.infracoes.shared.domain.repositories.ViolationRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Page<Violation> findAll(ViolationFiltersRequestDto violationFiltersRequestDto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<ViolationEntity> spec = createSpecification(violationFiltersRequestDto);
        return violationJpaRepository.findAll(spec, pageable).map(violationMapperJpa::toDomain);
    }


    private Specification<ViolationEntity> createSpecification(ViolationFiltersRequestDto violationFiltersRequestDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("equipmentsSerialId"), violationFiltersRequestDto.serial()));

            if ( violationFiltersRequestDto.from() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("occurrenceDate"), violationFiltersRequestDto.from()));
            }

            if ( violationFiltersRequestDto.to() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("occurrenceDate"), violationFiltersRequestDto.to()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
