package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.dao.*;
import br.edu.pucsp.avaliador.entities.*;
import br.edu.pucsp.avaliador.model.Avaliacao;
import br.edu.pucsp.avaliador.model.membroAcademico.Aluno;
import br.edu.pucsp.avaliador.model.membroAcademico.Aula;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AulaControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private GradeHorariaRepository gradeHorariaRepository;
    @Autowired
    private MongoOperations mongo;
    private AlunoEntity aluno;
    private Aula aula;

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
        aulaRepository.deleteAll();
        professorRepository.deleteAll();
        disciplinaRepository.deleteAll();
        alunoRepository.deleteAll();
        gradeHorariaRepository.deleteAll();


        cordenadorRepository.deleteAll();
        usuarioRepository.deleteAll();

        cordenador = cordenadorService.criar("Cordenador", "Para Teste");
        ResponseEntity<Usuario> usuarioResponse = this.restTemplate
                .postForEntity("/usuario/criar", new Usuario(cordenador.getRegistroAcademico(), "1234", "CORDENADOR"), Usuario.class);
        assertThat(usuarioResponse.getStatusCode(), Matchers.is(HttpStatus.OK));

        List<DisciplinaEntity> disciplinas = new ArrayList<>();
        disciplinas.add(new DisciplinaEntity("Analise de Teste", ""));
        disciplinas.add(new DisciplinaEntity("Analise de Requisitos", ""));
        ProfessorEntity professor = new ProfessorEntity("Daniel", "Gatti", disciplinas);

        ResponseEntity<ProfessorEntity> professorFromDB = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/RecursosHumanos/contratarProfessor", professor, ProfessorEntity.class);

        Disciplina diciplinaTestDeSoftware = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/Cordenador/cadastrarDisciplina", new Disciplina("Test de Software"), Disciplina.class).getBody();
        String codigoDisciplina = diciplinaTestDeSoftware.getCodigo();
        aula = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/Cordenador/cadastrarAula", new Aula(professorFromDB.getBody().getRegistroAcademico(), codigoDisciplina), Aula.class).getBody();
        aluno = this.restTemplate.postForEntity("/aluno/matricular", new Aluno("Nome1", "SobreNome"), AlunoEntity.class).getBody();
        ResponseEntity<Usuario> usuarioAlunoResponse = this.restTemplate
                .postForEntity("/usuario/criar", new Usuario(aluno.getRegistroAcademico(), "1234", "ALUNO"), Usuario.class);
        assertThat(usuarioAlunoResponse.getStatusCode(), Matchers.is(HttpStatus.OK));


    }

    @Test
    public void avaliar() {
        matricular();
        addicionarAgendamento();
        Avaliacao avaliacao = new Avaliacao(new Aluno(aluno), 5);
        ResponseEntity<AulaEntity> response = this.restTemplate
                .withBasicAuth(aluno.getRegistroAcademico(), "1234")
                .postForEntity("/aula/avaliar/" + aula.getId(), avaliacao, AulaEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getAgendamentos().get(0).getAvaliacoes().get(0), Matchers.allOf(
                Matchers.hasProperty("aluno", Matchers.is(aluno)),
                Matchers.hasProperty("numeroEstrelas", Matchers.is(5))
        ));
    }

    @Test
    public void matricular() {
        ResponseEntity<AulaEntity> response = this.restTemplate
                .withBasicAuth(aluno.getRegistroAcademico(), "1234")
                .postForEntity("/aula/matricular/" + aula.getId(), aluno, AulaEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getAlunosMatriculados().get(0), Matchers.allOf(
                Matchers.hasProperty("primeiroNome", Matchers.is("Nome1")),
                Matchers.hasProperty("sobreNome", Matchers.is("SobreNome")),
                Matchers.hasProperty("registroAcademico", Matchers.is("RA00000003"))
        ));
    }

    @Test
    public void addicionarAgendamento() {
        LocalDateTime horaInicio = LocalDateTime.now().minusDays(1).minusHours(4);
        AgendamentoDeAulaEntity agendamento = new AgendamentoDeAulaEntity(horaInicio, 50);
        ResponseEntity<AulaEntity> response = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/aula/addicionarAgendamento/" + aula.getId(), agendamento, AulaEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertTrue(response.getBody().getAgendamentos().contains(agendamento));
    }
}