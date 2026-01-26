package com.pragma.ms_tecnologia.domain.exceptions;

public class TechnologyAlreadyExistsException extends RuntimeException {
    public TechnologyAlreadyExistsException() {
        super("La tecnología ya existe.");
    }
}
