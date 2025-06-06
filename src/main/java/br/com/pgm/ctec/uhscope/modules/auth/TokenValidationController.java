package br.com.pgm.ctec.uhscope.modules.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pgm.ctec.uhscope.providers.JWTProvider;

@RestController
@RequestMapping("/auth")
public class TokenValidationController {

    @Autowired
    private JWTProvider jwtProvider;

    @GetMapping("/validate")
     public boolean validateToken(@RequestParam("token") String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String subject = jwtProvider.validateToken("Bearer " + token);
        return subject != null;
    }

}
