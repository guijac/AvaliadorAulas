package br.edu.pucsp.avaliador.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "gradeHoraria")
public class GradeHorariaEntity {
    @Id
    private String registroAcademico;
    @DBRef
    private MembroAcademicoEntity membroAcademico;
    @DBRef
    private List<AulaEntity> aulas;

    public GradeHorariaEntity(MembroAcademicoEntity membroAcademico) {
        this.registroAcademico = membroAcademico.getRegistroAcademico();
        this.membroAcademico = membroAcademico;
        this.aulas = new ArrayList<>();
    }

    public List<AulaEntity> getAulas() {
        return aulas;
    }

    public void setAulas(List<AulaEntity> aulas) {
        this.aulas = aulas;
    }

    public void incluirAula(AulaEntity aula) {
        //TODO verificar conflitos de aulas
        aulas.add(aula);
    }


    public String avaliacaoMedia(GradeHorariaEntity gradeHorariaOptional) {
        List<Integer> avaliacoesTotais = gradeHorariaOptional.getAulas().stream()
                .flatMap(aula -> aula.getAgendamentos().stream())
                .flatMap(agendamento -> agendamento.getAvaliacoes().stream())
                .filter(a -> a.getNumeroEstrelas() != null)
                .filter(a -> a.getNumeroEstrelas() >= 1)
                .filter(a -> a.getNumeroEstrelas() <= 5)
                .map(AvaliacaoEntity::getNumeroEstrelas).collect(Collectors.toList());

        float extrelasTotais = 0;
        for (Integer nrEstrelas : avaliacoesTotais) {
            extrelasTotais = extrelasTotais + nrEstrelas;
        }

        return String.valueOf(extrelasTotais / avaliacoesTotais.size());
    }
}
