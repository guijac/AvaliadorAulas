package br.edu.pucsp.avaliador.dto;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "professores")
public class ProfessorEntity extends MembroAcademicoEntity {
    @DBRef
    private List<DisciplinaEntity> disciplinasAptoALecionar;

    public ProfessorEntity(String primeiroNome, String sobreNome, List<DisciplinaEntity> disciplinasAptoALecionar) {
        super(primeiroNome, sobreNome);
        this.disciplinasAptoALecionar = disciplinasAptoALecionar;
    }

    public List<DisciplinaEntity> getDisciplinasAptoALecionar() {
        return disciplinasAptoALecionar;
    }
}
