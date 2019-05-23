package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dao.CordenadorRepository;
import br.edu.pucsp.avaliador.dao.CounterService;
import br.edu.pucsp.avaliador.dao.MembroAcademicoRepository;
import br.edu.pucsp.avaliador.entities.AlunoEntity;
import br.edu.pucsp.avaliador.entities.CordenadorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CordenadorService extends MembroAcademicoFactory<CordenadorEntity> {

    private DisciplinaFactory disciplinaFactory;
    private AulaService aulaService;

    @Autowired
    public CordenadorService(DisciplinaFactory disciplinaFactory, AulaService aulaService, CounterService counter, CordenadorRepository repository) {
        super(counter, repository);
        this.disciplinaFactory = disciplinaFactory;
        this.aulaService = aulaService;
    }

    public Disciplina cadastrarDisciplina(String disciplinaNome) {
        return disciplinaFactory.criar(disciplinaNome);
    }

    public Aula cadastrarAula(String raProfessor, String codigoDisciplina) {
        return aulaService.criar(raProfessor, codigoDisciplina);
    }


    public CordenadorEntity criar(String primeiroNome, String sobreNome) {
        CordenadorEntity cordenador = super.criar(new CordenadorEntity(primeiroNome, sobreNome));
        if (cordenador != null) {
            return cordenador;
        }
        return null;
    }
}