package br.com.api.infracoes.unit.features.violations.application;

import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.features.violations.application.ViolationsService;
import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import br.com.api.infracoes.features.violations.dto.ViolationFiltersRequestDto;
import br.com.api.infracoes.features.violations.exceptions.ViolationNotActiveException;
import br.com.api.infracoes.shared.domain.entities.Equipment;
import br.com.api.infracoes.shared.domain.entities.Violation;
import br.com.api.infracoes.shared.domain.repositories.ViolationRepository;
import br.com.api.infracoes.shared.exceptions.NotFoundErrorException;
import br.com.api.infracoes.shared.util.FileStorageService;
import br.com.api.infracoes.shared.util.HeaderService;
import br.com.api.infracoes.shared.util.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViolationsServiceTest {

    @InjectMocks
    private ViolationsService violationsService;

    @Mock
    private EquipmentsService equipmentsService;

    @Mock
    private ViolationRepository violationRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MessageService messageSource;

    @Mock
    private HeaderService headerService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile mockFile;

    private CreateViolationRequestDto violationDto;
    private Equipment activeEquipment;
    private Equipment inactiveEquipment;
    private Violation violation;

    @BeforeEach
    void setUp() {
        violationDto = new CreateViolationRequestDto();
        violationDto.setSerial("EQ123456");
        violationDto.setOccurrenceDateUtc("2024-01-15T10:30:00Z");
        violationDto.setPicture(mockFile);

        activeEquipment = new Equipment();
        activeEquipment.setSerial("EQ123456");
        activeEquipment.setActive(true);

        inactiveEquipment = new Equipment();
        inactiveEquipment.setSerial("EQ123456");
        inactiveEquipment.setActive(false);

        violation = new Violation();
        violation.setSerial("EQ123456");
    }

    @Test
    @DisplayName("Deveria salvar se equipamento ativo e dados válidos.")
    void save_WithActiveEquipmentAndValidData_ShouldSaveViolation() throws IOException {
        String expectedPictureUrl = "http://teste.com.br123.jpg";
        Long expectedViolationId = 1L;
        OffsetDateTime expectedOccurrenceDate = OffsetDateTime.parse("2024-01-15T10:30:00Z");

        when(equipmentsService.findBySerial(violationDto.getSerial())).thenReturn(activeEquipment);
        when(fileStorageService.storeFile(mockFile)).thenReturn(expectedPictureUrl);
        when(objectMapper.convertValue(violationDto, Violation.class)).thenReturn(violation);
        when(violationRepository.save(any(Violation.class))).thenReturn(expectedViolationId);

        violationsService.save(violationDto);

        ArgumentCaptor<Violation> violationCaptor = ArgumentCaptor.forClass(Violation.class);
        verify(violationRepository).save(violationCaptor.capture());

        Violation savedViolation = violationCaptor.getValue();
        assertEquals(expectedPictureUrl, savedViolation.getPicture());
        assertEquals(expectedOccurrenceDate, savedViolation.getOccurrenceDateUtc());

        verify(headerService).setHeader("Location", "/violations/" + expectedViolationId);
        verify(equipmentsService).findBySerial(violationDto.getSerial());
        verify(fileStorageService).storeFile(mockFile);
        verify(objectMapper).convertValue(violationDto, Violation.class);
    }

    @Test
    @DisplayName("Deveria retornar excessão se equipamento não ativo.")
    void save_WithInactiveEquipment_ShouldThrowViolationNotActiveException() {
        String expectedErrorMessage = "Equipamento não ativo.";
        when(equipmentsService.findBySerial(violationDto.getSerial())).thenReturn(inactiveEquipment);
        when(messageSource.get("violation.error.equip.inactive")).thenReturn(expectedErrorMessage);

        ViolationNotActiveException exception = assertThrows(
                ViolationNotActiveException.class,
                () -> violationsService.save(violationDto)
                                                            );

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(equipmentsService).findBySerial(violationDto.getSerial());
        verify(messageSource).get("violation.error.equip.inactive");
        verifyNoInteractions(fileStorageService);
        verifyNoInteractions(violationRepository);
        verifyNoInteractions(headerService);
        verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("Deveria retornar excessão se erro ao salvar a imagem.")
    void save_WhenFileStorageThrowsIOException_ShouldPropagateException() throws IOException {

        IOException fileStorageException = new IOException("Falha ao salvar a imagem");
        when(equipmentsService.findBySerial(violationDto.getSerial())).thenReturn(activeEquipment);
        when(fileStorageService.storeFile(mockFile)).thenThrow(fileStorageException);

        IOException exception = assertThrows(
                IOException.class,
                () -> violationsService.save(violationDto)
                                            );

        assertEquals("Falha ao salvar a imagem", exception.getMessage());

        verify(equipmentsService).findBySerial(violationDto.getSerial());
        verify(fileStorageService).storeFile(mockFile);
        verifyNoInteractions(violationRepository);
        verifyNoInteractions(headerService);
        verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("Deveria retornar notificação se id correto.")
    void findById_WithValidId_ShouldReturnViolation() {
        Long validId = 1L;
        when(violationRepository.findById(validId)).thenReturn(violation);

        Violation result = violationsService.findById(validId);

        assertNotNull(result);
        assertEquals(violation, result);
        assertEquals("EQ123456", result.getSerial());

        verify(violationRepository).findById(validId);
        verifyNoInteractions(messageSource);
    }

    @Test
    @DisplayName("Deveria retornar excessão se ID não existe.")
    void findById_WithNonExistentId_ShouldThrowNotFoundErrorException() {
        Long nonExistentId = 999L;
        String expectedErrorMessage = "Violation not found";

        when(violationRepository.findById(nonExistentId)).thenReturn(null);
        when(messageSource.get("violation.error.notfound")).thenReturn(expectedErrorMessage);

        NotFoundErrorException exception = assertThrows(
                NotFoundErrorException.class,
                () -> violationsService.findById(nonExistentId)
                                                       );

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(violationRepository).findById(nonExistentId);
        verify(messageSource).get("violation.error.notfound");
    }

    @Test
    @DisplayName("Deveria retornar violações com filtros fornecidos.")
    void findAll_shouldReturnViolationsPageWhenValidFiltersProvided() {
        ViolationFiltersRequestDto filtersDto = new ViolationFiltersRequestDto("SERIAL123",
                                                                               OffsetDateTime.parse("2025-07-09T18:42:00Z"),
                                                                               OffsetDateTime.parse("2025-07-09T18:42:00Z"));
        int page = 0;
        int size = 10;

        List<Violation> violations = Arrays.asList(
                new Violation(),
                new Violation()
                                                  );
        Page<Violation> expectedPage = new PageImpl<>(violations, PageRequest.of(page, size), violations.size());

        when(violationRepository.findAll(filtersDto, page, size)).thenReturn(expectedPage);

        Page<Violation> result = violationsService.findAll(filtersDto, page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(page, result.getNumber());
        assertEquals(size, result.getSize());

        verify(equipmentsService, times(1)).findBySerial("SERIAL123");
        verify(violationRepository, times(1)).findAll(filtersDto, page, size);
    }

    @Test
    @DisplayName("Deveria chamar equipmentsService.findBySerial com o serial correto do equipamento.")
    void shouldCallEquipmentsServiceWithCorrectSerial() {
        ViolationFiltersRequestDto filtersDto = new ViolationFiltersRequestDto("TEST_SERIAL",
                                                                               OffsetDateTime.parse("2025-07-09T18:42:00Z"),
                                                                               OffsetDateTime.parse("2025-07-09T18:42:00Z"));
        int page = 0;
        int size = 5;

        Page<Violation> mockPage = new PageImpl<>(List.of(), PageRequest.of(page, size), 0);
        when(violationRepository.findAll(filtersDto, page, size)).thenReturn(mockPage);
        violationsService.findAll(filtersDto, page, size);
        verify(equipmentsService, times(1)).findBySerial("TEST_SERIAL");
    }


}