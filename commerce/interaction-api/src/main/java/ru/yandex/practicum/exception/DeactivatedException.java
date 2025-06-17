package ru.yandex.practicum.exception;

public class DeactivatedException extends RuntimeException {

    public DeactivatedException(String message) {
        super(message);
    }
}
