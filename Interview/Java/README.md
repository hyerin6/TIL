# Java   

## 특징
* 객체지향 프로그래밍 언어
* 컴파일 언어
* JVM만 있으면 OS와 상관없이 동작 가능 (운영체제에서 독립적)
* 고성능: 바이트코드로 변환되어 실행
* 멀티 스레딩 지원

<br />

## JVM
* 자바 프로그램을 실행하는 역할
* 컴파일러를 통해 바이트코드로 변환된 파일을 JVM에 로딩하여 실행
* Garbage Collection 수행 (메모리 관리)
* Class Loader - JVM내 (Runtime Data Area)로 class 파일을 로드하고 링크(Linking)
* Excution Engine - 메모리(Runtime Data Area)에 적재된 클래스들을 기계어로 변경해 실행
* Garbage Collector - 힙 메모리에서 참조되지 않는 객체를 제거
* Runtime Data Area - 자바 프로그램을 실행할 때, 데이터를 저장

<br />

## Java 프로그램 실행 과정
(1) JVM은 OS로부터 메모리(Runtime Data Area)를 할당받는다.    
(2) 컴파일러(javac)가 소스코드(.java)를 읽어서 바이트코드(.class)로 변환   
(3) Class Loader를 통해 Class 파일을 JVM 내의 Runtime Data Area에 로딩      
(4) 로딩된 Class 파일을 Excution Engine을 사용해 해석 및 실행

<br />

## JVM 메모리 (Runtime Data Area) 구조
메소드(static)영역, JVM 스택, JVM힙

* 메소드 영역
    - 클래스가 사용되면 해당 클래스의 클래스 파일(.class)을 읽어들여, 클래스에 대한 정보(바이트코드)를 static 영역에 저장
    - 클래스와 인터페이스(타입 정보), 메소드(이름, 리턴 타입, 접근 제어자 등), 필드, static 변수, final class 변수
    - 런타임 상수 풀(클래스와 인터페이스의 상수, 메소드/필드에 대한 모든 레퍼런스): JVM이 이를 이용해 실제 메모리 주소를 찾아 참조


* JVM 스택
    - 스레드마다 존재, 스레드가 시작할 때 할당, LIFO
    - 지역변수, 매개변수, 연산 중 발생하는 임시 데이터 저장
    - 메소드 호출 시마다 개별적 스택 생성
    - 기본 타입 변수: 스택에 값 저장
    - 참조 타입 변수: 힙이나 메소드 영역 객체의 주소 저장


* JVM 힙
    - 런타임 시 동적으로 할당하여 사용하는 영역
    - New 연산자로 생성된 객체와 배열 저장
    - 힙 영역의 객체와 배열은 스택의 변수나 다른 객체에서 참조, 참조가 없으면 GC의 대상

<br />

## 변수 종류 & 메모리 구조
* 인스턴스 변수: 객체에서 사용되는 변수, 힙 영역
* 클래스 변수: static 변수, 메소드 영역 
* 지역변수: 메소드 내에서 선언되는 변수, 스택 영역 

<br />

## Java에서 GC가 필요한 이유  
자바는 메모리를 명시적으로 해제하지 않기 때문에 GC를 통해서 필요없는 객체를 지운다.   
더이상 사용하지 않는 동적 할당된 메모리 블럭(heap)을 찾아 다시 사용 가능한 자원으로 회수한다.   

<br />

## Java GC 동작 방식   
(1) 새롭게 생성된 객체는 Young의 Eden 영역에 저장     

(2) Eden 영역이 다 차면 Minor GC가 발생하여 참조 횟수에 따라 증가하는 age bit을 보고   
불필요한 객체는 삭제하고 생존하는 객체는 S0으로 이동한다.     

(3) Minor GC가 발생할 때마다 Young 영역의 객체들은 삭제와 이동을 한다.     
(Eden ➡️ S0 / S0 ➡️ S1 / S0 ⬅️ S1) 

(4) S1이 가득차면 불필요한 객체는 Old 영역으로 이동하고 Old 영역이 가득차면 Major GC를 통해서 값을 삭제한다.   

(5) GC가 실행될 때마다 STOP THE WORLD가 발생하여 프로그램이 중지된다.   

<br />

