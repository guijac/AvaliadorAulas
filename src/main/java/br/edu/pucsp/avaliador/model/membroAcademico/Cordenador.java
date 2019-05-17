package br.edu.pucsp.avaliador.model.membroAcademico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Cordenador {

    private DisciplinaFactory disciplinaFactory;
    private AulaFactory aulaFactory;

    @Autowired
    public Cordenador(DisciplinaFactory disciplinaFactory, AulaFactory aulaFactory) {
        this.disciplinaFactory = disciplinaFactory;
        this.aulaFactory = aulaFactory;
    }

    public Disciplina cadastrarDisciplina(String disciplinaNome) {
        return disciplinaFactory.criar(disciplinaNome);
    }

    public Aula cadastrarAula(String raProfessor, String codigoDisciplina) {
        return aulaFactory.criar(raProfessor, codigoDisciplina);
    }


}