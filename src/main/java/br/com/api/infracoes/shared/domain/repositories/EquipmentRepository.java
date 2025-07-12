package br.com.api.infracoes.shared.domain.repositories;

import br.com.api.infracoes.shared.domain.entities.Equipment;
import org.springframework.data.domain.Page;

public interface EquipmentRepository {
    void save(Equipment equipment);
    Equipment findBySerial(String serial);
    Page<Equipment> findAll(int page, int size);

}
