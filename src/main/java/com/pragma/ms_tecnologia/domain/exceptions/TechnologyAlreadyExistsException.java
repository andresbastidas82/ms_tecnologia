package com.pragma.ms_tecnologia.domain.exceptions;

import static com.pragma.ms_tecnologia.domain.utils.Constants.TECHNOLOGY_ALREADY_EXISTS;

public class TechnologyAlreadyExistsException extends RuntimeException {
    public TechnologyAlreadyExistsException() {
        super(TECHNOLOGY_ALREADY_EXISTS);
    }
}
