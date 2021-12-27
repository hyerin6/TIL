# Spring  
<br />     

## Spring Framework란?
<https://hyerin6.github.io/2021-09-24/spring/>

자바 엔터프라이즈 개발을 편하게 해주는 경량급 오픈소스 애플리케이션 프레임워크   


코드에 불필요하게 나타나지 않으면서 기술적인 복잡함과 비즈니스 로직을 깔끔하게 분리하여    
복잡한 엔터프라이즈 개발을 편하게 해주는 애플리케이션 프레임워크이다.      

<br />
<br />

## Spring 사용하는 이유
Spring이 제공하는 기능은 특정 환경에 종속되지 않도록하고, 기술적인 코드를 섞이지 않게 하여 개발자가 비즈니스 로직에만 집중할 수 있게 해준다.     

<br />
<br />

## Bean   
<https://hyerin6.github.io/2020-01-31/spring-DI-IoC/>

<br />

#### Bean이란?
* 컨테이너 안에 들어있는 객체 
* `@Bean`을 사용하거나 xml 설정을 통해 일반 객체를 등록할 수 있다. 

<br />

#### 생명주기 
* 객체 생성 → 의존 설정 → 초기화 → 사용 → 소멸 
* 스프링 컨테이너에 의해 생명주기가 관리된다. 
* 스프링 컨테이너 초기화 시 빈 객체가 생성되고 외존 객체 주입 및 초기화 
* 스프링 컨테이너 종료 시 빈 객체가 소멸된다. 

<br />
<br />

## Container   
<https://hyerin6.github.io/2021-10-06/ioc-di/>  

* 컨테이너는 보통 인스턴스의 생명주기를 관리하며, 생성된 인스턴스들에게 추가적인 기능을 제공하도록 하는 것이라 할 수 있다.       
* Spring 프레임워크는 컨테이너 기능을 제공한다.  
* 컨테이너 기능을 제공하는 것을 가능하도록 하는 것이 IoC 패턴이다. 

<br />
<br />

## IoC
#### IoC란?
객체의 생성부터 생명주기, 관리까지 모든 객체에 대한 제어권을 자신(개발자)이 아닌 다른 대상에게 위임하는 것이다.   

<br />

#### Spring에서의 IoC 
Spring 프레임워크에서 지원하는 IoC Container는 일반 POJO의 생명주기를 관리하며, 생성된 인스턴스들에게 추가적인 기능들을 제공한다.   

<br />

#### 라이브러리와 프레임워크의 차이 
* IoC의 개념이 적용되었느냐의 차이 
* 라이브러리를 사용하는 애플리케이션 코드는 애플리케이션 흐름을 직접 제어한다.      
  단지 동작하는 중에 필요한 기능이 있을 때 능동적으로 라이브러를 사용할 뿐이다.       
* 프레임워크는 애플리케이션 코드가 프레임워크에 의해 사용된다. 
  보통 프레임워크 위에 개발한 클래스를 등록해두고 프레임워크가 흐름을 주도하는 중에 개발자의 코드를 사용하도록 만드는 방식이다.   

<br />
<br />

## DI
#### DI란?
* Dependency Injection, 의존성 주입 
* DI는 Spring 프레임워크에서 지원하는 IoC의 형태이다. 
* DI는 클래스 사이의 의존관계를 빈 설정 정보를 바탕으로 컨테이너가 자동적으로 연결해주는 것을 말한다. 
  개발자는 직접 제어하지 않고 빈 설정 파일에 정보를 추가하면 된다. 
* 컨테이너가 실행 흐름의 주체가 되어 애플리케이션 코드에 의존관계를 주입해주는 것이다. 

<br />

#### 의존성 
현재 객체가 다른 객체와 상호작용(참조)하고 있다면 다른 객체들을 현재 객체의 의존이라 한다.  

<br />

#### 의존성이 위험한 이유 
* 하나의 모듈이 바뀌면 의존한 다른 모듈까지 변경되어야 한다. 
* 유닛테스트 작성이 어렵다. 

<br />

#### DI 특징 
* `new`를 사용해 모듈 내에서 다른 모듈을 초기화하지 않으려면 객체 생성은 다른 곳에서 하고, 생성된 객체를 참조하면 된다. 
* 의존성 주입은 IoC 개념을 바탕으로 한다. 클래스가 외부로부터 의존성을 가져야한다. 


<br />

#### DI가 필요한 이유 
* 클래스를 재사용할 가능성을 높이고, 다른 클래스와 독립적으로 클래스를 테스트 할 수 있다. 
* 비즈니스 로직의 특정 구현이 아닌 클래스를 생성하는 데 매우 효과적이다. 

