package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.entities.*;
import br.edu.pucsp.avaliador.model.Avaliacao;
import br.edu.pucsp.avaliador.model.membroAcademico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/aula")
public class AulaController {

    private AulaService aulaService;
    private AvaliacaoFactory avaliacaoFactory;
    private AlunoService alunoService;
    private GradeHorariaService gradeHorariaService;

    @Autowired
    public AulaController(AulaService aulaService, AvaliacaoFactory avaliacaoFactory, AlunoService alunoService, GradeHorariaService gradeHorariaService) {
        this.aulaService = aulaService;
        this.avaliacaoFactory = avaliacaoFactory;
        this.alunoService = alunoService;
        this.gradeHorariaService = gradeHorariaService;
    }

    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @PostMapping("/avaliar/{aulaId}")
    public ResponseEntity<AulaEntity> avaliar(@PathVariable String aulaId,
                                              @RequestBody Avaliacao avaliacaoRequest,
                                              HttpServletRequest request) {

        Optional<AulaEntity> optionalAula = aulaService.encontrarAula(aulaId);
        if (avaliacaoRequest.getAluno() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Dados de aluno faltando");
        }
        String registroAcademico = request.getUserPrincipal().getName();

        Optional<AvaliacaoEntity> optionalAvaliacao = avaliacaoFactory.criarAvaliacao(registroAcademico, avaliacaoRequest.getNrEstrelas());

        if (optionalAula.isPresent() && optionalAvaliacao.isPresent()) {
            AulaEntity aula = optionalAula.get();
            AvaliacaoEntity avaliacao = optionalAvaliacao.get();
            aula.avaliar(avaliacao);
            AulaEntity aulaAvaliada = aulaService.atualizar(aula);
            return new ResponseEntity<>(aulaAvaliada, HttpStatus.OK);
        }
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Falha na avaliação");
    }

    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @PostMapping("/matricular/{aulaId}")
    public ResponseEntity<AulaEntity> matricular(@PathVariable String aulaId,
                                                 @RequestBody Aluno requestAluno) {
        try {
            String registroAcademico = requestAluno.getRegistroAcademico();

            Optional<AlunoEntity> alunoOptional = alunoService.encontraPorRegistroAcademico(registroAcademico);

            Optional<AulaEntity> optionalAula = aulaService.encontrarAula(aulaId);
            if (optionalAula.isPresent() && alunoOptional.isPresent()) {
                AulaEntity aula = optionalAula.get();

                gradeHorariaService.adicionarAula(alunoOptional.get(), aula);
                aula.matricular(alunoOptional.get());
                aulaService.atualizar(aula);

                Optional<AulaEntity> aulaMatriculadoOptional = aulaService.encontrarAula(aulaId);
                if (aulaMatriculadoOptional.isPresent() && aulaMatriculadoOptional.get().estaMatriculado(alunoOptional.get())) {
                    return new ResponseEntity<>(aulaMatriculadoOptional.get(), HttpStatus.OK);
                }
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Matricula pode não ter sido concluida.");
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_CORDENADOR')")
    @PostMapping("/addicionarAgendamento/{aulaId}")
    public ResponseEntity<AulaEntity> addicionarAgendamento(@PathVariable String aulaId,
                                                            @RequestBody AgendamentoDeAulaEntity agendamento) {

        try {
            Optional<AulaEntity> optionalAula = aulaService.encontrarAula(aulaId);
            if (optionalAula.isPresent()) {
                AulaEntity aula = optionalAula.get();
                aula.addicionarAgendamento(agendamento.getHoraInicio(), agendamento.getDuration());
                aulaService.atualizar(aula);

                Optional<AulaEntity> aulaFromDB = aulaService.encontrarAula(aulaId);
                if (aulaFromDB.isPresent() && aulaFromDB.get().getAgendamentos().contains(agendamento)) {

                    return new ResponseEntity<>(aulaFromDB.get(), HttpStatus.OK);
                }
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Matricula pode não ter sido concluida.");
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
