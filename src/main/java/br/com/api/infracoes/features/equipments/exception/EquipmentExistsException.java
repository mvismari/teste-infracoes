package br.com.api.infracoes.features.equipments.exception;

public class EquipmentExistsException extends RuntimeException{
    public EquipmentExistsException(String message) {
        super(message);
    }
}
