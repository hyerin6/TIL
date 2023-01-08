# 4. 클래스, 객체, 인터페이스   
## 4.1 클래스 계층 정의 
### 4.1.1 인터페이스 

#### 인터페이스 선언 

```kotlin
interface Clickable {
    fun click()
}
```

click 이라는 추상 메서드가 있는 간단한 인터페이스를 선언했다. 

<br />    

#### 인터페이스 구현   

```kotlin
class Button : Clickable {
    override fun click() = println("I was cliked")
}
```

자바에서 `extends`와 `implements` 키워드를 사용하지만 코틀린에서는 클래스 이름 뒤에 콜론(`:`)을 붙이고 인터페이스와 클래스 이름을 적는 것으로 클래스 확장과 인터페이스 구현을 모두 처리한다.


* 자바와 동일하게 여러 개의 인터페이스 구현 가능, 클래스는 한 개만 상속 가능   
* `override` 수식어: 인터페이스나 상위 클래스의 메서드, 프로퍼티 재정의시 꼭 사용해야 한다.

> 자바와 달리 코틀린에서는 override 변경자를 꼭 사용해야 한다.   
> 실수로 상위 클래스 메서드를 오버라이드하는 경우를 방지해준다.     

<br />

#### 디폴트 메서드 
```kotlin
interface Clickable {
    fun click() // 일반 메서드 선언 
    fun showOff() = println("I'm clickable!") // 디폴트 구현이 있는 메서드 
}
```

<br />

#### 동일한 메서드를 구현하는 다른 인터페이스 정의 
상황) 클래스가 구현하는 두 상위 인터페이스에 `showOff()` 라는 동일한 이름의 메서드가 있다.   

어느 쪽 showOff 메서드가 선택될까? 어느 쪽도 선택되지 않는다.   
showOff 구현을 대체할 오버라이딩 메서드를 직접 제공하지 않으면 다음과 같은 컴파일 오류가 발생한다.   

```kotlin
The class 'Button' must 
override public open fun showOff() because it inherits
many implementations of it.
```
 

다음은 상속한 인터페이스의 메서드 구현을 호출하는 코드이다.  

```kotlin
class Button : Clickable, Focusable {
    override fun click() = println("I was clicked") 
    
    // 애매모호함을 없애기 위해 재정의함. 
    // 이름과 시그니처가 같은 멤버 메서드에 대해 둘 이상의 디폴트 구현이 있는 경우  
    // 인터페이스를 구현하는 하위 클래스에서 명시적으로 새로운 구현을 제공해야 한다. 
    override fun showOff() {
        // super<타입>으로 사용할 상위 타입 지정 
        super<Clickable>.showOff()
    }
} 
```

Button 클래스는 두 인터페이스를 구현한다.   
Button은 상속한 두 상위 타입의 `showOff()` 메서드를 호출하는 방식으로 showOff를 구현한다.   
상위 타입의 구현을 호출할 때 자바와 동일하게 `super`를 사용한다.    

> 코틀린의 디폴트 메서드는 자바의 정적 메서드로 구현 ﴾코틀린은 자바 1.6과 호환﴿  

<br />  
<br />  

### 4.1.2 open, final, abstract 변경자: 기본적으로 final 
자바에서는 final로 명시적으로 상속을 금지하지 않는 모든 클래스를 다른 클래스가 상속할 수 있다.   

기본적으로 상속이 가능하면 편리하겠지만 문제 생기는 경우도 많다.   

**취약한 기반 클래스**    
하위 클래스가 기반 클래스에 대해 가졌던 가정이 기반 클래스를 변경함으로써 깨져버리는 경우에 생긴다.

🌟 코틀린의 클래스와 메서드는 기본적으로 final이다. 

어떤 클래스의 상속을 허용하려면 클래스 앞에 `open` 변경자를 붙여야 한다.    
오버라이드를 허용하고 싶은 메서드나 프로퍼티의 앞에도 `open` 변경자를 붙여야 한다.   

<br />   

```kotlin
// 이 클래스는 열러있다. 다른 클래스가 상속할 수 있다.
open class RichButton : Clickable {
  
    // final 메서드  
    fun disable() {} 
  
    // 하위 클래스에서 이 메서드를 오버라이드할 수 있다. 
    open fun animate() {} 
  
    // 이 메서드는 (상위 클래스에서 선언된) 열려있는 메서드를 오버라이드한다. 
    // 오버라이드한 메서드는 기본적으로 열려있다. 
    override fun click() {} 
}
```

클래스나 인터페이스의 멤버를 오버라이드하는 경우 그 메서드는 기본적으로 열려있다.     
하위 클래스에서 오버라이드하지 못하게 금지하려면 `final override` 처럼 final을 명시해야 한다.     

<br />   

> <열린 클래스와 스마트 캐스트>  
> 클래스의 기본적인 상속 기능 상태를 final로 함으로써 얻을 수 있는 큰 이익은 다양한 경우에 스마트 캐스트가 가능하다는 점이다.    
> 스마트 캐스트는 타입 검사 뒤에 변경될 수 없는 변수에만 적용 가능하다.   
> 클래스 프로퍼티의 경우 val 이면서 커스텀 접근자가 없는 경우에만 스마트 캐스트를 쓸 수 있다는 의미이다.   
> 또한 프로퍼티가 final 이어야만 한다는 뜻이기도 하다.   
> 프로퍼티가 final이 아니라면 그 프로퍼티를 다른 클래스가 상속하면서 커스텀 접근자를 정의함으로써 스마트 캐스트의 요구 사항을 깰 수 있다.   

<br />


#### 추상 클래스 정의 
```kotlin
abstract class Animated {
  
    // 추상 함수 
    // 이 함수에는 구현이 없다. 
    // 하위 클래스에서 이 함수를 반드시 오버라이드해야 한다. 
    abstract fun animated()
    
    // 추상 클래스에 속했더라도 비추상 함수는 기본적으로 final 이다. 
    // 원하면 open 으로 오버라이드를 허용할 수 있다. 
    open fun stopAnimating() {}
    
    fun animateTwice() {}
    
}
```

<br />

#### 코틀린 상속 제어 변경자 

| 변경자 | 이 변경자가 붙은 멤버는 | 설명                                                                 |      
|---|---|--------------------------------------------------------------------|  
| final   | 오버라이드 가능| 클래스 멤버의 기본 변경자                                                     |  
| open    | 오버라이드 가능| 반드시 open을 명시해야 오버라이드할 수 있다.                                        |  
| abstract | 반드시 오버라이드해야 함| 추상 클래스의 멤버에만 이 변경자를 붙일 수 있다. <br /> 추상 멤버에는 구현이 있으면 안 된다.          |  
| override | 상위 클래스나 상위 인스턴스의 멤버를 오버라이드하는 중| 오버라이드하는 멤버는 기본적으로 열려있다. <br /> 하위 클래스의 오버라이드를 금지하려면 final을 명시해야 한다. |  

<br />
<br />

### 4.1.3 가시성 변경자: 기본적으로 공개 
가시성 변경자는 코드 기반에 있는 선언에 대한 클래스 외부 접근을 제어한다.   
어떤 클래스의 구현에 대한 접근을 **제한**함으로써 그 클래스에 의존하는 외부 코드를 깨지 않고도 클래스 내부 구현을 변경할 수 있다.   

<br />  

#### 코틀린의 internal  

* 코틀린은 아무 변경자도 없는 경우 선언은 모두 공개(public) 된다.   


