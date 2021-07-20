# 4. 성능 최적화     

## 4.1 N+1 문제    
JPA로 애플리케이션 개발할 때 성능상 가장 주의해야 하는 것이 N+1 문제다.     

* Member 엔티티의 일부 
```
@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
private List<Order> orders = new ArrayList<Order>();
```

Member(회원)와 Order(주문)은 1:N, N:1 양방향 연관관계다.    
회원이 참조하는 주문정보인 Member.orders는 즉시 로딩으로 설정했다.      

<br />      

### 즉시 로딩과 N+1    
특정 회원 하나를 `em.find()` 메소드로 조회하면 즉시 로딩으로 설정한 주문정보도 함께 조회한다.      

```
// CODE
em.find(Member.class, id);

// SQL 
SELECT M.*, O.*
FROM MEMBER M
OUTER JOIN ORDERS O ON M.ID = O.MEMBER.ID
```

SQL을 두 번 실행하는 것이 아니라 조인을 사용해서 한 번의 SQL로 회원과 주문정보를 함께 조회한다.    
문제는 JPQL을 사용할 때 발생한다.   

```
em.createQuery("SELECT m FROM MEMBER m", Member.class)
```

JPQL을 실행하면 JPA는 이것을 분석해서 SQL을 실행한다.     
이때는 즉시로딩과 지연로딩에 대해서 전혀 신경쓰지 않고 JPQL만 사용해서 SQL을 생성한다.      
<br />       

```
SELECT * FROM MEMBER // (1)
SELECT * FROM ORDERS WHERE MEMBER_ID=? // (2)
```

(2) 부분은 (1)에서 조회된 회원 수 만큼 조회된다.      
이처럼 처음 실행한 SQL의 결과 수만큼 추가로 SQL을 실행하는 것을 N+1 문제라한다.   
즉시 로딩은 JPQL을 실행할 때 N+1 문제가 발생할 수 있다.      

<br />    

### 지연 로딩 N+1     
지연 로딩으로 설정하면 어떻게 될까?     
지연 로딩이나 즉시 로딩이나 N+1 문제에서 자유로울 수는 없다.      

지연 로딩으로 설정하면 JPQL에서는 N+1 문제가 발생하지 않는다.     
```
em.createQuery("SELECT m FROM MEMBER m", Member.class);
```
지연 로딩이므로 회원만 조회한다.    
<br />   

이후 비즈니스 로직에서 주문 컬렉션을 실제 사용할 때 지연 로딩이 발생한다.       
```
Member member = members.get(0);
member.getOrders().size(); // 지연 로딩 초기화 
```
<br />     

`members.get(0)`로 회원만 조회해서 사용했기 때문에    
`member.getOrders().size()`를 호출하면 실행되는 SQL은 다음과 같다.      
```
SELECT * FROM ORDERS WHERE MEMBER_ID=?
```
<br />   

문제는 다음과 같이 모든 회원에 대한 연관된 주문 컬렉션을 사용할 때 발생한다.   
```
for(Member member : members) {
    // 지연 로딩 한다. 
    System.out.println("member=" + member.getOrders().size());
}
```

주문 컬렉션을 초기화하는 수만큼 SQL이 실행될 수 있다.    
이것도 결국 N+1 문제다.    

<br />    

### 페치 조인 사용    
페치 조인은 SQL 조인을 사용해서 연관된 엔티티를 함께 조회하므로 N+1 문제가 발생하지 않는다.     
자음은 페치 조인을 사용하는 JPQL이다.      
```
select m from Member m join fetch m.orders 
```

다음은 JPQL을 실행해서 실제도 실행된 SQL이다.     
```
select m.*, 0.* from Member M 
inner join Orders o on m.id=o.member_id
```

이 예제는 일대다 조인을 했으므로 결과가 늘어나서 중복된 결과가 나타날 수 있다.    
따라서 JPQL의 DISTINCT를 사용해서 중복을 제거하는 것이 좋다.     

<br />      

### 하이버네이트 `@BatchSize`          
하이버네이트가 제공하는 `@BatchSize` 어노테이션을 사용하면 연관된 엔티티를 조회하는데 지정한 size 만큼 SQL의 IN절을 사용해서 조회한다.     
만약 조회한 회원이 10명인데 size=5로 지정하면 2번의 SQL만 추가로 실행한다.   

즉시 로딩으로 설정하면 조회 시점에 10건의 데이터를 모두 조회해야 하므로 SQL이 두 번 실행된다.    
```
SELECT * FROM ORDERS 
WHERE MEMBER_ID IN (
    ?, ?, ?, ?, ?
)
```

지연 로딩으로 설정하면 엔티티를 최초 사용하는 시점에 위 SQL을 실행해서 5건의 데이터를 미리 로딩해둔다.     
그리고 6번째 데이터를 사용하면 SQL을 추가로 실행한다.    

