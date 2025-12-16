package de.digitalservice.service.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class RoleNumberRegistry {

    private final AtomicInteger nextRoleNumber = new AtomicInteger(0);

    public int next() {
        return nextRoleNumber.getAndIncrement();
    }

    public int current() {
        return nextRoleNumber.get();
    }
}
