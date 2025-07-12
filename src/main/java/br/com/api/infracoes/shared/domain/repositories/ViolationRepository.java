package br.com.api.infracoes.shared.domain.repositories;

import br.com.api.infracoes.shared.domain.entities.Violation;

public interface ViolationRepository {
    Long save(Violation violation);
    //Equipment findBySerial(String serial);
    //Page<Equipment> findAll(int page, int size);

}