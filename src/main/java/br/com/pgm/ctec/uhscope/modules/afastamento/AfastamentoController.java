package br.com.pgm.ctec.uhscope.modules.afastamento;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import br.com.pgm.ctec.uhscope.exceptions.AlreadyExistsException;
import br.com.pgm.ctec.uhscope.exceptions.ProcuradorNotFoundException;
import br.com.pgm.ctec.uhscope.modules.afastamento.dto.CreateAfastamentoDTO;
import br.com.pgm.ctec.uhscope.modules.afastamento.entities.AfastamentoEntity;
import br.com.pgm.ctec.uhscope.modules.utils.Relatorio;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
public class AfastamentoController {
    @Autowired
    AfastamentoService afastamentoService;
    
    @GetMapping("/relatorio")
    public ResponseEntity<?> getRelatorio(){
        try {
            ArrayList<Relatorio> relatorios = this.afastamentoService.getAllRelatorios();
            return ResponseEntity.ok(relatorios);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/afastamento/{matricula}")
    public ResponseEntity<?> create(@Valid @RequestBody CreateAfastamentoDTO createAfastamentoDTO, @Valid @PathVariable String matricula) {
        try {
            AfastamentoEntity result = this.afastamentoService.create(createAfastamentoDTO, matricula);
            return ResponseEntity.ok(result);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ProcuradorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }    
    }

    @GetMapping("/afastamento/{matricula}")
    public ResponseEntity<?> getAll(@PathVariable String matricula){
            ArrayList<AfastamentoEntity> afastamentos = this.afastamentoService.getAll(matricula);
            return ResponseEntity.status(HttpStatus.OK).body(afastamentos);
    }
    
}
