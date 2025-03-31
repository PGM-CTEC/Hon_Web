package br.com.pgm.ctec.uhscope.modules.utils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import ch.qos.logback.classic.Logger;
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
            {"(\\d{1})-(\\d{2})-(\\d{4})", "d-M-yyyy"},     // Exemplo: 2-2-2024
            {"(\\d{2})/(\\d{2})/(\\d{4})", "dd/MM/yyyy"},   // Exemplo: 20/02/2024
            {"(\\d{4})-(\\d{2})-(\\d{2})", "yyyy-MM-dd"},   // Exemplo: 2024-02-20
            {"(\\d{4})/(\\d{2})/(\\d{2})", "yyyy/MM/dd"},   // Exemplo: 2024/02/20
            {"(\\d{1})/(\\d{1})/(\\d{4})", "M/d/yyyy"},     // Exemplo: 1/2/1999
            {"(\\d{2})/(\\d{1})/(\\d{4})", "MM/d/yyyy"}     // Exemplo: 10/2/1999
        };
        

        // Ajuste: remova a correspondência de padrões conflitantes
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
            return null; // Caso ocorra algum erro na conversão
        }
    }

    // public double calculaUH(ArrayList<AfastamentoEntity> afastamentos, ProcuradorEntity procurador){
    //     double uh=0;
    //     for (int i=0; i<afastamentos.size(); i++) {
            
    //         if(i==0){
    //             AfastamentoEntity afastamento = afastamentos.get(i);
    //             LocalDate dataEntrada = procurador.getData_entrada();
    //             LocalDate afastamentoDataInicio = afastamento.getDataInicio();

    //             int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, afastamentoDataInicio);
            
    //             if(diffTempo>10){
    //                 uh=1.0; 
    //             }
    //             else if(diffTempo<=10)
    //             {
    //                 uh+=diffTempo/10;
    //             }
        
    //             LocalDate dataInicioAfastamento = afastamento.getDataInicio();
    //             LocalDate dataFimAfastamento= afastamento.getDataFim();
        
    //             diffTempo = (int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);

    //             if(diffTempo>10){
    //                 uh=0;
    //             }
    //             else if(diffTempo<=10 && diffTempo>0){
    //                 uh -= (diffTempo/10);
    //             }
    //         }
    //         else{
    //             AfastamentoEntity afastamentoAnterior = afastamentos.get(i-1);
    //             AfastamentoEntity afastamento = afastamentos.get(i);

    //             LocalDate dataFimAfastamentoAnterior = afastamentoAnterior.getDataFim();
    //             LocalDate dataInicioAfastamento2 = afastamento.getDataInicio();
               
                
    //             int diffTempo = (int) ChronoUnit.YEARS.between(dataInicioAfastamento2, dataFimAfastamentoAnterior);
             
    //             if(diffTempo>10){
    //                 uh=1.0;
    //             }
    //             else if(diffTempo<=10 && diffTempo>0)
    //             {
    //                 double uhTemp = uh+diffTempo/10;
    //                 if(uhTemp>1){
    //                     uh=1;
    //                 }
    //                 else{
    //                     uh+=diffTempo/10;
    //                 }
    //             }
        
    //             LocalDate dataFimAfastamento = afastamento.getDataFim();
    //             LocalDate dataInicioAfastamento = afastamento.getDataInicio();
          

    //             diffTempo = (int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);
                
    //             if(diffTempo>10){
    //                 uh=0;
    //             }
    //             else if(diffTempo<=10&&diffTempo>0){
        
    //                 double uhTemp = uh-diffTempo/10;
    //                 if(uhTemp<0){
    //                     uh=0;
    //                 }
    //                 uh-=diffTempo/10;
    //             }
    //         }
    //         }

            
    //     // Obtendo o último afastamento
    //     AfastamentoEntity ultimoAfastamento = afastamentos.get(afastamentos.size() - 1);

    //     // Convertendo a data de fim do último afastamento e a data de hoje
    //     LocalDate lastDate = ultimoAfastamento.getDataFim();
    //     LocalDate today = LocalDate.now();

    //     // Calculando a diferença de anos entre o último afastamento e hoje
    //     int diffTempo = (int) ChronoUnit.YEARS.between(lastDate, today);

    //     // Calculando o valor de uhPlus
    //     double uhPlus = diffTempo / 10.0;

    //     // Calculando o valor final de uh
    //     if (uhPlus >= 1) {
    //         uh = 1;
    //     } else if (uhPlus < 1) {
    //         double uhTemp = uh + uhPlus;
    //         if (uhTemp > 1) {
    //             uh = 1;
    //         } else {
    //             uh += uhPlus;
    //     }

    // return uh;
    // }
    
    // }

    public double calculaUH(List<AfastamentoEntity> afastamentos, ProcuradorEntity procurador) {

        Collections.sort(afastamentos, Comparator.comparing(AfastamentoEntity::getDataInicio));
        double uh = 0;
        
        // OK!
        if (afastamentos.isEmpty()) {
            LocalDate dataEntrada = procurador.getData_entrada();
            LocalDate today = LocalDate.now();

            int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, today);
    
            uh = diffTempo / 10.0;
    
            if (uh > 1) {
                uh = 1.0;
            }
            return uh;  
        }
        
        // Iterando sobre os afastamentos
        for (int i = 0; i < afastamentos.size(); i++) {

            if (i == 0) {
                // Primeira iteração (primeiro afastamento)
                AfastamentoEntity afastamento = afastamentos.get(i);
                LocalDate dataEntrada = procurador.getData_entrada();
                LocalDate afastamentoDataInicio = afastamento.getDataInicio();

                int diffTempo = (int) ChronoUnit.YEARS.between(dataEntrada, afastamentoDataInicio);

                // Lógica para o primeiro afastamento
                if (diffTempo >= 10) {
                    uh = 1.0;
                } else if (diffTempo < 10) {
                    uh += diffTempo / 10.0;
                }

                // Calculando a diferença de tempo entre a data de início e fim do afastamento
                LocalDate dataInicioAfastamento = afastamento.getDataInicio();
                LocalDate dataFimAfastamento = afastamento.getDataFim();
                diffTempo = (int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);

                // Ajustando `uh` com base no tempo de afastamento
                if (diffTempo >= 10) {
                    uh = 0;
                } else if (diffTempo > 0 && diffTempo < 10) {
                    uh -= diffTempo / 10.0;
                }
            } else {
                // Para os demais afastamentos
                AfastamentoEntity afastamentoAnterior = afastamentos.get(i - 1);
                AfastamentoEntity afastamento = afastamentos.get(i);

                LocalDate dataFimAfastamentoAnterior = afastamentoAnterior.getDataFim();
                LocalDate dataInicioAfastamento2 = afastamento.getDataInicio();

                int diffTempo = (int) ChronoUnit.YEARS.between(dataInicioAfastamento2, dataFimAfastamentoAnterior);

                // Ajustando `uh` com base na diferença de tempo entre o final do afastamento anterior e o início do atual
                if (diffTempo >= 10) {
                    uh = 1.0;
                } else if (diffTempo > 0 && diffTempo < 10) {
                    double uhTemp = uh + diffTempo / 10.0;
                    if (uhTemp > 1) {
                        uh = 1;
                    } else {
                        uh += diffTempo / 10.0;
                    }
                }

                // Calculando a diferença de tempo entre o início e o fim do afastamento atual
                LocalDate dataFimAfastamento = afastamento.getDataFim();
                LocalDate dataInicioAfastamento = afastamento.getDataInicio();
                diffTempo = (int) ChronoUnit.YEARS.between(dataInicioAfastamento, dataFimAfastamento);

                // Ajustando `uh` com base no tempo de afastamento
                if (diffTempo >= 10) {
                    uh = 0;
                } else if (diffTempo > 0 && diffTempo < 10) {
                    double uhTemp = uh - diffTempo / 10.0;
                    if (uhTemp < 0) {
                        uh = 0;
                    } else {
                        uh -= diffTempo / 10.0;
                    }
                }
            }
        }

        // Obtendo o último afastamento
        AfastamentoEntity ultimoAfastamento = afastamentos.get(afastamentos.size() - 1);

        // Convertendo a data de fim do último afastamento e a data de hoje
        LocalDate lastDate = ultimoAfastamento.getDataFim();
        LocalDate today = LocalDate.now();

        // Calculando a diferença de anos entre o último afastamento e hoje
        int diffTempo = (int) ChronoUnit.YEARS.between(lastDate, today);

        // Calculando o valor de uhPlus
        double uhPlus = diffTempo / 10.0;

        // Ajustando o valor final de uh
        if (uhPlus >= 1) {
            uh = 1;
        } else if (uhPlus < 1) {
            double uhTemp = uh + uhPlus;
            if (uhTemp > 1) {
                uh = 1;
            } else {
                uh += uhPlus;
            }
        }

        return uh;
    }
}
