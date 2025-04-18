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
        LocalDate dataEntrada = procurador.getData_entrada(); //Data entrada do procurador
    
        Collections.sort(afastamentos, Comparator.comparing(AfastamentoEntity::getDataInicio));
        double uh = 0;
    
        // Se a lista de afastamentos estiver vazia, trate isso
        if (afastamentos.isEmpty()) {
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
            AfastamentoEntity afastamento = afastamentos.get(i); //Afastamento
            LocalDate dataInicioAfastamento = afastamento.getDataInicio(); //Data inicio do afastamento
            LocalDate dataFimAfastamento = afastamento.getDataFim(); //Data fim do afastamento
            String afastamentoTipo = (String)afastamento.getTipo().name();

            if(afastamentoTipo.equals("INATIVO")) {
                
                if(i==0)
                {
                    if(afastamento.getDataInicio().isAfter(dataFimMes))
                    {
                        int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataFimMes);
                        uh = Math.min(diffTempo / 10.0, 1.0);
                        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue(); 
                    }
                    else if(afastamento.getDataInicio().isBefore(dataFimMes)&&afastamento.getDataFim().isAfter(dataFimMes))
                    {
                        int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataInicioAfastamento)-(int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimMes);
                        uh = Math.min(diffTempo / 10.0, 1.0);
                        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    }
                    else {
                        int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataInicioAfastamento)-(int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);
                        ultimoAfastamento = afastamentos.get(i);
                        uh = Math.min(diffTempo / 10.0, 1.0);
                        continue;
                    }
                }
                else {
                    System.out.println("resultado "+ afastamentos.get(i).getTipo().equals("INATIVO"));
                    AfastamentoEntity afastamentoAnterior = afastamentos.get(i-1);
                    LocalDate fimAfastamentoAnterior = afastamentoAnterior.getDataFim();

                    if(afastamento.getDataInicio().isAfter(dataFimMes))
                    {
                        int diffTempo = (int) ChronoUnit.YEARS.between(fimAfastamentoAnterior, dataFimMes);
                        uh = Math.min(uh + diffTempo / 10.0, 1.0);
                        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    }
                    else if(afastamento.getDataInicio().isBefore(dataFimMes)&&afastamento.getDataFim().isAfter(dataFimMes))
                    {
                        int diffTempo = (int) ChronoUnit.YEARS.between(fimAfastamentoAnterior, dataInicioAfastamento)-(int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimMes);
                        uh = Math.min(uh + diffTempo / 10.0, 1.0);
                        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    }
                    else {
                        int diffTempo = (int) ChronoUnit.YEARS.between(fimAfastamentoAnterior, dataInicioAfastamento)-(int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);
                        ultimoAfastamento = afastamentos.get(i);
                        uh = Math.min(uh + diffTempo / 10.0, 1.0);
                        continue;
                    }
                }
            }

            else if(afastamentoTipo.equals("ATIVO"))
            {
                if(i==0)
                {
                    if(afastamento.getDataInicio().isAfter(dataFimMes))
                    {
                        int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataFimMes);
                        uh = Math.min(uh+diffTempo / 10.0, 1.0);
                        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue(); 
                    }
                    else if(afastamento.getDataInicio().isBefore(dataFimMes)&&afastamento.getDataFim().isAfter(dataFimMes))
                    {
                        AfastamentoEntity afastamentoNovo = new AfastamentoEntity();
                        afastamentoNovo.setDataInicio(afastamento.getDataInicio());
                        afastamentoNovo.setDataFim(dataFimMes);
                        
                        if(this.passouUmAno(afastamentoNovo, dataEntrada)){
                            uh+=0.1;
                        }

                        int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataInicioAfastamento)-(int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimMes);
                        uh = Math.min(uh+diffTempo / 10.0, 1.0);
                        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    }
                    else {
                        
                        if(this.passouUmAno(afastamento, dataEntrada)){
                            uh+=0.1;
                        }
                        int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, dataInicioAfastamento)-(int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);
                        ultimoAfastamento = afastamentos.get(i);
                        uh = Math.min(uh+diffTempo / 10.0, 1.0);
                        continue;
                    }
                }
                else {
                    AfastamentoEntity afastamentoAnterior = afastamentos.get(i-1);
                    LocalDate fimAfastamentoAnterior = afastamentoAnterior.getDataFim();

                    if(afastamento.getDataInicio().isAfter(dataFimMes))
                    {
                        int diffTempo = (int) ChronoUnit.YEARS.between(fimAfastamentoAnterior, dataFimMes);
                        uh = Math.min(uh + diffTempo / 10.0, 1.0);
                        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    }
                    else if(afastamento.getDataInicio().isBefore(dataFimMes)&&afastamento.getDataFim().isAfter(dataFimMes))
                    {
                        AfastamentoEntity afastamentoNovo = new AfastamentoEntity();
                        afastamentoNovo.setDataInicio(afastamento.getDataInicio());
                        afastamentoNovo.setDataFim(dataFimMes);
                        if(this.passouUmAno(afastamentoNovo, dataEntrada)){
                            uh+=0.1;
                        }
                        int diffTempo = (int) ChronoUnit.YEARS.between(fimAfastamentoAnterior, dataInicioAfastamento)-(int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimMes);
                        uh = Math.min(uh + diffTempo / 10.0, 1.0);
                        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    }
                    else {
                        if(this.passouUmAno(afastamento, dataEntrada)){
                            uh+=0.1;
                        }
                        int diffTempo = (int) ChronoUnit.YEARS.between(fimAfastamentoAnterior, dataInicioAfastamento)-(int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);
                        ultimoAfastamento = afastamentos.get(i);
                        uh = Math.min(uh + diffTempo / 10.0, 1.0);
                        continue;
                    }
                }
            }
    }

        LocalDate lastDate = ultimoAfastamento.getDataFim();
        int diffFinal = (int) ChronoUnit.YEARS.between(lastDate, dataFimMes);
        uh = Math.min(uh + diffFinal / 10.0, 1.0);
        return new BigDecimal(uh).setScale(1, RoundingMode.HALF_UP).doubleValue();

    }

    public boolean passouUmAno(AfastamentoEntity afastamento, LocalDate dataEntrada)
    {
        int diaEntrada = dataEntrada.getDayOfMonth();
        int mesEntrada = dataEntrada.getMonthValue();
    
        LocalDate inicioAfastamento = afastamento.getDataInicio();
        LocalDate fimAfastamento = afastamento.getDataFim();
    
        int diaAfastamento = inicioAfastamento.getDayOfMonth();
        int mesAfastamento = inicioAfastamento.getMonthValue();
        int anoAfastamento = inicioAfastamento.getYear();
    
        LocalDate dataAlvo;
    
        // Se o afastamento começa exatamente no dia do aniversário
        if (diaEntrada == diaAfastamento && mesEntrada == mesAfastamento) {
            dataAlvo = LocalDate.of(anoAfastamento + 1, mesEntrada, diaEntrada);
        } 
        
        else if(LocalDate.of(anoAfastamento, mesEntrada, diaEntrada).isBefore(afastamento.getDataInicio()))
        {
            dataAlvo = LocalDate.of(anoAfastamento+1, mesEntrada, diaEntrada);
        }
        
        else {
            dataAlvo = LocalDate.of(anoAfastamento, mesEntrada, diaEntrada);
        }
    
        // Verifica se aniversariou durante o afastamento
        return (dataAlvo.isAfter(inicioAfastamento)) && (dataAlvo.isEqual(fimAfastamento) || dataAlvo.isBefore(fimAfastamento));
    }
}
