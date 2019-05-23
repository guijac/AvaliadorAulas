package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.entities.DisciplinaEntity;
import br.edu.pucsp.avaliador.entities.ProfessorEntity;
import br.edu.pucsp.avaliador.model.membroAcademico.RecursosHumanos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_CORDENADOR')")
@RequestMapping("/RecursosHumanos")
public class RecursosHumanosController {
    private RecursosHumanos recursosHumanos;

    @Autowired
    public RecursosHumanosController(RecursosHumanos recursosHumanos) {
        this.recursosHumanos = recursosHumanos;
    }


    @PostMapping("/contratarProfessor")
    public ResponseEntity<ProfessorEntity> contratarProfessor(@RequestBody ProfessorEntity professor) {
        String primeiroNome = professor.getPrimeiroNome();
        String sobreNome = professor.getSobreNome();
        List<DisciplinaEntity> disciplinasAptoALecionar = professor.getDisciplinasAptoALecionar();

        ProfessorEntity professorContratado = this.recursosHumanos.contratarProfessor(primeiroNome, sobreNome, disciplinasAptoALecionar);

        if (professorContratado == null) {
            return new ResponseEntity<>(professor, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(professorContratado, HttpStatus.OK);
    }


}