## 추상 클래스와 인터페이스   
#### Q. 추상 클래스란?   
* 반드시 구현(오버라이딩)해야하는 추상 메소드를 1개 이상 갖고 있는 클래스 
* 사용이유 
    - 필드와 메소드 통일 - 유지보수성 향상 및 통일성 유지   
    - 규격에 맞는 실체 클래스 구현   
    - 상속 받아서 기능을 확장시키는게 목적이다.


#### Q. 인터페이스란?     
* 어떤 메소드를 제공하는지 알려주는 명세 (Specification)    
* 상속 관계가 없는 클래스에서 공통되는 로직을 구현하여 사용   
* 사용이유 
    - 동일한 목적 하에 동일한 기능을 보장하는 것 - 메소드를 구현하는 것에 초점 
    - 다형성 극대화 - 코드 수정 감소, 유지보수성 증가  
  

#### 공통점 
* 독립적으로 객체 생성 불가, 상속을 목적으로 사용   
* 추상 메소드는 오버라이딩 필요  


#### 차이점 
* 추상 클래스는 상속을 통해 기능을 확장하는 것이 목적 
* 인터페이스는 추상 클래스와 달리 구현을 강제함으로써 구현 객체의 같은 동작을 보장하며   
인터페이스를 이용하여 표준화를 확립시킬 수 있으므로 서로 관계가 없는 객체들이 상호작용을 가능하게 한다.   

<br />

## Vector와 ArrayList 차이
* Vector: Thread-Safe
* ArrayList: Thread-Safe 하지 않음 

<br />

## Hash Map과 Hash Table의 차이
* Hash Map: 메소드 동기화를 지원하지 않음, Thread Safe 아님, 성능 빠름
* Hash Table: 메소드 동기화 지원, Thread Safe, 성능 느림

<br />

## Hash Map과 Tree Map의 차이
* Hash Map: 해싱 구현, 랜덤 정렬(순서 유지안됨), Null 키 가능, 훨씬 빠름
* Tree Map: 레드블랙트리(이진탐색트리)로 구현, key로 자동 정렬, Null 키 불가능

<br />

## Hash Set과 Tree Set의 차이
* Hash Set: 해싱으로 구현, 삽입된 요소는 랜덤 정렬, Null 저장 가능, 성능 빠름, 값 비교에 equals 사용
* Tree Set: 레드블랙트리(이진탐색트리) 구현, 정렬 순서 유지(자동 정렬), Null 저장 불가, 성능 느림, 값 비교에 comparedTo 사용

<br />

## Generic  
* 제네릭은 다양한 타입의 객체들을 다루는 메서드나 컬렉션 클래스에서 사용하는 것으로, 컴파일 과정에서 타입을 체크하는 기능
* 클래스 내부에서 사용할 데이터 타입을 외부에서 지정하는 기법
* 사용 이유
    - 잘못된 타입이 사용될 문제를 컴파일 과정에서 제거 가능
    - 코드 재사용성 증가

<br />

## Access Modifier(접근 제한자)  
* public: 모든 클래스에서 접근할 수 있다는 것을 의미(패키지가 달라도 허용)
* protected: 같은 클래스 내에서 접근 허용, 같은 패키지의 다른 클래스에서 접근 허용, 다른 패키지의 상속받은 클래스에서 접근 허용, 다른 패키지의 다른 클래스에서 접근 불가
* default: 같은 패키지 내에서만 접근 허용
* private: 동일 패키지라도 접근 불가, 같은 클래스 내에서만 접근 허용
* 사용 이유
    - 정보은닉(민감 정보 유출 안하기), 외부에서 알 필요 없는 값은 노출하지 않는다. 
    - 응집도 높이고 결합도 낮추기 위함   

<br />

## final 키워드
* final 키워드는 상수로 정의하는 키워드
* final class: 다른 클래스에서 상속하지 못함
* final method: 다른 메소드에서 오버라이딩하지 못함 (오버로딩 가능)
* final variable: 변하지 않는 상수값이 되어 새로 할당할 수 없는 변수가 됨
* 생성자: final이 될 수 없음

<br />

## non-static과 static 멤버의 차이
#### non-static
* 객체마다 멤버가 존재
* 객체 생성 시에 멤버 생성
* 객체가 생성될 때 멤버 생성, 객체가 사라질 때 멤버가 사라짐
* 공유X