* 자바의 기본 가시성인 패키지 전용(package-private)은 코틀린에 없다.   
코틀린은 패키지를 네임스페이스 관리 용도로만 사용한다. 


* 패키지 전용 가시성에 대한 대안으로 코틀린에서는 `internal` 이라는 새로운 가시성 변경자를 도입했다.   
    - internal: 모듈 내부에서만 볼 수 있음 이라는 뜻이다.     
    - 모듈: 한 번에 한꺼번에 컴파일되는 코틀린 파일들을 의미한다.  
    - 모듈 내부 가시성은 진정한 캡슐화를 제공한다는 장점이 있다.
    - 자바는 패키지가 같은 클래스를 선언하기만 하면 접근이 가능해 모듈의 캡슐화가 쉽게 깨진다.   


* 코틀린에서는 최상위 선언에 대해 private 가시성을 허용한다.

<br />   

#### 코틀린의 가시성 변경자 

| 변경자             | 클래스 멤버              | 최상위 선언             |    
|:-----------------|:---------------------|:--------------------|   
| public (기본 가시성) | 모든 곳에서 볼 수 있다.      | 모든 곳에서 볼 수 있다.     |  
| internal        | 같은 모듈 안에서 볼 수 있다.   | 같은 모듈 안에서만 볼 수 있다. |
| protected       | 하위 클래스 안에서만 볼 수 있다. | (최상위 선언에 적용할 수 없음) |
| private         | 같은 클래스 안에서만 볼 수 있다. | 같은 파일 안에서만 볼 수 있다. |

<br />

```kotlin
internal open class TalkativeButton : Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's talk!")
}

// 오류 : "public" 멤버가 자신의 "internal" 수신 타입인 "TalkativeButton"을 노출함 
fun TalkativeButton.giveSpeech() {
    // 오류 : "yell"에 접근할 수 없음. 
    // "TalkativeButton"의 private 멤버이다. 
    yell()
  
    // 오류 : "whisper"에 접근할 수 없음.
    // "TalkativeButton"의 protected 멤버이다. 
    whisper() 
}
```



코틀린은 public 함수인 giveSpeech 안에서 그보다 가시성이 더 낮은 타입은 TalkativeButton을 참조하지 못하게 한다.    


* 어떤 클래스의 기반 타입 목록에 들어있는 타입이나 제네릭 클래스의 타입 파라미터에 들어있는 타입의 가시성은 그 클래스 자신의 가시성과 같거나 높아야 한다.


* 메서드의 시그니처에 사용된 모든 타입의 가시성은 그 메서드의 가시성과 같거나 더 높아야 한다.


위 규칙은 어떤 함수를 호출하거나 어떤 클래스를 확장할 때 필요한 모든 타입에 접근할 수 있게 보장해준다.  


<br />

#### 컴파일 오류 없애는 방법 
* giveSpeech 확장 함수의 가시성을 internal로 바꾸기 
  
* TalkativeButton 클래스의 가시성을 public 으로 바꾸기 

<br />

#### 코틀린의 protected 
* 자바에서는 같은 패키지 안에서 protected 멤버에 접근할 수 있지만, 코틀린에서는 그렇지 않다는 점에서     
자바와 코틀린의 protected가 다르다는 사실에 유의해야 한다.   

* protected 멤버는 오직 어떤 클래스나 그 클래스를 상속한 클래스 안에서만 보인다. 

<br />


> **코틀린의 가시성 변경자와 자바**   
> 코틀린의 public, protected, private 변경자는 컴파일된 자바 바이트코드 안에서도 그대로 유지된다.    
> 유일한 예외는 private 이다.   
> 자바에서 클래스를 private으로 만들 수 없으므로 내부적으로 코틀린은 private 클래스를 package-private 클래스로 컴파일한다.   
> 
> 그렇다면 internal 변경자는 어떻게 처리될까?  
> package-private 가시성은 internal 과는 전혀 다르다.     
> 모듈은 보통 여러 패키지로 이뤄지며 서로 다른 모듈에 같은 패키지에 속한 선언이 들어있을 수도 있다.   
> 따라서 internal 변경자는 바이트코드상에서는 public 이 된다.   


<br /> 

코틀린과 자바 가시성 규칙의 또 다른 차이는 코틀린에서는 외부 클래스가 내부 클래스나 중첩된 클래스의 private 멤버에 접근할 수 없다는 점이다.   

<br />
<br />

### 4.1.4 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스 
코틀린의 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다는 점이다.   
<br />

#### 예제: View 요소를 하나 만들어보자. 
* View 상태를 직렬화해야 한다. 

* 직렬화할 수 있는 상태가 있는 뷰 선언 

```kotlin
interface State: Serializable

interface View {
    fun getCurrentState() : State
    fun restoreState(state: State) {}
}
```

* 자바에서 내부 클래스를 사용해 View 구현하기 


```kotlin
/* 자바 */
public class Button implements View {
    @Override 
    public State getCurrentState() {
        return new ButtonState();
    }

    @Override
    public void restoreStore(State state) { /*...*/ }

    public class ButtonState implements State { /*...*/ }
} 
```


State 인터페이스를 구현한 ButtonState 클래스를 정의해서 Button에 대한 구체적인 정보를 저장한다.   
getCurrentState 메서드 안에서는 ButtonState의 새 인스턴스를 만든다.   
실제로는 ButtonState 안에 필요한 모든 정보를 추가해야 한다.   

위에서 선언한 버튼의 상태를 직렬화하면 다음과 같은 오류가 발생한다.   

```
java.io.NotSeializableException: Button
```

자바에서 다른 클래스 안에 정의한 클래스는 자동으로 내부 클래스가 된다.   
이 예제의 ButtonState 클래스는 바깥쪽 Button 클래스에 대한 참조를 묵시적으로 포함한다.   
그 참조로 인해 ButtonState를 직렬화할 수 없다.   
Button을 직렬화할 수 없으므로 버튼에 대한 참조가 ButtonState의 직렬화를 방해한다.      
<br />                 
 
* 중첩 클래스를 사용해 코틀린에서 View 구현 

```kotlin 
class Button : View {
    override fun getCurrentState() : State = ButtonState()

    override fun restoreState(state: State) { /*...*/ }

    class ButtonState : State { /*...*/ } // 이 클래스는 자바의 정적 중첩 클래스와 대응한다. 
}
```



코틀린 중첩 클래스에 아무런 변경자가 붙지 않으면 자바 static 중첩 클래스와 같다.   
이를 내부 클래스로 변경해서 바깥쪽 클래스에 대한 참조를 포함하게 만들고 싶다면 `inner` 변경자를 붙여야 한다. 
<br />            

|클래스 B 안에 정의된 클래스 A|자바에서는|코틀린에서는|
|:---|:---|:---|
|중첩 클래스(바깥쪽 클래스에 대한 참조를 저장하지 않음)|`static class A`|`class A`|
|내부 클래스(바깥쪽 클래스에 대한 참조를 저장함)|`class A`|`inner class A`|


<br />
 
* 내부 클래스 Inner 안에서 바깥쪽 클래스 Outer의 참조에 접근하려면 `this@Outer` 라고 써야 한다. 


```kotlin 
class Outer {
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer 
    }

}
```


<br />
<br />

### 4.1.5 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한  

#### 인터페이스 구현을 통해 식 표현 


