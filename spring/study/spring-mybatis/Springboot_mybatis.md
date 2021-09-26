# Spring boot and mybatis  
Spring boot + mybatis 기술을 사용하여 DB 조회, 수정, 삽입, 삭제 기능을 구현한다.  

## 1. 배경지식  
<details markdown="1">
<summary>view</summary>
   
### (1) ORM (Object Relational Mapping)   
ORM 에서 Object 는 객체지향 언어의 객체를 의미한다.  
Ralational 은 관계형 데이터베이스(Relational Database)의 데이터를 의미한다.  
Mapping이 의미하는 것은 객체지향 언어의 객체와 관계형 데이터를 서로 변환해 준다는 것이다.  

**ORM 이란?**  
   관계형 데이터베이스에서 조회한 데이터를 Java 객체로 변환하여 리턴해 주고,   
   Java 객체를 관계형 데이터베이스에 저장해 주는    
   라이브러리 혹은 기술을 말한다.    

Java ORM 기술로 유명한 것은  
mybatis, Hibernate, JPA 이다.  

mybatis와 Hibernate는 오픈소스 프로젝트이고 jar 라이브러리 형태로 제공된다.  

JPA(Java Persistence API)는 제품의 이름이 아니고, API 표준의 이름이다.  
JPA 표준 규격대로 만들어진 제품 중에서 유명한 것이 Hibernate 오픈소스 라이브러리이다.  
우리가 사용하는 Spring JPA에 Hibernate 라이브러리가 포함되어 있다.  

우리 나라의 전자 정부 표준 프레임웍에서 Spring mybatis를 채택하고 있기 때문에,    
우리 나라 공공 프로젝트에서 mybatis를 사용하는 경우가 많다. 그렇지만 JPA가 좀 더 미래지향적인 기술이기 때문에 점점 JPA를 사용하는 경우가 늘어나고 있다.   

### (2) JPA와 mybatis 비교   
MySQL, Oracle, SQL Server 등 DBMS  제품 마다 SQL 문법은 조금씩 다르다.
그래서 DBMS 제품을 교체하려면, SQL 문장도 수정해야 한다.

- JPA의 장점  
SQL 명령을 구현할 필요가 없다. 그래서 DBMS 제품을 교체하더라도 소스코드를 수정할 필요가 없다.  
자동으로 처리되는 부분이 많아서, 구현할 소스코드의 양이 상대적으로 적다.  
관계형 데이터베이스가 아니더라도 적용할 수 있다.  

- JPA의 단점  
복잡한 조회 명령을 구현해야 할 때, 익숙한 SQL 명령으로 구현할 수가 없고, JPA의 고급 기능을 공부해야 한다.  

- mybatis의 장점  
익숙한 SQL 명령으로 구현할 수 있다.  
SQL 문장을 그대로 사용하여 구현하기 때문에, SQL 문장에 익숙한 개발자에게 myBatis가 편하다.</br>  
DB 조회 결과를 복잡한 객체 구조로 변환해야 할 때 myBatis 기능이 좋다.  
mybatis의 resultMap 기능이 바로 그것이다.  
이 기능은 복잡한 보고서(report)를 출력해야 할 때, 특히 유용하다.</br>  
데이터베이스 성능 개선을 위해, 어떤 인덱스를 생성해야 하는지 파악하기 위해,   
SQL 쿼리들을 분석해야 하는데, 이때 myBatis는 SQL 문장을 그대로 사용하기 때문에,   
SQL 쿼리 분석하기 편하다.  
 
- mybatis의 단점  
구현할 소스코드의 양이 상대적으로 많다.  
관계형 데이터베이스에만 적용할 수 있다.</br>  
DBMS 제품을 교체하면 SQL 소스코드를 수정해야 한다.  
Oracle, MS SQL Server, mySQL 등 DBMS 마다 SQL 문법이 약간씩 차이가 있다.  
그래서 DBMS를 바꾸면 SQL 문도 수정해야 하는 불편함이 있다.  
SQL 문을 사용하지 않는 Hibernate, JPA에는 이런 문제가 없다.  

### (3) mybatis mapper   
데이터베이스는 테이블에 대한 SELECT / INSERT / UPDATE / DELETE SQL 명령들을 mybatis mapper에 구현한다.  
보통 데이터베이스 테이블 한 개당 mybatis mapper 한 개를 구현한다.  

