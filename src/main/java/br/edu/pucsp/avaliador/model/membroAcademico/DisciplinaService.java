package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.controller.dto.DisciplinaDTO;
import br.edu.pucsp.avaliador.dao.CounterService;
import br.edu.pucsp.avaliador.dao.DisciplinaRepository;
import br.edu.pucsp.avaliador.entities.DisciplinaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaService {
    private DisciplinaRepository disciplinaRepository;
    private CounterService counterService;

    @Autowired
    public DisciplinaService(DisciplinaRepository disciplinaRepository, CounterService counterService) {
        this.disciplinaRepository = disciplinaRepository;
        this.counterService = counterService;
    }

    public DisciplinaDTO criar(String disciplinaNome) {
        String codigo = "" + counterService.getNextSequence("disciplinas");
        DisciplinaEntity disciplina = new DisciplinaEntity(disciplinaNome, codigo);


        DisciplinaEntity disciplinaFromRepoSameCodigo = disciplinaRepository.findByCodigo(disciplina.getCodigo());
        DisciplinaEntity disciplinaFromRepoSameNome = disciplinaRepository.findByNome(disciplina.getNome());
        if (disciplinaFromRepoSameCodigo == null && disciplinaFromRepoSameNome == null) {
            DisciplinaEntity disciplinaFromRepo = disciplinaRepository.insert(disciplina);
            if (disciplinaFromRepo != null && disciplinaFromRepo.equals(disciplina)) {
                return disciplinaFromRepo.getDTO();
            }
        }
        return null;//TODO
    }
}
