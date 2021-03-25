* 예외 처리    
* 엔티티 비교   
* 프록시 심화 주제    
* 성능 최적화    
    - N+1 문제    
    - 읽기 전용 쿼리의 성능 최적화   
    - 배치 처리   
    - SQL 쿼리 힌트 사용   
    - 트랜잭션을 지원하는 쓰기 지원과 성능 최적화  
<br />         

# 1. 예외 처리      

### 1.1 JPA 표준 예외   
JPA 예외는 모두 언체크 예외다.   
 
JPA 표준 예외는 크게 2가지로 나뉜다.              

* 트랜잭션 롤백을 표시하는 예외      
  트랜잭션 롤백을 표시하는 예외는 심각한 예외이므로 복구해선 안된다.   
  이 예외는 트랜잭션을 강제로 커밋해도 트랜잭션이 커밋되지 않고 RollbackException 예외가 발생한다.

  
* 트랜잭션 롤백을 표시하지 않는 예외   
  트랜잭션 롤백을 표시하지 않는 예외는 심각한 예외는 아니기 때문에 개발자가 트랜잭션을 커밋할지 롤백할지 판단하면 된다.    

  
<br />         

### 1.2 트랜잭션 롤백 시 주의사항      
트랜잭션을 롤백하는 것은 데이터베이스의 반영사항만 롤백하는 것이지 수정한 자바 객체까지 원상태로 복구해주지는 않는다.    
데이터베이스는 원래대로 복구되지만 객체는 수정된 상태로 영속성 컨텍스트에 남아 있다.   
따라서 롤백된 영속성 컨텍스트를 그대로 사용하는 것은 위험하다.
새로운 영속성 컨텍스트를 생성해서 사용하거나 `EntityManeger.clear()`를 호출해서 초기화한 다음 사용해야 한다.    

스프링 프레임워크는 이런 문제를 예방하기 위해 영속성 컨텍스트의 범위에 따라 다른 방법을 제공한다.     
기본 전략인 트랜잭션당 영속성 컨텍스트 전략은 문제가 발생하면 트랜잭션 AOP 종료 시점에 트랜잭션을 롤백하면서 영속성 컨텍스트도 함께 종료한다.    

문제는 OSIV처럼 영속성 컨텍스트의 범위를 트랜잭션 범위보다 넓게 사용해서 여러 트랜잭션이 하나의 영속성 컨텍스트를 사용할 때 발생한다.   
이때는 잘못된 영속성 컨텍스트를 사용하는 문제를 예방해야 한다.   

<br />   

### 2. 엔티티 비교    
영속성 컨텍스트 내부에는 엔티티 인스턴스를 보관하기 위한 1차 캐시가 있다.    
이 1차 캐시는 영속성 컨텍스트와 생명주기를 같이 한다.   

영속성 컨텍스트를 통해 데이터를 저장하거나 조회하면 1차 캐시에 엔티티가 저장된다. 
이 1차 캐시 덕분에 변경 감지 기능도 동작하고 이름 그대로 데이터베이스를 통하지 않고 데이터를 바로 조회할 수 있다.   

영속성 컨텍스트를 더 정확히 이해하기 위해서는 1차 캐시의 가장 큰 장점인 애플리케이션 수준의 반복 가능한 읽기를 이해해야 한다.    
같은 영속성 컨텍스트에서 엔티티를 조회하면 다음 코드와 같이 항상 같은 엔티티 인스턴스를 반화한다.      

```
Member m1 = em.find(Member.class, "1L");
Member m2 = em.find(Member.class, "1L");

assertTrue(m1 == m2); // 둘은 같은 인스턴스다.  

```

이것은 단순히 동등성(equals) 비교 수준이 아니라 정말 주소값이 같은 인스턴스를 반환한다.    

<br />     

### 2.1 영속성 컨텍스트가 같을 때 인스턴스 비교    
영속성 컨텍스트가 같으면 엔티티를 비교할 때 다음 3가지 조건을 모두 만족한다.    

