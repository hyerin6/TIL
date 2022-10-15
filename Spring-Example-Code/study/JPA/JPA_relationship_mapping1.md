# JPA relationship mapping   

### 1. 배경지식    

## (1) Single-Valued Association 구현   
department와 employee 관계의 구현은 외래키가 포함된 employee 객체에 department 멤버변수를 구하는 것이 기본이다.    

**@ManyToOne 어노테이션**  
```java
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    int id;
    String name;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    Department department;
}
```
엔티티 클래스들 사이의 1 대 다 관계를 구현할 때  
외래키를 포함하는 엔티티 클래스(Employee)에서 @ManyToOne 어노테이션을 사용하여   
상대측 엔티티 객체 하나를 구현하는 것이 기본이다.   

**(주의!)**
외래키인 departmentId 멤버 변수를, Employee 에 만들지 않음에 주의하자.   
departmentId 대신 department 멤버 변수를 만들어야 한다.   


#### (2) Collection-Valued Association 구현   
Collection-Valued Association은 필수가 아니고 선택이다.   

department와 employee 관계를 구현할 때   
1 대 다의 관계에서 1에 해당하는 department 객체에   
그 부서 소속 직원들의 목록에 해당하는 List<Employee> employees; 멤버변수를 구현할 수 있다.   

Department 객체로부터 소속 직원인 Employee 목록을 구할 필요가 있을 때만 구현한다.   

**@OneToMany 어노테이션**  
```java
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;

    @OneToMany(mappedBy="department") // Employee 엔티티 클래스에서 @JoinColumn 어노테이션이 붙은 멤버변수
    List<Employee> employees;
// 이게 자동이 되려면 외래키로 등록이 되어 있어야 한다.
```

department 테이블의 레코드를 조회할 때, 다음 절차가 실행된다.   
(1) department 테이블에서 레코드가 하나 조회된다.   
(2) Department 객체가 한 개 생성된다.  
(3) 조회된 레코드가 Department 객체에 채워진다.  
(4) 조회된 department 레코드의 id 값과, departmentId 값이 일치하는 employee 레코드들이 조회된다.  
(5) Employee 객체가 여러 개 생성된다.  
(6) 조회된 employee 레코드들이 Employee 객체에 채워진다.  
(7) Employee 객체들이 List<Employee> 객체에 add 된다.  
(8) List<Employee> 객체가, Department 객체의 employees 멤버 변수에 대입된다.  

위 절차에서 1 ~ 3 부분은 department 테이블의 레코드를 조회하자마자 즉시 실행되지만,  
4 ~ 8 부분은 나중에 실행될 수도 있다.  

1 ~ 3 부분과 동시에 4 ~ 8 부분도 실행하는 정책을 Eager Loading 이라고 부른다.  
4 ~ 8 부분의 실행을 최대한 뒤로 미루어서 꼭 필요할 때만 실행하는 정책을     
Lazy Loading 이라고 부른다.   

**Q.** 그럼 언제 조회하는가?     
**A.** Department 객체의 getEmployees() 메소드가 처음으로 호출될 때, 실행될 가능성이 높다.    

처음 저 값을 가져올 때는 DB에서 가져와야 하기 때문에 조금 느릴 수 있고    
두번째로 로딩했을 때는 이미 값을 채워넣었었기 때문에 처음보다는 빨라진다.     

언제 처음으로 호출되는지 지켜보고 있다가 null이 들어가지 않게 가로채서 DB에 있는 목록들을 조회해와서 멤버변수에 채워넣고 리턴한다.          
👉🏻 외부에서 보면 약간 느려보일 수 있지만 어쨌든 목록이 조회되는 것,,          
👉🏻 실행되기 직전에 어떻게 가로채는 것? : **proxy 패턴** (spring의 AOP 매커니즘과 같다.)             


## (3) Eager Loading / Lazy Loading     
## Eager Loading 정책     
### 장점          
어차피 시장에 왔으니, 필요할 것 같은 상품들을 다 사오는 것이 편하다 !            
어차피 DB에 연결해서 명령을 실행하고 데이터를 받아오고 있으니, 필요할 것 같은 데이터들은 한 번에 다 가져오는 것이 좋다.           
### 단점         
사온 상품이 필요 없다는 것을 알게 되면 돈 낭비다.         
가져온 데이터를 결국 사용하지 않게 된다면, 자원 낭비다.      

