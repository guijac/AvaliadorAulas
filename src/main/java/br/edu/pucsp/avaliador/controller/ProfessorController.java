package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.entities.GradeHorariaEntity;
import br.edu.pucsp.avaliador.entities.ProfessorEntity;
import br.edu.pucsp.avaliador.model.membroAcademico.GradeHorariaService;
import br.edu.pucsp.avaliador.model.membroAcademico.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/professor")
public class ProfessorController {
    private ProfessorService professorService;
    private GradeHorariaService gradeHorariaService;


    @Autowired
    public ProfessorController(ProfessorService professorService, GradeHorariaService gradeHorariaService) {
        this.professorService = professorService;
        this.gradeHorariaService = gradeHorariaService;
    }

    @GetMapping("/avaliacao/{ra}")
    public ResponseEntity<String> getAvaliacao(@PathVariable String ra) {
        Optional<ProfessorEntity> professorOptional = professorService.encontraPorRegistroAcademico(ra);
        if (!professorOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recuperar Professor.");
        }

        Optional<GradeHorariaEntity> gradeHorariaOptional = gradeHorariaService.encontrar(professorOptional.get());
        if (!gradeHorariaOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recuperar Aulas do professor.");
        }

        String nrEstrelas = gradeHorariaOptional.get().avaliacaoMedia(gradeHorariaOptional.get());
        return new ResponseEntity<>(nrEstrelas, HttpStatus.OK);
    }

}