#### static
* 객체를 많이 생성해도 해당 변수는 한개만 존재(객체와 무관한 키워드), 클래스당 1개 존재
* 클래스를 로딩할 때, 멤버 생성, 처음 설정된 메모리 공간이 변하지 않음을 의미
* 객체가 생성되기 전에 사용 가능, 객체가 사라져도 멤버는 사라지지 않음 → 프로그램 종료 시 사라짐
* 동일한 클래스의 모든 객체들에 의해 공유

<br />

## 람다 표현식
* 식별자 없이 실행이 가능한 함수
* 함수를 따로 만들지 않고 코드 한 줄에 함수를 써서 그것을 호출
* 자바8부터 지원, 코드가 간결하고 가독성이 높음
* 재사용이 불가능하고 디버깅이 어려움
* 예시: Optional, Stream

<br />

## Reflection
JVM에서 실행되는 애플리케이션의 런타임 동작을 검사하거나 수정할 수 있는 기능이 필요한 프로그램에서 사용

<br />

## ThreadLocal
* 오직 한 쓰레드에 의해 읽고 쓰여질 수 있는 변수를 생성
* 오직 한 쓰레드 당 한 번의 실행을 허용

<br />

## Java7에서 Java8 
* 람다 표현식 추가: 함수형 프로그래밍
* Permanent Generation: Java7버전까지는 Heap에 존재, 8부터는 Native Method Stack에 Meta Space로 변경
* 인터페이스에 default 메소드, static 메소드 추가
* Stream API: 데이터의 추상화
* `java.time` 패키지: Joda-Time을 이용한 새로운 날짜와 시간 API
  
<br />

## 직렬화(serialization)란?        
자바에서 입출력을 할 때에는 스트림이라는 통로를 통해 데이터가 이동한다.      
하지만 객체는 바이트형이 아니라서 스트림을 통해 파일에 저장하거나 네트워크로 전송할 수 없다.           

따라서 객체를 스트림을 통해 입출력하려면 바이트 배열로 변환하는 것이 필요한데, 이를 '직렬화' 라고 한다.          
반대로 스트림을 통해 받은 직렬화된 객체를 원래 모양으로 만드는 과정을 역직렬화라고 한다.        

<br />

## 정적멤버를 `클래스명.정적멤버`로 접근해야 하는 이유  

```java
public class Test {
    static int f;

    public static void main(String[] args) {
        System.out.println(Test.f);  // 클래스명.정적멤버
    }
}
```

<br />  

#### (1) 일관된 형식으로 접근    

|             |                    | ClassA.pubSt | pubSt | this.pubSt |
|:-----------:|:------------------:|:------------:|:-----:|:----------:|
| 같은 패키지 |     상속한 경우    |       🆗      |   🆗   |      🆗     |
|             | 상속하지 않은 경우 |       🆗      |   ❌   |      ❌     |
| 다른 패키지 |     상속한 경우    |       🆗      |   🆗   |      🆗     |
|             | 상속하지 않은 경우 |       🆗      |   ❌   |      ❌     |

정적 멤버에 접근하는 방법은 위와 같이 세 가지가 존재한다.        
`클래스명.정적멤버`는 어떤 상황에서도 사용이 가능하지만 다른 접근 방법은 상황에 따라   
사용 가능 하기도 하고 사용 불가능하기도 한다.
이렇게 일관된 형식을 유지하기 위해서 `클래스명.정적멤버`로 접근하라고 하는 것이다.   

<br />

#### (2) 메모리의 물리적 접근   
<img width="650" src="https://user-images.githubusercontent.com/33855307/145699456-3cb8c54e-0587-4c8b-b3f4-4f410865b33b.jpeg">

`사람.인구(클래스명.정적멤버)`로 접근하면 한번에 바로 접근할 수 있다.        
반면 `홍길동.인구수(참조변수.정적멤버)`로 접근을 하면        
스택 영역에 존재하는 참조변수 → 힙 영역에 존재하는 객체 → 객체가 다시 정적 멤버를 가리키는 순으로 참조를 하게 된다.    


<br />


## 객체지향 5원칙: SOLID  
<https://hyerin6.github.io/2020-01-08/solid/>  
<br />  

#### (1) SRP - 단일 책임 원칙  
어떤 클래스를 변경해야 하는 이유는 오직 하나뿐이어야 한다.      

<br />

