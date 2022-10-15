## Springboot   
제품 수준의 Spring Framework를 빠르고 쉽게 개발할 수 있게 해준다.    
Springboot는 Spring Framework 뿐만 아니라 Third-party librarie(내장 톰캣)에 대한 설정도 자동으로 해준다.   

<br />
<br />

## Springboot 의존성 관리 
Gradle이 Maven보다 빌드 속도가 빠르고 Groovy로 작성하기 때문에 직관적이고 간편하게 작성할 수 있다.     
Gradle Wrapper를 이용하면 빌드도 쉽고 별도의 의존성 설치를 필요로 하지 않는다.    


Gradle의 목적은 다음과 같다.      
(1) 의존성 관리  
(2) 애플리케이션 패키징(실행 가능한 jar, war 파일로 패키징해주는 기능)    



스프링부트 레퍼러느에서 `io.spring.dependency-management`라는 플러그인을 같이 사용할 것을 권장하고 있다.    
`io.spring.dependency-management`의 주요 기능은 현재 사용 중인 Springboot 버전에 따라 의존성을 자동으로 관리해준다.    

<br />
<br />

## Springboot 자동 설정 
`@EnableAutoConfiguration` (`@SpringBootApplication` 안에 숨어있다.)    
빈은 총 두 단계에 걸쳐 빈을 읽어온다.   
Springboot에서는 `@ComponentScan`으로 빈을 읽어온 후 `@EnableAutoConfiguration`에서 또 빈을 읽어온다.    


(1) `@ComponentScan` 
ComponentScan은 `@Component` 어노테이션을 비롯해    
`@Service`, `@Controlloer`, `@RestController`, `@Repository` 어노테이션이     
붙은 클래스들을 찾아서 Spring IoC Container에 빈으로 등록해준다.         


(2) `@EnableAutoConfiguration`   
springboot-autoconfiguration 이라는 프로젝트의 내부를 보면 메타 파일 안에 spring.factories라는 파일이 있다.    
spring.factories 파일에 설정된 키값들을 보고 여기에 있는 값들을 빈으로 등록시킨다.    
여기서 WebAutoConfiguration이라는 클래스 내부에 `@ConditionalOnMissingBEan`이라는 어노테이션이 존재한다.    
이 어노테이션은 조건에 맞으면 이 설정 값을 빈으로 등록하라는 얘기다.   
즉 spring.factories에 키값으로 등록된 모든 설정들이 빈으로 등록되는 것은 아니다.    
`@EnableAutoConfiguration` 덕분에 내장 톰캣을 포함한 다양한 기능들을 자동으로 사용할 수 있게 되는 것이다.   

<br />
<br />

## Springboot 내장 웹 서버     
스프링부트는 서버가 아니다.   
(서버: netty, tomcat, underflow 등)     

```text
1. 톰캣 객체 생성 
2. 포트 설정 
3. 톰캣에 컨텍스트 추가 
4. 서블릿 만들기 
5. 톰캣에 서블릿 추가 
6. 컨텍스트에 서블릿 매핑 
7. 톰캣 실행 및 대기 
```

이 과정을 통해 웹 서버를 만드는데 스프링 부트 자동 설정에서 이 과정을 유연하고 상세하게 설정하고 실행해주는 것이다.       

* 서블릿 웹 서버 생성: ServletWebServerFactoryAutoConfiguration (톰캣, 내장 서블릿 컨테이너가 만들어진다.)       
* DispatcherServlet을 만들고 서블릿 컨테이너에 등록: DispatcherServletAutoConfiguration      

<br />
<br />

## Springboot 독립적으로 실행 가능한 JAR    
Gradle Wrapper를 통해 빌드를 하면 `.jar` 파일을 생성할 수 있다.       
jar 파일을 실행하기만 하면 스프링 내부의 수많은 의존성들과 프로젝트들을 실행할 수 있다.     

jar 파일을 만드는 것을 패키징한다고 한다.     
jar 내부에 프로젝트 실행을 위한 모든 의존성들이 패키징되어 들어간다.      

<br />
<br />

## Springboot 목적  
스프링 부트의 주요한 목적 중 하나는 독립적으로 실행 가능한 애플리케이션을 만들어주는 것이다.      

최근 마이크로서비스가 부상하면서 스프링 또한 시대에 발맞춰 독립적으로 실행 가능한 애플리케이션을 만들 수 있는 기능을 스프링 부트에 추가하였고   
이는 스프링 부트가 갖는 주요 목적 중 하나가 되었다.   

이를 위해 자동적으로 내장 웹서버가 들어가있다. (tomcat)    

이러한 사용 또한 사용자가 특별히 설정 없이 자동 설정으로 설정되어져 있고 실행만 하면 바로 웹서버를 실행할 수 있게 되었다.  

<br />
<br />

## Controller / RestController   
이 둘은 HTTP Response가 생성되는 방식에 차이가 있다.             
MVC `@Controller`는 view를 사용하지만,        
`@RestController`는 반환 시 JSON/XML 타입의 HTTP 응답을 직접 리턴하게 된다.        
`@Controller`의 메서드에 `@ResponseBody`를 선언해서 객체를 리턴할 수 있다.       

<br />

* `@Controller` 실행 

```text
Client > Request > Dispatcher Servlet > Handler Mapping 
> Controller > View > Dispatcher Servlet > Response > Client 
```




<br />

* `@ResponseBody` 실행   

```text
Client > Request > Dispatcher Servlet > Handler Mapping 
> Controller (ResponseBody) > Response > Client
```

<br />


* `@RestController` 실행 흐름

```text
Client > HTTP Request > Dispatcher Servlet > Handler Mapping 
> RestController (자동 ResponseBody 추가) > HTTP Response > Client
```


View의 유무에 따라 사용 용도가 다르다.     

<br />
<br />

## Spring Rest Client   
Springboot가 Rest Client에 관해 직접적인 기능을 제공하는 것은 아니며,  
Rest Client는 스프링 프레임워크에서 제공하는 기능이다.    

스프링부트는 RestTemplateBuilder와 WebClient.Builder를 빈으로 등록해준다.   

<br />

#### RestTemplate 
Blocking I/O 기반의 Synchronous API   

<br />

#### WebClient 
* Non-Blocking I/O 기반의 Asynchronous API
* Webflux 의존성을 추가해주어야 사용 가능 

<br />
<br />



























