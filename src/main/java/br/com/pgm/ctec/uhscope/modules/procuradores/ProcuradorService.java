package br.com.pgm.ctec.uhscope.modules.procuradores;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pgm.ctec.uhscope.modules.procuradores.dto.CreateProcuradorDTO;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import jakarta.validation.ValidationException;

@Service
public class ProcuradorService  {

    @Autowired
    ProcuradorRepository procuradorRepository;

    public ArrayList<ProcuradorEntity> getAll() {
        
        ArrayList<ProcuradorEntity> list = (ArrayList<ProcuradorEntity>) this.procuradorRepository.findAll();
        return list;
    
    }

    public ProcuradorEntity create(CreateProcuradorDTO createProcuradorDTO) throws ValidationException{
        if (this.procuradorRepository.findByCpf(createProcuradorDTO.getCpf()).isPresent()) {
            throw new ValidationException("CPF já cadastrado"); // Retorna erro se o CPF já existe
        }

        ProcuradorEntity procurador = new ProcuradorEntity();
            procurador.setMatricula(createProcuradorDTO.getMatricula());
            String cpfFormatted = createProcuradorDTO.getCpf().replace(".", "").replace("-", "");
            procurador.setCpf(cpfFormatted);
            procurador.setNome(createProcuradorDTO.getNome());
            procurador.setData_entrada(createProcuradorDTO.getData_entrada()); 
        
        ProcuradorEntity procuradorSaved = (ProcuradorEntity) this.procuradorRepository.save(procurador);
        return procuradorSaved;

    }
    
}