#### (2) OCP - 개방 폐쇄 원칙  
소프트웨어 엔티티(클래스, 모듈, 함수 등)는 확장에 대해서는 열려 있어야 하지만 변경에 대해서는 닫혀 있어야 한다.    
자신의 확장에는 열려 있고, 주변의 변화에 대해서는 닫혀 있어야 한다.      

<br />    

#### (3) LSP - 리스코프 치환 원칙    
서브 타입은 언제나 자신의 기반 타입으로 교체할 수 있어야 한다.

* 하위 클래스 is a kind of 상위 클래스 - 하위 분류는 상위 분류의 한 종류다.
* 구현 클래스 is able to 인터페이스 - 구현 분류는 인터페이스 할 수 있어야 한다.

즉, 하위 클래스의 인스턴스는 상위형 객체 참조 변수에 대입해 상위 클래스의 인스턴스 역할을 하는 데 문제가 없어야 한다.     

<br />  

#### (4) ISP - 인터페이스 분리 원칙    
클라이언트는 자신이 사용하지 않는 메소드에 의존 관계를 맺으면 안 된다.

* 상위 클래스는 풍성할수록 좋고, 인터페이스는 작을 수록 좋다.
* 상위 클래스는 하위 클래스에게 자신의 기능을 확장해주기 때문에, 하위 클래스 입장에선 많은 기능이 확장되면 될 수록 좋을 거 같다.
* 인터페이스는 하위 클래스에게 어떠한 기능을 필수록 구현시키는 것이기 때문에,   
하위 클래스에서 많은 기능을 오버라이딩 해야 하고 그러다보면 SOLID 원칙에 위배될 거 같다.

<br />    

#### (5) DIP - 의존 역전 원칙   
* 고차원 모듈은 저차원 모듈에 의존하면 안 된다. 
* 이 두 모듈 모두 추상화된 것에 의존해야 한다. 
* 추상화된 것은 구체적인 것에 의존하면 안 된다 
* 구체적인 것이 추상화된 것에 의존해야 한다. 
* 자주 변경되는 구체 클래스에 의존하지 마라 

> 자신보다 변하기 쉬운 것에 의존하던 것을 추상화된 인터페이스나 상위 클래스를 두어      
> 변하기 쉬운 것의 변화에 영향받지 않게 하는 것이 의존 역전 원칙이다.        


<br />


## Comparator와 Comparable 차이  

* Comparator 인터페이스 

```java
package java.util;

@FunctionalInterface
public interface Comparator<T> {

    int compare(T o1, T o2);
}
```

기본 정렬 기준 외에 다른 기준으로 정렬하고자 할 때 사용(내림차순, 자기가 정한 정렬 기준 등등)


* Comparable 인터페이스

```java
package java.lang;

public interface Comparable<T> {

    public int compareTo(T o);
}
```

기본 정렬 기준을 구현하는데 사용


* 예제 코드 

```java
import java.util.Arrays;
import java.util.Comparator;

public class Test {
    public static void main(String[] args) {
        String[] list=  {"cat", "Dog", "lion", "tiger"};
        Arrays.sort(list); // String의 Comparable 구현에 의한 정렬
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER); // 대소문자 구문 안함
        Arrays.sort(list, new Descending()); // 내림차순 정렬
    }
}

class Descending implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Comparable && o2 instanceof Comparable) {
            Comparable c1 = (Comparable)o1;
            Comparable c2 = (Comparable)o2;
            return c1.compareTo(c2) * -1;
        }
        return -1;
    }
}
```

<br />

## String 클래스의 hashcode 메소드 재정의한 이유는?    
String 클래스의 hashcode 메소드는 Object 클래스의 hashcode 메소드를 그대로 사용하지 않고 재정의했다.     
hashcode 메소드 규칙에 따르면 같은 문자열이면 같은 hashcode 값을 가져야 한다.    
그런데 String은 문자열이 같더라도 다른 객체일 수 있기 때문에 String 클래스는 같은 문자열이면 hashcode가 같도록 재정의했다.   
(즉 주소값이 아니라 내용 값으로 비교한다.)

`System.identityHashCode()`는 Object 클래스의 hashCode 메소드처럼 객체의 주소값으로 해시코드를 생성하기 때문에   
모든 객체에 대해 항상 다른 hashcode 값을 반환할 것을 보장한다.   

