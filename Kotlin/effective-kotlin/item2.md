# item2 변수의 스코프를 최소화하라  
상태를 정의할 때는 변수와 프로퍼티의 스코프를 최소화하는 것이 좋다.   

* 프로퍼티보다는 지역 변수를 사용하는 것이 좋다. 
* 최대한 좁은 스코플를 갖게 변수를 사용한다. 

코틀린의 스코프는 기본적으로 중괄호로 만들어지며,   
내부 스코프에서 외부 스코프에 있는 요소에만 접근할 수 있다.           
  
```kotlin
// 나쁜 예
var user: User
for (i in users.indices) {
	user = users[i]
	print("User at $i is $user")
}

// 조금 더 좋은 예
for (i in users.indices) {
	val user = users[i]
	print("User at $i is $user")
}

// 제일 좋은 예
for ((i, user) in users.withIndex()) {
	print("User at $i is $user")
}
```

<br />

#### 스코프를 좁게 만드는 것이 좋은 이유 
* 프로그램 추적, 관리가 쉽다. 
* mutable 프로퍼티는 좁은 스코프에 걸쳐 있을수록, 그 변경을 추적하는 것이 쉽다.
* 변수의 스코프가 너무 넓으면, 다른 개발자에 의해서 변수가 잘못 사용될 수 있다. 
* 변수는 읽기 전용 또는 읽고 쓰기 전용 여부와 상관 없이, 변수를 정의할 때 초기화되는 것이 좋다.
* if, when, try-catch, Elvis 표현식 등을 활용하면, 변수를 정의할 때 초기화할 수 있다.

<br />

```kotlin
//나쁜 예
val user: User
if (hasValue) {
	user = getValue()
} else {
	user = User()
}

// 조금 더 좋은 예
val user: User = if(hasValue) {
	getValue()
} else {
	User()
}

// 나쁜 예
fun updateWeather(degrees: Int) {
	val description: String
	val color: Int
	if (degrees < 5) {
		description = "cold"
		color = Color.BULE
	} else if (degrees < 23) { 
		desciption = "mild"
		color = Color.YELLOW
	} else {
		desciption = "hot"
		color = Color.RED
	}
}

// 조금 좋은 예
fun updateWeather(degrees: Int) {
	val (description, color) = when {
		degrees < 5 -> "cold" to Color.BLUE
		degrees < 23 -> "mild" to Color.YELLOW
		else -> "hot" to Color.RED
	}
}
```

<br />
<br />

## 캡처링 

시퀀스 빌드를 사용해 에라코스테네스의 체(소수 구하는 알고리즘)을 구현해보자. 

<br />

* 간단한 구현 

```kotlin

var numbers = (2..100).toList()
val primes = mutableListOf<Int>()
while (numbers.isNotEmpty()) {
	val prime = numbers.first()
	primes.add(prime)
	numbers = numbers.filter { it % prime != 0 }
}
print(primes) // [2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97]
```

<br />  

* 시퀀스 활용 

```kotlin
val primes: Sequence<Int> = sequence {
	var numbers = generateSequence(2) { it + 1 }
	
	while (true) {
		val prime = numbers.first()
		yield(prime)
		numbers = numbers.drop(1).filter { it % prime != 0 }
	}
}
print(primes.take(10).toList()) // [2,3,5,7,11,13,17,19,23,29]
```

<br />  

* 잘못된 최적화 

```kotlin
val primes: Sequence<Int> = sequence {
	var numbers = generateSequence(2) { it + 1 }
	
	var prime: Int
	while (true) {
		prime = numbers.first()
		yield(prime)
		numbers = numbers.drop(1).filter { it % prime != 0 }
	}
}
print(primes.take(10).toList()) // [2,3,5,6,7,8,9,10,11,12]
```
<br />  

이러한 결과는 prime이라는 변수를 캡처했기 때문이다.   
<br />    

* 시퀀스를 활용하므로 필터링이 지연된다. 따라서 최종적인 prime 값으로만 필터링이 된다.
* prime이 2로 설정되어 있을 때 필터링된 4를 제외하면, drop만 동작하므로 그냥 연속된 숫자가 나온다.

<br />  

항상 잠재적인 캡처 문제를 주의애햐 한다.   
가변성을 피하고 스코프 범위를 좁게 만들면, 이런 문제를 간단하게 피할 수 있다.   

