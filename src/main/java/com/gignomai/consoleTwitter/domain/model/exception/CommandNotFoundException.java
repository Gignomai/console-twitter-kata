package com.gignomai.consoleTwitter.domain.model.exception;

public class CommandNotFoundException extends Throwable {
    public CommandNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
