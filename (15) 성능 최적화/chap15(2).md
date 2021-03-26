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

* 요약  
  트랜잭션 사용 X -> 플러시가 일어나지 않음 -> 조회 성능 향상
    - 읽기 전용 트랜잭션 사용: 플러시를 작동하지 않도록 해서 성능 향상
    - 읽기 전용 엔티티 사용: 엔티티를 읽기 전용으로 조회해서 메모리 절약

    
읽기 전용 데이터를 조회할 때, 메모리를 최적화하려면 스칼라 타입으로 조회하거나 하이버네이트가 제공하는 읽기 전용 쿼리 힌트를 사용하면 되고   
플러시 호출을 막아서 속도를 최적화하려면 읽기 전용 트랜잭션을 사용하거나 트랜잭션 밖에서 읽기를 사용하면 된다.   
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