<br />
<br />

## AOP
#### AOP란?
* Aspect Oriented Programming, 관점 지향 프로그래밍 
* 어떤 로직을 기준으로 핵심, 부가 관점으로 나누고 관점을 기준으로 모듈화하는 것

<br />

#### AOP 목적 
* 소스 코드에서 여러 번 반복해서 쓰는 코드를 Aspect로 모듈화하여 핵심 로직에서 분리 및 재사용
* 개발자가 핵심 로직에 집중할 수 있게 하기 위함 
* 주로 부가 기능을 모듈화한다. 

<br />

#### AOP 적용 방법 
* 컴파일 시 사용 
  - AspectJ가 사용하는 방법
  - 자바 파일을 클래스 파일로 만들 때 Advice 소스가 추가되어 조작된 바이트 코드 생성하는 방법 



* 로드 시 적용
  - AspectJ가 사용하는 방법 
  - 컴파일 후 컴파일 된 클래스를 로딩하는 시점에 Advice 소스를 끼워넣는 방법 
  



* 런타임 시 적용
  - Spring AOP가 사용하는 방법 
  - 스프링 런타임 시 Bean 생성 
  - A라는 빈을 만들 때 A 타입 프록시 빈도 생성하고 프록시 빈이 A의 메소드 호출 직전에 Advice 소스를 호출한 후 A 메소드를 호출한다.

<br />

#### Spring AOP 특징 
* 프록시 패턴 기반의 AOP 구현체 
  - Target 객체에 대한 프록시를 만들어 제공 
  - Tartget을 감싸는 프록시는 런타임 시 생성 
  - 접근 제어 및 부가 기능 추가를 위해 프록시 객체 사용 
  


* 프록시가 Target 객체의 호출을 가로채 Advice 수행 전/후 핵심 로직 호출 




* 스프링 Bean에만 AOP 적용 가능 
  - 메소드 조인 포인트만 지원하여 메소드가 호출되는 런타임 시점에만 Advice 적용 가능 
  

* 모든 AOP 기능을 제공하는 것은 아니다. 


<br />
<br />

## POJO
* <https://hyerin6.github.io/2021-09-24/spring/>
* <https://azderica.github.io/00-java-pojo/>  

#### POJO 란?  
POJO는 plain old java object의 약자이다.  
오래된 자바의 간단한 자바 오브젝트라고 해석할 수 있는데,  
주요 자바 오브젝트 모델, 컨벤션 또는 프레임워크를 따르지 않는 자바 오브젝트를 의미한다.   


2009년에 마틴 파울러, 레베카 파슨스, 조시 맥켄지에 의해 만들어졌다.    
다음은 POJO에 대한 기원이다.   

```text
"We wondered why people were so against using regular objects in their systems and concluded that it was because simple objects lacked a fancy name. 
So we gave them one, and it's caught on very nicely."

우리는 왜 사람들이 자기들 시스템에 일반적인 오브젝트를 사용하는 것에 반대하는지 궁금했고, 
그 이유는 단순한 오브젝트에 멋진 이름이 없기 때문이라고 결론을 지었습니다. 
그래서 우리는 멋진 이름을 지었고, 매우 인기를 얻었습니다.
```

예) ORM 기술을 적용하기 위해 ORM 프레임워크인 Hibernate을 사용하기 위해 직접 의존하는 경우 POJO가 아니게된다.   
이를 사용하기 위해서는 JPA라는 특정 표준 인터페이스를 통해서 사용해야 한다.   
(PSA 일관성 있는 추상화, 같은 일을 하는 다수의 기술을 공통의 인터페이스로 제어할 수 있게한 것을 서비스 추상화라고 한다.)      

<br />

#### POJO 정의 
이상적인 POJO는 Java 언어 규약에 의해 강제된 것 이외의 제한에 구속되지 않는 자바 오브젝트이다.   

POJO프로그래밍의 진정한 가치는 **자바의 객체지향적인 특징을 살려 비즈니스 로직에 충실한 개발이 가능하도록 하는 것이다.**      

예) 미리 정의된 클래스, 인터페이스, 어노테이션을 extends, implements, 포함하면 안된다. 

```java
public class Exam extends javax.servlet.http.HttpServlet { ... }

public class Exam implements javax.ejb.EntityBean { ... }

@javax.persistence.Entity public class Exam { ... }
```

위와 같은 구현은 특정 환경에 종속되게 만든다.       
POJO 개념을 적용하면 코드와 라이브러리와의 결합성을 줄이도록 도와준다.   

