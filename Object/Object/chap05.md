<br />

# chap05 책임 할당하기            

책임에 초점을 맞춰 설계할 때 가장 큰 어려움은 어떤 객체에게 어떤 책임을 할당할지 결정하는 것이다.    
올바른 책임 할당을 위해 다양한 관점에서 설계를 평가할 수 있어야 한다.   

<br />

# 01 책임 주도 설계   
데이터 중심의 설계에서 책임 중심의 설계로 전환하기 위해서 다음 두 가지 원칙을 따라야 한다.     

* 데이터보다 행동을 먼저 결정하라 
* 협력이라는 문맥 안에서 책임을 결정하라  

<br />

### 데이터보다 행동을 먼저 결정하라 
클라이언트의 관점에서 객체가 수행하는 행동이란 곧 객체의 책임을 의미한다.   
객체는 협력에 참여하기 위해 존재하며 협력 안에서 수행하는 책임이 객체의 존재가치를 증명한다.   

객체의 데이터에서 행동으로 무게 중심을 옮기기 위한 가장 기본적인 해결 방법은 설계 질문의 순서를 바꾸는 것이다.        
"이 객체가 수행해야 하는 책임은 무엇인가"를 결정한 이후에 "책임을 수행하는 데 필요한 데이터가 무엇인가"를 결정한다.   
즉 책임을 먼저 결정한 후 객체의 상태를 결정하는 것이다.   

<br />

### 협력이라는 문맥 안에서 책임을 결정하라  
객체에게 할당된 책임이 협력에 어울리지 않는다면 그 책임은 나쁜 것이다.   
객체의 입장에서 어색해 보이더라도 협력에 적합하면 좋은 책임이다.   
**책임은 객체의 입장이 아니라 객체가 참여하는 협력에 적합해야 한다.**  

협력을 시작하는 주체는 메시지 전송자이기 때문에 협력에 적합한 책임이란 메시지 전송자에게 적합한 책임을 의미한다.          
메시지를 전송하는 클라이언트의 의도에 적합한 책임을 할당해야 한다는 것이다.      
메시지가 클라이언트의 의도를 표현한다는 사실에 주목하자.   

메시지를 먼저 결정하기 때문에 메시지 송신자는 메시지 수신자에 대한 어떠한 가정도 할 수 없다.   
❗️ 메시지 전송자의 관점에서 메시지 수신자가 깔끔하게 캡슐화되는 것이다. 

<br />

### 책임 주도 설계   

다음은 책임 주도 설계의 흐름이다.  

```
- 시스템이 사용자에게 제공해야 하는 기능인 시스템 책임을 파악한다. 
- 시스템 책임을 더 작은 책임으로 분할한다. 
- 분할된 책임을 수행할 수 있는 적절한 객체 또는 역할을 찾아 책임을 할당한다. 
- 객체가 책임을 수행하는 도중 다른 객체의 도움이 필요한 경우 이를 책임질 적절한 객체 또는 역할을 찾는다. 
- 해당 객체 또는 역할에게 책임을 할당함으로써 두 객체가 협력하게 한다. 
```

<br />
<br />

# 02 책임 할당을 위한 GRASP 패턴   
많은 책임 할당 기법 중 가장 널리 알려진 것은 GRASP 패턴이다.             
GRASP은 `General Responsibility Assignment Software Pattern` 의 약자로         
객체에게 책임을 할당할 때 지침으로 삼을 수 있는 원칙들의 집합을 패턴 형식으로 정리한 것이다.         

영화 예매 시스템을 책임 중심으로 설계하는 과정을 계속 따라가보자.  

<br />

### 도메인 개념에서 출발하기   
도메인 개념을 책임 할당의 대상으로 사용하면 코드에 도메인 모습을 투영하기 좀 더 수월해진다.    
설계 시작 단계에서 개념들의 의미와 관계가 정확하거나 완벽할 필요가 없다.   

<img width="600" src="https://user-images.githubusercontent.com/33855307/145413637-f83c525a-fa12-426d-80be-059c96880065.jpeg">

<br />

### 정보 전문가에게 책임을 할당하라   
책임 주도 설계 방식의 첫 단계는 애플리케이션이 제공해야 하는 기능을 애플리케이션의 책임으로 생각하는 것이다.   

