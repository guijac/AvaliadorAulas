package br.edu.pucsp.avaliador.controller.dto;

import java.time.LocalDateTime;

public class AgendamentoParaAvaliacaoDTO {
    private LocalDateTime horaInicio;
    private long duration;

    public AgendamentoParaAvaliacaoDTO(LocalDateTime horaInicio, long duration) {
        this.horaInicio = horaInicio;
        this.duration = duration;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public long getDuration() {
        return duration;
    }
}
