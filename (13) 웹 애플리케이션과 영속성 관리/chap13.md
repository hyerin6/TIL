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
        // 반환된 member 엔티티는 준영속 상태다. 
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

