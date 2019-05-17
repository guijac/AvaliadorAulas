package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.model.membroAcademico.Aula;
import br.edu.pucsp.avaliador.model.membroAcademico.Cordenador;
import br.edu.pucsp.avaliador.model.membroAcademico.Disciplina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Cordenador")
public class CordenadorController {

    private Cordenador cordenador;

    @Autowired
    public CordenadorController(Cordenador cordenador) {
        this.cordenador = cordenador;
    }

    @PostMapping("/cadastrarDisciplina")
    public ResponseEntity<Disciplina> cadastrarDisciplina(@RequestBody Disciplina disciplina) {
        Disciplina disciplinaCriada = cordenador.cadastrarDisciplina(disciplina.getNome());
        if (disciplinaCriada != null) {
            return new ResponseEntity<>(disciplinaCriada, HttpStatus.OK);
        }
        return new ResponseEntity<>(new Disciplina(disciplina.getNome(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/cadastrarAula")
    public ResponseEntity<Aula> cadastrarDisciplina(@RequestBody Aula aula) {
        Aula aulaCriada = cordenador.cadastrarAula(aula.getRaProfessor(),aula.getCodigoDisciplina());
        if (aulaCriada != null) {
            return new ResponseEntity<>(aulaCriada, HttpStatus.OK);
        }
        return new ResponseEntity<>(aula, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
