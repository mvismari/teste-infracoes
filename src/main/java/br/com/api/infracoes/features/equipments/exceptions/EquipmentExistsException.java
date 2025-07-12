package br.com.api.infracoes.features.equipments.exceptions;

public class EquipmentExistsException extends RuntimeException{
    public EquipmentExistsException(String message) {
        super(message);
    }
}
