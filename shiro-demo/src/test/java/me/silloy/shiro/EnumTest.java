package me.silloy.shiro;


import org.junit.Test;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;


public class EnumTest {

    public enum AttemperStrategyEnum {
        TIME {
            @Override
            public boolean judge(Long value) {
                return value >= 1;
            }
        },
        REGION {
            @Override
            public boolean judge(Long value) {
                return value < 5;
            }
        };

        public abstract boolean judge(Long value);
    }


    @Test
    public void coll() {
        AttemperStrategyEnum.REGION.judge(5L);
    }


    public class IgnoreValue {

        private final Predicate<Object> predicate;

        private IgnoreValue(Predicate<Object> tPredicate) {
            this.predicate = tPredicate;
        }
    }



}