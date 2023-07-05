package com.practice.data.java.stream;

import com.practice.data.util.ObjectConverter;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

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
    void reduceThreadUnsafeInParallel() {
        final var result = sample.stream()
                .reduce(new Statistics(0, 0), Statistics::accumulate, Statistics::combine);
        System.out.println(result);
    }

    @Test
    void reduceThreadSafeInParallel() {
        final var result = sample.stream()
                        .collect(new CustomSumCollector());
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

    static class CustomSumCollector implements Collector<Person, Statistics, Statistics> {
        @Override
        public Supplier<Statistics> supplier() {
            return Statistics::new;
        }

        @Override
        public BiConsumer<Statistics, Person> accumulator() {
            return Statistics::accumulate;
        }

        @Override
        public BinaryOperator<Statistics> combiner() {
            return Statistics::combine;
        }

        @Override
        public Function<Statistics, Statistics> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED, Characteristics.CONCURRENT);
        }
    }

    @Getter
    @ToString
    static class Statistics {
        int sumAge;
        int sumSalary;

        Statistics() {
            sumAge = 0;
            sumSalary = 0;
        }

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