mybatis mapper는 Java Interface 파일 한 개와, XML 파일 한 개로 구현된다.   

DB 테이블에 대한 조회, 삽입, 수정, 삭제 SQL 명령을 mapper XML 파일에 구현한다.   
그리고 이 명령을 호출하기 위한 Java 메소드를 mapper Java Interface 파일에 선언한다.  

mapper 메소드를 호출하기 위한 Java 메소드를 Java Interface에 선언하기만 하면 된다.    
하지만 이 메소드를 구현(implements)할 필요는 없다.     
즉, Mapper Java Interface만 만들면되고, 이 인터페이스는 mybatis spring이 자동으로 구현해주기 때문에 구현할 필요는 없다.   

### (4) Auto Increment 필드 (identity 필드)  
Student 테이블의 기본키(primary key)는 id 필드이다.   
MySQL에서 Student 테이블을 생성할 때, id 필드를 Auto Increment 필드로 지정하였다.  

Auto Increment 필드의 값은 1부터 시작하는 일련번호이다.  
테이블에 새 레코드를 insert 할 때, 이 필드의 값에 일련번호가 자동으로 부여된다.   

Auto Increment 필드의 값인 자동으로 부여되기 때문에,  
insert나 update SQL 문에서 이 필드의 값을 저장하는 것이 에러이다.  

### (5) Referential Integrity Constraint 참조 무결성 제약   
Student 테이블의 departmentId 필드는 외래키(foreign key) 이다.  
이 필드의 값은 department 테이블의 기본키인 id 필드값과 일치해야 한다.  
Student 테이블과 Department 테이블을 조인할 때, departmentId 필드를 사용한다.  

```
SELECT s.*, d.departmentName
FROM Student s LEFT JOIN department d ON s.departmentId = d.id
```
Department 테이블에서 레코드를 한 개 삭제 하려고 할 때,   
만약 Student 테이블의 어떤 레코드의 departmentId 필드 값이,   
그 삭제하려는 Department 레코드의 id 필드 값과 일치한다면,  
삭제는 실패하고 에러가 발생한다.  
이 에러를 참조 무결성 제약조건 위반(referential intergity constraint violation)이라고 부른다.  
쉽게 표현하자면, 국어국문학과 소속 학생들이 존재한다면, 국어국문학과를 삭제할 수 없다는 얘기다.   

Register 테이블에 외래키인 studentId 필드가 들어있다.  
그래서 Student 테이블의 레코드를 삭제하려 할 때, 참조 무결성 제약조건 위반 에러가 발생할 수 있다.  
쉽게 표현하자면, 201132050 학생의 수강신청 내역이 존재한다면, 그 학생을 삭제할 수 없다는 얘기다.  

데이터베이스가 참조 무결성 제약조건을 실시간 검사해 준다.  
테이블을 생성할 때, 데이터베이스가 참조 무결성 제약조건을 설정해 주는 것이 바람직하다.  

참조 무결성을 제약조건을 외래키 제약조건이라고도 부른다.  

Student 테이블에 FK_Student_Department 이름의 외래키 제약조건이 이미 설정되어 있다.  
이 외래키 제약조건을 삭제하는 명령은 다음과 같다.  
```
ALTER TABLE Student
DROP FOREIGN KEY FK_Student_Department;
```

**외래키 제약조건 생성하기**    
Student 테이블의 departmentId 필드와 Department 테이블의 id 필드 사이에  
외래키 제약조건을 생성하는 명령은 다음과 같다.  
```
ALTER TABLE Student
ADD CONSTRAINT FK_Student_Department
FOREIGN KEY (departmentId) REFERENCES Department(id);
```
제약조건의 이름은 FK_Student_Department 이다.  
외래키는 Student 테이블의 departmentId 필드이다.  
이 필드는 Department 테이블의 id 필드를 참조(references)한다.  

### (6) 참조 무결성 제약조건 위반 피하기  
- 먼저 삭제하기  
Department 테이블의 레코드를 삭제하기 전에, 먼저 그 레코드를 참조하는 Student 레코드들을 전부 삭제한다.  
예) 소프트웨어공학과를 없애려면 학생테이블의 소프트웨어공학과 학생들을 먼저 지우고 지워야 한다.   
```
DELETE FROM Student WHERE departmentId = 2;
DELETE FROM Department WHERE id = 2;
```

