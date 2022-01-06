# Web

<br />

## HttpSession 동작

Controller에서 HttpSession을 사용하려면 다음과 같은 방법이 있다.

(1) HttpSession 파라미터를 추가한다.
→ 항상 HttpSession 생성

(2) HttpServletRequest 파라미터를 추가하고 HttpServletRequest를 이용해 HttpSession 구현
→ 필요한 시점에만 생성 가능

<br />

```
HttpSession.getAttribute("user");

사용자 A가 접속해도 "user" key로 값을 가져오고, 
사용자 B가 접속해도 "user" key로 값을 가져온다. 
같은 key를 사용하는데 어떻게 구분할까?
```

JSESSIONID 라는 것에 대해 먼저 알아보자.

<br />

#### JSESSIONID

* 톰캣 컨테이너에서 세션을 유지하기 위해 발급하는 키
* 상태를 저장하기 위해 톰캣은 JSESSIONID 쿠키를 클라이언트(브라우저)에게 발급해주고 이 값을 통해 세션을 유지할 수 있도록 한다.

<br />

#### 전체 흐름

(1) 클라이언트(웹 브라우저 사용자)가 처음으로 웹 어플리케이션을 방문하거나 `request.getSessio()`을 통해
HttpSession을 처음으로 가져오면 서블릿 컨테이너는 새로운 HttpSession 객체를 생성하고 unique한 ID를 생성해 서버의 메모리에 저장한다.

(2) 서블릿 컨테이너는 JSESSIONID란 이름을 key로, 생성한 session ID를 value로
HTTP 응답의 Set-cookie로 설정한다.

(3) 브라우저는 다음 요청부터 Request Headers에 JSESSIONID를 담아서 서버로 요청을 보낸다.

(4) 서블릿 컨테이너는 들어오는 모든 HTTP Request의 cookie header에서 JSESSIONID 라는 이름의 cookie가 있는지 확인하고
해당 값 (Session ID)을 사용하여 서버의 메모리에 저장된 HttpSession을 가져온다.

<br />

#### Thread Safety

서블릿과 필터는 모든 request에서 공유된다.
멀티 스레드와 다른 스레드(HTTP request)는 동일한 인스턴스를 사용할 수 있다.
그렇지 않으면 매 request마다 init() 및 destroy()를 다시 실행하기에는 너무 많은 비용이 든다.

또한 request나 session에서 사용하는 데이터를 서블릿이나 필터의 인스턴스 변수로 할당해서는 안된다.
다른 session의 모든 request 간에 공유되어 스레드로부터 안전하지 않다.

<br />
<br />

## Servlet

클라이언트의 요청을 처리하고 그 결과를 반환하는 Servlet 클래스의 구현 규칙을 지킨 자바 웹 프로그래밍 기술     
즉 요청에 대해 동적으로 처리해주는 역할로 Server Side에서 작동(WAS)   

<br />

#### Servlet 특징

* 클라이언트의 요청에 대해 동적으로 작동하는 웹 어플리케이션 컴포넌트
* html을 사용하여 요청에 응답한다.
* Java Thread를 이용하여 동작한다.
* MVC 패턴에서 Controller로 이용된다.
* HTTP 프로토콜 서비스를 지원하는 `javax.servlet.http.HttpServlet` 클래스를 상속받는다.

<br />

#### Servlet 동작 방식    
<img width="650" src="https://user-images.githubusercontent.com/33855307/148338820-43d31bb4-999e-43c4-b86d-23427dc77d9b.png">  

(1) 사용자가 URL을 입력하면 HTTP Request가 Servlet Container로 전송한다.      
(2) 요청을 전송받은 Servlet Container는 HttpServletRequest, HttpServletResponse 객체를 생성한다.       
(3) web.xml을 기반으로 사용자가 요청한 URL이 어느 서블릿에 대한 요청인지 찾는다.      
(4) 해당 서블릿에서 service 메소드를 호출하고 클라이언트의 GET, POST 여부에 따라 `doGet()` 또는 `doPost()` 호출      
(5) `doGet()` 또는 `doPost()` 메소드는 동적 페이지를 생성한 후 HttpServletResponse 객체에 응답을 보낸다.        
(6) 응답이 끝나면 HttpservletRequset, HttpServletResponse 두 객체를 소멸시킨다.        

<br />

#### Servlet Container (서블릿 컨테이너)  
서블릿을 관리해주는 역할이 서블릿 컨테이너이다.  
서블릿 컨테이너는 클라이언트의 요청을 받아주고 응답할 수 있도록 웹 서버와 소켓으로 통신한다.    
즉 서블릿이 웹서버와 통신할 수 있도록 해준다.    

대표적인 예로 톰캣(Tomcat)이 있다.        
톰캣은 실제로 웹 서버와 통신하여 JSP(자바 서버 페이지)와 Servlet이 동작하는 환경을 제공해준다.   
    
<br />

#### Servlet Container 역할 
(1) 웹 서버와 통신 지원   
서블릿 컨테이너는 서블릿과 웹 서버가 쉽게 통신할 수 있게 해준다.    

(2) 서블릿 생명주기 관리   
서블릿 클래스를 로딩하여 인스턴스화하고, 초기화 메소드를 호출하고 요청이 들어오면 적절한 서블릿 메소드를 호출한다.      
서블릿 사용이 끝나면 적절하게 GC를 진행하여 편의를 제공한다.    

(3) 멀티스레드 지원 및 관리   
서블릿 컨테이너는 요청이 올 때 마다 새로운 자바 스레드를 하나 생성하는데   
HTTP 서비스 메소드를 실행하고 나면, 스레드는 자동으로 죽는다.   

(4) 선언적인 보안 관리   
서블릿 컨테이너를 사용하면 개발자가 보안에 관련된 내용을 구현해 놓지 않아도 된다.   

<br />

#### Dispatcher Servlet   
* 클라이언트가 요청을 주며 Servlet Container가 요청을 받는다.     
  이때 제일 앞에서 서버로 들어오는 모든 요청을 처리하는 front controller라는 것을 spring에서 정의했고   
  이를 Dispatcher Servlet 이라고 한다.   
* 기존에 모든 Servlet에 대해 URL 매핑을 활용하기 위해 web.xml에 모두 등록해야 했는데     
  Dispatcher Servlet이 해당 애플리케이션으로 들어오는 모든 요청을 핸들링해주면서 작업의 효율이 높아졌다.     



<br />






