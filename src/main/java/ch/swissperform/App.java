package ch.swissperform;

import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author Philipp Gatzka
 * Sample Application, should be deleted when using this template
 */
@Log4j2
public class App {

    public static void main(String[] args) {
        App app = new App();
        List<Person> family = app.createFamily();
        family.forEach(person -> log.info(person.toString()));
    }

    public List<Person> createFamily() {

        Person person1 = new Person("Franz", "Schuhmann");
        Person person2 = new Person("Marie", "Schuhmann");
        Person person3 = new Person("Carlos", "Schuhmann");
        Person person4 = new Person("Jeff", "Schuhmann");
        log.info("Franz, Marie, Carlos and Jeff are now a family");

        return List.of(person1, person2, person3, person4);
    }

}
