package com.github.michaelwittmann.conditionalstateful;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * A builder for simple event based state machines
 *
 * @param <State> The state of the entity

 */
final public class StateMachineBuilder<State extends Enum<State>> {
  private final Map<State, Node<State>> nodes;
  private final Node<State> root;

  /**
   * @param initialState the initial state of the state machine
   */
  public StateMachineBuilder(State initialState) {
    nodes = new HashMap<>();
    root = new Node<>(initialState);
    nodes.put(initialState, root);
  }

  /**
   * Use this method to construct the state machine after
   * completing the declaration of state machine
   * topology and listeners.
   *
   * @return the final state machine
   */
  public StateMachine<State> build() {
    return new StateMachine<>(root);
  }

  /**
   * Add a transition to the state machine from "startState"
   * to "endState" in response to events of type "eventType"
   *
   * @param startState the starting state of the transition
   * @param condition the transition condition expression
   * @param endState the end state of the transition
   */
  public StateMachineBuilder<State> addTransition(State startState, Callable<Boolean> condition, State endState) {
    Node<State> startNode = nodes.get(startState);

    if (startNode == null) {
      startNode = new Node<>(startState);
      nodes.put(startState, startNode);
    }

    Node<State> endNode = nodes.get(endState);

    if (endNode == null) {
      endNode = new Node<>(endState);
      nodes.put(endState, endNode);
    }
    startNode.addTransition(new Transition<>(startNode, condition, endNode));

    return this;
  }

  /**
   * Add a runnable to the state machine which will only be
   * executed when the state machine enters the specified state.
   *
   * @param state The state for which we are listening to onEnter events
   * @param onEnter The runnable to call when the state is entered
   */
  public StateMachineBuilder<State> onEnter(State state, Runnable onEnter) {
    Node<State> node = nodes.get(state);

    if (node == null) {
      node = new Node<>(state);
      nodes.put(state, node);
    }

    node.addOnEnterListener(onEnter);

    return this;
  }

  /**
   * Add a runnable to the state machine which will only be
   * executed when the state machine exits the specified state.
   *
   * @param state The state for which we are listening to onExit events
   * @param onExit The runnable to call when the state is exited
   */
  public StateMachineBuilder<State> onExit(State state, Runnable onExit) {
    Node<State> node = nodes.get(state);

    if (node == null) {
      node = new Node<>(state);
      nodes.put(state, node);
    }

    node.addOnExitListener(onExit);

    return this;
  }
}
