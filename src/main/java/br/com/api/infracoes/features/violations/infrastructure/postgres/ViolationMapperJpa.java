package br.com.api.infracoes.features.violations.infrastructure.postgres;

import br.com.api.infracoes.shared.domain.entities.Violation;
import org.springframework.stereotype.Component;

@Component
public class ViolationMapperJpa {

    public ViolationEntity toEntity(Violation domain) {
        ViolationEntity entity = new ViolationEntity();
        entity.setEquipmentsSerialId(domain.getSerial());
        entity.setOccurrenceDate(domain.getOccurrenceDateUtc());
        entity.setMeasuredSpeed(domain.getMeasuredSpeed());
        entity.setConsideredSpeed(domain.getConsideredSpeed());
        entity.setRegulatedSpeed(domain.getRegulatedSpeed());
        entity.setPicture(domain.getPicture());
        entity.setType(domain.getType());


        return entity;
    }

    public Violation toDomain(ViolationEntity entity) {
        Violation violation = new Violation();
        violation.setSerial(entity.getEquipmentsSerialId());
        violation.setOccurrenceDateUtc(entity.getOccurrenceDate());
        violation.setMeasuredSpeed(entity.getMeasuredSpeed());
        violation.setConsideredSpeed(entity.getConsideredSpeed());
        violation.setRegulatedSpeed(entity.getRegulatedSpeed());
        violation.setPicture(entity.getPicture());
        violation.setType(entity.getType());
        return violation;
    }
}
