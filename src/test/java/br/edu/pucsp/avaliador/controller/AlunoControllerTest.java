package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.dao.AlunoRepository;
import br.edu.pucsp.avaliador.dao.AulaRepository;
import br.edu.pucsp.avaliador.dao.DisciplinaRepository;
import br.edu.pucsp.avaliador.dao.ProfessorRepository;
import br.edu.pucsp.avaliador.dto.AlunoEntity;
import br.edu.pucsp.avaliador.model.membroAcademico.Aluno;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AlunoControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private MongoOperations mongo;

    @Before
    public void setup(){
        mongo.dropCollection("counters");
        aulaRepository.deleteAll();
        professorRepository.deleteAll();
        disciplinaRepository.deleteAll();
        alunoRepository.deleteAll();
    }

    @Test
    public void matricular() {
        Aluno aluno1 = new Aluno("Nome1","SobreNome");
        Aluno aluno2 = new Aluno("Nome2","SobreNome");
        ResponseEntity<AlunoEntity> response1 = this.restTemplate.postForEntity("/aluno/matricular", aluno1, AlunoEntity.class);
        assertThat(response1.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response1.getBody(), Matchers.allOf(
                Matchers.hasProperty("primeiroNome", Matchers.is("Nome1")),
                Matchers.hasProperty("sobreNome", Matchers.is("SobreNome")),
                Matchers.hasProperty("registroAcademico", Matchers.is("RA00000001"))
        ));

        ResponseEntity<AlunoEntity> response2 = this.restTemplate.postForEntity("/aluno/matricular", aluno2, AlunoEntity.class);
        assertThat(response2.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response2.getBody(), Matchers.allOf(
                Matchers.hasProperty("primeiroNome", Matchers.is("Nome2")),
                Matchers.hasProperty("sobreNome", Matchers.is("SobreNome")),
                Matchers.hasProperty("registroAcademico", Matchers.is("RA00000002"))
        ));

    }

}