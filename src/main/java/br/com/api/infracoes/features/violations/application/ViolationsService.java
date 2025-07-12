package br.com.api.infracoes.features.violations.application;

import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.features.violations.exceptions.ViolationNotActiveException;
import br.com.api.infracoes.shared.domain.entities.Equipment;
import br.com.api.infracoes.shared.domain.entities.Violation;
import br.com.api.infracoes.shared.domain.repositories.ViolationRepository;
import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import br.com.api.infracoes.shared.util.HeaderService;
import br.com.api.infracoes.shared.util.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ViolationsService {

    private final EquipmentsService equipmentsService;
    private final ViolationRepository violationRepository;
    private final ObjectMapper objectMapper;
    private final MessageService messageSource;
    private final HeaderService headerService;

    public void save(CreateViolationRequestDto violationDto) {

        Equipment equipment = equipmentsService.findBySerial(violationDto.getSerial());
        if(!equipment.getActive())
            throw new ViolationNotActiveException(messageSource.get("violation.error.equip.inactive"));

        OffsetDateTime dateFormatted = OffsetDateTime.parse(violationDto.getOccurrenceDateUtc());
        Violation violation = objectMapper.convertValue(violationDto, Violation.class);
        violation.setOccurrenceDateUtc(dateFormatted);
        violation.setPicture("http://teste.com.br"); // tmp
        Long id = violationRepository.save(violation);

        addHeader(id);
    }

    private void addHeader(Long id) {
        headerService.setHeader("Location", "/violations/" + id);
    }

}
