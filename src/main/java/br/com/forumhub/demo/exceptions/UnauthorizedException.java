package br.com.forumhub.demo.exceptions;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
}

