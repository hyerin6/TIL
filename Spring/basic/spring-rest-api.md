## REST API    
API는 Application Programming Interface이고,       
REST는 Representational State Transfer이다.   

<br />

REST API는 아래의 아키텍처 스타일을 따라야 하는데 대부분의 REST API는 아래의 아키텍처를 완벽하게 따르지는 않는다.  
* Client-Server
* Stateless
* Cache
* Uniform Interface
* Layered System 
* Code-On-Demand           


특히 이중에서도 Uniform Interface를 구성하는 네가지 구성 요소 중 Self-descriptive message와   
Hypermisa as the egine of application state(HATEOAS)를 만족하지 않는다.  

<br />

#### Self-descriptive message
메시지 스스로가 자신에 대한 설명이 가능해야한다.

서버가 메시지를 바꾸더라도 클라이언트는 바뀐 메시지의 내용을 해석할 수 있어야 한다.

왜? 메시지를 해석할 수 있는 정보가 메시지 안에 존재하기 때문이다.

그러나 오늘날 REST API는 Self-descriptive message를 만족하고 있지 않다.

<br />

#### HATEOAS
응답에 하이퍼미디어가 포함이 되어있어야한다.

그리고 그 하이퍼미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야 한다.

즉, 예를 들어 우리가 유튜브 메인화면에서 영상을 보기 위해 영상을 클릭하면 영상을 볼 수 있듯이

다음 애플리케이션 상태로 전이하기 위해 서버가 보내준 응답의 하이퍼링크를 사용해서 이동을 해야한다는 것이다. (물론 이는 서버,클라이언트에 모두 해당하는 이야기다.)

응답에는 애플리케이션의 상태를 변화시킬 수 있는 하이퍼미디어 정보가 함께 동봉되어야한다.

그렇다면 어떻게 이 두 가지를 만족시킬 수 있을까?

<br />

#### Self-descriptive message 해결 방법

* 미디어 타입을 정의하고 IANA에 등록하고 그 미디어 타입을 리소스 리턴할 때 Content-type으로 사용한다.    

* profile 링크 헤더를 추가한다.  
    - 브라우저들이 아직 스펙 지원을 잘안한다.   
    - 그래서 본문에 profile과 링크를 HAL 스펙에 따라 추가  


<br />


#### HATEOAS 해결 방법 

* 데이터에 링크 제공
     - HAL 사용 (Hypertext Application Language)


<br />

