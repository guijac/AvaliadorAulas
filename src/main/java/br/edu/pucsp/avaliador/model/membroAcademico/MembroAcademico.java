package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dto.MembroAcademicoEntity;

import java.util.Objects;

public class MembroAcademico<E extends MembroAcademicoEntity> {
    protected String primeiroNome;
    protected String sobreNome;
    protected String registroAcademico;

    public MembroAcademico(E entity) {
        this.primeiroNome = entity.getPrimeiroNome();
        this.sobreNome = entity.getSobreNome();
        this.registroAcademico = entity.getRegistroAcademico();
    }

    public MembroAcademico(String primeiroNome, String sobreNome) {
        this.primeiroNome = primeiroNome;
        this.sobreNome = sobreNome;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembroAcademico<?> that = (MembroAcademico<?>) o;
        return primeiroNome.equals(that.primeiroNome) &&
                sobreNome.equals(that.sobreNome) &&
                registroAcademico.equals(that.registroAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primeiroNome, sobreNome, registroAcademico);
    }
}
