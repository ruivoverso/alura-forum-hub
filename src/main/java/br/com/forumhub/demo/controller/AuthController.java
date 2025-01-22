package br.com.forumhub.demo.controller;

import br.com.forumhub.demo.config.security.TokenService;
import br.com.forumhub.demo.dto.usuario.LoginResponseDTO;
import br.com.forumhub.demo.dto.usuario.UsuarioLoginDTO;
import br.com.forumhub.demo.dto.usuario.UsuarioRegisterDTO;
import br.com.forumhub.demo.dto.usuario.UsuarioResponseDTO;
import br.com.forumhub.demo.model.entities.Usuario;
import br.com.forumhub.demo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UsuarioLoginDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var usuario = usuarioService.buscarUsuarioPorEmail(data.email());
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok().body(new LoginResponseDTO(usuario.getId(), token));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody @Valid UsuarioRegisterDTO dto) {
        try {
            return ResponseEntity.ok().body(new UsuarioResponseDTO(usuarioService.adicionarUsuario(dto)));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}