# 코드 구성하기 

예제) BuckPal, 사용자가 본인의 계좌에서 다른 계좌로 돈을 송금할 수 있는 '송금하기' 유스케이스  
예제코드: <https://github.com/wikibook/clean-architecture>  

<br />

### 계층으로 구성하기
```
buckpal
├── domain
│   ├── Account
│   ├── Activity
│   ├── AccountRepository 
│   └── AccountService 
│    
│   ├── Persistence
│       └── AccountRepositoryImpl
│    
└── Web
    └── AccounController 
```  

**최적의 구조가 아닌 이유**  
1. 애플리케이션의 기능 조각이나 특성을 구분 짓는 패키지 경계가 없다.
    - 이 구조에 사용자 관리 기능을 추가해야 한다면,   
      web에 UserController, domain에 UserService, UserRepository,   
      persistence에 UserRepositoryImpl을 추가하게 된다.     
      빠르게 연관되지 않은 기능들의 클래스들이 엉망진창 묶음으로 변할 가능성이 크다.     
    

2. 애플리케이션이 어떤 유스케이스들을 제공하는지 파악할 수 없다.   
    - 특정 기능을 찾기 위해 어떤 서비스가 구현했는지 추측하고 메서드를 찾아야 한다. 


3. 패키지 구조를 통해서 우리가 목표로 하는 아키텍처를 파악할 수 없다.     
    - 영속성 어댑터가 도메인 계층에 어떤 기능을 제공하는지 한눈에 알아볼 수 없다.    
      인커밍 포트와 아웃고잉 포트가 코드 속에 숨겨져 있다.   
    
<br />
<br />  

### 기능으로 구성하기 
계층으로 구성하는 방법의 몇 가지 문제점을 해결해보자.   

```
buckpal
└── account
    ├── Account
    ├── AccountController
    ├── AccountRepository 
    ├── AccountRepositoryImpl 
    └── SendMoneyService 
```

#### 장점 
* 패키지 경계를 package-private 접근 수준과 결합하면 각 기능 사이의 불필요한 의존성을 방지할 수 있다.      

* `AccountService → SendMoneyService` 변경으로 '송금하기' 유스케이스를 구현한 코드를 클래스명만으로도 찾을 수 있다.  


#### 문제점   
* 기능을 기준으로 코드를 구성하면 기반 아키텍처가 명확하게 보이지 않는다.

* 게층에 의한 패키징보다 아키텍처의 가시성을 훨씬 더 떨어뜨린다. 

* 도메인 코드와 영속성 코드 간 의존성을 역전시켜 SendMoneyService가 AccountRepository 인터페이스만 알고 있고 구현체는 모르는데   
  도메인 코드가 실수로 영속성 코드에 의존하는 것을 막을 수 없다.           


<br />
<br />

### 아키텍처적으로 표현력 있는 패키지 구조 
육각형 아키텍처에서 구조적으로 핵심적인 요소는 엔티티, 유스케이스, 인커밍/아웃고잉 코트, 인커밍/아웃고잉 어탭터다.   
이 요소들을 애플리케이션의 아키텍처를 표현하는 패키지 구조로 구성해 보자. 

```
buckpal
└── account
    ├── adapter
    │   ├── in
    │   │   └── web
    │   │       └── AccountController
    │   ├── out
    │   │   └── persistence
    │   │       ├── AccountPersistenceAdapter
    │   │       └── SpringDataAccountRepository 
    │   │
    ├── domain
    │   ├── Account 
    │   └── Activity
    └── application 
        └── SendMoneyService (구현 클래스)
        └── port 
            ├── in
            │   └── SendMoneyUseCase (인터페이스) 
            └── out
                ├── LoadAccountPort
                └── UpdateAccountStatePort 
```

buckpal 예제는 각각의 하위 패키지를 가진 web 어댑터와 persistence 어댑터로 이뤄진 간단한 웹 애플리케이션이 됐다.   


#### 장점 
* 각 아키텍처 요소들에 정해진 위치가 있어 직관적이다.   

* 어댑터 코드를 자체 패키지로 이동시키면 필요할 경우 하나의 어댑터를 다른 구현으로 쉽게 교체할 수 있다.   

* DDD 개념을 직접적으로 대응할 수 있다.   
    - 상위 레벨 패키지는 다른 바운디드 컨텍스트와 통신할 전용 진입점과 출구(포트)를 포함하는 바운디드 컨텍스트에 해당한다.    


#### 문제점 
* 패키지가 많다고 모든 것을 public으로 만들어야 하는 것은 아니지만,    
application 패키지와 domain 패키지 내의 일부 클래스들은 public으로 지정해야 한다.     

* 이처럼 표현력 있는 패키지 구조는 아키텍처에 대한 적극적인 사고를 촉진한다.      
많은 패키지가 생기고, 현재 작업 중인 코드를 어떤 패키지에 넣어야 할지 계속 생각해야 하기 때문이다.  


<br />
<br />

### 의존성 주입 역할 
어댑터는 애플리케이션 계층에 위치한 서비스를 호출할 뿐이다.   
영속성 어댑터와 같이 아웃고잉 어댑터에 대해서는 제어 흐름의 반대 방향으로 의존성을 돌리기 위해 의존성 역전 원칙을 이용해야 한다.    

모든 계층에 의존성을 가진 중립적인 컴포넌트를 하나 도입하면 의존성 주입을 활용할 수 있다.   
중립적인 컴포넌트는 아키텍처를 구성하는 대부분의 클래스를 초기화하는 역할을 한다.  


<img width="700" src="https://user-images.githubusercontent.com/33855307/152731686-0edb14c3-b245-48f1-a124-780865209661.jpeg">


웹 컨트롤러가 서비스에 의해 구현된 인커밍 포트를 호출한다.    
서비스는 어댑터에 의해 구현된 아웃고잉 포트를 호출한다.  

