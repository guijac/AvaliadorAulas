package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dao.DisciplinaRepository;
import br.edu.pucsp.avaliador.dto.DisciplinaEntity;
import br.edu.pucsp.avaliador.dto.ProfessorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecursosHumanos {
    private ProfessorFactory professorFactory;
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    public RecursosHumanos(ProfessorFactory professorFactory, DisciplinaRepository disciplinaRepository) {
        this.professorFactory = professorFactory;
        this.disciplinaRepository = disciplinaRepository;
    }

    public ProfessorEntity contratarProfessor(String primeiroNome, String sobreNome, List<DisciplinaEntity> disciplinasAptoALecionar) {
        List<DisciplinaEntity> disciplinasAptoALecionarDisponiveis = new ArrayList<>();

        disciplinasAptoALecionar.forEach((disciplina) -> {
            DisciplinaEntity disciplinaDisponivel = disciplinaRepository.findByNome(disciplina.getNome());
            if (disciplinaDisponivel != null) {
                disciplinasAptoALecionarDisponiveis.add(disciplinaDisponivel);
            }
        });

        return professorFactory.contratar(primeiroNome, sobreNome, disciplinasAptoALecionarDisponiveis);
    }
}