```kotlin 
interface Expr

class Num(val value: Int) : Expr

class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr) : Int = 
    when (e) {
        is Num -> e.value 
        is Sum -> eval(e.right) + eval(e.left)
        else -> 
            throw IllegalArgumentException("Unknown expression")
    }
```


* 코틀린은 when 사용 시 항상 디폴트 분기인 else 분기를 덧붙여야 한다.   

* 디폴트 분기가 있으면 새로운 하위 클래스를 추가하더라도 컴파일러가 when이 모든 경우를 처리하는지 제대로 검사할 수 없다.   

* 실수로 새로운 클래스 처리를 잊어버렸더라도 디폴트 분기가 선택되기 때문에 심각한 버그가 발생할 수도 있다.   


<br />

#### sealed 클래스로 식 표현하기 

```kotlin 
sealed class Expr {
    class Num(val value: Int) : Expr() // 기반 클래스를 sealed로 봉인한다. 
 
    class Sum(val left: Expr, val right: Expr) : Expr() // 기반 클래스의 모든 하위 클래스를 중첩 클래스로 나열한다. 
}

/*
when 식이 모든 하위 클래스를 검사하므로 
별도의 else 분기가 없어도 된다. 
*/
fun eval(e: Expr) : Int = 
    when (e) {
        is Expr.Num -> e.value 
        is Expr.Sum -> eval(e.right) + eval(e.left)
    }
```


* when 식에서 sealed 클래스의 모든 하위 클래스를 처리한다면 디폴트 분기가 필요 없다.   

* sealed로 표시된 클래스는 자동으로 open 이다. 

* 나중에 sealed 클래스의 상속 계층에 새로운 하위 클래스를 추가해도 when 식이 컴파일되지 않는다.       
  즉 when 식을 고쳐야 한다는 사실을 쉽게 알 수 있다.   



* 내부적으로 Expr 클래스는 private 생성자를 가진다.      
👉🏻 그 생성자는 클래스 내부에서만 호출 가능     
👉🏻 sealed 인터페이스 정의 못함.       
👉🏻 봉인된 인터페이스를 만들 수 있다면 코틀린 컴파일러에게 그 인터페이스를 자바 쪽에서 구현하지 못하게 막을 수 있는 수단이 없다.   

<br />

> 코틀린 1.5 부터 봉인된 클래스가 정의된 패키지 안의 아무 위치에 선언할 수 있게 됐고,   
> 봉인된 인터페이스도 추가되었다. 


<br />
<br />

## 4.2 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언 

* 코틀린은 주 생성자와 부 생성자를 구분한다.       
    - 주 생성자: 클래스를 초기화할 때 주로 사용, 클래스 본문 밖에 정의 
    - 부 생성자: 클래스 본문 안에서 정의

* 코틀린에서는 초기화 블록을 통해 초기화 로직을 추가할 수 있다.   

<br />

### 4.2.1 클래스 초기화: 주 생성자와 초기화 블록 

```kotlin 
class User(val nickname: String)
```

클래스 이름 뒤에 오는 괄호로 둘러싸인 코드를 주 생성자라고 부른다. 

<br />            


```kotlin 
class User constructor(_nickname: String) { // 파라미터 하나만 있는 주 생성자
    val nickname: String

    // 초기화 블록
    init {
        nickname = _nickname
    }
}
```

<br />


* `constructor` : 주 생성자나 부 생성자 정의를 시작할 때 사용
  주 생성자 앞에 별다른 어노테이션이나 가시성 변경자가 없다면 생략 가능   

* `init` : 초기화 블록을 시작할 때 사용
 

* `_` : 프로퍼티와 생성자 파라미터를 구분함.  


```kotlin 
class User(_nickname: String) {
    val nickname = _nickname
}
```

<br />


주 생성자의 파라미터로 프로퍼티를 초기화한다면 그 주 생성자 파라미터 이름 앞에 val을 추가하는 방식으로 프로퍼티 정의와 초기화를 쓸 수 있다. 


```kotlin 
class User(val nickname: String) // val은 이 파라미터에 상응하는 프로퍼티가 생성된다는 뜻이다. 
```


<br />

함수 파라미터와 마찬가지로 생성자 파라미터에도 디폴트 값을 정의할 수 있다. 

```kotlin 
// 생성자 파라미터에 대한 디폴트 값을 제공 
class User(val nickname; String, val isSubscribed: boolean = true)
```

<br />

클래스의 인스턴스를 만들려면 new 키워드 없이 생성자를 직접 호출하면 된다.   

```kotlin 
// ex. 디폴트 값 사용 
val hyerin = User("혜린")

// ex. 생성자 인자 중 일부에 대해 이름 지정 가능 
val hyerin = User("혜린", isSubscribed = false)

// ex. 모든 인자를 파라미터 선언 순서대로 지정 가능 
val hyerin = User("혜린", false)
```

<br />

클래스에 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야 할 필요가 있다.   
기반 클래스를 초기화하려면 기반 클래스 이름 뒤에 괄호를 치고 생성자 인자를 넘긴다.   

```kotlin 
open class User(val nickname: String) { ... }
class TwitterUser(nickname: String) : User(nickname) { ... }
```

<br />

클래스를 정의할 때 별도로 생성자를 정의하지 않으면 컴파일러가 자동으로 아무 일도 하지 않는, 인자가 없는 디폳트 생서자를 만들어준다.   

```kotlin 
open class Button // 인자가 없는 디폴트 생성자가 만들어진다. 
```

<br />

* Button의 생성자는 아무 인자도 받지 않지만, Button 클래스를 상속한 하위 클래스는 반드시 Button 클래스의 생성자를 호출해야 한다.  

* 위 규칙으로 인해 기반 클래스의 이름 뒤에 꼭 빈 괄호가 들어간다. 

* 인터페이스는 생성자가 없기 때문에 어떤 클래스가 인터페이스를 구현하는 경우 인터페이스 이름 뒤에는 아무 괄호도 없다. 
  
* 이름 뒤 괄호가 있는지에 따라 기반 클래스와 인터페이스를 구별할 수 있다.

```kotlin 
class RadioButton : Button()
```

<br />

Secretive 클래스 안에 주 생성자밖에 없고 그 주 생성자는 비공개이므로 외부에서는 Secretive를 인스턴스화할 수 없다.   


```kotlin
class Secretive private constructor() {} // 이 클래스의 주 생성자는 비공개이다.
```

<br />

> **비공개 생성자에 대한 대안**   
> 유틸리티 함수 제공 클래스, 싱글턴 등의 경우 자바에서는 private 생성자를 정의해서 다른 곳에서의 인스턴스화를 막는다.   
> 코틀린은 이런 경우 언에서 기본 지원한다.   
> 정적 유틸리티 함수 대신 최상위 함수를 사용할 수 있고 싱글턴을 사용하고 싶으면 객체를 선언하면 된다.   


<br />
<br />

### 4.2.2 부 생성자: 상위 클래스를 다른 방식으로 초기화        
자바에서 오버로드한 생성자가 필요한 상황 중 상당수는 코틀린의 디폴트 파라미터 값과 이름 붙인 인자 문법을 사용해 해결할 수 있다.   

<br /> 

#### 🍿 Tip!  
인자에 대한 디폴트 값을 제공하기 위해 부 생성자를 여럿 만들지 말라.   
대신 파라미터의 디폴트 값을 생성자 시그니처에 직접 명시하라.   

<br /> 

