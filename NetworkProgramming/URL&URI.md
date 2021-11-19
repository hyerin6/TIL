# 1. URL 클래스 

웹서버에 TCP/IP 통신을 연결해서 데이터를 받아오는 Java 코드를 구현하려면 

먼저 URL 문자열로부터 URL Java 객체를 생성해야 한다. 


Java의 URL 클래스는 웹서버에 통신을 연결하기 위한 용도의 클래스이다.

<br />

### 1) 생성자 

```java
public URL(String url) throws MalformedUTLException
```

문자열 url로부터 URL 객체를 생성한다. 

<br />

```java
public URL(String protocol, String hostname, String file) throws MalformedURLException
```

URL 객체를 생성하기 위한 문자열 url을 3부분으로 쪼개 파라미터로 전달한다.

(http나 https 프로토콜 부분, 호스트이름 부분, 그 나머지 부분)


<br />

```java
public URL(String protocol, String host, int port, String file) throws MalformedURLException
```

URL 객체를 생성하기 위한 문자열 url을 4 부분으로 쪼개어 파라미터로 전달한다.

서버 포트 번호가 http 표준 80번이 아니면, 포트 번호를 명시해야 한다.

<br />

```java
public URL(URL base, String relative) throws MalformedURLException
```

절대 경로 URL과 상대 경로 URL을 결합하여, URL 객체를 생성한다. 

첫째 파라미터 절대 경로 URL에서 파일 이름 부분이 둘째 파라미터 상대 경로 URL로 치환한다. 

<br />
<br />

### 2) 데이터를 받아오기 위한 URL 클래스의 메소드 

웹서버로부터 데이터를 받아오기 위한 URL 클래스의 메소드는 다음과 같다. 

```java
public InputStream openStream() throws IOException
```

웹서버에 TCP/IP 통신을 연결한 후, 데이터를 받아오기 위한 InputStream 객체를 리턴한다.

이 InputStream 객체로부터 HTTP response의 본문(body) 부분만 받을 수 있고,

Http response 헤더(header) 부분은 받을 수 없다.

<br />


```java
public URLConnection openConnection() throws IOException
public URLConnection openConnection(Proxy proxy) throws IOException
```

웹서버에 TCP/IP 통신을 연결한 후, 그 연결 객체를 리턴한다. (URLConnection 객체) 

이 연결 객체로부터 데이터를 받아오기 위한 InputStream 객체를 얻을 수 있다.

이 연결 객체로부터 Http response 헤더(header)도 받을 수 있다.

데이터의 유형, 문자 인코딩 등의 정보는 헤더에 있다.

프록시 서버를 통해서 웹서버에 연결하려면, Proxy 객체를 파라미터로 전달한다.

<br />

```java
public Object getContent() throws IOException
public Object getContent(Class[] classes) throws IOException
```

이 메소드를 이용해서 데이터를 받아올 수도 있다.

<br />
<br />


### 3) getter

예를들어 다음과 같이 URL 개게를 생성했을 경우 

```java
URL urlObj = new  URL("http://nothing.com:8088/context/path/file?id=3");
```
 
아래의 메소드들은 URL 클래스의 getter 들이다.   


* `public String getProtocol()`  
프로토콜 문자열을 리턴한다. "http"



 
* `public String getHost()`  
호스트 이름을 리턴한다. "nothing.com"



* `public int getPort()`  
포트 번호를 리턴한다. 8088



 
* `public String getPath()`  
context 부터 파일 이름부분까지 리턴한다. "/context/path/file"




* `public String getFile()`  
context 부터 query string부분까지 리턴한다. "/context/path/file?id=3"



* `public String getQuery()`  
query string 부분을 리턴한다. "id=3"

  
<br />
<br />

### 4) `equals()`, `hashCode()`
URL 클래스에는 `equals()`, `hashCode()`가 잘 재정의되어 있다. 

`equals()`는 DNS 서버에 조회해서 IP 주수가 같은지 확인해서 true/false를 리턴한다.        
따라서 이 메소드는 똑똑하지만 조금 느리다.   

"www.ibiblio.com", "ibiblio.com" 이 두 호스트 이름은 IP 주소가 동일하기 때문에 equals 메소드는 true를 리턴한다. 

그런데 프로토콜, 포트번호 경로 등 URL의 나머지 부분이 다르면 당연히 false를 리턴한다. 


<br />
<br />

