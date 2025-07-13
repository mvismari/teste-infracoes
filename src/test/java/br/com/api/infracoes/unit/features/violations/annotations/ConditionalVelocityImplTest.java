package br.com.api.infracoes.unit.features.violations.annotations;

import br.com.api.infracoes.features.violations.annotations.ConditionalVelocityImpl;
import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConditionalVelocityImplTest {

    private ConditionalVelocityImpl validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;


    @BeforeEach
    void setUp() {
        validator = new ConditionalVelocityImpl();
    }

    @Test
    void isValid_ShouldReturnTrue_WhenDtoIsNull() {
        // Given
        CreateViolationRequestDto dto = null;

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertTrue(result);
        verifyNoInteractions(context);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenTypeIsNotVelocity() {
        // Given
        CreateViolationRequestDto dto = new CreateViolationRequestDto();
        dto.setType(CreateViolationRequestDto.TypeViolations.StopCrossWalking); // Assuming there's another type

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertTrue(result);
        verifyNoInteractions(context);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenAllVelocityFieldsAreValid() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertTrue(result);
        verifyNoInteractions(context);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenMeasuredSpeedIsNull() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setMeasuredSpeed(null);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Velocidade medida é obrigatória e deve ser maior que 0");
        verify(violationBuilder).addPropertyNode("measuredSpeed");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenMeasuredSpeedIsZero() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setMeasuredSpeed(0.0);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Velocidade medida é obrigatória e deve ser maior que 0");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenMeasuredSpeedIsNegative() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setMeasuredSpeed(-10.0);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Velocidade medida é obrigatória e deve ser maior que 0");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenConsideredSpeedIsNull() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setConsideredSpeed(null);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Velocidade considerada é obrigatória e deve ser maior que 0");
        verify(violationBuilder).addPropertyNode("consideredSpeed");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenConsideredSpeedIsZero() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setConsideredSpeed(0.0);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Velocidade considerada é obrigatória e deve ser maior que 0");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenConsideredSpeedIsNegative() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setConsideredSpeed(-5.0);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Velocidade considerada é obrigatória e deve ser maior que 0");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRegulatedSpeedIsNull() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setRegulatedSpeed(null);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Velocidade regulamentada é obrigatória e deve ser maior que 0");
        verify(violationBuilder).addPropertyNode("regulatedSpeed");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRegulatedSpeedIsZero() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setRegulatedSpeed(0.0);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Velocidade regulamentada é obrigatória e deve ser maior que 0");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRegulatedSpeedIsNegative() {
        // Given
        CreateViolationRequestDto dto = createValidVelocityDto();
        dto.setRegulatedSpeed(-20.0);

        setupMockContext();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Velocidade regulamentada é obrigatória e deve ser maior que 0");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenMultipleFieldsAreInvalid() {
        // Given
        CreateViolationRequestDto dto = new CreateViolationRequestDto();
        dto.setType(CreateViolationRequestDto.TypeViolations.Velocity);
        dto.setMeasuredSpeed(null);
        dto.setConsideredSpeed(0.0);
        dto.setRegulatedSpeed(-10.0);

        setupMockContextForMultipleViolations();

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Velocidade medida é obrigatória e deve ser maior que 0");
        verify(context).buildConstraintViolationWithTemplate("Velocidade considerada é obrigatória e deve ser maior que 0");
        verify(context).buildConstraintViolationWithTemplate("Velocidade regulamentada é obrigatória e deve ser maior que 0");
    }

    @Test
    void isValid_ShouldReturnTrue_WhenAllSpeedsArePositive() {
        // Given
        CreateViolationRequestDto dto = new CreateViolationRequestDto();
        dto.setType(CreateViolationRequestDto.TypeViolations.Velocity);
        dto.setMeasuredSpeed(80.0);
        dto.setConsideredSpeed(75.0);
        dto.setRegulatedSpeed(60.0);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertTrue(result);
        verifyNoInteractions(context);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenSpeedsAreVerySmallPositiveNumbers() {
        // Given
        CreateViolationRequestDto dto = new CreateViolationRequestDto();
        dto.setType(CreateViolationRequestDto.TypeViolations.Velocity);
        dto.setMeasuredSpeed(0.1);
        dto.setConsideredSpeed(0.01);
        dto.setRegulatedSpeed(0.001);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertTrue(result);
        verifyNoInteractions(context);
    }

    @Test
    void isValid_ShouldHandleEdgeCaseWithVeryLargeNumbers() {
        // Given
        CreateViolationRequestDto dto = new CreateViolationRequestDto();
        dto.setType(CreateViolationRequestDto.TypeViolations.Velocity);
        dto.setMeasuredSpeed(Double.MAX_VALUE);
        dto.setConsideredSpeed(Double.MAX_VALUE);
        dto.setRegulatedSpeed(Double.MAX_VALUE);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertTrue(result);
    }

    private CreateViolationRequestDto createValidVelocityDto() {
        CreateViolationRequestDto dto = new CreateViolationRequestDto();
        dto.setType(CreateViolationRequestDto.TypeViolations.Velocity);
        dto.setMeasuredSpeed(80.0);
        dto.setConsideredSpeed(75.0);
        dto.setRegulatedSpeed(60.0);
        return dto;
    }

    private void setupMockContext() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    private void setupMockContextForMultipleViolations() {



        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    // Note: You'll need to make getErrorMessage method package-private or add a getter method
    // to test it directly, or you can remove these tests if the method should remain private
}
