package br.com.api.infracoes.features.equipments.domain;

import org.springframework.data.domain.Page;

public interface EquipmentRepository {
    void save(Equipment equipment);
    Equipment findBySerial(String serial);
    Page<Equipment> findAll(int page, int size);

}
