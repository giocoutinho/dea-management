package br.com.dea.management.exceptions;

import java.util.List;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Class<?> clazz, long id) {
        super(String.format("Entity %s with id %d not found", clazz.getSimpleName(), id));
    }

    public NotFoundException(Class<?> clazz, List<Long> ids) {
        super(String.format("Entity %s with id %s not found", clazz.getSimpleName(), ids));
    }

    public NotFoundException(Class<?> clazz, String id) {
        super(String.format("Entity %s with id %s not found", clazz.getSimpleName(), id));
    }

}
