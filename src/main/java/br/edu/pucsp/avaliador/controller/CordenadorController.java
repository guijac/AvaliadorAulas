package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.controller.dto.DisciplinaDTO;
import br.edu.pucsp.avaliador.controller.dto.MembroAcademicoDTO;
import br.edu.pucsp.avaliador.controller.dto.ProfessorAvaliadoDTO;
import br.edu.pucsp.avaliador.entities.GradeHorariaEntity;
import br.edu.pucsp.avaliador.entities.ProfessorEntity;
import br.edu.pucsp.avaliador.entities.ValidationException;
import br.edu.pucsp.avaliador.model.membroAcademico.Aula;
import br.edu.pucsp.avaliador.model.membroAcademico.CordenadorService;
import br.edu.pucsp.avaliador.model.membroAcademico.GradeHorariaService;
import br.edu.pucsp.avaliador.model.membroAcademico.RecursosHumanos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ROLE_CORDENADOR')")
@RequestMapping("/cordenador")
public class CordenadorController {

    private CordenadorService cordenadorService;
    private RecursosHumanos recursosHumanos;
    private GradeHorariaService gradeHorariaService;

    @Autowired
    public CordenadorController(CordenadorService cordenadorService, RecursosHumanos recursosHumanos, GradeHorariaService gradeHorariaService) {
        this.cordenadorService = cordenadorService;
        this.recursosHumanos = recursosHumanos;
        this.gradeHorariaService = gradeHorariaService;
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
        try {
            Aula aulaCriada = cordenadorService.cadastrarAula(aula.getRaProfessor(), aula.getCodigoDisciplina());
            if (aulaCriada != null) {
                return new ResponseEntity<>(aulaCriada, HttpStatus.OK);
            }
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao cadastrar aula.");
    }

    @GetMapping("/ranking/professores")
    public ResponseEntity<List<ProfessorAvaliadoDTO>> getRankingProfessores() {
        List<ProfessorEntity> professores = recursosHumanos.encontrarTodosProfessores();

        List<ProfessorAvaliadoDTO> ranking = new ArrayList<>();

        for (ProfessorEntity professor : professores) {
            Optional<GradeHorariaEntity> gradeHorariaOptional = gradeHorariaService.encontrar(professor);
            if (!gradeHorariaOptional.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recuperar Aulas do professor.");
            }

            String nrEstrelas = gradeHorariaOptional.get().avaliacaoMedia(gradeHorariaOptional.get());
            ranking.add(new ProfessorAvaliadoDTO(professor.getDTO(),nrEstrelas));
        }

        ranking = ranking.stream()
                .filter(p->!p.getNumeroDeEstrelas().equals("NaN"))
                .sorted((p1,p2)-> p2.getNumeroDeEstrelas().compareTo(p1.getNumeroDeEstrelas())).collect(Collectors.toList());

        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }
}
