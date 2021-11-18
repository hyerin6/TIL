# spring form    
액션 메소드에서 request parameter를 받는 방법   
select 태그 입력 구현, spring form 확장 태그   
<br/>
## 1. form0 프로젝트 생성   

<br/>

## 2. edit1 구현   
### (1) 개요   
1. 웹 브라우저의 입력폼에 입력된 데이터 >> 서버의 액션 메소드   
- 사용자가 입력폼에 데이터를 입력하고 submit 버튼을 누르면, 입력된 데이터가 서버에 전송된다.   
- 서버에 전송된 데이터는 서버의 액션 메소드의 파라미터 변수에 채워져 전달된다.  
- http request에는 URL, request parameter, method(GET, SET), cookie, body, header 등      

**@RequestParam("이름") 어노테이션 사용하여 request parameter 데이터 받기**    
```
public String edit1(Model model, @RequestParam("title") String title, 
                                 @RequestParam("color") int color){
```  

<br/>  

2. 서버의 액션 메소드에서 서버의 뷰로 전달되는 데이터    
- 액션 메소드의 파라미터 변수에 Model 클래스 객체가 선언되어 있다.      
예) `public String edit1(Model model){ `     

- 이 model 객체에 데이터를 채운다.  
```
model.addAttribute("title", title);
model.addAttribute("color", color);
```  
**Q.** @RequestParam 어노테이션을 사용하는 이유는?  
**A.** 컴파일하면 변수명이 없어진다. 그래서 '이 값은 이 이름이다.' 라고 알려주기 위해 어노테이션을 사용해서 변수명을 알려준다.  
하지만 여러 값을 보내는 경우, 그냥 클래스의 객체로 받으면 된다.    

<br/>

3. 뷰에서 데이터 출력하기   
```
<input type="text" name="title" value="${title}" class="form-control">

<select name="color" class="form-control">
  <option value="1" ${color == 1 ? "selected" : ""}> 빨강 </option>
  ...
</select>
```    

${color == 1 ? "selected" : ""} 이 부분은 JSP EL(jsp expression language)로   
java가 아니기 때문에 꼭 equals를 사용할 필요는 없다.   

출력된 데이터와 태그들이 웹 브라우저에 전송된다.   
확장 태그를 사용하지 않으면 위 예제처럼 구현해야 한다.   

<br/>

### (2) Form1Controller.java  
- 액션 메소드의 이름과 URL은 전혀 상관없다.   

- spring은 context path를 알아서 spring한테 url 줄 때는 context path를 적지 않아도됨   
액션 메소드를 호출하는 것 -> spring      
즉, 액션 메소드의 url 에는 context path를 적어주지 않아도 된다. 리다이렉트 URL도 마찬가지이다.   
상대 URL이면 당연히 적어줄 필요없고 절대 URL도 적어주지 않는다.      
하지만 views 파일은 spring이 아니라서 그대로 웹 브라우저에 출력된다, 때문에 절대 URL을 입력할 때 context path가 필요하다.    

- 액션 메소드의 return 값이 리다이렉트 URL인 경우, 절대 URL로도 적을 수 있는데   
context path는 적으면 404 not found 에러 발생  

- 액션 메소드의 request mapping 에 상대 경로 URL의 경우,     
context path 적으면 마찬가지로 404 not found 에러 발생   

- 클래스에 @RequestMapping을 적어주지 않으면 모든 메소드의 @RequestMapping 경로가 절대경로가 된다.    
이때 절대경로지만 context path를 적으면 안된다.    
context path는 적으면 404 not found 에러 발생    

- 프로젝트에 내장된 톰캣 emebeded tomcat (디폴트)context path = '/' -> run on server    
내장되지 않은 톰캣 external tomcat (디폴트)context path = '/프로젝트명' -> run as server  
예) http://localhost:포트번호/context path 생략  
http://localhost:포트번호/context path 생략하지 않음  

<br/>

## 3. edit2   
### (1) 개요   
1. 웹 브라우저의 입력폼에 입력된 데이터 >> 서버의 액션 메소드   
- 사용자가 입력폼에 데이터를 입력하고 submit 버튼을 누르면, 입력된 데이터가 서버에 전송된다.   
- 서버에 전송된 데이터는 서버의 액션 메소드의 파라미터 변수에 채워져 전달된다.   

**액션 메소드의 파라미터 객체를 사용하여 request parameter (입력한 데이터 + query String) 데이터 받기**    
```
@RequestMapping(value="/form1/edit2", method=RequestMethod.POST)
public String edit2(Model model, Data data){
```  

- 액션 파라미터 변수가 객체인 경우에 이 객체의 속성에 데이터가 자동으로 채워진다.   

- 객체의 속성이란? getter과 setter로 정의된다. (예: getTitle, setTitle)  

- 객체로 받는 것이면 @RequestParam() 어노테이션이 필요없다. 자동으로 채워진다.  
하지만 객체가 아니라 속성으로 받는 것이면 어노테이션이 필요하다.   
어노테이션을 적었는데 전달되는 것이 없으면 400 에러 발생한다.   
예제 코드는 form으로 받기 때문에 "" 빈 문자열이 전달되어 에러가 발생하지 않는다.     

- 객체를 전달하는 것은 컴파일해도 메소드 이름이 남아있고 속성에 NULL이 있을 수 있기 때문에     
입력하지 않아도 에러가 발생하지 않는다.  

<br/>

2. 서버의 액션 메소드에서 서버의 뷰로 전달되는 데이터  
- 뷰 데이터 전달하기  
액션 메소드의 파라미터가 객체이면 이 객체는 자동으로 model에 추가된다.  
추가되는 모델 데이터의 이름은 그 객체의 클래스 이름에서 첫문자만 소문자로 바꾼 문자열이다.  

즉, edti2 액션 메소드의 파라미터가 Data 객체이므로 자동으로 아래 문장이 실행된다.  
`model.addAttribite("data", data);`  
위 문장이 spring에 의해 자동으로 실행되기 때문에 edit2 액션 메소드에 위 문장을 구현할 필요가 없다.  

**Q.** 변수명이 아닌 클래스명(첫문자만 소문자로 변경)으로 채워지는 이유는?    
**A.** 자바에서 변수명은 컴파일하면 사라지지만 클래스명은 사라지지 않는다.   
때문에 변수명이 아니라 클래스명으로 채워진다.  

<br/>

### (2) Form1Controller.java  

### (3) edit2.jap     
**Q.** name에 오타가 발생하면 에러가 발생하는가?  
**A.** 에러가 발생하지 않는다. getTitle은 null을 return 한다. (그냥 채워지지 않는 것)  

<br/>

## 4. edit3  
### (1) 개요  
**뷰에서 데이터 출력하기**  
```
<form:form method="post" modelAttribute="data">
    <form:input path="title" class="form-control"/>
    <form:select path="color" class="form-control">
        <form:option value="1" label="빨강"/>
        ...
    </form:select>
```  
spring form 확장 태그를 사용하면,   
input 태그에 value 애트리뷰트 값이 자동으로 출력되고   
select 태그의 자식 option 태그에 select 애트리뷰트가 자동으로 출력된다.  

















