package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.dao.CordenadorRepository;
import br.edu.pucsp.avaliador.dao.DisciplinaRepository;
import br.edu.pucsp.avaliador.dao.ProfessorRepository;
import br.edu.pucsp.avaliador.dao.UsuarioRepository;
import br.edu.pucsp.avaliador.entities.CordenadorEntity;
import br.edu.pucsp.avaliador.entities.DisciplinaEntity;
import br.edu.pucsp.avaliador.entities.ProfessorEntity;
import br.edu.pucsp.avaliador.entities.Usuario;
import br.edu.pucsp.avaliador.model.membroAcademico.CordenadorService;
import br.edu.pucsp.avaliador.model.membroAcademico.Disciplina;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class RecursosHumanosControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    @Autowired
    private MongoOperations mongo;

    @Autowired
    private CordenadorRepository cordenadorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CordenadorService cordenadorService;
    private CordenadorEntity cordenador;

    @Before
    public void setup() {
        mongo.dropCollection("counters");
        professorRepository.deleteAll();
        disciplinaRepository.deleteAll();
        cordenadorRepository.deleteAll();
        usuarioRepository.deleteAll();

        cordenador = cordenadorService.criar("Cordenador", "Para Teste");
        ResponseEntity<Usuario> usuarioResponse = this.restTemplate
                .postForEntity("/usuario/criar", new Usuario(cordenador.getRegistroAcademico(), "1234", "CORDENADOR"), Usuario.class);
        assertThat(usuarioResponse.getStatusCode(), Matchers.is(HttpStatus.OK));

        ResponseEntity<DisciplinaEntity> analiseDeTeste = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/Cordenador/cadastrarDisciplina", new Disciplina("Analise de Teste"), DisciplinaEntity.class);
        assertNotNull(analiseDeTeste);
        ResponseEntity<DisciplinaEntity> analiseDeRequisitos = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/Cordenador/cadastrarDisciplina", new Disciplina("Analise de Requisitos"), DisciplinaEntity.class);
        assertNotNull(analiseDeRequisitos);
    }

    @Test
    public void contratarProfessor() {

        List<DisciplinaEntity> disciplinas = new ArrayList<>();
        disciplinas.add(new DisciplinaEntity("Analise de Teste", ""));
        disciplinas.add(new DisciplinaEntity("Analise de Requisitos", ""));
        ProfessorEntity professor = new ProfessorEntity("Daniel", "Gatti", disciplinas);

        ResponseEntity<ProfessorEntity> response = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/RecursosHumanos/contratarProfessor", professor, ProfessorEntity.class);

        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertNotNull(response.getBody());
        assertThat(response.getBody(), Matchers.allOf(
                Matchers.hasProperty("primeiroNome", Matchers.is("Daniel")),
                Matchers.hasProperty("sobreNome", Matchers.is("Gatti")),
                Matchers.hasProperty("registroAcademico", Matchers.is("RA00000002"))
        ));
        assertThat(response.getBody().getDisciplinasAptoALecionar(), Matchers.allOf(
                Matchers.hasItem(Matchers.allOf(
                        Matchers.hasProperty("nome", Matchers.is("Analise de Teste")),
                        Matchers.hasProperty("codigo", Matchers.is("1"))
                )),
                Matchers.hasItem(Matchers.allOf(
                        Matchers.hasProperty("nome", Matchers.is("Analise de Requisitos")),
                        Matchers.hasProperty("codigo", Matchers.is("2"))
                ))
        ));
    }
}