package br.com.pgm.ctec.uhscope.modules.auth.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    public String username;
    public String password;
    public String email;
}
