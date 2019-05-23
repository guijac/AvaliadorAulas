package br.edu.pucsp.avaliador.model.membroAcademico;

import br.edu.pucsp.avaliador.dao.CounterService;
import br.edu.pucsp.avaliador.dao.MembroAcademicoRepository;
import br.edu.pucsp.avaliador.entities.MembroAcademicoEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class MembroAcademicoFactory<E extends MembroAcademicoEntity> {
    private CounterService counterService;
    private MembroAcademicoRepository<E> repository;

    protected MembroAcademicoFactory(CounterService counterService, MembroAcademicoRepository<E> repository){
        this.counterService = counterService;
        this.repository = repository;
    }

    private String getProximoRegistroAcademico(){
        int raValue = counterService.getNextSequence("MembrosAcademico");
        return formatRA(raValue);
    }

    @NotNull
    private String formatRA(int raValue) {
        return "RA"+String.format("%08d", raValue);
    }

    protected E criar(E membroAcademico){
        membroAcademico.setRegistroAcademico(getProximoRegistroAcademico());
        return repository.insert(membroAcademico);

    }

    public Optional<E> encontraPorRegistroAcademico(String registroAcademico){
        return repository.findByRegistroAcademico(registroAcademico);
    }

    public E atualizar(E alunoEntity) {
        return repository.save(alunoEntity);
    }
}
