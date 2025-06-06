package br.com.pgm.ctec.uhscope.modules.user.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="user")
@Data
public class UserEntity {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer user_id;

    @Column(name="username", unique = true, nullable = false)
    public String username;

    @Email
    @Column(name="email", unique = true, nullable = false)
    public String email;

    @Size(min=8)
    @Column(name="password", nullable = false)
    public String password;

    @Column(name="cpf", unique = true, nullable = false, length = 11)
    private String cpf;  // Ex: 123.456.789-00
}