<br />


## Object 클래스의 clone 메소드   

```java
public class Object {
    protected native Object clone() throws CloneNotSupportedException;
}
```

clone()은 단순히 참조 변수의 값만을 복사하기 때문에 shallow copy(얕은 복사) 된다.    

#### shallow copy
shallow copy는 객체 자체를 복사하는 것이 아니라 객체가 가리키고 있는 곳의 주소를 복사한다는 뜻이다.     
그래서 원본을 수정했을 때 복사본도 같이 수정이 된다.   

#### Deep copy
원본을 수정했을 때 복사본에 영향을 미치지 않는다.   
객체를 복사해 새로운 메모리에 할당한 후에 그 메모리를 가리킨다.    

#### clone()을 사용하려면 Cloneable 인터페이스를 구현해야 한다.    
```java
public interface Cloneable {
}
```

내부에 아무 것도 없는 인터페이스이다.     
직렬화처럼 Cloneable 인터페이스를 구현해야 자바에서 `clone()`을 사용할 수 있게 해준다.     

Cloneable 인터페이스를 구현한 클래스의 인스턴스만 `clone()`을 통한 복제가 가능한데,   
그 이유는 인스턴스의 데이터를 보호하기 위해서이다.   
Cloneable 인터페이스가 구현되어 있다는 것은 클래스 작성자가 복제를 허용한다는 의미이다.  

<br />

## String이 불변 객체인 이유  
<https://www.baeldung.com/java-string-immutable>  

불변 객체는 객체가 완전히 생성된 후에 내부 상태가 일정하게 유지되는 객체이다.      
Java에서 String을 불변 객체로 만든 이유에는 성능, 동기화, 캐싱, 보안이 있다.     
<br />  

#### (1) 성능
<img width="500" src="https://user-images.githubusercontent.com/33855307/145700917-702b8023-a51c-40b1-9bff-9bacf29b6ca6.jpeg">

자바에서 문자열은 정말 많이 사용된다.   
그래서 자바는 상수 풀이라는 것을 만들었다.   

위 그림에서 보면 s2는 상수 풀에서 해당 문자열이 있는지 검색하고 같은 값을 가진 s1을 이미 상수 풀에 등록했기 때문에 같은 레퍼런스를 반환한다.     
문자열 리터럴을 캐싱하고 재사용하면 문자열 풀의 다른 문자열 변수가 동일한 개체를 참조하기 때문에 힙 공간을 많이 절약할 수 있다.   

이렇게 상수 풀을 사용하는데 String이 불변 객체가 아니라면?      
다음 코드의 결과는 true가 될 것이다.    
상수 풀을 장점을 얻어 사용하기 위해서 String을 불변 객체로 만들어야 한다.   

```java
String s1 = "Hello World";
String s2 = "Hello World";
s1 = "Hi";
System.out.println(s1 == s2); 
```

<br />

#### (2) 동기화(Synchronization)
불변 객체는 값이 바뀔 일이 없기 때문에 멀티스레드 환경에서 `Thread-safe` 하다는 장점이 있다.    
따라서 일반적으로 불변의 개체는 동시에 실행되는 여러 스레드에서 공유할 수 있다.     
스레드가 값을 변경하면 동일한 문자열을 수정하는 대신 문자열 풀에 새 문자열이 생성되기 때문에 스레드 안전하다.       

<br />

#### (3) 해시코드 캐싱(Hashcode Caching)  
문자열 객체는 데이터 구조로 많이 사용되기 때문에 HashMap, HashTable, HashSet 등과 같은 Hash 구현에 많이 사용된다.   
해시 구현에 따라 작동할 때 `hashCode()` 메소드가 자주 호출된다.    

String의 `hashCode()` 메서드 구현을 보면 아직 hash 값을 계산한 적이 없을 때      
최초 1번만 실제 계산 로직을 수행하고, 이후부터는 해당 값을 그냥 리턴만 하도록 overriding 되어 있다.   
(계산해놓은 해시코드를 재사용)    

String이 불변이기 때문에 이렇게 캐싱이 가능하다는 이점을 활용할 수 있다.   
따라서 String으로 해시 구현을 사용하는 컬렉션의 성능이 향상시킬 수 있습니다.

<br />

