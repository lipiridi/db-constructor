package com.divizia.dbconstructor.model;

@FunctionalInterface
public interface Updatable<T> {
    T updateAllowed(T other);
}
