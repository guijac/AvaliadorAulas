package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dao.AlunoRepository;
import br.edu.pucsp.avaliador.dao.CounterService;
import br.edu.pucsp.avaliador.entities.AlunoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlunoService extends MembroAcademicoFactory<AlunoEntity> {
    @Autowired
    protected AlunoService(CounterService counterService, AlunoRepository repository) {
        super(counterService, repository);
    }


    public AlunoEntity matricular(String primeiroNome, String sobreNome) {
        AlunoEntity alunoCriado = super.criar(new AlunoEntity(primeiroNome, sobreNome));
        if (alunoCriado != null) {
            return alunoCriado;
        }
        return null;
    }


}
