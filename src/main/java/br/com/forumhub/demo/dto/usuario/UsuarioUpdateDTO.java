package br.com.forumhub.demo.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDTO(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email
) {
}
