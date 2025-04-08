package br.com.pgm.ctec.uhscope.providers;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;


@Service
public class JWTProvider {
       
    @Value("${security.token.secret}")
    String secret;

    public String validateToken(String token)
    {
        token = token.replace("Bearer ", "");
        Algorithm algorithm = Algorithm.HMAC256(secret);

        try {
            var subject = JWT.require(algorithm).build().verify(token).getSubject();
            return subject;
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return null;
        }
       
    }
}
