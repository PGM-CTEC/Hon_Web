package br.com.pgm.ctec.uhscope.modules.auth.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String password;
    private String username;
    private String cpf;
}

