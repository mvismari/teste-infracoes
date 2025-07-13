package br.com.api.infracoes.unit.features.violations.infrastructure.postgres;

import br.com.api.infracoes.features.violations.dto.ViolationFiltersRequestDto;
import br.com.api.infracoes.features.violations.infrastructure.postgres.ViolationEntity;
import br.com.api.infracoes.features.violations.infrastructure.postgres.ViolationJpaRepository;
import br.com.api.infracoes.features.violations.infrastructure.postgres.ViolationMapperJpa;
import br.com.api.infracoes.features.violations.infrastructure.postgres.ViolationRepositoryImpl;
import br.com.api.infracoes.shared.domain.entities.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViolationRepositoryImplTest {

    @Mock
    private ViolationJpaRepository violationJpaRepository;

    @Mock
    private ViolationMapperJpa violationMapperJpa;

    @InjectMocks
    private ViolationRepositoryImpl violationRepository;

    private Violation violation;
    private ViolationEntity violationEntity;
    private ViolationEntity savedViolationEntity;
    private Page<ViolationEntity> entityPage;
    private Pageable pageable;
    private List<ViolationEntity> violationEntities;
    private List<Violation> violations;
    private ViolationFiltersRequestDto filtersDto;

    private static final Long EXISTING_ID = 123L;
    private static final String VIOLATION_SERIAL = "ABC-DEF";
    private static final String VIOLATION_OCCURRENCE_DATE = "2024-01-15T10:30:00Z";
    private static final Double VIOLATION_SPEED_MEASURED = 100.0;
    private static final Double VIOLATION_SPEED_CONSIDERED = 90.0;
    private static final Double VIOLATION_SPEED_REGULATED = 70.0;
    private static final String VIOLATION_FILE_NAME = "picture";
    private static final String VIOLATION_FILE_ORIGINAL_FILENAME = "test-image.jpg";
    private static final String VIOLATION_FILE_TYPE = "image/jpeg";


    @BeforeEach
    void setUp() {
        filtersDto = createFiltersDto();
        violation = createViolation();
        violations = createViolations();
        violationEntity = createViolationEntity();
        savedViolationEntity = createViolationEntity();
        pageable = PageRequest.of(0, 10);
        violationEntities = createViolationEntities();
        entityPage = new PageImpl<>(violationEntities, pageable, violationEntities.size());
    }

    @Test
    @DisplayName("Deveria salvar a violação e retornar o ID")
    void shouldSaveViolationAndReturnGeneratedId() {
        when(violationMapperJpa.toEntity(violation)).thenReturn(violationEntity);
        when(violationJpaRepository.save(violationEntity)).thenReturn(savedViolationEntity);

        Long result = violationRepository.save(violation);

        assertNotNull(result);
        assertEquals(1235L, result);

        verify(violationMapperJpa, times(1)).toEntity(violation);
        verify(violationJpaRepository, times(1)).save(violationEntity);
    }

    @Test
    @DisplayName("Retorna a violação se existe.")
    void shouldReturnViolationWhenEntityExists() {
        when(violationJpaRepository.findById(EXISTING_ID)).thenReturn(Optional.of(violationEntity));
        when(violationMapperJpa.toDomain(violationEntity)).thenReturn(violation);

        Violation result = violationRepository.findById(EXISTING_ID);

        assertNotNull(result);
        assertEquals(VIOLATION_SERIAL, result.getSerial());

        verify(violationJpaRepository, times(1)).findById(EXISTING_ID);
        verify(violationMapperJpa, times(1)).toDomain(violationEntity);
    }


    private Violation createViolation() {
        Violation violation = new Violation();
        MockMultipartFile mockFile = new MockMultipartFile(
                VIOLATION_FILE_NAME,
                VIOLATION_FILE_ORIGINAL_FILENAME,
                VIOLATION_FILE_TYPE,
                "test image content".getBytes()
        );

        violation.setSerial(VIOLATION_SERIAL);
        violation.setOccurrenceDateUtc(OffsetDateTime.parse(VIOLATION_OCCURRENCE_DATE));
        violation.setMeasuredSpeed(VIOLATION_SPEED_MEASURED);
        violation.setConsideredSpeed(VIOLATION_SPEED_CONSIDERED);
        violation.setRegulatedSpeed(VIOLATION_SPEED_REGULATED);
        violation.setPicture("http://teste.com.br");
        violation.setType("Velocity");
        return violation;
    }


    private ViolationEntity createViolationEntity() {
        ViolationEntity violation = new ViolationEntity();
        violation.setId(1235L);
        violation.setEquipmentsSerialId(VIOLATION_SERIAL);
        violation.setOccurrenceDate(OffsetDateTime.parse(VIOLATION_OCCURRENCE_DATE));
        violation.setMeasuredSpeed(VIOLATION_SPEED_MEASURED);
        violation.setConsideredSpeed(VIOLATION_SPEED_CONSIDERED);
        violation.setRegulatedSpeed(VIOLATION_SPEED_REGULATED);
        violation.setPicture("http://teste.com.br");
        violation.setType("Velocity");
        return violation;
    }

    private List<ViolationEntity> createViolationEntities() {
        ViolationEntity entity1 = new ViolationEntity();
        entity1.setId(1L);

        ViolationEntity entity2 = new ViolationEntity();
        entity2.setId(2L);

        return Arrays.asList(entity1, entity2);
    }

    private List<Violation> createViolations() {
        Violation violation1 = new Violation();
        violation1.setSerial("ABC-DEF");

        Violation violation2 = new Violation();
        violation2.setSerial("ABC-DEF");

        return Arrays.asList(violation1, violation2);
    }

    private ViolationFiltersRequestDto createFiltersDto() {
        return new ViolationFiltersRequestDto(
                "ABC-DEV",
                OffsetDateTime.parse("2025-07-09T18:42:00Z"),
                OffsetDateTime.parse("2025-07-09T18:42:00Z")
        );
    }







}
