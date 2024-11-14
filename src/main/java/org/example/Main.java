package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Класс для сериализации YAML и XML
@XmlRootElement(name = "people")
class People {
    private List<Person> personList;

    @XmlElement(name = "person")
    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }
}

class Person {
    public int id;
    public String name;
    public int age;
    public boolean isStudent;
    public List<Course> courses;
    public Address address;

    public Person() {}

    public Person(int id, String name, int age, boolean isStudent, List<Course> courses, Address address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isStudent = isStudent;
        this.courses = courses;
        this.address = address;
    }
}

class Course {
    public String title;
    public int credits;

    public Course() {}

    public Course(String title, int credits) {
        this.title = title;
        this.credits = credits;
    }
}

class Address {
    public String street;
    public String city;
    public String zip;

    public Address() {}

    public Address(String street, String city, String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        Random random = new Random();

        String[] courseTitles = {"Math", "Biology", "Chemistry", "History"};
        String[] cities = {"Springfield", "Shelbyville", "Capital City"};

        for (int i = 0; i < 2150; i++) {
            Person person = new Person();
            person.id = i + 1;
            person.name = "Person_" + (i + 1);
            person.age = random.nextInt(48) + 18; // Возраст от 18 до 65
            person.isStudent = random.nextBoolean();

            // Генерация курсов
            int numberOfCourses = random.nextInt(3) + 1; // от 1 до 3 курсов
            person.courses = new ArrayList<>();
            for (int j = 0; j < numberOfCourses; j++) {
                String title = courseTitles[random.nextInt(courseTitles.length)];
                int credits = random.nextInt(5) + 1; // от 1 до 5 кредитов
                person.courses.add(new Course(title, credits));
            }

            // Генерация адреса
            String street = (random.nextInt(999) + 1) + " Main St";
            String city = cities[random.nextInt(cities.length)];
            String zip = String.valueOf(random.nextInt(90000) + 10000); // 5-значный ZIP-код
            person.address = new Address(street, city, zip);

            people.add(person);
        }

        // Запись в JSON файл
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("data.json"), people);
            System.out.println("Файл data.json успешно создан!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Запись в XML файл
        People peopleWrapper = new People();
        peopleWrapper.setPersonList(people);
        try {
            JAXBContext context = JAXBContext.newInstance(People.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(peopleWrapper, new File("data.xml"));
            System.out.println("Файл data.xml успешно создан!");
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        // Запись в YAML файл
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        try (FileWriter writer = new FileWriter("data.yml")) {
            yaml.dump(people, writer);
            System.out.println("Файл data.yml успешно создан!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}