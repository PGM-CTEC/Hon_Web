package br.com.pgm.ctec.uhscope.modules.procuradores;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.pgm.ctec.uhscope.exceptions.ProcuradorNotFoundException;
import br.com.pgm.ctec.uhscope.modules.afastamento.AfastamentoRepository;
import br.com.pgm.ctec.uhscope.modules.procuradores.dto.CreateProcuradorDTO;
import br.com.pgm.ctec.uhscope.modules.procuradores.dto.UpdateProcuradorDTO;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import br.com.pgm.ctec.uhscope.modules.utils.MethodsUtils;
import jakarta.validation.ValidationException;

@Service
public class ProcuradorService  {

    @Autowired
    ProcuradorRepository procuradorRepository;

    @Autowired
    AfastamentoRepository afastamentoRepository;

    @Autowired
    MethodsUtils methodsUtils;

    public ArrayList<ProcuradorEntity> getAll() { 
        ArrayList<ProcuradorEntity> list = (ArrayList<ProcuradorEntity>) this.procuradorRepository.findAll();
        return list;
    }

    public ProcuradorEntity create(CreateProcuradorDTO createProcuradorDTO) throws ValidationException {
        String cpfFormatted = createProcuradorDTO.getCpf().replace(".", "").replace("-", "");
    
        if (this.procuradorRepository.existsById(createProcuradorDTO.getMatricula())) {
            throw new ValidationException("Matrícula já cadastrada. Não é possível sobrescrever.");
        }
    
        if (this.procuradorRepository.findByCpf(cpfFormatted) != null) {
            throw new ValidationException("CPF já cadastrado");
        }
    
        ProcuradorEntity procurador = new ProcuradorEntity();
        procurador.setMatricula(createProcuradorDTO.getMatricula());
        procurador.setCpf(cpfFormatted);
        procurador.setNome(createProcuradorDTO.getNome());
    
        LocalDate dataConvertida = methodsUtils.convertDate(createProcuradorDTO.getData_entrada());
        if (dataConvertida == null) {
            throw new ValidationException("Formato de data inválido.");
        }
        procurador.setData_entrada(dataConvertida);
    
        return this.procuradorRepository.save(procurador);
    }

    @Transactional
    public ProcuradorEntity update(UpdateProcuradorDTO updateProcuradorDTO, String matricula) throws ValidationException, ProcuradorNotFoundException {
        ProcuradorEntity procurador = this.procuradorRepository.findByMatricula(matricula);
        
        if (procurador == null) {
            throw new ProcuradorNotFoundException("Procurador não existe!");
        }
        
        if (updateProcuradorDTO.getNome() != null && !updateProcuradorDTO.getNome().isEmpty()) {
            procurador.setNome(updateProcuradorDTO.getNome());
        }
        
        if (updateProcuradorDTO.getData_entrada() != null && !updateProcuradorDTO.getData_entrada().isEmpty()) {
            LocalDate dataConvertida = methodsUtils.convertDate(updateProcuradorDTO.getData_entrada());
            if (dataConvertida == null) {
                throw new ValidationException("Formato de data inválido.");
            }
            procurador.setData_entrada(dataConvertida);
        }

        return this.procuradorRepository.save(procurador);
    }

    @Transactional
    public ProcuradorEntity delete(String matricula) throws ValidationException, ProcuradorNotFoundException {     
        ProcuradorEntity procurador = this.procuradorRepository.findByMatricula(matricula);
        if(procurador==null){
            throw new ProcuradorNotFoundException("Procurador não encontrado!");
        }

        else if(procurador.getAfastamentos().isEmpty())
        {
            this.procuradorRepository.deleteByMatricula(matricula);
            return procurador;
        }

        this.afastamentoRepository.deleteByProcurador_matricula(matricula);
        this.procuradorRepository.deleteByMatricula(matricula);

        return procurador;    
       
    }
}
