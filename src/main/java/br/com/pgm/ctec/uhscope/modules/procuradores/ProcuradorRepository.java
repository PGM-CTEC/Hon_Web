package br.com.pgm.ctec.uhscope.modules.procuradores;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;

public interface ProcuradorRepository extends JpaRepository<ProcuradorEntity, String> {
    Optional<ProcuradorEntity> findByCpf(String email);
    
}
