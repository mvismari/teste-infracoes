package br.com.api.infracoes.features.violations.infrastructure.postgres;

import br.com.api.infracoes.shared.domain.entities.Violation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationJpaRepository extends JpaRepository<ViolationEntity, Long>, JpaSpecificationExecutor<ViolationEntity> {
}
