package br.edu.pucsp.avaliador.dto;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgendamentoDeAulaEntity {
    private final LocalDate data;
    private final LocalTime horaInicio;
    private final LocalTime horaFim;

    @DBRef
    private  List<AlunoEntity> alunosPresentes;

    private  List<AvaliacaoEntity> avaliacoes;

    public AgendamentoDeAulaEntity(LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.alunosPresentes = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    public List<AlunoEntity> getAlunosPresentes() {
        return alunosPresentes;
    }

    public List<AvaliacaoEntity> getAvaliacoes() {
        return avaliacoes;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgendamentoDeAulaEntity that = (AgendamentoDeAulaEntity) o;
        return data.equals(that.data) &&
                horaInicio.toSecondOfDay()==that.horaInicio.toSecondOfDay() &&
                horaFim.toSecondOfDay()==that.horaFim.toSecondOfDay();
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, horaInicio, horaFim);
    }
}
