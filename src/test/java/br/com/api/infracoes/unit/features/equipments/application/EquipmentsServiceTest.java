package br.com.api.infracoes.unit.features.equipments.application;

import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.shared.domain.entities.Equipment;
import br.com.api.infracoes.shared.domain.repositories.EquipmentRepository;
import br.com.api.infracoes.features.equipments.dto.CreateEquipmentRequestDto;
import br.com.api.infracoes.features.equipments.exceptions.EquipmentExistsException;
import br.com.api.infracoes.shared.exceptions.NotFoundErrorException;
import br.com.api.infracoes.shared.util.MessageHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentsServiceTest {

    private CreateEquipmentRequestDto dto;
    private Equipment eqp1;
    private Equipment eqp2;

    @InjectMocks
    private EquipmentsService equipmentsService;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private MessageHelper messageSource;

    @Mock
    private ObjectMapper objectMapper;

    private static final int PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 5;
    private static final int EXPECTED_TOTAL_ELEMENTS = 2;

    private final static String EQP_SERIAL = "EQUIP-TEST";
    private final static String EQP_MODEL = "Modelo";
    private final static String EQP_ADDR = "Endereço";
    private static final String EQP1_SERIAL = "Equip 1";
    private static final String EQP2_SERIAL = "Equip 2";
    private static final String DEFAULT_MODEL = "Modelo";
    private static final String DEFAULT_ADDRESS = "endereço";
    private static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90);
    private static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180);

    private static final String MSG_ERR_NOT_FOUND = "Equipamento não encontrado";
    private static final String MSG_ERR_EXISTS = "Equipamento já existe.";

    @BeforeEach
    void setUp() {

        dto = new CreateEquipmentRequestDto();
        dto.setSerial(EQP_SERIAL);
        dto.setModel(EQP_MODEL);
        dto.setAddress(EQP_ADDR);
        dto.setLatitude(MAX_LATITUDE);
        dto.setLongitude(MAX_LONGITUDE);
        dto.setActive(true);

        eqp1 = new Equipment();
        eqp1.setSerial(EQP1_SERIAL);
        eqp1.setModel(DEFAULT_MODEL);
        eqp1.setAddress(DEFAULT_ADDRESS);

        eqp2 = new Equipment();
        eqp2.setSerial(EQP2_SERIAL);
        eqp2.setModel(DEFAULT_MODEL);
        eqp2.setAddress(DEFAULT_ADDRESS);
    }

    @Test
    @DisplayName("Deveria cadastrar um equipamento se o serial ainda não existe.")
    void save_ShouldSaveEquipment() {
        Equipment expectedEquipment = new Equipment();
        expectedEquipment.setSerial(EQP_SERIAL);
        when(equipmentRepository.findBySerial(dto.getSerial())).thenReturn(null);
        when(objectMapper.convertValue(dto, Equipment.class)).thenReturn(expectedEquipment);
        equipmentsService.save(dto);
        verify(equipmentRepository).save(expectedEquipment);
    }

    @Test
    @DisplayName("Deveria lançar exceção quando equipamento já está cadastrado")
    void save_WhenEquipmentAlreadyExists_ShouldThrowEquipmentExistsException() {
        when(equipmentRepository.findBySerial(EQP_SERIAL)).thenReturn(eqp1);
        when(messageSource.get("equipment.error.exists")).thenReturn(MSG_ERR_EXISTS);

        assertThatThrownBy(() -> equipmentsService.save(dto))
                .isInstanceOf(EquipmentExistsException.class)
                .hasMessage(MSG_ERR_EXISTS);

        verify(equipmentRepository).findBySerial(EQP_SERIAL);
        verify(messageSource).get("equipment.error.exists");
    }

    @Test
    @DisplayName("Deveria retornar todos equipamentos.")
    void findAll_WhenExists_ShouldReturnAllEquipments() {
        List<Equipment> list = List.of(eqp1, eqp2);
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<Equipment> page = new PageImpl<>(list, pageable, EXPECTED_TOTAL_ELEMENTS);
        when(equipmentRepository.findAll(PAGE_NUMBER, PAGE_SIZE)).thenReturn(page);
        Page<Equipment> equipments = equipmentsService.findAll(PAGE_NUMBER, PAGE_SIZE);
        assertNotNull(equipments);
        assertEquals(EXPECTED_TOTAL_ELEMENTS, equipments.getContent().size());
        assertEquals(EQP1_SERIAL, equipments.getContent().get(0).getSerial());
        assertEquals(EQP2_SERIAL, equipments.getContent().get(1).getSerial());
        verify(equipmentRepository, times(1)).findAll(PAGE_NUMBER, PAGE_SIZE);
    }


    @Test
    @DisplayName("Deveria retornar um equipamento se existe.")
    void findBySerial_WhenExists_ShouldReturnEquipment() {
        when(equipmentRepository.findBySerial(EQP1_SERIAL)).thenReturn(eqp1);
        Equipment equipmentFound = equipmentsService.findBySerial(EQP1_SERIAL);
        assertNotNull(equipmentFound);
        assertEquals(equipmentFound.getSerial(), eqp1.getSerial());
        verify(equipmentRepository, times(1)).findBySerial(EQP1_SERIAL);
    }

    @Test
    @DisplayName("Deveria retornar uma excessão caso o equipamento não exista.")
    void findBySerial_WhenNotExists_ShouldReturnNotFound() {
        when(equipmentRepository.findBySerial(EQP1_SERIAL)).thenReturn(null);
        when(messageSource.get(any())).thenReturn(MSG_ERR_NOT_FOUND);
        NotFoundErrorException exception = assertThrows(NotFoundErrorException.class, () -> {
            equipmentsService.findBySerial(EQP1_SERIAL);
        });
        assertEquals(MSG_ERR_NOT_FOUND, exception.getMessage());
        verify(equipmentRepository).findBySerial(EQP1_SERIAL);
        verify(messageSource).get(any());
    }



}
