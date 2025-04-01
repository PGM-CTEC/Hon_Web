package br.com.pgm.ctec.uhscope.modules.procuradores;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.pgm.ctec.uhscope.exceptions.ProcuradorNotFoundException;
import br.com.pgm.ctec.uhscope.modules.procuradores.dto.CreateProcuradorDTO;
import br.com.pgm.ctec.uhscope.modules.procuradores.dto.UpdateProcuradorDTO;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RequestMapping("/procurador")
@RestController
public class ProcuradorController {

    @Autowired
    ProcuradorService procuradorService;

    @GetMapping()
    public ResponseEntity<ArrayList<ProcuradorEntity>> getALL(){
        ArrayList<ProcuradorEntity> lista = this.procuradorService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody CreateProcuradorDTO createProcuradorDTO) {
        try {
            ProcuradorEntity savedProcurador = this.procuradorService.create(createProcuradorDTO);
            return ResponseEntity.status(HttpStatus.OK).body(savedProcurador);
        } catch (ValidationException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/{matricula}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateProcuradorDTO updateProcuradorDTO, @PathVariable String matricula) {
        try {
            ProcuradorEntity savedProcurador = this.procuradorService.update(updateProcuradorDTO, matricula);
            return ResponseEntity.status(HttpStatus.OK).body(savedProcurador);
        } catch (ValidationException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (ProcuradorNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<?> delete(@PathVariable String matricula)
    {
        try {
            ProcuradorEntity deletedProcurador = this.procuradorService.delete(matricula);
            return ResponseEntity.status(HttpStatus.OK).body(deletedProcurador);
        }
        catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(ProcuradorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } 
        
        
    }
}

