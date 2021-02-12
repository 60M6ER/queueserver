package com.baikalsr.queueserver.exceptions;

public class ManagerNotFound extends RuntimeException {
    public ManagerNotFound(long id) {
        super("Мэнеджер с таким id не найден " + id);
    }

    public ManagerNotFound(String loginAD) {
        super("Мэнеджер с таким логином не найден " + loginAD);
    }
}