- Cascade Delete 옵션 
외래키 제약 조건을 생성할 때, Casecade Delete 옵션을 지정할 수 있다.  
이 옵션이 지정된 경우에는, Department 테이블의 레코드를 삭제할 때,  
그 레코드를 참조하는 Student 레코드들이 전부 자동으로 삭제된다.  
(자동으로 지워지도록 미리 설정 / 장점 : 편하다. / 단점 : 실수로 레코드 전부를 지울 수 있다.)  
```
ALTER TABLE Student
ADD CONSTRAINT FK_Student_Department
FOREIGN KEY (departmentId) REFERENCES Department(id)
ON DELETE CASCADE;
```
</details>

<br/>

## 2. mybatis1 프로젝트    

<details markdown="1">
<summary>view</summary> 

### (1) 프로젝트 생성    

### (2) pom.xml - Maven 설정 파일   
pom.xml 파일의 <dependency> 태그들에 있는 항목들을 maven dependency라고 부른다.    
필요한 라이브러리나 빌드 방법을 설정하는 폴더이다.   
Maven : 프로젝트 관리 도구 (라이브러리 설치, 빌드)       

파일 하나의 결과 >> 디버깅   
프로젝트 전체 결과 >> 빌드   

### (3) application.properties - Spring boot 설정 파일   

`spring.mvc.view.prefix=/WEB-INF/views/`  
뷰 파일이 위치할 폴더를 지정한다.    

`spring.mvc.view.suffix=.jsp`  
뷰 파일의 확장자를 지정한다.  

`spring.datasource.driver-class-name=com.mysql.jdbc.Driver`  
JDBC 드라이버 클래스의 이름을 지정, Mysql JDBC 드라이버 클래스 

`spring.datasource.url=jdbc:mysql://localhost:3306/student1?useUnicode=yes&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=UTC`  
DB 서버 IP와 DB 이름 설정, 서버 타임존 설정   

`spring.datasource.username=user1` DB 연결 계정 설정    

`spring.datasource.password=test123` DB 연결 계정 설정     
  
`mybatis.type-aliases-package=net.skhu.dto`  
여기서 net.skhu.dto 는 DB 조회 결과 데이터를 담은 클래스의 패키지를 지정한다.  
mybatis mapper XML 파일에서 select 태그의 resultType으로 등록된 클래스들의 패키지를 지정한다.    
예를들어, `<select id="findById" resultType="Student">`    
select 태그의 resultType으로 등록된 Student 클래스의 패키지는 net.skhu.dto 이어야 한다.   
</details>

<br/>  

## 3. DTO 클래스 구현    
### (1) Student.java  
Student 테이블에서 조회한 데이터를 채울 DTO(Data Transfer Object)이다.  

예제에서 Student 테이블에 departmentName은 없지만, Student 테이블에서 조회할 때    
DepartmentName 필드도 조회하기 때문에 그 조회 결과를 채우기 위해 departmentName 멤버변수를 Student 클래스에 추가하였다.   

### (2) Department.java  
Department 테이블에서 조회한 데이터를 채울 DTO이다.   

<br/>  

## 4. Mapper 구현 
 
<details markdown="1">  
<summary>view</summary>  

### (1) StudentMapper.java   
**src/main/java/net/skhu/mapper/StudentMapper.java**  
```
package net.skhu.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import net.skhu.dto.Student;

@Mapper
public interface StudentMapper {
    Student findOne(int id);
    Student findByStudentNumber(String studentNumber); // 파라미터가 Student 객체
    List<Student> findAll(); // List type 
    void insert(Student student);
    void update(Student student);
    void delete(int id);
}
```
- Mapper 인터페이스 위에 @Mapper 어노테이션을 작성해준다.    
- DB 의 Student 테이블에 대한 조회, 삽입, 수정, 삭제 SQL 명령을 StudentMapper.xml 파일에 구현한다.
그리고 이 명령을 호출하기 위한 메소드를 StudentMapper 인터페이스에 선언한다.  

**StudentMapper 인터페이스의 메소드들의 리턴 타입, 이름, 파마티러 타입에 주목하자.**  
- 인터페이스의 메소드 이름은 StudentMapper.xml 파일 태그들의 id 애트리뷰트 값과 일치
- 인터페이스의 메소드의 파라미터는 StudentMapper.xml 파일 태그들의 mybatis 파라미터와 일치  
- 인터페이스의 메소드들의 리턴 타입은 StudentMapper.xml 파일 태그들의 resultType 애트리뷰트 값과 일치  