#### (4) 보안 
문자열은 사용자 이름, 암호, 연결 URL, 네트워크 연결 등과 같은 중요한 정보를 저장하는 데 널리 사용된다.   
클래스를 로드하는 동안 JVM 클래스 로더에서도 광범위하게 사용된다.   

만약에 String이 불변 객체가 아니라면 메소드를 호출했던 클라이언트는 String에 대한 참조가 메소드를 호출한 이후에도 남아있다.     
따라서 보안 검사를 실시한 이후에도 이 문자열이 안전하다고 보장할 수 없고   
메소드를 호출했던 클라이언트가 참조를 갖고 있기 때문에 문자열을 변경할 수 있다는 가능성이 남아있다.       
이 경우 SQL 주입을 쉽게 수행할 수 있다. 따라서 문자열이 변경되면 시간이 지남에 따라 보안 성능이 저하될 수 있다.     
또한 문자열 사용자 이름이 다른 스레드에 표시될 수 있으며, 이 스레드는 무결성 검사 후 해당 값을 변경할 수 있다.   






<br />

## String `+` 연산이 일어나는 과정

StackOverFlow에서 다음과 같은 내용을 본 적이 있다.    
`+` 연산자는 Java 컴파일러에서 구현이 되며 `String + String` 연산은 컴파일 타임에 상수 혹은 `StringBuilder` 코드로 변환된다.   
<br />  


* byte 코드 확인하는 방법  

```
> javac Application.java 
> javap -c Application.class
```

StringBuilder 객체가 생성되고 StringBuilder의 `append()` 메소드가 호출되는 것을 확인할 수 있었다.   

컴파일 타임에 다음과 같이 변환되는 것으로 유추할 수 있었다.   


```java
StringBuilder builder = new StringBuilder(); 
builder.append(s1); 
builder.append(s2); 
String s3 = builder.toString();

```

StringBuilder를 내부적으로 사용하긴 하지만 매번 새로 생성되어 `append()` 연산을 한 후   
다시 String으로 변환되는 방식이기 때문에 문자열 연결이 느리다는 것이다.  

<br />

## String Constant pool    
* <https://www.baeldung.com/java-string-pool>  
* <https://johngrib.github.io/wiki/java8-why-permgen-removed/>  
* <https://stackoverflow.com/questions/4918399/where-does-javas-string-constant-pool-live-the-heap-or-the-stack>  
 

`new` 연산자를 사용하지 않고 String 클래스를 사용하면 String Constant pool(상수 풀)을 사용한다.   

Java7에서 상수 풀은 Perm 영역에 있었다.     
Perm 영역은 보통 Class의 Meta 정보나 Method의 Meta 정보, Static 변수와 상수 정보들이 저장되는 공간으로 흔히 메타데이터 저장 영역이다.      
메모리 사이즈도 고정이기 때문에 String 상수 풀이 많이 생기거나 많은 클래스 정보들이 등록되었을 때  
OutOfMemory Error가 발생할 가능성이 존재해서 위험하다는 특징을 가지고 있다.    


그래서 Java8부터  Perm 영역이 삭제되고 Native 영역으로 이동하여 Metaspace 영역으로 변경되었다.     
단, 기존 Perm에 존재하는 Static Object는 Heap 영역으로 옮겨져서 GC의 대상이 최대한 될 수 있도록 했다.    
정리하면, 각종 메타 정보를 OS가 관리하는 영역으로 옮겨 Perm 영역의 사이즈 제한을 없앤 것이라고 할 수 있다.     


따라서 상수풀은 JVM 내에 Runtime Data Area → Method Area 안에 존재하는 것을 알 수 있다.  


<br />

## check exception와 unchecked exception 차이  

자바의 예외는 크게 3가지로 나눌 수 있다.  
* 체크 예외(Checked Exception)
* 에러(Error)
* 언체크 예외(Unchecked Exception)

<br />  


#### Error  
에러는 시스템에 비정상적인 상황이 발생했을 경우에 발생한다.   
메모리 부족(OutofMemoryError)나 스택오버플로우(StackOverflowError)와 같이 복구할 수 없는 것을 말한다.    
이러한 에러는 개발자가 예측하기 쉽지 않고 처리할 수 있는 방법이 없다.  

<br />

