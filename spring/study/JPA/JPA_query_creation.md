# JPA query creation

### 1. 배경지식 
(1) 표준 API   
Java 언어를 배운다는 것은 문법을 배우고 Java 표준 라이브러리의 클래스들을 배우는 것을 말한다.        
예를들어, String 클래스와 ArrayList 클래스는 Java 표준 라이브러리 클래스이다.       
Android 개발에서 사용하는 위 클래스들은 Google이 만든 Java 표준 라이브러리에 들어있고,      
Spring WEB MVC 개발에서 사용하는 String 클래스와 ArrayList 클래스는 Oracle이 만든 Java 표준 라이브러리에 들어있다.       

Google이 만든 클래스와 Oracle이 만든 클래스의 사용법이 동일한 이유는,        
두 회사가 라이브러리 API를 동일하게 구현했기 때문이다.             


💁🏻‍♀️
라이브러리 API가 동일하다 ?        
라이브러리의 public 클래스와 public 메소드 목록이 동일하다는 의미이다.        


이렇게 사용법이 통일된 라이브러리들의 API를 표준 API라고 부른다.      
표준 API의 존재 이유는 여러 라이브러리들의 사용법을 통일하는 것이다.        


(2) JPA와 Hibernate       
JPA(Java Persistence API)는 ORM 기술의 하나이다.      
- Java Persistence - Java 객체를 DB에 저장하거나 DB 데이터를 Java 객체로 조회하는 기술     
- JPA - Java Persistence를 구현한 라이브러리에 대한 표준 API        

JPA는 표준 API일 뿐, 제품은 아니다.        
JPA 표준 API를 구현한 대표적인 오픈소스 라이브러리는 Hibernate이다.      


(3) Spring Data JPA       
Spring Data는 Spring 프로젝트에서 사용되는 데이터베이스 라이브러리 개발 프로젝트이다.      
Spring Data 라이브러리에는      
JDBC 데이터베이스 프로그래밍을 편하게 할 수 있도록 도와주는 클래스들이 포함되어 있고,     
JPA 데이터베이스 프로그래밍을 편하게 할 수 있도록 도와주는 클래스들도 포함되어 있다.      
이 클래스들의 그룹을 Spring Data JPA 라고 부른다.     

Spring Data 라이브러리에       
mybatis 데이터베이스 프로그래밍을 도와주는 클래스들은 포함되어 있지 않다.      
우리가 사용하는 JPARepository 인터페이스는 JPA에 포함된 것이 아니고 Spring Data JPA에 포함된 인터페이스이다.           
즉, Spring Data는 JDBC와 JPA를 지원하지만 mybatis는 지원하지 않는다.         


(4) JPA Repository 기본 메소드       
예를들어, StudentReposirory 인터페이스에는 아래 메소드가 기본적으로 포함되어 있다.        

```java
Optional<Student>  findById (int id);
List<Student>      findAll();
Student            save(Student student); // id = 0 이면 INSERT, id에 해당하는 레코드가 있으면 UPDATE
void               delete(Student student);
boolean            exists(int id); // id 값과 일치하는 메소드가 있으면 True를 리턴 
```

💡java.util.Optional<T> 클래스    
  Integer 이나 Double 클래스처럼 'T' 타입의 객체를 포장해주는 래퍼 클래스이다. (Wrapper Class)   
  따라서 Optional 인스턴스는 모든 타입의 참조 변수를 저장할 수 있다.      
  Optional 객체를 사용하면 예상치 못한 NullPointerException 예외를 제공되는 메소드로 간단하게 회피할 수 있다.       
  즉, 복잡한 조건문 없이 널(null) 값으로 인한 예외를 처리할 수 있다.       
  
  
  
우리가 JPAReposiroty 인터페이스를 상속하여 StudentRepository 인퍼테이스를 정의하면,    
Spring Data JPA가 다음과 같은 일들을 자동으로 해준다.   
- StudentRepository 인터페이스를 구현한 클래스를 자동으로 구현한다.   
- 그 클래스의 JPA Repository 기본 메소드를 구현해준다.       
- 그 클래스의 객체를 한 개 자동 생성해 준다.        
- 생성된 객체를 ```@Autowired StudentRepository studentRepository;``` 멤버 변수에 자동으로 대입해준다.       



(5) 클래스 메소드 자동 구현     
Spring Data JPA는 JPA Reposiroty 기본 메소드 뿐만 아니라,   
Reposiroty 인터페이스에 선언된 다른 메소드들도 자동으로 구현해준다.        

**예시**         
```java 
public interface StudentRepository extends JpaRepository<Student, Integer>  {         
    List<Student> findByName(String name);         
    List<Student> findByStudentNo(String studentNo);        
    List<Student> findByNameStartsWith(String name);        
    List<Student> findByDepartmentName(String name);        
    List<Student> findByDepartmentNameStartsWith(String name);        
    List<Student> findAllByOrderByName();        
    List<Student> findAllByOrderByNameDesc();        
    List<Student> findByDepartmentIdOrderByNameDesc(int id);        
}        
```       
위 메소드에서 Name, Department 부분은 Student 엔터티 클래스의 속성 이름이다.       
     Student 엔터티 클래스에 department 속성이 정의되어 있다.        
     Java에서 속성은 멤버 변수가 아니고, getter/setter 메소드에 의해서 정의된다.      
     Student 엔터티 클래스의 getter/setter 메소드는, lombok에 의해서 자동 생성된다.        
     (파라미터명은 중요하지 않다. 순서만 맞으면 된다.)        