## 2. URI 클래스   
서버에 통신을 연결하기 위한 용도가 아니고 URL 주소를 검색하고 비교하는 등의 용도라면 URI 클래스를 사용해야 한다.   

<br />

### 1) 생성 
```java
public URI(String uri) throws URISyntaxException
```

`toURL()`, `toURI` 메소드     
➡️ URL 클래스의 `toURI()` 메소드를 호출하면 URI 객체가 리턴되고,        
URI 클래스의 `toURL()` 메소드를 호출하면 URL 객체가 리턴된다.     


<br />
<br />


### 2) getter 
code 

```java
public static void main(String[] args) throws MalformedURLException {
        URI uriObj = new URI("http://nothing.com:8080/context/path/file?id=3");
        System.out.println(uriObj.getScheme());
        System.out.println(uriObj.getHost());
        System.out.println(uriObj.getPort());
        System.out.println(uriObj.getPath());
        System.out.println(uriObj.getQuery());
}
```

output 

```text
http
nothing.com
8080
/context/path/file
id=3
```


<br />
<br />

### 3) equals 메소드 
문자열 비교를 한다. 

URL 클래스의 equals 메소드보다 빠르지만 똑똑하지는 않다. 

URL에서 프로토콜과 호스트 이름 부분은 대소문자 무관하고 그 나머지 부분은 대소문자를 구분한다. 


```java  
public static void main(String[] args) throws MalformedURLException {
        URI uriA = new URI("http://www.ibiblio.com");
        URI uriB = new URI("http://ibiblio.com");
        System.out.println(uriA.equals(uriB)); // false
        URI uriC = new URI("https://www.ibiblio.com");
        System.out.println(uriA.equals(uriC)); // false
        URI uriD = new URI("HTTP://WWW.IBIBLIO.COM");
        System.out.println(uriA.equals(uriD)); // true
}
```

<br />
<br />

### 4) resolve 메소드 

현재 URL과 상대 URL을 결합하여 절대 URL을 계산해주는 메소드이다. 

* `resolve(String 상대주소)`
* `resolve(URI 상대주소)`


```java
public static void main(String[] args) throws URISyntaxException { 
    URI 현재주소 = new URI("http://localhost:8088/student/list"); 
    String 상대주소 = "edit?id=3";
    URI 절대주소 = 현재주소.resolve(상대주소);
    System.out.println(절대주소);
    // http://localhost:8088/student/edit?id=3 
}
```



<br />
<br />


## URL 인코딩 (x-www-form-urlencoded)

URL에는 다음과 같은 문자만 사용될 수 있다.

* 영어 알파벳 `a~z, A~Z`
* 숫자 `0~9`
* 기호 문자 `_ . ! ~ * ( )`
* 특수 문자 `: / & ? @ # ; $ + =`



특수 문자는 URL에서 특수한 용도로 사용되는 문자라서, 경로명이나 파일명이나 쿼리 스트링 부분에 나올 수 없다.

경로명이나 파일명이나 쿼리 스트링 부분에, 특수 문자를 사용하려면, 

16진수 형태로 인코딩되어야 한다. (URL 인코딩)


<br />

* URL 인코딩 방법      
`String encoded = URLEncoder.encode(s, "UTF-8");`     
URL에 포함될 수 없는 문자열을 URL 인코딩한다.      


* URL 디코딩 방법   
`String decoded = URLDecoder.decode(encoded, "UTF-8");`    
URL 인코딩된 문자열을 디코딩한다.   

<br />


code 

```java
public static void main(String[] args) throws UnsupportedEncodingException  {
    // 인코딩 오류
    String s = "http://www.google.com/search?q=흥부 놀부"; 
    String urla = URLEncoder.encode(s, "UTF-8"); 
    System.out.println(urla);
    // 인코딩 OK
    String urlb = "http://www.google.com/search?q=" + URLEncoder.encode("흥부 놀부", "UTF-8");
    System.out.println(urlb);
    // 디코딩 Ok
    String t = URLDecoder.decode(urlb, "UTF-8");
    System.out.println(t);
}
```

output 

```text
http%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3D%ED%9D%A5%EB%B6%80+%EB%86%80%EB%B6%80 
http://www.google.com/search?q=%ED%9D%A5%EB%B6%80+%EB%86%80%EB%B6%80 
http://www.google.com/search?q=흥부 놀부
```

<br />
<br />

## 4. HTTP Request 구현 

### 1) HTTP Request and Response

웹브라우저의 주소창에 URL을 입력하고 엔터키를 누르면, 웹브라우저는 웹서버에 HTTP Request를 전송한다. 

