## Spring DI Pattern

DI는 Spring IoC Container가 관리하는 빈들을 필요한 시점에 의존성 주입 시 사용하는 기술의 이름이다. 

DI를 하는 가장 큰 목적은 객체 간의 결합도를 낮추기 위함이다. 

Spring IoC Container가 `@Bean`으로 등록된 객체들을 싱글톤으로 관리하고 있다가 

런타임시 의존성을 주입함으로써 객체 간의 유연한 구조를 만드는 것이 가능하다. 

<br />

SOLID 원칙에 해당하는 Open Closed Principle을 지키기 위해 

디자인 패턴 중 하나인 Strategy Pattern을 사용한다. 

전략 패턴은 DI의 3가지 방법 중 생성자 주입을 사용할 시 사용하게 되는데 

인터페이스 객체를 만들고 생성자의 매개변수로 구체적인 구현체를 외부에서 주입하는 방식을 사용한다. 

<br /> 

```java
@Service
public class OrderService {
 
  private DiscountPolicy discountPolicy;
 
  public void setDiscountPolicy(DiscountPolicy discountPolicy) {
  	this.discountPolicy = discountPolicy;
  }
  
}
```

위 코드를 보면 외부에서 의존성 주입을 하고 있고 생성자를 통해 외부로부터 받아온 의존성을 주입하고 있다. 

왜 이 방법을 사용해야만 할까? 다른 방법을 알아보며 이해하자. 

<br />
<br />

## Setter Injection
수정자 주입 방식의 의존관계 주입은 런타임시에 할 수 있기 때문에 낮은 결합도를 갖는다.

그러나 구현체를 주입하지 않았는데 해당 객체의 메소드를 호출해버리면?

NullPointException이 발생한다. 

수정자 주입 방식은 구현체 주입 여부와 관계없이 메소드 호출을 허용하기 때문에 위험하다. 

<br />
<br />

## Fild Injection
`@Autowired` 사용은 가장 나쁜 DI Pattern이다. 

이유는 다음과 같다. 

<br />

#### (1) 테스트가 어렵다.  

➡️ 단위 테스트시 의존 관계를 가지는 객체를 생성해서 주입할 수 있는 방법이 없다.

Mock 객체를 만들어야 하는데 DI 컨테이너에 의존하기 때문에 인스턴스화가 불가능하다. 

<br />

#### (2) 순환참조가 발생하기 쉽다.
 
➡️ 서로를 호출하다 StackOverflow를 발생시키고 죽는다. 

Fild Injection의 치명적인 문제는 필드 주입이나 수정자 주입의 경우 

객체 생성 시점에서 순환 참조가 일어나는지 확인할 수 있는 방법이 없다는 점이다.

<br />
<br />


## 생성자 주입 방식 정리 

* 외부에서 인스턴스 주입이 가능하기 때문에 Mock 객체를 만들 수 있다. DI 컨테이너와 독립성을 갖게 된다.      
* 순환 의존성을 가질 경우 BeanCurrentlyCreationExcepiton를 발생시켜 미리 알 수 있다.    
* 생성자 주입 방식에서 필드는 final로 선언 가능하다. 불변 객체           
* 생성자 주입 방식은 의존관계 설정이 되지 않으면 객체생성이 불가능하다. NPE 발생 방지       

<br />
<br />

## Reference
* [용어 정리 참고](https://hyerin6.github.io/2020-01-31/spring-DI-IoC/)
* [전략 패턴 참고](https://hyerin6.github.io/2020-06-13/template/)
* [Spring IoC/DI 란?](https://hyerin6.github.io/2021-10-06/ioc-di/)

<br />
