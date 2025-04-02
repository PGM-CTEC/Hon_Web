package br.com.pgm.ctec.uhscope.modules.relatorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    // @Autowired
    // RelatorioService relatorioService;

    // @GetMapping('/{mes}/{ano}')
    // public ResponseEntity<String> getRelatorio(@RequestBody double valor, @PathVariable int mes, @PathVariable int ano)
    // {
    //     try {
    //         this.relatorioService
    //     } catch (Exception e) {
    //         // TODO: handle exception
    //     }
    // }

}
