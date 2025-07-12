package br.com.api.infracoes.features.equipments.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipmentRepository {
    void save(Equipment equipment);
    Equipment findBySerial(String serial);
    Page<Equipment> findAll(Pageable pageable);

}
