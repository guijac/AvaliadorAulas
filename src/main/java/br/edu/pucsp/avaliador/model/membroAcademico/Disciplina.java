package br.edu.pucsp.avaliador.model.membroAcademico;

public class Disciplina {
        private String nome;
        private String codigo;

        public Disciplina(String nome, String codigo) {
            this.nome = nome;
            this.codigo = codigo;
        }

    public Disciplina(String nome) {
        this.nome = nome;
    }

    public String getNome() {
            return nome;
        }

        public String getCodigo() {
            return codigo;
        }
}
