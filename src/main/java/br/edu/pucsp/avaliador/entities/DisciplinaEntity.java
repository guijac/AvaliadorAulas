package br.edu.pucsp.avaliador.entities;

import br.edu.pucsp.avaliador.controller.dto.DisciplinaDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "disciplinas")
public class DisciplinaEntity {
    private String nome;
    @Id
    private String codigo;

    public DisciplinaEntity(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }


    public DisciplinaDTO getDTO(){
        return new DisciplinaDTO(nome,codigo);
    }
}
