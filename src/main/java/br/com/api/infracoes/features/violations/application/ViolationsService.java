package br.com.api.infracoes.features.violations.application;

import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.features.violations.exceptions.ViolationNotActiveException;
import br.com.api.infracoes.shared.domain.entities.Equipment;
import br.com.api.infracoes.shared.domain.entities.Violation;
import br.com.api.infracoes.shared.domain.repositories.ViolationRepository;
import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import br.com.api.infracoes.shared.util.FileStorageService;
import br.com.api.infracoes.shared.util.HeaderService;
import br.com.api.infracoes.shared.util.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ViolationsService {

    private final EquipmentsService equipmentsService;
    private final ViolationRepository violationRepository;

    private final ObjectMapper objectMapper;
    private final MessageService messageSource;
    private final HeaderService headerService;
    private final FileStorageService fileStorageService;

    public void save(CreateViolationRequestDto violationDto) throws IOException {
        Equipment equipment = equipmentsService.findBySerial(violationDto.getSerial());
        if (!equipment.getActive()) {
            throw new ViolationNotActiveException(messageSource.get("violation.error.equip.inactive"));
        }

        String pictureUrl = fileStorageService.storeFile(violationDto.getPicture());

        Violation violation = objectMapper.convertValue(violationDto, Violation.class);
        violation.setOccurrenceDateUtc(OffsetDateTime.parse(violationDto.getOccurrenceDateUtc()));
        violation.setPicture(pictureUrl);

        Long id = violationRepository.save(violation);
        headerService.setHeader("Location", "/violations/" + id);
    }

}
