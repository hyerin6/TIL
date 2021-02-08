## 객체 매핑    

![스크린샷 2021-01-13 오후 12 28 51](https://user-images.githubusercontent.com/33855307/104402991-e88fe100-559a-11eb-9c25-56bedfda612f.png)     


여기서 `@Entity`, `@Table`, `@Column`이 매핑 정보다.     
JPA는 매핑 어노테이션을 분석해서 회원 클래스에 사용한 매핑 어노테이션을 분석해서 어떤 객체가 어떤 테이블과 관계가 있는지 알아낸다.    

* `@Entity`   
이 클래스를 테이블과 매핑한다고 JPA에게 알려준다. 이렇게 `@Entity`가 사용된 클래스를 엔티티 클래스라고 한다.     

* `@Table`    
엔티티 클래스에 매핑할 테이블 정보를 알려준다.   
이 어노테이션을 생략하면 클래스 이름을 테이블 이름으로 매핑한다.   

* `@Id`      
엔티티 클래스의 필드를 테이블의 기본 키(primary key)에 매핑한다. 여기서는 엔티티의 id 필드를 테이블의 ID 기본 키 컬럼에 매핑했다.    
이렇게 `@Id`가 사용된 필드를 식별자 필드라 한다.      

* `@Column`        
필드를 컬럼에 매핑한다. 여기서 name 속성을 사용해서 Member 엔티티의 username 필드를 MEMBER 테이블의 NAME 컬럼에 매핑했다.   

* 매핑 정보가 없는 필드     
age 필드에는 매핑 어노테이션이 없다. 이렇게 매핑 어노테이션을 생략하면 필드명을 사용해서 컬럼명으로 매핑한다.      
여기서 필드명이 age이기 때문에 age 컬럼으로 매핑한다. (지금은 대소문자 구분하지 않는다고 가정)     
만약 대소문자를 구분하는 디비를 사용한다면 `@Column(name="AGE")`처럼 명시적으로 매핑해야 한다.       
<br />     


## `persistence.xml` 설정        

* JPA는 persistence.xml을 사용해서 필요한 설정 정보를 관리         
* META-INF/persistence.xml 클래스 패스 경로에 있으면 별도 설정없이 JPA 인식할 수 있다.         

```
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">

    <persistence-unit name="jpabook">

        <properties>

            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect" />

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true" />
            <property name="hibernate.format_sql" value="true" />
            <property name="hibernate.use_sql_comments" value="true" />
            <property name="hibernate.id.new_generator_mappings" value="true" />

            <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
        </properties>
    </persistence-unit>

</persistence>
```

* JPA 설정은 영속성 유닛(persistence-unit)부터 시작   
일반적으로 연결할 데이터베이스당 하나의 영속성 유닛을 등록한다.   
그리고 영속성 유닛에는 고유한 이름을 부여해야 하는데 여기서 jpabook이라는 이름을 사용했다.   

* JPA 표준 속성      
    - javax.persistence.jdbc.driver : JDBC 드라이버  
    - javax.persistence.jdbc.user : 데이터베이스 접속 아이디  
    - javax.persistence.jdbc.password : 데이터베이스 접속 비밀번호  
    - javax.persistence.jdbc.url : 데이터베이스 접속 URL  

* 하이버네이트 속성   
    - hibernate.dialect : 데이터베이스 방언 설정    
<br />             
    
#### 데이터베이스 방언           
JPA는 특정 데이터베이스에 종속적이지 않은 기술이다.               
다른 데이터베이스로 손쉽게 교체할 수 있다.                  

데이터베이스마다 차이점          
- 데이터 타입         
- 다른 함수명         
- 페이징 처리          

SQL 표준을 지키지 않거나 특정 데이터베이스만의 고유한 기능을 JPA에서는 "Dialect(방언)"이라고 한다.        
하이버네이트를 포함해서 대부분 JPA 구현체들은 이런 문제를 해결하려고 다양한 데이터베이스 방언 클래스를 제공한다.             
<br />       


#### 하이버네이트 설정 옵션             

**옵션**            
* hibernate.show_sql : 실행한 SQL을 출력                   
* hibernate.format_sql : SQL을 보기 좋게 정렬함.               
* hibernate.use_sql_comments : 쿼리 출력 시 주석도 함께 출력                   
* hibernate.id.new_generator_mappings : JPA 표준에 맞는 새로운 키 생성 전략을 사용함.               

**하이버네이트 설정**             
* create : Session factory가 실행될 때에 스키마를 지우고 다시 생성. 클래스패스에 import.sql 이 존재하면 찾아서, 해당 SQL도 함께 실행함.                   
* create-drop : create와 같지만 session factory가 내려갈 때 스키마 삭제.         
* update : 시작시, 도메인과 스키마 비교하여 필요한 컬럼 추가 등의 작업 실행. 데이터는 삭제하지 않음.           
* validate : Session factory 실행시 스키마가 적합한지 검사함. 문제가 있으면 예외 발생.           
개발시에는 create가, 운영시에는 auto 설정을 빼거나 validate 정도로 두는 것이 좋아 보인다.              
update로 둘 경우에, 개발자들의 스키마가 마구 꼬여서 결국은 drop 해서 새로 만들어야 하는 사태가 발생한다.           
<br />        


## 애플리케이션 개발      

```
package jpabook.start;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {

            tx.begin(); //트랜잭션 시작
            logic(em);  //비즈니스 로직
            tx.commit();//트랜잭션 커밋

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void logic(EntityManager em) {

        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("지한");
        member.setAge(2);

        //등록
        em.persist(member);

        //수정
        member.setAge(20);

        //한 건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age=" + findMember.getAge());

        //목록 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("members.size=" + members.size());

        //삭제
        em.remove(member);

    }
}
```
 
코드 구성         
* 엔티티 매니저 설정     
* 트랜잭션 관리      
* 비지니스 로직     


#### 엔티티 매니저 설정   
다음은 엔티티 매니저의 생성 과정이다.    

![스크린샷 2021-01-13 오후 1 00 37](https://user-images.githubusercontent.com/33855307/104405016-58a06600-559f-11eb-93c5-f5906c4546c0.png)     
<br />            

**엔티티 매니저 팩토리 생성**          
```  
EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
```    
- persistence.xml의 설정 정보를 사용해서 엔티티 매니저 팩토리 생성                        
- Persistence 클래스 사용                        
- Persistence 클래스 : 엔티티 매니저 팩토리를 생성해서 JPA를 사용할 수 있게 준비                    
- 설정정보를 읽고 JPA 동작을 위한 기반 객체 만든다.       
- JPA 구현체에 따라 데이터베이스 커넥션 풀도 생성하므로 엔티티 매니저 팩토리를 생성하는 비용이 아주 크다.       
- 엔티티 매니저 팩토리는 애플리케이션 전체에서 딱 한 번만 생성하고 공유해서 사용해야 한다.             
<br />     
       
**엔티티 매니저 생성**         
```   
EntityManager em = emf.createEntityManager();  
```     
- 엔티티 매니저를 사용해서 엔티티를 데이터베이스에 등록/수정/삭제/조회할 수 있다.       
- 엔티티 매니저는 데이터베이스 커넥션과 밀접한 관계가 있으므로 스레드간에 공유하거나 재사용하면 안된다.         
<br />       

**종료**             
사용이 끝난 엔티티 매니저는 반드시 종료          
```         
em.close();     // 엔티티 매니저 종료         
```         
         
어플리케이션을 종료할 때 엔티티 매니저 팩토리도 종료            
```         
emf.close();    // 엔티티 매니저 팩토리 종료         
```         
<br />          

#### 트랜잭션 관리        
JPA를 사용하면 항상 트랜잭션 안에서 데이터를 변경해야 한다.      
트랜잭션 없이 데이터를 변경하면 예외가 발생한다.         
트랜잭션을 시작하려면 엔티티 매니저에서 트랜잭션 API를 받아와야 한다.       
<br />          

#### JPQL   
하나 이상의 회원 목록을 조회하는 다음 코드를 살펴보자.   

```
//목록 조회
List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
System.out.println("members.size=" + members.size());
```

JPA는 엔티티 객체를 중심으로 개발하므로 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색해야 한다.      
그런데 테이블이 아닌 엔티티 객체를 대상으로 검색하려면 데이터베이스의 모든 데이터를 애플리케이션으로 불러와서   
엔티티 객체로 변경한 다음 검색하야 하는데, 이는 사실상 불가능하다.        

애플리케이션이 필요한 데이터만 불러오려면 결국 검색 조건이 포함된 SQL을 사용해야 한다.        
JPA는 JPQL 이라는 쿼리 언어로 문제를 해결한다.            

차이점은 다음과 같다.             
- JPQL은 **엔티티 객체를 대상**으로 쿼리한다. 쉽게 말해 클래스와 필드를 대상으로 쿼리한다.            
- SQL은 데이터베이스의 **테이블을 대상**으로 쿼리한다.           
<br />     

`select m from Member m`이 바로 JPQL이다.   
여기서 `from Member`는 회원 엔티티 객체를 말하는 것이지 MEMBER 테이블이 아니다.   
**JPQL은 데이터베이스 테이블을 전혀 알지 못한다.**   

JPA는 JPQL을 분석해서 적절한 SQL을 만들어 데이터베이스에서 데이터를 조회한다.      
 
```  
SELECT M.ID, M.NAME, M.AGE FROM MEMBER M
```
<br />          