프레임워크 클래스를 확장해야 하는데 여러 가지 방법으로 인스턴스를 초기화할 수 있게 다양한 생성자를 지원해야 하는 경우가 있다.   



#### 자바에서 선언된 생성자가 2개인 View 클래스 

* 부 생성자는 `constructor` 키워드로 시작하고 필요에 따라 많이 선언해도 된다.   

* 부 생성자는 `super()` 키워드를 통해 자신에 대응하는 상위 클래스 생성자를 호출한다.   


```kotlin 
/* 부 생성자들 */
open class View {
    constructor(ctx: Context) {
        // code
    }

    constructor(ctx: Context, attr: AttributeSet) {
        // code 
    }
}

/* 상위 클래스의 생성자를 호출 */
class MyButton : View {
    constructor(ctx: Context)  
         : super(ctx) {
            // ...
         }

    constructor(ctx: Context, attr: AttributeSet)
        : super(ctx, attr) {
            // ...
        }
}
```

<img width="646" alt="스크린샷 2023-01-08 오후 10 09 01" src="https://user-images.githubusercontent.com/33855307/211197739-5a80cd50-a0a9-49af-abc8-bfe5a2cb5f39.png">

<br /> 

자바처럼 `this()` 를 통해 클래스 자신의 다른 생성자를 호출할 수 있다. 

```kotlin 
class MyButton : View {
    constructor(ctx: Context): this(ctx, MY_STYLE) { // 이 클래스의 다른 생성자에게 위임한다.
        // ...
    } 
    
    constructor(ctx: Context, attr: AttributeSet): super(ctx, attr) {
        // ...
    }
}
```


<img width="638" alt="스크린샷 2023-01-08 오후 10 09 22" src="https://user-images.githubusercontent.com/33855307/211197741-815b1dfc-cc27-4d2e-85d0-905b559d7942.png">


* 클래스에 주 생성자가 없다면 모든 부 생성자는 반드시 상위 클래스를 초기화하거나 다른 생성자에게 생성을 위힘해야 한다.   

* 부 생성자가 필요한 주된 이유 
     - 자바 상호운용 
     - 클래스 인스턴스를 생성할 때 파라미터 목록이 다른 생성 방법이 여러 개 존재하는 경우 부 생성자를 여럿 둘 수 밖에 없다. 


<br />
<br />

### 4.2.3 인터페이스에 선언된 프로퍼티 구현 
코틀린에서는 인터페이스 추상 프로퍼티 선언을 넣을 수 있다.   

```kotlin 
interface User {
    val nickname: String
}
```

* User 인터페이스를 구현하는 클래스가 nickname의 값을 얻을 수 있는 방법을 제공해야 한다.  

* 인터페이스에 있는 프로퍼티 선언에는 Backing Field나 getter 등의 정보가 없다.   

* 상태를 저장할 필요가 있다면 인터페이스를 구현한 하위 클래스에서 상태 저장을 위한 프로퍼티 등을 만들어야 한다. 

<br />

#### 인터페이스의 프로퍼티 구현 

```kotlin 
// 주 생성자에 있는 프로퍼티
class PrivateUser(override val nickname: String) : User 

// 커스텀 getter
class Subscribing(val email: String) : User {
    override val nickname: String
        get() = email.substringBefore('@') 
}

// 프로퍼티 초기화 식
class FacebookUser(val accountId: Int) : User {
    override val nickname = getFacebookName(accountId) 
}
```

* PrivateUser는 주 생성자 안에 프로퍼티를 직접 선언, User의 추상 프로퍼티를 구현하므로 override를 표시해야 한다.   
  
* Subscribing는 커스텀 getter로 프로퍼티 설정, 필드에 값을 저장하지 않고 매번 로직을 통해 값을 반환 

* FacebookUser는 초기화 식, getFacebookName은 페이스북에 접속해 인증을 거친 후 데이터를 가져오기 때문에 비용이 많이들 수 있음. 
  때문에 객체를 초기화하는 단계에 한 번만 getFacebookName을 호출하게 설계함

<br />

#### 인터페이스의 getter/setter

인터페이스에는 추상 프로퍼티뿐 아니라 getter/setter가 있는 프로퍼티를 선언할 수도 있다.    
단, getter/setter는 필드를 참조할 수는 없다. 

```kotlin 
interface User {
    val email: String 

    /*
    프로퍼티에 Backing Field 필드가 없다. 
    대신 매번 결과를 계산해 돌려준다. 
    */
    val nickname: String
        get() = email.substringBefore('@') 
}
```

* 하위 클래스는 추상 프로퍼티인 email을 반드시 오버라이드해야 한다.   

* 반면 nickname은 오버라이드하지 않고 상속할 수 있다.   


인터페이스에 선언된 프로퍼티와 달리 클래스에 구현된 프로퍼티는 Backing Field를 원하는 대로 사용할 수 있다.   

<br />
<br />

### 4.2.4 getter/setter에서 Backing Field에 접근 
* 값을 저장하는 프로퍼티 
* 커스텀 접근자에서 매번 값을 계산하는 프로퍼티
 
값을 저장하는 동시에 로직을 실행할 수 있게 하기 위해서는 접근자 안에서 프로퍼티를 Backing Field에 접근할 수 있어야 한다. 

<br />

#### setter에서 Backing Field 접근 

```kotlin 
class User(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was changed for $name:
                "$field" -> "$value".""".trimIndent()) // Backing Field 값 읽기 
            field = value // Backing Field 값 변경
        }
}
```


* 코틀린 프로퍼티 값을 바꿀 때 `user.address = "new value"` 처럼 필드 설정 구문을 사용하면 내부적으로 address의 setter를 호출한다. 

* 접근자의 본문에서는 field 라는 식별자를 통해 Backing Field에 접근할 수 있다.   
  
* 변경 가능 프로퍼티의 getter/setter 중 하나만 직접 정의해도 된다.   

<br />


#### Backing Field 가 있는 프로퍼티와 없는 프로퍼티의 차이점은?
* 클래스의 프로퍼티를 사용하는 쪽에서 프로퍼티를 읽는 방법, 쓰는 방법은 Backing Field의 유무와 관계가 없다.


* 컴파일러는 디폴트, 커스텀 관계없이 field를 사용하는 프로퍼티에 Backing Field를 생성해준다. 


* 단, field를 사용하지 않는 커스텀 접근자 구현을 정의하면 Backing Field는 존재하지 않는다. 
    - val인 경우 getter에 field가 없으면 된다. 
    - var인 경우 getter/setter 모두 field가 없어야 한다. 

<br />
<br />

### 4.2.5 접근자의 가시성 변경 
접근자의 가시성은 기본적으로 프로퍼티의 가시성과 같다.    


```kotlin 
class LengthCounter {
    var counter: Int = 0
        private set
    
    fun addWord(word: String) {
        counter += word.length
    }
}
```


* 클래스의 내부에서 길이를 변경하도록 만들고 싶다. 
  
* 기본 가시성을 가진 getter를 컴파일러가 생성하게 내버려 두는 대신 setter의 가시성을 private으로 지정한다. 

<br />

```kotlin 
val lengthCounter = LengthCounter()
lengthCounter.addWord("Hi!")
println(lengthCounter.counter) // 3
```

<br />

