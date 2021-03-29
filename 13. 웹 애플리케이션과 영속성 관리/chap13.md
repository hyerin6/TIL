스프링이나 J2EE 컨테이너 환경에서 JPA를 사용하면 컨테이너가 트랜잭션과 영속성 컨텍스트를 관리해주므로 애플리케이션을 쉽게 개발할 수 있다.      

<br />    

# 1. 트랜잭션 범위의 영속성 컨텍스트      
스프링에서 JPA를 사용하면 컨테이너가 제공하는 전략을 따라야 한다.     

<br />   


### 1.1 스프링 컨테이너의 기본 전략    
스프링 컨테이너는 트랜잭션 범위의 영속성 컨텍스트 전략을 기본으로 사용한다.    
이 전략은 트랜잭션 범위와 영속성 컨텍스트의 생존 범위가 같다는 뜻이다.    

트랜잭션을 시작할 때 영속성 컨텍스트를 생성하고 트랜잭션이 끝날 때 영속성 컨텍스트를 종료한다.      
같은 트랜잭션 안에서는 항상 같은 영속성 컨텍스트에 접근한다.       


![스크린샷 2021-03-21 오후 10 56 53](https://user-images.githubusercontent.com/33855307/111907468-c9c93180-8a98-11eb-8d5c-d0d8bfb1bd36.png)    

스프링 프레임워크를 사용하면 보통 비즈니스 로직을 시작하는 서비스 계층에 `@Transactional` 어노테이션을 선언해서 트랜잭션을 시작한다.       
외부에서는 단순히 서비스 계층의 메소드를 호출하는 것처럼 보이지만 이 어노테이션이 있으면     
호출한 메소드를 실행하기 직전에 스프링의 트랜잭션 AOP가 먼저 동작한다.       


<br />     


![스크린샷 2021-03-21 오후 11 03 35](https://user-images.githubusercontent.com/33855307/111907662-aeaaf180-8a99-11eb-90f6-fd58d30edb3b.png)    

트랜잭션을 커밋하면 JPA는 먼저 영속성 컨텍스트를 플러시해서 변경 내용을 데이터베이스에 반영한 후 데이터베이스 트랜잭션을 커밋한다.   
따라서 영속성 컨텍스트의 변경 내용이 데이터베이스에 정상 반영된다.   
예외가 발생하면 트랜잭션을 롤백하고 종료하는데 이때는 플러시를 호출하지 않는다.      


<br />       


다음은 트랜잭션 범위의 영속성 컨텍스트 전략 예제 코드이다.     

```
@Controller
class HelloController {
    @Autowired 
    HelloService helloService;
    
    public void hello() {
        // 반환된 member 엔티티는 준영속 상태다. (4)
        Member member = helloService.logic();
    }
}


@Service
class HelloService {
    @PersistenceContext // 엔티티 매니저 주입 
    EntityManeger em;
    
    @Autowired 
    Repository1 repo1;
    @Autowired 
    Repository2 repo2;
    
    // 트랜잭션 시작 (1)
    @Transactional
    public void logic() {
        repo1.hello();
        
        // member는 영속 상태다. (2)
        Member member = repo2.findMember();
    }
    // 트랜잭션 종료 (3)
}


@Repository
calss Repository1 {
    @PersistenceContext
    EntityManeger em;
    
    public void hello() {
        em.xxx(); // A. 영속성 컨텍스트 접근 
    }
}


@Repository
calss Repository2 {
    @PersistenceContext
    EntityManeger em;
    
    public Member findMember() {
        return em.find(Member.class, "id1"); // B. 영속성 컨텍스트 접근 
    }
}
```

<br />   

(1) `HelloService.logic()` 메소드에 `@Transactional`을 선언해서 메소드를 호출할 때 트랜잭션을 먼저 시작한다.    

(2) `repo2.findMember()`를 통해 조회한 member 엔티티는 트랜잭션 범위 안에 있으므로 영속성 컨텍스트의 관리를 받는다. 따라서 지금은 영속 상태다.   

(3) `@Transactional`을 선언한 메소드가 종료되면 트랜잭션을 커밋하는데 이때 영속성 컨텍스트를 종료한다.       
영속성 컨텍스트가 사라졌으므로 조회한 엔티티(member)는 이제부터 준영속 상태가 된다.     

(4) 서비스 메소드가 끝나면서 트랜잭션과 영속성 컨텍스트가 종료되었다. 따라서 컨트롤러에 반환된 member 엔티티는 준영속 상태다.    



<br />     

### 1.2 트랜잭션 범위의 영속성 컨텍스트 전략   
* 트랜잭션이 같으면 같은 영속성 컨텍스트를 사용한다.    
엔티티 매니저가 달라도 같은 영속성 컨텍스트 사용   
  
* 트랜잭션이 다르면 다른 영속성 컨텍스트를 사용한다.     
<br />     
 
스프링 컨테이너의 큰 장점은 트랜잭션과 복잡한 멀티 스레드 상황을 컨테이너가 처리해준다는 점이다.      
따라서 개발자는 싱글 스레드 애플리케이션처럼 개발할 수 있고 결과적으로 비즈니스 로직 개발에 집중할 수 있다.       


<br />     


# 2. 준영속 상태와 지연 로딩   

컨테이너 환경의 기본 전략인 트랜잭션 범위의 영속성 컨텍스트 전략을 사용하면 트랜잭션이 없는 프리젠테이션 계층에서 엔티티는 준영속 상태다.    
따라서 변경 감지와 지연 로딩이 동작하지 않는다.     
다음과 같이 컨트롤러에 있는 로직인데 지연 로딩 시점에 예외가 발생한다.


```
@Entity
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    
}


@Controller
class OrderController {

    public String view(Long orderId) {
        Order order = orderService.findOne(orderId);
        Member member = order.getMember();
        member.getName(); // 지연 로딩 시 예외 발생     
        
        ...
    
    }
    
}
```
<br />   

### 준영속 상태와 변경 감지    
변경 감지 기능은 영속성 컨텍스트가 살아있는 서비스 계층(트랜잭션 범위)까지만 동작하고 영속성 컨텍스트가 종료된 프리젠테이션 계층에서는 동작하지 않는다.    
비즈니스 로직은 서비스 계층에서 끝내고 프리젠테이션 계층은 데이터를 보여주는 데 집중해야 한다.      
<br />   

### 준영속 상태와 지연 로딩   
준영속 상태의 가장 골치 아픈 문제는 지연 로딩 기능이 동작하지 않는다는 점이다.   
예를 들어 뷰를 렌더링 할 때 연관된 엔티티도 함께 사용해야 하는데 연관된 엔티티를 지연 로딩으로 설정해서 프록시 객체로 조회했다고 가정하자.   
하지만 준영속 상태는 영속성 컨텍스트가 없으므로 지연 로딩을 할 수 없다. 이때 지연로딩을 시도하면 문제가 발생한다.     

준영속 상태의 지연 로딩 문제를 해결하는 방법은 크게 2가지가 있다.   

* 뷰가 필요한 엔티티를 미리 로딩해두는 방법   
    - 글로벌 페치 전략 수정  
    - JPQL 페치 조인    
    - 강제로 초기화      
    
* OSIV를 사용해서 엔티티를 항상 영속 상태로 유지하는 방법   

<br />   

### 2.1 글로벌 페치 전략 수정    
지연 로딩에서 즉시 로딩으로 변경하면 된다.   

```
@ManyToOne(fetch = FetchType.EAGER)
private Member member;
```

하지만 이렇게 글로벌 페치 전략을 즉시 로딩으로 설정하는 것은 2가지 단점이 있다.    
<br />       

### 글로벌 페치 전략에 즉시 로딩 사용 시 단점   
* 사용하지 않는 엔티티를 로딩한다.
* N+1 문제가 발생한다.      


JPA를 사용하면서 성능상 가장 조심해야 하는 것이 바로 N+1 문제다. 

`em.find()` 메소드로 엔티티를 조회할 때 연관된 엔티티를 로딩하는 전략이 즉시 로딩이면 데이터베이스에 JOIN 쿼리를 사용해서   
한 번에 연관된 엔티티까지 조회한다. 다음 예제는 `Order.member`를 즉시 로딩으로 설정했다.    

```
// CODE
Oder order = em.find(Order.class, 1L);

// SQL 
select o.*, m.*
from Order o
left outer join Member m on o.MEMBER_ID=m.MEMBER_ID
where o.id=1
```
<br />      

실행된 SQL을 보면 즉시 로딩으로 설정한 member 엔티티를 JOIN 쿼리로 함께 조회한다.    
문제는 JPQL을 사용할 때 발생한다. 즉시 로딩으로 설정했다고 하정하고 JPQL로 조회해보자.    

```
// CDOE
List<Order> orders = 
    em.ccreateQuery("select o from Oder o", Order.class)
    .getResultList(); // 연관된 모든 엔티티를 조회한다.     
    
    
// SQL 
select * from Order // JPQL로 실행된 SQL
select * from Member where id=? // EAGER로 실행된 SQL
select * from Member where id=? // EAGER로 실행된 SQL
select * from Member where id=? // EAGER로 실행된 SQL
select * from Member where id=? // EAGER로 실행된 SQL
...

```

JPA가 JPQL을 분석해서 SQL을 생성할 때는 글로벌 페치 전략을 참고하지 않고 오직 JPQL 자체만 사용한다.     
따라서 즉시 로딩이든 지연 로딩이든 구분하지 않고 JPQL 쿼리 자체에 충실하게 SQL을 만든다.      

내부에서 다음과 같은 순서로 동작한다. 
1. `select o from Order o` JPQL을 분석해서 `select * from Order` SQL을 생성한다.     
2. 데이터베이스에서 결과를 받아 order 엔티티 인스턴스를 생성한다.    
3. `Order.member`의 글로벌 페치 전략이 즉시 로딩이므로 order를 로딩하는 즉시 연관된 member도 로딩해야 한다.    
4. 연관된 member를 영속성 컨텍스트에서 찾는다.    
5. 만약 영속성 컨텍스트에 없으면 `SELECT * FROM MEMBER WHERE id=?` SQL을 조회한 order 엔티티 수만큼 실행한다.   

<br />     

만약 조회한 order 엔티티가 10개이면 member를 조회하는 SQL도 10번 실행한다.   
이처럼 처음 조회한 데이터 수만큼 다시 SQL을 사용해서 조회하는 것을 N+1 문제라 한다.  
N+1이 발생하면 SQL이 상당히 많이 호출되므로 조회 성능에 치명적이다.   
이는 최우선 최적화 대상이고 N+1 문제는 JPQL 페치 조인으로 해결할 수 있다.   

<br />       

### 2.2 JPQL 페치 조인    
글로벌 페치 전략을 즉시 로딩으로 설정하면 애플리케이션 전체에 영향을 주므로 너무 비효율적이다.   
이번에는 JPQL을 호출하는 시점에 함께 로딩할 엔티티를 선택할 수 있는 페치 조인을 알아보자.    

* 페치 조인 사용 전    
```
JPQL: select o from Order o
SQL: select * from Order
```

* 페치 조인 사용 후 
```
JPQL:
    select o 
    from Order o
    join fetch o.member
    
SQL:
    select o.*, m.*
    from Order o
    join Member m on o.MEMBER_ID=m.MEMBER_ID
```

페치 조인은 조인 명령어 마지막에 fetch를 넣어주면 된다.   
페치 조인을 사용하면 SQL JOIN을 사용해서 페치 조인 대상까지 함께 조회한다.    
따라서 N+1 문제가 발생하지 않는다. (연관된 엔티티를 이미 로딩했으므로 글로벌 페치 전략은 무의미하다.)    

<br />   

### JPQL 페치 조인의 단점   
페치 조인이 현실적인 대안이긴 하지만 무분별하게 사용하면 화면에 맞춘 리포지토리 메소드가 증가할 수 있다.    
프리젠테이션 계층이 알게 모르게 데이터 접근 계층을 침범하는 것이다.     

예를 들어서 화면 A는 order 엔티티만 필요하다. 반면에 화면 B는 order 엔티티와 연관된 member 엔티티 둘 다 필요하다.     
결국 두 화면을 모두 최적화하기 위해 둘을 지연 로딩으로 설정하고 리포지토리에 다음 2가지 메소드를 만들었다.     


* 화면 A를 위해 order만 조회하는 `repository.findOrder()` 메소드    
* 화면 B를 위해 order와 연관된 member를 페치 조인으로 조회하는 repository.findOrderWithMember() 메소드    

이처럼 메소드를 각각 만들면 최적화는 할 수 있지만 **뷰와 리포지토리 간에 논리적인 의존관계가 발생**한다.       

다른 대안은 `repository.findOrder()` 하나만 만들고 여기서 페치 조인으로 order와 member를 함께 로딩하는 것이다.    
order 엔티티만 필요한 화면 B는 약간의 로딩 시간이 증가하겠지만        
페치 조인은 JOIN을 사용해서 쿼리 한번으로 필요한 데이터를 조회하므로 성능에 미치는 영향이 미비하다.          

무분별한 최적화로 프리젠테이션 계층과 데이터 접근 계층 간에 의존관계가 급격하게 증가하는 것보다는 적절한 선에서 타협점을 찾는 것이 합리적이다.      


<br />     

### 2.3 강제로 초기화     
```
class OrderService {
    @Transactional 
    public Order findOrder() {
        Order order = orderRepository.findOrder(id);
        order.getMember().getName(); // 프록시 객체를 강제로 초기화한다.  
        return order;
    }
}
```

글로벌 페치 전략을 지연 로딩으로 설정하면 연관된 엔티티를 실제 엔티티가 아니라 프록시 객체로 조회한다.     
프록시 객체는 실제 사용하는 시점에 초기화된다.     

`order.getMember()`까지만 호출하면 단순히 프록시 객체만 반환하고 아직 초기화하지 않는다.    
프록시 객체는 `member.getName()` 처럼 실제 값을 사용하는 시점에 초기화 된다.     

프리젠테이션 계층에서 필요한 프록시 객체를 영속성 컨텍스트가 살아 있을 때 강제로 초기화해서 반환하면 이미 초기화했으므로 준영속 상태에서도 사용할 수 있다.    
하이버네이트를 사용하면 `initialize()` 메소드를 사용해서 프록시를 강제로 초기화할 수 있다.    

위 예제처럼 프록시를 초기화하는 역할을 서비스 계층이 담당하면 뷰가 필요한 엔티티에 따라 서비스 계층의 로직을 변경해야 한다.    
은근 슬쩍 프리젠케이션 계층이 서비스 계층을 침범하는 상황이다.     
서비스 계층에서 프리젠테이션 계층을 위한 프록시 초기화 역할을 분리해야 하고 FACADE 계층이 그 역할을 담당해줄 것이다.   

<br />   

### 2.4 FACADE 계층 추가    
이는 프리젠테이션 계층과 서비스 계층 사이에 FACADE 계층을 하나 더 두는 방법이다.   
뷰를 위한 프록시 초기화는 이곳에서 담당한다.   

결과적으로 FACADE 계층을 도입해서 서비스 계층과 프리젠테이션 계층 사이에 논리적인 의존성을 분리할 수 있다.     


![스크린샷 2021-03-22 오후 4 27 07](https://user-images.githubusercontent.com/33855307/111954450-78b64d80-8b2b-11eb-8d37-fae409dec9a9.png)    

프록시를 초기화하려면 영속성 컨텍스트가 필요하므로 FACADE에서 트랜잭션을 시작해야 한다.     
<br />     

### FACADE 계층의 역할과 특징      
* 프리젠테이션 계층과 도메인 모델 계층 간의 논리적 의존성을 분리해준다.       
* 프리젠테이션 계층에서 필요한 프록시 객체를 초기화한다.     
* 서비스 계층을 호출해서 비즈니스 로직을 실행한다.     
* 리포지토리를 직접 호출해서 뷰가 요구하는 엔티티를 찾는다.     

```
class OrderFacade {
    @Autowired
    OrderService orderService;
    
    public Order findOrder(Long id) {
        Order order = orderService.findOrder(id);
        // 프리젠테이션 계층이 필요한 프록시 객체를 강제로 초기화한다. 
        order.getMember().getName();
        return order;
    }
}

class OrderService {
    public Order findOrder(Long id) {
        return orderRepository.findOrder(id);
    } 
}
```

FACADE 계층을 사용해서 서비스 계층과 프리젠테이션 계층 간에 논리적 의존관계를 제거했다.    
하지만 실용적 관점에서 FACADE의 최대 단점은 중간에 계층이 하나 더 끼어들고 많은 코드를 작성해야 하며 위임 코드가 상당히 많다는 점이다.    


<br />    

### 2.5 준영속 상태와 지연 로딩의 문제점      
지금까지 준영속 상태일 때 지연 로딩 문제를 극복하기 위해 여러 방법을 알아봤지만    
뷰를 개발할 때 필요한 엔티티를 미리 초기화하는 방법은 생각보다 오류가 발생할 가능성이 높다.      
초기화되어 있는지 아닌지 확인하기 위해 FACADE나 서비스 클래스까지 열어보는 것은 번거롭고 놓치지 쉽기 때문이다.    

애플리케이션 로직과 뷰가 물리적으로는 나누어져 있지만 논리적으로 서로 의존한다는 문제가 있다.   
FACADE를 사용해서 이런 문제를 어느 정도 해소할 수는 있지만 상당히 번거롭다.    
예를들어 주문 엔티티와 연관된 회원 엔티티를 조회할 때 화면별로 최적화된 엔티티를 딱딱 맞아떨어지게          
초기화해서 조회하려면 FACADE 계층에 여러 종류의 조회 메소드가 필요하다.        

* 화면 A는 order만 필요하다.    
    - 조회 메소드: `getOrder()`    
* 화면 B는 order, order.member가 필요하다.     
    - 조회 메소드: `getOrderWithMember()`         
* 화면 C는 order, order.orderItems가 필요하다.            
    - 조회 메소드: `getOrderWithOrderItems()`           

<br />      

결국 모든 문제는 엔티티가 프리젠테이션 계층에서 준영속 상태이기 때문에 발생한다.      
영속상 컨텍스트를 뷰까지 살아있게 열어두자.     
그럼 뷰에서도 지연 로딩을 사용할 수 있는데 이것이 OSIV다.    


<br />       


# 3. OSIV      
OSIV(Open Session View)는 영속성 컨텍스트를 뷰까지 열어둔다는 뜻이다.    
영속성 컨텍스트가 살아있으면 엔티티는 영속 상태로 유지된다.   
따라서 뷰에서도 지연 로딩을 사용할 수 있다.      

> OSIV는 하이버네이트에서 사용하는 용어고 JPA에서는 OEIV라고 부른다.   


<br />    

### 3.1 과거 OSIV: 요청 당 트랜잭션     
가장 단순한 구현 방법은 클라이언트의 요청이 들어오자마자 서블릿 필터나 스프링 인터셉터에서 트랜잭션을 시작하고 요청이 끝날 때 트랜잭션도 같이 끝내는 것이다.      

![스크린샷 2021-03-22 오후 9 40 45](https://user-images.githubusercontent.com/33855307/111991117-491d3a80-8b57-11eb-8a21-7fdad1933b2f.png)    

요청이 들어오자마자 영속성 컨텍스트를 만들어서 트랜잭션을 시작하고 요청이 끝날 때 트랜잭션과 영속성 컨텍스트를 함께 종료한다.
<br />    

### 요청 당 트랜잭션 방식의 OSIV 문제점      
컨트롤러나 뷰 같은 프리젠테이션 계층이 엔티티를 변경할 수 있다.    
예를 들어 고객 예제를 출력해야 하는데 보안상의 이유로 고객 이름을 XXX로 변경해서 출력해야 한다고 가정하자.   

```
class MemberController {
    public String viewMember(Long id) {
        Member member = memberService.getMember(id);
        member.setName("XXX");
        model.addAttribute("member", member);
        ...
    }
}
```

데이터베이스에 있는 고객 이름까지 변경하고 싶은 것은 아니었지만 요청 당 트랜잭션 방식의 OSIV는 뷰를 렌더링한 후에 트랜잭션을 커밋한다.      
트랜잭션을 커밋하면 영속성 컨텍스트를 플러시하고 이때 영속성 컨텍스트의 변경 감지 기능이 작동해서 변경된 엔티티를 데이터베이스에 반영해버린다.        
<br />   

프리젠테이션 계층에서 엔티티를 수정하지 못하게 막는 방법들은 다음과 같다.    

* 엔티티를 읽기 전용 인터페이스로 제공   
  이 방법은 엔티티를 직접 노출하는 대신 읽기 전용 메소드만 제공하는 인터페이스를 제공하는 방법이다.        

```
interface MemberView {
    public String getName();
}

@Entity
class Member implements MemberView {
    ...
}

class MemberService {
    public MemberView getMember(Long id) {
        return memberRepository.findById(id);
    }
}
```  


* 엔티티 래핑    
  이 방법은 엔티티의 읽기 전용 메소드만 가지고 있는 엔티티를 감싼 객체를 만들고 이것을 프리젠테이션 계층에 반환하는 방법이다.   
  
```
class MemberWeapper {
    private Member member;
    
    public MemberWrapper(Member member) {
        this.member = member;
    }
    
    // 읽기 전용 메소드만 제공 
    public String getName() {
        return member.getName();
    }
}
```
  
  
* DTO만 반환    
  가장 전통적인 방법인데 엔티티 대신 단순 데이터만 전달하는 객체인 DTO를 생성해서 반환하는 것이다.    
  하지만 이 방법은 OSIV를 사용하는 장점을 살릴 수 없고 엔티티를 거의 복사한 듯한 DTO 클래스도 하나 더 만들어야 한다.     
  
위 방법 전부 코드량이 상당히 증가한다는 단점이 있다.     

최근에는 이런 문제점을 어느정도 보완해서 비즈니스 계층에서만 트랜잭션을 유지하는 방식의 OSIV를 사용한다.   
스프링 프레임워크가 제공하는 OSIV가 바로 이 방식을 사용하는 OSIV다.      

<br />          

### 3.2 스프링 OSIV: 비즈니스 계층 트랜잭션    
스프링 프레임워크의 spring-orm.jar는 다양한 OSIV 클래스를 제공한다.    
OSIV를 서블릿 필터에 적용할지 스프링 인터셉터에 적용할지에 따라 원하는 클래스를 선택해서 사용하면 된다.    

* 하이버네이트 OSIV 서블릿 필터      
* 하이버네이트 OSIV 스프링 인터셉터    
* JPA OEIV 서블릿 필터     
* JPA OEIV 스프링 인터셉터     

<br />   

### 스프링 OSIV 분석    
스프링 프레임워크가 제공하는 OSIV는 "비즈니스 계층에서 트랜잭션을 사용하는 OSIV"다.    
OSIV를 사용하기는 하지만 트랜잭션은 비즈니스 계층에서만 사용한다는 뜻이다.    

![스크린샷 2021-03-22 오후 10 14 37](https://user-images.githubusercontent.com/33855307/111995207-0316a580-8b5c-11eb-94ec-2bd99a27db96.png)    

1. 클라이언트의 요청이 들어오면 서블릿 필터나 스프링 인터셉터에서 영속성 컨텍스트를 시작   
단 이때 트랜잭션은 시작하지 않는다.        
<br />        
   
2. 서비스 계층에서 `@Transactional`로 트랜잭션을 시작할 때 1번에서 미리 생성해둔 영속성 컨텍스트를 찾아와서 트랜잭션을 시작한다.   
<br />              
   
3. 서비스 계층이 끝나면 트랜잭션을 커밋하고 영속성 컨텍스트를 플러시한다.    
이때 트랜잭션은 끝내지만 영속성 컨텍스트는 종료하지 않는다.     
<br />

4. 컨트롤러와 뷰까지 영속성 컨텍스트가 유지되어 조회한 엔티티는 영속 상태를 유지한다.      
<br />   
   
5. 서블릿 필터나 스프링 인터셉터로 요청이 돌아오면 영속성 컨텍스트를 종료한다.   
이때 플러시를 호출하지 않고 바로 종료한다.      
   
<br />       



### 트랜잭션 없이 읽기      
영속성 컨텍스트를 통한 모든 변경은 트랜잭션 안에서 이루어져야 한다.   
만약 트랜잭션 없이 변경하고 영속성 컨텍스트를 플러시하면 `javax.persistence.TransactionRequiredException` 예외가 발생한다.     

* 영속성 컨텍스트는 트랜잭션 범위 안에서 엔티티를 조회하고 수정할 수 있다.     
* 영속성 컨텍스트는 트랜잭션 범위 밖에서 엔티티를 조회만 할 수 있다.     
이것을 트랜잭션 없이 읽기라 한다.     

스프링이 제공하는 OSIV를 사용하면 프리젠테이션 계층에서는 트랜잭션이 없어 엔티티를 수정할 수 없다.    
따라서 기존 OSIV의 단점을 보완했고 트랜잭션 없이 읽기를 사용해서 지연 로딩 기능을 사용할 수 있게 되었다.    

<br />  

```
member.setName("XXX"); 
```

위 코드를 실행해도 데이터베이스가 변경되지 않는, 즉 플러시가 동작하지 않는 이유는 다음과 같다.   

* 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영하려면 영속성 컨텍스트를 플러시해야 한다.    
하지만 서비스 계층이 끝날 때 트랜잭션이 커밋되면서 이미 플러시해버렸다.    
스프링이 제공하는 OSIV는 요청이 끝나면 플러시를 호출하지 않고 `em.close()`로 영속성 컨텍스트만 종료해 버리므로 플러시가 일어나지 않는다.    


* 프리젠테이션 계층에서 `em.flush()`를 호출해서 강제로 플러시해도 트랜잭션 범위 밖이므로 데이터를 수정할 수 없다는 예외를 만난다.     


<br />    

### 스프링 OSIV 주의사항   
스프링 OSIV를 사용하면 프리젠테이션 계층에서 엔티티를 수정해도 수정 내용이 데이터베이스에 반영되지 않는다.    
그러나 한 가지 예외가 있다.   
프리젠테이션 계층에서 엔티티를 수정한 직후에 트랜잭션을 시작하는 서비스 계층을 호출하면 문제가 발생한다.   

```
member.setName("XXX"); 
```

위 코드 실행 후 트랜잭션이 있는 비즈니스 로직이 실행되면   
트랜잭션 AOP가 동작하면서 영속성 컨텍스트에 트랜잭션을 시작한다.    
메소드가 끝나면 트랜잭션 AOP는 트랜잭션을 커밋하고 영속성 컨텍스트를 플러시하는데 이때 변경 감지가 동작하면서   
Member 엔티티의 수정 사항을 데이터베이스에 반영한다.     

<br />   

보통 컨트롤러는 비즈니스 로직을 먼저 호출하고 결과를 조회하는 순서로 실행하기 때문에 이런 문제는 거의 발생하지 않는다.   

```
memberService.biz(); // 비즈니스 로직 실행 

Member member = memberService.getName(id);
member.setName("XXX");
```

스프링 OSIV는 같은 영속성 컨텍스트를 여러 트랜잭션이 공유할 수 있으므로 이런 문제가 발생한다.    
OSIV를 사용하지 않는 트랜잭션 범위의 영속성 컨텍스트 전략은 트랜잭션의 생명주기와 영속성 컨텍스트의 생명주기가 같으므로 이런 문제가 발생하지 않는다.      


