package br.com.pgm.ctec.uhscope.modules.user;
import java.time.Instant;
import java.time.Duration;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import br.com.pgm.ctec.uhscope.modules.auth.UserRepository;
import br.com.pgm.ctec.uhscope.modules.user.entities.UserEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${security.token.secret}")
    String secret;
    @Transactional
    public UserEntity create(String email, String password, String username, String cpf) throws ValidationException {

        if (this.userRepository.findByUsername(username).isPresent()
        || this.userRepository.findByEmail(email).isPresent()
        || this.userRepository.findByCpf(cpf).isPresent()) {
            throw new ValidationException("Usuário já existente!");
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setUsername(username);
        user.setCpf(cpf);
        user.setPassword(this.passwordEncoder.encode(password));

        this.userRepository.save(user);
        return user;
    }


    @Transactional
    public String login(String username, String password) throws ValidationException{

        Optional<UserEntity> optionalUser = this.userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ValidationException("Usuário não existente!");
        }
    
        UserEntity user = optionalUser.get();
    
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidationException("Senha incorreta!");
        }
 
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create().withIssuer("pgm")
        .withExpiresAt(Instant.now().plus(Duration.ofMinutes(5)))
        .withSubject(user.getCpf())
        .sign(algorithm);
        return token;
    }

}