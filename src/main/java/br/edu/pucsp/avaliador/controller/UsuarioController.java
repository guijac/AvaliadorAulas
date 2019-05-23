package br.edu.pucsp.avaliador.controller;

import br.edu.pucsp.avaliador.entities.*;
import br.edu.pucsp.avaliador.model.membroAcademico.AlunoService;
import br.edu.pucsp.avaliador.model.membroAcademico.CordenadorService;
import br.edu.pucsp.avaliador.model.membroAcademico.ProfessorService;
import br.edu.pucsp.avaliador.model.membroAcademico.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;
    private AlunoService alunoService;
    private ProfessorService professorService;
    private CordenadorService cordenadorService;


    @Autowired
    public UsuarioController(UsuarioService usuarioService, AlunoService alunoService, ProfessorService professorService, CordenadorService cordenadorService) {
        this.usuarioService = usuarioService;
        this.alunoService = alunoService;
        this.professorService = professorService;
        this.cordenadorService = cordenadorService;
    }

    @GetMapping()
    public ResponseEntity<MembroAcademicoEntity> encontrarUsuario(HttpServletRequest request) {
        Optional<Usuario> usuario = usuarioService.encontrarUsuario(request.getUserPrincipal().getName());
        if (!usuario.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Usuario não existe.");
        }

        MembroAcademicoEntity membroAcademico = validarUsuario(usuario.get().getNomeDeUsuario(), usuario.get().getRole());
        return new ResponseEntity<>(membroAcademico, HttpStatus.OK);
    }

    @PostMapping("/criar")
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        String nomeDeUsuario = usuario.getNomeDeUsuario();
        String senha = usuario.getSenha();
        String role = usuario.getRole();

        if (nomeDeUsuario != null && nomeDeUsuario.matches("^RA[0-9]{9}$")) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "O nome de usuario deve ser do formato (RAxxxxxxxx).");
        }

        if (senha != null && !senha.matches("^[a-zA-Z0-9]{4,8}$")) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "A senha deve conter de 4 a 8 caracteres alfanumericos.");
        }
        validarUsuario(nomeDeUsuario, role);
        Usuario usuarioCriado = usuarioService.criarUsuario(nomeDeUsuario, senha, role);
        if (usuarioCriado == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao criar usuario.");
        }
        return new ResponseEntity<>(usuarioCriado, HttpStatus.OK);
    }

    private MembroAcademicoEntity validarUsuario(String nomeDeUsuario, String role) {
        switch (role) {
            case "ALUNO":
                Optional<AlunoEntity> aluno = alunoService.encontraPorRegistroAcademico(nomeDeUsuario);
                if (!aluno.isPresent()) {
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, String.format("O ra (%s) não pertence a um aluno valido.", nomeDeUsuario));
                }
                return aluno.get();
            case "PROFESSOR":
                Optional<ProfessorEntity> professor = professorService.encontraPorRegistroAcademico(nomeDeUsuario);
                if (!professor.isPresent()) {
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, String.format("O ra (%s) não pertence a um professor valido.", nomeDeUsuario));
                }
                return professor.get();
            case "CORDENADOR":
                Optional<CordenadorEntity> cordenador = cordenadorService.encontraPorRegistroAcademico(nomeDeUsuario);
                if (!cordenador.isPresent()) {
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, String.format("O ra (%s) não pertence a um cordenador valido.", nomeDeUsuario));
                }
                return cordenador.get();
            default: {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Opção de papel invalida.");
            }
        }
    }
}
