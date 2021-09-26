# Spring Form Validation2           

### 1. validation message 수정        
#### (1) 목표     

<img width="536" alt="스크린샷 2019-12-03 오후 11 17 42" src="https://user-images.githubusercontent.com/33855307/70058923-29d96180-1623-11ea-9632-bb2190683411.png">

위 화면에서 자동으로 출력된 에러 메시지 문구를 수정하자.      

각 validation annotation에 대한 에러 메시지를 ValidationMessage.properties 파일에 등록해 주면 된다.     

### (2) ValidationMessage.properties 생성          

```
javax.validation.constraints.Size.message=크기가 {min} 이상 {max} 이하이어야 합니다.
org.hibernate.validator.constraints.Email.message=이메일 주소가 바르지 않습니다.
org.hibernate.validator.constraints.NotEmpty.message=필수 입력항목입니다. 
```
javax.validation.constraints.Size.message 는 @Size 어노테이션 클래스의 경로명                   
org.hibernate.validator.constraints.Email.message 는 @Email 어노테이션 클래스의 경로명           
org.hibernate.validator.constraints.NotEmpty.message 는 @NotEmpty 어노테이션 클래스의 경로명             

### 2. 개별 항목에 대한 에러 메시지 등록          
#### (1) 목표            
@NotEmpty 어노테이션에 위배되는 모든 항목에 "필수 입력 항목입니다." 에러메시지가 동일하게 출력된다.      
항목에 따라서 다른 에러 메시지가 표시되어야 하는 경우에는         
@NotEmpty 어노테이션에 message 애트리뷰트 값으로 에러 메시지를 등록하면 된다.      

#### (2) message 애트리뷰트     
어떤 항목의 @NotEmpty 에러 메시지를 다른 것으로 바꾸려면,      
이 어노테이션에 message 애트리뷰트 값으로 에러 메시지를 등록하면 된다.      

Example
```java
@NotEmpty(message="학번을 입력하세요")
@Size(min=6, max=12, message="6 자리 이상 12 자리 이하이어야 합니다.")
@Min(value=1, message="양의 정수를 입력하세요")
```

``` @Min(1) ```     
위와같이 값이 한개인 어노테이션에서 그 값 한개인 애트리뷰트 이름은 value이다.       
애트리뷰트 값이 하나 뿐일 때, value 이름을 생략할 수 있다.      
하지만, 값이 여러개 일 때는 애트리뷰트 이름을 생략할 수 없다. >> ``` @Min(value=1, message="error message") ```        


### 3. 로직 에러 처리하기         
#### (1) 목표     
validation annotation에 의해서 자동으로 검사되기 힘든 에러들도 있다.     
예를들어, 사용자 아이디 중복을 검사하려면 DB의 user 테이블을 조회해야 한다.      
입력된 두 비밀번호가 일치하는지 검사하려면, 두 멤버변수를 비교해야 한다.       
이런 에러 검사는 validation annotation으로 구현하기 힘들고 따로 직접 구현해야 한다.      

- 로직 에러 처리하기 절차      

(1) spring form validation 에러가 있다면, 회원가입 뷰 이름을 리턴한다.    
회원가입 뷰 이름이 리턴되면 웹 브라우저 창에 회원가입 화면이 다시 출력된다.   

(2) 입력된 두 비밀번호가 일치하는지 검사한다.    

(3) 일치하지 않으면 비밀번호 불일치 에러이다.   
&emsp;	(3-1) bindingResult 객체에 비밀번호 불일치 에러메시지를 등록한다.   
&emsp;	(3-2) 회원가입 뷰 이름을 리턴한다.    

(4) 입력된 사용자 아이디로 DB의 사용자 테이블에서 조회한다.    

(5) 조회결과가 null이 아니면, 사용자 아이디 중복 에러이다.   
&emsp;	(5-1) bindingResult 객체에 사용자 아이디 중복 에러 메시지를 등록한다.   
&emsp;	(5-2) 회원가입 뷰 이름을 리턴한다.        


- bindingResult 객체에 에러 메시지 등록하기   

```bindingResult.rejectValue("password2", null, "비밀번호가 일치하지 않습니다.");```  
rejectValue 메소드를 호출하여 에러 메시지를 등록한다.      

첫번째 파라미터 : 에러가 발생한 멤버변수 이름    
세번쨰 파라미터 : 에러 메시지        

bindingResult에 위 에러가 등록되면, 아래 태그에 그 에러 메시지가 출력된다.           
``` <form:errors path="password2" class="error" /> ```           


#### (2) UserService.java        
```java
public boolean hasErrors(UserRegistrationModel userModel, BindingResult bindingResult){
	if(bindingResult.hasErrors()){
		return true;
	}
		
	...

}
```

#### (3) UserController.java           
```java
public String register(...){
	if(userService.hasErrors(userModel, bindingResult)){
		return "user/register";
	}

	...

}
```
**Q.** 아까 입력했던 내용이 view에 어떻게 나타나는걸까?    
**A.** 액션 메소드의 파라미터가 객체일 때, 자동으로 일어나는 일은      
userModel이 model에 자동으로 addAttribute 된다. (userRegistration이라는 이름으로)        





