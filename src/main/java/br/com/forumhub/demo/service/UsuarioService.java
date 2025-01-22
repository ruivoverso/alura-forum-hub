package br.com.forumhub.demo.service;

import br.com.forumhub.demo.dto.usuario.UsuarioRegisterDTO;
import br.com.forumhub.demo.dto.usuario.UsuarioUpdateDTO;
import br.com.forumhub.demo.exceptions.NotFoundException;
import br.com.forumhub.demo.exceptions.UsuarioAlreadyExistsException;
import br.com.forumhub.demo.model.entities.Usuario;
import br.com.forumhub.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario adicionarUsuario(UsuarioRegisterDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()) != null) {
            throw new UsuarioAlreadyExistsException("Usuário com o email " + dto.email() + " já existe.");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        Usuario usuario = new Usuario(dto.nome(), dto.email(), senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        UserDetails userDetails = usuarioRepository.findByEmail(email);

        if (userDetails == null) {
            throw new NotFoundException("Usuário com email " + email + " não foi encontrado.");
        }

        if (!(userDetails instanceof Usuario)) {
            throw new IllegalStateException("O usuário encontrado não corresponde à entidade esperada.");
        }

        return (Usuario) userDetails;
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com ID " + id + " não foi encontrado."));
    }

    public Usuario atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        var usuario = buscarUsuarioPorId(id);
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());

        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        var usuario = buscarUsuarioPorId(id);
        usuarioRepository.delete(usuario);
    }
}
