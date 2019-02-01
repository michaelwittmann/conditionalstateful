package com.github.michaelwittmann.conditionalstateful;

/**
 * @author Michael Wittmann on 01.02.2019
 */
public class UnevaluatedTransitionException extends Exception {
    private final Enum<?> source;
    private final String message;

    /**
     * @param source  The state on which the transition could not be evaluated
     */
    public UnevaluatedTransitionException(Enum<?> source,String message) {
        this.source = source;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Unable to evaluate transition from " + source.toString() +
                ". Message: " + message;
    }
}

