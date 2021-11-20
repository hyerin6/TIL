## Springboot Web MVC   
Springboot는 Web MVC 개발을 바로 시작하는 것이 가능하다.           
Springboot-AutoConfiguration이 존재하기 때문에 Springboot에서는 별다른 설정 없이 바로 웹 개발이 가능하다.    

Spring MVC의 기능을 사용하며 추가적인 설정이 필요한 경우라면    
WebMvcConfigurere의 인터페이스를 구현하여 커스터마이징 가능하다.        

<br />

## HttpMessageConverters  
스프링 프레임워크에서 제공하는 인터페이스이며 Spring MVC 일부분이다.    
Http 요청 본문으로 들어오는 것을 객체로 변환하거나 응답 본문을 객체로 변환해서 전송할 때 주로 사용한다.    

`@RequestBody`와 `@ResponseBody`를 사용해서 메시지 컨버터를 사용할 수 있다.                    
`@RestController`를 사용하며 `@ResponseBody`를 명시하지 않아도 된다.            

<br />

## ViewResolver   
ViewResolver의 한 종류인 ContentNegotiatingViewResolver에 대해 알아보자.             
ContentNegotiatingViewResolver는 들어오는 요청의 accept-header에 따라 응답이 달라진다.          

<br />




