package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.entities.AgendamentoDeAulaEntity;
import br.edu.pucsp.avaliador.entities.AulaEntity;

public class Aula {
    private String id;
    private String raProfessor;
    private String codigoDisciplina;
    private AgendamentoDeAulaEntity agentamentoParaAvaliacao;

    public Aula(){
    }

    public Aula(AulaEntity aulaCriada) {
        this.id = aulaCriada.getId();
        this.raProfessor = aulaCriada.getProfessor().getRegistroAcademico();
        this.codigoDisciplina = aulaCriada.getDisciplina().getCodigo();
    }
    public Aula(String raProfessor, String codigoDisciplina) {
        this.raProfessor = raProfessor;
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getRaProfessor() {
        return raProfessor;
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public String getId() {
        return id;
    }

    public void setAgendamentoParaAvaliacao(AgendamentoDeAulaEntity agendamento) {
        this.agentamentoParaAvaliacao = agendamento;
    }

    public AgendamentoDeAulaEntity getAgentamentoParaAvaliacao() {
        return agentamentoParaAvaliacao;
    }
}
