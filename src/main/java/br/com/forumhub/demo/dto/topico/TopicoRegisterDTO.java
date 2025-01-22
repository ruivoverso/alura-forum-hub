package br.com.forumhub.demo.dto.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record TopicoRegisterDTO(
        @NotBlank(message = "O título é obrigatório")
        String titulo,
        @NotBlank(message = "A mensagem é obrigatória")
        String mensagem,
        @NotNull(message = "O ID do Autor do topico é obrigatório")
        Long autorId
) {
}
