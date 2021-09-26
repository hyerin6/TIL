package e2018.exam2.sol;

class World {
    String message = "안녕하세요";

    public void doSomething() {
        Hello hello = new Hello();
        hello.say();
    }

    class Hello {
        public void say() {
            System.out.println(message);
        }
    }
}

public class Exam12 {

    public static void main(String[] args) {
        World world = new World();
        world.doSomething();
    }
}
