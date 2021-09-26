package e2018.exam2;

interface Parent {
    void method1();
}

class Child1 implements Parent{
    @Override
    public void method1() {
        System.out.println("Child1");
    }
}

class Child2 implements Parent{
    @Override
    public void method1() {
        System.out.println("Child2");
    }
}

public class Exam09 {

    static void doSomething(Child1 child) {
        child.method1();
    }

    static void doSomething(Child2 child) {
        child.method1();
    }

    public static void main(String[] args) {
        Child1 child1 = new Child1();
        Child2 child2 = new Child2();
        doSomething(child1); // 다형성 호출이 아님
        doSomething(child2); // 다형성 호출이 아님
    }
}
