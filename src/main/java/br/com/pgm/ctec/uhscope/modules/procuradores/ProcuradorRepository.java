package br.com.pgm.ctec.uhscope.modules.procuradores;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;

public interface ProcuradorRepository extends JpaRepository<ProcuradorEntity, String> {
    ProcuradorEntity findByCpf(String email);
    ProcuradorEntity findByMatricula(String matricula);
    
}
