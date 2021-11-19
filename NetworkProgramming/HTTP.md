# 1. HTTP 프로토콜 
HTTP(Hypertext Transfer Protocol)은 웹브라우저와 웹서버간의 표준 통신 규약이다.    
HTTP 프로토콜이 주로 HTML 문서를 주고 받기 위한 프로토콜이라고 생각한다면 오해이다.    
어떤 종류의 데이터를 주고 받는지에 대한 부분이 HTTP 프로토콜에 없다.   
HTTP 프로토콜을 이용하여 어떤 종류의 데이터도 주고 받을 수 있다.    
즉 HTTP 프로토콜은 데이터 종류에 무관하다.    



HTTP 프로토콜을 이용하는 프로그램을 개발하려면, 웹개발자보다 HTTP를 더 깊게 알아야 한다.    
웹브라우저창의 주소 칸에 `http://www.google.com`을 입력하고 엔터키를 눌렀을 때,
웹브라우저 창 뒤에서 어떤 일이 일어나는지 알아보자.      

<br />


HTTP는 웹 브라우저와 웹 서버 사이에 통신을 위한 표준 프로토콜이다.   
HTTP 연결은 데이터 전송을 위해 TCP/IP 프로토콜을 사용한다.    


HTTP는   
* 클라이언트와 서버가 연결을 맺는 방법   
* 클라이언트가 서버에게 데이터를 요청하는 방법   
* 서버가 요청에 응답하는 방법   
* 마지막으로 연결을 종료하는 방법에 대해 명시한다.     

<br />

### 1) HTTP 1.0 통신 과정 

(1) 클라이언트가 서버의 80번 포트에 TCP 통신 연결을 시도한다.           
URL에 포트 번호가 포함되어 있다면 그 포트 번호를 사용한다. (ex. `http://localhost:8080`)              



(2) 클라이언트가 특정 경로에 위치한 리소스를 요청하는 메시지를 서버로 보낸다.               
(ex. `http://localhost:8080/student/list`)                   
요청의 내용은 (header + blank line + body)이다.   
(blank line + body)는 없는 경우도 있다. GET 방식의 요청인 경우에 body가 없다.      
header의 내용은 metadata이다.    
body의 내용은 request parameter이다. 여기에 업로드될 파일 내용이 들어있을 수도 있다.    



(3) 서버는 클라이언트에게 응답을 보낸다.    
응답의 내용은 (response code + header + blank line + body)이다.   
header의 내용은 metadata이다.    
body의 내용은 html 문서, 이미지 파일, 다운로드 파일 등 다양하다.     



(4) 서버는 클라이언트와 연결을 종료한다.   

<br />
<br />

### 2) HTTP 1.1 이후 버전    
HTTP 1.0 통신은 위의 1, 2, 3, 4 절차를 순서대로 실행하고 종료한다.    
하나의 웹 페이지에 다수의 이미지 파일, js 파일, css 파일들이 포함되어 있는데     
이 파일 각각을 서버에서 받아올 때 마다 TCP 연결을 새로 만드는 것은 비효율적이다.    


HTTP 1.1 이후 버전의 통신은 2, 3번 절차를 여러 번 반복 실행한 후 4번으로 넘어가서 종료한다.    
즉 TCP 연결을 한 후, 요청과 응답을 여러 번 반복할 수 있다.    
서버에 한 번 연결하면 여러 파일들을 전달 받고서 연결을 끊을 수 있기 때문에 효율적이다.     

<br />
<br />

## 2. HTTP request
### 1) HTTP request 예시 
```
GET /index.html HTTP/1.1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:20.0)
  Gecko/20100101 Firefox/20.0
Host: en.wikipedia.org
Connection: keep-alive
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
```

<br />
<br />

### 2) request line 
HTTP request의 첫 줄은 request line이다.    

```
GET /index.html HTTP/1.1
```

GET 방식 요청이고, 요청 대상의 경로는 /index.html 이다.          
HTTP/1.1 프로토콜로 통신             


request line의 내용 
 * HTTP request method (ex. GET, POST, PUT, DELETE, ...)
 * Path (ex. `http://localhost:8080/index.html`)  
 * HTTP 프로토콜 버전 (ex. HTTP/1.0, HTTP/1.1)    


<br />
<br />

### 3) header  
GET 방식 요청에는 body가 없다.  
따라서 둘째줄부터 나머지 내용은 전부 header이다. 

