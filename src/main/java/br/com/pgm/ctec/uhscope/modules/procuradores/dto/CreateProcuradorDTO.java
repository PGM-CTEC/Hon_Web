package br.com.pgm.ctec.uhscope.modules.procuradores.dto;
import java.time.LocalDate;
import org.hibernate.validator.constraints.br.CPF;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProcuradorDTO {
    
    @Size(min=7, max=7)
    private String matricula;

    @CPF
    private String cpf;

    @Size(min=8,max=100)
    private String nome;

    private LocalDate data_entrada; 
}
