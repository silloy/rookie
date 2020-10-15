package me.silloy;

import lombok.Data;
import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author shaohuasu
 * @date 2019-01-22 17:31
 * @since 1.8
 */
public class SwfTest {
    public enum PlayerTypes {
        TENNIS {
            @Override
            public Player createPlayer() {
                return new Player();
            }
        },
        FOOTBALL {
            @Override
            public Player createPlayer() {
                return new Player();
            }
        },
        SNOOKER {
            @Override
            public Player createPlayer() {
                return new Player();
            }
        };

        public abstract Player createPlayer();
    }

    @Test
    public void a() {
        Player p = PlayerTypes.TENNIS.createPlayer();
        Player footballPlayer =
                PlayerTypes.valueOf("FOOTBALL").createPlayer();
    }

    @Data
    public static class Player {
    }


    @Test
    public void test() {
        getClassFieldAndMethod(JTree.class);
    }

    static void getClassFieldAndMethod(Class cur_class) {
        String class_name = cur_class.getName();
        Field[] obj_fields = cur_class.getDeclaredFields();
        for (Field field : obj_fields) {
            field.setAccessible(true);
            System.out.println(class_name + ":" + field.getName());
        }
        Method[] methods = cur_class.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            System.out.println(class_name + ":" + method.getName());
        }
        if (cur_class.getSuperclass() != null) {
            getClassFieldAndMethod(cur_class.getSuperclass());
        }
    }

}
