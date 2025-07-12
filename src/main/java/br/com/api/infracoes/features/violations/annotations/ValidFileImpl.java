package br.com.api.infracoes.features.violations.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ValidFileImpl implements ConstraintValidator<ValidFile, MultipartFile> {

    private long maxSize;
    private List<String> allowedExtensions;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowedExtensions = Arrays.stream(constraintAnnotation.allowedTypes())
                .map(String::toLowerCase)
                .toList();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file.isEmpty() || file.getSize() > maxSize) {
            return false;
        }
        String filename = file.getOriginalFilename();
        return filename != null && allowedExtensions.stream()
                .anyMatch(ext -> filename.toLowerCase().endsWith(ext));
    }
}