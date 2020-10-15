//package me.silloy.study.stateless4j.controller;
//
//import com.github.oxo42.stateless4j.StateMachineConfig;
//
///**
// * @author shaohuasu
// * @since 1.8
// */
//public class IndexController {
//
//    private enum Trigger { NEXT, PREV }
//    private enum State { ALPHA, BRAVO }
//
//    public static void main(String[] args) {
//        StateMachineConfig<State, Trigger> phoneCallConfig = new StateMachineConfig<>();
//
//        phoneCallConfig.configure(State.OffHook)
//                .permit(Trigger.CallDialed, State.Ringing);
//
//        phoneCallConfig.configure(State.Ringing)
//                .permit(Trigger.HungUp, State.OffHook)
//                .permit(Trigger.CallConnected, State.Connected);
//
//// this example uses Java 8 method references
//// a Java 7 example is provided in /examples
//        phoneCallConfig.configure(State.Connected)
//                .onEntry(this::startCallTimer)
//                .onExit(this::stopCallTimer)
//                .permit(Trigger.LeftMessage, State.OffHook)
//                .permit(Trigger.HungUp, State.OffHook)
//                .permit(Trigger.PlacedOnHold, State.OnHold);
//
//// ...
//
//        StateMachine<State, Trigger> phoneCall =
//                new StateMachine<>(State.OffHook, phoneCallConfig);
//
//        phoneCall.fire(Trigger.CallDialed);
//        assertEquals(State.Ringing, phoneCall.getState());
//    }
//
//}
