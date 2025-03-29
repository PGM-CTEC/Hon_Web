package br.com.pgm.ctec.uhscope.modules.procuradores.entities;
import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity(name="procurador")
@Table
@Data
public class ProcuradorEntity {
    
    @Id
    @Column(name="matricula", nullable =false)
    @Size(min=7, max=7)
    private String matricula;

    @Column(name="cpf", nullable = false)
    @CPF
    private String cpf;

    @Column(name="nome", nullable = false)
    @Length(min=8,max=100)
    private String nome;

    @Column(name="data_entrada", nullable = false)

    private LocalDate data_entrada; 
}
