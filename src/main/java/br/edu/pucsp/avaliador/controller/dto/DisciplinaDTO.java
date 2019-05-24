package br.edu.pucsp.avaliador.controller.dto;

public class DisciplinaDTO {
    private String nome;
    private String codigo;

    public DisciplinaDTO(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }
    public DisciplinaDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }
}