Student 테이블과 Department 테이블 departmentId 외래키로 조인된다.             
조인된 테이블의 속성임을 명시적으로 표현하기 위해서, '_' 문자를 넣을 수도 있다. (넣어도되고 안넣어도되고)      

 **예)**     
 findByDepartment_Name(String name);              
 findByDepartment_NameStartWith(String name);            
 findByDepartment_IdOrderByNameDesc(int id);             

```List<Student> findByName(String name);```           
student 테이블에서 name 필드로 레코드를 조회하여 리턴한다.      
파라미터 변수 name의 값과 필드 값 전체가 일치해야 한다.     

```List<Student> findByDepartmentIdOrderByNameDesc(int id);```    
student 테이블과 department 테이블들을 조회하고 department 테이블의 id로 조회하여    
일치하는 student 테이블 레코드를 리턴한다.     
조회 결과를 student 테이블의 name 필드로 내림차순 정렬하여 리턴한다.        


💡 ASC, DESC ?        
ASC - 오름차순 ex) 1, 2, 3, 4, ... 생략 가능       
DESC - 내림차순 ex) 5, 4, 3, 2, ...       

(6) 쿼리 메소드 이름 규칙    

| keyword | Sample | 설명 |      
|:---:|:---:|:---:|
| And | findByLastNameAndFirstName(String lastName, String firstName) | |
| Or | findByLastNameOrFirstName(String lastName, String firstName) | |
| Between | findByStartDateBetween(Date date1, Date date2) | |
| LessThan | findByAgeLessThan(int age) | - 보다 작은 레코드 조회 |
| GreaterThan | findByAgeGreaterThan(int age) | - 와 같거나 큰 레코드 조회 |
| After | findByStartDateAfter(Date date1) | WHERE x.startDate > #{startDate} |
| Before | findByStartDateBefore(Date date1) | WHERE x.startDate < #{startDate} |
| IsNull | findByAgeIsNull() | |
| IsNotNull, NotNull | findByAgeNotNull(), findByAgeIsNotNull() | |
| Like | findByFirstNameLike(String pattern) | |
| NotLike | findByFirstNameNotLike(String pattern) | |
| StartingWith | findByFirstNameStartingWith(String name) | |
| EndingWith | findByFirstNameEndingWith(String name) | |
| Containing | findByFirstNameContaining(String name) | |
| OrderBy | findByAgeOrderByLastNameDesc(int age) | WHERE 조건컬럼명 Like + '%' + #{파라미터} + '%', WHERE name LIKE '%김%' |
| Not | findByLastNameNot(String name) | |
| In | findByAgeIn(Collection<Integer> ages) | WHERE age IN #{ages} |
| NotIn | findByAgeNotIn(Collection<Integer> age) | |
| True | findByActiveTrue() | WHERE active, WHERE active = true |
| False | findByActiveFalse() | WHERE NOT active, WHERE active = false |
| Top | findTop10ByOrderByAge() | 조회 결과에서 선두 10개 레코드만 리턴한다. |
  

💁🏻‍♀️ 주의 !             
mybatis는 Collection 타입 파라미터를 지원하지 않는다.       

**에러 메시지**         
만약 메소드 이름이 규칙에 어긋난다면,        
STS 편집창에서 에러 메시지가 표시될 것이다.        

예를 들어, Student 엔터티의 name 속성명을 namme 으로 수정하자.        
아래와 같은 에러 메시지가 표시된다.        

"Invalid derived query! No property namme found for type Student! Did you mean 'name'?"        

</br>   

### 파라미터 변수 이름은 중요하지 않다. 파라미터 변수 순서가 중요하다.        
ex) findByLastNameAndFirstName(String s1, String s2)    
파라미터 변수명이 무엇이든 상관없이       
첫번째 파라미터가 lastName이고, 두번째 파라미터가 firstNmae이어야 한다.        

</br>   

### 파라미터 변수명이 무시되는 이유      
Java 언어는 바이트코드로 컴파일 된다. (*.class 파일)

컴파일된 바이트코드가 JVM에서 실행된다.   

컴파일된 *.class 파일에 Java 지역변수명과 파라미터 변수명은 들어있지 않다.   
그래서 JVM에서 바이트코드가 실행될 때 메소드 파라미터 변수명은 무시될 수 밖에 없다.     

</br>   


### 파라미터 변수명이 무시되는 사례   
mybatis mapper 메소드   
JPA Repository 메소드   
Spting MVC 액션 메소드     


(7) countBy, deleteBy, removeBy      
findBy 메소드 이름 규칙은   
countBy, deleteBy, removeBy 메소드에도 적용된다.     

countBy는 주어진 조건에 일치하는 레코드 수를 리턴한다.    
deleteBy, removeBy는 조건에 일치하는 레코드를 삭제한다.    


**예**   
```java
public interface StudentRepository extends JpaRepository<Student, Integer>  {
    int countByName(String name);
    int countByNameStartsWith(String name);
    int countByStudentNoOrName(String studentNo, String name);

    void deleteByName(String name);
    void deleteByDepartmentId(int departmentId);
}
```

