# item3 최대한 플랫폼 타입을 사용하지 말라 

만약 자바에서 String 타입을 리턴하는 메서드가 있다고 가정하자.    
코틀린에서 이를 사용하려면 어떻게 해야 할까?   

* `@Nullable` 어노테이션이 붙어 있다면,   
nullable로 추정하고 `String?` 으로 변경하면 된다.      



* `@NotNull` 어노테이션이 붙어 있다면,   
`String` 으로 변경하면 된다. 

<br />

#### 아무것도 붙어 있지 않다면? 
자바에서 모든 것이 nullable일 수 있으므로 최대한 안전하게 접근한다면, 이를 nullable로 가정하고 다루어야 한다.   
하지만 어떤 메서드는 null을 리턴하지 않을 것이 확실할 수 있다. 이런 경우 not-null 단정을 나타내는 `!!`를 붙인다.

<br />

nullable에 관련하여 자주 문제가 되는 부분은 바로 자바의 제네릭 타입이다.   
자바 API에서 `List<User>` 를 리턴하고, 어노테이션이 따로 붙어 있지 않은 경우를 생각해보자.   

```kotlin
// Java
public class UserRepo{
	public List<User> getUsers() {
		//...
	}
}

// Kotlin 
val users: List<User> = UserRepo().users!!.fillterNotNull()

// 만약 List<List<User>>를 리턴한다면
val users: List<List<User>> = UserRepo().groupedUsers!!.map { filterNotNull() } 
```


<br />

코틀린은 자바 등 다른 프로그래밍 언어에서 넘어온 타입들을 특수하게 다룬다.   
이러한 타입을 플랫폼 타입이라고 부른다.   

플랫폼 타입은 `String!` 처럼 타입 이름 뒤에 `!` 기호를 붙여 표기한다.   

```kotlin
// Java 
public class UserRepo {
	public User getUser() {
		//...
	}
}

// Kotlin 
val repo = UserRepo()
val user1 = repo.user        // user1의 타입은 User!
val user2: User = repo.user  // user2의 타입은 User
val user3: User? = repo.user // user3의 타입은 User?

val users: List<User> = UserRepo().users
val users: List<List<User>> = UserRepo().groupedUsers
```

<br />

문제는 null이 아니라고 생각되는 것이 null일 가능성이 있으므로, 여전히 위험하다는 것이다.    
때문에 설계자가 명시적으로 어노테이션으로 표시하거나, 주석으로 달아두지 않으면 언제든지 동작이 변경될 가능성이 있다.   

<br /> 

코틀린 코드도 관련된 코드를 작성할 수 있지만, 플랫폼 타입은 안전하지 않으므로, 최대한 빨리 제거하는게 좋다.    
다음 코드를 보며 그 이유를 알아보자.   



```kotlin
// Java
public class JavaClass {
	public String getValue() {
		return null;
	}
}

// Kotlin
fun statedType() {
    val value: String = JavaClass().value // NPE
    // ...
    println(value.length)
}

fun platformType() {
    val value = JavaClass().value
    // ...
    println(value.length) // NPE
}
```


* 두 가지 모두 NPE가 발생한다.


* statedType에서는 자바에서 값을 가져오는 위치에서 NPE가 발생한다. 
이 위치에서 오류가 발생하면, null이 아니라고 예상을 했지만 null이 나온다는 것을 굉장히 쉽게 알 수 있다.


* platformType에서는 값을 활용할 때 NPE가 발생한다.     
객체를 사용한다고 해서 NPE가 발생될 거라고 생각하지는 않으므로, 오류를 찾는데 굉장히 오랜 시간이 걸리게 될 것이다.  

<br />

인터페이스에서 다음과 같이 플랫폼 타입을 사용했다고 해보자.

```kotlin
interface UserRepo {
	fun getUserName() = JavaClass().value
}
```


이러한 경우 메서드의 inferred 타입(추론된 타입)이 플랫폼 타입이다.   
이는 누구나 nullalbe 여부를 지정할 수 있다는 것이다.  

<br />  

```kotlin
class RepoImpl: UserRepo {
	override fun getUserName(): String? {
		return null
	}
}

fun main() {
	val repo: UserRepo = RepoImpl()
	val text: String = repo.getUserName() // 런타임 NPE
	print("User name length is ${text.length}")
}
```

<br />

### 정리 
다른 프로그래밍 언어에서 와서 nullable 여부를 알 수 없는 타입을 플랫폼 타입이라고 부른다.   
이러한 플랫폼 타입을 사용하는 코드는 해당 부분만 위험할 뿐만 아니라, 이를 활용하는 곳까지 영향을 줄 수 있는 위험한 코드이다.  

따라서 이런 코드를 사용하고 있다면 빨리 해당 코드를 제거하는 것이 좋다.   

<br />  
