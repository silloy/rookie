package me.silloy.study.stateless4j.controller;

import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.transitions.Transition;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class ExitAction implements Action1<Transition<CurrentState,Trigger>> {
    @Override
    public void doIt(Transition<CurrentState, Trigger> arg1) {
        System.out.println("OUT FROM : " + arg1.getSource());
    }
}
