package br.edu.pucsp.avaliador.controller.dto;

import br.edu.pucsp.avaliador.entities.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AulaParaAvaliacao {
    private String id;
    private MembroAcademicoDTO professor;
    private DisciplinaDTO disciplina;
    private AgendamentoParaAvaliacaoDTO agentamentoParaAvaliacao;

    public AulaParaAvaliacao(AulaEntity aula) {
        this.id = aula.getId();
        this.professor = aula.getProfessor().getDTO();
        this.disciplina = aula.getDisciplina().getDTO();
    }

    public AulaParaAvaliacao() {
    }

    public String getId() {
        return id;
    }

    public MembroAcademicoDTO getProfessor() {
        return professor;
    }

    public DisciplinaDTO getDisciplina() {
        return disciplina;
    }

    public AgendamentoParaAvaliacaoDTO getAgentamentoParaAvaliacao() {
        return agentamentoParaAvaliacao;
    }

    public void setAgendamentoParaAvaliacao(AgendamentoParaAvaliacaoDTO agentamentoParaAvaliacao) {
        this.agentamentoParaAvaliacao = agentamentoParaAvaliacao;
    }
}
