package br.com.pgm.ctec.uhscope.modules.procuradores;

import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.pgm.ctec.uhscope.modules.procuradores.dto.CreateProcuradorDTO;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import br.com.pgm.ctec.uhscope.modules.utils.MethodsUtils;
import jakarta.validation.ValidationException;

@Service
public class ProcuradorService  {

    @Autowired
    ProcuradorRepository procuradorRepository;

    @Autowired
    MethodsUtils methodsUtils;

    public ArrayList<ProcuradorEntity> getAll() {
        
        ArrayList<ProcuradorEntity> list = (ArrayList<ProcuradorEntity>) this.procuradorRepository.findAll();
        return list;
    
    }

    public ProcuradorEntity create(CreateProcuradorDTO createProcuradorDTO) throws ValidationException {
        String cpfFormatted = createProcuradorDTO.getCpf().replace(".", "").replace("-", "");
    
        // Verifica se a matrícula já existe
        if (this.procuradorRepository.findById(createProcuradorDTO.getMatricula()).isPresent()) {
            throw new ValidationException("Matrícula já cadastrada. Não é possível sobrescrever.");
        }
    
        // Verifica se o CPF já existe no banco com o formato correto
        if (this.procuradorRepository.findByCpf(cpfFormatted).isPresent()) {
            throw new ValidationException("CPF já cadastrado");
        }
    
        ProcuradorEntity procurador = new ProcuradorEntity();
        procurador.setMatricula(createProcuradorDTO.getMatricula());
        procurador.setCpf(cpfFormatted); // Já armazenamos o CPF formatado no banco
        procurador.setNome(createProcuradorDTO.getNome());
    
        // Converte a data de entrada para o formato correto
        LocalDate dataConvertida = methodsUtils.convertDate(createProcuradorDTO.getData_entrada());
        if (dataConvertida == null) {
            throw new ValidationException("Formato de data inválido. Use um formato válido como dd/MM/yyyy, MM/dd/yyyy ou yyyy-MM-dd.");
        }
        procurador.setData_entrada(dataConvertida);
    
        // Salva o procurador no repositório
        return this.procuradorRepository.save(procurador);
    }
    
    
    
    
}
