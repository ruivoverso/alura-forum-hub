package br.com.forumhub.demo.exceptions;

public class NotFoundException extends CustomException{

    public NotFoundException(String message) {
        super(message, "NOT_FOUND");
    }
}
