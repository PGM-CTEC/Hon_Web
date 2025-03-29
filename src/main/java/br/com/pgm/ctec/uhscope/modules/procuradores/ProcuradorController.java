package br.com.pgm.ctec.uhscope.modules.procuradores;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.pgm.ctec.uhscope.modules.procuradores.dto.CreateProcuradorDTO;
import br.com.pgm.ctec.uhscope.modules.procuradores.dto.UpdateProcuradorDTO;
import br.com.pgm.ctec.uhscope.modules.procuradores.entities.ProcuradorEntity;
import jakarta.validation.Valid;

@RequestMapping("/procurador")
@RestController
public class ProcuradorController {

    @Autowired
    ProcuradorService procuradorService;

    @GetMapping("/hello")
    public String ok(){
        return "Ok!";
    }

    // Retorna todos os procuradores (fazer calculo de unidade honorária nesse controller)
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
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/{matricula}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateProcuradorDTO updateProcuradorDTO, @PathVariable String matricula) {
        try {
            ProcuradorEntity savedProcurador = this.procuradorService.update(updateProcuradorDTO, matricula);
            return ResponseEntity.status(HttpStatus.OK).body(savedProcurador);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    

}
