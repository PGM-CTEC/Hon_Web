package br.com.pgm.ctec.uhscope.modules.afastamento.entities;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "afastamento")
@Data
public class AfastamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Corrigido para UUID
    @Column(name = "id_afastamento", nullable = false, updatable = false)
    private UUID idAfastamento;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_volta", nullable = false)
    private LocalDate dataFim;

    @Column(name = "uh_afastamento", nullable = false)
    private double uhAfastamento;

    @ManyToOne
    @JsonIgnore 
    @JoinColumn(name = "procurador_matricula", nullable = false) 
    private ProcuradorEntity procurador;
}

