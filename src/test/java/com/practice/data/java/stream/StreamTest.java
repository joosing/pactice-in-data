package com.practice.data.java.stream;

import com.practice.data.util.ObjectConverter;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public class StreamTest {
    List<Person> sample = new ArrayList<>();

    @BeforeEach
    void setUp() {
        sample.add(new Person(40, "David", "Reading", 1000));
        sample.add(new Person(30, "Kary", "Writing", 2000));
        sample.add(new Person(20, "Alice", "Reading", 3000));
        sample.add(new Person(10, "Hello", "Writing", 1500));
    }

    @Test
    void reduce() {
        final var result = sample.stream()
                .map(Person::getAge)
                .reduce(0, Integer::sum);
        System.out.println(result);
    }

    @Test
    void reduceToOtherObject() {
        // reduce API의 accumulator와 combiner가 상태를 가지기 때문에 병렬 스트림을 사용할 수 없습니다.
        final var result = sample.stream()
                .reduce(new Statistics(0, 0), Statistics::accumulate, Statistics::combine);
        System.out.println(result);
    }

    @Test
    void group() {
        final var result = sample.stream()
                .collect(groupingBy(Person::getHabit, mapping(ObjectConverter::toMap, toList())));
        System.out.println(result);
    }

    @Test
    void map() {
        final var result = sample.stream()
                .map(ObjectConverter::toMap)
                .collect(toMap(map -> map.get("name"), Function.identity()));
        System.out.println(result);
    }

    @Getter
    @ToString
    static class Statistics {
        int sumAge;
        int sumSalary;

        Statistics(int sumAge, int sumSalary) {
            this.sumAge = sumAge;
            this.sumSalary = sumSalary;
        }

        private void addAge(int age) {
            sumAge += age;
        }

        private void addSalary(int salary) {
            sumSalary += salary;
        }

        public static Statistics accumulate(Statistics statistics, Person person) {
            statistics.addAge(person.getAge());
            statistics.addSalary(person.getSalary());
            return statistics;
        }

        public static Statistics combine(Statistics s1, Statistics s2) {
            s1.addAge(s2.getSumAge());
            s1.addSalary(s2.getSumSalary());
            return s1;
        }
    }

    @Value
    static class Person {
        int age;
        String name;
        String habit;
        int salary;
    }
}
