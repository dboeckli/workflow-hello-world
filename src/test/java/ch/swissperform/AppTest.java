package ch.swissperform;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Philipp Gatzka
 * Sample Tests, should be deleted when using this template
 */
class AppTest {

    @Test
    void test1() {
        Person person = new Person("Franz", "Carl");
        assertEquals("Franz", person.getFirstname());
    }

    @Test
    void test2() {
        App app = new App();
        List<Person> family = app.createFamily();

        Person person1 = new Person("Franz", "Schuhmann");
        Person person2 = new Person("Marie", "Schuhmann");
        Person person3 = new Person("Carlos", "Schuhmann");
        Person person4 = new Person("Jeff", "Schuhmann");


        assertTrue(family.stream().anyMatch(person -> person.getFirstname().equals(person1.getFirstname())));
        assertTrue(family.stream().anyMatch(person -> person.getFirstname().equals(person2.getFirstname())));
        assertTrue(family.stream().anyMatch(person -> person.getFirstname().equals(person3.getFirstname())));
        assertTrue(family.stream().anyMatch(person -> person.getFirstname().equals(person4.getFirstname())));
    }

    @TestFactory
    List<DynamicTest> test3() {
        App app = new App();
        List<Person> family = app.createFamily();
        Person person1 = new Person("Franz", "Schuhmann");
        Person person2 = new Person("Marie", "Schuhmann");
        Person person3 = new Person("Carlos", "Schuhmann");
        Person person4 = new Person("Jeff", "Schuhmann");
        return List.of(
                DynamicTest.dynamicTest("Person1", () -> assertTrue(family.stream().anyMatch(person -> person.getFirstname().equals(person1.getFirstname())))),
                DynamicTest.dynamicTest("Person2", () -> assertTrue(family.stream().anyMatch(person -> person.getFirstname().equals(person2.getFirstname())))),
                DynamicTest.dynamicTest("Person3", () -> assertTrue(family.stream().anyMatch(person -> person.getFirstname().equals(person3.getFirstname())))),
                DynamicTest.dynamicTest("Person4", () -> assertTrue(family.stream().anyMatch(person -> person.getFirstname().equals(person4.getFirstname()))))
        );


    }


}
