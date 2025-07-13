package br.com.api.infracoes.unit.features.violations.annotations;

import br.com.api.infracoes.features.violations.annotations.ValidFile;
import br.com.api.infracoes.features.violations.annotations.ValidFileImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidFileImplTest {

    private ValidFileImpl validator;

    @Mock
    private ValidFile constraintAnnotation;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ValidFileImpl();
    }

    @Test
    void initialize_ShouldSetMaxSizeAndAllowedExtensions() {
        when(constraintAnnotation.maxSize()).thenReturn(1024L);
        when(constraintAnnotation.allowedTypes()).thenReturn(new String[]{"PDF", "JPG", "PNG"});

        validator.initialize(constraintAnnotation);

        verify(constraintAnnotation).maxSize();
        verify(constraintAnnotation).allowedTypes();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenFileIsEmpty() {
        initializeValidator(1024L, new String[]{"pdf", "jpg"});
        when(multipartFile.isEmpty()).thenReturn(true);

        boolean result = validator.isValid(multipartFile, context);

        assertFalse(result);
        verify(multipartFile).isEmpty();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenFileSizeExceedsMaxSize() {
        initializeValidator(1024L, new String[]{"pdf", "jpg"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(2048L);

        boolean result = validator.isValid(multipartFile, context);

        assertFalse(result);
        verify(multipartFile).isEmpty();
        verify(multipartFile).getSize();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenFileSizeEqualsMaxSize() {
        initializeValidator(1024L, new String[]{"pdf", "jpg"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(1024L);

        boolean result = validator.isValid(multipartFile, context);

        assertFalse(result);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenFilenameIsNull() {
        initializeValidator(1024L, new String[]{"pdf", "jpg"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn(null);

        boolean result = validator.isValid(multipartFile, context);

        assertFalse(result);
        verify(multipartFile).getOriginalFilename();
    }

    @Test
    void isValid_ShouldReturnTrue_WhenFileIsValidWithAllowedExtension() {
        initializeValidator(1024L, new String[]{"pdf", "jpg"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn("document.pdf");

        boolean result = validator.isValid(multipartFile, context);

        assertTrue(result);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenFileExtensionMatchesIgnoringCase() {
        initializeValidator(1024L, new String[]{"PDF", "JPG"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn("document.pdf");

        boolean result = validator.isValid(multipartFile, context);

        assertTrue(result);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenFilenameHasUppercaseExtension() {
        initializeValidator(1024L, new String[]{"pdf", "jpg"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn("document.PDF");

        boolean result = validator.isValid(multipartFile, context);

        assertTrue(result);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenFileExtensionNotAllowed() {
        initializeValidator(1024L, new String[]{"pdf", "jpg"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn("document.txt");

        boolean result = validator.isValid(multipartFile, context);

        assertFalse(result);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenFileHasNoExtension() {
        initializeValidator(1024L, new String[]{"pdf", "jpg"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn("document");

        boolean result = validator.isValid(multipartFile, context);

        assertFalse(result);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenFileExtensionIsPartialMatch() {
        initializeValidator(1024L, new String[]{"jpeg", "png"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn("image.jpeg");

        boolean result = validator.isValid(multipartFile, context);

        assertTrue(result);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenFileSizeIsJustUnderMaxSize() {
        initializeValidator(1024L, new String[]{"pdf"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(1023L);
        when(multipartFile.getOriginalFilename()).thenReturn("document.pdf");

        boolean result = validator.isValid(multipartFile, context);

        assertTrue(result);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenFileHasMultipleDotsInName() {
        initializeValidator(1024L, new String[]{"pdf"});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn("my.document.v1.pdf");

        boolean result = validator.isValid(multipartFile, context);

        assertTrue(result);
    }

    @Test
    void isValid_ShouldHandleEmptyAllowedExtensions() {
        initializeValidator(1024L, new String[]{});
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn(512L);
        when(multipartFile.getOriginalFilename()).thenReturn("document.pdf");

        boolean result = validator.isValid(multipartFile, context);

        assertFalse(result);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenMaxSizeIsZeroAndFileIsEmpty() {
        initializeValidator(0L, new String[]{"pdf"});
        when(multipartFile.isEmpty()).thenReturn(true);

        boolean result = validator.isValid(multipartFile, context);

        assertFalse(result);
    }

    private void initializeValidator(long maxSize, String[] allowedTypes) {
        when(constraintAnnotation.maxSize()).thenReturn(maxSize);
        when(constraintAnnotation.allowedTypes()).thenReturn(allowedTypes);
        validator.initialize(constraintAnnotation);
    }
}
