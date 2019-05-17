package br.edu.pucsp.avaliador.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Document(collection = "aulas")
public class AulaEntity {
    @Id
    private String id;

    @DBRef
    private final ProfessorEntity professor;
    @DBRef
    private final DisciplinaEntity disciplina;
    @DBRef
    private List<AlunoEntity> alunosMatriculados;

    private List<AgendamentoDeAulaEntity> agendamentos;

    public AulaEntity(String id, ProfessorEntity professor, DisciplinaEntity disciplina) {
        this.id = id;
        this.professor = professor;
        this.disciplina = disciplina;
        this.alunosMatriculados = new ArrayList<>();
        this.agendamentos = new ArrayList<>();

    }


    public ProfessorEntity getProfessor() {
        return professor;
    }

    public DisciplinaEntity getDisciplina() {
        return disciplina;
    }

    public List<AlunoEntity> getAlunosMatriculados() {
        return alunosMatriculados;
    }

    public String getId() {
        return id;
    }

    public List<AgendamentoDeAulaEntity> getAgendamentos() {
        return agendamentos;
    }

    public void avaliar(AvaliacaoEntity avaliacao) {
        AlunoEntity aluno = avaliacao.getAluno();

        boolean isMatriculado = getAlunosMatriculados().contains(aluno);
        if (!isMatriculado) {
            return;//TODO error
        }


        Optional<AgendamentoDeAulaEntity> agendamentoDisponivelParaAvaliacao = getAgendamentoDisponivelParaAvaliacao();

        if (!agendamentoDisponivelParaAvaliacao.isPresent()) {
            return;//TODO error
        }

        if (agendamentoDisponivelParaAvaliacao.get().getAvaliacoes().stream().anyMatch(a -> a.getAluno().equals(aluno))) {
            return;//TODO error
        }


        agendamentoDisponivelParaAvaliacao.get().getAvaliacoes().add(avaliacao);
    }

    public Optional<AgendamentoDeAulaEntity> getAgendamentoDisponivelParaAvaliacao() {
        return getAgendamentos().stream()
                .sorted(Comparator.comparing(AgendamentoDeAulaEntity::getData))
                .filter(o -> {
                    LocalDate now = LocalDate.now();
                    return now.isAfter(o.getData()) && !now.minusDays(2).isAfter(o.getData());
                })
                .findFirst();
    }

    public void matricular(AlunoEntity aluno) {
        if (!estaMatriculado(aluno)) {
            alunosMatriculados.add(aluno);

        }
        //TODO error

    }

    public boolean estaMatriculado(AlunoEntity aluno) {
        return alunosMatriculados.contains(aluno);
    }

    public void addicionarAgendamento(LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        //TODO validar data e horario
        getAgendamentos().add(new AgendamentoDeAulaEntity(data, horaInicio, horaFim));
    }
}
