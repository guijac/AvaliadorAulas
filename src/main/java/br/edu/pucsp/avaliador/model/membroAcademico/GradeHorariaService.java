package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.controller.dto.AulaParaAvaliacao;
import br.edu.pucsp.avaliador.dao.GradeHorariaRepository;
import br.edu.pucsp.avaliador.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeHorariaService {
    private GradeHorariaRepository repository;

    @Autowired
    public GradeHorariaService(GradeHorariaRepository repository) {
        this.repository = repository;
    }

    public GradeHorariaEntity criar(MembroAcademicoEntity membroAcademico) {
        return repository.insert(new GradeHorariaEntity(membroAcademico));
    }

    public Optional<GradeHorariaEntity> encontrar(MembroAcademicoEntity membroAcademico) {
        return repository.findById(membroAcademico.getRegistroAcademico());
    }

    public GradeHorariaEntity adicionarAula(MembroAcademicoEntity membroAcademico, AulaEntity aula) throws ValidationException {
        Optional<GradeHorariaEntity> grade = encontrar(membroAcademico);
        if (grade.isPresent()) {
            grade.get().incluirAula(aula);
            GradeHorariaEntity save = repository.save(grade.get());
            if (save == null) {
                throw new ValidationException("Falha ao atualizar a grade horaria do aluno.");
            }
            return save;
        } else {
            throw new ValidationException("O aluno não possui uma grade de aula.");
        }

    }

    public List<AulaParaAvaliacao> getAulasParaAvaliacao(AlunoEntity aluno) {
        Optional<GradeHorariaEntity> gradeHorariaOptional = encontrar(aluno);
        return gradeHorariaOptional.map(gradeHorariaEntity -> gradeHorariaEntity.getAulas().stream().map(aula -> {
            AulaParaAvaliacao aulaDTO = new AulaParaAvaliacao(aula);
            aula.getAgendamentoDisponivelParaAvaliacao(aluno).ifPresent(agendamento ->aulaDTO.setAgendamentoParaAvaliacao(agendamento.getDTO()));
            return aulaDTO;
        }).filter(aula -> aula.getAgentamentoParaAvaliacao() != null).collect(Collectors.toList())).orElse(Collections.emptyList());
    }
}
