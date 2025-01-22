package br.com.forumhub.demo.dto.resposta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RespostaRequestDTO(
        @NotBlank(message = "A mensagem é obrigatória")
        String mensagem,
        @NotNull(message = "O ID do tópico é obrigatório")
        Long topicoId,
        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId
) {}

