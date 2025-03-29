package br.com.pgm.ctec.uhscope.modules.procuradores.entities;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity(name="procurador")
@Table
@Data
public class ProcuradorEntity {
    
    @Id
    @Column(name="matricula", nullable =false)
    
    private String matricula;

    @Column(name="cpf", nullable = false)
    private String cpf;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="data_entrada", nullable = false)
    private LocalDate data_entrada; 
}
