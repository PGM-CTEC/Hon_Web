package br.com.pgm.ctec.uhscope.modules.relatorio;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import br.com.pgm.ctec.uhscope.modules.procuradores.ProcuradorRepository;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import br.com.pgm.ctec.uhscope.modules.utils.MethodsUtils;
import br.com.pgm.ctec.uhscope.modules.utils.Relatorio;
import jakarta.validation.ValidationException;

@Service
public class RelatorioService {

    @Autowired
    ProcuradorRepository procuradorRepository;

    // @Autowired
    // MethodsUtils methodsUtils;
    
    // public String get(double valor, int mes, int ano) throws ValidationException{
    //     int anoAtual = LocalDate.now().getYear();
    //     if(mes<1 || mes>12)
    //     {
    //         throw new ValidationException("Esse mês não existe! Insira um mês que exista (1-12)");
    //     }
    //     if(valor<=0){
    //         throw new ValidationException("O valor não pode ser menor ou igual a zero.");
    //     }
    //     if (ano < 1900 || ano > anoAtual) {
    //         throw new ValidationException("Ano inválido! Insira um ano entre 1900 e " + anoAtual + ".");
    //     }

    //     String dataInicio = String.format("%02d/%02d/%d", 1, mes, ano);
    //     ArrayList<ProcuradorEntity> procuradores = (ArrayList<ProcuradorEntity>) this.procuradorRepository.findAll();
    //     ArrayList<Relatorio> relatorios = new ArrayList<>();
    
    //     for (ProcuradorEntity procurador : procuradores) {
    //         List<AfastamentoEntity> afastamentos = new ArrayList<>(procurador.getAfastamentos());

    //         double uh = this.methodsUtils.calculaUHMensal(afastamentos, procurador);
            
    //         Relatorio relatorio = new Relatorio();
            
    //         relatorio.setCpf(procurador.getCpf());
    //         relatorio.setMatricula(procurador.getMatricula());
    //         relatorio.setNome(procurador.getNome());
    //         relatorio.setUh(uh);
    //         relatorio.setValor(uh);
    //         relatorios.add(relatorio);
    //     }
    
    //     return relatorios;
    // }
    
    // }

        
}
