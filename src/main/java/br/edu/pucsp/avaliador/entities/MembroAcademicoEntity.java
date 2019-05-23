package br.edu.pucsp.avaliador.entities;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public abstract class MembroAcademicoEntity {
    private String primeiroNome;
    private String sobreNome;
    @Id
    private String registroAcademico;

    public MembroAcademicoEntity(String primeiroNome, String sobreNome) {
        this.primeiroNome = primeiroNome;
        this.sobreNome = sobreNome;
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
