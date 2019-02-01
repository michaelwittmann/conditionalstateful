[![Build Status](https://travis-ci.org/zevada/stateful.svg?branch=master)](https://travis-ci.org/zevada/stateful)

# Introduction

Conditionalstateful is a modification of the simple and lightweight event driven state machine library [stateful](https://travis-ci.org/zevada/stateful). With 
Conditionalstateful you can implement complex state dependant logic with evaluation of matching conditions in a clean and simple design. 

# Installation

The project is built using Maven and the artifacts will be available at maven central in the future

# Usage

### Simple Transitions

```java
enum State {
  INIT, RUNNING, COMPLETED
}

...

StateVariable<Integer> a = new StateVariable<>(5);
StateVariable<Boolean> b = new StateVariable<>(false);

StateMachine<State> stateMachine =
  new StateMachineBuilder<State>(State.INIT)
    .addTransition(State.INIT, () -> a.getValue() >=10 , State.RUNNING)
    .addTransition(State.RUNNING,() -> b, State.COMPLETED)
    .build();

stateMachine.getState(); // State.INIT
a.setValue(10)
stateMachine.evaluate()
stateMachine.getState(); // State.RUNNING
b.setValue(true)
stateMachine.evaluate()
stateMachine.getState(); // State.COMPLETED
```

### On State Enter/Exit Listeners

```java
StateMachine<State, > stateMachine =
  new StateMachineBuilder<State>(State.INIT)
    .addTransition(State.INIT, () -> a.getValue() >=10 , State.RUNNING)
    .onExit(State.INIT, () -> System.out.println("Exiting Init!"))
    .onEnter(State.RUNNING, () -> System.out.println("Entering Running!"))
    .build();
a.setValue(10)
stateMachine.evaluate()
```