어떤 데이터에 대한 설명에 해당하는 데이터를 metadata라고 한다.     
header의 내용은 metadata이다.  


header의 각 줄은 다음과 같은 형태이다. 

```
keyword: value
```


keyword 부분은 대소문자에 무관하지만 value 부분은 대소문자를 구분할 수도 있다.     
keyword와 value에는 8bit ASCII 문자만 포함되어야 한다. (한글을 포함될 수 없다.)    

보통 header의 각 항목은 한 줄로 끝나지만,   
value가 너무 길어 여러 줄이어야ㅏ 하는 경우 두번째 줄 부터는 공백이나 탭 문자로 시작한다.    

줄바꿈 문자는 CR LF 두 문자이어야 한다. (carriage return, linefeed)    

<br />

#### User-Agent 
```
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:20.0)
 Gecko/20100101 Firefox/20.0
```

User-Agent 항목은 웹브라우저 버전을 명시한다.      
그 다음 줄이 공백 문자로 시작되기 때문에, 이 항목은 두 줄에 이다.      



Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:20.0) Gecko/20100101 Firefox/20.0     
이 값은 Mac OS X 에서 실행중인 Firefox 웹브라우저를 의미한다.

<br />


#### Host 

```
Host: en.wikipedia.org
```

URL에 포함된 host name을 명시한다.  



IP 주소 하나에 여러 host name 이 연결되는 경우도 있다.     
예를 들어 가난한 학교에서 서버 컴퓨터 한 대에 학교 홈페이지와 도서관 홈페이지를 동시에 운영할 수 있다.      
그 서버의 IP에 `www.myscool.ac.kr` 이름과 `library.myschool.ac.kr` 이름을 둘 다 연결한다.    

그 서버에 도착한 클라이언트의 HTTP 요청에서 host 항목의 값이 `www.myschool.ac.kr` 이라면,       
학교 홈페이지에 대한 요청으로 해석하고, `library.myschool.ac.kr`이라면, 도서관 홈페이지에 대한 요청으로 해석해서       
적절하게 서비스할 수 있다.    

<br />   

#### Accept  
```
Accept: text/html, text/plain, image/gif, image/jpeg
```

Accept 항목은, 클라이언트가 처리할 수 있는 데이터 종류를 명시한다.      
즉 나는 이런 종류의 데이터를 처리할 수 있으니,     
가급적 이런 종류의 데이터로 보내달라고 서버에게 요청하는 내용이다.     



Accept 항목의 값은 MIME 표준을 따른다.


MIME 표준은 데이터 타입을 두 단계로 분류한다.   


첫 단계는 주로 아래의 8 타입이다.

* `text/*` 텍스트
* `image/*` 이미지
* `model/*` 3D 모델
* `audio/*` 오디오
* `video/*` 비디오
* `application/*` 이진 데이터
* `message/*` 프로토콜에 정의된 데이터 (프로토콜마다 다른 데이터) 
* `multipart/*` 데이터(파일) 한 개가 아니고 여러 데이터(파일)의 묶음.


<br />


MIME 타입 예: 

```
text/plain
text/html
text/css

image/jpeg
image/png
image/gif

audio/mpeg
audio/ogg
audio/wav

video/mp4

application/octet-stream    이진 데이터
application/pdf
application/msword          MS word
application/vnd.ms-excel    MS excel
application/zip             
multipart/form-data
```


내 앱에서 사용할 나만의 MIME 타입을 만들려면, 

application/x-이름 형태를 사용한다.

예: application/x-my-data

<br />
<br />


### 4) 마지막 빈줄

HTTP request의 마지막 줄은 빈 줄 이어야 한다.

따라서 HTTP request의 마지막 4 문자는 \r\n\r\n 이어야 한다. (CR LF CR LF) 

(내용이 있는 마지막 줄의 \r\n + 빈 줄의 \r\n = \r\n\r\n)


<br />
<br />

## HTTP response   

HTTP request의 마지막 빈줄을 확인한 후, 서버는 HTTP response를 시작한다.

<br />

### 1) HTTP response 예

```
HTTP/1.1 200 OK
Date: Sun, 21 Apr 2013 15:12:46 GMT
Server: Apache
Connection: close
Content-Type: text/html; charset=ISO-8859-1
Content-length: 115

<html>
<head>
<title>
A Sample HTML file
</title>
</head>
<body>
The rest of the document goes here
</body>
</html>
```

