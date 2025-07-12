package br.com.api.infracoes.shared.domain.repositories;

import br.com.api.infracoes.shared.domain.entities.Violation;

public interface ViolationRepository {
    Long save(Violation violation);
    Violation findById(Long id);
    //Page<Equipment> findAll(int page, int size);

}