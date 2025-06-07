package ureca.muneobe.entity;

import java.util.HashSet;
import java.util.Objects;

public class Main {

    static class Person {

        private String name;

        public Person(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

//        @Override
//        public boolean equals(Object obj) {
//            return this.name.equals(((Person) obj).getName());
//        }
    }

    public static void main(String[] args) {
        Person person1 = new Person("정지호");
        Person person2 = new Person("정지호");

        person1.equals(person2);

        HashSet<Person> persons = new HashSet<>();
        persons.add(person1);
        persons.add(person2);
        persons.remove(new Person("정지호")) ;
        System.out.println(persons.size());
    }
}
