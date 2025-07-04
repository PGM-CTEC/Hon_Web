package br.com.pgm.ctec.uhscope.modules.relatorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/{mes}/{ano}")
    public ResponseEntity<String> getRelatorio(@RequestParam double valor, 
                                               @PathVariable int mes, 
                                               @PathVariable int ano) {
        try {
            String relatorios = this.relatorioService.get(valor, mes, ano);
            return ResponseEntity.status(HttpStatus.OK).body(relatorios);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de validação: " + e.getMessage());
        }
    }
}
