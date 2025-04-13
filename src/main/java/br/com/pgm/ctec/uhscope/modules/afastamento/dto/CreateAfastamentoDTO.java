package br.com.pgm.ctec.uhscope.modules.afastamento.dto;
import br.com.pgm.ctec.uhscope.modules.afastamento.enums.AfastamentoTipo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAfastamentoDTO {
    
    @NotNull(message = "A data de início do afastamento é obrigatória")
    private String dataInicio;
    
    @NotNull(message = "A data de fim do afastamento é obrigatória")
    private String dataFim;
    
    private AfastamentoTipo tipo;
}
