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


