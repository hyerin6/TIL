# Server   
<br />      

## WAS & Web Server 
<https://hyerin6.github.io/2021-07-24/ws-was/>   

<br />

#### Web Server 
* 클라이언트가 서버에 페이지 요청을 하면 요청을 받아 정적 컨텐츠(html, png, css 등)를 제공하는 서버이다.  
* 클라이언트에서 요청이 올 때 가장 앞에서 요청에 대한 처리를 한다. 
* 정적 컨텐츠를 요청했는가?
   - OK → 응답한다. (response)
   - NO → 웹 서버에서 간단히 처리하지 못함, WAS에 부탁하여 WAS가 처리한 컨텐츠를 받아 응답한다. (response)
* 예) Nginx, Apache

<br />

#### WAS
* 동적 컨텐츠를 제공하기 위해 만들어진 애플리케이션 서버 
* JSP, Servlet 구동 환경 제공 
* 컨테이너, 웹 컨테이너, 서블릿 컨테이너 라고도 부른다. 
* WAS 동작   
    - (1) 웹서버로부터 요청이 오면 컨테이너가 받아서 처리한다. 
    - (2) 컨테이너는 web.xml을 참조하여 해당 서블릿에 대한 스레드를 생성하고      
       httpServletRequest와 httpServletResponse 객체를 생성하여 전달한다.   
    - (3) 컨테이너는 서블릿을 호출한다. 
    - (4) 호출된 서블릿의 작업을 담당하게 된 스레드는 (2번에서 만든 스레드) doPost() 혹은 doGet()을 호출한다. 
    - (5) 호출된 doPost(), doGet() 메소드는 생성된 동적 페이지를 Response 객체를 담아 컨테이너에 전달한다. 
    - (6) 컨테이너는 전달받은 Response 객체를 HttpResponse 형태로 바꿔 웹 서버에 전달하고    
       생성되었던 스레드 종료, HttpServletRequest와 HttpServletResponse 객체를 소멸시킨다.   
* 예) Tomcat, Jeus, JBoss

<br />

#### Web Server 와 WAS를 따로 두는 이유    
* 기능을 분리하여 서버 부하 방지  
    - WAS는 DB 조회나 다양한 로직을 처리하느라 바쁘기 때문에    
      단순한 정적 컨텐츠는 Web Server에서 빠르게 클라이언트에 제공하는 것이 좋다.  
    - WAS는 기본적으로 동적 컨텐츠를 제공하기 위해 존재하는 서버이다.    

* 물리적으로 분리하여 보안 강화
    - SSL에 대한 암복호화 처리에 Web Server를 사용
  
* 여러 대의 WAS를 연결 가능
    - Load Balancing을 위해서 Web Server를 사용
    - fail over(장애 극복), fail back 처리에 유리
    - 앞 단의 Web Server에서 오류가 발생한 WAS를 이용하지 못하도록 한 후 
      WAS를 재시작함으로써 사용자는 오류를 느끼지 못하고 이용할 수 있다.

* 여러 웹 어플리케이션 서비스 가능  
    - Java 서버, PHP 서버와 같이 서로 다른 서버를 하나의 WEB서버에 연결해서 서비스 할 수 있다.  
    - 접근 허용 IP 관리, 2대 이상의 서버에서의 세션 관리 등 Web Server에서 처리하면 효율적이다.  


> 자원 이용의 효율성 및 장애 극복, 배포 및 유지보수의 편의성 을 위해 Web Server와 WAS를 분리한다.   
> Web Server를 WAS 앞에 두고 필요한 WAS들을 Web Server에 플러그인 형태로 설정하면 더욱 효율적인 분산 처리가 가능하다.

<br />
<br />




