### (2) StudentMapper.xml   

DB의 Student 테이블에 대한 조회, 삽입, 수정, 삭제 SQL 명령을 StudentMapper.xml 파일에 구현한다.  
이 파일은 studentMapper.java와 동일한 폴더에 있어야 한다.  
SQL 명령문만 제대로 입력하면 java는 자동으로 구현된다.   

**src/main/java/net/skhu/mapper/StudentMapper.xml**   
```
<mapper namespace="net.skhu.mapper.StudentMapper">

  <select id="findOne" resultType="Student">
    SELECT * FROM Student WHERE id = #{id}
  </select>
  
  <select id="findByStudentNumber" resultType="Student">
    SELECT * FROM Student WHERE studentNumber = #{studentNumber}
  </select>   

  <select id="findAll" resultType="Student">
    SELECT s.*, d.departmentName
    FROM Student s LEFT JOIN department d ON s.departmentId = d.id
  </select>

  <insert id="insert" useGeneratedKeys="true" keyProperty="id">
    INSERT Student (studentNumber, name, departmentId, year)
    VALUES (#{studentNumber}, #{name}, #{departmentId}, #{year})
  </insert>

  <update id="update">
    UPDATE Student SET 
      studentNumber = #{studentNumber}, 
      name = #{name}, 
      departmentId = #{departmentId}, 
      year = #{year} 
    WHERE id = #{id}
  </update>

  <delete id="delete">
    DELETE FROM Student WHERE id = #{id}
  </delete>

</mapper>
```

| xml 예 | 설명 | 
|:--------|:--------:|
| resultType="Student" | StudentMapper 인터페이스의 메소드들 리턴타입과 일치해야 한다. | 
| id="findOne" | StudentMapper 인터페이스의 메소드들 이름과 일치해야 한다. | 
| id=#{id} | StudentMapper 인터페이스의 메소드 파라미터와 일치해야 한다. | 

java에서는 파라미터가 Student 객체인데 xml에서는 객체의 속성이다.   
리턴타입도 `List<Student> findAll();` java 에서는 List인데 xml에서는 resultType은 Student이다.   

### (3) mapper 구현 규칙    
#### ⓵ namespace 일치   
**StudentMapper.xml**  
`<mapper namespace="net.skhu.mapper.StudentMapper">`    

**StudentMapper.java**  
``` 
package net.skhu.mapper;

@Mapper
public interface StudentMapper {
   . . .
}
```   
namespace 애트리뷰트 값은 StudentMapper 인터페이스의 이름과 패키지가 정확하게 일치해야 한다.  
그렇지 않은 경우, StudentMapper bean을 생성할 수 없다는 에러가 발생한다.  

#### ⓶ 메소드 이름 일치   
XML 파일에서 id 애트리뷰트와 StudentMapper 인터페이스의 메소드 이름과 일치해야 한다.   

#### ⓷ resultType : 패키지  
**application.properties**  
`mybatis.type-aliases-package=net.skhu.dto`  
mybatis mapper XML 파일에서 select 태그의 resultType으로 등록된 클래스들의 패키지를 지정한다.   

**StudentMapper.xml**   
`<select id="findById" resultType="Student">`   
select 태그의 resultType으로 등록된 Student 클래스의 패키지는 net.skhu.dto 이어야 한다.    

#### ⓸ resultType : 레코드 한 개 리턴   
**StudentMapper.java**  
`Student findOne(int id);`    

**StudentMapper.xml**  
`<select id="findOne" resultType="Student">`  

id 애트리뷰트 값은 StudentMapper 인터페이스의 메소드 이름과 일치해야 한다.   
resultType 애트리뷰트 값은 메소드의 리턴 타입과 일치해야 한다.   

이 SQL 명령은 Student 레코드 한 개를 조회한다.    
조회 결과는 Student 자바 객체에 자동으로 채워져서 리턴된다.  

DB 조회 결과가 자바 객체에 자동으로 채워질 때, 조회 결과 컬럼 제목과 자바 객체의 setter 이름이 일치해야 한다.  

#### ⓹ resultType : 레코드 여러 개 리턴  
findAll 태그의 SQL 명령은 Student 레코드 여러 개를 조회한다.   
조회 결과 레코드 각각은 Student 객체에 채워지고 Student 객체는 List<Student> 객체에 채워져서 리턴된다.   
그래서 findAll 자바 메소드의 리턴 타입은 List<Student>이다.    

