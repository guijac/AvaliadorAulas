package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.dto.AgendamentoDeAulaEntity;
import br.edu.pucsp.avaliador.dto.AlunoEntity;
import br.edu.pucsp.avaliador.dto.AulaEntity;
import br.edu.pucsp.avaliador.dto.AvaliacaoEntity;
import br.edu.pucsp.avaliador.model.Avaliacao;
import br.edu.pucsp.avaliador.model.membroAcademico.AlunoFactory;
import br.edu.pucsp.avaliador.model.membroAcademico.AulaFactory;
import br.edu.pucsp.avaliador.model.membroAcademico.AvaliacaoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/aula")
public class AulaController {

    private AulaFactory aulaFactory;
    private AvaliacaoFactory avaliacaoFactory;
    private AlunoFactory alunoFactory;

    @Autowired
    public AulaController(AulaFactory aulaFactory, AvaliacaoFactory avaliacaoFactory, AlunoFactory alunoFactory) {
        this.aulaFactory = aulaFactory;
        this.avaliacaoFactory = avaliacaoFactory;
        this.alunoFactory = alunoFactory;
    }

    @PostMapping("/avaliar/{aulaId}")
    public ResponseEntity<AulaEntity> avaliar(@PathVariable String aulaId,
                                              @RequestBody Avaliacao avaliacaoRequest) {

        Optional<AulaEntity> optionalAula = aulaFactory.encontrarAula(aulaId);
        if (avaliacaoRequest.getAluno() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Dados de aluno faltando");
        }
        String registroAcademico = avaliacaoRequest.getAluno().getRegistroAcademico();

        Optional<AvaliacaoEntity> optionalAvaliacao = avaliacaoFactory.criarAvaliacao(registroAcademico, avaliacaoRequest.getNrEstrelas());

        if (optionalAula.isPresent() && optionalAvaliacao.isPresent()) {
            AulaEntity aula = optionalAula.get();
            AvaliacaoEntity avaliacao = optionalAvaliacao.get();
            aula.avaliar(avaliacao);
            AulaEntity aulaAvaliada = aulaFactory.atualizar(aula);
            return new ResponseEntity<>(aulaAvaliada, HttpStatus.OK);
        }
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Falha na avaliação");
    }

    @PostMapping("/matricular/{aulaId}")
    public ResponseEntity<AulaEntity> matricular(@PathVariable String aulaId,
                                                 @RequestBody AlunoEntity requestAluno) {

        String registroAcademico = requestAluno.getRegistroAcademico();

        Optional<AlunoEntity> alunoOptional = alunoFactory.encontraPorRegistroAcademico(registroAcademico);

        Optional<AulaEntity> optionalAula = aulaFactory.encontrarAula(aulaId);
        if (optionalAula.isPresent() && alunoOptional.isPresent()) {
            AulaEntity aula = optionalAula.get();
            aula.matricular(alunoOptional.get());
            aulaFactory.atualizar(aula);

            Optional<AulaEntity> aulaMatriculadoOptional = aulaFactory.encontrarAula(aulaId);
            if (aulaMatriculadoOptional.isPresent() && aulaMatriculadoOptional.get().estaMatriculado(alunoOptional.get())) {
                return new ResponseEntity<>(aulaMatriculadoOptional.get(), HttpStatus.OK);
            }
        }

        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Matricula pode não ter sido concluida.");
    }

    @PostMapping("/addicionarAgendamento/{aulaId}")
    public ResponseEntity<AulaEntity> addicionarAgendamento(@PathVariable String aulaId,
                                                            @RequestBody AgendamentoDeAulaEntity agendamento) {


        Optional<AulaEntity> optionalAula = aulaFactory.encontrarAula(aulaId);
        if (optionalAula.isPresent()) {
            AulaEntity aula = optionalAula.get();
            aula.addicionarAgendamento(agendamento.getData(), agendamento.getHoraInicio(), agendamento.getHoraFim());
            aulaFactory.atualizar(aula);

            Optional<AulaEntity> aulaFromDB = aulaFactory.encontrarAula(aulaId);
            if (aulaFromDB.isPresent() && aulaFromDB.get().getAgendamentos().contains(agendamento)) {

                return new ResponseEntity<>(aulaFromDB.get(), HttpStatus.OK);
            }
        }

        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Matricula pode não ter sido concluida.");
    }
}
