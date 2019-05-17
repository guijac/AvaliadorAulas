package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.dao.AlunoRepository;
import br.edu.pucsp.avaliador.dao.AulaRepository;
import br.edu.pucsp.avaliador.dao.DisciplinaRepository;
import br.edu.pucsp.avaliador.dao.ProfessorRepository;
import br.edu.pucsp.avaliador.dto.*;
import br.edu.pucsp.avaliador.model.Avaliacao;
import br.edu.pucsp.avaliador.model.membroAcademico.Aluno;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
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
    private MongoOperations mongo;
    private AlunoEntity aluno;
    private Aula aula;

    @Before
    public void setup() {

        mongo.dropCollection("counters");
        aulaRepository.deleteAll();
        professorRepository.deleteAll();
        disciplinaRepository.deleteAll();
        alunoRepository.deleteAll();

        List<DisciplinaEntity> disciplinas = new ArrayList<>();
        disciplinas.add(new DisciplinaEntity("Analise de Teste", ""));
        disciplinas.add(new DisciplinaEntity("Analise de Requisitos", ""));
        ProfessorEntity professor = new ProfessorEntity("Daniel", "Gatti", disciplinas);

        ResponseEntity<ProfessorEntity> professorFromDB = this.restTemplate.postForEntity("/RecursosHumanos/contratarProfessor", professor, ProfessorEntity.class);

        Disciplina diciplinaTestDeSoftware = this.restTemplate.postForEntity("/Cordenador/cadastrarDisciplina", new Disciplina("Test de Software"), Disciplina.class).getBody();
        String codigoDisciplina = diciplinaTestDeSoftware.getCodigo();
        aula = this.restTemplate.postForEntity("/Cordenador/cadastrarAula", new Aula(professorFromDB.getBody().getRegistroAcademico(), codigoDisciplina), Aula.class).getBody();
        aluno = this.restTemplate.postForEntity("/aluno/matricular", new Aluno("Nome1", "SobreNome"), AlunoEntity.class).getBody();


    }

    @Test
    public void avaliar() {
        matricular();
        addicionarAgendamento();
        Avaliacao avaliacao = new Avaliacao(new Aluno(aluno), 5);
        ResponseEntity<AulaEntity> response = this.restTemplate.postForEntity("/aula/avaliar/" + aula.getId(), avaliacao, AulaEntity.class);
        assertThat(response.getBody().getAgendamentos().get(0).getAvaliacoes().get(0), Matchers.allOf(
                Matchers.hasProperty("aluno", Matchers.is(aluno)),
                Matchers.hasProperty("numeroEstrelas", Matchers.is(5))
        ));
    }

    @Test
    public void matricular() {
        ResponseEntity<AulaEntity> response = this.restTemplate.postForEntity("/aula/matricular/" + aula.getId(), aluno, AulaEntity.class);
        assertThat(response.getBody().getAlunosMatriculados().get(0), Matchers.allOf(
                Matchers.hasProperty("primeiroNome", Matchers.is("Nome1")),
                Matchers.hasProperty("sobreNome", Matchers.is("SobreNome")),
                Matchers.hasProperty("registroAcademico", Matchers.is("RA00000002"))
        ));
    }

    @Test
    public void addicionarAgendamento() {
        LocalDate data = LocalDate.now().minusDays(1);
        LocalTime horaInicio = LocalTime.now().minusHours(4);
        LocalTime horaFim = LocalTime.now().minusHours(2);
        AgendamentoDeAulaEntity agendamento = new AgendamentoDeAulaEntity(data, horaInicio, horaFim);
        ResponseEntity<AulaEntity> response = this.restTemplate.postForEntity("/aula/addicionarAgendamento/" + aula.getId(), agendamento, AulaEntity.class);

        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertTrue(response.getBody().getAgendamentos().contains(agendamento));
    }
}