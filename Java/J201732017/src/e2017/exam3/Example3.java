package e2017.exam3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Person {
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("Person(\"%s\",%d) ", name, age);
    }
}

class PersonComparator implements Comparator<Person> {
    public static final int ORDERBY_NAME = 0;
    public static final int ORDERBY_AGE = 1;
    public static final int ASC = 1;
    public static final int DESC = -1;

    int orderBy, asc;

    public PersonComparator(int orderBy, int asc) {
        this.orderBy = orderBy;
        this.asc = asc;
    }

    @Override
    public int compare(Person p1, Person p2) {
        if (orderBy == ORDERBY_NAME)
            return p1.name.compareTo(p2.name) * asc;
        return (p1.age - p2.age) * asc;
    }
}

public class Example3 {

    static void sort1(List<Person> list) {
        Collections.sort(list, new PersonComparator(PersonComparator.ORDERBY_NAME, PersonComparator.ASC));
    }

    static void sort2(List<Person> list) {
        Collections.sort(list, new PersonComparator(PersonComparator.ORDERBY_NAME, PersonComparator.DESC));
    }

    static void sort3(List<Person> list) {
        Collections.sort(list, new PersonComparator(PersonComparator.ORDERBY_AGE, PersonComparator.ASC));
    }

    static void sort4(List<Person> list) {
        Collections.sort(list, new PersonComparator(PersonComparator.ORDERBY_AGE, PersonComparator.DESC));
    }

    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("홍길동", 18));
        list.add(new Person("임꺽정", 19));
        list.add(new Person("전우치", 20));
        sort1(list);
        System.out.println(list.toString());
        sort2(list);
        System.out.println(list.toString());
        sort3(list);
        System.out.println(list.toString());
        sort4(list);
        System.out.println(list.toString());
    }
}
