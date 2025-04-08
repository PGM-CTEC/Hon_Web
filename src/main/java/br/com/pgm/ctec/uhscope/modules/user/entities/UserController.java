package br.com.pgm.ctec.uhscope.modules.user.entities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import br.com.pgm.ctec.uhscope.modules.auth.dto.LoginDTO;
import br.com.pgm.ctec.uhscope.modules.auth.dto.RegisterDTO;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register (@RequestBody RegisterDTO registerDTO)
    {
        try {
            UserEntity user = this.userService.create(registerDTO.getEmail(), registerDTO.getPassword(), registerDTO.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO)
    {
        try{
            String token = this.userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    
}
