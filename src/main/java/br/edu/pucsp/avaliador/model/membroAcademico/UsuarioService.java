package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dao.UsuarioRepository;
import br.edu.pucsp.avaliador.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String nomeDeUsuario) throws UsernameNotFoundException {
        Optional<Usuario> usuario = encontrarUsuario(nomeDeUsuario);
        if (usuario.isPresent()) {
            Collection<? extends GrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+usuario.get().getRole()));
            return new User(usuario.get().getNomeDeUsuario(), usuario.get().getSenha(), roles);
        }
        return null;
    }

    public Optional<Usuario> encontrarUsuario(String nomeDeUsuario) {
        return usuarioRepository.findByNomeDeUsuario(nomeDeUsuario);
    }

    public Usuario criarUsuario(String nomeDeusuario, String senha, String role) {

        return usuarioRepository.insert(new Usuario(nomeDeusuario, passwordEncoder.encode(senha), role));

    }
}
