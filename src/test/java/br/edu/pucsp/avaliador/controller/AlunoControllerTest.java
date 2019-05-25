package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.controller.dto.AulaParaAvaliacao;
import br.edu.pucsp.avaliador.controller.dto.DisciplinaDTO;
import br.edu.pucsp.avaliador.dao.*;
import br.edu.pucsp.avaliador.entities.*;
import br.edu.pucsp.avaliador.model.membroAcademico.Aluno;
import br.edu.pucsp.avaliador.model.membroAcademico.Aula;
import br.edu.pucsp.avaliador.model.membroAcademico.CordenadorService;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
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
    private GradeHorariaRepository gradeHorariaRepository;
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

    }

    @Test
    public void matricular() {
        Aluno aluno1 = new Aluno("Nome1", "SobreNome");
        Aluno aluno2 = new Aluno("Nome2", "SobreNome");
        ResponseEntity<AlunoEntity> response1 = this.restTemplate.postForEntity("/aluno/matricular", aluno1, AlunoEntity.class);
        assertThat(response1.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response1.getBody(), Matchers.allOf(
                Matchers.hasProperty("primeiroNome", Matchers.is("Nome1")),
                Matchers.hasProperty("sobreNome", Matchers.is("SobreNome")),
                Matchers.hasProperty("registroAcademico", Matchers.is("RA00000002"))
        ));

        ResponseEntity<AlunoEntity> response2 = this.restTemplate.postForEntity("/aluno/matricular", aluno2, AlunoEntity.class);
        assertThat(response2.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response2.getBody(), Matchers.allOf(
                Matchers.hasProperty("primeiroNome", Matchers.is("Nome2")),
                Matchers.hasProperty("sobreNome", Matchers.is("SobreNome")),
                Matchers.hasProperty("registroAcademico", Matchers.is("RA00000003"))
        ));
    }

    @Test
    public void getAulasDisponiveisParaAvaliacao() {
        List<DisciplinaEntity> disciplinas = new ArrayList<>();
        disciplinas.add(new DisciplinaEntity("Analise de Teste", ""));
        disciplinas.add(new DisciplinaEntity("Analise de Requisitos", ""));

        ProfessorEntity professorDaniel = new ProfessorEntity("Daniel", "Gatti", disciplinas);
        ResponseEntity<ProfessorEntity> professorDanielFromDB = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/recursosHumanos/contratar/professor", professorDaniel, ProfessorEntity.class);

        ProfessorEntity professor = new ProfessorEntity("Renato", "Manzan", disciplinas);
        ResponseEntity<ProfessorEntity> professorRenatoFromDB = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/recursosHumanos/contratar/professor", professor, ProfessorEntity.class);

        LocalDateTime localTime = LocalDateTime.now();


        ResponseEntity<AlunoEntity> alunoMatriculado = this.restTemplate.postForEntity("/aluno/matricular", new Aluno("Nome1", "SobreNome"), AlunoEntity.class);
        assertThat(alunoMatriculado.getStatusCode(), Matchers.is(HttpStatus.OK));

        ResponseEntity<Usuario> usuarioAlunoResponse = this.restTemplate
                .postForEntity("/usuario/criar", new Usuario(alunoMatriculado.getBody().getRegistroAcademico(), "1234", "ALUNO"), Usuario.class);
        assertThat(usuarioAlunoResponse.getStatusCode(), Matchers.is(HttpStatus.OK));


        adicionarAula(professorDanielFromDB, "Testes 1", alunoMatriculado, localTime.minusDays(2).minusHours(2));
        adicionarAula(professorDanielFromDB, "Testes 2", alunoMatriculado, localTime.minusDays(1).minusHours(2));
        adicionarAula(professorDanielFromDB, "Testes 3", alunoMatriculado, localTime.minusHours(2));
        adicionarAula(professorDanielFromDB, "Testes 4", alunoMatriculado, localTime.minusHours(1));
        adicionarAula(professorDanielFromDB, "Testes 5", alunoMatriculado, localTime);

        adicionarAula(professorRenatoFromDB, "Engenharia de SW 1", alunoMatriculado, localTime.minusDays(2).minusHours(4));
        adicionarAula(professorRenatoFromDB, "Engenharia de SW 2", alunoMatriculado, localTime.minusDays(1).minusHours(4));
        adicionarAula(professorRenatoFromDB, "Engenharia de SW 3", alunoMatriculado, localTime.minusHours(4));
        adicionarAula(professorRenatoFromDB, "Engenharia de SW 4", alunoMatriculado, localTime.plusHours(1));
        adicionarAula(professorRenatoFromDB, "Engenharia de SW 5", alunoMatriculado, localTime);


        String url = "/aluno/aulasDisponiveisParaAvaliacao";
        ResponseEntity<ArrayList<AulaParaAvaliacao>> aulasParaAvaliacao = this.restTemplate
                .withBasicAuth(alunoMatriculado.getBody().getRegistroAcademico(), "1234")
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<AulaParaAvaliacao>>() {
                });
        assertThat(aulasParaAvaliacao.getStatusCode(), is(HttpStatus.OK));

        assertNotNull(aulasParaAvaliacao.getBody());

        assertThat(aulasParaAvaliacao.getBody().size(), is(4));

        assertThat(aulasParaAvaliacao.getBody().get(0).getDisciplina(), allOf(
                hasProperty("codigo", is("2"))
        ));
        assertThat(aulasParaAvaliacao.getBody().get(1).getDisciplina(), allOf(
                hasProperty("codigo", is("3"))
        ));
    }

    @NotNull
    private Aula adicionarAula(ResponseEntity<ProfessorEntity> professorFromDB, String nomeAula, ResponseEntity<AlunoEntity> alunoMatricula, LocalDateTime horaInicio) {
        DisciplinaDTO diciplinaTestDeSoftware = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/cordenador/cadastrarDisciplina", new DisciplinaDTO(nomeAula), DisciplinaDTO.class).getBody();
        String codigoDisciplina = diciplinaTestDeSoftware.getCodigo();
        Aula aula = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/cordenador/cadastrarAula", new Aula(professorFromDB.getBody().getRegistroAcademico(), codigoDisciplina), Aula.class).getBody();

        adicionarAgendamento(aula, horaInicio);

        ResponseEntity<AulaEntity> alunoAulaMatricula = this.restTemplate
                .withBasicAuth(alunoMatricula.getBody().getRegistroAcademico(), "1234")
                .postForEntity("/aula/matricular/" + aula.getId(), alunoMatricula.getBody(), AulaEntity.class);
        assertThat(alunoAulaMatricula.getStatusCode(), is(HttpStatus.OK));
        return aula;
    }

    private void adicionarAgendamento(Aula aula, LocalDateTime horaInicio) {
        AgendamentoDeAulaEntity agendamento = new AgendamentoDeAulaEntity(horaInicio, 120);
        ResponseEntity<AulaEntity> agendamentoResponse = this.restTemplate
                .withBasicAuth(cordenador.getRegistroAcademico(), "1234")
                .postForEntity("/aula/addicionarAgendamento/" + aula.getId(), agendamento, AulaEntity.class);
        assertThat(agendamentoResponse.getStatusCode(), is(HttpStatus.OK));
    }

}