## Lazy Loading        
### 장점        
불필요하게 많은 데이터를 조회하는 자원 낭비를 줄일 수 있다.      

### 단점    
DB에서 가져오지 않은 데이터가 필요하다는 것을 나중에 알게 되면   
또 DB에 연결해서 데이터를 받아와야 한다.    
한 번 DB에 연결해서 필요한 것을 다 받아오는 것보다, 하나씩 따로 받아오는 것이 비효율적이다.    
시장에 자주 왔다갔다 하려면, 시간이 많이 걸린다.    


## 어떤 정책?    
Single-Valued Association 객체 생성은 Eager Loading일 확률이 높다.         
Collection-Valued Association 객체 생성은 Lazy Loading일 확률이 높다.          
확실히 정해진 것은 아니고 확률이 높을 뿐이다.           
확실한 것은 JPA 엔진을 구현하는 사람 맘대로이다.          

이 정책을 선택하는 것은 성능 상의 차이만 있을뿐 기능상의 차이는 없다.          
Lazy Loading / Eager Loading 정책의 차이는 언제 데이터 조회를 하는가 이다.          

Lazy Loading 정책은, 데이터 조회를 가급적 미루자는 것일 뿐이다.           
두 정책 모두, 데이터를 사용할 때는 언제나 그 데이터가 이미 조회되어 있다.              
따라서 기능상의 차이는 없다.          

## 정책 설정  
@ManytoOne(fetch = FetchType.EAGER)        
@OneToMany(fetch = FetchType.LAZY)       
@OneToOne(fetch = FetchType.EAGER)           
 
위와 같이 FetchType.LAZY, FetchType.EAGER 를 설정해 줄 수 있다.        
그렇지만, 이 설정이 언제나 지켜진다는 보장은 없다.       
확실한 것은 JPA 엔진을 구현하는 사람 맘대로이다.      



## (4) Infinite Recursion 에러와 @JsonIgnore 어노테이션     
엔터티 클래스들 사이의 관계를, @ManyToOne, @OneToMany 등의 어노테이션을 사용하여 구현할 때,  
두 엔터티 클래스 양쪽 다 구현하면,    
"Could not write JSON: Infinite recursion" 에러가 발생한다.  

예를 들어 Employee 클래스에서, @ManyToOne 어노테이션을 사용하여 Department 멤버 변수를 구현하고  
Department 클래스에서, @OneToMany 어노테이션을 사용하여 List<Employee> 멤버 변수를 구현하면,  
Department 객체나 Employee 객체를 JSON 포멧으로 변환할 때,  
"Could not write JSON: Infinite recursion" 에러가 발생한다.  

그 이유는 다음과 같다.      
(1) Employee 객체를 JSON 포멧으로 변환할 때, department 멤버 변수 값도 포함된다.      
(2) department 멤버 변수 값은 Department 객체이다. 이 값도 JSON 포멧으로 변환된다.      
(3) Department 객체를 JSON 포멧으로 변환할 때, employees 멤버 변수 값도 포함된다.       
(4) employees 멤버 변수는 List<Employee> 객체이다. 이 객체도 JSON 포멧으로 변환된다.      
(5) List<Employee> 객체가 JSON 포멧으로 변환될 때,        
    이 객체에 포함된 Employee 객체도 JSON 포맷으로 변환된다.         
(6) 위의 1번 절차로 점프한다.     

위 (1)~(6) 절차가 무한 루프로 반복된다. (Infinite recursion)       

이 에러를 피하려면, Depatment 객체를 JSON 포멧으로 변환할 때,        
employees 멤버 변수를 포함하지 말아야 한다.       
객체를 JSON 포멧으로 변환할 때 무시해야할 멤버 변수에 @JsonIgnore 어노테이션을 붙여야 한다.       
이 어노테이션을 employees 멤버 변수에 붙이면,          
"Could not write JSON: Infinite recursion" 에러가 발생하지 않는다.         

**예)**       
{ name : “A”,  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Friend : { name : “B”,   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Friend : { name : “A”,    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Friend : { “B”,   ...

JSON 양방향 참조는 무한 반복이다.   

