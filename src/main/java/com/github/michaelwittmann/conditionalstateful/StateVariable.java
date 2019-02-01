package com.github.michaelwittmann.conditionalstateful;

/**
 * @author Michael Wittmann on 01.02.2019
 */
public class StateVariable<T> {
    private T value;
    public StateVariable(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
