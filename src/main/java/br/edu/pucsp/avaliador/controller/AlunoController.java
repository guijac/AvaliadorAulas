package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.dto.AlunoEntity;
import br.edu.pucsp.avaliador.model.membroAcademico.Aluno;
import br.edu.pucsp.avaliador.model.membroAcademico.AlunoFactory;
import br.edu.pucsp.avaliador.model.membroAcademico.AulaFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
    private AlunoFactory alunoFactory;
    private AulaFactory aulaService;

    @Autowired
    public AlunoController(AlunoFactory alunoFactory, AulaFactory aulaService) {
        this.alunoFactory = alunoFactory;
        this.aulaService = aulaService;
    }

    @PostMapping("/matricular")
    public ResponseEntity<AlunoEntity> matricular(@RequestBody AlunoEntity aluno) {
        AlunoEntity alunoMatriculado = this.alunoFactory.matricular(aluno.getPrimeiroNome(), aluno.getSobreNome());

        if(alunoMatriculado==null){
            return new ResponseEntity<>(alunoMatriculado, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(alunoMatriculado, HttpStatus.OK);

    }


}
