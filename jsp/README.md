**JSP 웹프로그래밍 공부 👩🏻‍💻**

----  

- 2019-02-21 ~ 2019-02-23   
이클립스 에러로 인해   
JavaSE - 1.7 Java SE [1.8.0_191] 변경   


- 2019-02-24  
이클립스 Java JDK 1.8 변경  
mysql-connector-java-버전-bin.jar 파일을 /Library/Java/.. 혹은 tomcat폴더/lib 에 추가하면    
모든 자바 프로젝트 또는 톰캣 서버를 사용하는 프로젝트에 전부 추가되므로     
현재 공부중인 jdbc1 프로젝트의 WebContent/WEB-INF/lib 폴더에 추가한다.  


- 2019-03-06   
Tomcat 9.0 버전 추가  


- 2019-04-15      
Tomcat 9.0 에서    
```
request.getAttribute("javax.servlet.forward.request_uri");
// 버전 8은 언제나 웹페이지에서 처음 요청한 url을 리턴
```
위 코드 null이 return되서 error 발생    

```
Object temp = request.getAttribute("javax.servlet.forward.request_uri");
String uri = (temp != null) ? temp.toString() : request.getRequestURI();
String url = uri + "?" + queryString; 
// forward의 경우에만 처음 요청한 url을 리턴, forward가 아니면 null을 리턴
```
위 코드로 수정    
 
**~4월 29일**  
