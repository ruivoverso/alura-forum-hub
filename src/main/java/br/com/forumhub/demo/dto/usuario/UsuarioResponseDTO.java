package br.com.forumhub.demo.dto.usuario;

import br.com.forumhub.demo.model.entities.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(), usuario.getNome(), usuario.getEmail()
        );
    }
}
