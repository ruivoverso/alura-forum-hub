package br.com.forumhub.demo.exceptions;

public class DuplicateTopicoException extends CustomException{
    public DuplicateTopicoException(String titulo, String mensagem) {
        super("Já existe um tópico com o título '" + titulo + "' e a mensagem fornecida.", "DUPLICATE_TOPICO");
    }
}
