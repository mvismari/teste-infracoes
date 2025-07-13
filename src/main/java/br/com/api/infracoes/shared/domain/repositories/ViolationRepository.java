package br.com.api.infracoes.shared.domain.repositories;

import br.com.api.infracoes.features.violations.dto.ViolationFiltersRequestDto;
import br.com.api.infracoes.shared.domain.entities.Violation;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;

public interface ViolationRepository {
    Long save(Violation violation);
    Violation findById(Long id);
    Page<Violation> findAll(ViolationFiltersRequestDto violationFiltersRequestDto, int page, int size);

}