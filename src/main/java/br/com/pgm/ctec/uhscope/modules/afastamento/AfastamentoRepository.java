package br.com.pgm.ctec.uhscope.modules.afastamento;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;

public interface AfastamentoRepository extends JpaRepository<AfastamentoEntity, UUID> {
}