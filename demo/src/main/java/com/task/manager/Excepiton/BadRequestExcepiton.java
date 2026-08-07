package com.task.manager.Excepiton;

public class BadRequestExcepiton extends RuntimeException {
    public BadRequestExcepiton(String message) {
        super(message);
    }
}
