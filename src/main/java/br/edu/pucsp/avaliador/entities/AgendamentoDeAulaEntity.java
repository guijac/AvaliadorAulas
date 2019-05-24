package br.edu.pucsp.avaliador.entities;

import br.edu.pucsp.avaliador.controller.dto.AgendamentoParaAvaliacaoDTO;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgendamentoDeAulaEntity {
    private final LocalDateTime horaInicio;
    private long duration;

    @DBRef
    private List<AlunoEntity> alunosPresentes;

    private List<AvaliacaoEntity> avaliacoes;

    public AgendamentoDeAulaEntity(LocalDateTime horaInicio, long duration) {
        this.horaInicio = horaInicio;
        this.duration = duration;
        this.alunosPresentes = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    public List<AlunoEntity> getAlunosPresentes() {
        return alunosPresentes;
    }

    public List<AvaliacaoEntity> getAvaliacoes() {
        return avaliacoes;
    }


    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgendamentoDeAulaEntity that = (AgendamentoDeAulaEntity) o;
        return horaInicio.truncatedTo(ChronoUnit.SECONDS).isEqual(that.horaInicio.truncatedTo(ChronoUnit.SECONDS)) &&
                duration == that.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(horaInicio, duration);
    }

    public long getDuration() {
        return duration;
    }

    public AgendamentoParaAvaliacaoDTO getDTO() {
        return new AgendamentoParaAvaliacaoDTO(horaInicio,duration);
    }
}
