package com.practice.data.behavior;

import lombok.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ObjectToMapTest {

    @Test
    void map() throws IllegalAccessException {
        // given
        var person = new Person(40, "Joo", "Reading");

        // when
        Map<String, Object> map = new HashMap<>();
        var fields = person.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object value = field.get(person);
            map.put(field.getName(), value);
        }

        // then
        assertEquals(map.get("age"), 40);
        assertEquals(map.get("name"), "Joo");
        assertEquals(map.get("habit"), "Reading");
    }

    @Value
    static class Person {
        int age;
        String name;
        String habit;
    }
}
