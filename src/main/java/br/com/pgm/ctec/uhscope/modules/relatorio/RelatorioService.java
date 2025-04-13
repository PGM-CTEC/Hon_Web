package br.com.pgm.ctec.uhscope.modules.relatorio;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import br.com.pgm.ctec.uhscope.modules.procuradores.ProcuradorRepository;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import br.com.pgm.ctec.uhscope.modules.utils.MethodsUtils;
import jakarta.validation.ValidationException;

@Service
public class RelatorioService {

    @Autowired
    ProcuradorRepository procuradorRepository;

    @Autowired
    MethodsUtils methodsUtils;
    
    
    public String get(double valor, int mes, int ano) throws ValidationException {
        int anoAtual = LocalDate.now().getYear();
        
        if (mes < 1 || mes > 12) {
            throw new ValidationException("Esse mês não existe! Insira um mês que exista (1-12)");
        }
        if (valor <= 0) {
            throw new ValidationException("O valor não pode ser menor ou igual a zero.");
        }
        if (ano < 1900 || ano > anoAtual) {
            throw new ValidationException("Ano inválido! Insira um ano entre 1900 e " + anoAtual + ".");
        }
    
        String dataInicio = String.format("%02d/%02d/%d", 1, mes, ano);
        String dataFim = String.format("%02d/%02d/%d", LocalDate.of(ano, mes, 1).lengthOfMonth(), mes, ano);
    
        ArrayList<ProcuradorEntity> procuradores = (ArrayList<ProcuradorEntity>) this.procuradorRepository.findAll();
        StringBuilder resultado = new StringBuilder();
    
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("00000.00", symbols);
    
        double somaUh = 0; // Variável para armazenar a soma de todas as UHs
        List<Double> uhs = new ArrayList<>();
    
        // Calcular as UHs de todos os procuradores e somá-las
        for (ProcuradorEntity procurador : procuradores) {
            List<AfastamentoEntity> afastamentos = new ArrayList<>(procurador.getAfastamentos());
    
            double uh = this.methodsUtils.calculaUHMensal(afastamentos, procurador, mes, ano);
            if (uh<0) {
                uh=0;
            }
            uhs.add(uh); // Adicionar UH de cada procurador à lista
            somaUh += uh; // Somar a UH ao total

            System.out.println("\n");
            System.out.println("Procurador: "+procurador.getNome()+" | "+"UH: "+uh);
            System.out.println("\n");
        }
    
        // Verificar se a soma total das UHs é válida (evitar divisão por zero)
        if (somaUh == 0) {
            throw new ValidationException("A soma das UHs é zero. Não é possível calcular os valores.");
        }
    
        // Calcular o valor final para cada procurador com base na sua UH e na soma total das UHs
        for (int i = 0; i < procuradores.size(); i++) {
            ProcuradorEntity procurador = procuradores.get(i);
            double uh = uhs.get(i); // UH do procurador atual
    
            // Calcular o valor final do procurador
            double valorFinal = (uh / somaUh) * valor;
    
            // Formatar o resultado e adicionar ao StringBuilder
            resultado.append(String.format("%s;HON SUCUMBENCIA;%s;%s;%s\n",
                    procurador.getMatricula(),
                    dataInicio,
                    dataFim,
                    df.format(valorFinal)
            ));
        }
    
        // Retornar o resultado final
        return resultado.toString();
    }
    
    
}

