**Chpater10**

## 1. 세션 사용하기 : session 기본 객체  
웹 브라우저에 정보를 보관할 때 쿠키를 사용한다면, 세션은 웹 컨테이너 정보를 보관할 때 사용한다.   
세션은 오직 서버에만 생성된다.   
웹 컨테이너는 기본적으로 한 웹 브라우저마다 한 세션을 사용한다.   
즉, 같은 jsp 페이지라도 웹 브라우저에 따라 서로 다른 세션을 사용한다.   
  
웹 브라우저마다 세션이 따로 존재하기 때문에 세션은 웹 브라우저와 관련된 정보를 저장하기에 알맞은 장소이다.     
세션을 생성해야만 정보를 저장할 수 있다.   
일단 세션을 생성하면 session 기본 객체를 통해서 세션을 사용할 수 있다.   
  
##### (1) 세션 생성하기     
jsp에서 세션을 생성하려면 page 디렉티브의 session 속성을 “true”로 지정하면 된다.   
```  
<%@ page contentType = … %>   
<%@ page session = “true” %>   
<%  
session.setAtttribute(“userInfo”, userInfo);  
%>  
```  
page 디렉티브의 session 속성은 기본값이 true 이므로 false로 지정하지만 않으면 세션이 생성된다.   
세션을 사용하는 서버 프로그램에 웹 브라우저가 처음 접속할 때 세션을 생성하고,   
이후로는 이미 생성된 세션을 사용한다.   

##### (2) session 기본 객체   
세션을 사용한다는 것은 session 기본 객체를 사용한다는 것을 말한다.   
request 기본 객체와 마찬가지로 속성을 제공하므로 setAttribute(), getAttribute() 등의   
메소드를 사용하여 속성값을 저장하거나 읽어올 수 있다.    
추가로 세션은 세션만의 고유 정보를 제공하며, 이들 정보를 구할 때 사용하는 메소드는 아래와 같다.   

- String getId() : 세션의 고유 ID를 구한다.  
- long getCreationTime() : 세션이 생성된 시간을 구한다.   
- long getLastAccessedTime() : 웹 브라우저가 가장 마지막에 세션에 접근한 시간을 구한다.  

##### (3) 기본 객체의 속성 사용   
한 번 생성된 세션은 지정한 유효 시간 동안 유지된다. 
따라서, 웹 어플리케이션을 실행하는 동안 지속적으로 사용해야 하는 데이터의 저장소로 세션이 적당하다.    
request 기본 객체가 하나의 요청을 처리하는 데 사용되는 jsp 페이지 사이에서 공유된다면,  
session 기본 객체는 웹 브라우저의 여러 요청을 처리하는 jsp 페이지 사이에서 공유된다.  
따라서, 로그인한 회원 정보 등 웹 브라우저와 일대일로 관련된 값을 저장할 때에는 쿠키 대신 세션을 사용할 수 있다.  

세션의 값을 저장할 때는 속성을 사용한다. 속성에 값을 저장하려면 setAttribute() 메소드를 사용하고,  
속성값을 읽으려면 getAttribute() 메소드를 사용한다.   
**chap10/setMemberInfo.jsp 참고**   

##### (4) 세션 종료  
세션을 유지할 필요가 없으면 session.invalidate() 메소드를 사용해서 세션을 종료한다.  
세션을 종료하면 현재 사용중인 session 기본 객체를 삭제하고 session 기본 객체에 저장했던 속성 목록도 함께 삭제한다.  

##### (5) 세션 유효 시간 - 생략

##### (6) request.getSession()을 이용한 세션 생성   
httpSession을 생성하는 또 다른 방법은 request 기본 객체의 getSession()를 사용하는 것이다.  
request.getSession() 메소드는 현재 요청과 관련된 session 객체를 리턴한다.  
request.getSession()를 사용하여 세션을 구하므로, page 디렉티브의 session 속성은 false로 지정한다.  

## 2. 세션을 사용한 로그인 상태 유지  
세션을 사용해서 로그인을 처리하는 방식은 쿠키를 사용한 방식과 비슷하다.

- 로그인에 성공하면 session 기본 객체의 특정 속성에 데이터를 기록한다.  
- 이후로 session 기본 객체의 특정 속성이 존재하면 로그인한 것으로 간주한다.  
- 로그아웃할 경우 session.invalidate() 메소드를 호출하여 세션을 종료한다.  

##### (1) 인증된 사용자 정보 session 기본 객체에 저장하기   
세션을 사용해서 로그인 상태를 유지하려면 session 기본 객체의 속성에 로그인 성공 정보를 저장하면 된다.    

예) chap10/member/sessionLogin.jsp, chap10/member/sessionLoginForm.jsp  
sessionLogin.jsp는 id 요청 파라미터와 password 요청 파라미터가 같으면 로그인에 성공한 것으로 간주하고,     
session 기본 객체의 "MEMBERID" 속성에 사용자 아이디 정보를 저장한다.    
즉, "MEMBERID" 속성이 존재하면 현재 사용자는 로그인한 사용자로 간주한다.  

##### (2) 인증 여부 판단  
session 기본 객체에 로그인 상태를 위한 속성의 존재 여부에 따라 로그인 상태를 판단할 수 있다.    

예) chap10/member/sessionLoginCheck.jsp    
sessionLogin.jsp은 로그인에 성공하면 MEMBERID 속성에 로그인 상태 정보를 보관하므로    
sessionLoginCheck.jsp와 같이 session 기본 객체의 "MEMBERID" 속성을 사용해서 로그인 여부를 판단하면 된다.  

##### (3) 로그아웃 처리  
로그아웃을 처리할 때는 session.invalidate() 메소드를 사용하여 세션을 종료합니다.  

예) chap10/member/sessionLogOut.jsp  
session.invalidate()을 사용하지 않고 다음과 같이 로그인 상태를 보관할 때 사용한 session 기본 객체를 모두   
삭제해도 로그아웃한 효과를 낼 수 있다.  
```
session.removeAttribute("MEMBERID");  
```
하지만, 로그인할 때 session 기본 객체에 추가하는 속성이 늘어나면 로그아웃 코드도 함께 변경해야 하므로  
session.invalidate() 메소드를 사용하는 것이 좋다.  

## 3. 연관된 정보 저장을 위한 클래스 작성  
앞서 예제 코드에서 사용한 사용자 정보를 다음과 같이 저장했다.  
```
<%  
  session.setAttribute("MEMBERID", memberid);  
%>   
```
만약 세션에 저장할 값의 개수가 많다면 클래스를 사용하는 것이 좋다.  
예를 들어, 회원과 관련된 정보를 다음과 같은 클래스에 묶어서 저장한다고 가정해보자.  
```
public class MemberInfo{
  private String id;
  private String name;
  private String email;
  private boolean male;
  private int age;
  
  //get 메서드
}
```
연관된 정보를 클래스로 묶어서 저장하면 각 정보를 개별 속성으로 저장하지 않고 다음과 같이 한 개의 속성을 이용해서 저장할 수 있다.  
```
<%
  MemberInfo memberInfo = new MemberInfo(id, name);
  session.setAttribute("memberInfo", memberInfo);
%>
```
연관된 정보를 한 객체에 담아 저장하기 때문에, 세션에 저장한 객체를 사용할 때에도 다음과 같이 객체를 가져온 뒤 객체로부터 필요한 값을 읽을 수 있다.  
```
<%
  MemberInfo member = (MemberInfo)session.getAttribute("memberInfo");
%>
...
<%= member.getEmail().toLowerCase() %>
```
