## 스프링 프로젝트 시작하기   

### Gradle 프로젝트   
프로젝트 루트 폴더에서 `gradle wrapper` 명령어를 실행해서 래퍼 파일을 생성해보자.   

명령어 실행에 성공하면 프로젝트 루트 폴더에 `gradlew.bat` 파일, `gradlew` 파일, `gradle` 폴더가 생긴다.     
`gradlew.bat` 파일, `gradlew` 파일은 각각 윈도우와 리눅스에서 사용할 수 있는 실행 파일로 gradle 명령어 대신 사용할 수 있는 래퍼 파일이다.   
이 래퍼 파일을 사용하면 gradle 설치 없이 gradle 명령어를 실행할 수 있다.   

`gradlew compileJava` 명령어를 실행하면 `gradlew.bat`을 이용해서 실행한다.   

예제 코드: <https://github.com/madvirus/spring5fs/tree/master/sp5-chap02>     

* Greeter.java: 콘솔에 간단한 메시지를 출력하는 자바 클래스 
* AppContext.java: 스프링 설정 파일 
* Main.java: main() 메서드를 통해 스프링과 Greeter를 실행하는 자바 클래스 

<br />

### `@Configuration`
해당 클래스를 스프링 설정 클래스로 지정하는 어노테이션 

<br />

### `@Bean` 
스프링이 생성하는 객체를 빈(bean) 객체라고 부른다.          
`@Bean` 어노테이션을 메서드에 붙이면 해당 메서드가 생성한 객체를 스프링이 관리하는 빈 객체로 등록한다.     

메서드 이름은 빈 객체를 구분할 때 사용한다.    

<br />

## 스프링은 객체 컨테이너     
### AnnotationConfigApplicationContext 
스프링의 핵심 기능은 객체를 생성하고 초기화하는 것이다.   
이와 관련된 기능은 ApplicationContext라는 인터페이스에 정의되어 있다.   
AnnotationConfigApplicationContext 클래스는 이 인터페이스를 알맞게 구현한 클래스 중 하나이다.   
이 클래스는 자바 클래스에서 정보를 읽어와 객체 생성과 초기화를 수행한다.   

<img width="600" src="https://user-images.githubusercontent.com/33855307/154844430-337d173b-75b7-496c-b777-812aa6812c2d.jpg">


















