HTTP Request에는 요청하는 것을 가르키는 URL과 파라미터가 들어있다. 



HTTP Request를 받은 웹서버는 요청받은 내용을 실행한 다음, 실행결과를 웹브라우저에 전송한다. 

휍브라우저의 HTTP Request의 결과로 웹서버가 전송하는 내용을 HTTP Response라고 부른다. 

HTTP Response에는 웹브라우저에 출력될 내용(contents)이 담겨있다. (HTML 태그, 이미지 등)  


```text 
           HTTP Request(URL, 파라미터)  
웹브라우저               ↔️               웹서버
         HTTP Response(HTML태그, 이미지)
```


<br />
<br />

### 2) HttpURLConnection 클래스 

웹브라우저뿐만 아니라 Java 코드도 웹서버에 HTTP Request를 전송하여 HTTP Response를 받아올 수 있다. 

HttpURLConnection 클래스를 이용하여 웹서버에 HTTP Request를 전송한다. 

<br />
<br />

### 3) Java 문자열의 특수 문자
* `"` 문자  

Java 문자열에 포함된 `"` 문자는 `\"` 형태로 입력해야 한다.

```System.out.println("value=3"); // value=3
System.out.println("value='3'"); // value='3'
System.out.println("value=\"3\""); // value="3"
```

<br />

* `\` 문자

Java 문자열에 포함된 `\` 문자는 `\\` 형태로 입력해야 한다.

```java
System.out.println("c:\\temp\\data.txt"); // c:\temp\data.txt
```

<br />

* `\n` 문자

Java 문자열에 포함된 줄바꿈 문자는 `\n` 형태로 입력해야 한다.

```java
System.out.println(" hello\n world"); 

/* 
hello 
world
*/
```       


<br />
<br />


### 4) HTTP request 구현

```java
public static void main(String[] args) throws Exception {
    String listUrl = "http://www.skhu.ac.kr/board/boardlist.aspx?curpage=1&bsid=10004&searchBun=51";
    String html = getHttpResponse(listUrl);
    System.out.println(html);
}

static String getHttpResponse(String url) throws IOException {
    URL urlObj = new URL(url);
    HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();
    // HTTP request header에 정보를 채운다
    connection.setRequestMethod("GET"); // GET 방식으로 요청한다. connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // 웹브라우저인척 한다
    // HTTP response header 정보에서 문자 인코딩을 파악한다.
    // 문자 인코딩은 UTF-8 아니면 EUC-KR 둘 중의 하나라고 가정한다. (한글 웹페이지라고 가정) String contentType = connection.getContentType();
    String encoding = (contentType.toUpperCase().indexOf("UTF-8") >= 0) ? "UTF-8" : "EUC-KR";
    // HTTP Response 본문(body)을 읽어서 문자열로 리턴한다.
    StringBuilder builder = new StringBuilder();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding))) {
        String s;
        while ((s = reader.readLine()) != null) {
            builder.append(s);
            builder.append('\n');
        } 
    }
    return builder.toString();
}
```

위 예제 코드는

성공회대학교 홈페이지 웹서버에 URL을 요청하고, 그 결과를 받아서, 받아 온 웹페이지 내용 전체를 화면에 출력한다.

아래의 URL은 성공회대학교 홈페이지 게시판의 URL 이다.

`"http://www.skhu.ac.kr/board/boardlist.aspx?curpage=1&bsid=10004&searchBun=51"`

String getHttpReqponse(String url) 메소드는

웹서버에 URL을 요청하고, 그 결과를 받아서 문자열로 리턴하는 메소드이다.

<br />


```java
public static void main(String[] args) throws Exception {
    String listUrl = "http://www.skhu.ac.kr/board/boardlist.aspx?curpage=1&bsid=10004&searchBun=51";
    String html = getHttpResponse(listUrl);
    String regex = "<a href='boardread.aspx[^']+'>([^<]+)</a>";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(html);
    while (matcher.find()) {
        System.out.println(matcher.group(1));
    }
}
```

위 예제 코드는

성공회대학교 홈페이지 웹서버에 URL을 요청하고, 

그 결과를 받아서, 받아 온 웹페이지 내용 중에서 게시글 제목을 찾아서 화면에 출력한다.

아래의 정규식은 게시글 제목을 찾기위한 정규식이다. (부록 참고)

`"<a href='boardread.aspx[^']+'>([^<]+)</a>"`

<br />
