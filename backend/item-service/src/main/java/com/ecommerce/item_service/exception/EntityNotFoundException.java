package com.ecommerce.item_service.exception;
import java.util.Arrays;

public class EntityNotFoundException extends RuntimeException {
    private final String[] args;

    public EntityNotFoundException(String... args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "EntityNotFoundException " + Arrays.toString(args);
    }

}