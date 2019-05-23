package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.entities.AlunoEntity;
import br.edu.pucsp.avaliador.entities.AvaliacaoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AvaliacaoFactory {
    private AlunoService alunoService;

    @Autowired
    public AvaliacaoFactory(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    public Optional<AvaliacaoEntity> criarAvaliacao(String registroAcademico, Integer nrEstrelas) {
        Optional<AlunoEntity> aluno = alunoService.encontraPorRegistroAcademico(registroAcademico);
        return aluno.map(value -> new AvaliacaoEntity(value, nrEstrelas));
    }
}
