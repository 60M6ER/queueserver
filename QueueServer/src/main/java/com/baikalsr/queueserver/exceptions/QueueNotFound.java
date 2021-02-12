package com.baikalsr.queueserver.exceptions;

public class QueueNotFound extends RuntimeException {
    public QueueNotFound(long id) {
        super("Очередь с таким id не найдена " + id);
    }
}
