package br.com.pgm.ctec.uhscope.modules.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;

@Service
public class MethodsUtils {

    public LocalDate convertDate(String dateString) throws ValidationException {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new ValidationException("Data de entrada não pode ser nula ou vazia.");
        }

        dateString = dateString.trim();
        System.out.println(dateString);

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
}
