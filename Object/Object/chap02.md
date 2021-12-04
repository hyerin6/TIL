
<br />        

# chap02 객체지향 프로그래밍  
진정한 객체지향 패러다임으로의 전환은 클래스가 아닌 객체에 초점을 맞출 때에만 얻을 수 있다.       
이를 위해 다음 두 가지에 집중해야 한다.       

1️⃣ &nbsp; 어떤 클래스가 필요한지를 고민하기 전에 어떤 객체들이 필요한지 고민하라.      
어떤 상태와 행동을 가지는지를 먼저 결정해야 한다.     

2️⃣ &nbsp; 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 일원으로 봐야 한다.     
객체를 협력하는 공동체의 일원으로 바라보는 것은 설계를 유연하고 확장 가능하게 만든다.   
고립된 존재가 아닌 협력에 참여하는 참여자로 바라봐야 한다.   

<br />
<br />   

### 도메인의 구조를 따르는 프로그램 구조   
**도메인(domain)** 이라는 용어를 살펴보자.       
소프트웨어는 사용자가 원하는 어떤 문제를 해결하기 위해 만들어진다.       
**사용자가 프로그램을 사용하는 분야를 도메인이라고 부른다.**  

객체지향 패러다임이 강력한 이유는    
요구 사항 분석 초기 ~ 구현 마지막까지   
객체라는 동일한 추상화 기법을 사용할 수 있기 때문이다.   

<br />
<br />   

### 클래스 구현  
클래스의 내부와 외부를 구분해야 하는 이유는 무엇일까?    
그 이유는 경계의 명확성이 객체의 자율성을 보장하기 때문이다.   
그리고 프로그래머에게 구현의 자유를 제공한다.   

<br />

#### 자율적인 객체   
1️⃣ &nbsp; 객체는 상태와 행동을 함께 가지는 복잡적인 존재이다.        
2️⃣ &nbsp; 객체는 스스로 판단하고 행동하는 자율적인 존재이다.      

데이터와 기능을 객체 내부로 함께 묶는 것을 캡슐화라고 한다.    

객체지향 프로그래밍은 상태와 행동을 캡슐화하는 것에서 더 나아가   
(외부에서의) 접근 제어, 접근 수정자를 제공한다.   

캡슐화와 접근 제어는 객체를 두 부분으로 나눈다.     
* 외부 접근이 가능한 부분: `public interface`     
* 오직 내부에서만 접근 가능한 부분: 구현 (`implementation`)     
 
인터페이스와 구현의 분리 원칙은 훌륭한 객체지향 프로그램을 만들기 위해 따라야 하는 핵심이다.   

<br />   
<br />   