<br />

#### HTTP response 첫줄은 상태 코드
 
```
HTTP/1.1 200 OK
```

* HTTP/1.1: 프로토콜 버전
* 200 OK: response status code.


<br />

#### 둘째 줄 부터 header 

```
Date: Sun, 21 Apr 2013 15:12:46 GMT
```

현재 서버 시각

```
Server: Apache 
```

서버 SW

```
Connection: close
```

이 응답을 보내고 나면, TCP 연결을 끊겠다.


```
Content-Type: text/html; charset=ISO-8859-1 
```

body에 담긴 데이터의 MIME 타입과 문자 인코딩

```
Content-length: 115
```

body에 담긴 데이터의 크기 (바이트 수)


<br />
<br />

### 2)  HTTP response code 


#### 1XX 정보 응답

```text
100 Continue
서버가 request header의 metadata를 받은 직후, 
아직 대용량의 request body를 받기 직전에 보내는 응답 코드이다. 
request body 전송을 계속하라는 의미. 
클라이언트가 대용량의 request body를 보내는 힘든 작업을 하고 나서 거부 당하지 않고,
계속 전송해도 되는지 미리 확인을 받기 위한 용도의 응답 코드이다.

101 Switching Protocol
이 코드는 클라이언트가 보낸 Upgrade 요청 헤더에 대한 응답에 들어가며 
서버에서 프로토콜을 변경할 것임을 클라이언트에 알려준다. 
(예: HTTP 프로토콜에서 WebSockets 프로토콜로 변경)

102 Processing (WebDAV)
이 코드는 서버가 요청을 수신하였으며 이를 처리하고 있지만, 
아직 제대로 된 응답을 알려줄 수 없음을 클라 이언트에 알려준다.

103 Early Hints
이 상태 코드는 주로 Link 헤더와 함께 사용되어 서버가 응답을 준비하는 동안 
사용자 에이전트가(user agent) 사전 로딩(preloading)을 시작할 수 있도록 한다.
```

요약: 아직 본격적인 응답을 하기 전 사전 정보를 전달하기 위한 응답

<br />

#### 2XX 성공 응답

```text
200 OK
요청이 성공적으로 처리되었다. 
response body에는 요청된 데이터가 들어있다. 
HTTP_OK (HttpURLConnection 클래스에 정의된 상수)

201 Created
POST request에 의해서 서버에 새 리소스가 생성되었다. 
그 새 리소스에 대한 URL이 response body에 들어있다. 
HTTP_CREATED

202 Accepted
요청이 접수되었으나, 아직 처리가 완료되지 않았다.
(이런 경우에, 처리 결과를 확인하기 위한 방법을 안내하는 정보나 링크가 response body에 들어있어야 바람 직할 것이다)
HTTP_ACCEPTED

203 Non-authoritative Information
이 응답은 요청된 서버가 아닌, 네트웍 통신 중간의 캐시 역할을 하는 누군가가 보낸 응답이다. 
따라서 이 응답은 최신 버전이 아닐 수 있다.
HTTP_NOT_AUTHORITATIVE

204 No Content
요청이 성공적으로 처리되었으나, 응답으로 보낼 자료는 없다.
(서버 앱이 아무것도 출력하지 않고, exception이 throw 되지도 않은 경우 응답이다.) 
HTTP_NO_CONTENT

205 Reset Content
요청이 성공적으로 처리되었으나, 응답으로 보낼 자료는 없다. 
그리고 클라이언트는 입력 폼을 비워라.
HTTP_RESET_CONTENT

206 Partial Content
클라이언트가 자료의 일부만 요청한 경우에 대한 응답이다. 
(request header의 Range 항목으로 요청) response body에는 요청된 일부 자료만 들어있다.
HTTP_PARTIAL
```

<br />

#### 3XX redirection

