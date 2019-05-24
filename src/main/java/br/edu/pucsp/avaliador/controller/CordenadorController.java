package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.controller.dto.DisciplinaDTO;
import br.edu.pucsp.avaliador.model.membroAcademico.Aula;
import br.edu.pucsp.avaliador.model.membroAcademico.CordenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_CORDENADOR')")
@RequestMapping("/Cordenador")
public class CordenadorController {

    private CordenadorService cordenadorService;

    @Autowired
    public CordenadorController(CordenadorService cordenadorService) {
        this.cordenadorService = cordenadorService;
    }

    @PostMapping("/cadastrarDisciplina")
    public ResponseEntity<DisciplinaDTO> cadastrarDisciplina(@RequestBody DisciplinaDTO disciplina) {
        DisciplinaDTO disciplinaCriada = cordenadorService.cadastrarDisciplina(disciplina.getNome());
        if (disciplinaCriada != null) {
            return new ResponseEntity<>(disciplinaCriada, HttpStatus.OK);
        }
        return new ResponseEntity<>(new DisciplinaDTO(disciplina.getNome(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/cadastrarAula")
    public ResponseEntity<Aula> cadastrarAula(@RequestBody Aula aula) {
        Aula aulaCriada = cordenadorService.cadastrarAula(aula.getRaProfessor(), aula.getCodigoDisciplina());
        if (aulaCriada != null) {
            return new ResponseEntity<>(aulaCriada, HttpStatus.OK);
        }
        return new ResponseEntity<>(aula, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