> 프로퍼티에 대해서 나중에 다룰 내용   
> * lateinit 변경자를 not null 프로퍼티에 지정하면 프로퍼티를 생성자가 호출된 다음에 초기화한다는 뜻이다.     
>   일부 프레임워크에서는 이런 특성이 꼭 필요하다.   
>
> * 요청이 들어오면 비로소 초기화되는 지연 초기화 프로퍼티는 더 일반적인 위임 프로퍼티의 일종이다.  
>
> * 자바 프레임워크와의 호환성을 위해 자바의 특징을 코틀린에서 에뮬레이션하는 어노테이션을 활용할 수 있다.     
>   예) `@JvmField` 어노테이션을 프로퍼티에 붙이면 접근자가 없는 public 필드를 노출시켜준다.     
>   const 변경자를 사용하면 어노테이션을 더 편리하게 다룰 수 있고,  
>   원시 타입이나 String 타입인 값을 어노테이션의 인자로 활용할 수 있다.   

<br />
<br />

## 4.3 컴파일러가 생성한 메서드: 데이터 클래스와 클래스 위임 
### 4.3.1 모든 클래스가 정의해야 하는 메서드 

```kotlin 
class Client(val name: String, val postalCode: Int)
```

위 클래스의 인스턴스를 어떻게 문자열로 표현할 수 있을까?  

<br />

#### 문자열 표현: `toString()`
기본 제공되는 객체의 문자열 표현은 Client@5e9fjs34 같은 방식인데, 이는 유용하지 않다.    
기본 구현을 바꾸려면 toString 메서드를 오버라이드해야 한다.   

```kotlin 
class Client(val name: String, val postalCode: Int) {
    override fun toString() = "Client(name=$name, postalCode=$postalCode)"
}
```

<br />

#### 객체의 동등성: `equals()`
* 코틀린에서 `==` 연산자는 참조 동일성을 검사하지 않고 객체의 도등성을 검사한다.   
  따라서 `==` 연산은 equals를 호출하는 식으로 컴파일된다. 

<br />

> 동등성 연산에서 `==` 를 사용하면?
> - 자바
>   * 원시 타입: 값이 같은지 비교 
>   * 참조 타입: 주소가 같은지 비교 
>
> - 코틀린
>   * `==`: 내부적으로 equals를 호출해서 비교한다. 
>     즉 equals 를 오버라이드하면 안전하게 비교할 수 있다.  
>   * `===`: 자바에서 참조 타입을 비교할 때 사용하는 `==` 과 같다.  

<br />

```kotlin 
class Client(val name: String, val postalCode: Int) {
    override fun equals(other: Any?): Boolean {
        if(other == null || other !is Client)
            return false
        return name == other.name &&
            postalCode == other.postalCode
    }

    ...

}
```

* `Any?` 는 java.lang.Object에 대응하는 클래스이다.      
  코틀린의 모든 클래스의 최상위 클래스로 "other"는 null일 수 있다. 


* 코틀린의 `is` 는 자바의 `instanceof` 와 같다. 


<br />

equals를 오버라이드 하고 나서 더 복잡한 작업을 수행하다보면 제대로 작동하지 않을 때가 있다.   
이때 hashCode 정의를 빠뜨려서 그렇다고 생각할 수 있는데,    
이 예제에서는 실제 hashCode가 없다는 점이 원인이다.   

<br />

#### 해시 컨테이너: `hashCode()`
* JVM 언어에서는 hashCode가 지켜야 하는     
'equals()가 true를 반환하는 두 객체는 반드시 같은 hashCode()를 반환해야 한다.'     
라는 제약이 있는데 Client는 이를 어기고 있다.    


* processed 집합은 HashSet이다.   

* HashSet은 원소를 비교할 때 비용을 줄이기 위해 먼저 객체의 해시 코드를 비교하고 해시 코드가 같은 경우에만 실제 값을 비교한다.   

* 즉, 원소 객체들이 코드에 대한 규칙을 지키지 않는 경우 HashSet은 제대로 작동할 수 없다.   


<br />

```kotlin 
override fun hashCode(): Int = name.hashCode() * 31 + postalCode
```


<br />
<br />

### 4.3.2 데이터 클래스: 모든 클래스가 정의해야 하는 메서드 자동 생성 
* `data` 변경자를 클래스 앞에 붙이면 필요한 메서드를 컴파일러가 자동으로 만들어준다.    
  - equals
  - hashCode
  - toString 

<br />

#### 데이터 클래스의 불변성: `copy()` 메서드
* 데이터 클래스의 프로퍼티가 꼭 val 일 필요는 없다. var 프로퍼티 사용 가능     
* 하지만 데이터 클래스의 모든 프로퍼티를 읽기 전용으로 만들어서 데이터 클래스를 불변으로 만들라고 권장한다.   
* HashMap 등의 컨테이너에 데이터 클래스 객체를 담는 경우엔 불변성이 필수적이다.  
  why? 데이터 클래스 객체를 key로 담았는데 key 데이터 객체의 프로퍼티를 변경하면 컨테이너 상태가 잘못될 수 있다.   
* 불변 객체를 주로 사용하는 프로그램 > 스레드가 사용 중인 데이터를 다른 스레드가 변경 x > 스레드 동기화할 필요가 줄어든다. 

<br />  

코틀린은 데이터 클래스 인스턴스를 불변 객체로 더 쉽게 활용할 수 있게 copy 메서드를 제공한다.  
* 객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해주는 copy 메서드이다.         
* 객체를 메모리상에서 직접 바꾸는 대신 복사본을 만드는 편이 더 낫다.         
* 복사본은 원본과 다른 생명주기를 가지며, 복사를 하면서 일부 프로퍼티 값을 바꾸거나 복사본을 제거해도            
  프로그램에서 원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않는다.

<br />
<br />

### 4.3.3 클래스 위임: `by` 키워드 사용 
대규모 객체지향 시스템을 설계할 때 시스템을 취약하게 만드는 문제는 보통 구현 상속에 의해 발생한다. (implementation inheritance)

상위 클래스의 구현이 바뀌거나 새로운 메서드가 추가되면 하위 클래스에 영향을 미칠 수 있다.   

이런 문제를 인식하고 코틀린에서 기본적으로 클래스를 `final`로 취급하기로 했다.

* 모든 클래스를 기본적으로 final로 취급, 상속을 염두에 두고 open 변경자로 열어둔 클래스만 확장  

* but 종종 상속을 허용하지 않는 클래스에 새로운 동작을 추가해야 할 때가 있다.   

* 이럴 때 사용하는 일반적인 방법이 데코레이터 패턴이다.
    - 상속을 허용하지 않는 클래스(기존 클래스) 대신 사용할 수 있는 새로운 클래스를 만들되 기존 클래스와 같은 인터페이스를 데코레이터가 제공하게 만들고,   
      기존 클래스를 데코레이터 내부에 필드로 유지하는 것이다. 
    - 이때 데코레이터 패턴의 메서드에 새로 정의하고 (기존 클래스의 메서드나 필드를 활용) 기존 기능이 그대로 필요한 부분은   
      데코레이터의 메서드가 기존 클래스의 메서드에게 요청을 전달(forwarding)한다.   
  
* 위 접근 방법의 단점은 준비 코드가 상당히 많이 필요하다는 점이다.   
    - 예) Collection 같이 비교적 단순한 인터페이스를 구현하면서 아무 동작도 변경하지 않는 데코레이터를 만들 때조차 복잡한 코드를 작성해야 한다. 


#### Collection 데코레이터 

