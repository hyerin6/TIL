<br />


* Spring 정의: <https://hyerin6.github.io/2021-09-24/spring/>
* Spring Transaction으로 알아보는 AOP: <https://hyerin6.github.io/2021-10-06/aop/>
* Ioc/DI: <https://hyerin6.github.io/2020-01-31/spring-DI-IoC/>
* AOP: <https://hyerin6.github.io/2020-02-14/spring-AOP/>

<br />
<br />

### AOP란?
Aspect-oriented Programming (AOP, 관점지향 프로그래밍)는 OOP를 보완하는 수단으로, 

흩어진 Aspect를 모듈화 할 수 있는 프로그래밍 기법이다.

<br />
<br />

### AOP 구현체 
Java에는 일반적으로 AspectJ가 있다. 

Spring에서는 Spring AOP를 제공하는데 AspectJ의 일부 기능만을 제공하고 있다. 

<br />
<br />

### AOP 적용 방법 1: 컴파일

`hello()`라는 메서드의 Aspect가 있다고 가정하자

Class A의 `a()` 메서드가 실행되기 이전 Aspect가 실행되어야 한다면 

자바 코드가 바이트 코드로 변환될 때 조작하여 `hello()`가 같이 바이트 코드에 들어가게 된다.

이렇게 컴파일 타임에 AOP를 구현하는 것이 가능하다.

<br />

**장점**

* 바이트 코드를 만들때 이미 AOP를 구현했기 때문에 로드 타임이나 런타임 보다는 성능적 부하가 없다.
* AspectJ를 사용할 수 있기 때문에 다양한 기능 사용 가능 




**단점**

* 별도의 컴파일 과정을 거쳐야한다. 


<br />
<br />

### AOP 적용 방법 2: 로드 타임

Class A를 컴파일 후, A 클래스를 로딩하는 시점에 코드를 바꿔치기 할 수 있다.

이러한 기술을 Load Time에 Weaving 한다고 표현한다.

Weaving은 Aspect를 끼워서 넣는 기술을 의미한다.

<br />

**장점**
* AspectJ를 사용할 수 있기 때문에 다양한 기능 사용 가능



**단점**
* 클래스 로딩할때 약간의 부하가 생길 수 있다.
* Load Time Weaver를 따로 설정해주어야한다.




<br />
<br />

### AOP 적용 방법 3: 런타임 
Spring AOP는 이 방법을 사용한다.

A라는 빈에 Aspect를 적용해야하는 것을 스프링은 알고 있다.

Class A 타입의 빈을 만들 때 A라는 빈을 감싼 Proxy Bean을 만든다.


이 Proxy Bean이 이 `a()`라는 메소드를 찍기 이전에 `hello()` 메서드를 수행하고 `a()` 메소드를 호출한다.


<br />

**장점**
* 아무런 설정이 필요없다. 



**단점**
* 최초의 빈을 만드는데 성능 부하가 있을 수 있다.


<br />
<br />


### 스프링 AOP 특징 
* Spring AOP는 프록시 기반의 AOP 구현체를 사용하고 있다.   

* Spring Bean에만 AOP를 적용할 수 있다.  

* 모든 AOP 기능을 제공하는 것이 아닌 Spring IoC와 연동하여       
엔터프라이즈 애플리케이션에서 가장 흔한 문제에 대한 해결책을 제공하는 것이 목적이다.              

<br />

<img width="400px" src="https://user-images.githubusercontent.com/33855307/142434786-7039f259-d498-4790-8fba-d6041ffbdb82.png">

<br />

Proxy 패턴은 인터페이스가 있고 클라이언트는 인터페이스 타입으로 프록시 객체를 사용하게 된다. 

Proxy 객체는 target 객체를 참조하고 있다. 

실제 수행 로직은 Real Subject에 있고 Real Subject 객체를 Proxy가 감싸서 실제 클라이언트의 요청을 처리하게 된다.

<br />

* Subject 

```java
public interface EventService {
  void createEvent();
  void publishEvent(); 
  viid deleteEvent();
}
```

<br />  


* Real Subject

```java
@Service
public class SimpleEventService implements EventService {
  
  @Override
  public void createEvent() {
    long begin = System.currentTimeMillis();
    
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Created an event");
    
    System.out.println(System.currentTimeMillis() - begin);
  }
  
  @Override
  public void publicEvent() {
    long begin = System.currentTimeMillis();
    
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Published an event");
    
    System.out.println(System.currentTimeMillis() - begin);
  }
  
  @Override
  public void deleteEvent() {
    System.out.println("Delete an event");
  }
}
```

<br />  

* Client

```java
public class AppRunner implements ApplicationRunner {
  
  @Autowired
  EventService eventService;
  // Interface가 있는 경우 Interface type으로 주입 받는 것이 가장 좋다.
  
  @Override
  public void run(ApplicationArguments args) throws Exception {
    eventService.createEvent();
    eventService.publicEvent();
    eventService.deleteEvent();
  }
}
```


SimpleEventService(Real Subject)의 createEvent와 publicEvent method에 crosscutting concern가 있다.  

기존의 코드를 건드리지 않고 실행 시간을 측정하기 위해 Proxy 패턴을 사용해서 문제를 해결할 수 있다.  

<br />

```java
// Client가 EventService를 Autowired할 때 ProxySimpleEventService를 가져다 쓴다.

@Primary
@Service
public class ProxySimpleEventService implements EventService {
  
  @Autowired
  SimpleEventService simpleEventService;
  
  @Override
  public void createEvent() {
    long begin = System.currentTimeMillis();
    simpleEventService.createEvent();
    System.out.println(System.currentTimeMillis() - begin);
  }
  
  @Override
  public void publishEvent() {
    long begin = System.currentTimeMillis();
    simpleEventService.createEvent();
    System.out.println(System.currentTimeMillis() - begin);
  }
  
  @Override
  public void deleteEvent() {
    simpleEventService.deleteEvent();
  }
}
```

이와 같이 코드를 작성할 때 중복된 코드가 여전히 남아있고 다른 클래스에서도 같은 기능이 필요하다면 모든 클래스에 중복된 코드를 심어야 한다.    

객체 관계 또한 복잡해진다.  


<br />



이러한 문제 때문에 Spring AOP가 등장했다. 

동적으로(Runtime 중에) 어떠한 객체를 감싸는 Proxy 객체(Dynamic Porxy)를 만들어 사용한다. 

이 방법을 기반으로 Spring IoC Container가 제공하는 방법과 혼합해서 이 문제를 간단하게 해결 할 수 있다. 

<br />

Spring IoC가 기존 빈을 대체하는 Dynamic Proxy Bean을 만들어 등록 시켜준다.

구체적으로 `SimpleEventService`가 Bean으로 등록되면 `BeanPostProcessor Interface`를 구현한 `AbstarctAutoProxyCreator`로 

`SimpleEventService`을 감싸는 Proxy Bean을 만들어 이 Bean을 `SimpleEventService` Bean 대신에 등록한다.


<br />