#### 체크 예외, Checked Exception  
체크 예외는 RuntimeException의 하위 클래스가 아니면서 Exception 클래스의 하위 클래스들이다.   
체크 예외의 특징은 반드시 에러 처리를 해야하는 특징을 가지고 있다. (try/catch or throw)

<br />

#### 언체크 예외, Unchecked Exception 
언체크 예외는 RuntimeException의 하위 클래스들을 의미한다.     
체크 예외와는 달리 에러 처리를 강제하지 않는다.    
말 그대로 실행 중에 발생할 수 있는 예외를 의미한다.       

<br />

## 언체크 예외를 강제하지 않는 이유는?  

```java
public class ArrayTest {
    public static void main(String[] args) {
        try {
            int[] list = {1, 2, 3, 4, 5};
            System.out.println(list[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
```


단순히 배열을 만들어 출력하고자 하는데 try/catch 문을 꼭 사용해야 한다.          
이러한 RuntimeException은 개발자들의 의해 실수로 발생하는 것들이기 때문에 예외처리를 강제하지 않는다.         

<br />

## 체크 예외와 언체크 예외의 Rollback 여부   

|                                  | Checked exception                                                                                                      | Unchecked Exception                                                                                                                                      |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| 처리 여부                        | 반드시 예외를 처리해야 함                                                                                              | 명시적인 처리를 강제하지 않음                                                                                                                            |
| 확인 시점                        | 컴파일 단계                                                                                                            | 실행 단계                                                                                                                                                |
| 예외발생시 <br />  트랜잭션 처리 | roll-back 하지 않음                                                                                                    | roll-back 함                                                                                                                                             |
| 대표 예외                        | Exception의 상속받는 하위 클래스 중 <br /> Runtime Exception을 제외한 모든 예외  <br /> *  IOException  <br /> * SQLException | RuntimeException 하위 예외 <br /> *  NullPointerException <br /> *  IllegalArgumentException <br /> *  IndexOutOfBoundException <br /> * SystemException |



예외 복구 전략이 명확하고 해결 가능하면 Checked Exception을 try/catch로 잡고 복구하는 것이 좋다.                         
하지만 그렇지 않은 경우 더 구체적인 Unchecked Exception을 발생시키고 예외에 대한 메시지를 명확하게 전달하는 것이 효과적이다.         

<br />

## Process & Thread  
<https://hyerin6.github.io/2020-06-12/프로세스와스레드/>  

#### 프로세스 
프로세스란 실행 중인 프로그램이다.    
프로그램을 실행하면 OS로부터 실행에 필요한 자원(메모리)을 할당받아 프로세스가 된다.   

프로세스는 프로그램을 수행하는 데 필요한 데이터, 메모리 등의 자원과 스레드로 구성되어 있다.   

#### 스레드
프로세스 자원을 이용해서 실제로 작업을 수행하는 것이 스레드이다.   

#### 싱글 스레드 & 멀티 스레드   
멀티 스레드는 하나의 프로세스 내에서 둘 이상의 스레드가 동시에 작업을 수행하는 것을 의미한다.     
멀티 스레드와 멀티 프로세스 모두 여러 흐름을 동시에 수행한다는 공통점이 있다.       
그러나 멀티 프로세스는 각 프로세스가 독립적인 메모리를 가지고 별도로 실행되지만,      
멀티 스레드는 각 스레드가 자신이 속한 프로세스의 메모리를 공유한다는 점이 다르다.       

<br />

## volatile 
멀티 코어 프로세서에서는 코어마다 별도의 캐시를 가지고 있다.   


<img width="550" src="https://user-images.githubusercontent.com/33855307/145704468-f6e8d7cd-2f56-4485-aab0-7265181b3832.png">


코어는 메모리에서 읽어온 값을 캐시에 저장하고 캐시에서 값을 읽어서 작업한다.    
다시 값을 읽어올 때는 먼저 캐시에 있는지 확인하고 없을 때만 메모리에서 읽어온다.   
캐싱으로 인해 메모리의 특정 변수 값이 변경되었는데 캐시에 변경된 값이 반영되지 않아 문제가 발생할 수 있다.   

이러한 문제는 다음과 같은 방법으로 해결할 수 있다.  
* volatile 사용하기: 코어가 변수 값을 읽어올 때 캐시가 아닌 메모리에서 값을 읽어온다.   
* synchronized 블록 사용하기: 스레드가 synchronized 블록으로 들어가고 나올 때, 캐시와 메모리간의 동기화가 이루어진다. 


