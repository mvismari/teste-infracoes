package br.com.api.infracoes.shared.exceptions;

public class NotFoundErrorException extends RuntimeException{
    public NotFoundErrorException(String message) {
        super(message);
    }
}
