package br.edu.pucsp.avaliador.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cordenador")
public class CordenadorEntity extends MembroAcademicoEntity {
    public CordenadorEntity(String primeiroNome, String sobreNome) {
        super(primeiroNome, sobreNome, Type.CORDENADOR);
    }
}
