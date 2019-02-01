package com.github.michaelwittmann.conditionalstateful;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author Michael Wittmann on 01.02.2019
 */
public class Transition<State extends Enum<State>> {
    private Node<State> source;
    private Node<State> target;
    private Callable<Boolean> condition;

    public Transition(Node<State> source, Callable<Boolean> condition, Node<State> target) {
        this.condition = condition;
        this.source = source;
        this.target = target;
    }

    public State evaluate() throws Exception {
           return condition.call() ? target.getState():source.getState();

    }

    public Node<State> getSource() {
        return source;
    }

    public Node<State> getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition<?> that = (Transition<?>) o;
        return getSource().equals(that.getSource()) &&
                getTarget().equals(that.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget());
    }
}
