package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.entities.AlunoEntity;

public class Aluno extends MembroAcademico {

    public Aluno(AlunoEntity entity) {
        super(entity);
    }

    public Aluno(String primeiroNome, String sobreNome) {
        super( primeiroNome,  sobreNome);
    }
}