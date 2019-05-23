package br.edu.pucsp.avaliador.dao;

import br.edu.pucsp.avaliador.entities.MembroAcademicoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MembroAcademicoRepository<E extends MembroAcademicoEntity> extends MongoRepository<E, String>{

    public Optional<E> findByRegistroAcademico(String registroAcademico);
}
