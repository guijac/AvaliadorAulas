package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dto.AlunoEntity;
import br.edu.pucsp.avaliador.dto.AvaliacaoEntity;
import br.edu.pucsp.avaliador.model.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AvaliacaoFactory {
    private AlunoFactory alunoFactory;

    @Autowired
    public AvaliacaoFactory(AlunoFactory alunoFactory) {
        this.alunoFactory = alunoFactory;
    }

    public Optional<AvaliacaoEntity> criarAvaliacao(String registroAcademico, Integer nrEstrelas) {
        Optional<AlunoEntity> aluno = alunoFactory.encontraPorRegistroAcademico(registroAcademico);
        return aluno.map(value -> new AvaliacaoEntity(value, nrEstrelas));
    }
}
