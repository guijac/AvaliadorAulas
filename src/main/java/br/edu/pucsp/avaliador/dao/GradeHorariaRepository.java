package br.edu.pucsp.avaliador.dao;

import br.edu.pucsp.avaliador.entities.GradeHorariaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeHorariaRepository extends MongoRepository<GradeHorariaEntity, String> {
}