<br />

#### POJO: JavaBeans
JavaBeans는 특별한 POJO의 변형이라고 할 수 있다.  


|                                         POJO                                        |                            JAVA BEAN                           |
|:-----------------------------------------------------------------------------------:|:--------------------------------------------------------------:|
|                     Java laguage 에 의한 특별한 제한이 없습니다.                    |                      POJO 보다 제한적이다.                     |
|                       Fields 에 대한 통제를 제공하지 않습니다.                      |                 Fields에 대한 통제를 가집니다.                 |
|                SErializable 에 대한 interface 를 구현할 수 있습니다.                |      Serializable 에 대한 interface 를 구현해야만 합니다.      |
|                        Fields 는 이름으로 접근할 수 있습니다.                       |      Fields 는 getter 와 setter 로만 접근할  수 있습니다.      |
|                    Fields 에 대한 접근제어자 규칙이 자유롭습니다.                   |      Fields 는 접근제어자를 private 로만 가질 수 있습니다.     |
|                     constructor 에 argument를 가질 수 있습니다.                     |           constructor 에 argument를 가질 수 없습니다.          |
| member와 field에 대해  <br />  제한적인 접근 규칙을 정하고 싶지 않을 때 사용됩니다. | member와 restriction 한 접근 규칙을 정하고자 할 때 사용됩니다. |


<br />

#### 왜 JavaBeans를 사용하는가?
Java Bean은 Builder Tool에서 시각적으로 조작할 수 있는 재사용 가능한 소프트웨어 컴포넌트이다.      
서로 다른 Builder에서 편리하게 Java Class를 쓸 수 있도록 한다.     

즉, 어디서나 공통의 Convention을 지키며 사용할 수 있는 것이고,    
따라서 별도의 문서 없이 쉽고 일관된 방식으로 사용할 수 있음을 의미한다.     

<br />

#### POJO: EJB(Enterprise JavaBeans)
EJB는 상속과 다형성 등의 혜택을 누릴 수 없다.     
간단한 기능 하나를 위해서도 많은 인터페이스와 EJB 의존적인 상속을 해야한다.     

결국 EJB는 형편없는 생산성, 느린 성능, 불필요한 기술적인 복잡도, 과도한 스펙 등으로 불신을 받게 되었고, POJO 방식으로 돌아서는 계기가된다.    

<br />
<br />

## DAO와 DTO의 차이
#### DAO(Data Access Object)
* DB의 데이터를 조회하거나 조작하는 기능을 전담하도록 만든 객체를 말한다.
* DB에 접근을 하기위한 로직과 비즈니스 로직을 분리하기 위해서 사용 한다.

<br />

#### DTO(Data Transfer Object)
* 계층간 데이터 교환을 위한 자바빈즈를 말한다.
  - 여기서 말하는 계층은 Controller, View, Business Layer, Persistent Layer 이다.
* 일반적인 DTO는 로직을 갖고 있지 않는 순수한 데이터 객체이며, 속성과 그 속성에 접근하기 위한 getter, setter 메소드만 가진 클래스이다.
* VO(Value Object) 라고도 불린다.
  - DTO와 동일한 개념이지만 read only 속성을 가진다.
  
<br />
<br />

## Filter와 Interceptor 차이

#### Filter 특징
* Dispatcher Servlet 이전에 수행되고, 응답 처리에 대해서도 변경 및 조작 수행 가능
    - WAS 내의 ApplicationContext에서 등록된 필터가 실행
* WAS 구동 시 FilterMap이라는 배열에 등록되고, 실행 시 Filter chain을 구성하여 순차적으로 실행
* Spring Context 외부에 존재하여 Spring과 무관한 자원에 대해 동작
* 일반적으로 web.xml에 설정
* 예외 발생 시 Web Application에서 예외 처리
* ex. 인코딩 변환, XSS 방어 등
* 실행 메소드
    - `init()` : 필터 인스턴스 초기화
    - `doFilter()` : 실제 처리 로직
    - `destroy()` : 필터 인스턴스 종료

<br />

#### Interceptor 특징
* Dispatcher Servlet 이후 Controller 호출 전, 후에 끼어들어 기능 수행
* Spring Context 내부에서 Controller의 요청과 응답에 관여하며 모든 Bean에 접근 가능
* 일반적으로 servlet-context.xml에 설정
* 예외 발생 시 `@ControllerAdvice`에서 `@ExceptionHandler`를 사용해 예외 처리
* ex. 로그인 체크, 권한 체크, 로그 확인 등
* 실행 메소드
    - `preHandler()` : Controller 실행 전
    - `postHandler()` : Controller 실행 후
    - `afterCompletion()` : view Rendering 후
    
