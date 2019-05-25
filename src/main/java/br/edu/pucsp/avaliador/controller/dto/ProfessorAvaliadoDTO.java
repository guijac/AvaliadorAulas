package br.edu.pucsp.avaliador.controller.dto;

public class ProfessorAvaliadoDTO {
    private MembroAcademicoDTO professor;
    private String numeroDeEstrelas;

    public ProfessorAvaliadoDTO(MembroAcademicoDTO professor, String numeroDeEstrelas) {

        this.professor = professor;
        this.numeroDeEstrelas = numeroDeEstrelas;
    }

    public String getNumeroDeEstrelas() {
        return numeroDeEstrelas;
    }

    public MembroAcademicoDTO getProfessor() {
        return professor;
    }
}
