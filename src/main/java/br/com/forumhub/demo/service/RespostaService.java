package br.com.forumhub.demo.service;

import br.com.forumhub.demo.dto.resposta.RespostaRequestDTO;
import br.com.forumhub.demo.dto.resposta.RespostaResponseDTO;
import br.com.forumhub.demo.dto.resposta.RespostaUpdateDTO;
import br.com.forumhub.demo.exceptions.NotFoundException;
import br.com.forumhub.demo.exceptions.UnauthorizedException;
import br.com.forumhub.demo.model.entities.Resposta;
import br.com.forumhub.demo.repository.RespostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoService topicoService;

    @Autowired
    private UsuarioService usuarioService;

    public Resposta criarResposta(RespostaRequestDTO dto) {
        var topico = topicoService.buscarTopicoPorId(dto.topicoId());
        var usuario = usuarioService.buscarUsuarioPorId(dto.usuarioId());

        if (!"ABERTO".equals(topico.getStatus())) {
            throw new IllegalStateException("Não é possível adicionar respostas a um tópico fechado.");
        }

        var resposta = new Resposta(dto.mensagem(), topico, usuario);
        return respostaRepository.save(resposta);
    }

    public Page<RespostaResponseDTO> listarRespostas(Pageable pageable) {
        return respostaRepository.findAll(pageable)
                .map(RespostaResponseDTO::new);
    }

    public Resposta buscarPorId(Long id) {
        return respostaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resposta não encontrada com o ID: " + id));
    }

    public Resposta atualizarResposta(Long respostaId, Long usuarioId, RespostaUpdateDTO dto) {
        var resposta = buscarPorId(respostaId);
        var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

        if (!resposta.getUsuario().getId().equals(usuario.getId())) {
            throw new UnauthorizedException("Usuário não autorizado a alterar esta resposta.");
        }

        resposta.setMensagem(dto.mensagem());
        return respostaRepository.save(resposta);
    }

    public void deletarResposta(Long respostaId, Long usuarioId) {
        var resposta = buscarPorId(respostaId);
        var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

        if (!resposta.getUsuario().getId().equals(usuario.getId())) {
            throw new UnauthorizedException("Usuário não autorizado a deletar esta resposta.");
        }

        respostaRepository.deleteById(respostaId);
    }
}
