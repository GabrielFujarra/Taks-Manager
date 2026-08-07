package com.task.manager.demo.Excepiton;

public class BadRequestExcepiton extends RuntimeException {
    public BadRequestExcepiton(String message) {
        super(message);
    }
}
