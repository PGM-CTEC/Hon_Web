package br.com.pgm.ctec.uhscope.modules.procuradores.entities;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "procurador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<AfastamentoEntity> afastamentos = new ArrayList<>();

}