```kotlin
class DelegatingCollection<T> : Collection<T> {
  private val innerList = arrayListOf<T>()
  
  override val size: Int get() = innerList.size
  override fun isEmpty(): Boolean = innerList.isEmpty()
  ...
}
```


* 이런 위임을 언어가 제공하는 일급 시민 기능으로 지원한다는 점이 코틀린의 장점이다.
* 인터페이스를 구현할 때 `by` 키워드를 통해 그 인터페이스에 대한 구현을 다른 객테에 위임 중이라는 사실을 명시할 수 있다. 

#### 위임을 사용한 Collection 데코레이터 
```kotlin
class DelefatingCollection<T> (
  innerList: Collection<T> = ArrayList<T>()
) : Collection<T> by innerList {}
```

* 클래스 안에 있던 모든 메서드를 컴파일러가 자동으로 생성한다. 
* 메서드 중 일부의 동작을 변경하고 싶은 경우 메서드를 오버라이드하면 컴파일러가 생성한 메서드 대신 오버라이드한 메서드가 쓰인다.   

<br />
<br />

## 4.4 `object` 키워드: 클래스 선언과 인스턴스 생성 
코틀린에서는 `object` 키워드는 모든 경우 클래스를 정의하면서 동시에 인스턴스(객체)를 생성한다는 공통점이 있다.   

* 객체 선언(object declaration): 싱글턴을 정의하는 방법 중 하나다.       


* 동반 객체(companion object): 인스턴스 메서드는 아니지만 어떤 클래스와 관련 있는 메서드와 팩토리 메서드를 담을 때 쓰인다.     
  동반 객체 메서드에 접근할 때는 동반 객체가 포함된 클래스의 이름을 사용할 수 있다.   
 

* 객체 식은 자바의 익명 내부 클래스(anonymous inner class) 대신 쓰인다.       

<br />
<br />

### 4.4.1 객체 선언: 싱글턴을 쉽게 만들기 
자바에서는 보통 클래스의 생성자를 private으로 제한하고 정적인 필드에 그 클래스의 유일한 객체를 저장하는 싱클턴 패턴을 통해 이를 구현한다.   

코틀린은 객체 선언 기능을 통해 싱글턴을 언어에서 기본 지원한다.   

객체 선언은 클래스 선언과 그 클래스에 속한 단일 인스턴스의 선언을 합친 선언이다.   

예) 객체 선언을 사용한 회사 급여 대장 - 한 회사에 여러 대장이 필요하지 않아 싱글턴을 쓰는 게 맞아 보인다.  

```kotlin
object Payroll {
  val allEmployee = arrayListOf<Person>()
  
  fun calculateSalary() {
    for (person in allEmployee) {
      ...
    }
  }
}
```


* 객체 선언은 object 키워드로 시작한다.

* 클래스와 마찬가지로 객체 선언 안에도 프로퍼티, 메서드, 초기화 블록 등이 들어갈 수 있다.
    - but 생성자는 객체 선언에 쓸 수 없다. 
    - 일반 클래스 인스턴스와 달리 싱글턴 객체는 객체 선언문이 있는 위치에서 생성자 호출 없이 즉시 만들어진다. 
    - 따라서 객체 선언에는 생성자 정의가 필요 없다. 
  
* 변수와 마찬가지로 객체 선언에 사용한 이름 뒤에 마침표(`.`)를 붙이면 객체에 속한 메서드나 프로퍼티에 접근할 수 있다.

```kotlin
Payroll.allEmployee.add(Person(...))
Payroll.calculateSalary()
```

* 객체 선언도 클래스나 인터페이스를 상속할 수 있다.
    - 프레임워크 사용을 위해 인터페이스를 구현해야 하는데, 구현 내부에 다른 상태가 필요하지 않은 경우 유용한 기능이다.  
    - 예) java.util.Comparator 인터페이스 
    - Comparator 안에는 데이터를 저장할 필요가 없다. 따라서 Comparator 인스턴스를 만드는 방법으로 객체 선언이 가장 좋은 방법이다. 

<br />  

#### 객체 선언을 사용해 Comparator 구현하기 
```kotlin
object CaseInsentiveFileComparator : Comparator<File> {
  override fun compre(file1: FIle, file2: File) : Int {
    return file1.path.compareTo(file2.path, ignoreCase = true)
  }
}

>>> println(CaseInsentiveFileComparator.compare(
...   File("/User"), File("/user")))
0
```

일반 객체(클래스 인스턴스)를 사용할 수 있는 곳에서는 항상 싱글턴 객체를 사용할 수 있다. 
예) Comparator 를 인자로 받는 함수에게 인자로 넘길 수 있다. 

```kotlin
>>> val files = listOf(File("/Z"), File("/a"))
>>> println(files.sortedWith(CaseInsentiveFileComparator))
[/a, /Z]
```

전달받은 Comparator에 따라 리스트를 정렬하는 sortedWith 함수를 사용한다. 


> **싱글톤과 의존관계 주입**    
> 싱글톤 패턴과 마찬가지 이유로 대규모 소프트웨어 시스템에서는 객체 선언이 항상 적합하지는 않다.           
> * 대규모 컴포넌트에서는 다양한 구성 요소와 상호작용이 있다.   
>     - 싱글톤이 적합하지 않다.
>     - 객체 생성을 제어할 수 없고 생성자 파라미터를 지정할 수 없기 때문이다.
> * 생성 제어 x, 생성자 파라미터 지정 x 
>     - 단위 테스트가 어렵다.
>     - 설정이 달라질 때 객체를 대체하거나 객체의 의존관계를 바꿀 수 없다.
>
> 따라서 그런 기능이 필요하다면 의존 관계 주입 프레임워크와 코틀린 클래스를 함께 사용해야 한다.   

<br />

클래스 안에서 객체를 선언할 수도 있다.   
그런 객체도 인스턴스는 단 하나뿐이다. (바깥 클래스의 인스턴스마다 중첩 객체 선언에 해당하는 인스턴스가 하나씩 따로 생기는 것이 아니다.)   

#### 중첩 객체를 사용해 Comparator 구현하기 
```kotlin
data class Person(val name: String) {
  object NameComparator : Comparator<Person> {
    override fun compare(p1: Person, p2: Person) : Int = 
      p1.name.compareTo(p2.name)
  } 
}

>>> val persons = listOf(Person("Bob"), Person("Alice"))
>>> println(persons.sortedWith(Person.NameComparator))
[Person(name=Alice), Person(name=Bob)]
```

<br />

> **코틀린 객체를 자바에서 사용하기**      
> 코틀린 객체 선언은 유일한 인스턴스에 대한 정적인 필드가 있는 자바 클래스로 컴파일된다.   
> 이때 인스턴스 필드의 이름은 항상 `INSTANCE`다.   
> 싱글톤 패턴을 자바에서 구현해도 비슷한 필드가 필요하다.   
> 자바 코드에서 코틀린 싱글톤 객체를 사용하려면 정적인 `INSTANCE` 필드를 통하면 된다.   
> 
> ```java
> /* 자바 */
> caseInsensitiveFileComparator.INSTANCE.compare(file1, file2);
> ```
> 
> 이 예제에서 `INSTANCE` 필드의 타입은 `caseInsensitiveFileComparator` 이다. 


<br />
<br />

### 4.4.2 동반 객체: 팩토리 메서드와 정적 멤버가 들어갈 장소  
* 코틀린 클래스 안에는 정적인 멤버가 없다. 
    - 코틀린 언어는 자바 `static` 키워드를 지원하지 않는다. 


