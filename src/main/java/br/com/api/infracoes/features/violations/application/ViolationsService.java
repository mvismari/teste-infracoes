package br.com.api.infracoes.features.violations.application;

import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.features.violations.dto.ViolationFiltersRequestDto;
import br.com.api.infracoes.features.violations.exceptions.ViolationNotActiveException;
import br.com.api.infracoes.shared.domain.entities.Equipment;
import br.com.api.infracoes.shared.domain.entities.Violation;
import br.com.api.infracoes.shared.domain.repositories.ViolationRepository;
import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import br.com.api.infracoes.shared.exceptions.NotFoundErrorException;
import br.com.api.infracoes.shared.util.FileStorageManager;
import br.com.api.infracoes.shared.util.HeaderHelper;
import br.com.api.infracoes.shared.util.MessageHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ViolationsService {

    private final EquipmentsService equipmentsService;
    private final ViolationRepository violationRepository;

    private final ObjectMapper objectMapper;
    private final MessageHelper messageSource;
    private final HeaderHelper headerHelper;
    private final FileStorageManager fileStorageManager;

    public void save(CreateViolationRequestDto violationDto) throws IOException {
        Equipment equipment = equipmentsService.findBySerial(violationDto.getSerial());
        if (!equipment.getActive()) {
            throw new ViolationNotActiveException(messageSource.get("violation.error.equip.inactive"));
        }

        String pictureUrl = fileStorageManager.storeFile(violationDto.getPicture());

        Violation violation = objectMapper.convertValue(violationDto, Violation.class);
        violation.setOccurrenceDateUtc(OffsetDateTime.parse(violationDto.getOccurrenceDateUtc()));
        violation.setPicture(pictureUrl);

        Long id = violationRepository.save(violation);
        headerHelper.setHeader("Location", "/violations/" + id);
    }

    public Violation findById(Long id) {
        Violation violation = violationRepository.findById(id);
        if (null == violation)
            throw new NotFoundErrorException(messageSource.get("violation.error.notfound"));
        return violation;
    }

    public Page<Violation> findAll(ViolationFiltersRequestDto violationFiltersRequestDto, int page, int size) {
        equipmentsService.findBySerial(violationFiltersRequestDto.serial());
        return violationRepository.findAll(violationFiltersRequestDto, page, size);
    }

}