영화 예매 시스템에서는 영화를 예매할 책임이 있다고 말할 수 있다.     
메시지는 메시지를 수신할 객체가 아니라 **메시지를 전송할 객체의 의도를 반영해서 결정해야 한다.**     

<br />  

#### 첫 번째 질문: 메시지를 전송할 객체는 무엇을 원하는가?        
예) 영화를 예매하는 것         


#### 두 번째 질문: 메시지를 수신할 적잡한 객체는 누구인가?    
이 질문에 답하기 위해서 객체가 상태와 행동을 통합한 캡슐화의 단위라는 사실에 집중해야 한다.     
객체는 자신의 상태를 스스로 처리하는 자율적인 존재여야 한다.   
객체의 책임과 책임을 수행하는 데 필요한 상태는 동일한 객체 안에 존재해야 한다.    
따라서 첫 번째 원칙은 책임을 수행할 정보를 알고 있는 객체에게 책임을 할당하는 것이다.  
이를 GRASP에서는 information expert(정보 전문가) 패턴이라고 부른다.   

information expert 패턴은 객체가 자신이 소유하고 있는 정보와 관련된 작업을 수행한다는 일반적인 직관을 표현한 것이다.     
❗️ 그러나 정보가 데이터는 아니다. 정보를 `알고`있다고 해서 그 정보를 `저장`하고 있을 필요는 없다.           

<br />

### 높은 응집도와 낮은 결합도   
예) 할인 요금 계산을 위해 Movie가 DiscountCondition에 `할인 여부를 판단해라` 메시지를 전송한다.   
이 설계의 대안으로 Movie 대신 Screening이 직접 DiscountCondition과 협력하게 하는 것은 어떨까?   
이를 위해서는 Screening이 DiscountCondition에게 `할인 여부를 판단해라` 메시지를 전송하고 반환받은 할인 여부를    
Movie에 전송하는 메시지의 인자로 전달하도록 수정해야 한다.   
Movie는 전달된 할인 여부 값을 이용해 요금을 어떻게 계산할지 결정할 것이다.   

위 설계는 기능적인 측면에서만 보면 Movie와 DiscountCondition이 직접 상호작용하는 앞의 설계와 동일하다.   
차이점은 DiscountCondition과 협력하는 객체가 Movie가 아니라 Screening이라는 것이다.   
왜 위 설계 대신 Movie와 DiscountCondition과 협력하는 방법을 선택했을까?  

이유는 응집도와 결합도에 있다.   
두 협력 패턴 중에서 높은 응집도와 낮은 결합도로 얻을 수 있는 설계가 있다면 그 설계를 선택해야 한다.   
GRASP에서 이를 LOW COUPLING(낮은 결합도) 패턴과 HIGH COHESION(높은 응집도) 패턴이라고 부른다.    


Screening의 가장 중요한 책임은 예매를 생성하는 것이다.       
Screening이 DiscountCondition과 협력하면 Screening은 영화 요금 계산과 관련된 책임 일부를 떠안아야 한다.        
이 경우 Screening이 DiscountCondition이 할인 여부 판단, Movie가 할인 여부를 필요로 한다는 사실을 알고 있어야 한다.        
결과적으로 서로 다른 이유로 변경되는 책임을 짊어지게 되므로 응집도가 낮아진다.        


<br />

### 창조자에게 객체 생성 책임을 할당하라   
영화 예매 협력의 최종 결과물은 Reservation 인스턴스를 생성하는 것이다.   
GRASP의 CREATOR(창조자) 패턴은 이 같은 경우에 사용할 수 있는 책임 할당 패턴으로서 객체를 생성할 책임을 어떤 객체에게 할당할지에 대한 지침을 제공한다.   

```
객체 A를 생성해야 할 때 아래 조건을 최대한 많이 만족하는 B에게 객체 생성 책임을 할당하라. 

- B가 A 객체를 포함하거나 참조한다. 
- B가 A 객체를 기록한다.   
- B가 A 객체를 긴밀하게 사용한다. 
- B가 A 객체를 초기화하는 데 필요한 데이터를 가지고 있다. 

이미 결합돼 있는 객체에게 생성을 할당하는 것은 설계의 전체적인 결합도에 영향을 미치지 않아 
낮은 결합도를 유지할 수 있게 한다. 
```

