package br.com.forumhub.demo.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioLoginDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String senha) {
}
