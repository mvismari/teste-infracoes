package br.com.api.infracoes.features.equipments.infrastructure.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentJpaRepository extends JpaRepository<EquipmentEntity, String> {
}
