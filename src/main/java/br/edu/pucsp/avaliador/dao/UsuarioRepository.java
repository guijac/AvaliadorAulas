package br.edu.pucsp.avaliador.dao;

import br.edu.pucsp.avaliador.entities.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario,String> {
    Optional<Usuario> findByNomeDeUsuario(String nomeDeUsuario);
}
