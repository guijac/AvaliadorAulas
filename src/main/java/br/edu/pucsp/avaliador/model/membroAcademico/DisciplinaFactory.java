package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dao.CounterService;
import br.edu.pucsp.avaliador.dao.DisciplinaRepository;
import br.edu.pucsp.avaliador.dto.DisciplinaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaFactory {
    private DisciplinaRepository disciplinaRepository;
    private CounterService counterService;

    @Autowired
    public DisciplinaFactory(DisciplinaRepository disciplinaRepository, CounterService counterService) {
        this.disciplinaRepository = disciplinaRepository;
        this.counterService = counterService;
    }

    public Disciplina criar(String disciplinaNome) {
        String codigo = "" + counterService.getNextSequence("disciplinas");
        DisciplinaEntity disciplina = new DisciplinaEntity(disciplinaNome, codigo);


        DisciplinaEntity disciplinaFromRepoSameCodigo = disciplinaRepository.findByCodigo(disciplina.getCodigo());
        DisciplinaEntity disciplinaFromRepoSameNome = disciplinaRepository.findByNome(disciplina.getNome());
        if (disciplinaFromRepoSameCodigo == null && disciplinaFromRepoSameNome == null) {
            DisciplinaEntity disciplinaFromRepo = disciplinaRepository.insert(disciplina);
            if (disciplinaFromRepo != null && disciplinaFromRepo.equals(disciplina)) {
                return new Disciplina(disciplinaFromRepo.getNome(), disciplinaFromRepo.getCodigo());
            }
        }
        return null;//TODO
    }
}