<br />

#### Filter, Interceptor 차이점 요약
* Filter는 WAS단에 설정되어 Spring과 무관한 자원에 대해 동작하고,   
  Interceptor는 Spring Context 내부에 설정되어 컨트롤러 접근 전, 후에 가로채서 기능 동작  
* Filter는 `doFilter()` 메소드만 있지만, Interceptor는 pre와 post로 명확하게 분리
* Interceptor의 경우 AOP 흉내 가능
    - handlerMethod(`@RequestMapping`을 사용해 매핑 된 `@Controller`의 메소드)를   
      파라미터로 제공하여 메소드 시그니처 등 추가 정보를 파악해 로직 실행 여부 판단 가능  
    
<br />
<br />

## MVC 패턴
* 스프링 프레임워크는 MVC 패턴을 준수한다.    
* 모델, 뷰, 컨트롤러의 세 가지 요소로 나누어진 패턴을 MVC 패턴이라고 한다. 

<br />  

#### MVC1         
<img width="500" src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FI1UUx%2FbtqEZ0IhZ6O%2FqzvwssYAAkEltNqYd3Kpik%2Fimg.png">

MVC1 패턴의 경우 View와 Controller를 모두 JSP가 담당하는 형태를 가진다.      
즉 JSP 하나로 유저의 요청을 받고 응답을 처리하므로 구현 난이도는 쉽다.  
단순한 프로젝트에는 괜찮겠지만 내용이 복잡하고 거대해질수록 이 패턴은 힘을 잃는다.   
JSP 하나에서 MVC 가 모두 이루어지다보니 재사용성도 매우 떨어지고, 읽기도 힘들어진다.     
즉 유지보수에 있어서 문제가 발생합니다.     

<br />  

#### MVC2       
<img width="500" src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbgbhsb%2FbtqE0BOnWqs%2FGhoRjVi90P1dYSBjRHSo91%2Fimg.png">

MVC2 패턴은 널리 표준으로 사용되는 패턴이다.    
요청을 하나의 컨트롤러(Servlet)가 먼저 받는다.     
즉 MVC1과는 다르게 Controller, View가 분리되어 있다.    
따라서 역할이 분리되어 MVC1패턴에서의 단점을 보완할 수 있다.   
그러므로 개발자는 M, V, C 중에서 수정해야 할 부분이 있다면, 그것만 꺼내어 수정하면 된다.     
따라서 유지보수에 있어서도 큰 이점을 가진다.     

MV2는 MVC1 패턴보다 구조가 복잡해질 수 있지만, 개발자가 이러한 세부적인 구성까지 신경쓰지 않을 수 있도록      
각종 프레임워크들이 지금까지 잘 발전되어 왔다. 그 중에서 대표적인 것이 바로 스프링 프레임워크이다.     

<br />

#### Spring의 MVC 패턴 
<img width="500" src="https://i.imgur.com/blr7x6q.png">

스프링에서는 유저의 요청을 받는 DispathcerServlet이 핵심이다.   
이것이 Front Controller의 역할을 맡는다.   

Front Controller(프런트 컨트롤러)란, 우선적으로 유저(클라이언트)의 모든 요청을 받고, 그 요청을 분석하여 세부 컨트롤러들에게 필요한 작업을 나눠주게 된다.   

<br />
<br />

## 싱글톤 패턴(Singleton pattern)  
최초 한번만 메모리를 할당하고(static) 그 메모리에 인스턴스르 만들어 사용하는 디자인 패턴이다.  


#### 싱글톤 패턴을 쓰는 이유 
* 고정된 메모리 영역을 얻으면서 한번의 `new`로 인스턴스를 사용하기 때문에 메모리 낭비를 방지할 수 있다. 
* 싱글톤으로 만들어진 클래스의 인스턴스는 전역 인스턴스이기 때문에 다른 인스턴스들이 공유하기 쉽다. 


#### 싱글톤 패턴의 문제점 
* 싱글톤 인스턴스가 너무 많은 일을 하거나 많은 데이터를 공유시킬 경우,     
  다른 클래스의 인스턴스들 간에 결합도가 높아져 개방 폐쇄 원칙을 위배하게 된다.   
  → 수정이 어려워지고 테스트하기 어려워진다. 

* 멀티스레드 환경에서 동기화 처리를 하지 않으면 인스턴스가 두개 생성될 수도 있다. 

