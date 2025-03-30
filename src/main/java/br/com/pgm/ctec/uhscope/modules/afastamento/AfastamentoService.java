package br.com.pgm.ctec.uhscope.modules.afastamento;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.pgm.ctec.uhscope.modules.afastamento.dto.CreateAfastamentoDTO;
import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import br.com.pgm.ctec.uhscope.modules.procuradores.ProcuradorRepository;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import br.com.pgm.ctec.uhscope.modules.utils.MethodsUtils;
import jakarta.validation.ValidationException;

@Service
public class AfastamentoService {

    @Autowired
    MethodsUtils methodsUtils;

    @Autowired
    AfastamentoRepository afastamentoRepository;

    @Autowired
    ProcuradorRepository procuradorRepository;

    public AfastamentoEntity create(CreateAfastamentoDTO createAfastamentoDTO, String matricula) throws ValidationException {
    
    AfastamentoEntity afastamento = new AfastamentoEntity();

    LocalDate dataInicioConverted = methodsUtils.convertDate(createAfastamentoDTO.getDataInicio());
    LocalDate dataFimConverted = methodsUtils.convertDate(createAfastamentoDTO.getDataFim());

    if (dataFimConverted.isBefore(dataInicioConverted)) {
        throw new ValidationException("A data de volta não pode ser anterior à data de início.");
    }

    if (dataInicioConverted == null || dataFimConverted == null) {
        throw new ValidationException("Formato de data inválido. Use um formato válido como dd/MM/yyyy, MM/dd/yyyy ou yyyy-MM-dd.");
    }

    // Buscar procurador pelo número de matrícula
    ProcuradorEntity procurador = procuradorRepository.findByMatricula(matricula);

    if (procurador==null) {
        throw new ValidationException("Procurador de matrícula inexistente " + matricula + " não encontrado.");
    }

    // Calcular diferença em anos
    int anosDeDiferenca = (int) ChronoUnit.YEARS.between(dataInicioConverted, dataFimConverted);

    // Configurar afastamento
    afastamento.setDataInicio(dataInicioConverted);
    afastamento.setDataFim(dataFimConverted);
    afastamento.setUhAfastamento(anosDeDiferenca);
    afastamento.setProcurador(procurador); // Definir o procurador

    return this.afastamentoRepository.save(afastamento);
}

}
