package br.com.pgm.ctec.uhscope.modules.afastamento;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;

public interface AfastamentoRepository extends JpaRepository<AfastamentoEntity, UUID> {
    public ArrayList<AfastamentoEntity> getByProcurador_matricula(String matricula);
    public void deleteByProcurador_matricula(String matricula);

    
}