## Springboot Web MVC   
Springboot는 Web MVC 개발을 바로 시작하는 것이 가능하다.           
Springboot-AutoConfiguration이 존재하기 때문에 Springboot에서는 별다른 설정 없이 바로 웹 개발이 가능하다.    

Spring MVC의 기능을 사용하며 추가적인 설정이 필요한 경우라면    
WebMvcConfigurere의 인터페이스를 구현하여 커스터마이징 가능하다.       

<br />
<br />

## HttpMessageConverters  
스프링 프레임워크에서 제공하는 인터페이스이며 Spring MVC 일부분이다.    
Http 요청 본문으로 들어오는 것을 객체로 변환하거나 응답 본문을 객체로 변환해서 전송할 때 주로 사용한다.    

`@RequestBody`와 `@ResponseBody`를 사용해서 메시지 컨버터를 사용할 수 있다.                    
`@RestController`를 사용하며 `@ResponseBody`를 명시하지 않아도 된다.            

<br />
<br />

## ViewResolver   
ViewResolver의 한 종류인 ContentNegotiatingViewResolver에 대해 알아보자.             
ContentNegotiatingViewResolver는 들어오는 요청의 accept-header에 따라 응답이 달라진다.          

<br />
<br />


## ExceptionHandler  
스프링 부트 애플리케이션을 실행하면 기본적으로 ErrorHandler가 등록되어있다.    
기본적인 에러 핸들러는 BasicErrorHandler에 등록되어있다.    
직접 ExceptionHandler를 구현하는 것이 가능한데 `@ExceptionHandler`를 사용하면 된다.   

<br />
<br />

## 서블릿 애플리케이션   
#### 서블릿이란?    
자바 엔터프라이즈 에디션은 웹 애플리케이션 개발용 스펙과 API를 제공한다.   
요청 당 스레드 사용    

<br />

#### 서블릿의 장점      
* 빠르다.    
* 플랫폼 독립적이다.    
* 보안     
* 이식성      

<br />

#### 서블릿 엔진 또는 서블릿 컨테이너   
서블릿 스펙을 준수하는 컨테이너들이다.   
이들의 역할은 다음과 같다.   
* 세션 관리 
* 네트워크 서비스 
* MIME 기반 메시지 인코딩, 디코딩 
* 서블릿 생명주기 관리 

서블릿 컨테이너가 있어야 서버 실행이 가능하다.    

<br />

#### 서블릿 생명 주기   

```text
1. 서블릿 컨테이너가 서블릿 인스턴스의 init() 메소드를 호출하여 초기화 
```
최초 요청을 받았을 때 한번 초기화하고 나면 그 다음 요청부터는 이 과정을 생략한다. 


```text
2. 서블릿이 초기화된 다음부터 클라이언트의 요청 처리 가능, 
각 요청은 스레드로 처리하고 이때 서블릿 인스턴스의 service() 메소드를 호출한다. 
```

* HTTP 요청을 받고 클라이언트로 보낼 HTTP 응답을 만든다. 
* `service()`는 보통 HTTP method에 따라 `doGet()`, `doPost()` 등으로 처리를 위임한다.  
* 그래서 보통 `doGet()`, `doPost()`를 구현한다.   

```text
3. 서블릿 컨테이너 판단에 따라 해당 서블릿을 메모리에서 내려야 할 시점에 detroy()를 호출한다.   
```

<br />
<br />


## 서블릿 리스너   
서블릿 리스너의 역할은 서블릿 컨테이너가 구동될 때 DB 커넥션을 맺어놓고 DB 커넥션을 서블릿 애플리케이션에서 만든 여러 서블릿에세 제공해줄 수 있다.   

이때 서블릿들이 DB 커넥션이 필요한 이벤트를 감지해서 서블릿 컨테이너에게 커넥션을 가져오자고 할 때 사용하는 것이 서블릿 리스너이다.   

웹 애플리케이션에서 발생하는 주요 이벤트를 감지하고 각 이벤트에 특별한 작업이 필요한 경우 사용한다.   

