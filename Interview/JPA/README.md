# JPA  
<br />      

## ORM이란?  
Object Relational Mapping의 약자로 객체와 관계형 데이터베이스의 데이터를 자동으로 매핑해주는 것이다.     

#### 장점
* 개발자가 객체 모델만 이용해 프로그래밍에 집중할 수 있다.     
* 재사용, 유지보수, 리팩토링이 용이하다.   
* DBMS 종속성이 낮다.   

#### 단점
잘못된 설계는 속도 저하, 일관성을 무너뜨리는 문제점들을 발생시킬 수 있다.   

<br />
<br />

## 영속성 컨텍스트란?
엔티티를 영구 저장하는 환경이다.   
엔티티 매니저를 생성하면 자동으로 영속성 컨텍스트가 생성되고 엔티티를 관리하고 보관할 수 있다.    

#### 1) 1차 캐시 
영속 상태의 엔티티는 모두 1차 캐시에 저장되고 1차 캐시는 `@Id`를 키로 가지고 있는 Map이 존재한다.      
엔티티를 조회할 때 바로 DB에 접근하지 않고 1차 캐시에 있는 데이터를 먼저 조회한 후 다시 1차 캐시에 저장한다.      

#### 2) 동일성 보장 (같은 객체를 참조)
모든 엔티티의 데이터들은 1차 캐시에 저장되기 때문에   
식별자가 동일한 엔티티의 경우 동일성이 보장된다.   

#### 3) 트랜잭션을 지연하는 쓰기 지연   
영속성 컨텍스트에서 DML이 발생했을 떄, 바로 DB에 저장하지 않고   
트랜잭션이 커밋될 때 영속성 컨텍스트의 쓰기 지연 SQL 저장소에 모아둔 쿼리들을 한 번에 저장한다.  

#### 4) 변경 감지 
영속성 컨텍스트의 1차 캐시에는 스냅샷을 통해 엔티티의 변경을 감지한다.    
변경 감지는 영속 상태의 엔티티에만 적용된다.   

<br />
<br />

## JPA Query Cache  
* 쿼리 캐시는 쿼리와 파라미터 정보를 키로 사용해서 쿼리 결과를 캐시하는 방법이다.      
* 쿼리 캐시는 결과 집합의 식별자 값만 캐시한다.  

쿼리 캐시를 적용하려면 영속성 유닛 설정에 `hibernate.cache.use_query_cache` 옵션을 `true`로 설정해야 하고   
쿼리 캐시를 적용하려는 쿼리마다 `org.hibernate.cacheable`을 `true`로 설정하는 힌트를 주면 된다.   

```java  
// 쿼리 캐시 적용
em.createQuery("select i from Item i", Item.class)
	.setHint("org.hibernate.cacheable", true)
    .getResultList();


// NamedQuery에 쿼리 캐시 적용
@Entity
@NamedQuery(
	hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"),
    name = "Member.findByUsername",
    query = "select m.address from Mebmer m where m.name = :username"
)


public class Member {
	....
}
```
