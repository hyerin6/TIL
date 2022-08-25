# 코틀린 소개 

### 코틀린을 배우는 이유 

* 자동완성, 자바-코틀린 변환, 코루틴 등 코틀린 관련 편의 기능을 완벽하게 지원       
 
* 자바에서 Best-Practice로 불리는 기법들을 언어적 차원에서 기본 제공한다.        
  예) 이펙티브 자바, 디자인 패턴 등       

* 자바에 비해 문법이 간결하기 때문에 가독성, 생산성이 높다. → 오류 가능성이 적어진다.       

* 자바와 상호 운용이 가능하기 때문에 자바 프로젝트에 쉽게 적용할 수 있고 자바로 작성된 오픈소스를 그대로 사용할 수 있다. 

* Google Home 팀에서 코틀린 도입 후 비정상 종료의 원인인 NullPointerException 33% 절감  
      
<br />

### 유명 오픈 소스 프로젝트의 코틀린 지원 현황 
* 스프링 프레임워크
* Gradle 
    - 오픈 소스 빌드 자동화 도구 
    - Kotlin DSL을 지원하기 때문에 IntelliJ의 지원을 받을 수 있음 
* Ktor 
    - 서버 프레임워크
    - 100% Kotlin으로 작성 
  
* Exposed 
    - 코틀린으로 만든 ORM 프레임워크 
    - 100% 코틀린으로 작성 
  

<br />

### 자바에는 있지만 코틀린에는 없는 기능 
#### 1. Checked Exception 
* 자바의 예외        
  참고: <https://hyerin6.github.io/2022-02-08/chap10/>      


* 자바에서 checked exception은 컴파일 에러가 발생하기 때문에 무조건 try-catch로 감싸거나 throws로 예외를 전파해야 한다.    


* 대부분 의미 없는 예외 처리를 반복한다. 
    - try-catch 감싸기 
    - runtime exception으로 감싸기 
  

* checked exception이 발생할 경우 catch 안에서 에러를 해결하는 일은 생각보다 흔하지 않고 오히려 생산성을 감소시킨다. 


* 코틀린은 checked exception을 강제하지 않는다.   

<br />

#### 2. 기본 자료형 
* 자바는 원시 자료형을 지원하며 객체로된 레퍼런스 타입도 지원한다.

```kotlin
int i = 0;
Integer ii = 0;
String str = ii.toString();
```


* 코틀린은 레퍼런스 타입만 지원한다. 

```kotlin
val i: Int = 0
val str: String = i.toString()
```


* 코틀린의 레퍼런스 타입은 최적화된 방식으로 컴파일된다. 

```kotlin
int i = 0 ;
String str = String.valueOf(i);
```


<br />


#### 3. 정적 멤버 
* 자바는 static 키워드로 정적 멤버를 선언한다.   

* 코트린은 companion object 로 대체 

```kotlin
class KotlinClass {
  
  companion object {
    val i: Int = 0
    
    fun function() {
        // ...
    }
  }
    
}
```


<br />    


#### 4. 삼항 연산자 
* 자바는 삼항 연산자가 존재한다.     

* 코틀린은 if-else로 대신한다.   
  
```kotlin
val animalSound: String = if ("호랑이" == animal) "어흥" else "야옹"
```


<br />

#### 5. 세미콜론 (;)

코틀린은 세미콜론이 필수가 아님 


<br />

### 코틀린에는 있지만 자바에는 없는 기능 
#### 1. 확장 
개발자가 임의로 객체의 함수나 프로퍼티를 확장해서 사용할 수 있다. 

```kotlin
MyStringExtensions.kt

fun String.first(): Char {
  return this[0]
}

fun String.addFirst(char: Char): String {
  return char + this.substring(0)
}


fun main() {
  println("ABCD".first())         // 출력: A
  println("ABCD".addFirst('Z'))   // 출력: ZABCD 
}

```


<br />

#### 2. 데이터 클래스 
* 데이터를 보관하거나 전달하는 목적을 가진 불변 객체로 사용 - `DTO`

```kotlin
// equals(), hashCode(), toString() 등 함수를 자동 생성 
data class Person(val name: String, val age: Int)
```


* 자바에서는 Lombok 사용: `@Data` 


* JDK 15에서 `record` 라는 이름으로 추가됨 

```java
public record Person(String name, int age) { ... }
```


<br />





















