package com.avanade.user.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class DatabaseException extends DataIntegrityViolationException {

    public DatabaseException(String msg) {
        super(msg);
    }
}