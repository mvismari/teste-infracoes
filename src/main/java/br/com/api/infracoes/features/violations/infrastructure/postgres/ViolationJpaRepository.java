package br.com.api.infracoes.features.violations.infrastructure.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationJpaRepository extends JpaRepository<ViolationEntity, Long> {
}
