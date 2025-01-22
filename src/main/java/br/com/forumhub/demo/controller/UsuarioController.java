package br.com.forumhub.demo.controller;

import br.com.forumhub.demo.dto.usuario.UsuarioRegisterDTO;
import br.com.forumhub.demo.dto.usuario.UsuarioResponseDTO;
import br.com.forumhub.demo.dto.usuario.UsuarioUpdateDTO;
import br.com.forumhub.demo.exceptions.NotFoundException;
import br.com.forumhub.demo.exceptions.UsuarioAlreadyExistsException;
import br.com.forumhub.demo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/id/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            var usuario = usuarioService.buscarUsuarioPorId(id);
            return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarUsuarioPorEmail(@PathVariable String email) {
        try {
            var usuario = usuarioService.buscarUsuarioPorEmail(email);
            return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateDTO dto) {
        try {
            var usuario = usuarioService.atualizarUsuario(id, dto);
            return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /*

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            usuarioService.deletarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    */
}
