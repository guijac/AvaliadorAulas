package br.edu.pucsp.avaliador.controller.dto;

import br.edu.pucsp.avaliador.entities.MembroAcademicoEntity;

public class MembroAcademicoDTO {

    private String primeiroNome;
    private String sobreNome;
    private String registroAcademico;
    private Type type;

    public MembroAcademicoDTO(String primeiroNome, String sobreNome, String registroAcademico, String type) {
        this.primeiroNome = primeiroNome;
        this.sobreNome = sobreNome;
        this.registroAcademico = registroAcademico;
        this.type = Type.valueOf(type);
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }


    public String getSobreNome() {
        return sobreNome;
    }


    public String getRegistroAcademico() {
        return registroAcademico;
    }


    public Type getType() {
        return type;
    }

    public enum Type {
        PROFESSOR,
        CORDENADOR,
        ALUNO;
    }
}
