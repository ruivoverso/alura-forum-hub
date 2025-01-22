package br.com.forumhub.demo.controller;

import br.com.forumhub.demo.dto.topico.TopicoRegisterDTO;
import br.com.forumhub.demo.dto.topico.TopicoResponseDTO;
import br.com.forumhub.demo.dto.topico.TopicoUpdateDTO;
import br.com.forumhub.demo.exceptions.CustomException;
import br.com.forumhub.demo.exceptions.NotFoundException;
import br.com.forumhub.demo.exceptions.UnauthorizedException;
import br.com.forumhub.demo.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public ResponseEntity<?> cadastrarTopico(@RequestBody @Valid TopicoRegisterDTO dto) {
        try {
            var dtoTopico = topicoService.criarTopico(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoTopico);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado ao criar tópico.");
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTopicos(
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable pageable) {
        try {
            var topicoPag = topicoService.listarTopicos(pageable);
            var topicoDtoPag = topicoPag.map(TopicoResponseDTO::new);
            return ResponseEntity.ok(topicoDtoPag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar tópicos.");
        }
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<?> buscarTopicosPorData(
            @RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable pageable) {
        try {
            var topicoPag = topicoService.buscarTopicosPorData(data, pageable);
            var topicoDtoPag = topicoPag.map(TopicoResponseDTO::new);
            return ResponseEntity.ok(topicoDtoPag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> buscarTopicoPorId(@PathVariable Long id) {
        try {
            var topico = new TopicoResponseDTO(topicoService.buscarTopicoPorId(id));
            return ResponseEntity.ok(topico);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @PutMapping("/{topicoId}/{usuarioId}")
    public ResponseEntity<?> atualizarTopico(@PathVariable Long topicoId,
                                             @PathVariable Long usuarioId,
                                             @RequestBody @Valid TopicoUpdateDTO dto) {
        try {
            var topicoAtualizado = new TopicoResponseDTO(topicoService.atualizarTopico(topicoId, usuarioId, dto));
            return ResponseEntity.ok(topicoAtualizado);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @DeleteMapping("/{topicoId}/{usuarioId}")
    public ResponseEntity<?> deletarTopico(@PathVariable Long topicoId, @PathVariable Long usuarioId) {
        try {
            topicoService.deletarTopico(topicoId, usuarioId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
}
