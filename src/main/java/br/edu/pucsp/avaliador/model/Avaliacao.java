package br.edu.pucsp.avaliador.model;

import br.edu.pucsp.avaliador.model.membroAcademico.Aluno;

public class Avaliacao {

    private Aluno aluno;
    private Integer nrEstrelas;

    public Avaliacao(Aluno aluno, Integer nrEstrelas) {
        this.aluno = aluno;
        this.nrEstrelas = nrEstrelas;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Integer getNrEstrelas() {
        return nrEstrelas;
    }
}
