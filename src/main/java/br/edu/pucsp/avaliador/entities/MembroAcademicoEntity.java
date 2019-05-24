package br.edu.pucsp.avaliador.entities;

import br.edu.pucsp.avaliador.controller.dto.MembroAcademicoDTO;
import org.springframework.data.annotation.Id;

import java.util.Objects;

public abstract class MembroAcademicoEntity {
    public enum Type {
        PROFESSOR,
        CORDENADOR,
        ALUNO;
    }

    private String primeiroNome;
    private String sobreNome;
    @Id
    private String registroAcademico;

    private Type type;

    public MembroAcademicoEntity(String primeiroNome, String sobreNome, Type type) {
        this.primeiroNome = primeiroNome;
        this.sobreNome = sobreNome;
        this.type = type;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getRegistroAcademico() {
        return registroAcademico;
    }

    public void setRegistroAcademico(String registroAcademico) {
        this.registroAcademico = registroAcademico;
    }

    public  MembroAcademicoDTO getDTO(){
        return new MembroAcademicoDTO(primeiroNome,sobreNome,registroAcademico,type.name());
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembroAcademicoEntity that = (MembroAcademicoEntity) o;
        return registroAcademico.equals(that.registroAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registroAcademico);
    }
}
