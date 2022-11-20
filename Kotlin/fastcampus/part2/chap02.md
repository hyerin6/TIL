# 예외 처리 

* 코틀린에서 모든 예외 클래스는 최상위 클래스인 `Throwable`을 상속한다.


* Error: 시스템에 비정상적인 상황이 발생. 예측이 어렵고 기본적으로 복구가 불가능함 
    - ex) OutOfMemoryError, StackOverflowError 


* Exception: 시스템에서 포착 가능하여(try-catch) 복구 가능, 예외 처리 강제 
    - ex) IoException, FileNotFoundException 
    - `@Transactional` 에서 해당 예외가 발생하면 기본적으로는 롤백이 동작하지 않음
         + rollbackFor를 사용해야함 



* RuntimeException: 런타임시에 발생하는 예외. 예외 처리를 강제하지 않음 
    - ex) NullPointerException, ArrayIndexOutOfBoundsException 

  
* 자바에서 checked exception은 컴파일 에러가 발생하기 때문에 무조건 try-catch로 감싸거나 throws로 예외를 전파해야 한다.


* 코틀린은 checked exception을 강제하지 않는다.   
  아래 코드는 코틀린에서 컴파일 오류가 발생하지 않는다.   

```kotlin
Thread.sleep(1)
```

<br /> 
<br /> 

# 싱글톤과 동반객체 
* 싱글톤 패턴은 클래스의 인스턴스를 하나의 단일 인스턴스로 제한하는 디자인 패턴이다. 



* 싱글톤 패턴 구현 제약사항 
    - 직접 인스턴스화 하지 못하도록 생성자를 private 으로 숨긴다. 
    - `getInstance()` 라는 클래스의 단일 인스턴스를 반환하는 static 메서드를 제공한다.
    - 멀티 스레드 환경에서도 안전하게 유일한 인스턴스를 반환해야 한다. 



* 구현 방법 
    - DCL (Double Check Locking): JVM 환경에서 거의 사용하지 않음
    - Enum 싱글톤: 이펙티브 자바 설명 참고, 실무에서 거의 사용 x
    - 이른 초기화(Eager): getter에서 미리 선언해놓은 멤버 변수를 리턴, 메모리에 로드하는 단점이 있음 
    - 지연 초기화(Lazy): 내부에 static 클래스를 만들어 그 안에서 멤버 변수를 초기화 > getter에서 내부 private class의 멤버 변수를 리턴 
  
<br /> 
<br /> 

# 지연 초기화 
지연 초기화란 대상에 대한 초기화를 미뤘다가 실제 사용시점에 초기화하는 기법이다.  

* 초기화 과정에서 자원을 많이 쓰거나 오버헤드가 발생할 경우 지연초기화를 사용하는게 유리할 수 있다.   
* 지연초기화는 다음과 같은 상황에서 많이 쓰인다. 

* ex. 웹페이지에서 특정 스크롤에 도달했을 때 컨텐츠를 보여주는 무한 스크롤 

* ex. 싱글톤 패턴의 지연 초기화 

```java 
public class Java_Singleton {
  private Java_Singleton() {
    /* do nothing */
  }
  
  public Java_Singleton getInstance() {
    return LazyHolder.INSTANCE;
  }
  
  private static class LazyHolder {
    private static final Java_Singleton INSTANCE = new Java_Singleton();
  }
  
}
```

* ex. JPA 의 엔티티 LazyHolding 기능 

```java 
@OneToMany(fetch = FetchType.LAZY)
```

