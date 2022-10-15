## 1. 자원 반납과 finally  

**자원 반납 문제**  
```
변수1 = new 자원객체1(); // 자원 할당 
변수1.작업(); 
변수1.close(); // 자원 반납
```
위 코드에서 작업() 메소드가 에러 없이 실행된 경우  
마지막 문장에서 close() 메소드가 호출되어 자원이 반납된다.  

그러나 작업() 메소드 내부에서 exception이 발생하면,  
실행 흐름이 catch 블럭으로 점프하게 된다.  
이 경우에, 마지막 문장도 건너뛰게 되어 close() 메소드가 호출되지 않는다.  
→ 자원을 반납하지 못하는 문제 발생  

**finally 블럭에서 자원 반납 구현**  
```
변수1 = null;
변수2 = null;
try{
  변수1 = new 자원객체1();
  변수2 = new 자원객체2();
  변수1.작업();
  변수2.작업();
  ...
} finally {
  if(변수1 != null) 변수1.close();
  if(변수2 != null) 변수2.close();
}
```
try finally 문법으로 자원 반납(resource close)을 구현한 코드이다.  
작업 메소드에서 exception이 발생한 경우에도, 반드시 close 메소드를 호출하여 자원 반납을 해야 하는 경우에   
위 코드처럼 구현해야 한다.  
exception이 발생해서 현재 메소드의 실행을 중단하고 나갈 때에도, finally 블럭은 반드시 실행된다.  
즉 어떠한 경우에도 finally 블럭의 코드는 반드시 실행된다.  
따라서 자원을 반납하지 못하는 문제가 발생하지 않는다.  

<br>

## 2. try with resource 문법과 자원 반납    
**try with resource 문법**  
Java 1.7에 추가된 try with resource 문법을 사용하면 간결하게 구현할 수 있다.  

try with resource 문법은 다음과 같다.  
```
try (변수1 = new 자원객체1()) {
...
}
```
위 형태로 구현하면 자원객체의 close 메소드 호출은 자동으로 실행되기 때문에 finally 블럭을 생략해도 된다.  

자원 객체가 여러개인 경우 다음과 같이 구현한다.  
```
try (변수1 = new 자원객체1();
변수2 = new 자원객체2()) {
...
}
```

try 문장을 중첩하여 구현할 수도 있다.  
```
try (변수1 = new 자원객체1();
변수2 = new 자원객체2()) {
...
try (변수3 = new 자원객체3()) {
...
}
}
```
실행 흐름이 try 블럭을 빠져나갈때, 자원객체의 close 메소드가 자동으로 호출된다.  
exception이 발생해서 실행흐름이 점프할 때에서, 자원객체의 close 메소드는 반드시 자동으로 호출된다. 
(예제 생략)  
<br>

## 3. AutoCloseable 인터페이스  
자원객체가 AutoCloseable 인터페이스를 implements 한 클래스만 try with resource 문법을 사용할 수 있다.  
이 인터페이스에는 close 메소드가 정의되어 있다.  
따라서 이 인터페이스를 implements 한 클래스들은 반드시 close 메소드를 구현해야 한다.    
(예제 생략)  
<br>

## 4. JDBC 객체  
| name | 설명 | 
|:--------|:--------|
| Connection | 데이터베이스에 연결하기 위한 객체 | 
| PreparedStatement | SQL 명령을 실행하기 위한 객체이다. |  
| ResultSet | SQL 명령의 조회 결과 데이터를 전달해주는 객체이다. |  

이 클래스들은 AutoCloseable 인터페이스를 implements 하였다.  

작업이 끝난 후, Connection 객체의 close 메소드는 반드시 호출해 주어야 한다.  
PreparedStatement, ResultSet 객체의 close 메소드도 가급적 호출해 주는 것이 좋다.  
<br>

## 5. DTO, DAO  
**Data Transfer Object(DTO)**  
데이터를 채워서 전달하는 용도로 구현된 클래스  

**Data Access Object(DAO)**  
어떤 DB 테이블에 대한 SELECT,INSERT, UPDATE, DELETE 작업이 메소드로 구현된 클래스  

**클래스에 역할 한 개만 구현**  
바람직한 객체지향 프로그래밍 측면에서,  
하나의 클래스에는, 하나의 역할로 요약될 수 있는 기능들만 구현되어 있어야 한다.  
여러가지 역할들을 클래스 하나에 모아서 구현하는 것은 바람직하지 않다.  
데이터를 채워서 전달하는 용도로 구현되는 DTO 클래스에는  
데이터를 넣고 꺼내기 위한 메소드만 구현되어야 한다.  
DTO 클래스에 어떤 작업을 구현하는 것은 바람직하지 않다.  
DAO 클래스에는, 어떤 DB 테이블에 대한 SELECT,INSERT, UPDATE, DELETE 작업만 구현되어야 한다.  
DAO 클래스에 DB 데이터를 조회하고 저장하는 것과 무관한 작업을 구현하는 것은 바람직하지 않다.  
그리고 가급적 DB 테이블 하나 당 DAO 클래스 한 개를 구현하는 것이 바람직하다.  
