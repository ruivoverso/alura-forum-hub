package br.com.forumhub.demo.dto.topico;

import br.com.forumhub.demo.dto.resposta.RespostaResponseDTO;
import br.com.forumhub.demo.model.entities.Topico;
import br.com.forumhub.demo.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record TopicoResponseDTO(
        Long id,
        String titulo,
        String mensagem,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataCriacao,
        Status status,
        Long autorId,
        List<RespostaResponseDTO> respostas
) {
    public TopicoResponseDTO(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao().toLocalDate(),
                topico.getStatus(),
                topico.getAutor().getId(),
                topico.getRespostas().stream().map(RespostaResponseDTO::new).collect(Collectors.toList())
        );
    }
}
