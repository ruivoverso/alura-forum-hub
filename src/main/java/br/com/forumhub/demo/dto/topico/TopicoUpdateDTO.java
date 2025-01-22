package br.com.forumhub.demo.dto.topico;

import jakarta.validation.constraints.NotBlank;

public record TopicoUpdateDTO(
        @NotBlank(message = "O título é obrigatório")
        String titulo,
        @NotBlank(message = "A mensagem é obrigatória")
        String mensagem
) {
}
