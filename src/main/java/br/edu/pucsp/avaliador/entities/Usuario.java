package br.edu.pucsp.avaliador.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String nomeDeUsuario;
    private String senha;
    private String role;

    public Usuario(String nomeDeUsuario, String senha, String role) {
        this.nomeDeUsuario = nomeDeUsuario;
        this.senha = senha;
        this.role = role;
    }

    public String getNomeDeUsuario() {
        return nomeDeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public String getRole() {
        return role;
    }
}
