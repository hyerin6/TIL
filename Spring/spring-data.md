## Spring Data 
Springboot가 지원하는 데이터 연동 기술   


* SQL DB 
    - Spring JDBC, JPA
    

* NoSQL
    - Redis, MongoDB 

  

<br />

## Spring Data MySQL      
DBCP는 데이터베이스 커넥션 풀이다.         
DBCP는 DB 성능을 결정하는 핵심적인 역할을 한다.            
Springboot는 기본적으로 HikariCP를 사용한다.       

* properties에서 DBCP 설정 : `spring.datasource.hikari.*`       
* HikatiConfig 클래스에서 설정 관리       
* `mysql-connector-java` 의존성을 추가하여 MySQL에 접근할 수 있는 커넥터를 추가한다.    

<br />

## ORM, JPA   
ORM은 Object-Relational Mapping의 약자로 객체와 릴레이션 매핑할 때     
발생하는 개념적인 불일치를 해결하는 프레임워크이다.     

JPA는 자바 진영에서의 ORM을 위한 표준이다.   
대부분 자바 표준은 hibernate를 기준으로 한다.    
hibernate는 JPA의 구현체이다.   

<br />

## Spring data JPA   
spring-data-jpa는 ORM 표준을 쉽게 사용할 수 있게 추상화 시켜 놓은 것이다.   
spring-data-jpa는 JPA를 사용하고 JPA의 구현체는 hibernate이며   
hibernate는 Datasource를 사용하게 된다.  


<br />

## Spring Data 데이터베이스 초기화
JPA를 사용하면 테이블을 만들면서 자동으로 스키마를 생성한다.    
JPA를 사용하여 데이터베이스를 초기화할 수 있다.   

`spring.jpa.hibernate.ddl-auto`로 5가지 옵션을 지정할 수 있다. 

* update: 기존 데이터를 유지하면서 기존의 스키마에서 추가된 것만 변경한다.
* create: drop하고 새로 스키마를 생성한다.
* create-drop: 애플리케이션이 동작할 때 스키마를 생성하고 애플리케이션이 종료하면 스키마를 삭제한다.
* validate:  스키마는 건드리지 않고 Object를 검사한다. 오브젝트와 스키마의 정보가 다르면 에러를 발생시킨다.



create와 create-drop은 개발 환경에서 사용하면 편리하지만 실제 프로덕션에서는 사용하면 안되는 옵션이기도 하다.

이를 사용하기 위해서는 아래의 설정을 true로 만들어주어야한다.

```
spring.jpa.generate-ddl = true
```

<br />


## Spring Data 
* Spring JDBC만 있으면 `JdbcTemplate`을 빈으로 등록해서 손쉽게 사용할 수 있게 지원해주고 있다.    
* `spring.datasource.~` 으로 설정해서 사용하면 된다.   
* production DB에 연결할 때 기본적으로 Hikari-cp가 사용이 된다.   
* JPA는 자바 진영의 ORM 기술 표준   
* `spring.jpa.hibernate.ddl-auto`에 옵션 설정 가능 (`spring.jpa.generate-ddl=true`로 설정해주어야 사용 가능)

<br />
