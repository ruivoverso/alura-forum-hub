package br.com.forumhub.demo.service;

import br.com.forumhub.demo.dto.topico.TopicoRegisterDTO;
import br.com.forumhub.demo.dto.topico.TopicoResponseDTO;
import br.com.forumhub.demo.dto.topico.TopicoUpdateDTO;
import br.com.forumhub.demo.exceptions.CustomException;
import br.com.forumhub.demo.exceptions.DuplicateTopicoException;
import br.com.forumhub.demo.exceptions.NotFoundException;
import br.com.forumhub.demo.exceptions.UnauthorizedException;
import br.com.forumhub.demo.model.entities.Topico;
import br.com.forumhub.demo.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public TopicoResponseDTO criarTopico(TopicoRegisterDTO dto) {
        try {
            if (topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem())) {
                throw new DuplicateTopicoException(dto.titulo(), dto.mensagem());
            }

            var usuario = usuarioService.buscarUsuarioPorId(dto.autorId());
            var topico = new Topico(dto.titulo(), dto.mensagem(), usuario);

            topicoRepository.save(topico);
            return new TopicoResponseDTO(topico);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o tópico.", e);
        }
    }

    public Page<Topico> listarTopicos(Pageable pageable) {
        try {
            return topicoRepository.findAll(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar os tópicos.", e);
        }
    }

    public Page<Topico> buscarTopicosPorData(LocalDate data, Pageable pageable) {
        try {
            LocalDateTime inicioDoDia = data.atStartOfDay();
            LocalDateTime fimDoDia = data.atTime(LocalTime.MAX);
            return topicoRepository.findByDataCriacaoBetween(inicioDoDia, fimDoDia, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tópicos pela data.", e);
        }
    }

    public Topico buscarTopicoPorId(Long id) {
        try {
            return topicoRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Tópico com ID " + id + " não foi encontrado."));
        } catch (NotFoundException e) {
            throw new NotFoundException("Tópico com ID " + id + " não foi encontrado.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tópico por ID.", e);
        }
    }

    public Topico atualizarTopico(Long topicoId, Long usuarioId, TopicoUpdateDTO dto) {
        try {
            var topico = buscarTopicoPorId(topicoId);
            var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

            if (!usuario.getId().equals(topico.getAutor().getId())) {
                throw new UnauthorizedException("Usuário não autorizado a modificar este tópico.");
            }

            topico.setTitulo(dto.titulo());
            topico.setMensagem(dto.mensagem());

            return topicoRepository.save(topico);
        } catch (NotFoundException e) {
            throw new NotFoundException("Tópico com ID " + topicoId + " não foi encontrado.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o tópico.", e);
        }
    }

    public void deletarTopico(Long topicoId, Long usuarioId) {
        try {
            var usuario = usuarioService.buscarUsuarioPorId(usuarioId);
            var topico = buscarTopicoPorId(topicoId);

            if (!usuario.getId().equals(topico.getAutor().getId())) {
                throw new UnauthorizedException("Usuário não autorizado a deletar este tópico.");
            }

            topicoRepository.delete(topico);
        } catch (UnauthorizedException e){
            throw new UnauthorizedException("Usuário não autorizado a deletar este tópico.");
        } catch (NotFoundException e) {
            throw new NotFoundException("Tópico com ID " + topicoId + " não foi encontrado.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar o tópico.", e);
        }
    }
}
