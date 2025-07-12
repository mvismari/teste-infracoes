package br.com.api.infracoes.unit.features.equipments.web;

import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.features.equipments.domain.Equipment;
import br.com.api.infracoes.features.equipments.dto.CreateEquipmentRequestDto;
import br.com.api.infracoes.features.equipments.exception.EquipmentExistsException;
import br.com.api.infracoes.features.equipments.web.EquipmentsController;
import br.com.api.infracoes.shared.exceptions.NotFoundErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentsControllerTest {

    private CreateEquipmentRequestDto createEquipmentRequestDto;
    private Equipment equipment1;
    private Equipment equipment2;

    @Mock
    private EquipmentsService equipmentsService;

    @InjectMocks
    private EquipmentsController equipmentsController;

    @BeforeEach
    void setUp() {
        createEquipmentRequestDto = new CreateEquipmentRequestDto();
        createEquipmentRequestDto.setSerial("123456789");
        createEquipmentRequestDto.setModel("Modelo");
        createEquipmentRequestDto.setAddress("Endereço aqui");
        createEquipmentRequestDto.setLatitude(BigDecimal.valueOf(90));
        createEquipmentRequestDto.setLongitude(BigDecimal.valueOf(180));
        createEquipmentRequestDto.setActive(true);

        equipment1 = new Equipment();
        equipment1.setSerial("Equip 1");
        equipment1.setModel("Modelo");
        equipment1.setAddress("endereço");

        equipment2 = new Equipment();
        equipment2.setSerial("Equip 2");
        equipment2.setModel("Modelo");
        equipment2.setAddress("endereço");
    }

    @Test
    @DisplayName("Deveria retornar status 201 após salvar o equipamento.")
    void shouldReturnCreatedStatusWhenSaveEquipment() {
        doNothing().when(equipmentsService).save(createEquipmentRequestDto);
        ResponseEntity<Void> response = equipmentsController.save(createEquipmentRequestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
        verify(equipmentsService, times(1)).save(createEquipmentRequestDto);
    }

    @Test
    @DisplayName("Deveria retornar excessão quando o serial do equipamento já existe.")
    void shouldReturnExceptionWhenSaveEquipmentDuplicated() {
        doThrow(new EquipmentExistsException("Equipamento já existe cadastrado.")).when(equipmentsService).save(createEquipmentRequestDto);
        try {
            ResponseEntity<Void> response = equipmentsController.save(createEquipmentRequestDto);
            assertThat(false).isTrue(); // Não pode chegar aqui
        } catch (EquipmentExistsException e){
            assertThat(e).isInstanceOf(EquipmentExistsException.class);
            assertThat(e.getMessage()).isEqualTo("Equipamento já existe cadastrado.");
        }
    }

    @Test
    @DisplayName("Deveria retornar uma lista de equipamentos, caso existe pelo menos um.")
    void shouldReturnListOfEquipmentsIfExists() {
        List<Equipment> list = List.of(equipment1, equipment2);
        Pageable pageable = PageRequest.of(1, 5);
        Page<Equipment> page = new PageImpl<>(list, pageable, 2);

        when(equipmentsService.findAll(pageable)).thenReturn(page);
        ResponseEntity<Page<Equipment>> response = equipmentsController.findAll(1, 5);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        Page<Equipment> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.getContent().size());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(equipmentsService).findAll(pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(1, capturedPageable.getPageNumber());
        assertEquals(5, capturedPageable.getPageSize());
    }

    @Test
    @DisplayName("Deveria retornar uma lista vazia quando não existe nenhum equipamento.")
    void shouldReturnEmptyListOfEquipmentsIfNotExists() {
        List<Equipment> list = List.of();
        Pageable pageable = PageRequest.of(1, 5);
        Page<Equipment> page = new PageImpl<>(list, pageable, 0);

        when(equipmentsService.findAll(pageable)).thenReturn(page);
        ResponseEntity<Page<Equipment>> response = equipmentsController.findAll(1, 5);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        Page<Equipment> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.getContent().size());
        assertEquals(0, responseBody.getTotalElements());
        assertEquals(1, responseBody.getNumber());
        assertEquals(5, responseBody.getSize());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(equipmentsService).findAll(pageableCaptor.capture());
    }

    @Test
    @DisplayName("Deveria retornar um equipamento se existe.")
    void shouldReturnEquipmentIfExists() {
        when(equipmentsService.findBySerial("Equip 1")).thenReturn(equipment1);
        ResponseEntity<Equipment> response = equipmentsController.findBySerial("Equip 1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        Equipment body = response.getBody();
        assertNotNull(body);
        assertEquals("Equip 1", body.getSerial());
    }

    @Test
    @DisplayName("Deveria retornar uma excessão caso o equipamento não exista.")
    void shouldReturnNotFoundWhenEquipmentNotExists() {
        doThrow(new NotFoundErrorException("Equipamento não encontrado."))
                .when(equipmentsService)
                .findBySerial("Equip 1");

        NotFoundErrorException exception = assertThrows(NotFoundErrorException.class, () -> {
            equipmentsController.findBySerial("Equip 1");
        });

        assertThat(exception).isInstanceOf(NotFoundErrorException.class);
        assertThat(exception.getMessage()).isEqualTo("Equipamento não encontrado.");

        verify(equipmentsService).findBySerial("Equip 1");
    }


}