### 협력하는 객체들의 공동체
[chapter02/step01](https://github.com/eternity-oop/object/tree/master/chapter02/src/main/java/org/eternity/movie/step01)  

객체지향의 장점은 객체를 이용해 도메인의 의미를 풍부하게 표현할 수 있다는 것이다.        
의미를 좀 더 명시적으로 표현할 수 있다면 객체를 사용해 해당 개념을 구현해야 한다.        
그 개념이 하나의 인스턴스 변수만 포함하더라도 개념을 명시적으로 표현하는 것은 전체적인 설계의 명확성과 유연성을 높이는 첫걸음이다.       

<br />

영화를 예매하기 위해 Screening, Movie, Reservation 인스턴스들은 서로의 메서드를 호출하며 상호작용한다.   
시스템의 어떤 기능을 구현하기 위해 객체들 사이에 이뤄지는 상호작용을 협력이라고 부른다.   

<br />
<br />

### 협력   
메시지와 메서드를 구분하는 것은 매우 중요하다.  
메시지와 메서드의 구분에서부터 다형성(polymorphism)의 개념이 출발한다.     

<br />
<br />


### 할인 요금 계산을 위한 협력  

```java
public class Movie {
    private String title; // 제목 
    private Duration runningTime; // 상영 시간
    private Money fee; // 기본 요금
    private DiscountPolicy discountPolicy; // 할인 정책 

    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPolicy = discountPolicy;
    }

    public Money getFee() {
        return fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }
}
```



calculateMovieFee 메서드는 한 가지 이상한 점이 있다.   
어떤 할인 정책을 사용할 것인지 결정하는 코드가 없다.   
단지 discountPolicy에게 메시지를 전송할 뿐이다.   

이 코드에 객체지향에서 중요한 개념 상속(inheritance), 다형성이 숨겨져 있고   
그 기반에는 추상화(abstraction)라는 원리가 숨겨져 있다.     

<br />
<br />

### 할인 정책 & 할인 조건  
영화 예매 시스템에서 할인 정책은 두 가지로 요구되고 있다.   
할인 정책이 하나가 아니기 때문에 중복 코드를 제거하기 위해 공통 코드를 보관할 장소가 필요하다.   

부모 클래스인 DiscountPolicy 안에 중복 코드를 두고   
AmountDiscountPolicy와 PercentDiscountPlicy가 이 클래스를 상속 받는다.   

실제 애플리케이션에서는 DiscountPolicy의 인스턴스를 생성할 필요가 없기 때문에 추상 클래스로 구현한다.   


```java
public abstract class DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DiscountPolicy(DiscountCondition ... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public Money calculateDiscountAmount(Screening screening) {
        for(DiscountCondition each : conditions) {
            if (each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }

        return Money.ZERO;
    }

    abstract protected Money getDiscountAmount(Screening Screening);
}
```

이처럼 부모 클래스에 기본적인 알고리즘 흐름을 구현하고 중간에 필요한 처리를    
자식 클래스에게 위임하는 디자인 패턴을 **템플릿 메소드 패턴**이라고 한다.        

DiscountCondition은 인터페이스를 이용해 선언했다.     

<br />

<img width="650" src="https://user-images.githubusercontent.com/33855307/144695586-f044c437-0573-45e2-a736-8488c289b834.jpeg">  

<br />
<br />

### 컴파일 시간 의존성과 실행 시간 의존성   
Movie 클래스에는 할인 정책이 금액 할인 정책인지 비율 할인 정책인지 판단하지 않는다.   
그런데 어떻게 영화 요금을 계산할까?   
→ 상속과 다향성    

<br />

위 할인 정책과 할인 조건을 나타낸 다이어그램을 보면     
어떤 클래스가 다른 클래스에 접근할 수 있는 경로를 가지거나   
해당 클래스의 객체의 메서드를 호출한다.     
이 경우 두 클래스 사이에 의존성이 존재한다고 말한다.      


Movie는 DiscountPolicy 클래스와 연결돼 있다.   
영화 요금을 계산하기 위해서는 추상 클래스인 DiscountPolicy가 아니라   
AmountDiscountPolicy나 PercentDiscountPolicy의 인스턴스에 의존해야 한다.   
그러나 **코드 수준에서 두 클래스에 의존하지 않고 오직 추상 클래스에만 의존**한다.   

Movie 인스턴스 생성 시, 인자로 DiscountPolicy가 아닌 이 클래스를 상속받은 자식 클래스 중 하나를 전달한다.   
이렇게 구현하면 **실행 시점에서 의존**하게 된다.  

코드의 의존성과 실행 시점의 의존성이 서로 다를 수 있다는 것이다.  
클래스 사이의 의존성과 객체 사이의 의존성은 동일하지 않을 수 있다.   
유연하고, 쉽게 재사용할 수 있으며 확장 가능한 객체지향 설계가 가지는 특징은   
코드의 의존성과 실행 시점의 의존성이 다르다는 것이다.   

<br />
<br />

### 상속과 인터페이스   
상속이 가치 있는 이유는 부모 클래스가 제공하는 모든 인터페이스를 자식 클래스가 물려받을 수 있기 때문이다.        

인터페이스는 객체가 이해할 수 있는 메시지의 목록을 정의한 것이다.    
결과적으로 자식 클래스는 부모 클래스가 수신할 수 있는 모든 메시지를 수신할 수 있기 때문에    
외부 객체는 자식 클래스를 부모 클래스와 동일한 타입으로 간주할 수 있다.   

정리하면 자식 클래스는 부모 클래스 대신 사용될 수 있고   
컴파일러는 코드 상에서 부모 클래스가 나오는 모든 장소에서 자식 클래스를 사용하는 것을 허용한다.   
이를 업캐스팅이라고 부른다.   

<br />
<br />

### 다형성   
코드 상에서 DiscountPolicy 클래스에게 메시지를 전송하지만   
실행 시점에 실제로 실행되는 메서드는 Movie와 협력하는 객체의 실제 클래스가 무엇인지에 따라 달라진다.   
Movie는 동일한 메시지를 전송하지만 실제로 어떤 메서드가 실행될 것인지는 메시지를 수신하는 객체의 클래스가 무엇이냐에 따라 달라진다.   
이를 다형성이라고 한다.   

다형성은 객체지향 프로그램의 컴파일 시간 의존성과 실행 시간 의존성이 다를 수 있다는 사실을 기반으로 한다.   
또한 동일한 메시지를 수신했을 때 객체의 타입에 따라 다르게 응답할 수 있는 능력을 의미한다.   
따라서 다형적인 협력에 참여하는 객체들은 모두 같은 메서드를 이해할 수 있어야 한다.   
인터페이스가 동일해야 한다는 것이다. 그리고 이 두 클래스의 인터페이스를 통일하기 위해 사용한 구현 방법이 상속인 것이다.   

메시지와 메서드를 실행 시점에 바인딩하는 것을 지연 바인딩 또는 동적 바인딩이라고 부른다.   
객체지향이 컴파일 시점의 의존성과 실행 시점의 의존성을 분리하고,    
하나의 메시지를 선택적으로 서로 다른 메서드에 연결할 수 있는 이유가 바로 지연 바인딩을 사용하기 때문이다.   

<br />
<br />

### 추상화   
프로그래밍 언어 측면에서 DiscountPolicy와 DiscountCondition이 더 추상적인 이유는 인터페이스에 초점을 맞추기 때문이다.      
구현의 일부 또는 전체를 자식 클래스가 결정할 수 있도록 결정권을 위임한다.   

추상화는 두 가지 장점을 가지고 있다.         
1️⃣ &nbsp; 추상화의 계층만 따로 떼어 놓고 살펴보면 요구상항의 정책을 높은 수준에서 서술할 수 있다는 것이다.      
2️⃣ &nbsp; 추상화를 이용하면 설계가 좀 더 유연해진다.   

<br />

추상화를 사용하면 세부적인 내용을 무시한 채 상위 정책을 쉽고 간단하게 표현할 수 있다.     

추상화를 이용해 상위 정책을 기술한다는 것은 기본적인 애플리케이션의 협력 흐름을 기술한다는 것을 의미한다.     
영화표를 계산하기 위한 흐름은 항상 Movie에서 DiscountPolicy로 향해 흐른다.   
추상화를 이용해서 정의한 상위의 협력 흐름을 그대로 따르게 된다.   

<br />
<br />

### 유연한 설계   
할인 요금을 계산할 필요 없이 영화에 설정된 기본 금액을 그대로 사용하는 경우에 어떻게 될까?    

```java
public Money calculateMovieFee(Screening screening) {
    if(discountPolicy == null) {
        return fee;
    }
    return fee.minus(discountPolicy.calculateDiscountAmount(screening));
}
```

위 방식의 문제점은 할인 정책이 없는 경우를 예외 케이스로 취급하기 때문에   
일관성 있던 협력 방식이 무너지게 된다는 것이다.   
할인 금액이 0원이라는 사실을 결정하는 책임이 DiscountPolicy가 아닌 Movie 쪽에 있기 때문이다.   
책임의 위치를 결정하기 위해 조건문을 사용하는 것은 협력의 설계 측면에서 나쁜 선택이다.   

이 경우 일관성을 지키는 방법은 0원이라는 할인 요금 계산 책임을 DiscountPolicy 계층에 유지시키는 것이다.     
DiscountPolicy를 상속받는 NoneDiscountPolicy라는 클래스를 추가하자.   
이 클래스를 추가하는 것만으로 애플리케이션의 기능이 확장되었다.   

<br />
<br />

### 추상 클래스와 인터페이스 트레이드오프   
NoneDiscountPolicy 클래스의 `getDiscountAmount()` 메서드가 어떤 값을 반환하더라도 상관이 없다.   
할인 조건이 없을 경우 호출하지 않기 때문이다.   
NoneDiscountPolicy의 개발자는 `getDiscountAmount()`가 호출되지 않을 경우 0원을 반환할 것이라는 사실을 가정하고 있기 때문이다.   

이 문제를 해결하는 방법은 DiscountPolicy를 인터페이스로 바꾸고   
NoneDiscountPolicy가 `calculatieDiscountAmount()` 오퍼레이션을 오버라이딩하도록 변경하는 것이다.    


<img width="650" src="https://user-images.githubusercontent.com/33855307/144699877-8a1668c9-2362-413f-a476-b1c547a4af2c.jpeg">

<br />
<br />

### 코드 재사용   
상속은 코드를 재사용하기 위해 널리 사용되는 방법이다. 그러나 가장 좋은 방법은 아니다.    
코드 재사용을 위해 상속보다 합성(composition)이 더 좋은 방법이라는 이야기를 많이 들었을 것이다.   
합성은 다른 객체의 인스턴스를 자신의 인스턴스 변수로 포함해서 재사용하는 방법을 말한다.   

Movie가 DiscountPolicy의 코드를 재사용하는 방법이 바로 합성이다.   

<br />
<br />

### 상속   
상속은 두 가지 관점에서 설계에 안 좋은 영향을 미친다.  

1️⃣ &nbsp; 상속은 캡슐화를 위반한다.     
2️⃣ &nbsp; 설계를 유연하지 못하게 만든다.     

상속을 이용하기 이해서는 부모 클래스의 내부 구조를 잘 알고 있어야 한다.   
결과적으로 부모 클래스의 구현이 자식 클래스에게 노출되기 때문에 캡슐화가 약화된다.   
캡슐화의 약화로 자식 클래스가 부모 클래스에 강하게 결합되도록 하기 때문에   
부모 클래스를 변경할 때 자식 클래스도 함께 변경될 확률을 높인다.   

상속은 부모 클래스와 자식 클래스 사이의 관계를 컴파일 시점에 결정한다.   
따라서 실행 시점에 객체의 종류를 변경하는 것이 불가능하다.   

<br />
<br />

### 합성   
Movie는 요금을 계산하기 위해 DiscountPolicy의 코드를 재사용한다.   
Movie가 DiscountPolicy의 인터페이스를 통해 약하게 결합된다는 것이다.   
Movie는 DiscountPolicy가 제공하는 메서드만 알고 내부 구현에 대해서는 전혀 알지 못한다.   
이처럼 인터페이스에 정의된 메시지를 통해서만 코드를 재사용하는 방법을 합성이라고 부른다.

합성은 방속이 가지는 두 가지 문제점을 모두 해결한다.   
인터페이스에 정의된 메시지를 통해서만 재사용이 가능하기 때문에 구현을 효과적으로 캡슐화할 수 있다.   
또한 의존하는 인스턴스를 교체하는 것이 비교적 쉽기 때문에 설계를 유연하게 만든다.   

합성은 메시지를 통해 느슨하게 결합된다.         
이처럼 코드를 재사용하는 경우에는 상속보다 합성을 선호하는 것이 옳지만 다형성을 위해      
인터페이스를 재사용하는 경우에는 상속과 합성을 함께 조합해서 사용할 수밖에 없다.        

<br />  
