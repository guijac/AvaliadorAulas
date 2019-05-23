package br.edu.pucsp.avaliador.dao;

import br.edu.pucsp.avaliador.entities.DisciplinaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DisciplinaRepository extends MongoRepository<DisciplinaEntity, String> {

    DisciplinaEntity findByCodigo(String codigo);
    DisciplinaEntity findByNome(String nome);
}