<br />   

### 하이버네이트 `@Fetch(FetchMode.SUBSELECT)`      
하이버네이트가 제공하는 `@Fetch` 어노테이션에 FetchMode를 SUBSELECT로 사용하면   
연관된 데이터를 조회할 때 서브 쿼리를 사용해서 N+1 문제를 해결한다.    

다음 JPQL로 회원 식별자 값이 10을 초과하는 회원을 모두 조회해보자.    
```
select m from Member m where m.id > 10
```

즉시 로딩으로 설정하면 조회 시점에 지연 로딩으로 설정하면   
지연 로딩된 엔티티를 사용하는 시점에 SQL이 실행된다.
```
select o from Orders o
where o.member_id in(
    select m.id from Member m
    where m.id > 10
)
```

<br />     

### N+1 정리    
* `@OneToOne`, `@ManyToOne`: 기본 페치 전략은 즉시 로딩    
* `@OneToMany`, `@ManyToMany`: 기본 페치 전략은 지연 로딩    

따라서 즉시 로딩인 `@OneToOne`, `@ManyToOne`은 `fetch = FetchType.LAZY`로 설정해서 지연 로딩 전략을 사용하도록 변경하자.    


<br />     

## 4.2 읽기 전용 쿼리의 성능 최적화          
엔티티가 영속성 컨텍스트에 관리되면 1차 캐시부터 변경 감지까지 얻을 수 있는 혜택이 많다.     
하지만 영속성 컨텍스트는 변경 감지를 위해 스냅샷 인스턴스를 보관하므로 더 많은 메모리를 사용한다는 단점이 있다.    

**예)** 100건의 구매 내용을 출력하는 단순한 조회 화면     
위 예시에서는 수정과 재조회를 할 필요가 없다.      
이때는 읽기 전용으로 엔티티를 조회하면 메모리 사용량을 최적화할 수 있다.    

다음 JPQL 쿼리를 최적화해보자. 
```
select o from Order o
```

<br />   

### 스칼라 타입으로 조회    
가장 확실한 방법은 엔티티가 아닌 스칼라 타입으로 모든 필드를 조회하는 것이다.    
스칼라 타입은 영속성 컨텍스트가 결과를 관리하지 않는다.
```
select o.id, o.name, o.price from Order o
```

<br />   


### 읽기 전용 쿼리 힌트 사용    
하이버네이트 전용 힌트인 `org.hibernate.readOnly`를 사용하면   
읽기 전용으로 영속성 컨텍스트는 스냅샷을 보관하지 않는다.    
따라서 메모리 사용량 최적화가 가능하고 변경 감지는 안된다.   

<br />    

### 읽기 전용 트랜잭션 사용    
```
@Transactional(readOnly = true)
```

이렇게 설정하면 스프링 프레임워크가 하이버네이트 세션의 플러시 모드를 MANUAL로 설정한다.   
이렇게 하면 강제로 플러시를 호출하지 않는 이상 플러시가 일어나지 않는다.    
영속성 컨텍스트를 플러시하지 않으므로 엔티티 등록, 수정, 삭제가 동작하지 않는다.    
플러시할 때 일어나는 스냅샷 비교같은 무거운 로직들을 수행하지 않으므로 성능이 향상된다.       

<br />       

### 트랜잭션 밖에서 읽기       
트랜잭션 밖에서 읽기란 트랜잭션 없이 엔티티를 조회한다는 뜻이다.      
JPA는 데이터를 변경하려면 트랜잭션은 필수다. 따라서 이 조회는 조회가 목적일 때만 사용해야 한다.            
스프링 프레임워크는 다음처럼 설정한다.       
```
@Transactional(propagation = Propagation.NOT_SUPPORTED)
```

이렇게 트랜잭션을 사용하지 않으면 플러시가 일어나지 않으므로 조회 성능이 향상된다.   

기본적으로 플러시 모드는 AUTO로 설정되어 있어 트랜잭션을 커밋하거나 쿼리를 실행하면 플러시가 작동한다.   
그런데 트랜잭션 자체가 존재하지 않으므로 트랜잭션을 커밋할 일이 없다.    
그리고 JPQL 쿼리도 트랜잭션 없이 실행하면 플러시를 호출하지 않는다.    
<br />   

* 요약    
  트랜잭션 사용 X -> 플러시가 일어나지 않음 -> 조회 성능 향상    
<br />       
    - 읽기 전용 트랜잭션 사용: 플러시를 작동하지 않도록 해서 성능 향상
    - 읽기 전용 엔티티 사용: 엔티티를 읽기 전용으로 조회해서 메모리 절약   
<br />       
    - 메모리 최적화: 스칼라 타입으로 조회 or 읽기 전용 쿼리 힌트를 사용   
    - 플러시 호출막아 속도 최적화: 읽기 전용 트랜잭션을 사용 or 트랜잭션 밖에서 읽기를 사용    

