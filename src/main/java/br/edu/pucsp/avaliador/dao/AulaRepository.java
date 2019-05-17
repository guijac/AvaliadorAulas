package br.edu.pucsp.avaliador.dao;

import br.edu.pucsp.avaliador.dto.AulaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AulaRepository  extends MongoRepository<AulaEntity, String> {
}
