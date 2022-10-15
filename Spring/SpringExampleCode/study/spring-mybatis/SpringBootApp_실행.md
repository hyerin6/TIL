# Spring Boot App 실행   
## 1. 개요     
- Spring Boot App  
Spring Boot App 실행 기능을 사용하면, spring 프로젝트를 좀 더 빠르게 실행해 볼 수 있다.    

- embedded tomcat   
Spring Boot App 프로젝트를 빌드 결과 파일(*.war)에 tomcat 서버가 포함되어 있기 때문에,   
tomcat 서버나 Pivotal tc Server 없이도 실행 가능하다.    
프로젝트 빌드 결과 파일에 내장된 서버를 embedded tomcat 이라고 부른다.    

- spring-boot-devtools          
소스코드를 수정한 후 웹브라우저를 새로고침하면, 수정된 내용이 즉기 반영되도록 해주는 도구이다.    
    
- Spring framework = spring core + spring web mvc   

<br/>

## 2. 프로젝트   
(1) 프로젝트 생성 과정 생략  

(2) pom.xml 수정  

dependency 항목에 필요한 라이브러리를 등록하면 maven이 자동으로 다운로드 해준다.   
Maven은 프로젝트 생성, 빌드, 실행까지 종합관리해주는 프로젝트 개발 도구   
Maven 설정 파일 = pom.xaml  

추가된 maven dependency 항목은 다음과 같은 라이브러리를 프로젝트에 포함하기 위함이다.     

- JSTL(JSP Standard Tag Lib)    
JSTL 확장 태그를 사용하기 위해 필요한 라이브러리    
예를 들어,   
<c:forEach> - 반복문 대신 사용할 수 있는 태그    
<c:if> - if문 대신 사용할 수 있는 태그     

- tomcat-embed-jasper  
스프링 부트 앱에는 톰캣 서버가 내장되어 있다.    
톰캣 서버는 servlet container 이다.  
톰캣 서버는 servlet을 실행하는 서버이다.   
톰캣에서 JSP 파일은, 먼저 servlet이로 변환(컴파일)된 후 실행된다.  </br></br>
tomcat-embed-jasper는 JSP 파일을 servlet으로 변환하는 컴파일러이다.  
이 것을 프로젝트에 추가하지 않고 실행하면, JSP 파일이 실행되지 않고, 웹브라우저에서 다운로드 될 것이다.   
톰캣은 servlet만 실행할뿐, 다른 파일들은 웹브라우저로 그대로 전송하기 때문이다.  

(3) application.properties  
**src/main/resources**  
```
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
server.port = 8088
```
Spring boot의 설정 파일 = application.properties  

내장된 톰캣 서버가 실행될 디폴트 포트는 8080인데, 8080 포트를 사용하는 다른 서버와 충돌이 발생할 확률이 높기 때문에  
8088 포트로 변경했다.  

(이미 사용중이라면, 'port 8080 failed to start. The port may already be in use' 포트 충돌 에러가 발생)  

(4) HomeController.java  
**src/main/java/net/skhu/HomeController.java**  
```
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // spring에게 Controller라는 것을 알려주는 것, Controller 어노테이션은 꼭 필요하다. 
public class HomeController {
   
   @RequestMapping("/")   
    public String index(Model model) { // view 에게 전달될 데이터를 변수 model에 전달 
        model.addAttribute("message", "좋은 아침");
        return "index"; // view의 이름이 index, view의 이름이나 리다이렉트 url을 반환한다.
    }
}
```

(5) index.jsp  
**src/main/webapp/WEB-INF/views/index.jsp (view를 jsp로 구현)**   

**MVC : Model View Controller**    
(모형, 사람이 볼 수 있는 출력을 담당하여 보여주는 객체, 지휘/통제)       
가장 먼저 나온 객체지향 설계 패턴이다.    

jsp 에서는 WebContant 아래의 파일들을 url로 요청할 수 있다.    
WEB-INF는 url로 웹 브라우저로 요청해서 받으면 안되는 파일을 모아두는 곳이다.  
예) 서버 설정, 웹 브라우저에서 사용하는 데이터, 웹 브라우저 실행에 필요한 라이브러리 등    
즉, spring 에서는 url 요청을 view가 받지 않고 Contrllor가 받아야 한다.   

(6) 실행 - http://localhost:8088  

<br/>

## 3. 정적 파일    
Spring Boot App 프로젝트에서 정적 파일들은 (*.css, *.js)  
src/main/resources/static 폴더에 위치해야 한다.  

(1) common.css  
**src/main/resources/static/common.css**  
```
h1 { color: red }
```

(2) common.js    
```
$(function() {
	$("h1").click(function() {
		alert("clicked");
	})
})
```
h1 태그를 클릭하면, alert 대화상자가 나타난다.    

<br/>

## 4. context path  
hello 프로젝트에 내장된 tomcat 서버에서 실행할 때 URL은 다음과 같다.  
http://localhost:8080   

hello 프로젝트를 tomcat 서버에 설치(배포)하고 실행할 때 URL은 다음과 같다.  
http://localhost:8080/hello1  

tomcat 서버에 프로젝트를(서버 애플리케이션을) 여러 개 설치하는 경우에,   
각각의 이름을 구분할 수 있어야 한다.  
tomcat 서버에 설치된 프로젝트(서버 애플리케이션)의 이름을 context path 혹은 context name 이라고 부른다.  
보통 프로젝트 이름이 context path가 된다.  
http://localhost:8080/hello1 URL에서 /hello1 부분이 context path 이다.  

프로젝트에 내장된 tomcat 서버에서 실행할 때는,   
이름을 구별할 다른 서버 애플리케이션이 있지 않으므로    
context path 값이 / 로 지정되고,   
그래서 URL은 http://localhost:8080/ 이거나 http://localhost:8080 이다.   

즉 URL에서 context path 값은  
프로젝트 이름이거나 (http://localhost:8080/hello1)  
/ 이다. (http://localhost:8080)  

프로젝트 개발이 끝난 후, 서버에 설치할 때 URL도 마찬가지이다.  
예를 들어 소프트웨어 공학과 홈페이지 프로젝트가 sw 이었다면,  
개발이 끝난 후 서버에 설치하여 운영할 때 URL은  
http://home.skhu.ac.kr/sw 이거나. (context path: /sw )  
http://sw.skhu.ac.kr 이다. (context path: / )  


context path를 변경하려면,  
Project - Properties 메뉴 - Web Project Settings - Context root 값 변경  
