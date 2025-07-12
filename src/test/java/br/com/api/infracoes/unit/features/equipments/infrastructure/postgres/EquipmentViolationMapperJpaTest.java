package br.com.api.infracoes.unit.features.equipments.infrastructure.postgres;

import br.com.api.infracoes.shared.domain.entities.Equipment;
import br.com.api.infracoes.features.equipments.infrastructure.postgres.EquipmentEntity;
import br.com.api.infracoes.features.equipments.infrastructure.postgres.EquipmentMapperJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EquipmentViolationMapperJpaTest {

    @InjectMocks
    private EquipmentMapperJpa equipmentMapperJpa;

    private final static String EQP_SERIAL = "EQUIP-TEST";
    private final static String EQP_MODEL = "Modelo";
    private final static String EQP_ADDR = "Endereço";
    private static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90);
    private static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180);

    @Test
    @DisplayName("Deve converter Domínio para Entidade.")
    void toEntity_WithAllRequiredFields_ShouldBeValid() {
        Equipment equipment = createValidEquipment();
        EquipmentEntity equipmentEntity = equipmentMapperJpa.toEntity(equipment);

        assertEquals(equipmentEntity.getSerial(), equipment.getSerial());
        assertEquals(equipmentEntity.getModel(), equipment.getModel());
        assertEquals(equipmentEntity.getAddress(), equipment.getAddress());
        assertEquals(equipmentEntity.getLatitude(), equipment.getLatitude());
        assertEquals(equipmentEntity.getLongitude(), equipment.getLongitude());
        assertEquals(equipmentEntity.getActive(), equipment.getActive());
    }

    @Test
    @DisplayName("Deve converter Entidade para Domínio.")
    void toDomain_WithAllRequiredFields_ShouldBeValid() {
        EquipmentEntity equipmentEntity = createValidEquipmentEntity();
        Equipment equipment = equipmentMapperJpa.toDomain(equipmentEntity);

        assertEquals(equipmentEntity.getSerial(), equipment.getSerial());
        assertEquals(equipmentEntity.getModel(), equipment.getModel());
        assertEquals(equipmentEntity.getAddress(), equipment.getAddress());
        assertEquals(equipmentEntity.getLatitude(), equipment.getLatitude());
        assertEquals(equipmentEntity.getLongitude(), equipment.getLongitude());
        assertEquals(equipmentEntity.getActive(), equipment.getActive());
    }


    private Equipment createValidEquipment() {
        Equipment equipment = new Equipment();
        equipment.setSerial(EQP_SERIAL);
        equipment.setModel(EQP_MODEL);
        equipment.setAddress(EQP_ADDR);
        equipment.setLatitude(MAX_LATITUDE);
        equipment.setLongitude(MAX_LONGITUDE);
        return equipment;
    }

    private EquipmentEntity createValidEquipmentEntity() {
        EquipmentEntity equipment = new EquipmentEntity();
        equipment.setSerial(EQP_SERIAL);
        equipment.setModel(EQP_MODEL);
        equipment.setAddress(EQP_ADDR);
        equipment.setLatitude(MAX_LATITUDE);
        equipment.setLongitude(MAX_LONGITUDE);
        return equipment;
    }

}
