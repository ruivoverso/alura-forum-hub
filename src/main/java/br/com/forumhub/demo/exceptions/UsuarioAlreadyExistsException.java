package br.com.forumhub.demo.exceptions;

public class UsuarioAlreadyExistsException extends CustomException {
    public UsuarioAlreadyExistsException(String message) {
        super(message, "USER_ALREADY_EXISTS");
    }
}
