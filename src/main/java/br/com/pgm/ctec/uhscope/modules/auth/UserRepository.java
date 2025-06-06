package br.com.pgm.ctec.uhscope.modules.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pgm.ctec.uhscope.modules.user.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    public Optional<UserEntity> findByUsername (String username);
    public Optional<UserEntity> findByEmail (String email);
    public Optional<UserEntity> findByCpf(String cpf);
}
