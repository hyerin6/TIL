# 의존성 역전하기

### 단일 책임 원칙

`하나의 컴포넌트는 오로지 한 가지 일만 해야 하고, 그것을 올바르게 수행해야 한다.`

단일 책임 원칙의 실제 의도는 아니다.

'오로지 한 가지 일만 하는 것'은 단일 책임이라는 말을 가장 직관적으로 해석한 것이므로 오해의 여지가 있다.

실제 정의는 다음과 같다.

`컴포넌트를 변경하는 이유는 오직 하나 뿐이어야 한다.`

아키텍처에서 이것이 어떤 의미일까?

만약 컴포넌트를 변경할 이유가 한 가지라면 우리가 어떤 다른 이유로 소프트웨어를 변경하더라도 이 컴포넌트에 대해서는 전혀 신경 쓸 필요가 없다.

소프트웨어가 변경되더라도 이 컴포넌트에 대해서는 전혀 신경 쓸 필요가 없다.

변경할 이유라는 것은 컴포넌트 간의 의존성을 통해 너무 쉽게 전파된다.

```plaintext
A → B → E

A → C 

D → E
```

컴포넌트 E는 의존하는 것이 전혀 없다.

컴포넌트 E를 변경할 유일한 이유는 새로운 요구사항에 의해 E의 기능을 바꿔야 할 때뿐이다.

반면 A는 모든 컴포넌트에 의존하고 있기 때문에 어떤 컴포넌트가 바뀌든지 같이 바뀐다.

<br />
<br />

### 의존성 역전 원칙

계층형 아키텍처에서 계층 간 의존성은 항상 다음 계층인 아래 방향을 가리키다.

단일 책임 원칙을 고수준에서 적용할 때 상위 계층들이 하위 계층들에 비해 변경할 이유가 더 많다는 것을 알 수 있다.

그러므로 영속성 계층에 대한 도메인 계층의 의존성 때문에 영속성 계층을 변경할 때마다 잠재적으로 도메인 계층도 변경해야 한다.

그러나 도메이 코드는 애플리케이션에서 가장 중요한 코드다.

영속성 코드가 바뀐다고 해서 도메인 코드까지 바꾸고 싶지 않다.

이 의존성을 어떻게 제거할 수 있을까?

**→ 의존성 역전 원칙**


> `코드상의 어떤 의존성이든 그 방향을 바꿀 수 있다.`       
> 의존성의 양쪽 코드를 모두 제어할 수 있을 때만 의존성을 역전시킬 수 있다.     
> 만약 서드파티 라이브러리에 의존성이 있다면 해당 라이브러리를 제어할 수는 없기 때문에 이 의존성은 역전시킬 수 없다.          

<br />  

의존성 역전은 도메인 코드와 영속성 코드 간의 의존성을 역전 시켜서

영속성 코드가 도메인 코드에 의존하고, 도메인 코드를 '변경할 이유'의 개수를 줄여보자.

<br />

<img width="600" src="https://user-images.githubusercontent.com/33855307/152629273-d01451ca-638a-4c08-8f97-9e8a9fb5423c.png">


엔티티는 도메인 객체를 표현하고 도메인 코드는 이 엔티티들의 상태를 변경하는 일을 중심으로 하기 때문에 먼저 엔티티를 도메인 계층으로 올린다.    


<img width="400" src="https://user-images.githubusercontent.com/33855307/152629418-a99844f8-cce3-4bc9-ac6e-fdbd00877469.png">

순환 의존성이 생겼다. 이 부분이 바로 DIP를 적용하는 부분이다.     

도메인 계층에 Repository에 대한 인터페이스를 만들고, 실제 Repository는 영속성 계층에서 구현하게 하는 것이다.  


<img width="600" src="https://user-images.githubusercontent.com/33855307/152629423-31775026-e8fe-4b8e-b1a6-656f1c7c465b.png">


영속성 코드에 있는 의존성으로부터 도메인 로직을 해방시켰다.

<br />
<br />

### 클린 아키텍처    
<img width="600" src="https://user-images.githubusercontent.com/53366407/150670907-d02e23be-f3e8-4b42-9dc2-218bed86bc9a.png">

클린 아키텍처   
➡️ 비즈니스 규칙의 테스트를 용이하게, 비즈니스 규칙이 독립적일 수 있게한다.    
➡️ 도메인 코드가 바깥으로 향하는 어떤 의존성도 없어야 함. / 의존성 역전 원칙으로 모든 의존성이 도메인 코드를 향한다.   
➡️ 위에서 유스케이스는 서비스 (도메인 엔티티에 접근), 단일 책임을 위해 더 세분화됨.  
➡️ 이를 통해 넓은 서비스 문제를 피할 수 있다.   


도메인 코드에서는 어떤 영속성 프레임워크가 사용되는지 알 수 없기 때문에 특정 프레임워크에 특화된 코드를 가질 수 없고 비즈니스 규칙에 집중할 수 있다.     
도메인 코드를 자유롭게 모델링할 수 있다.    
예) 도메인 주도 설계(DDD)를 가장 순수한 형태로 적용해볼 수도 있음.    

클린 아키텍처는 도메인 계층이 영속성이나 UI 같은 외부 계층과 철저하게 분리돼야 하므로   
애플리케이션의 엔티티에 대한 모델을 각 계층에서 유지보수해야 한다.   

예) ORM 프레임워크 사용   
일반적으로 ORM 프레임워크는 데이터베이스 구조 및 객체 필드와 데이터베이스 칼럼의 매핑을 서술한 메타데이터를 담고 있는 엔티티 클래스가 필요하다.      
도메인 계층은 영속성 계층을 모르기 때문에 도메인 계층에서 사용하는 엔티티를 영속성 계층에서 함께 사용할 수 없고 두 계층에서 각각 엔티티를 만들어야 한다.     
즉, 도메인 계층과 영속성 계층이 데이터를 주고 받을 때, 두 엔티티를 서로 변환해야 한다는 의미다. (다른 계층에서도 마찬가지)  

추상적인 클린 아키텍처를 조금 더 깊게 들어가 '육각형 아키텍처'에 대해 알아보자.


<br />
<br />

### 육각형 아키텍처 (헥사고날 아키텍처)  
클린 아키텍처에서 좀 더 일반적인 용어로 동일한 원칙을 적용한다.   

<img width="600" src="https://user-images.githubusercontent.com/53366407/150670896-9a968565-1721-4cb1-bdba-b33d465cd671.png">

육각형 안에는 도메인 엔티티와 이와 상호작용하는 유스케이스가 있다.    
육각형에서 외부로 향하는 의존성이 없기 때문에 클린 아키텍처의 의존성 규칙이 그대로 적용된다.   
모든 의존성은 코어를 향한다.  

