package br.com.forumhub.demo.repository;

import br.com.forumhub.demo.model.entities.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensagem(String titulo, String mensagem);

    Page<Topico> findByDataCriacao(LocalDate dataCriacao, Pageable pageable);

    Page<Topico> findByDataCriacaoBetween(LocalDateTime inicioDoDia, LocalDateTime fimDoDia, Pageable pageable);
}