* 코틀린에서는 패키지 수준의 최상위 함수와 객체 선언을 활용한다. 
    - 최상위 함수: 자바의 정적 메서드 역할을 거의 대신 할 수 있다. 
    - 객체 선언: 자바의 정적 메서드 역할 중 코틀린 최상위 함수가 대신할 수 없는 역할이나 정적 필드를 대신할 수 있다.  


* 대부분의 경우 최상위 함수를 활용하는 편을 더 권장한다.   
But! 최상위 함수는 private 으로 표시된 클래스 비공개 멤버에 접근할 수 없다.   
그래서 클래스의 인스턴스와 관계없이 호출해야 하지만, 클래스 내부 정보에 접근해야 하는 함수가 필요할 때는   
클래스에 중첩된 객체 선언의 멤버 함수로 정의해야 한다.  

<br />

<img width="486" alt="img3" src="https://user-images.githubusercontent.com/33855307/211196847-227b3c88-f54d-4d99-81b9-776a77d3fbb4.png">

클래스 밖에 있는 최상위 함수는 비공개 멤버를 사용할 수 없다. ⤴      

<br />

클래스 안에 정의된 객체 중 하나에 `companion` 이라는 특별한 표시를 붙이면 그 클래스의 동받 객체로 만들 수 있다.    

* 동반 객체의 프로퍼티나 메서드에 접근하려면 그 동받 객체가 정의된 클래스 이름을 사용한다. 
* 객체의 이름을 따로 지정할 필요가 없다.  
* 동반 객체의 멤버를 사용하는 구문은 자바의 정적 메서드 호출이나 정적 필드 사용 구문과 같아진다.   

```kotlin
class A {
  companion object {
    fun bar() {
      println("Companion object called")
    }
  }
}

>>> A.bar()
Companion object called
```


동반 객체는 `private` 생성자를 호출하기 좋은 위치이다.    

* 동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근할 수 있다. 
* 동반 객체는 바깥쪽 클래스의 private 생성자도 호출할 수 있다. 
* 동반 객체는 팩토리 패턴을 구현하기 가장 적합한 위치다.   

<br />

#### 부 생성자가 여럿 있는 클래스 정의하기 
```kotlin  
class User {
  val nickname: String 
  
  constructor(email: String) { // 부 생성자 
    nickname = email.substringBefore('@)
  }
  
  constructor(facebookAccountId: Int) { // 부 생성자 
    nickname = getFacebookName(facebookAccountId)
  }
  
}
```

이런 로직을 표현하는 더 유용한 방법으로 클래스의 인스턴스를 생성하는 팩토리 메서드가 있다.   

<br />  

#### 부 생성자를 팩토리 메서드로 대신하기
```kotlin 
class User private constructor(val nickname: String) { // 주 생성자를 비공개로 만든다. 
  companion object { // 동반 객체를 선언한다. 
    fun newSubscribingUser(email: String) = 
      User(email.substringBefore('@'))
    fun newFacebookUser(accountId: Int) = // 페이스북 사용자 ID로 사용자를 만드는 팩토리 메서드 
      User(getFacebookName(accountId))
  }
}
```

클래스 이름을 사용해 그 클래스에 속한 동반 객체의 메서드를 호출할 수 있다. 

```text 
>>> val subscribingUser = User.newSubscribingUser("bob@gmail.com")
>>> val facebookUser = User.newFacebookUser(4)
>>> println(subscribingUser.nickname)
bob 
```

* 목적에 따라 팩토리 메서드 이름을 정할 수 있다. 


* 팩토리 메서드는 그 팩토리 메서드가 선언된 클래스의 하위 클래스 객체를 반환할 수 있다.   


* 팩토리 메서드는 생성할 필요가 없는 객체를 생성하지 않을 수도 있다.   
    - 예) 이미 존재하는 인스턴스에 해당하는 이메일 주소를 전달받으면 캐시에 있는 인스턴스를 반환할 수 있다.
  

* 클래스를 확장해야만 하는 경우, 동반 객체 멤버를 하위 클래스에서 오버라이드할 수 없으므로 여러 생성자를 사용하는 편이 더 나은 해법이다.   

<br />  
<br />  

### 4.4.3 동반 객체를 일반 객체처럼 사용 
* 동반 객체는 클래스 안에 정의된 일반 객체다. 
    - 동반 객체에 이름을 붙이기 가능 
    - 인터페이스 상속 가능 
    - 동반 객체 안에 확장 함수와 프로퍼티를 정의 가능 

<br /> 

#### 예시 상황) 회사의 급여 명부를 제공하는 웹 서비스 만들기 
서비스에서 사용하지 위해 객체를 JSON으로 직렬화 or 역직렬화를 해야 한다.    
직렬화 로직을 동반 객체 안에 넣을 수 있다.

<br /> 

#### 동반 객체에 이름 붙이기 
```kotlin
class Person(val name: String) {
  companion object Loader {
    fun fromJSON(jsonText: String): Person = ... // 동반 객체에 이름을 붙인다. 
  }
}

>>> person = Person.Loader.fromJSON("{name: 'Dmitry'}") // 방법 (1)
>>> person.name
Dmitry
>>> person2 = Person.fromJSON("{name: 'Brent'}") // 방법 (2)
>>> person2.name
Brent

// 두 방법 모두 제대로 fromJSON을 호출할 수 있다.  
```

<br />

#### 동반 객체에서 인터페이스 구현 

(1) 동반 객체도 인터페이스를 구현할 수 있다.  
(2) 인터페이스를 구현하는 동반 객체를 참조할 때 객체를 둘러싼 클래스의 이름을 바로 사용할 수 있다.   


* 동반 객체에서 인터페이스 구현 
```kotlin
interface JSONFactory<T> {
  fun fromJSON(jsonText: string): T
}

class Person(val name: string) {
  companion object: JSONFactory<Person> {
    override fun fromJSON(jsonText: String): Person = ... // 동반 객체가 인터페이스를 구현 
  }
}
```

* 동반 객체가 구현한 JSONFactory의 인스턴스를 넘길 때 Person 클래스 이름을 사용한다. 

```kotlin
fun loadFromJSON<T>(factory: JSONFactory<T>): T {
  ...
}

loadFromJSON(Person) // 동반 객체의 인스턴스를 함수에 넘긴다. 
```

<br />  

> **코틀린 동반 객체와 정적 멤버**   
> 클래스의 동반 객체는 일반 객체와 비슷한 방식으로, 클래스에 정의된 인스턴스를 가리키는 정적 필드로 컴파일된다.   
> 동반 객체에 이름을 붙이지 않았다면 자바 쪽에서 Companion 이라는 이름으로 그 참조에 접근할 수 있다.   
> 
> ```
> /* 자바 */
> Person.Companion.fromJSON("...");
> ```  
> 동반 객체에 이름을 붙였다면 Companion 대신 그 이름이 쓰인다. 
> 
> 때로 자바에서 사용하기 위해 코틀린 클래스의 멤버를 정적인 멤버로 만들어야 할 필요가 있다.   
> 그런 경우 `@JvmStatic` 어노테이션을 코틀린 멤버에 붙이면 된다.   
> 정적 필드가 필요하다면 `@JvmField` 어노테이션을 최상위 프로퍼티나 객체에서 선언된 프로퍼티 앞에 붙인다.   
> 이 기능은 자바와의 상호운용성을 위해 존재하며, 정확히 말하자면 코틀린 핵심 언어가 제공하는 기능은 아니다.   
>
> 코틀린에서도 자바의 정적 필드나 메서드를 사용할 수 있다. 그런 경우 자바와 똑같은 구문을 사용한다. 

