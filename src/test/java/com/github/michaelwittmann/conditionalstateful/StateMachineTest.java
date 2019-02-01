package com.github.michaelwittmann.conditionalstateful;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StateMachineTest {
    enum State {
        IDLE, DRIVING, CHARGING
    }

    @Test
    public void testStateMachineTransitions() throws InconsistentTranstitionException, UnevaluatedTransitionException {
        StateVariable<Integer> position = new StateVariable(1);
        StateVariable<Integer> targetPosition = new StateVariable(2);

        StateVariable<Double> charge = new StateVariable<>(85.5);


        StateMachine<State> stateMachine =
                new StateMachineBuilder<State>(State.IDLE)
                        .onEnter(State.IDLE, () -> System.out.println("Entering IDLE"))
                        .onExit(State.IDLE, () -> System.out.println("Exit IDLE"))
                        .onEnter(State.DRIVING, () -> System.out.println("Entering DRIVING"))
                        .onExit(State.DRIVING, () -> System.out.println("Exit DRIVING"))
                        .onEnter(State.CHARGING, () -> System.out.println("Entering CHARGING"))
                        .onExit(State.CHARGING, () -> System.out.println("Exit CHARGING"))
                        .addTransition(State.IDLE, () -> position.getValue()!=targetPosition.getValue(), State.DRIVING)
                        .addTransition(State.IDLE, () -> shouldRecharge(charge.getValue()), State.CHARGING)
                        .addTransition(State.DRIVING, () -> position.getValue()==targetPosition.getValue(), State.IDLE)
                        .addTransition(State.CHARGING, () -> charge.getValue() >=80, State.IDLE)
                        .build();

        stateMachine.evaluate();
        targetPosition.setValue(1);
        stateMachine.evaluate();
        position.setValue(1);
        stateMachine.evaluate();
        charge.setValue(10.0);
        stateMachine.evaluate();
        charge.setValue(90.0);
        stateMachine.evaluate();

    }

    public boolean shouldRecharge(double currrentCharge){
        return currrentCharge<=25?true:false;
    }

    @Test(expected = InconsistentTranstitionException.class)
    public void forceTransitionsIntoError() throws UnevaluatedTransitionException {
        MyDataContainer position = new MyDataContainer(1, 0);
        MyDataContainer targetPosition = new MyDataContainer(1, 0);

        MyDataContainer charge = new MyDataContainer(1, 85);

        StateMachine<State> stateMachine =
                new StateMachineBuilder<State>(State.IDLE)
                        .onEnter(State.IDLE, () -> System.out.println("Entering IDLE"))
                        .onExit(State.IDLE, () -> System.out.println("Exit IDLE"))
                        .onEnter(State.DRIVING, () -> System.out.println("Entering DRIVING"))
                        .onExit(State.DRIVING, () -> System.out.println("Exit DRIVING"))
                        .onEnter(State.CHARGING, () -> System.out.println("Entering CHARGING"))
                        .onExit(State.CHARGING, () -> System.out.println("Exit CHARGING"))
                        .addTransition(State.IDLE, () -> position.getValue()!=targetPosition.getValue(), State.DRIVING)
                        .addTransition(State.IDLE, () -> shouldRecharge(charge.getValue()), State.CHARGING)
                        .addTransition(State.DRIVING, () -> position.getValue()==targetPosition.getValue(), State.IDLE)
                        .addTransition(State.CHARGING, () -> charge.getValue() >=80, State.IDLE)
                        .build();

        stateMachine.evaluate();
        targetPosition.setValue(1);
        charge.setValue(10);
        stateMachine.evaluate();
    }

    @Test(expected = InconsistentTranstitionException.class)
    public void checkRedundantTransitions() throws UnevaluatedTransitionException {
        MyDataContainer position = new MyDataContainer(1, 0);
        MyDataContainer targetPosition = new MyDataContainer(1, 0);

        MyDataContainer charge = new MyDataContainer(1, 85);

        StateMachine<State> stateMachine =
                new StateMachineBuilder<State>(State.IDLE)
                        .onEnter(State.IDLE, () -> System.out.println("Entering IDLE"))
                        .onExit(State.IDLE, () -> System.out.println("Exit IDLE"))
                        .onEnter(State.DRIVING, () -> System.out.println("Entering DRIVING"))
                        .onExit(State.DRIVING, () -> System.out.println("Exit DRIVING"))
                        .onEnter(State.CHARGING, () -> System.out.println("Entering CHARGING"))
                        .onExit(State.CHARGING, () -> System.out.println("Exit CHARGING"))
                        .addTransition(State.IDLE, () -> true, State.DRIVING)
                        .addTransition(State.IDLE, () -> false, State.DRIVING)
                        .build();


    }

    static class MyDataContainer{
        private int id;
        private double value;

        public MyDataContainer(int id, double value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