```
300 Multiple Choices
요청된 자료는 여러가지 형태(PDF, HWP, TEXT)가 있으니, 
이들 중 하나를 골라서 다시 요청하라.
HTTP_MULTI_CHOICE

301 Moved Permanently
요청된 자료의 URL이 바뀌었다.
그 새 URL은 response header의 Location 항목에 들어있다. 
HTTP_MOVED_PERM

302 Moved Temporarily
요청된 자료의 URL이 임시로 바뀌었다.
그 새 URL은 response header의 Location 항목에 들어있다. 
(서버앱에서 사용하는 리다이렉션의 상태 코드이다) 
HTTP_MOVED_TEMP

303 See Other
여기를 봐라.
볼 URL은 response header의 Location 항목에 들어있다. 
(POST, PUT 요청에 대한 응답으로 보낼 코드이다) 
HTTP_SEE_OTHER

304 Not Modified
클라이언트가 보낸 요청에 request header 항목 'If-Modified-Since: 시각' 이 들어있으면 
그 시각 이후로 변경된 경우에만 데이터를 보내달라는 것이다.
변경되지 않았으면 데이터는 보낼 필요 없고, 304 Not Modified 상태 코드만 보내면 된다. 
주로 캐시 역할을 하는 proxy가 이 요청을 보낸다.
HTTP_NOT_MODIFIED
```

<br />

#### 4XX 클라이언트 요청의 오류

```text
400 Bad Request
HTTP request에 오류가 있다.
HTTP_BAD_REQUEST

401 Unauthorized
접근 권한이 없는 URL 요청에 대한 응답 코드.
HTTP_UNAUTHORIZED

403 Forbidden
접근 권한 문제는 아니고, 다른 문제로 접근이 금지된 경우에 대한 응답 코드. 
예를 들어 네트웍 트래픽 초과.
HTTP_FORBIDDEN

404 Not Found
요청된 URL에 대한 자료를 찾을 수 없다는 응답 코드.
HTTP_NOT_FOUND

405 Method Not Allowed
request method가 허용되지 않는다는 응답. (POST, PUT, DELETE 등)
HTTP_BAD_METHOD

406 Not Acceptable
request header의 Accept 항목에 나열된 MIME 타입으로 자료를 제공할 수 없다.
8 / 19
HTTP_NOT_ACCEPTABLE

408 Request Timeout
클라이언트 요청 완료(마지막 빈줄)까지 너무 오래걸려서 시간 제한을 초괴했다.
HTTP_CLIENT_TIMEOUT

410 Gone
요청된 URL에 대한 자료가 없다. (찾을 수 없는 것이 아니고, 서비스가 종료되었다)
HTTP_GONE

411 Length Required
request header에 Content-length 항목이 있어야 하는데 없다.
HTTP_LENGTH_REQUIRED

413 Request Entity Too Large
reqeust body가 너무 크다.
(너무 큰 파일을 업로드 할 때 발생하는 에러) 
HTTP_ENTITY_TOO_LONG

414 Request URI Tool Long
URL이 너무 길 때 발생하는 에러.
HTTP_REQ_TOO_LONG

415 Unsupported Media Type
request body의 MIME 타입을 서버가 알지 못한다는 에러.
HTTP_UNSUPPORED_TYPE
```

<br />


#### 5XX 서버의 오류

```text
500 Internal Server Error
서버 내부의 알 수 없는 에러. (서버앱에서 exception throw 된 경우) 
HTTP_SERVER_ERROR HTTP_INTERNAL_ERROR

502 Bad Gateway
중간의 proxy server가 클라이언트에 보내는 응답 코드이다.
proxy 서버가 실제 서버로부터 정상적이지 않은 응답을 받은 경우이다. 
HTTP_BAD_GATEWAY

503 Service Unavailable
서버가 잠시 응답할 수 없다. (서버 정비 들의 이유로)
HTTP_UNAVAILABLE

504 Gateway Timeout
중간의 proxy server가 클라이언트에 보내는 응답 코드이다. 
proxy 서버가 실제 서버로부터 응답을 받을 수 없는 경우이다. 
HTTP_GATEWAY_TIMEOUT

505 HTTP Version Not Supproted
request 의 HTTP 버전을 서버는 지원하지 않는다.

HTTP_VERSION
511 Network Authentication Required
Network 사용자 인증이 필요하다. (예: KT WIFI 인증이 필요하다)
```

<br />
<br />


## 4. Keep-Alive

HTTP 1.0 통신은 요청을 한 번 할 때 마다 TCP 연결을 새로 만들어야 한다.  
보통 TCP 연결을 새로 만드는 시간이, 그 연결로 데이터를 한 번 보내고 받는 시간보다 더 긴 경우가 흔하다.  


