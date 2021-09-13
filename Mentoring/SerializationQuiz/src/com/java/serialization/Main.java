package com.java.serialization;

public class Main {

    // 이 코드는 InvalidClassException이 발생한다.
    public static void main(String[] args) throws Exception {
        Serialization serialization = new Serialization();

        // SerialVersionUID 명시하지 않은 경우
        User user = new User("hyerin", 24L, "hyerinn6@gmail.com");

        // User(name, age) 직렬화
        String serializeUser1 = "rO0ABXNyABtjb20uamF2YS5zZXJpYWxpemF0aW9uLlVzZXIjvHPIBefM0QIAAkoAA2FnZUwABG5hbWV0ABJMamF2YS9sYW5nL1N0cmluZzt4cAAAAAAAAAAYdAAGaHllcmlu";
        // User(name, age, email) 직렬화
        String serializeUser2 = serialization.serialize(user);
        System.out.println(serializeUser2);

        // 역직렬화
        User deserializeUser1 = serialization.deserialize(serializeUser1);
        User deserializeUser2 = serialization.deserialize(serializeUser2);

        System.out.println("\n" + deserializeUser1);
        System.out.println(deserializeUser2);

    }
}
