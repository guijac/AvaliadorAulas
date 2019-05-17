package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dao.CounterService;
import br.edu.pucsp.avaliador.dao.ProfessorRepository;
import br.edu.pucsp.avaliador.dto.DisciplinaEntity;
import br.edu.pucsp.avaliador.dto.ProfessorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorFactory extends MembroAcademicoFactory<ProfessorEntity> {
    @Autowired
    protected ProfessorFactory(CounterService counterService, ProfessorRepository repository) {
        super(counterService, repository);
    }

    public ProfessorEntity contratar(String nome, String sobreNome, List<DisciplinaEntity> disciplinasApto) {
        return super.criar(new ProfessorEntity(nome, sobreNome, disciplinasApto));
    }
}