예제에서 findAll 메소드의 리턴 타입은 List<Student> 이지만, findAll 태그의 resultType은 Student 임에 주의하자.  
   
SQL 조회 결과가 DTO에 자동으로 채워진다.  

#### ⓺ 조회 결과 컬럼명 일치     
DB 조회 결과가 resultType 자바 객체에 자동으로 채워질 때,  
조회 결과 컬럼 제목과 resultType 자바 객체의 set 메소드 이름이 일치해야 한다.  
에러가 발생하지는 않지만 자바 객체에 결과가 채워지지 않는다.   

#### ⓻ mybatis 파라미터 : 파라미터 한 개   
**StudentMapper.java**  
``` 
Student findOne(int id);
Student findByStudentNumber(String studentNumber);
void delete(int id);
``` 

**StudentMapper.xml**  
```
<select id="findOne" resultType="Student">
    SELECT * FROM Student WHERE id = #{id}
</select>
    
<select id="findByStudentNumber" resultType="Student">
    SELECT * FROM Student WHERE studentNumber = #{studentNumber}
</select>   

<delete id="delete">
    DELETE FROM Student WHERE id = #{id}
</delete>
```  

#{id}, #{studentNumber} 부분이 mybatis 파라미터이다.  
메소드를 호출할 때 전달된 파라미터 값이 SQL 문장의 mybatis 파라미터 부분에 채워져서 SQL 문장이 실행된다.    

mybatis 파라미터로 전달할 값이 한 개이고, 값의 타입이 int, long, float, double, boolean 등 기본자료형이나 
String, Date, Time, Timestamp 클래스 객체인 경우에는 위와 같은 방법으로 구현한다.  
 
여기서 Java 파라미터 변수 이름은 중요하지 않다.    
중요한 것은 파라미터가 한 개이고, 파라미터 타입이 기본자료형이거나    
String, Date, Time, Timestamp 클래스 중 하나의 객체이어야 한다는 점이다.    

#### ⓼ mybatis 파라미터 : 파라미터 여러 개   
#{...} 부분이 mybatis 파라미터이다.  

Java의 파라미터 변수 이름은 중요하지 않고, 
Java 파라미터 변수의 타입이 DTO 클래스이어야 하고  
이 클래스의 getter 이름과 mybatis 파라미터의 이름이 일치해야 한다.   

### (4) auto Increment 필드와 insert   
Student 테이블에 새 레코드를 insert할 때, auto increment 필드인 id 필드 값은 자동으로 부여된다.   
그래서 insert SQL 문에 id 필드값은 지정하지 않는다.  

insert SQL 문이 실행되어 새 레코드가 저장된 후,
값이 자동으로 부여된 새 레코드의 id값이 Student 객체의 id 속성에 자동으로 채워진다.   

```
Student student = new Student();
student.setStudentNumber("201132091");
student.setName("홍길동");
student.setDepartmentId(1);
student.setYear(1);

System.out.println(student.getId());
studentMapper.insert(student); // inset하면 mybatis가 id값을 자동으로 채워준다. 
System.out.println(student.getId());
```
줄7에서 출력되는 id 값은 0 이다.  
Student 객체의 id 멤버 변수에 아직 아무것도 대입되지 않았기 때문이다.  

줄9에서 출력되는 id 값은, 줄8에서 저장된 새 레코드의 id 필드값이다.  
이 값은 0 이 아니다.  

```
  <insert id="insert" useGeneratedKeys="true" keyProperty="id">
    INSERT Student (studentNumber, name, departmentId, year)
    VALUES (#{studentNumber}, #{name}, #{departmentId}, #{year})
  </insert>
```
위 insert 태그에서 노란색으로 칠한 부분이 의미하는 것은,  
새 레코드의 id 필드값을 Student 객체의 id 속성에 대입해 달라는 것이다.   
</details>      

<br/>    

## 5. 컨트롤러 구현   

<details markdown="1">  
<summary>view</summary> 

### (1) @Autowired  
StudentMapper 인터페이스를 구현한 java 클래스를 mybatis spring이 자동으로 구현해주고  
그 클래스의 객체를 한 개 생성하여, StudentMapper 멤버변수에 자동으로 대입해준다.   

