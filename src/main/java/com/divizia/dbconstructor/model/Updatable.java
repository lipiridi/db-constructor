package com.divizia.dbconstructor.model;

@FunctionalInterface
public interface Updatable<T> {
    void updateAllowed(T other);
}
