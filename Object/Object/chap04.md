
<br />

# chap04 설계 품질과 트레이드오프     

객체지향 설계란 올바른 객체에게 올바른 책임을 할당하면서 낮은 결합도와 높은 응집도를 가진 구조를 창조하는 활동이다.     

객체지향 설계 두 가지 관점    
(1) 객체지향 설계의 핵심이 책임이라는 것    
(2) 책임을 할당하는 작업이 응집도와 결합도 같은 설계 품질과 깊이 연관돼 있다는 것       

<br />

객체를 단순히 데이터의 집합으로 바라보는 시각   
→ 객체의 내부 구현을 public interface에 노출시키는 결과 초래   
→ 변경에 취약해진다.   
→ 해결방법: 객체의 책임에 초점을 맞춘다.   
→ 책임은 객체의 상태에서 행동으로, 나아가 객체와 객체 사이 상호작용으로 설계 중심을 이동시킨다.   
→ 결합도가 낮고 응집도가 높으며 구현을 효과적으로 캡슐화하는 객체들을 창조할 수 있는 기반을 제공한다.   


<br />

나쁜 설계와 좋은 설계의 차이를 알아보기 위해   
데이터 중심의 설계와 객체지향적으로 설계한 구조 모두 알아보자.   

<br />

# 01 데이터 중심의 영화 예매 시스템        
객체지향의 설계에서 두 가지 방법으로 시스템을 객체로 분할할 수 있다.   
(1) 상태를 분할의 중심축으로 삼는 방법: 객체를 독립된 데이터 덩어리로 바라본다.         
(2) 책임을 분할의 중심축으로 삼는 방법: 객체를 협력하는 공동체의 일원으로 바라본다.   

데이터 중심 설계는 객체가 내부에 저장해야 하는 데이터가 무엇인가를 묻는 것으로 시작한다.   

Movie 클래스부터 데이터 중심으로 다시 설계해보자.          
Movie 클래스는 영화 제목, 상영시간, 기본 요금을 인스턴스 변수로 포함했었다.        

