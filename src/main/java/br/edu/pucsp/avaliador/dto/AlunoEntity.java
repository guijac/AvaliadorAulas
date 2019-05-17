package br.edu.pucsp.avaliador.dto;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "alunos")
public class AlunoEntity extends MembroAcademicoEntity {

    public AlunoEntity(String primeiroNome, String sobreNome) {
        super(primeiroNome, sobreNome);
    }
}