<br />
<br />

# 03 구현을 통한 검증 
협력의 관점에서 Screening은 `예매하라` 메시지에 응답할 수 있어야 한다.   

```java
public class Screening {
    public Reservation reserve(Customer customer, int audienceCount) {
    }
}
```

책임을 결정했기 때문에 책임을 수행하는 데 필요한 인스턴스 변수를 결정해야 한다.    
Screening은 상영 시간, 상영 순번을 인스턴스 변수로 포함한다.   
또한 Movie에 `가격을 계산하라` 메시지를 전송해야 하기 때문에 영화에 대한 참조도 포함해야 한다.   

```java
public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public LocalDateTime getWhenScreened() {
        return whenScreened;
    }

    public int getSequence() {
        return sequence;
    }
}
```

Screening을 구현하는 과정에서 Movie에 전송하는 메시지의 시그니처를 `movie.calculateMovieFee(Screening screening)`로 선언했음에 주목하자.   
이 메시지는 수신자인 Movie가 아니라 송신자인 Screening의 의도를 표현한다.    
Screening이 Movie의 내부 구현에 대한 어떤 지식도 없이 전송할 메시지를 결정했다는 것이다.   
이처럼 Movie의 구현을 고려하지 않고 필요한 메시지를 결정하면 Movie의 내부 구현을 깔끔하게 캡슐화할 수 있다.    

<br />

```java
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

    public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }

        return fee;
    }

    private boolean isDiscountable(Screening screening) {
        return discountConditions.stream()
                .anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    private Money calculateDiscountAmount() {
        switch(movieType) {
            case AMOUNT_DISCOUNT:
                return calculateAmountDiscountAmount();
            case PERCENT_DISCOUNT:
                return calculatePercentDiscountAmount();
            case NONE_DISCOUNT:
                return calculateNoneDiscountAmount();
        }

        throw new IllegalStateException();
    }

    private Money calculateAmountDiscountAmount() {
        return discountAmount;
    }

    private Money calculatePercentDiscountAmount() {
        return fee.times(discountPercent);
    }

    private Money calculateNoneDiscountAmount() {
        return Money.ZERO;
    }
}
```

Movie는 각 DiscountCondition에 `할인 여부를 판단하라` 메시지를 전송한다.    
DiscountCondition은 이 메시지를 처리하기 위한 `isSatisfiedBy` 메서드를 구현해야 한다.   

```java
public class DiscountCondition {
    private DiscountConditionType type;
    private int sequence;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public boolean isSatisfiedBy(Screening screening) {
        if (type == DiscountConditionType.PERIOD) {
            return isSatisfiedByPeriod(screening);
        }

        return isSatisfiedBySequence(screening);
    }
    
    . . .

}
```


[전체 코드 보러가기](https://github.com/eternity-oop/object/tree/master/chapter05/src/main/java/org/eternity/movie/step01)   

위 코드 안에는 몇 가지 문제점이 있다.   

<br />

### DiscountCondition 개선  
가장 큰 문제점은 변경에 취약한 클래스를 포함하고 있다는 것이다.    
변경의 이유가 다양한 클래스는 DiscountCondition이다.   
다음과 같은 이유로 변경될 수 있다.  

* 새로운 할인 조건 추가 
* 순번 조건을 판단하는 로직 변경 
* 기간 조건을 판단하는 로직이 변경되는 경우

낮은 응집도가 초래하는 문제를 해결하기 위해서는 **변경의 이유에 따라 클래스를 분리해야 한다.**  

일반적으로 설계를 개선하는 작업은 변경의 이유가 하나 이상인 클래스를 찾는 것으로부터 시작하는 것이 좋다.    

코드를 통해 변경의 이유를 파악할 수 있는 방법은 다음과 같다.   

* 인스턴스 변수가 초기화되는 시점   
* 메서드들이 인스턴스 변수를 사용하는 방식 
    - 모든 메서드가 객체의 모든 속성을 사용한다면 클래스의 응집도가 높다고 볼 수 있다. 
    - 메서드들이 사용하는 속성에 따라 그룹이 나뉜다면 클래스의 응집도가 낮다고 볼 수 있다. 

<br />

