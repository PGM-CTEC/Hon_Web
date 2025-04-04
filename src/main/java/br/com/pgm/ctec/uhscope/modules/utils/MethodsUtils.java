package br.com.pgm.ctec.uhscope.modules.utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import jakarta.validation.ValidationException;

@Service
public class MethodsUtils {

    public LocalDate convertDate(String dateString) throws ValidationException {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new ValidationException("Data de entrada não pode ser nula ou vazia.");
        }

        dateString = dateString.trim();

        String[][] patterns = {
            {"(\\d{2})/(\\d{2})/(\\d{4})", "MM/dd/yyyy"},   // Exemplo: 12/30/2013
            {"(\\d{1})/(\\d{2})/(\\d{4})", "M/dd/yyyy"},    // Exemplo: 1/22/1999
            {"(\\d{2})-(\\d{2})-(\\d{4})", "dd-MM-yyyy"},   // Exemplo: 20-02-2024
            {"(\\d{1})-(\\d{1})-(\\d{4})", "d-M-yyyy"},     // Exemplo: 2-2-2024
            {"(\\d{2})/(\\d{2})/(\\d{4})", "dd/MM/yyyy"},   // Exemplo: 20/02/2024
            {"(\\d{4})-(\\d{2})-(\\d{2})", "yyyy-MM-dd"},   // Exemplo: 2024-02-20
            {"(\\d{4})/(\\d{2})/(\\d{2})", "yyyy/MM/dd"},   // Exemplo: 2024/02/20
            {"(\\d{1})/(\\d{1})/(\\d{4})", "M/d/yyyy"},     // Exemplo: 1/2/1999
            {"(\\d{2})/(\\d{1})/(\\d{4})", "MM/d/yyyy"}     // Exemplo: 10/2/1999
        };
        
        for (String[] pattern : patterns) {
            if (dateString.matches(pattern[0])) {
                return convertToLocalDate(dateString, pattern[1]);
            }
        }
        throw new ValidationException("Formato de data inválido: " + dateString);
    }

    private LocalDate convertToLocalDate(String dateString, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erro de conversão");
            return null; 
        }
    }

    public double calculaUH(List<AfastamentoEntity> afastamentos, ProcuradorEntity procurador) {
        Collections.sort(afastamentos, Comparator.comparing(AfastamentoEntity::getDataInicio));
        double uh = 0;

        if (afastamentos.isEmpty()) {
            LocalDate dataEntrada = procurador.getData_entrada();
            LocalDate today = LocalDate.now();
            int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, today);
            uh = diffTempo / 10.0;
            uh = Math.min(uh, 1.0);
            return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();
        }

        for (int i=0; i < afastamentos.size(); i++) {
            AfastamentoEntity afastamento = afastamentos.get(i);
            LocalDate dataInicioAfastamento = afastamento.getDataInicio();
            LocalDate dataFimAfastamento = afastamento.getDataFim();

            if (i==0) {
                LocalDate dataEntrada = procurador.getData_entrada();
                int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataInicioAfastamento);
                uh = Math.min(diffTempo / 10.0, 1.0);
            } else {
                AfastamentoEntity afastamentoAnterior = afastamentos.get(i - 1);
                LocalDate dataFimAfastamentoAnterior = afastamentoAnterior.getDataFim();
                int diffTempo = (int) ChronoUnit.YEARS.between(dataFimAfastamentoAnterior, dataInicioAfastamento);
                uh = Math.min(uh + diffTempo / 10.0, 1.0);
            }

            int diffAfastamento = (int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);
            uh = Math.max(uh - diffAfastamento / 10.0, 0.0);
        }

        AfastamentoEntity ultimoAfastamento = afastamentos.get(afastamentos.size() - 1);
        LocalDate lastDate = ultimoAfastamento.getDataFim();
        LocalDate today = LocalDate.now();
        int diffFinal = (int) ChronoUnit.YEARS.between(lastDate, today);
        uh = Math.min(uh + diffFinal / 10.0, 1.0);

        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    public double calculaUHMensal(List<AfastamentoEntity> afastamentos, ProcuradorEntity procurador, int mes, int ano) {
        // Definir o intervalo fixo do mês escolhido
        LocalDate dataInicioMes = LocalDate.of(ano, mes, 1);
        LocalDate dataFimMes = YearMonth.of(ano, mes).atEndOfMonth();
    
        Collections.sort(afastamentos, Comparator.comparing(AfastamentoEntity::getDataInicio));
        double uh = 0;
    
        // Se a lista de afastamentos estiver vazia, trate isso
        if (afastamentos.isEmpty()) {
            LocalDate dataEntrada = procurador.getData_entrada();
            System.out.println("\n\n\n");
            System.out.println("Data entrada procurador: " + dataEntrada + " | Procurador: " + procurador.getNome());
            int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataFimMes);
            System.out.println("Tempo trabalhado por " + procurador.getNome() + " | Tempo: " + diffTempo);
            uh = Math.min(diffTempo / 10.0, 1.0);
            System.out.println("Unidade Honorária de " + procurador.getNome() + "| " + "Uh: " + uh);
            System.out.println("\n");
            return Math.max(0.0, new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue());
        }
    
        AfastamentoEntity ultimoAfastamento = afastamentos.get(afastamentos.size() - 1);
    
        for (int i = 0; i < afastamentos.size(); i++) {
            AfastamentoEntity afastamento = afastamentos.get(i);
            LocalDate dataInicioAfastamento = afastamento.getDataInicio();
            LocalDate dataFimAfastamento = afastamento.getDataFim();
    
            if (dataInicioAfastamento.isAfter(dataFimMes)) {
                break;
            }
    
            if (dataInicioAfastamento.isBefore(dataFimMes) && dataFimAfastamento.isAfter(dataFimMes)) {
                dataFimAfastamento = dataFimMes;
                ultimoAfastamento = afastamentos.get(i);
                break;
            }
    
            if (i == 0) {
                LocalDate dataEntrada = procurador.getData_entrada();
                int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataInicioAfastamento);
                uh = Math.min(diffTempo / 10.0, 1.0);
            } else {
                AfastamentoEntity afastamentoAnterior = afastamentos.get(i - 1);
                LocalDate dataFimAfastamentoAnterior = afastamentoAnterior.getDataFim();
                int diffTempo = (int) ChronoUnit.YEARS.between(dataFimAfastamentoAnterior, dataInicioAfastamento);
                uh = Math.min(uh + diffTempo / 10.0, 1.0);
            }
    
            // Calcular o tempo de afastamento que realmente ocorreu dentro do mês
            int diffAfastamento = (int) ChronoUnit.DAYS.between(dataInicioAfastamento, dataFimAfastamento); // Em dias
            uh = Math.max(uh - diffAfastamento / 365.0 / 10.0, 0.0);
        }
    
        LocalDate lastDateFim = ultimoAfastamento.getDataFim();
    
        // Se o último afastamento terminar depois do fim do mês, ajusta a data final para o mês
        if (lastDateFim.isAfter(dataFimMes)) {
            lastDateFim = dataFimMes;
        }
    
        int diffFinal = (int) ChronoUnit.YEARS.between(lastDateFim, dataFimMes);
        uh = Math.min(uh + diffFinal / 10.0, 1.0);
    
        // Garantir que UH nunca seja negativo
        return Math.max(0.0, new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue());
    }
    
    

}
