package br.edu.pucsp.avaliador.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private AgendamentoDeAulaEntity agentamentoDisponivelParaAvaliacao;

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
                .sorted(Comparator.comparing(AgendamentoDeAulaEntity::getHoraInicio))
                .filter(o -> {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime horarioFim = o.getHoraInicio().plusMinutes(o.getDuration());
                    return now.isAfter(horarioFim) &&
                            now.minusDays(2).isBefore(horarioFim);
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

    public void addicionarAgendamento( LocalDateTime horaInicio, long duration) throws ValidationException {
        //TODO validar data e horario

        if (duration < 50 || duration > 180) {
            throw new ValidationException("Hora de fim deve ser maior do que hora de inicio.");
        }
        getAgendamentos().add(new AgendamentoDeAulaEntity(horaInicio, duration));
    }
}