<br />        
따라서 다음과 같이 읽기 전용 트랜잭션(또는 트랜잭션 밖에서 읽기)과 읽기 전용 쿼리 힌트(또는 스칼라)를 동시에 사용하는 것이 가장 효과적이다.   

```
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 
public List<DataEntity> findDatas() {
    return em.createQuery("select d from DataEntity d", DataEntity.class)
    .setHint("org.hibernate.readOnly", true) // 읽기 전용 쿼리 힌트 
    .getResultList();
}
```



<br />      

## 4.3 배치 처리    
수백만 건의 데이터를 배치 처리해야 하는 상황이라 가정해보자.     
일반적인 방법으로 엔티티를 계속 조회하면 영속성 컨텍스트에 아주 많은 엔티티가 쌓이면서 메모리 부족 오류가 발생한다.    
따라서 이런 배치 처리는 적절한 단위로 영속성 컨텍스트를 초기화해야 한다.    
또한 2차 캐시를 사용하고 있다면 2차 캐시에 엔티티를 보관하지 않도록 주의해야 한다.     

<br />        

### JPA 등록 배치    
수천~수만건 이상의 엔티티를 한 번에 등록할 때 주의할 점은 영속성 컨텍스트에 엔티티가 계속 쌓이지 않도록      
일정 단위마다 영속성 컨텍스트의 엔티티를 데이터베이스에 플러시하고 영속성 컨텍스트를 초기화해야 한다.     
N건을 저장할 때마다 플러시를 호출하고 영속성 컨텍스트를 초기화한다.     
<br />    

### JPA 수정 배치 처리      
배치 처리는 아주 많은 데이터를 조회해서 수정한다.    
이때 수많은 데이터를 한번에 메모리에 올려둘 수 없어서 2가지 방법을 주로 사용한다.    

* 페이징 처리: 데이터베이스 페이징 기능을 사용한다.    
* 커서(cursor): 데이터베이스가 지원하는 커서 기능을 사용한다.    
<br />      

### JPA 페이징 배치 처리    
한 번에 100건씩 페이징 쿼리로 조회하면서 상품의 가격을 100원씩 증가한다.   
페이지 단위마다 영속성 컨텍스트를 플러시하고 초기화시킨다.   

JPA는 JDBC 커서를 지원하지 않는다. 따라서 커서를 사용하려면 하이버네이트 세션을 사용해야 한다.              
<br />             

### 하이버네이트 scroll 사용    
하이버네이트는 scroll이라는 이름으로 JDBC 커서를 지원한다.      

```
Session session = em.unwrap(Session.class);

ScrollableResults scroll = session.createQuery
  ("select p from Product p")
  .setCacheMode(CacheMode.IGNORE) // 2차 캐시 기능을 끈다. 
  .scroll(ScrollMode.FORWARD_ONLY);
  
while(scroll.next()) {
    ...
}

...

```

scroll은 하이버네이트 전용 기능이므로 먼저 `em.unwrap()` 메소드를 사용해서 하이버네이트 세션을 구한다.    
다음으로 쿼리를 조회하면서 `scroll()` 메소드로 `ScrollableResults` 객체를 반환받는다.   
이 객체의 `next()` 메소드를 호출하면 엔티티를 하나씩 조회할 수 있다.   
<br />      


### 하이버네이트 무상태 세션 사용      
하이버네이트 무상태 세션이라는 특별한 기능을 제공한다. 이름 그대로 무상태 세션은 영속성 컨텍스트를 만들지 않고 심지어 2차 캐시도 사용하지 않는다.     
무상태 세션은 영속성 컨텍스트가 없다. 그리고 엔티티를 수정하려면 무상태 세션이 제공하는 `update()` 메소드를 직접 호출해야 한다.    

```
SessionFactory sessionFactory = 
  entityManagerFactory.unwrap(SessionFactory.class);
StatelessSession session = sessionFactory.openStatelessSession();
Transaction tx = session.beginTransaction();
ScrollableResults scroll = session.createQuery("select p from Product p").scroll();

while(scroll.next()) {
    Product p = (Product) scroll.get(0);
    p.setPrice(p.getPrice() + 100);
    session.update(p); // 직접 update를 호출해야 한다. 
}

tx.commit();
session.close();
```

하이버네이트 무상태 세션은 일반 하이버네이트 세션과 거의 비슷하지만 영속성 컨텍스트가 없다.    
따라서 영속성 컨텍스트를 플러시하거나 초기화하지 않아도 된다.   
대신 엔티티를 수정할 때 `update()` 메소드를 직접 호출해야 한다.   
<br />     

