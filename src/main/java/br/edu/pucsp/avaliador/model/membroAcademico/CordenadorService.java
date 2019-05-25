package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.controller.dto.DisciplinaDTO;
import br.edu.pucsp.avaliador.dao.CordenadorRepository;
import br.edu.pucsp.avaliador.dao.CounterService;
import br.edu.pucsp.avaliador.entities.CordenadorEntity;
import br.edu.pucsp.avaliador.entities.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CordenadorService extends MembroAcademicoFactory<CordenadorEntity> {

    private DisciplinaService disciplinaService;
    private AulaService aulaService;

    @Autowired
    public CordenadorService(DisciplinaService disciplinaService, AulaService aulaService, CounterService counter, CordenadorRepository repository, ProfessorService professorService) {
        super(counter, repository);
        this.disciplinaService = disciplinaService;
        this.aulaService = aulaService;
    }

    public DisciplinaDTO cadastrarDisciplina(String disciplinaNome) {
        return disciplinaService.criar(disciplinaNome);
    }

    public Aula cadastrarAula(String raProfessor, String codigoDisciplina) throws ValidationException {
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