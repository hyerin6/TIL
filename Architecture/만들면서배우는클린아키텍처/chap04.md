# 유스케이스 구현하기         

애플리케이션, 웹, 영속성 계층이 현재 아키텍처에서 아주 느슨하게 결합돼 있기 때문에 필요한 대로 도메인 코드를 자유롭게 모델링할 수 있다.   
DDD, 풍부하거나 빈약한 도메인 모델 구현 등   

헥사고날 아키텍처 스타일에서 유스케이스를 구현해보자.    

헥사고날 아키텍처는 도메인 중심의 아키텍처에 적합하기 때문에 도메인 엔티티를 만드는 것으로 시작, 해당 도메인 엔티티를 중심으로 유스케이스를 구현해보자.   
예) 한 계좌에서 다른 계좌로 송금하는 유스케이스 

<br />

### 도메인 모델 구현하기   
객체지향적인 방식으로 모델링하는 한 가지 방법은 입금과 출금할 수 있는 Account 엔티티를 만들고   
출금 계좌에서 돈을 출금해 입금 계좌로 돈을 입금하는 것이다.  

```java
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

	@Getter 
	private final AccountId id;

	@Getter 
	private final Money baselineBalance;

	@Getter 
	private final ActivityWindow activityWindow;

	public static Account withoutId(
					Money baselineBalance,
					ActivityWindow activityWindow) {
		return new Account(null, baselineBalance, activityWindow);
	}

	public static Account withId(
					AccountId accountId,
					Money baselineBalance,
					ActivityWindow activityWindow) {
		return new Account(accountId, baselineBalance, activityWindow);
	}

	public Optional<AccountId> getId(){
		return Optional.ofNullable(this.id);
	}

	public Money calculateBalance() {
		return Money.add(
				this.baselineBalance,
				this.activityWindow.calculateBalance(this.id));
	}

	public boolean withdraw(Money money, AccountId targetAccountId) {

		if (!mayWithdraw(money)) {
			return false;
		}

		Activity withdrawal = new Activity(
				this.id,
				this.id,
				targetAccountId,
				LocalDateTime.now(),
				money);
		this.activityWindow.addActivity(withdrawal);
		return true;
	}

	private boolean mayWithdraw(Money money) {
		return Money.add(
				this.calculateBalance(),
				money.negate())
				.isPositiveOrZero();
	}

	public boolean deposit(Money money, AccountId sourceAccountId) {
		Activity deposit = new Activity(
				this.id,
				sourceAccountId,
				this.id,
				LocalDateTime.now(),
				money);
		this.activityWindow.addActivity(deposit);
		return true;
	}

	@Value
	public static class AccountId {
		private Long value;
	}

}
```


Account 엔티티는 실제 계좌의 현재 스냅샷을 제공한다.   
계좌에 대한 모든 입금과 출금은 Activity 엔티티에 포착된다.   

한 계좌에 대한 모든 활동들을 항상 메모리에 한꺼번에 올리는 것은 현명한 방법이 아니기 때문에    
며칠 혹은 몇 주간의 범위에 해당하는 활동만 보유한다.  


계좌의 현재 잔고를 계산하기 위해 Account 엔티티는 활동창(activity window)의 첫번째 활동 바로 전의 잔고를 표현하는 baselineBalance 속성을 가지고 있다.    
총 잔고는 기준 잔소에 활동창의 모든 활동들의 잔고를 합한 값이 된다.   

출금하기 전 잔고를 초과하는 금액은 출금할 수 없도록 하는 비즈니스 규칙을 검사한다.   

입금과 출금할 수 있는 Account 엔티티가 있으므로 이를 중심으로 유스켕스를 구현하기 위해 바깥 방향으로 나아갈 수 있다.   

<br />

### 유스케이스 둘러보기 
일반적으로 유스케이스는 다음과 같은 단계를 따른다.


1. 입력을 받는다.   
2. 비즈니스 규칙을 검증한다.   
3. 모델 상태를 조작한다.   
4. 출력을 반환한다.   


유스케이스는 인커밍 어댑터로부터 입력을 받는다.

유스케이스는 비즈니스 규칙을 검증할 책임이 있다.    
그리고 도메인 엔티티와 이 책임을 공유한다.   

비즈니스 규칙을 충족하면 유스케이스는 입력을 기반으로 어떤 방법으로든 모델의 상태를 변경한다.   
일반적으로 도메인 객체릐 상태를 바꾸고 영속성 어댑터를 통해 구현된 포트로 이 상태를 전달해서 저장될 수 있게 한다.   
유스케이스는 또 다른 아웃고잉 어댑터를 호출할 수도 있다.   


마지막 단계는 아웃고잉 어댑터에서 온 출력값을 유스케이스를 호출한 어댑터로 반환할 출력 객체로 변환하는 것이다.   

'송금하기' 유스케이스를 구현해보자.  