## 4.4 SQL 쿼리 힌트 사용      
JPA는 데이터베이스 SQL 힌트 기능을 제공하지 않는다.    
SQL 힌트를 사용하려면 하이버네이트를 직접 사용해야 한다.    

> 여기서 말하는 SQL 힌트는 JPA 구현체에게 제공하는 힌트가 아니다.    
> 데이터베이스 벤더에게 제공하는 힌트다.   

<br />             

SQL 힌트는 하이버네이트 쿼리가 제공하는 `addQueryHint()` 메소드를 사용한다.    

```
Session session = em.unwrap(Session.class); // 하이버네이트 직접 사용  
List<Member> list = session.createQuery("select m from Member m") 
      .addQueryHint("FULL (MEMBER)") // SQL HINT 추가 
      .list();
```

실행된 SQL은 다음과 같다.   
```
select 
  /*+ FULL (MEMBER) */ m.id, m.name
from 
  Member m
```

<br />       

## 4.5 트랜잭션을 지원하는 쓰기 지연과 성능 최적화      

### 트랜잭션을 지원하는 쓰기 지연과 JDBC 배치   
```
insert(member1); // INSERT INTO ...
insert(member2);
insert(member3);
insert(member4);
insert(member5);

commit();
```

네트워크 호출 한 번은 단순한 메소드를 수만 번 호출하는 것보다 더 큰 비용이든다.    
이 코드는 5번의 INSERT SQL과 1번의 커밋으로 총 6번 데이터베이스와 통신한다.    
이것을 최적화하려면 5번의 INSERT SQL을 모아서 한 번에 데이터베이스로 보내면 된다.    

JDBC가 제공하는 SQL 배치 기능을 사용하면 SQL을 모아서 데이터베이스에 한 번에 보낼 수 있는데   
이 기능을 사용하려면 코드의 많은 부분을 수정해야 한다.   
특히 비즈니스 로직이 복잡하게 얽혀 있는 곳에서 사용하기는 쉽지 않고 적용해도 코드가 상당히 지저분해진다.   

JPA는 플러시 기능이 있으므로 SQL 배치 기능을 효과적으로 사용할 수 있다.    

> 엔티티가 영속 상태가 되려면 식별자가 꼭 필요하다.    
> 그런데 IDENTITY 식별자 생성 전략은 엔티티를 데이터베이스에 저장해야 식별자를 구할 수 있으므로      
> `em.persist()`를 호출하는 즉시 INSERT SQL이 데이터베이스에 전달된다.       
> 따라서 쓰기 지연을 활용한 성능 최적화를 할 수 없다.   

<br />  

### 트랜잭션을 지원하는 쓰기 지연과 애플리케이션 확장성    
트랜잭션을 지원하는 쓰기 지연과 변경 감지 기능 덕분에 성능과 개발의 편의성이라는 장점을 얻을 수 있었다.     
진짜 장점은 데이터베이스 테이블 로우(row)에 락(lock)이 걸리는 시간을 최소화한다는 점이다.      

이 기능은 트랜잭션을 커밋해서 영속성 컨텍스트를 플러시하기 전까지는 데이터베이스에 데이터를 등록, 수정, 삭제하지 않는다.   
따라서 커밋 직전까지 데이터베이스 로우에 락을 걸지 않는다.             
<br />                   

```
update(memberA); // UPDATE SQL A 
비즈니스로직A(); // UPDATE SQL ...
비즈니스로직B(); // INSERT SQL ...
commit();
```

JPA를 사용하지 않고 SQL을 직접 다루면 `update(memberA)`를 호출할 때 UPDATE SQL을 실행하면서 데이터베이스 테이블 로우에 락을 건다.    
이 락은 비즈니스로직 A(), 비즈니스로직 B()를 모두 수행하고 `commit()`을 호출할 때까지 유지된다.   
트랜잭션 격리 수준에 따라 다르지만 보통 많이 사용하는 커밋된 읽기를 수정하려는 다른 트랜잭션은 락이 풀릴 때까지 대기한다.    

JPA는 커밋을 해야 플러시를 호출하고 데이터베이스에 수정 쿼리를 보낸다.      
위 코드에서 `commit()`을 호출할 때 UPDATE SQL을 실행하고 바로 데이터베이스 트랜잭션을 커밋한다.    
쿼리를 보내고 바로 트랜잭션을 커밋하고 결과적으로 데이터베이스에 락이 걸리는 시간을 최소화한다.    

데이터베이스 락은 애플리케이션 서버 증설만으로 해결할 수 없다. 오히려 애플리케이션 서버를 증설해서 트랜잭션이 증가할수록 더 많은 데이터베이스 락이 걸린다.    
JPA의 쓰기 지연 기능은 데이터베이스에 락이 걸리는 시간을 최소화해서 동시에 더 많은 트랜잭션을 처리할 수 있다는 장점이 있다.    

