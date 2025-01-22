package br.com.forumhub.demo.dto.resposta;

import jakarta.validation.constraints.NotBlank;

public record RespostaUpdateDTO(
        @NotBlank
        String mensagem
        ) {
}