그래서 StudentMapper 멤버변수를 선언했을 뿐이고 어떤 객체를 대입한 것도 아니지만   
mybatis spring이 자동으로 생성해준 객체가 이 멤버변수에 자동으로 대입되어 있기 때문에    
액셔 메소드는 이 객체를 사용할 수 있다.   

### (2) @RequestMapping()   
**클래스에 작성할 때**   
```
@Controller   
@RequestMapping("/student")
```  
컨트롤러 클래스의 request mapping 어노테이션이다.   
요청된 URL이 "/student" 이면 이 컨트롤러를 사용하라. 라는 의미이다.  
이 부분은 선택적으로 작성하며, 웹 브라우저에서 요청하는 URL(http request)에 mapping된 액션 메소드가 실행되는 것이다.   

Q. 그렇다면 컨트롤러 클래스의 request mapping 어노테이션은 언제 작성해야 할까?  
A. 액션 메소드의 어노테이션의 URL 앞 부분이 전부 동일할 때 작성하면 됩니다.   

**액션 메소드에 작성할 때**   
`@RequestMapping("list")`  
액션 메소드의 어노테이션이다.   
"student/list" URL이 요청되면 list 액션 메소드가 자동으로 호출된다.   
request mapping URL = 컨트롤러 URL + 액션 메소드 URL   

**value, method**   
`@RequestMapping(value="create", method=RequestMethod.GET)`   
"/student/create" URL이 GET 방식으로 요청되면 이 액션 메소드가 호출된다.  

### (3) return "redirect:...";   
액션 메소드를 실행한 후, list 상대URL로 redirect 하라는 메타 태그를 웹 브라우저에 출력한다.   
이 메타 태그를 받은 웹 브라우저는 redirect하라고 지정된 URL을 서버에 즉시 다시 요청하게 된다.  
뷰는 건너뛰고 즉시 URL을 요청한다.   

### (4) model.addAttribute();  
```   
List<Student> students = StudentMapper.findAll();
model.addAttribute("students", students);
```      
"Students" 이름의 데이터를 model 객체에 넣는다.   
이 데이터를 model attribute 라고 부른다.  
model attribute 데이터는 뷰(view)에 전달된다.   

### (5) request parameter 전달   
액션 메소드의 파라미터에 request parameter 값이 채워져서 액션 메소드에 전달된다.   

**값을 하나씩 전달받을 때**  
``` 
@RequestMapping(value="edit", method=RequestMethod.GET)
public String edit(Model model, @RequestParam("id") int id)
``` 
위 소스코드에서 @RequestParam("id") int id 부분에서 일어나는 일은 아래와 같다.   
String s = request.getParameter("id");  
int id = Integer.parseInt(s);  

**값을 여러개 전달받을 때**  
``` 
@RequestMapping(value="edit", method=RequestMethod.POST) 
public String edit(Model model, Student student)
```   
request parameter 데이터가 Student 객체에 자동으로 채워진다.  
request parameter 데이터의 이름과 액션 메소드의 파라미터 객체의 속성 이름이 일치할 경우에 (setter 이름),  
request parameter 데이터가 그 속성에 자동으로 채워진다.   
</details>

<br/>

## 6. 뷰 구현    

<details markdown="1">  
<summary>view</summary> 

### (1) WEB-INF/views/student 폴더 생성    
### (2) student/list.jsp    
`<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>`  
C: 로 시작하는 JSTL 확장 태그를 사용하기 위한 선언   

`c:url var="R" value="/" />`   
현재 url에서 context path이 변수 R에 대입된다.    
프로젝트명이 mybatis이므로, R에 대입되는 값은 "/mybatis/"이다.   

`<script src="${R}res/common.js"></script>`   
변수 R의 값이 ${R} 부분에 출력된다.   
예를들어, 변수 R의 값이 "/mybatis/"이면 출력되는 내용은 다음과 같다.   
`<script src="/mybatis/res/common.js"></script>`  

`<c:forEach var="student" items="${ students }">`  
model에서 "students" 이름으로 등록된 학생 목록의 학생 객체 각각에 대해서  
학생 객체를 student 변수에 대입하고 <C:forEach> ... </C:forEach> 태그 사이의 내용을 출력한다.   

