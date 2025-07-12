package br.com.api.infracoes.features.violations.exceptions;

public class ViolationNotActiveException extends RuntimeException {
    public ViolationNotActiveException(String message) {
        super(message);
    }
}
