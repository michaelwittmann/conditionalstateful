package com.github.michaelwittmann.conditionalstateful;

import java.util.*;

final class Node<State extends Enum<State>> {
    private final Map<State, Node<State>> neighbors;
    private final Set<Transition> transitions;
    private final List<Runnable> onEnterListeners;
    private final List<Runnable> onExitListeners;
    private final State state;

    Node(State state) {
        this.state = state;
        neighbors = new HashMap<>();
        transitions = new HashSet<>();
        onEnterListeners = new LinkedList<>();
        onExitListeners = new LinkedList<>();
    }

    public State getState() {
        return state;
    }

    public Node<State> evaluate() throws UnevaluatedTransitionException, InconsistentTranstitionException{
        State target = null;
        State result;
        for (Transition<State> transition: transitions) {
            try {
                result = transition.evaluate();
            } catch (Exception e) {
                throw new UnevaluatedTransitionException(state, e.getMessage());
            }
            if (result != state) {
                if (target == null) {
                    target = result;
                } else {
                    throw new InconsistentTranstitionException();
                }
            }

        }
        return target==null? this: neighbors.get(target);
    }

    public void onEnter() {
        onEnterListeners.forEach(Runnable::run);
    }

    public void onExit() {
        onExitListeners.forEach(Runnable::run);
    }

    public void addTransition(Transition<State> transition) {
        neighbors.put(transition.getTarget().getState(), transition.getTarget());
        if(!transitions.add(transition)) throw new InconsistentTranstitionException();
    }

    public void addOnEnterListener(Runnable onEnterListener) {
        onEnterListeners.add(onEnterListener);
    }

    public void addOnExitListener(Runnable onExitListener) {
        onExitListeners.add(onExitListener);
    }
}
