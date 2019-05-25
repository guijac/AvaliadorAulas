package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.entities.DisciplinaEntity;
import br.edu.pucsp.avaliador.entities.GradeHorariaEntity;
import br.edu.pucsp.avaliador.entities.ProfessorEntity;
import br.edu.pucsp.avaliador.model.membroAcademico.GradeHorariaService;
import br.edu.pucsp.avaliador.model.membroAcademico.RecursosHumanos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_CORDENADOR')")
@RequestMapping("/recursosHumanos")
public class RecursosHumanosController {
    private RecursosHumanos recursosHumanos;
    private GradeHorariaService gradeHorariaService;

    @Autowired
    public RecursosHumanosController(RecursosHumanos recursosHumanos, GradeHorariaService gradeHorariaService) {
        this.recursosHumanos = recursosHumanos;
        this.gradeHorariaService = gradeHorariaService;
    }


    @PostMapping("/contratar/professor")
    public ResponseEntity<ProfessorEntity> contratarProfessor(@RequestBody ProfessorEntity professor) {
        String primeiroNome = professor.getPrimeiroNome();
        String sobreNome = professor.getSobreNome();
        List<DisciplinaEntity> disciplinasAptoALecionar = professor.getDisciplinasAptoALecionar();

        ProfessorEntity professorContratado = this.recursosHumanos.contratarProfessor(primeiroNome, sobreNome, disciplinasAptoALecionar);

        if (professorContratado == null) {
            return new ResponseEntity<>(professor, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        GradeHorariaEntity quadroDeHorarios = gradeHorariaService.criar(professorContratado);
        if (quadroDeHorarios == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao criar quadro de horarios.");
        }
        return new ResponseEntity<>(professorContratado, HttpStatus.OK);
    }

}
