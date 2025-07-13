package br.com.api.infracoes.unit.features.violations.infrastructure.postgres;

import br.com.api.infracoes.features.violations.infrastructure.postgres.ViolationEntity;
import br.com.api.infracoes.features.violations.infrastructure.postgres.ViolationMapperJpa;
import br.com.api.infracoes.shared.domain.entities.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ViolationMapperJpaTest {

    @InjectMocks
    private ViolationMapperJpa violationMapperJpa;

    private static final String VIOLATION_SERIAL = "ABC-DEF";
    private static final String VIOLATION_OCCURRENCE_DATE = "2024-01-15T10:30:00Z";
    private static final Double VIOLATION_SPEED_MEASURED = 100.0;
    private static final Double VIOLATION_SPEED_CONSIDERED = 90.0;
    private static final Double VIOLATION_SPEED_REGULATED = 70.0;
    private static final String VIOLATION_FILE_NAME = "picture";
    private static final String VIOLATION_FILE_ORIGINAL_FILENAME = "test-image.jpg";
    private static final String VIOLATION_FILE_TYPE = "image/jpeg";

    @Test
    @DisplayName("Deve converter Domínio para Entidade.")
    void toEntity_WithAllRequiredFields_ShouldBeValid() {
        Violation violation = createValidViolation();
        ViolationEntity equipmentEntity = violationMapperJpa.toEntity(violation);

        assertEquals(equipmentEntity.getEquipmentsSerialId(), violation.getSerial());
        assertEquals(equipmentEntity.getOccurrenceDate(), violation.getOccurrenceDateUtc());
        assertEquals(equipmentEntity.getMeasuredSpeed(), violation.getMeasuredSpeed());
        assertEquals(equipmentEntity.getConsideredSpeed(), violation.getConsideredSpeed());
        assertEquals(equipmentEntity.getRegulatedSpeed(), violation.getRegulatedSpeed());
        assertEquals(equipmentEntity.getPicture(), violation.getPicture());
        assertEquals(equipmentEntity.getType(), violation.getType());
    }

    @Test
    @DisplayName("Deve converter Entidade para Dominio.")
    void toDomain_WithAllRequiredFields_ShouldBeValid() {
        ViolationEntity violationEntity = createValidViolationEntity();
        Violation violation = violationMapperJpa.toDomain(violationEntity);

        assertEquals(violationEntity.getEquipmentsSerialId(), violation.getSerial());
        assertEquals(violationEntity.getOccurrenceDate(), violation.getOccurrenceDateUtc());
        assertEquals(violationEntity.getMeasuredSpeed(), violation.getMeasuredSpeed());
        assertEquals(violationEntity.getConsideredSpeed(), violation.getConsideredSpeed());
        assertEquals(violationEntity.getRegulatedSpeed(), violation.getRegulatedSpeed());
        assertEquals(violationEntity.getPicture(), violation.getPicture());
        assertEquals(violationEntity.getType(), violation.getType());
    }

    private Violation createValidViolation() {
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

    private ViolationEntity createValidViolationEntity() {
        ViolationEntity violation = new ViolationEntity();
        violation.setId(123L);
        violation.setEquipmentsSerialId(VIOLATION_SERIAL);
        violation.setOccurrenceDate(OffsetDateTime.parse(VIOLATION_OCCURRENCE_DATE));
        violation.setMeasuredSpeed(VIOLATION_SPEED_MEASURED);
        violation.setConsideredSpeed(VIOLATION_SPEED_CONSIDERED);
        violation.setRegulatedSpeed(VIOLATION_SPEED_REGULATED);
        violation.setPicture("http://teste.com.br");
        violation.setType("Velocity");
        return violation;
    }

}
