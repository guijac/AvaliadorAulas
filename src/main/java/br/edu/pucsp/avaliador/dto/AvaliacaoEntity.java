package br.edu.pucsp.avaliador.dto;

public class AvaliacaoEntity {
    private AlunoEntity aluno;
    private Integer numeroEstrelas;

    public AvaliacaoEntity(AlunoEntity aluno, Integer numeroEstrelas) {
        this.aluno = aluno;
        this.numeroEstrelas = numeroEstrelas;
    }

    public AlunoEntity getAluno() {
        return aluno;
    }

    public Integer getNumeroEstrelas() {
        return numeroEstrelas;
    }
}
