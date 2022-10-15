# jpa 기초      
### 1. 배경지식   
#### (1) Domain Model   
데이터베이스를 설계할 때, Entity Relationship 다이어그램으로 데이터베이스의 저장 정보를 설계한다.   
이런 설계의 결과물을 모델이라고 부른다.   
즉, 데이터베이스 설계 작업에서 작성된 ER 다이어그램도 모델이다.   
도메인 모델은 ER 다이어그램과 매우 유사하다. 그러나 보통 도메인 모델은 UML 다이어그램으로도 작성된다.   
Domain - 업무, 분야, 영역이라고 이해해도 된다. 예) 대학교 학사 업무 영역(학사 정보 시스템의 도메인)  
대학교 학사 정보 시스템에는 과목, 강좌, 학기, 수강, 학점, 교수, 재수강 등의 개념이 포함된다.   

대학교 학사 정보 시스템을 개발하려면 먼저 학사 업무를 이해해야 한다.   
그러기 위해, 포함된 개념을 정리하고 사이 관계를 정리해야 한다.   
보통 UML 다이어그램을 그리면서 정리하고 이 모델을 학사 업무에 대한 도메인 모델이라고 한다.   
이것으로부터 데이터베이스 스키마가 만들어진다.   
그리고 도메인 모델의 객체와 그들간의 관계가 그대로 JPA Entity 클래스로 구현된다.   
JPA Entity 클래스에서 Entity의 의미는 ER 다이어그램의 Entity와도 같은 의미이다.   

Domain model - Conceptual model - Business model  **>>**  유사한 개념  


#### (2) JPA Repository 기본 메소드   
```java
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
}
```  
위와 같은 형태로 인터페이스를 선언하면, 이 인터페이스를 구현한 클래스를 spring JPA가 자동으로 구현해준다.   
이렇게 자동으로 구현된 클래스에는 다음과 같은 기본 메소드가 포함된다.  

<details>  
<summary>기본 메소드</summary>  
<div markdown="1">    
  
- employeeRepository.findAll()           
  employee 테이블에서 레코드 전체 목록을 조회한다.    
  List<Employee> 객체가 리턴된다.     

- employeeRepository.findById(id)     
  employee 테이블에서 기본키 필드 값이 id인 레코드를 조회한다.    
  Optional<Employee> 타입의 객체가 리턴된다. 이 객체의 get 메소드를 호출하면 Employee 객체가 리턴된다.    
  예: Employee employee = employeeRepository.findById(id).get();     
  (Optional 이란, “존재할 수도 있지만 안 할 수도 있는 객체”, 즉, “null이 될 수도 있는 객체”을 감싸고 있는 일종의 래퍼 클래스입니다.    
  원소가 없거나 최대 하나 밖에 없는 Collection이나 Stream으로 생각하셔도 좋습니다.)    
  
- employeeRepository.save(employee)   
  Employee 객체를 employee 테이블에 저장한다.    
  Employee 객체의 id(기본키) 속성값이 0 이면 INSERT 되고, 0 이 아니면 UPDATE 된다.   

- employeeRepository.saveAll(employeeList)  
  Employee 객체 목록을 employee 테이블에 저장한다.  

- employeeRepository.delete(employee)   
  Employee 객체의 id(기본키) 값과 일치하는 레코드가 삭제된다.    

- employeeRepository.deleteAll(employeeList)   
  Employee 객체 목록을 테이블에서 삭제한다.    

- employeeRepository.count()   
  employee 테이블의 전체 레코드 수를 리턴한다.   

- employeeRepository.exists(id)   
  employee 테이블에서 id에 해당하는 레코드가 있는지 true/false를 리턴한다.    

- employeeRepository.flush()      
  지금까지 employee 테이블에 대한 데이터 변경 작업들이 디스크에 모두 기록되도록 한다.
