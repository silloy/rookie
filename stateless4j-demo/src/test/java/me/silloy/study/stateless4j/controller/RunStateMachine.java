package me.silloy.study.stateless4j.controller;

import com.github.oxo42.stateless4j.StateMachine;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.testng.Assert.*;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class RunStateMachine {
    private static StateMachine<CurrentState, Trigger> stateMachine =
            new StateMachine<>(CurrentState.SMALL, StateConver.config);

    @Test
    public void testStateMachine() {
        Properties properties = new Properties();
        stateMachine.fire(Trigger.FLOWER);
        System.out.println("currentState-->" + stateMachine.getState());
        stateMachine.fire(Trigger.MONSTER);
    }



}