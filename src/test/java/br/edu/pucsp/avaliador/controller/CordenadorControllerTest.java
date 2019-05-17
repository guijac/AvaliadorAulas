package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.dao.DisciplinaRepository;
import br.edu.pucsp.avaliador.dao.ProfessorRepository;
import br.edu.pucsp.avaliador.dto.DisciplinaEntity;
import br.edu.pucsp.avaliador.dto.ProfessorEntity;
import br.edu.pucsp.avaliador.model.membroAcademico.Aula;
import br.edu.pucsp.avaliador.model.membroAcademico.Disciplina;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CordenadorControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DisciplinaRepository repository;
    @Autowired
    private MongoOperations mongo;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Before
    public void setup() {
        mongo.dropCollection("counters");
        repository.deleteAll();
        professorRepository.deleteAll();
        disciplinaRepository.deleteAll();

        ResponseEntity<DisciplinaEntity> analiseDeTeste = this.restTemplate.postForEntity("/Cordenador/cadastrarDisciplina", "Analise de Teste", DisciplinaEntity.class);
        assertNotNull(analiseDeTeste);
        ResponseEntity<DisciplinaEntity> analiseDeRequisitos = this.restTemplate.postForEntity("/Cordenador/cadastrarDisciplina", "Analise de Requisitos", DisciplinaEntity.class);
        assertNotNull(analiseDeRequisitos);
    }

    @Test
    public void cadastrarDisciplina() {
        ResponseEntity<Disciplina> response1 = cadastrarDisciplina("Disciplina1");
        assertThat(response1.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response1.getBody(), Matchers.allOf(
                Matchers.hasProperty("nome", Matchers.is("Disciplina1")),
                Matchers.hasProperty("codigo", Matchers.is("1"))
        ));

        ResponseEntity<Disciplina> response2 = cadastrarDisciplina("Disciplina2");
        assertThat(response2.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response2.getBody(), Matchers.allOf(
                Matchers.hasProperty("nome", Matchers.is("Disciplina2")),
                Matchers.hasProperty("codigo", Matchers.is("2"))
        ));
    }

    private ResponseEntity<Disciplina> cadastrarDisciplina(String disciplina1) {
        return this.restTemplate.postForEntity("/Cordenador/cadastrarDisciplina", new Disciplina(disciplina1), Disciplina.class);
    }

    @Test
    public void cadastrarAula() {
        List<DisciplinaEntity> disciplinas = new ArrayList<>();
        disciplinas.add(new DisciplinaEntity("Analise de Teste", ""));
        disciplinas.add(new DisciplinaEntity("Analise de Requisitos", ""));
        ProfessorEntity professor = new ProfessorEntity("Daniel", "Gatti", disciplinas);

        ResponseEntity<ProfessorEntity> professorFromDB = this.restTemplate.postForEntity("/RecursosHumanos/contratarProfessor", professor, ProfessorEntity.class);

        Disciplina diciplinaTestDeSoftware = cadastrarDisciplina("Test de Software").getBody();
        String codigoDisciplina = diciplinaTestDeSoftware.getCodigo();
        ResponseEntity<Aula> response1 = this.restTemplate.postForEntity("/Cordenador/cadastrarAula", new Aula(professorFromDB.getBody().getRegistroAcademico(), codigoDisciplina), Aula.class);

    }
}