(mybatis 캐시는 디폴트로 꺼져있지만, JPA는 캐시(혹은 버퍼)가 디폴트로 켜져있다. 저장안된 것을 저장하라고 하는 메소드이다.)     
(캐시와 버퍼는 성능 향상을 위한 메모리지만 매커니즘이 조금 다르다.    
캐시는 똑같은 데이터를 반복 조회하지 않으려는 것이고 버퍼는 디스크에 기록할 때 성능이 좋으려면 블록으로 기록해야한다.     
즉, 쓰기와 읽기 !)   
    
위 메소드들은 Spring JPA가 자동으로 구현해준 메소드이다.   

</details> 
</div>

#### (3) REST API    
안드로이드 앱을 위한 서버를 개발하거나,     
웹브라우저에서 실행되는 Javascript로 개발한 앱을 위한 서버를 개발할 때,     
REST API 서비스를 제공하는 서버를 구현한다.     

REST API 서비스라는 것은,    
클라이언트의 URL 요청에 대해서 JSON 헝태의 데이터를 출력하는 서버의 메소드를 말한다.        

#### (4) JSON(Javascript Object Notation)   
서버와 클라이언트 사이에 데이터를 주고 받을 때 흔히 사용되는 포멧이다.    
Javascript 언어의 객체나 배열의 문법과 동일하다.   

#### (5) RestController    
Spinrg MVC 프레임웍에서 REST API 서비스를 구현할 때,    
컨트롤러에 @RestController 어노테이션을 붙인다.    

RestController의 액션 메소드가 리턴하는 Java 객체는 자동으로 JSON 포멧으로 변환되어 클라이언트에 전송된다.   
RestController의 액션 메소드는, 데이터를 클라이언트에 전송하기 때문에, 뷰(view)가 필요 없다.   

#### (6) @ResponseBody   
액션메소드 앞에 @ResponseBody 어노테이션을 붙이면, 이 액션메소드는 자동으로 Java 객체를 JSON 형식으로 변환하여 클라이언트에 전달한다.   
그러나 컨트롤러에 @RestController 어노테이션을 붙였다면, 생략해도 된다.    

#### (7) @RequestBody   
액션 메소드의 파라미터 변수 앞에 어노테이션을 붙여주면 JSON 형식으로 데이터를 받을 수 있다.   

#### (8) Request Method    
서버에 데이터를 요청할 때 **GET**     
서버에 저장할 새 데이터를 전송할 때 **POST**     
서버에 기존 데이터를 수정하기 위해 전송할 때 **PUT**    
서버의 데이터 삭제를 요청할 떄 **DELETE**    

#### (9) REST API URL 패턴     
- query string 사용하지 않기    
REST API 서비스의 URL에 query string을 사용하지 않는 것이 관례이다.   <br/><br/>
예를 들어 아래 URL은 바람직하지 않다.  
http://localhost:8080/studentServer/api/student?id=3  <br/><br/>
아래와 같은 형태이어야 한다.  
http://localhost:8080/studentServer/api/student/3    
Request Method = GET   

- 동사 사용하지 않기    
REST API 서비스의 URL에 동사를 사용하지 않는 것이 관례이다.    <br/><br/>
예를 들어 아래 URL은 바람직하지 않다.  
http://localhost:8080/studentServer/studentDelete?id=3  <br/><br/>
아래와 같은 형태이어야 한다.  
http://localhost:8080/studentServer/api/student/3  
Request Method = DELETE  


#### (10) @PathVriable    
요청된 URL이 다음과 같다면,    
api/student/3    

URL에 들어있는 id값 3을 받기 위한 액션 메소드는 다음과 같이 구현한다.     
```java
@RequestMapping(value="api/student/{id}")
public Student student(@PathVriable("id") int id){...}
```
 
#### (11) 액션 메소드 URL     
컨트롤러 클래스에도 @RequestMapping("URL1") 어노테이션 붙어있고  
액센 메소드에도 @RequestMapping("URL2") 어노테이션이 붙어있다면,  
그 액션 메소드를 호출하기 위한 URL은 "URL1/URL2" 이다.  

