package br.com.pgm.ctec.uhscope.modules.afastamento;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.pgm.ctec.uhscope.modules.afastamento.dto.CreateAfastamentoDTO;
import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import br.com.pgm.ctec.uhscope.modules.procuradores.ProcuradorRepository;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import br.com.pgm.ctec.uhscope.modules.utils.MethodsUtils;
import br.com.pgm.ctec.uhscope.modules.utils.Relatorio;
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

        if (dataFimConverted.isEqual(dataInicioConverted)) {
            throw new ValidationException("A data de volta não pode ser igual à data de início.");
        }

        if (dataInicioConverted == null || dataFimConverted == null) {
            throw new ValidationException("Formato de data inválido. Use um formato válido como dd/MM/yyyy, MM/dd/yyyy ou yyyy-MM-dd.");
        }
        
        ArrayList<AfastamentoEntity> afastamentos = this.afastamentoRepository.getByProcurador_matricula(matricula);
        for (AfastamentoEntity afast : afastamentos) {
            if (afast.getDataInicio().isEqual(dataInicioConverted)) {
                throw new ValidationException("Já existe um afastamento registrado com esta data de início.");
            }

            else if ((dataInicioConverted.isAfter(afast.getDataInicio())) && ((dataInicioConverted.isBefore(afast.getDataFim()))))
            {
                throw new ValidationException("Já existe um afastamento registrado nesse intervalo.");
            }
        }

        // Buscar procurador pelo número de matrícula
        ProcuradorEntity procurador = procuradorRepository.findByMatricula(matricula);

        if (procurador==null) {
            throw new ValidationException("Procurador de matrícula inexistente " + matricula + " não encontrado.");
        }

        // Calcular diferença em anos
        int anosDeDiferenca = (int) ChronoUnit.YEARS.between(dataInicioConverted, dataFimConverted);
        System.out.println(ChronoUnit.YEARS.between(dataInicioConverted, dataFimConverted));

        // Configurar afastamento
        afastamento.setDataInicio(dataInicioConverted);
        afastamento.setDataFim(dataFimConverted);
        afastamento.setUhAfastamento(anosDeDiferenca);
        afastamento.setProcurador(procurador); // Definir o procurador

        return this.afastamentoRepository.save(afastamento);
    }

    public ArrayList<AfastamentoEntity> getAll(String matricula){
        ArrayList<AfastamentoEntity> afastamentos = this.afastamentoRepository.getByProcurador_matricula(matricula);

        return afastamentos;

    }

    public ArrayList<Relatorio> getAllRelatorios() {
        ArrayList<Relatorio> relatorios = new ArrayList<>();
        List<ProcuradorEntity> procuradores = this.procuradorRepository.findAll(); // Alterado para List
        for (ProcuradorEntity procurador : procuradores) {
            List<AfastamentoEntity> afastamentos = new ArrayList<>(procurador.getAfastamentos());
            double uh = this.methodsUtils.calculaUH(afastamentos, procurador);
           
            Relatorio relatorio = new Relatorio();
            
            relatorio.setCpf(procurador.getCpf());
            relatorio.setMatricula(procurador.getMatricula());
            relatorio.setNome(procurador.getNome());
            relatorio.setUh(uh);
            relatorios.add(relatorio);
        }
    
        return relatorios;
    }
    
}
