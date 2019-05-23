package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dao.*;
import br.edu.pucsp.avaliador.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AulaService {

    private CounterService counterService;
    private AulaRepository aulaRepository;
    private ProfessorRepository professorRepository;
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    public AulaService(CounterService counterService,
                       AulaRepository aulaRepository,
                       ProfessorRepository professorRepository,
                       DisciplinaRepository disciplinaRepository) {
        this.counterService = counterService;
        this.aulaRepository = aulaRepository;
        this.professorRepository = professorRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public Aula criar(String raProfessor, String codigoDisciplina) {
        Optional<ProfessorEntity> professor = professorRepository.findByRegistroAcademico(raProfessor);
        if (!professor.isPresent()) {
            //TODO
        }

        DisciplinaEntity disciplina = disciplinaRepository.findByCodigo(codigoDisciplina);
        if (disciplina == null) {
            //TODO
        }
        String id = "" + counterService.getNextSequence("aulas");
        AulaEntity aula = new AulaEntity(id, professor.get(), disciplina);
        AulaEntity aulaCriada = aulaRepository.insert(aula);
        if (aulaCriada == null) {
            //TODO
        }
        return new Aula(aulaCriada);
    }

    public Optional<AulaEntity> encontrarAula(String aulaId) {
        return aulaRepository.findById(aulaId);
    }

    public AulaEntity atualizar(AulaEntity aula) {
        return aulaRepository.save(aula);
    }
}