하나의 웹 페이지에 다수의 이미지 파일, js 파일, css 파일들이 포함되어 있는데,   
이 파일 각각을 서버에서 받아올 때 마다 TCP 연결을 새로 만드는 것은 비효율적이다.  


HTTP 1.1 이후 버전의 통신은 TCP 연결을 한 후, 요청과 응답을 여러 번 반복할 수 있다.  
서버에 한 번 연결하면, 여러 파일들을 전달 받고서 연결을 끊을 수 있기 때문에 효율적이다.  


<br />


이렇게 연결을 통해서 여러 번 요청을 반복할 경우에, request header에 다음 항목이 포함되어야 한다. 

```
Connection: Keep-Alive
```


Java의 URL 클래스 내부에 Keep-Alive 기능이 구현되어 있어서, 기본적으로 TCP 연결 한 번에 여러 요청을 반복한다.

<br />


```java
public static void main(String[] args) {
    // keep.alive 속성은 설정되어 있지 않다. 디폴트 값은 true 이다. 
    System.out.println(System.getProperty("keep.alive"));
    
    // keep.alive 속성을 false로 설정할 수도 있지만, 그렇게 할 이유가 없다.
    System.setProperty("keep.alive", "false");
    System.out.println(System.getProperty("keep.alive"));
}
```


<br />
<br />


## 5. HTTP request method 

HTTP request의 첫 줄은 request line 이다. 

```
GET /index.html HTTP/1.1
```


여기서 GET 부분은 request method 이다.


<br />



#### request method 종류   
* GET 자료 조회 요청  
* POST 새 자료 등록 요청   
* PUT 기존 자료 수정 요청   
* DELETE 자료 삭제 요청    

클라이언트 앱에 대한 백엔드 앱을 개발하는 경우에는 (REST API) GET, POST, PUT, DELETE 방식을 구분해서 구현하는 것이 일반적이다.




<br />

#### 웹브라우저 새로고침
웹브라우저에서 새로고침을 하면, 직전의 http request를 다시 서버에 요청한다.     
그런데 직전의 http request가 GET 방식이 아닌 경우에, 웹브라우저는 다시 확인한다. 

GET 요청을 여러 번 반복하는 것은 부작용이 없지만,      
POST, PUT, DELETE를 여러 번 반복하면 문제가 발생할 수도 있기 때문이다. (예: 두 번 결제)     
사용자가 실수로 버튼을 여러 번 누르거나, 새로고침 하더라도,   
돈 결제 같은 서버 작업이 반복 실행되지 않도록 서버에서 구현해야 한다.     


<br />

#### GET 방식 요청은 자료 조회만 
서버 데이터의 수정, 삭제 같은 변경 작업을 GET 방식 요청으로 구현하는 것은 바람직하지 않다.   

GET 방식 요청은 웹브라우저 새로고침에 의해서, 의도치 않게 여러 번 반복될 수 있고,   
클라이언트와 서버 중간의 캐시에 의해서, 서버에 요청이 전달되지 않을 수도 있다.  

클라이언트와 서버 중간의 캐시는 GET 방식 요청만 캐싱한다.

<br />
<br />

## 6. Request Body

GET 방식의 요청은 request body가 없지만,   
POST, PUT 방식의 요청에는 reqest header 뒤에 request body가 포함된다.    
request header와 request body 사이에는 빈 줄이 있어야 한다.  

<br />

#### POST 방식의 요청의 예

```
POST /cgi-bin/register.pl HTTP 1.0
Date: Sun, 27 Apr 2013 12:32:36
Host: www.cafeaulait.org
Content-type: application/x-www-form-urlencoded
Content-length: 54

username=Elliotte+Harold&email=elharo%40ibiblio.org
```

`<form action="/cgi-bin/register.pl" method="post">` 태그 내부의 submit 버튼이 클릭된 경우에, 

위와 같은 요청이 서버에 전송된다.


<br />

```
Content-type: application/x-www-form-urlencoded
```

폼에 입력된 데이터는, application/x-www-form-urlencoded 형태로 인코딩되어서 request body에 담겨있다.


<br />


```
username=Elliotte+Harold&email=elharo%40ibiblio.org
```

application/x-www-form-urlencoded 형식의 데이터이다.      
URL 인코딩과 같은 인코딩 방식이다.    

<br />

request header와 request body 사이에 빈 줄이 있어야 한다.     
request body 끝에 빈 줄이 있어야 한다.         