<br /> 

#### 동반 객체 확장
확장 함수를 사용해 코드 기반의 다른 곳에서 정의된 클래스의 인스턴스에 대해 새로운 메서드를 정의할 수 있다.     

그렇다면 자바의 정적 메서드나 코틀린의 동반 객체 메서드처럼 기존 클래스에 대해 호출할 수 있는 새로운 함수를 정의하고 싶다면?  
클래스에 동반 객체가 있으면 그 객체 안에 함수를 정의함으로써 클래스에 대해 호출할 수 있는 확장 함수를 만들 수 있다.   

```text
C 라는 클래스 안에 동반객체가 있고 그 동반객체 (C.companion) 안에 func를 정의하면 외부에서는 func() 를 C.func()로 호출할 수 있다.    
```

<br />


위 Person 예제에서 관심사를 좀 더 명확히 분리하려면?   
* Person 클래스는 핵심 비즈니스 로직 모듈의 일부이다.   
* 비즈니스 로직 모듈이 특정 타입에 의존하기를 원치 않는다.   
* 확장 함수를 사용하면 다음과 같이 클라이언트-서버 통신을 담당하는 모듈 안에 역직렬화 함수를 포함시킬 수 있다. 



**동반 객체에 대한 확장 함수 정의**   

```kotlin
// 비즈니스 로직 모듈 
class Person(val firstName: String, val lastName: String) {
  companion object { // 비어있는 동반 객체를 선언한다. 
  }
}

// 클라이언트-서버 통신 모듈
fun Person.Companion.fromJSON(json: String): Person { // 확장 함수 선언
  ...
} 

val p = Person.fromJSON(json)
```

동반 객체 안에서 fromJSON 함수를 정의한 것처럼 fromJSON을 호출할 수 있다.    
그러나 실제로 fromJSON은 클래스 밖에서 정의한 확장 함수다.   
여기서 동반 객체에 대한 확장 함수를 작성할 수 있으려면 원래 클래스에 동반 객체가 꼭 선언되어 있어야 한다.   
설령 빈 객체라도 동반 객체가 꼭 있어야 한다.   

<br />
<br />


### 4.4.4 객체 식: 무명 내부 클래스를 다른 방식으로 작성 
* 익명 객체를 정의할 때도 object 키워드를 쓴다.   
* 익명 객체는 자바의 익명 내부 클래스를 대신한다. 

**익명 객체로 이벤트 리스너 구현**  
```kotlin
window.addMouseListener(
  object : MouseAdapter() { // MouseAdapter를 확장하는 익명 객체를 선언한다. 
    
    // MouseAdapter의 메서드를 오버라이드 한다. 
    override fun mouseClicked(e: MouseEvent) {
      ...
    }
    // MouseAdapter의 메서드를 오버라이드 한다. 
    override fun mouseEntered(e: MouseEvent) {
      ...
    }
    
  }
)
```


객체 식은 클래스를 정의하고 그 클래스에 속한 인스턴스를 생성하지만 그 클래스나 인스턴스에 이름을 붙이지는 않는다.     
이런 경우 보통 함수를 호출하면서 인자로 무명 객체를 넘기기 때문에 클래스와 인스턴스 모두 이름이 필요하지 않다.   
<br />  

한 인터페이스만 구현하거나 한 클래스만 확장할 수 있는 자바의 익명 내부 클래스와 달리   
코틀린 익명 클래스는 여러 인터페이스를 구현하거나 클래스를 확장하면서 인터페이스를 구현할 수 있다.     
<br />  

> 객체 선언과 달리 익명 객체는 싱글턴이 아니다.   
> 객체 식이 쓰일 때마다 새로운 인스턴스가 생성된다.  

<br />

* 자바의 익명 클래스와 같이 객체 식 안의 코드는 그 식이 포함된 함수의 변수에 접근 가능 
    - 그러나 자바와 달리 final이 아닌 변수도 객체 식 안에서 사용 가능   
* 따라서 객체 식 안에서 그 변수의 값 변경 가능 
* 예) 윈도우가 호출된 횟수를 리스너에서 누적하게 구현 가능 

```kotlin
fun countClicks(window: Window) {
  var clickCount = 0 // 로컬 변수 정의 
  window.addMouseListener(object: MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) {
      clickCount++ // 로컬 변수의 값 변경 
    }
  })
  // ...
}
```


> 객체 식은 익명 객체 안에서 여러 메서드를 오버라이드해야 하는 경우에 훨씬 더 유용하다.   
> 메서드가 하나뿐인 인터페이스(Runnable 등의 인터페이스)를 구현해야 한다면 코틀린의 SAM 변환 지원을 활용하는 편이 낫다.

<br />
<br />

## 요약 
* 코틀린의 인터페이스는 자바 인터페이스와 비슷하지만 디폴트 구현을 포함 할 수 있고 (자바8부터 가능) 프로퍼티도 포함할 수 있다. (자바에서 불가능)


* 모든 코틀린 선언은 기본적으로 final이며 public이다. 


* 선언이 final이 되지 않게 만들려면(상속과 오버라이딩이 가능하게 하려면) 앞에 open을 붙여야 한다. 


* internal 선언은 같은 모듈 안에서만 볼 수 있다. 


* 중첩 클래스는 기본적으로 내부 클래스가 아니다. 
  바깥쪽 클래스에 대한 참조를 중첩 클래스 안에 포함시키려면 inner 키워드를 줄첩 클래스 선언 앞에 붙여서 내부 클래스로 만들어야 한다.   


* sealed 클래스를 상속하는 클래스를 정의하려면 반드시 부모 클래스 정의 안에 중첩 클래스로 정의해야 한다. 
    - 코틀린 1.1 부터는 같은 파일 안에만 있으면 된다.   


* 초기화 블록과 부 생성자를 활용해 클래스 인스턴스를 더 유연하게 초기화할 수 있다.   


* field 식별자를 통해 프로퍼티 접근자(getter/setter) 안에서 프로퍼티의 데이터를 저장하는 데 쓰이는 Backing Field 를 참조할 수 있다.   


* 데이터 클래스를 사용하면 컴파일러가 equals, hashCode, toString, copy 등의 메서드를 자동으로 생성해준다.   


* 클래스 위임을 사용하면 위임 패턴을 구현할 때 필요한 수많은 성가신 준비 코드를 줄일 수 있다.  


* 객체 선언을 사용하면 코틀린 답게 싱글턴 클래스를 정의할 수 있다.  


* (패키지 수준 함수와 프로퍼티 및 동반 객체와 더불어) 동반 객체는 자바의 정적 메서드와 필드 정의를 대신한다. 


* 동반 객체도 다른 (싱글턴) 객체와 마찬가지로 인터페이스를 구현할 수 있다.   
외부에서 동반 객체에 대한 확장 함수와 프로퍼티를 정의할 수 있다.  


* 코틀린의 객체 식은 자바의 익명 내부 클래스를 대신한다.     
하지만 코틀린 객체 식은 여러 인스턴스를 구현하거나 객체가 포함된 영역(scope)에 있는 변수의 값을 변경할 수 있는 등 자바 익명 내부 클래스보다 더 많은 기능을 제공한다.    



