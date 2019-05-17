package br.edu.pucsp.avaliador.dao;

import br.edu.pucsp.avaliador.dto.MembroAcademicoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MembroAcademicoRepository<E extends MembroAcademicoEntity> extends MongoRepository<E, String>{

    public Optional<E> findByRegistroAcademico(String registroAcademico);
}