* 동일성(identity): `==` 비교가 같음        
* 동등성(equinalent): `equals()` 비교가 같음    
* 데이터베이스 동등성: `@Id`인 데이터베이스 식별자가 같음      

<br />  

### 2.2 영속성 컨텍스트가 다를 때 엔티티 비교     
영속성 컨텍스트가 다르면 동일성 비교에 실패한다.   
* 동일성: `==` 비교 실패   
* 동등성(equinalent): `equals()` 비교 만족, 단 `equals()`를 구현해야 한다.   
보통 비즈니스 키로 구현한다.
* 데이터베이스 동등성: `@Id`인 데이터베이스 식별자가 같음      

<br />    

# 3. 프록시   
프록시는 원본 엔티티를 상속받아서 만들어지므로 엔티티를 사용하는 클라이언트는 엔티티가 프록시인지 원본인지 구분하지 않고 사용할 수 있다.    
따라서 원본 엔티티를 사용하다가 지연 로딩을 하려고 프록시로 변경해도 클라이언트의 비즈니스 로직을 수정하지 않아도 된다.    
하지만 예상하지 못한 문제가 발생하기도 한다.   

<br />   

### 3.1 영속성 컨텍스트와 프록시     
영속성 컨텍스트는 자신이 관리하는 영속 엔티티의 동일성을 보장한다.   
프록시로 조회한 엔티티의 동일성도 보장할까?   

```
@Test
public void 영속성컨텍스트와_프록시() {
      Member newMember = new Member("member1", "회원1");
      em.persist(newMember);
      em.flush();
      em.clear();
    
      Member refMember = em.getReference(Member.class, "member1");
      Member findMember = em.find(Member.class, "member1");
    
      System.out.println("refMember Type = " + refMember.getClass());
      System.out.println("findMember Type = " + findMember.getClass());
    
      Assert.assertTrue(refMember == findMember); // 성공 
}
```

출력 결과는 같다.    
프록시로 조회해도 영속성 컨텍스트는 영속 엔티티의 동일성을 보장한다.    

원본 엔티티를 먼저 조회하면 영속성 컨텍스트는 원본 엔티티를 이미 데이터베이스에서 조회했으므로 프록시를 반환할 이유가 없다.   
따라서 순서를 바꿔도 동일성을 보장한다.    

<br />   

### 3.2 프록시 타입 비교   
프록시로 조회한 엔티티의 타입을 비교할 때는 `==` 비교를 하면 안 되고 대신에 `instanceof`를 사용해야 한다.        

<br />

### 3.3 프록시 동등성 비교      
엔티티의 동등성을 비교하려면 비즈니스 키를 사용해서 `equals()` 메소드를 오버라이딩하고 비교하면 된다.    
그런데 IDE나 외부 라이브러리를 사용해서 구현한 `equals()` 메소드로 엔티티를 비교할 때   
비교 대상이 원본 엔티티면 문제가 없지만 프록시면 문제가 발생할 수 있다.   

```
@Override
public boolean equals(Object obj) {
      if(this == obj) return true;
      if(obj == null) return false;
      if(this.getClass() != obj.getClass()) return false; // (1)
    
      Member member = (Member) obj;
    
      if(name != null ? !name.equals(member.name)) :
        member.name != null) // (2)
        return false;
       
      return false;
}
```

위 코드의 Member 엔티티는 name 필드를 비즈니스 키로 사용해서 `equals()` 메소드를 오버라이딩했다.   
(name이 중복되는 회원이 없다고 가정한다.)      

<br />        

```
@Test
public void 프록시와_동등성비교() {
      Member saveMember = new Member("member1", "회원1");
    
      em.persist(saveMember);
      em.flush();
      em.clear();
    
      Member newMember = new Member("member1", "회원1");
      Member refMember = em.getReference(Member.class, "member1");
    
      Assert.assertTrue(newMember.equals(refMember));
}
```