volatile는 변수의 read와 write를 메인 메모리에서 진행하게 된다.          
CPU cache보다 메인 메모리가 비용이 더 크기 때문에         
변수 값 일치를 보장해야 하는 경우에만 volatile를 사용하는 것이 좋다.         

그렇다면 언제 volatile이 적합할까?       
멀티 스레드 환경에서 하나의 스레드만이 read&write하고 나머지 스레드가 read하는 상황에서       
가장 최신의 값을 보장해야 할 때 volatile를 사용해야 한다.      

여러 스레드가 write 하는 상황에서는     
synchronized 를 통해 변수 read&write의 원자성을 보장해야 한다.     

<br />

## ThreadLocal   
각 스레드에서 혼자 쓸 수 있는 값을 가지려면 ThreadLocal을 사용하면 된다.     
이전에 여러 스레드에서 데이터를 공유할 때 발생하는 문제를 해결하기 위해 synchronized 키워드를 사용했는데    
그런데 만약 스레드 별로 서로 다른 값을 처리해야 한다면? ThreadLocal을 사용하면 된다.   

```java
public class ThreadLocalSample {
    private final static ThreadLocal<Integer> local = new ThreadLocal<>();

    . . .
}
```

<br />

## 객체지향 
#### 객체란?
물리적으로 존재하거나 추상적으로 생각할 수 있는 것 중에서 자신의 속성을 가지고 있고 식별 가능한 것이다. 

#### 객체지향이란? (OOP)
프로그래밍에서 필요한 데이터를 추상화시켜 상태와 행위를 가진 객체를 만들고      
그 객체들 간의 연관된 상호작용을 통해 로직을 구성하는 프로그래밍 방법이다.        

#### 객체지향 장점 
* 코드 재사용이 용이하다. (상속, 캡슐화, 다형성)
* 유지보수가 쉽다. 
* 클래스 단위로 모듈화시켜 개발할 수 있으므로 개발 업무 분담이 쉽다. 

#### 객체지향 단점 
* 처리 속도가 상대적으로 느리다. 
* 설계가 어렵다.

<br />

## 객체지향 특징 
<https://hyerin6.github.io/2020-06-09/스프링입문을위한(1)/>    

#### 추상화
* 공통의 속성이나 기능을 묶어 이름을 붙이는 것

#### 캡슐화
* 캡슐화의 목적 2가지
  - 코드를 재수정 없이 재활용하는 것.
  - 접근 제어자를 통한 정보 은닉

* 객체가 외부에 노출하지 않아야할 정보 또는 기능을 접근제어자를 통해 적절히 접근 제어  

* 기능과 특성의 모음을 클래스라는 캡슐에 분류해서 넣는 것


#### 상속
* 부모 클래스의 속성과 기능을 그대로 이어받아 사용할 수 있게하고 기능의 일부분을 변경해야 할 경우   
  상속받은 자식 클래스에서 해당 기능만 다시 수정(정의)하여 사용할 수 있게 하는 것  


#### 다형성
* 하나의 변수명, 함수명 등이 상황에 따라 다른 의미로 해석될 수 있는 것
* 오버라이딩: 부모클래스의 메서드와 같은 이름, 매개변수를 재정의 하는것
* 오버로딩: 같은 이름의 함수를 여러개 정의하고, 매개변수의 타입과 개수를 다르게 하여 매개변수에 따라 다르게 호출할 수 있게 하는 것  

<br />

## 람다에서 사용되는 지역 변수가 final 혹은 effectively final 이어야 하는 이유 
<https://hyerin6.github.io/2021-12-28/lambda/>  
지역 변수가 **스택에 저장**되기 때문에 람다식에서 값을 바로 참조하는 것에 제약이 있어 복사된 값을 이용하게 되는데,   
이때 멀티 쓰레드 환경에서 복사 될/복사된 값이 변경 가능 할 경우 이로 인한 **동시성 이슈**를 대응할 수 없기 때문이다.

<br />

## Optional`에서 `orElseGet`와 `orElse` 차이 & 원리


<br />

## 우리가 주로 멀티 스레딩으로 구현하는 이유


<br />


## 컴파일 언어 & 인터프리터 언어


<br />


## Vector와 List 





