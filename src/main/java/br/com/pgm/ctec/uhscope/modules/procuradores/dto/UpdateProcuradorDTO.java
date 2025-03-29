package br.com.pgm.ctec.uhscope.modules.procuradores.dto;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProcuradorDTO {

    @Size(min=8,max=100)
    private String nome;
    
    private String data_entrada; 
}


