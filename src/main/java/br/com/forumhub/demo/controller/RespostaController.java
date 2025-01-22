package br.com.forumhub.demo.controller;

import br.com.forumhub.demo.dto.resposta.RespostaRequestDTO;
import br.com.forumhub.demo.dto.resposta.RespostaResponseDTO;
import br.com.forumhub.demo.dto.resposta.RespostaUpdateDTO;
import br.com.forumhub.demo.exceptions.NotFoundException;
import br.com.forumhub.demo.exceptions.UnauthorizedException;
import br.com.forumhub.demo.service.RespostaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/respostas")
public class RespostaController {

    @Autowired
    private RespostaService respostaService;

    @PostMapping
    public ResponseEntity<?> criarResposta(@RequestBody @Valid RespostaRequestDTO dto) {
        try {
            var resposta = respostaService.criarResposta(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RespostaResponseDTO(resposta));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar resposta.");
        }
    }

    @GetMapping
    public ResponseEntity<?> listarRespostas(Pageable pageable) {
        try {
            var respostas = respostaService.listarRespostas(pageable);
            return ResponseEntity.ok(respostas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar respostas.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            var resposta = respostaService.buscarPorId(id);
            return ResponseEntity.ok(new RespostaResponseDTO(resposta));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar resposta.");
        }
    }

    @PutMapping("/{respostaId}/{usuarioId}")
    public ResponseEntity<?> atualizarResposta(@PathVariable Long respostaId,
                                               @PathVariable Long usuarioId,
                                               @RequestBody @Valid RespostaUpdateDTO dto) {
        try {
            var resposta = respostaService.atualizarResposta(respostaId, usuarioId, dto);
            return ResponseEntity.ok(new RespostaResponseDTO(resposta));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar resposta.");
        }
    }

    @DeleteMapping("/{respostaId}/{usuarioId}")
    public ResponseEntity<?> deletarResposta(@PathVariable Long respostaId, @PathVariable Long usuarioId) {
        try {
            respostaService.deletarResposta(respostaId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar resposta.");
        }
    }
}
