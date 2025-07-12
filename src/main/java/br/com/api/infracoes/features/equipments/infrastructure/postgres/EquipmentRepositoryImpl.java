package br.com.api.infracoes.features.equipments.infrastructure.postgres;

import br.com.api.infracoes.features.equipments.domain.Equipment;
import br.com.api.infracoes.features.equipments.domain.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EquipmentRepositoryImpl implements EquipmentRepository {

    private final EquipmentJpaRepository equipmentJpaRepository;
    private final MapperJpa mapperJpa;

    @Override
    public void save(Equipment equipment) {
        EquipmentEntity entity = mapperJpa.toEntity(equipment);
        equipmentJpaRepository.save(entity);
    }

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


}
