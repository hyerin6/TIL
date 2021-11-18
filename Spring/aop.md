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
Spring AOP는 프록시 기반의 AOP 구현체를 사용하고 있다.

또한 Spring Bean에만 AOP를 적용할 수 있다.

<br />

모든 AOP 기능을 제공하는 것이 아닌 Spring IoC와 연동하여 

엔터프라이즈 애플리케이션에서 가장 흔한 문제에 대한 해결책을 제공하는 것이 목적

<br />

<img width="400px" src="https://user-images.githubusercontent.com/33855307/142434786-7039f259-d498-4790-8fba-d6041ffbdb82.png">

<br />