**Example Code**    
```java
@Controller
@RequestMapping("studnet")
public class StudentController {

   @RequestMapping("list")
   public String list(...) { 
     ...
   }
}
```

list 액션 메소드를 호출하기 위한 URL은 "studnt/list" 이다.  


#### (12) JPA Entity 클래스     
JPA Entity 클래스는 데이터베이스 테이블의 레코드레 해당하는 Java 클래스이다.       
데이터베이스 조회 결과가 자동으로 엔티티 클래스 객체에 채워져 리턴된다.      

**@Entity**     
   JPA Entity 클래스 앞에 @Entity 어노테이션을 붙여야 한다.   
 
**@Id**    
   기본키(primary key)에 해당하는 멤버 변수에 @Id 어노테이션을 붙여야 한다.   

**@GeneratedValue(strategy = GenerationType.IDENTITY)**    
   기본키가 Auto Increment 필드이거나 Identity 필드인 경우에    
   이 어노테이션을 붙여야 한다.    
   Identity 필드란, key 생성을 데이터베이스에 위임한다는 것이다.    
   (ex. MySQL의 Auto Increment)  

**@ManyToOne**     
**@JoinColumn(name = "departmentId")**  
**Department department;**  
   Employee 테이블의 departmentId 필드가 외래키(foreign key)이고,  
   이 외래키가 Department 테이블의 레코드를 한 개 가르킨다면,  
   Employee 테이블에 int departmentId 멤버 변수를 만드는 대신에  
   Department department 멤버 변수를 만들고, 위 어노테이션들을 붙어야 한다.  
   Employee 테이블과 Department 테이블의 관계가   
   다 대 1 이면, @ManyToOne 어노테이션을 붙이고,  
   1 대 1 이면 @OneToOne 어노테이션을 붙인다.  


#### (13) Eager Loading, Lazy Loading        
위와 같이 departmentId 외래키 대신 Department department 멤버 변수를     
Employee 엔터티 클래스에 정의하면 다음과 같은 일이 자동으로 일어난다.    

(1) DB 에서 Employee 레코드가 조회되면,   
(2) Employee 엔터티 클래스 객체가 생성되고,   
(3) 이 객체에, 조회된 Employee 레코드가 채워지고,  
(4) 그 Employee 의 departmentId 외래키가 가르키는 Department 레코드도 같이 조회되고,   
(5) Department 엔터티 클래스 객체가 생성되고,  
(6) 이 객체에, 조회된 Department 레코드가 채워지고  
(7) 이 Department 객체가 Employee 객체의 department 멤버 변수에 대입된다.  

**Eager Loading**  
DB 에서 Employee 레코드를 조회할 때,  
(1) ~ (7) 절차가 자동으로 실행되는 정책을 Eager Loading 이라고 부른다.  
보통 Eager Loading 이 기본 정책이다.  

**Lazy Loading**  
DB 에서 Employee 레코드를 조회할 때,  
(1) ~ (3) 절차만 즉시 자동으로 실행되고 (4) ~ (7) 절차의 실행은 최대한 뒤로 미루는 것을  
Lazy Loading 정책이라고 부른다.  
(사용할 때 잠깐 멈추고 데이터를 가져오기 때문에  Eager Loading 보다 조금 느리다. )  

Employee 클래스의 getDepartment() 메소드가 호출되기 전에는 (4) ~ (7) 절차의 실행을 미루다가  
최초로 getDepartment() 메소드가 호출될 때 이 절차를 실행한다.  

### 2. jpa10 프로젝트    
#### (1) employee1 데이터베이스 import   
#### (2) 프로젝트 생성    
maven - WEB, JPA, MySQL    
#### (3) application.properties   

```
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```  
엔터티 클래스의 studentNumber 속성에 자동으로 연결될 데이터베이스 필드 명이    
studentNumber 형태이면 이 설정이 필요하다.  (camel case)    
student_number 형태이면 이 설정이 필요없다. (snake case)     

```
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```  
JPA 엔진에 의해서 자동 생성되고 실행되는 SQL 명령을 실시간으로 보여준다.   


