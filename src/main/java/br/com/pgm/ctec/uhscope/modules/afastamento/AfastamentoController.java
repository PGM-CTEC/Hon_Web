package br.com.pgm.ctec.uhscope.modules.afastamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pgm.ctec.uhscope.modules.afastamento.dto.CreateAfastamentoDTO;
import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
public class AfastamentoController {
    @Autowired
    AfastamentoService afastamentoService;
    

    @PostMapping("/afastamento/{matricula}")
    public ResponseEntity<?> create(@Valid @RequestBody CreateAfastamentoDTO createAfastamentoDTO, @Valid @PathVariable String matricula) {
        try {
            
            AfastamentoEntity result = this.afastamentoService.create(createAfastamentoDTO, matricula);
            return ResponseEntity.ok(result);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } 
    }
}
