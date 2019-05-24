package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.controller.dto.AulaParaAvaliacao;
import br.edu.pucsp.avaliador.entities.AlunoEntity;
import br.edu.pucsp.avaliador.entities.GradeHorariaEntity;
import br.edu.pucsp.avaliador.model.membroAcademico.AlunoService;
import br.edu.pucsp.avaliador.model.membroAcademico.Aula;
import br.edu.pucsp.avaliador.model.membroAcademico.GradeHorariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
    private AlunoService alunoService;
    private GradeHorariaService gradeHorariaService;

    @Autowired
    public AlunoController(AlunoService alunoService, GradeHorariaService gradeHorariaService) {
        this.alunoService = alunoService;
        this.gradeHorariaService = gradeHorariaService;
    }

    @PostMapping("/matricular")
    public ResponseEntity<AlunoEntity> matricular(@RequestBody AlunoEntity aluno) {
        AlunoEntity alunoMatriculado = this.alunoService.matricular(aluno.getPrimeiroNome(), aluno.getSobreNome());

        if (alunoMatriculado == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao matricular aluno.");
        }
        GradeHorariaEntity quadroDeHorarios = gradeHorariaService.criar(alunoMatriculado);
        if (quadroDeHorarios == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao criar quadro de horarios.");
        }
        return new ResponseEntity<>(alunoMatriculado, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @GetMapping("/aulasDisponiveisParaAvaliacao")
    public ResponseEntity<List<AulaParaAvaliacao>> getAulasDisponiveisParaAvaliacao(HttpServletRequest request) {
        Optional<AlunoEntity> aluno = alunoService.encontraPorRegistroAcademico(request.getUserPrincipal().getName());
        if (aluno.isPresent()) {
            List<AulaParaAvaliacao> aulas = gradeHorariaService.getAulasParaAvaliacao(aluno.get());
            return new ResponseEntity<>(aulas, HttpStatus.OK);
        }
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recuperar Aulas para avaliação.");
    }


}