[데이터 중심 설계 코드 보러가기](https://github.com/eternity-oop/object/tree/master/chapter04/src/main/java/org/eternity/movie/step01)    


```java
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;
}
```

이전과 달라진 점은 할인 조건의 목록, 할인 정책, 할인 금액, 할인 비율이 전부 Movie 안에서 직접 정의한다는 것이다.   

할인 정책은 영화별로 하나만 지정할 수 있기 때문에 한 시점에 할인 정책 목록에서 하나의 값만 사용될 수 있다.     
영화에 사용된 할인 정책의 종류를 결정하는 방법은 movieType이다.      

```java
public enum MovieType {
    AMOUNT_DISCOUNT,    // 금액 할인 정책
    PERCENT_DISCOUNT,   // 비율 할인 정책
    NONE_DISCOUNT       // 미적용
}
```


객체지향의 가장 중요한 원칙인 캡슐화를 달성할 수 있는 간단한 방법은   
내부의 데이터를 반환, 변경하는 접근자와 수정자를 추가하는 것이다.  

```java 
public double getDiscountPercent() {
    return discountPercent;
}

public void setDiscountPercent(double discountPercent) {
   this.discountPercent = discountPercent;
}
```


나머지 클래스들도 전부 이렇게 데이터 중심으로 설계하면 다음과 같은 데이터 클래스들이 구현된다.  

<img width="650" src="https://user-images.githubusercontent.com/33855307/144794628-675cd025-4429-45b7-97e2-1133b236d301.jpeg">

<br />
<br />

### 영화 예매하기   
ReservationAgency는 데이터 클래스들을 조합해 영화 예매 절차를 구현하는 클래스다.  

```java
public class ReservationAgency {
    public Reservation reserve(Screening screening, Customer customer,
                               int audienceCount) {
        Movie movie = screening.getMovie();

        boolean discountable = false;
        for(DiscountCondition condition : movie.getDiscountConditions()) {
            if (condition.getType() == DiscountConditionType.PERIOD) {
                discountable = screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek()) &&
                        condition.getStartTime().compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
                        condition.getEndTime().compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
            } else {
                discountable = condition.getSequence() == screening.getSequence();
            }

            if (discountable) {
                break;
            }
        }

        Money fee;
        if (discountable) {
            Money discountAmount = Money.ZERO;
            switch(movie.getMovieType()) {
                case AMOUNT_DISCOUNT:
                    discountAmount = movie.getDiscountAmount();
                    break;
                case PERCENT_DISCOUNT:
                    discountAmount = movie.getFee().times(movie.getDiscountPercent());
                    break;
                case NONE_DISCOUNT:
                    discountAmount = Money.ZERO;
                    break;
            }

            fee = movie.getFee().minus(discountAmount).times(audienceCount);
        } else {
            fee = movie.getFee().times(audienceCount);
        }

        return new Reservation(customer, screening, fee, audienceCount);
    }
}
```

이 설계를 책임 중심의 설계 방법과 비교해 보면서 장단점을 파악해보자.   


 
<br />
<br />


# 02 설계 트레이드오프   
장단점 비교를 위해 캡슐화, 응집도, 결합도를 사용해보자.   
세 가지 품질 척도의 의미를 알아보자.   

<br />

### 캡슐화   
상태와 행동을 하나의 객체 안에 모으는 이유는 객체의 내부 구현을 외부로부터 감추기 위해서다.     
여기서 구현이란 나중에 변경될 가능성이 높은 어떤 것을 의미한다.   
상대적으로 안정적인 부분을 인터페이스라고 하며, 인터페이스를 외부에 공개함으로써 변경의 여파를 통제하는 것이 목표이다.   

객체를 설계하기 위한 가장 기본적인 아이디어는 변경의 정도에 따라 구현과 인터페이스를 분리하고 외부에서는 인터페이스에만 의존하도록 관계를 조절하는 것이다.   
캡슐화는 외부에서 알 필요 없는 부분을 감춤으로써 대상을 단순화하는 추상화의 한 종류다.   

<br />

### 응집도와 결합도   
**응집도**는 모듈에 포함된 내부 요소들이 연관돼 있는 정도를 나타낸다.      
모듈 내의 요소들이 서로 다른 목적을 추구한다면 그 모듈은 낮은 응집도를 가진다.        
**결합도**는 의존성의 정도를 나타내며 다른 모듈에 대해 얼마나 많은 지식을 갖고 있는지를 나타내는 척도다.        

**응집도와 결합도는 변경과 관련이 깊다.**        
응집도와 결합도를 변경의 관점에서 바라보는 것은 설계에 대한 시각을 크게 변화시킬 것이다.   

**캡슐화의 정도가 응집도와 결합도에 영향을 미친다.**     
캡슐화를 지키면 모듈 안의 응집도는 높아지고 모듈 사이의 결합도는 낮아진다.     

<br />
<br />

# 03 데이터 중심 설계의 문제점   
데이터 중심, 책임 중심의 근본적인 차이점은 캡슐화를 다루는 방식이다.     
데이터 중심의 설계는 캡슐화를 위반하고 객체의 내부 구현을 인터페이스의 일부로 만든다.          
책임 중심의 설계는 객체의 내부 구현을 안정적인 인터페이스 뒤로 캡슐화한다.   

데이터 중심 설계가 가진 대표적인 문제점은 다음과 같다.  
* 캡슐화 위반   
* 높은 결합도   
* 낮은 응집도  

<br />


### 캡슐화 위반    
데이터 중심의 설계의 Movie 클래스를 보면 객체의 내부 상태에 접근하려면 오직 메서드를 통해서만 가능하다.       
정말 getter/setter로만 접근하는게 캡슐화의 원칙을 지키는걸까? 아니다.     
예를들어 `getFee()`, `setFee()` 메서드는 Movie 내부에           
Money 타입의 fee 라는 이름의 인스턴스 변수가 존재한다는 사실을 퍼블릭 인터페이스에 노골적으로 드러낸다.       

캡슐화의 원칙을 어기게 된 근본적인 원인은 객체가 수행할 책임이 아니라 내부에 저장할 데이터에 초점을 맞췄기 때문이다.   
구현을 캡슐화할 수 있는 적절한 책임은 협력이라는 문맥을 고려할 때만 얻을 수 있다.   

과도한 setter/getter에 의존하는 설계 방식을 추측에 의한 설계 전략이라고 부른다.   
이 전략은 객체가 사용될 협력을 고려하지 않고 다양한 상황에서 사용될 수 있을 것이라는 막연한 추측을 기반으로 설계를 진행한다.   

결과적으로 대부분의 내부 구현이 퍼블릭 인터페이스에 그대로 노출된다.   

<br />

### 높은 결합도   
우선 데이터 중심의 설계는 setter/getter로 내부 구현을 인터페이스의 일부로 만들기 때문에 캡슐화를 위반한다.     
객체 내부의 구현이 객체의 인터페이스에 드러난다는 것은 클라이언트가 구현에 강하게 결합된다는 것을 의미한다.   
더 나쁜 것은 단지 객체의 내부 구현을 변경했음에도 이 인터페이스에 의존하는 모든 클라이언트도 변경해야 한다는 것이다.   

결합도 측면에서 또 다른 단점은 여러 데이터 객체들을 사용하는 제어 로직이 특정 객체 안에 집중되기 때문에  
하나의 제어 객체가 다수의 데이터 객체에 강하게 결합된다는 것이다.   
이 결합도로 인해 어떤 데이터 객체를 변경하더라도 제어 객체를 함께 변경할 수밖에 없다.   

<br />

### 낮은 응집도 
서로 다른 이유로 변경되는 코드가 하나의 모듈 안에 공존할 때 모듈의 응집도가 낮다고 말한다.   

낮은 응집도는 두 가지 측면에서 설계에 문제를 일으킨다.     
* 변경의 이유가 서로 다른 코드들을 하나의 모듈 안에 뭉쳐놓았기 때문에 변경과 아무 상관이 없는 코드들이 영향을 받는다.  
어떤 코드를 수정한 후 아무 상관도 없던 코드에 문제가 발생하는 것은 모듈의 응집도가 낮을 때 발생하는 대표적인 증상이다.   
* 하나의 요구사항 변경을 반영하기 위해 동시에 여러 모듈을 수정해야 한다.   

<br />
<br />

# 04 자율적인 객체를 향해   
상태와 행동을 객체라는 하나의 단위로 묶는 이유는 객체 스스로 자신의 상태를 처리할 수 있게 하기 위해서다.     
객체는 단순한 데이터 제공자가 아니다.   
다음과 같은 두 개의 개별적인 질문으로 분리해서 설계해야 한다.   

```
Q1. 이 객체가 어떤 데이터를 포함해야 하는가?  
Q2. 이 객체가 데이터에 대해 수행해야 하는 오퍼레이션은 무엇인가?  
```

<br />

ReservationAgency로 새어나간 데이터에 대한 책임을 실제 데이터를 포함하고 있는 객체로 옮겨보자.   



### 할인 조건 DiscountCondition 

```java
public class DiscountCondition {
    private DiscountConditionType type;
    private int sequence;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
```

첫 번째 질문인 데이터는 이미 앞에서 결정해놨다.  

두 번째 질문은 이 데이터에 대해 수행할 수 있는 오퍼레이션이 무엇인가를 묻는 것이다.   
할인 조건에는 두 가지 종류가 존재하기 때문에 할인 조건을 판단할 수 있게 두 개의 `isDiscountable` 메서드가 필요할 것이다.  
각 메서드 안에서 type의 값을 이용해 할인 조건 타입에 맞는 적절한 메서드가 호출된건지 판단한다.   

```java
public class DiscountCondition {
   
    . . .

    public boolean isDiscountable(DayOfWeek dayOfWeek, LocalTime time) {
        if (type != DiscountConditionType.PERIOD) {
            throw new IllegalArgumentException();
        }

        return this.dayOfWeek.equals(dayOfWeek) &&
                this.startTime.compareTo(time) <= 0 &&
                this.endTime.compareTo(time) >= 0;
    }

    public boolean isDiscountable(int sequence) {
        if (type != DiscountConditionType.SEQUENCE) {
            throw new IllegalArgumentException();
        }

        return this.sequence == sequence;
    }
}
```

<br />  

### Movie 
첫 번째 질문의 답으로 Movie의 데이터를 이미 정의해놨다.   

두 번째 질문에 대한 대답은 영화 요즘 계산, 할인 여부 판단이다.   

요금을 계산하기 위해서 할인 정책을 염두에 둬야 하고, 할인 정책에 3가지 타입이 있다는 것을 기억해야 한다.   
따라서 세 가지 메서드를 구현해야 한다.   

```java
public Money calculateAmountDiscountedFee() {
    if (movieType != MovieType.AMOUNT_DISCOUNT) {
        throw new IllegalArgumentException();
    }

    return fee.minus(discountAmount);
}

public Money calculatePercentDiscountedFee() {
    if (movieType != MovieType.PERCENT_DISCOUNT) {
        throw new IllegalArgumentException();
    }

    return fee.minus(fee.times(discountPercent));
}

public Money calculateNoneDiscountedFee() {
    if (movieType != MovieType.NONE_DISCOUNT) {
        throw new IllegalArgumentException();
    }

    return fee;
}
```

Movie는 DiscountCondition의 목록을 포함하고 있기 때문에 할인 여부를 판단하는 메서드도 추가해야 한다.  

```java
public boolean isDiscountable(LocalDateTime whenScreened, int sequence) {
    for(DiscountCondition condition : discountConditions) {
        if (condition.getType() == DiscountConditionType.PERIOD) {
            if (condition.isDiscountable(whenScreened.getDayOfWeek(), whenScreened.toLocalTime())) {
                return true;
            }
        } else {
            if (condition.isDiscountable(sequence)) {
                return true;
            }
        }
    }

    return false;
}
```


<br />

### Screening  

```java
public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Screening(Movie movie, int sequence, LocalDateTime whenScreened) {
        this.movie = movie;
        this.sequence = sequence;
        this.whenScreened = whenScreened;
    }

    public Money calculateFee(int audienceCount) {
        switch (movie.getMovieType()) {
            case AMOUNT_DISCOUNT:
                if (movie.isDiscountable(whenScreened, sequence)) {
                    return movie.calculateAmountDiscountedFee().times(audienceCount);
                }
                break;
            case PERCENT_DISCOUNT:
                if (movie.isDiscountable(whenScreened, sequence)) {
                    return movie.calculatePercentDiscountedFee().times(audienceCount);
                }
            case NONE_DISCOUNT:
                movie.calculateNoneDiscountedFee().times(audienceCount);
        }

        return movie.calculateNoneDiscountedFee().times(audienceCount);
    }
}
```

Screening은 Movie가 금액 할인 정책이나 비율 할인 정책을 지원할 경우   
Movie의 isDiscountable 메서드를 호출해 할인이 가능한지 여부를 판단한 후 적절한 Movie의 메서드를 호출해서 요금을 계산한다.   

<br />

### ReservationAgency
ReservationAgency는 Screening의 calculateFee 메서드를 호출해 예매 요금을 계산한 후  
계산된 요금을 이용해 Reservation을 생성한다.   

```java
public class ReservationAgency {
    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        Money fee = screening.calculateFee(audienceCount);
        return new Reservation(customer, screening, fee, audienceCount);
    }
}
```

<br />

<img width="650" src="https://user-images.githubusercontent.com/33855307/144873357-64bbeb86-1364-43a6-872a-61ee328bd21b.JPG">


<br />
<br />

# 05 여전한 캡슐화 위반  
위 설계를 보면 나아진건 맞지만 아직 문제가 여전하다.  

<br />


### 캡슐화 위반  
앞에서 객체들은 자기 자신의 데이터를 스스로 처리하도록 설계했다.     

DiscountCondition 클래스를 살펴보자.     

```java 
public class DiscountCondition {
    private DiscountConditionType type;
    private int sequence;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public DiscountCondition(int sequence) { ... }

    public DiscountCondition(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) { ... }

    public boolean isDiscountable(DayOfWeek dayOfWeek, LocalTime time) { ... }

    public boolean isDiscountable(int sequence) { ... }
}
```


