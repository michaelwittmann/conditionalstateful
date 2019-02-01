package com.github.michaelwittmann.conditionalstateful;

/**
 * A simple transition based state machine.
 *
 * @param <State> The state of the entity
 */
public final class StateMachine<State extends Enum<State>> {
  private Node<State> root;

  StateMachine(Node<State> root) {
    this.root = root;
  }

  /**
   * Apply an event to the state machine.
   */
  public void evaluate() throws UnevaluatedTransitionException, InconsistentTranstitionException {

    Node<State> nextNode = null;
    nextNode = root.evaluate();
    if (!nextNode.equals(root)){
      root.onExit();
      root = nextNode;
      root.onEnter();
    }

  }

  /**
   * @return The current state of the state machine
   */
  public State getState() {
    return root.getState();
  }
}
