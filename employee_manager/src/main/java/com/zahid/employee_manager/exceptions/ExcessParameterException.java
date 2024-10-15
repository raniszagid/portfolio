package com.zahid.employee_manager.exceptions;

public class ExcessParameterException extends Exception {
    public ExcessParameterException() {
        super("Лишний параметр команды");
    }
}