![스크린샷 2021-03-24 오후 10 19 01](https://user-images.githubusercontent.com/33855307/112317003-f8d8e080-8cee-11eb-9118-adfc2dae4c5d.png)     

테스트 코드와 그림을 보면 새로 생성한 회원 newMember와 프록시로 조회한 회원 refMember의      
name 속성은 둘 다 회원1로 같으므로 동등성 비교를 하면 성공할 것 같다.       
그러나 결과는 false가 나오면서 테스트는 실패한다.   

이 테스트는 프록시가 아닌 원본 엔티티를 조회해서 비교하면 성공한다.      
왜 이런 문제가 발생할까?     
프록시와 `equals()` 비교를 할 때 몇가지 주의점이 있다.     

<br />       

```
// (1)
if(this.getClass() != obj.getClass()) {
    return false;
}
```

여기서 타입을 동일성(`==`) 비교한다.   
프록시는 원본을 상속받은 자식 타입이므로 프록시의 타입을 비교할 때는 `==` 비교가 아닌 `instanceof`를 사용해야 한다.       

<br />     

```
// (2)
if(name != null ? !name.equals(member.name)) : member.name != null) {
    return false;
}
```

![스크린샷 2021-03-24 오후 10 34 51](https://user-images.githubusercontent.com/33855307/112319194-29ba1500-8cf1-11eb-88d7-88abb3c43f86.png)     

`member.name`을 보면 프록시의 멤버변수에 직접 접근하는데 이 부분이 문제다.    
`equals()` 메소드를 구현할 때 일반적으로 멤버변수를 직접 비교하는데, 프록시의 경우에는 문제가 된다.    
프록시는 실제 데이터를 가지고 있지 않다. 따라서 프록시의 멤버변수에 직접 접근하면 아무값도 조회할 수 없다.       

`member.name`의 결과는 null이 반환되고 `equals()`는 false를 반환한다.    
name 멤버변수가 private이므로 일반적인 상황에서는 프록시의 멤버변수에 직접 접근하는 문제가 발생하지 않지만   
`equals()` 메소드는 자신을 비교하기 때문에 private 멤버변수에도 접근할 수 있다.   

프록시의 데이터를 조회할 때는 다음과 같이 접근자(getter)를 사용해야 한다.   

```
if(name != null ? !name.equals(member.getName())) : member.getName() != null) {
    return false;
}
```


![스크린샷 2021-03-24 오후 10 42 24](https://user-images.githubusercontent.com/33855307/112320267-368b3880-8cf2-11eb-8ef9-0cdaf6973495.png)   

<br />      

### 프록시 동등성 비교 시 주의사항   
* 프록시의 타입 비교는 `==` 대신에 `instanceof`를 사용해야 한다.      
* 프록시의 멤버변수에 직접 접근하면 안 되고 대신에 접근자 메소드를 사용해야 한다.     

<br />     


### 3.4 상속관계와 프록시    
상속관계를 프록시로 조회할 때 발생할 수 있는 문제점과 해결방안을 알아보자.    

![스크린샷 2021-03-24 오후 10 46 52](https://user-images.githubusercontent.com/33855307/112320943-d648c680-8cf2-11eb-952c-09bf0de2cf83.png)    

프록시를 부모 타입으로 조회하면 문제가 발생한다.    

<br />   

```
@Test
public void 부모타입으로_프록시조회() {
    
    // 테스트 데이터 준비 
    Book saveBook = new Book();
    saveBook.setName("jpabook");
    saveBook.setAuthor("kim");
    em.persist(saveBook);
    
    em.flush();
    em.clrear();
    
    // 테스트 시작
    Item proxyItem = em.getReference(Item.class, saveBook.getId());
    System.out.println("proxyItem = " + proxyItem.getClass());
    
    if(proxyItem instanceof Book) {
        System.out.println("proxyItem = " + book.getAuthor());
        Book book = (Book) proxyItem;
        System.out.println("책 저자 = " + book.getAuthor);
    }
    
    // 결과 검증 
    Assert.assertFalse(proxyItem.getClass() == Book.class);
    Assert.assertFalse(proxyItem instanceof Book);
    Assert.assertTrue(proxyItem instanceof Item);  
}
```

위 테스트의 출력 결과는 다음과 같다.   

```
proxyItem = class jpabook.proxy.advanced.item.Item_$$_jvstXXX
```

Item을 조회해서 Book 타입이면 저자 이름을 출력하는 테스트다.    


먼저 `em.getReference()` 메소드를 사용해서 Item 엔티티를 프록시로 조회한다.    
그리고 `instanceof` 연산을 사용해서 proxyItem이 Book 클래스 타입인지 확인한다.     
Book 타입이면 다운캐스팅해서 Book 타입으로 변경하고 저자 이름을 출력한다.       
출력 결과는 기대와 다르게 저자가 출력되지 않는다.    


![스크린샷 2021-03-25 오후 10 28 01](https://user-images.githubusercontent.com/33855307/112480429-5df90880-8db9-11eb-8aba-3103fb66311b.png)    

`em.getReference(Item.class, saveBook.getId())` 메소드를 사용해서 Item 엔티티를 프록시로 조회했다.         
이때 실제 조회된 엔티티는 Book이므로 Book 타입을 기반으로 원본 엔티티 인스턴스가 생성된다.    
그런데 메소드에서 Item 엔티티를 대상으로 조회했기 때문에 프록시는 Item 타입을 기반으로 만들어진다.    
이 프록시 클래스는 원본 엔티티로 Book 엔티티를 참조한다.    

출력 결과와 그림을 보면 proxyItem이 Book이 아닌 Item 클래스를 기반으로 만들어진 것을 확인할 수 있다.    
이런 이유로 다음 연산이 기대와 다르게 false를 반환한다.     
프록시인 proxyItem은 Item$Proxy 타입이고 이 타입은 Book 타입과 관계가 없기 때문이다.     

따라서 직접 다운캐스팅을 하는 부분에도 문제가 발생한다.   

```
Book book = (Book) proxyItem; // java.lang.ClassCastException
```

proxyItem은 Book 타입이 아닌 Item 타입을 기반으로 한 Item$Proxy 타입이기 때문에 예외가 발생한다.   
프록시를 부모 타입으로 조회하면 부모의 타입을 기반으로 프록시가 생성되는 문제가 있다.    
* `instanceof` 연산을 사용할 수 없다.   
* 하위 타입으로 다운캐스팅을 할 수 없다.   

<br />     

프록시를 부모 타입으로 조회하는 문제는 주로 다형성을 다루는 도메인 모델에서 나타난다.   

```
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    
    ...    
    
}
```

OrderItem에서 Item을 지연 로딩으로 설정해 item이 프록시로 조회된다.   

```
OrderItem orderItem = em.find(OrderItem.class, saveOrderItem.getId());
Item item = orderItem.getItem();
```

Item을 지연로딩으로 설정했기 때문에 `item instanceof Book` 연산도 false를 반환한다.     

상속관계에서 발생하는 프록시 문제는 다음과 같은 방법으로 해결할 수 있다.    
* JPQL로 대상 직접 조회        
* 프록시 벗기기      

하이버네이트가 제공하는 기능을 사용해서 프록시에서 원본 엔티티를 가져올 수 있다.

영속성 컨텍스트는 한 번 프록시로 노출한 엔티티는 계속 프록시로 노출한다.    
그래야 영속성 컨텍스트가 영속 엔티티의 동일성을 보장할 수 있고, 클라이언트는 조회한 엔티티가 프록시인지 아닌지 구분하지 않고 사용할 수 있다.   

하지만 프록시를 벗기는 방법은 프록시에서 원본 엔티티를 직접 꺼내기 때문에 프록시와 원본 엔티티의 동일성 비교가 실패한다는 문제점이 있다.   
따라서 다음 연산의 결과는 false다.  

```
item == unProxyItem 
```

이 방법을 사용할 때는 원본 엔티티가 꼭 필요한 곳에서 잠깐 사용하고 다른 곳에서 사용되지 않도록 하는 것이 중요하다.    
원본 엔티티의 값을 직접 변경해도 변경 감지 기능은 동작한다.   


<br />      