### (3) student/edit.jsp  
`학생 ${ student.id > 0 ? "수정" : "등록" }`  
model에 들어있는 "Student" 이름의 객체의 id 속성값이 0보다 크면 "수정" 출력 아니면, "등록"을 출력한다.  
학생등록 화면일 경우 "Student" 이름의 model객체의 id 속성값은 0이고,  
학생수정 화면일 경우 "Student" 이름의 model객체의 id 속성값은 수정할 Student 레코드의 id이다.   

`<form:form method="post" modelAttribute="student">`   
이 확장 태그는 form 태그를 출력한다.  
이 입력 폼에서 편집할 데이터 객체는 model에 들어있는 "Student" 이름의 객체이다. (model Attribute)   

`<form:input path="studentNumber" class="form-control w200" />`  
input 태그를 출력한다. (type=text)    
modelAttribute 객체의 studentNumber 속성값이, input 태그의 value 애트리뷰트에 출력된다.  
input 태그의 name 값은 studentNumber 이다.   

```
<form:select path="departmentId" class="form-control w200"
             itemValue="id" itemLabel="departmentName" items="${ departments }" />
```
select 태그와 option 태그를 출력한다.  
(items값) departments 목록의 Department 객체 각각을 option 태그로 출력한다.   
(itemValue 값) option 태그의 value: Department 객체의 id 속성값이다.  
(itemLabel 값) option 태그의 텍스트: Department 객체의 departmentName 속성값이다.  

form:form 태그에 선언된 modelAttribute 객체는 Student이다. modelAttribute="student"  
이 student 객체의 departmentId 속성값과 일치하는 option 태그에 selected를 붙인다.  

select 태그의 name값은 departmentId이다.  
form이 submit될 때, 선택된 option 태그의 값이 request parameter로 전달된다.   
이 request parameter의 이름은 departmentId이다.   
</details>

<br/>

## 7. mybatis 실행과정       

<details markdown="1">  
<summary>view</summary>

### (1) request parameter 이해   
**Q.** request parameter 이란?  
**A.** 웹 브라우저에서 웹 서버에 URL의 실행은 요청할 때(http request)  
그 요청에 같이 담아 전송하는 데이터를 request parameter라고 부른다.  
즉, request parameter는 웹 브라우저에서 웹 서버로 전송되는 데이터이다.   

- 웹 브라우저 >> 서버 (전송되는 데이터)  
- 언제 전송? 웹 브라우저가 서버에 URL을 요청할 때, 그 요청과 함께 전송됨   
- http request는 언제 방생?
   + 웹 브라우저 주소칸에 URL을 입력하고 엔터   
   + a 태그 클릭 ( 요청되는 URL : a 태그의 URL )
   + submit 버튼 클릭 ( 요청되는 URL : submit 버튼을 포함하는 form 태그의 URL 혹은, action 애트리뷰트가 없으면 현재 URL)   
   
**Q.** 현재 URL은?  
**A.** 웹 브라우저에서 현재 URL은 웹 브라우저창의 주소이다.     
서버에서 현재 URL은 방금 웹 브라우저가 요청한 URL이다. (http request)      
 
**Q.** 무엇이 request parameter가 되는가?   
**A.** 자동으로 request parameter로 전송되는 것은 다음과 같다.     
URL의 query String, 압력폼에 입력된 데이터    

### (2) 실행과정    
이클립스에서 실행하는 경우에는 자동으로 웹 브라우저 창이 열리고 웹 브라우저창의 주소칸에 URL이 자동으로 입력된다.   

1. 웹 브라우저에서 http://localhost:8080/mybatis/student/list URL을 서버에 요청한다. (GET 방식)  

2. 요청된 URL에 연결된(request mapping) 액션 메소드가 실행된다.
없으면 404에러 발생   

3. StudentController의 list 메소드 실행됨  
studentMapper.selectAll() 메소드를 호출하여 리턴된 학생 목록을 model 객체에 추가함  

4. 뒤를 이어 실행될 뷰 이름을 리턴함   

5. "student/list" 뷰 이름 앞 뒤에 view prefix, view suffix가 붙는다. (application.properties 참고)  
/WEB-INF/views/student/list.jsp 파일이 실행된다.   

6. 뷰는 model에 들어있는 학생 목록 데이터를 출력한다. (model 데이터의 이름은 students)   
출력결과는 html태그, 출력된 html 태그들이 웹 브라우저로 전송되어 웹 브라우저창에 그려진다.  

------------------------------  
 
