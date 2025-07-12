package br.com.api.infracoes.features.equipments.application;

import br.com.api.infracoes.shared.domain.entities.Equipment;
import br.com.api.infracoes.shared.domain.repositories.EquipmentRepository;
import br.com.api.infracoes.features.equipments.dto.CreateEquipmentRequestDto;
import br.com.api.infracoes.features.equipments.exceptions.EquipmentExistsException;
import br.com.api.infracoes.shared.exceptions.NotFoundErrorException;
import br.com.api.infracoes.shared.util.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EquipmentsService {

    private final EquipmentRepository equipmentRepository;
    private final ObjectMapper objectMapper;
    private final MessageService messageSource;

    public void save(CreateEquipmentRequestDto equipmentDto) {
        if (null != equipmentRepository.findBySerial(equipmentDto.getSerial())) {
            throw new EquipmentExistsException(messageSource.get("equipment.error.exists"));
        }

        Equipment equipment = objectMapper.convertValue(equipmentDto, Equipment.class);
        equipmentRepository.save(equipment);
    }

    public Page<Equipment> findAll(int page, int size) {
        return equipmentRepository.findAll(page, size);
    }

    public Equipment findBySerial(String serial) {
        Equipment equipment = equipmentRepository.findBySerial(serial);
        if(null == equipment)
            throw new NotFoundErrorException(messageSource.get("equipment.error.notfound"));
        return equipment;
    }


}
