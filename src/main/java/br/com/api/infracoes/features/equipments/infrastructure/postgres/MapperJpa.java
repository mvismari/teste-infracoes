package br.com.api.infracoes.features.equipments.infrastructure.postgres;

import br.com.api.infracoes.features.equipments.domain.Equipment;
import org.springframework.stereotype.Component;

@Component
public class MapperJpa {

    public EquipmentEntity toEntity(Equipment domain) {
        EquipmentEntity entity = new EquipmentEntity();
        entity.setSerial(domain.getSerial());
        entity.setModel(domain.getModel());
        entity.setAddress(domain.getAddress());
        entity.setLatitude(domain.getLatitude());
        entity.setLongitude(domain.getLongitude());
        entity.setActive(domain.getActive());
        return entity;
    }

    public Equipment toDomain(EquipmentEntity entity) {
        Equipment equipment = new Equipment();
        equipment.setSerial(entity.getSerial());
        equipment.setModel(entity.getModel());
        equipment.setAddress(entity.getAddress());
        equipment.setLatitude(entity.getLatitude());
        equipment.setLongitude(entity.getLongitude());
        equipment.setActive(entity.getActive());
        return equipment;
    }
}