1. 웹 브라우저창의 학생 목록에서 고정희 학생 클릭   
클릭된 tr 태그의 onclick 이벤트 핸들러가 호출됨  
이 이벤트 핸들러는 common.js 파일의 javascript 코드에 의해서 tr 태그에 자동으로 등록되어 있다.  

2. 위 이벤트 핸들러는 다음과 같은 일을 수행한다.   
`<tr data-url="edit?id=3">`    
현재 웹 브라우저의 URL http://localhost:8080/mybatis/student/list   
결과 URL http://localhost:8080/mybatis/student/edit?id=3  
이 결과 URL이 서버에 요청됨. GET 방식  

3. 요청된 URL에 연결된(request mapping) 액션 메소드가 실행된다. 없으면 404에러   

4. request parameter로 전달된 id값에 해당하는 학생 레코드를 조회해서 model에 추가    
```
Student student = studentMapper.findOne(id);
model.addAttribute("student", student);
```

학과 목록 전체를 조회해서 model에 추가   
```
List<Department> departments = departmentMapper.findAll();
model.addAttribute("departments", departments);
```  

5. 뒤를이어 실행된 뷰 이름을 리턴함  

6. "student/list" 뷰 이름 앞 뒤에 view prefix, view suffix가 붙는다.    
edit.jsp 파일 실행   

7. 뷰는 model에 들어있는 학생 데이터를 form 태그와 input 태그에 출력한다.   
(model 데이터의 이름은 student)  
학과 목록 데이터를 select 태그와 option 태그에 출력한다.   
(model 데이터의 이름은 departments)      
출력 결과는 html 태그, 출력된 html 태그들이 웹 브라우저로 전송되어 웹 브라우저창에 그린다.   

**소스코드:**  
`<form:form method="post" modelAttribute="student">`  
**출력:**  
`<form id="student" action="/mybatis1/student/edit?id=3" method="post">`  

**소스코드:**  
`<form:input path="studentNumber" class="form-control w200" />`  
**출력:**   
`<input id="studentNumber" name="studentNumber" class="form-control w200" type="text" value="200032003"/>`  

**소스코드:**  
``` 
<form:select path="departmentId" class="form-control w200"  
            itemValue="id" itemLabel="departmentName" items="${ departments }" />  
``` 
**출력:**  
```   
<select id="departmentId" name="departmentId" class="form-control w200">  
  <option value="1">국어국문학</option>
  <option value="2">영어영문학</option>
  <option value="3" selected="selected">불어불문학</option>
  <option value="4">소프트웨어공학과</option>
  <option value="5">컴퓨터공학과</option>
  <option value="6">정보통신공학과</option>
  <option value="7">글로컬IT공학과</option>
</select>
``` 
   
--------------------------------------------------------------------------------------------

1. 웹브라우저 창에서 입력 폼의 데이터를 수정하고, 저장버튼을 클릭.  
저장 버튼은 submit 버튼.  
submit 버튼을 포함하고 있는 <form> 태그의 action 애트리뷰트값 URL이 서버에 요청된다.  
입력폼에 입력된 데이터가 request parameter로 전송된다.  <br/>
서버에 요청되는 URL: /mybatis1/student/edit?id=3  
POST 방식 요청  


2. 요청된 URL에 연결된(request mapping) 액션 메소드가 실행된다. 없으면 404 에러.  
```  
    @RequestMapping("/student")
    @Controller
    class StudentController {

    @RequestMapping(value="edit", method=RequestMethod.POST)
    public String edit(Model model, Student student) {
        studentMapper.update(student);
        return "redirect:list";
    }
```  
request parameter 데이터가 액션 메소드 파라미터 객체에 채워져 전달된다. (Student 객체)  
request parameter 데이터 이름(name)과 Student 객체의 set 메소드 이름이 일치해야 한다.  


3. Student 객체에 채워져 전달된 request parameter 데이터가 DB에 저장된다.  
    studentMapper.update(student);  


4. "list" URL로 리다이렉트 하라는 메타 태그가 웹브라우저에 전송된다.  
    return "redirect:list";  

     "list" 상대 URL  
     현재 서버에 요청된 URL: http://localhost:8080/mybatis1/student/edit?id=3  
     결과URL: http://localhost:8080/mybatis1/student/list  

5. 리다이렉트 메타 태그를 받은 웹브라우저는 요청된 URL을 서버에 즉시 요청한다.  
     http://localhost:8080/mybatis1/student/list  

</details>
