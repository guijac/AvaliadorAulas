package br.edu.pucsp.avaliador.dto;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class GradeHorariaDTO {
    @DBRef
    private List<AulaEntity> aulas;

    public List<AulaEntity> getAulas() {
        return aulas;
    }

    public void setAulas(List<AulaEntity> aulas) {
        this.aulas = aulas;
    }
}
