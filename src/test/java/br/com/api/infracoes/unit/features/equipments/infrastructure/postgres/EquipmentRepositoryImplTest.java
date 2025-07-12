package br.com.api.infracoes.unit.features.equipments.infrastructure.postgres;

import br.com.api.infracoes.features.equipments.domain.Equipment;
import br.com.api.infracoes.features.equipments.infrastructure.postgres.EquipmentEntity;
import br.com.api.infracoes.features.equipments.infrastructure.postgres.EquipmentJpaRepository;
import br.com.api.infracoes.features.equipments.infrastructure.postgres.EquipmentRepositoryImpl;
import br.com.api.infracoes.features.equipments.infrastructure.postgres.MapperJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentRepositoryImplTest {

    @InjectMocks
    private EquipmentRepositoryImpl equipmentRepository;

    @Mock
    private EquipmentJpaRepository equipmentJpaRepository;

    @Mock
    private MapperJpa mapperJpa;

    private Equipment equipment;
    private EquipmentEntity equipmentEntity;
    private EquipmentEntity equipmentEntity2;

    private static final int PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 5;
    private static final int EXPECTED_TOTAL_ELEMENTS = 2;

    private final static String EQP_SERIAL = "EQUIP-TEST";
    private final static String EQP_MODEL = "Modelo";
    private final static String EQP_ADDR = "Endereço";
    private static final String EQP2_SERIAL = "Equip 2";
    private final static String EQP_CONSTRAINT_ERROR_DUPLICATED = "Equipamento duplicado.";

    @BeforeEach
    void setUp() {

        equipment = new Equipment();
        equipment.setSerial(EQP_SERIAL);
        equipment.setModel(EQP_MODEL);
        equipment.setAddress(EQP_ADDR);

        Equipment equipment2 = new Equipment();
        equipment2.setSerial(EQP2_SERIAL);
        equipment2.setModel(EQP_MODEL);
        equipment2.setAddress(EQP_ADDR);

        equipmentEntity = new EquipmentEntity();
        equipmentEntity.setSerial(equipment.getSerial());
        equipmentEntity.setModel(equipment.getModel());
        equipmentEntity.setAddress(equipment.getAddress());

        equipmentEntity2 = new EquipmentEntity();
        equipmentEntity2.setSerial(equipment2.getSerial());
        equipmentEntity2.setModel(equipment2.getModel());
        equipmentEntity2.setAddress(equipment2.getAddress());
    }

    @Test
    @DisplayName("Deveria salvar o equipamento (Infrastructure/Postgres)")
    void save_ShouldSaveEquipment() {
        when(mapperJpa.toEntity(equipment))
                .thenReturn(equipmentEntity);

        equipmentRepository.save(equipment);
        verify(mapperJpa).toEntity(equipment);
        verify(equipmentJpaRepository).save(equipmentEntity);
    }

    @Test
    @DisplayName("Deveria retornar excessão por constraint de banco. (Infrastructure/Postgres)")
    void save_WhenSaveDuplicated_ShouldReturnException() {

        when(mapperJpa.toEntity(equipment))
                .thenReturn(equipmentEntity);

        when(equipmentJpaRepository.save(equipmentEntity))
                .thenThrow(new DataIntegrityViolationException(EQP_CONSTRAINT_ERROR_DUPLICATED));

        assertThrows(DataIntegrityViolationException.class, () -> {
            equipmentRepository.save(equipment);
        });

        verify(mapperJpa).toEntity(equipment);
    }

    @Test
    @DisplayName("Deveria retornar um equipamento (Infrastructure/Postgres)")
    void findBySerial_WhenExists_ShouldReturnEquipment() {
        when(equipmentJpaRepository.findById(EQP_SERIAL))
                .thenReturn(Optional.of(equipmentEntity));

        when(mapperJpa.toDomain(equipmentEntity))
                .thenReturn(equipment);

        Equipment result = equipmentRepository.findBySerial(EQP_SERIAL);

        assertNotNull(result);
        assertEquals(equipment, result);
        verify(equipmentJpaRepository).findById(EQP_SERIAL);
        verify(mapperJpa).toDomain(equipmentEntity);
    }

    @Test
    @DisplayName("Deveria retornar valor nulo por não encontrar o equipamento (Infrastructure/Postgres)")
    void findBySerial_WhenDoesNotExist_ShouldReturnNull() {
        when(equipmentJpaRepository.findById(EQP_SERIAL))
                .thenReturn(Optional.empty());

        Equipment result = equipmentRepository.findBySerial(EQP_SERIAL);

        assertNull(result);
        verify(equipmentJpaRepository).findById(EQP_SERIAL);
        verify(mapperJpa, never()).toDomain(any());
    }

    @Test
    @DisplayName("Deveria retornar todos os equipamentos (Infrastructure/Postgres)")
    void findAll_WhenExists_ShouldReturnAllEquipments() {

        List<EquipmentEntity> list = List.of(equipmentEntity, equipmentEntity2);
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<EquipmentEntity> page = new PageImpl<>(list, pageable, EXPECTED_TOTAL_ELEMENTS);

        when(equipmentJpaRepository.findAll(pageable)).thenReturn(page);
        when(mapperJpa.toDomain(equipmentEntity))
                .thenReturn(equipment);

        Page<Equipment> equipments = equipmentRepository.findAll(PAGE_NUMBER, PAGE_SIZE);
        assertNotNull(equipments);
        assertEquals(EXPECTED_TOTAL_ELEMENTS, equipments.getContent().size());

        verify(equipmentJpaRepository, times(1)).findAll(pageable);
        verify(mapperJpa, times(1)).toDomain(equipmentEntity);
    }

    @Test
    @DisplayName("Deveria retornar uma lista vazia de equipamentos (Infrastructure/Postgres)")
    void findAll_WhenNotExists_ShouldReturnEmpty() {

        List<EquipmentEntity> list = List.of();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<EquipmentEntity> page = new PageImpl<>(list, pageable, 0);

        when(equipmentJpaRepository.findAll(pageable)).thenReturn(page);

        Page<Equipment> equipments = equipmentRepository.findAll(PAGE_NUMBER, PAGE_SIZE);
        assertNotNull(equipments);
        assertEquals(0, equipments.getContent().size());

        verify(equipmentJpaRepository, times(1)).findAll(pageable);
    }

}