```
<form action="/cgi-bin/register.pl" method="post"> <input type="text" name="username" />
    <input type="text" name="email" />
    <button type="submit">전송</button>
</form>
```

이렇게 구현된 입력 폼의 두 입력 칸에 아래의 내용을 각각 입력하고,    
    Elliotte Harold   
    elharo@0ibiblio.org   
전송 버튼을 클릭하면, 위 POST 방식 요청이 서버에 전송된다.    

<br />
<br />

## 7. Cookies

쿠키(cookies)는 작은 문자열이다.  
서버가 쿠키를 만들어서 클라이언트 웹브라우저에 전송한다.    

서버로부터 쿠키를 받은 웹브라우저는, 그 서버에 다시 요청을 할 때 마다,    
그 쿠키를 request에 담아서 전송한다.  

<br />

### 1) cookie 전송의 예

#### http response
```
HTTP/1.1 200 OK
Content-type: text/html
Set-Cookie: cart=ATVPDKIKX0DER
```

쿠키의 이름 cart, 값은 ATVPDKIKX0DER 이다.  

위와 같이 서버가 보낸 http response에 쿠키가 포함되어 있으면,     
웹브라우저는 그 서버에 요청을 또 보낼 때 마다, 이 쿠키를 request에 포함한다.      

<br />

#### http request

```
GET /index.html HTTP/1.1
Host: www.example.org
Cookie: cart=ATVPDKIKX0DER
Accept: text/html
```

<br />
<br />

### 2) session ID 쿠키 

서버는 클라이언트를 구별하는 용도로 쿠키를 사용한다.     
대표적인 쿠키의 용도가 session ID 이다.    

많은 동시 접속자가 있는 서버에서 클라이언트 각각을 식별하는 용도로 session ID를 사용한다.     
클라이언트의 IP 주소로 각각의 클라이언트를 식별할 수 없다.    

무선공유기와 같은 NAT 장비는, 내부 망의 여러 클라이언트 요청들을, 동일한 IP 주소로 외부로 전송하기 때문이다.    

예를 들어 집에서 무선으로 가족들이 naver에 접속할 때,     
naver 서버가 보는 가족들의 IP 주소는 모두 그 무선 공유기의 IP 주소이다.    

서버는 어떤 웹브라우저가 처음으로 요청을 보내오면,   
새 session ID를 생성하고, 그 session ID 값으로 쿠키를 만들어서,    
http response에 넣어서, 웹브라우저에 전송한다.   

웹브라우저는 그 서버에 다시 요청을 보낼 때, 그 쿠키를 request에 담아서 전송할 것이다.    
서버는 이 쿠키의 session ID를 보고, 각각의 웹브라우저를 구별할 수 있다.   

웹브라우저가 종료되었다가 재시작하면, 웹브라우저는 session ID 쿠키 없이 서버에 요청하게 될 것이다.     
요청에 session ID 쿠키가 없으면, 이것을 새 웹브라우저(새 사용자)라고 서버는 인식하고,    
새 session ID 쿠키를 만들어서, http response에 넣어서, 웹브라우저에 전송한다.    

<br />

### 3) 쿠키 기능 구현

#### CookieManager 클래스   
웹브라우저에서 실행되는 Javascript 클라이언트 앱이라면, 웹브라우저가 쿠키의 저장과 서버 재전송을 처리해 주기 때문에,      
로그인 기능 구현할 때 session ID 쿠키까지 신경쓰지 않아도 된다.     

클라이언트가 Java로 구현된 앱에서 웹서버 로그인 기능을 구현하려면,    
sesssion ID 쿠키 처리 기능을 구현해야 한다.   
이때 CookieManager 클래스를 이용한다.    

그런데 요즘 웹사이트들의 여러가지 보안 강화 기능 구현 때문에, Java 앱의 웹 서버 로그인 기능 구현이 쉽지 않다.    


```java
CookieManager manager = new CookieManager();
CookieHandler.setDefault(manager);
```

위 두 줄이면, URL 클래스를 이용한 서버 연결에 쿠키 기능이 켜진다.

<br />


#### CookieStore 클래스

CookieManager가 관리 중인 쿠키를 디스크에 저장하려면, CookieStore 클래스를 이용한다.


```java
CookieStore store = manager.getCookieStore();
```

위의 코드로 CookieManager 객체로부터 CookieStore 객체를 얻을 수 있다.

<br />  
