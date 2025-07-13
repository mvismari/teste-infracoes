package br.com.api.infracoes.unit.features.violations.controller;

import br.com.api.infracoes.features.violations.application.ViolationsService;
import br.com.api.infracoes.features.violations.controller.ViolationsController;
import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import br.com.api.infracoes.features.violations.dto.ViolationFiltersRequestDto;
import br.com.api.infracoes.shared.domain.entities.Violation;
import br.com.api.infracoes.shared.exceptions.NotFoundErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViolationsControllerTest {

    @InjectMocks
    private ViolationsController violationsController;

    @Mock
    private ViolationsService violationsService;

    private static final Long VIOLATION_ID = 123L;
    private static final String VIOLATION_SERIAL = "ABC-DEF";
    private static final String VIOLATION_OCCURRENCE_DATE = "2024-01-15T10:30:00Z";
    private static final Double VIOLATION_SPEED_MEASURED = 100.0;
    private static final Double VIOLATION_SPEED_CONSIDERED = 90.0;
    private static final Double VIOLATION_SPEED_REGULATED = 70.0;
    private static final String VIOLATION_FILE_NAME = "picture";
    private static final String VIOLATION_FILE_ORIGINAL_FILENAME = "test-image.jpg";
    private static final String VIOLATION_FILE_TYPE = "image/jpeg";

    private CreateViolationRequestDto createValidViolationRequestDto() {
        MockMultipartFile mockFile = new MockMultipartFile(
                VIOLATION_FILE_NAME,
                VIOLATION_FILE_ORIGINAL_FILENAME,
                VIOLATION_FILE_TYPE,
                "test image content".getBytes()
        );

        CreateViolationRequestDto dto = new CreateViolationRequestDto();
        dto.setSerial(VIOLATION_SERIAL);
        dto.setOccurrenceDateUtc(VIOLATION_OCCURRENCE_DATE);
        dto.setMeasuredSpeed(VIOLATION_SPEED_MEASURED);
        dto.setConsideredSpeed(VIOLATION_SPEED_CONSIDERED);
        dto.setRegulatedSpeed(VIOLATION_SPEED_REGULATED);
        dto.setPicture(mockFile);
        dto.setType(CreateViolationRequestDto.TypeViolations.Velocity);

        return dto;
    }

    private Violation createViolation() {
        Violation violation = new Violation();
        violation.setSerial(VIOLATION_SERIAL);
        return violation;
    }

    @Test
    @DisplayName("Deveria retornar status 201 após salvar a notificação.")
    void save_WhenSave_ShouldReturnCreatedStatus() throws IOException {
        CreateViolationRequestDto violationRequestDto = createValidViolationRequestDto();
        doNothing().when(violationsService).save(violationRequestDto);
        ResponseEntity<Void> response = violationsController.save(violationRequestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
        verify(violationsService, times(1)).save(violationRequestDto);
    }

    @Test
    @DisplayName("Deveria retornarIOException se erro ao salvar a imagem.")
    void save_WhenIOException_ShouldPropagateException() throws IOException {

        CreateViolationRequestDto violationRequestDto = createValidViolationRequestDto();

        doThrow(new IOException("File processing error"))
                .when(violationsService).save(any(CreateViolationRequestDto.class));

        IOException exception = assertThrows(IOException.class, () -> {
            violationsController.save(violationRequestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("File processing error");
        verify(violationsService, times(1)).save(violationRequestDto);
    }

    @Test
    @DisplayName("Deveria retornar uma violação caso exista.")
    void findById_IfExists_ShouldReturnViolation() {
        Violation violation = createViolation();
        when(violationsService.findById(VIOLATION_ID)).thenReturn(violation);
        ResponseEntity<Violation> response = violationsController.findById(VIOLATION_ID);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        Violation body = response.getBody();
        assertNotNull(body);
        assertEquals(VIOLATION_SERIAL, body.getSerial());
    }

    @Test
    @DisplayName("Deveria retornar uma excessão caso a violação não exista.")
    void findById_IfNotExists_ShouldReturnNotFoundException() {
        doThrow(new NotFoundErrorException("Violação não encontrado."))
                .when(violationsService)
                .findById(VIOLATION_ID);

        NotFoundErrorException exception = assertThrows(NotFoundErrorException.class, () -> {
            violationsController.findById(VIOLATION_ID);
        });

        assertThat(exception).isInstanceOf(NotFoundErrorException.class);
        assertThat(exception.getMessage()).isEqualTo("Violação não encontrado.");

        verify(violationsService).findById(VIOLATION_ID);
    }

    @Test
    void findAllOrFilter_WithAllParameters_ShouldReturnOkWithViolations() {

        Violation violationDto1 = createViolation();
        Violation violationDto2 = createViolation();
        int page = 0;
        int size = 10;
        OffsetDateTime from = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        OffsetDateTime to = OffsetDateTime.parse("2024-12-31T23:59:59Z");

        List<Violation> violations = Arrays.asList(violationDto1, violationDto2);
        Page<Violation> violationPage = new PageImpl<>(violations);

        when(violationsService.findAll(any(ViolationFiltersRequestDto.class), eq(page), eq(size)))
                .thenReturn(violationPage);

        ResponseEntity<Page<Violation>> response = violationsController
                .findAllOrFilter(VIOLATION_SERIAL, page, size, from, to);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals(violationDto1, response.getBody().getContent().get(0));
        assertEquals(violationDto2, response.getBody().getContent().get(1));

        verify(violationsService, times(1)).findAll(
                argThat(dto -> dto.serial().equals(VIOLATION_SERIAL) &&
                        dto.from().equals(from) &&
                        dto.to().equals(to)),
                eq(page),
                eq(size)
                                                   );
    }

}
