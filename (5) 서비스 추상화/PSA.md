# 서비스 추상화     

지금까지 예제의 DAO에 트랜잭션을 적용해보면서 스프링이 어떻게 성격이 비슷한 여러 기술을 추상화하고 일관된 방법으로 사용할 수 있는지 공부해보자.     

<br />     

# 사용자 레벨 관리 기능 추가       

### 1. 필드 추가          
지금까지 DAO는 DB에 넣고 빼는 것을 제외하면 어떤 비즈니스 로직도 갖고있지 않다.   
간단한 비즈니스 로직을 추가해보자. 사용자의 활동 내역을 참고해서 레벨을 조정해주는 기능을 넣어보자.     
Enum으로 Level을 생성하고 User 필드에 추가한다.     


### 2. UserDaoJdbc 수정     

`INSERT` 문장이 들어있는 `add()` 메소드의 SQL과 조회작업에서 추가된 필드를 넣는다.     
사용자의 레벨 정보 변경을 위해 사용자 정보를 변경하는 `update()` 메소드를 구현한다.     


- 수정 테스트 보완      

JDBC 개발에서 리소스 반환과 같은 기본 작업을 제외하고 가장 많은 실수가 일어나는 곳은 SQL 문장이다.       
필드 이름이나 SQL 키워드 실수는 테스트에서 쉽게 발견할 수 있는데 `UPDATE` 문장에서 `WHERE` 절을 빼먹는 경우     
아무런 경고 없이 정상적으로 동작하는 것처럼 보인다.       
이 문제의 해결방법은 두 가지이다.   

(1) `update()` 리턴 값 확인하기 - 영향을 받은 로우의 개수가 1 이상이면 문제가 있는 것                      
(2) 테스트를 보강해서 원하는 사용자 외의 정보는 수정되지 않았음을 직접 확인하는 것                
사용자를 두 명 등록해 놓고 그 중 하나만 수정한 후, 수정된 사용자와 수정하지 않은 사용자 정보를 모두 확인해본다.   
                

### 3. `UserService.upgradeLevels()`      

사용자 관리 로직은 어디에 두는 게 좋을까?       
비즈니스 로직 서비스를 제공한다는 의미에서 클래스 이름을 `UserService`로 하고 여기에 구현하자.       
`UserService`는 `UserDao`의 인터페이스 타입으로 userDao 빈을 DI 받아 사용하게 만든다.   
DI 적용을 위해 `UserService`도 스프링 빈으로 등록돼야 한다.   


### 4. `userService.add()`  
   
처음 가입 시 사용자는 기본적으로 `BASIC` 레벨이어야 한다.   
이 로직은 어디에 담는 게 좋을까?   
`UserService`에도 `add()`를 만들어 두고 사용자가 등록될 때   
적용할 만한 비즈니스 로직을 담당하게 하면 된다.   


<br />   

# 트랜잭션 서비스 추상화         

사용자 레벨 관리 작업을 수행하는 도중에 네트워크가 끊기거나 서버에 장애가 생겨서 작업을 완료할 수 없다면   
그때까지 변경된 사용자의 레벨은 그대로 두는걸까? 아니면 작업 이전 상태로 돌아갈까?  
테스트를 만들어서 확인해보자. 이를 테스트하기 위해 중간에 예외를 강제로 발생시켜야 한다.            


### 테스트용 `UserService` 대역   

테스트용 `UserService` 확장 클래스는 `UserService`를 상속해서 필요한 기능이 구현된 메소드를 오버라이딩하면 된다.   
테스트 코드는 테스트 대상 클래스의 내부 구현 내용을 고려해서 밀접하게 접근해야 하는데   
private 처럼 제약이 강한 접근제한자는 사용이 불편하다.   
protected로 수정해서 상속을 통한 오버라이딩이 가능하게 하자.   


다음은 테스트용으로 만든 클래스이기 때문에 테스트 클래스의 내부 클래스로 구현했다.   

```
static class TestUserService extends UserService {  
    protected void upgradeLevel() {
        // 특정 상황에서 예외를 던져서 작업을 강제로 중단한다. 
        if(...) throw new TestUserServiceException(); 
    }
}
```

### 강제 예외 발생을 통한 테스트    

테스트 결과는 변경된 Level이 예외가 발생했지만 그대로 유지되고 있다.     
왜 이런 결과가 나왔을까?      

모든 사용자의 레벨을 업그레이드하는 작업인 `upgradeLevels()` 메소드가 하나의 트랜잭션 안에서 동작하지 않기 때문이다.   
트랜잭션이란 더 이상 나눌 수 없는 단위 작업을 말한다.   
작업을 쪼개서 작은 단위로 만들 수 없다는 것은 트랜잭션의 핵심 속성인 원자성을 의미한다.   

모든 사용자에 대한 레벨 업그레이드 작업은 부분적으로 성공하고, 여러 번에 걸쳐서 진행할 수 있는 작업이 아니어야 한다.   
따라서 예외가 발생해서 작업을 완료할 수 없다면 아예 작업이 시작되지 않은 것처럼 초기 상태로 돌려놔야 한다. 이것이 트랜잭션이다.   


### 트랜잭션 경계 설정     

- 롤백(rollback) : 취소 작업            
- 트랜잭션 커밋(commit) : 여러 개의 SQL을 하나의 트랜잭션으로 처리하는 경우 모든 SQL 수행 작업이 성공적으로 마무리됐다고 DB에 알려주는 것                   
- 트랜잭션의 경계설정 : commit 혹은 rollback 으로 트랜잭션을 종료하는 작업       
(트랜잭션의 경계는 하나의 Connection이 만들어지고 닫히는 범위 안에서 존재한다.)               
- 로컬 트랜잭션 : 하나의 DB 커넥션 안에서 만들어지는 트랜잭션                  


### `UserDao`와 `UserService`의 트랜잭션 문제      

왜 `UserService`의 메소드에 트래잭션이 적용되지 않았는지 생각해보자.     
그 이유는 트랜잭션 시작, 커밋, 롤백하는 경계설정 코드가 존재하지 않기 때문이다.      
JDBC의 트랜잭션 경계 설정 메소드는 전부 `Connection` 오브젝트를 사용하게 되어 있는데     
`JdbcTemplate`(지금까지 만든 `JdbcContext`라고 생각하면 된다.)를 사용하기 시작하고 `Connection` 오브젝트를 사용하지 않았다.          

하나의 템플릿 메소드 안에서 `DataSource`의 `getConnection()` 메소드를 호출해서 오브젝트를 가져오고         
작업을 마치면 `Connection`을 닫고 템플릿 메소드를 빠져나온다.         
그래서 `UserDao`는 각 메소드마다 하나씩의 독립적인 트랜잭션으로 실행될 수밖에 없다.          

어떤 일련의 작업이 하나의 트랜잭션으로 묶이려면 작업이 진행되는 동안 DB 커넥션도 하나만 사용돼야 한다.        


### 비즈니스 로직 내의 트랜잭션 경계설정     

문제해결을 위해 `upgradeLevels()` 메소드의 내용을 DAO 메소드 안으로 옮기는 방법을 생각해보자.       
이 방법은 비즈니스 로직과 데이터 로직을 한데 묶어버리는 결과가 초래된다.               

`UserService`와 `UserDao`를 그대로 분리하고 트랜잭션을 적용하려면 트랜잭션의 경계설정 작업을 `UserService` 쪽으로 가져와야 한다.                    
즉 `upgradeLevels()` 메소드 안에 DB 커넥션을 만들고 종료시켜야 한다는 의미이다.                

Connection 오브젝트를 가지고 데이터 액세스 작업을 진행하는 코드는 UserDao의 update() 메소드 안에 있어야 한다.      
순수한 데이터 로직은 UserDao에 둬야 하기 때문에 UserService에서 만든 Connection 오브젝트를 UserDao에서 사용하려면       
DAO 메소드를 호출할 때마다 Connection 오브젝트를 파라미터로 전달해줘야 한다.        


```       
public interface UserDao {
    void update(Connection c, User user);
}

class UserService {
    public void upgradeLevels() throws Exception {
        Connection c = ...;
        
        // Connection을 계속 전달하며 하나의 트랜잭션에서 작업하도록 한다. 
        upgradeLevel(c, user1);
        upgradeLevel(c, user2);
        upgradeLevel(c, user3);
        . . .
      
    }

    protected void upgradeLevel(Connection c, User user) {
        user.upgradeLevel();
        userDao.update(c, user);
    }
}
```      
   

### `UserService` 트랜잭션 경계설정의 문제점                  

위와 같은 식으로 수정하면 트랜잭션 문제는 해결되겠지만 그 대신 여러가지 새로운 문제가 발생한다.                     

1. 커넥션을 비롯한 리소스의 깔끔한 처리가 가능한 JdbcTemplate을 더 이상 활용할 수 없다.                  
try/catch/finally 블록이 이제 UserService 내에 존재하고,                      
JDBC 작업의 전형적인 문제점을 그대로 갖게 된다.                  

2. DAO의 메소드와 비즈니스 로직을 담고 있는 UserService의 메소드에 Connection 파라미터가 추가돼야 한다.                     
UserService는 스프링 빈으로 선언해서 싱글톤으로 되어 있으니 UserService의 인스턴스 변수에                       
이 Connection을 저장해뒀다가 다른 메소드에서 사용하게 할 수도 없다.                     
UserService의 메소드가 Connection 파라미터로 지저분해질 것이다.                        

3. Connection 파라미터가 UserDao 인터페이스 메소드에 추가되면 UserDao는 더 이상 데이터 액세스 기술에 독립적일 수가 없다.                  
JPA나 하이버네이트로 UserDao이 구현 방식이 변경되면 UserService도 함께 수정돼야 한다.                  

4. DAO 메소드에 Connection 파라미터를 받게 하면 테스트 코드에도 영향을 미친다.                 
Connection 오브젝트를 일일이 만들어서 DAO 메소드를 호출하도록 변경해야 한다.                


### 트랜잭션 동기화   

스프링은 이 딜레마를 해결할 수 있는 방법을 제공해준다.       

**1. `Connection` 파라미터 제거**        

`UserService`의 `upgradeLevels()` 메소드가 트랜잭션 경계설정을 해야 하는 것은 피할 수 없다.             
대신 Connection 오브젝트를 파라미터로 전달하는 것은 피할 수 있는데             
이를 위해 스프링이 제안하는 방법은 독립적인 트랜잭션 동기화 방법이다. (transaction synchronization)             

트랜잭션 동기화란 UserService에서 트랜잭션을 시작하기 위해 만든 Connection 오브젝트를 특별한 저장소에 보관해두고     
이후에 호출되는 DAO의 메소드에서는 저장된 Connection을 가져다가 사용하게 하는 것이다.            
즉 DAO가 사용하는 JdbcTemplate이 트랜잭션 동기화 방식을 사용하게 하는 것이다.         
트랜잭션이 모두 종료되면, 그때 동기화를 마치면 된다.   

트랜잭션 동기화 방식을 사용한 경우의 작업 흐름은 다음과 같다.   

![스크린샷 2020-12-20 오후 4 09 38](https://user-images.githubusercontent.com/33855307/102707442-54ee2c00-42de-11eb-84ea-0da76b73a428.png)          

(1) `UserService`는 `Connection`을 생성하고   
(2) 이를 트랜잭션 동기화 저장소에 저장해두고 `Connection`의 `setAutoCommit(false)`를 호출해(자동 커밋 옵션 끄기) 트랜잭션을 시작시킨 후에 본격적으로 DAO의 기능을 이용한다.   
(3) 첫번째 `update()` 메소드가 호출되고 `update()` 메소드 내부에서 이용하는 JdbcTemplate 메소드에서는 가장 먼저   
(4) 트랜잭션 동기화 저장소에 현재 시작된 트랜잭션을 가진 `Connection` 오브젝트가 존재하는지 확인한다.   
(2) `upgradeLevels()` 메소드 시작 부분에서 저장해둔 `Connection`을 가져온다.   
(5) `Connection`을 이용해 `PreparedStatement`를 만들어 수정 SQL을 실행한다.   
트랜잭션 동기화 저장소에서 DB 커넥션을 가져왔을 때는 `JdbcTemplate`은 `Connection`을 닫지 않은 채로 작업을 마친다.   

이렇게 첫 번째 `udpate()` 호출 DB 작업을 마쳤다.     
여전히 `Connection`은 열려 있고 트랜잭션은 진행 중인 채로 트랜잭션 동기화 저장소에 저장되어 있다.        

트랜잭션 내의 모든 작업이 정상적으로 끝났으면 UserService는    
(12) Connection의 commit()을 호출해서 트랜잭션을 완료시킨다.       
마지막으로 (13) 트랜잭션 저장소가 더 이상 Connection 오브젝트를 저장해두지 않도록 이를 제거한다.      
예외상황이 발생하면 UserService는 즉시 Connection의 rollback()을 호출하고 트랜잭션을 종료할 수 있다.      

트랜잭션 동기화 저장소는 작업 스레드마다 독립적으로 Connection 오브젝트를 저장하고 관리하기 때문에    
다중 사용자를 처리하는 서버의 멀티스레드 환경에서도 충돌이 날 염려는 없다.     


**2. 트랜잭션 동기화 사용**    

트랜잭션 동기화 방법을 구현하는 일이 간단하지 않은데   
스프링은 JdbcTemplate와 더불어 이런 트랜잭션 동기화 기능을 지원하는 간단한 유틸리티를 제공하고 있다.   
스프링이 제공하는 트랝개션 동기화 관리 클래스는 `TransactionSynchronizationManager`이고   
구현은 다음과 같다.   

```
public void upgradeLevels() {
    // 트랜잭션 동기화 관리자를 이용해 동기화 작업을 초기화한다. 
    TransactionSynchronizationManager.initSynchronization(); 

    . . .

    finally {
        // 동기화 작업 종료 및 정리 
        TransactionSynchronizationManager.unbindResource(this.dataSource);
        TransactionSynchronizationManager.clearSynchronization();
    }
}
```   


**3. `JdbcTemplate`과 트랜잭션 동기화**       

`JdbcTemplate`은 `update()`나 `query()` 같은 JDBC 작업의 템플릿 메소드를 호출하면     
직접 `Connection`을 생성하고 종료하는 일을 담당한다고 설명했다. 어떻게 동작하는 것일까?    

만약 미리 생성되서 트랜잭션 동기화 저장소에 등록된 DB 커넥션이나 트랜잭션이 없는 경우에는  
`JdbcTemplate`이 직접 커넥션을 만들고 트랜잭션을 시작해서 JDBC 작업을 진행한다.   

이는 JDBC 코드의 `try/catch/finally` 작업 흐름 지원, `SQLExeception`의 예외 반환과 함께     
`JdbcTemplate`이 제공해주는 세 가지 유용한 기능 중 하나다.      

`Connection` 파라미터를 계속 물고 다니지 않아도 되고     
`UserDao`는 여전히 데이터 액세스 기술에 종속되지 않는 깔끔한 인터페이스 메소드를 유지하고 있다.      

  
<br />    


### 트랜잭션 서비스 추상화     

**(1) 기술과 환경에 종속되는 트랜잭션 경계설정 코드**     

지금까지 트랜잭션을 적용하며, 책임과 성격에 따라 비즈니스 로직과 데이터 액세스 부분을 잘 분리했지만   
새로운 문제가 발생할 수 있다.          

이 사용자 관리 모듈을 구매해서 사용하기로 한 G 사에서 들어온 새로운 요구가 있다고 가정하자.       
지금까지 만든 코드로 업체별로 DB 연결 방법을 자유롭게 변경해서 사용할 수 있는데   
(이는 `DataSource` 인터페이스와 DI를 적용한 덕분!)              
트랜잭션 처리 코드를 담은 `UserService`에서 문제가 발생했다.               

G사는 이미 여러 개의 DB를 사용하고 있다.           
즉 한 개 이상의 DB로의 작업을 하나의 트랜잭션으로 넣는 작업을 해야 할 필요가 발생한 것이다.         
그러나 이는 JDBC의 `Connection`을 이용한 트랜잭션 방식인 로컬 트랜잭션으로는 불가능하다.   
로컬 트랜잭션은 하나의 DB `Connection`에 종속되기 때문이다.   

각 DB와 독립적으로 만들어지는 `Connection`을 통해서가 아니라   
별도의 트랜잭션 관리자를 통해 트랜잭션을 관리하는 글로벌 트랜잭션 방식을 사용해야 한다.   
글로벌 트랜잭션을 적용해야 트랜잭션 매니저를 통해 여러 개의 DB가 참여하는 작업을 하나의 트랜잭션으로 만들 수 있다.   

자바는 JDBC 외에 이런 글로벌 트랜잭션을 지원하는 트랜잭션 매니저를 지원하기 위한 API인 JTA(Java Transaction API)를 제공하고 있다.   
JTA를 이용한 트랜잭션 처리 코드의 전형적인 구조는 다음과 같다.   


```
// JNDI를 이용해 서버의 UserTransaction 오브젝트를 가져온다. 
InitialContext ctx = new InitialContext();
UserTransaction tx = (UserTransaction)ctx.lookup(USER_TX_JNDI_NAME);

tx.begin();
Connection c = dataSource.getConnection(); // JNDI로 가져온 dataSource를 사용해야 한다.   

try{
    // 데이터 액세스 코드 
    
    . . .

```

JTA를 이용한 방법으로 바뀌긴 했지만 트랜잭션 경계설정을 위한 구조는 JDBC를 사용했을 때와 비슷하다.   
문제는 JDBC로컬 트랜잭션을 JTA를 이용하는 글로벌 트랜잭션으로 바꾸려면 `UserService` 코드를 수정해야 한다는 점이다.   
로컬 트랜잭션을 사용해도 충분한 고객을 위해서는 JDBC를 이용한 트랜잭션 코드를,   
글로벌 트랜잭션이 필요한 곳을 위해서는 JTA를 이용한 트랜잭션 관리 코드를 적용해야 한다는 문제가 생긴다.   
`UserService`는 자신의 로직이 바뀌지 않았음에도 기술환경에 따라 코드가 바뀌는 코드가된 것이다.   

여기서 Y사에서 하이버네이트를 사용한다고 연락이 왔다고 가정하자.    
하이버네이트는 `Connection`을 직접 사용하지 않고 `Session`이라는 것을 사용하고   
독자적인 트랜잭션 관리 API를 사용한다.   
그렇다면 `UserService`는 하이버네이트의 `Session`과 `Transaction` 오브젝트를 사용하는 트랜잭션 경계설정 코드로 변경해야 한다.   


**(2) 트랜잭션 API의 의존관계 문제와 해결책**     

`UserService`에서 트랜잭션의 경계설정을 해야 할 필요가 생기면서 다시 특정 데이터 액세스 기술에 종속되는 구조가 되었다.     

![스크린샷 2020-12-20 오후 5 29 34](https://user-images.githubusercontent.com/33855307/102708863-04300080-42e9-11eb-96fe-5637a09e43ca.png)     

원래 `UserService`가 UserDao 인터페이스에만 의존하는 구조였다.       
그런데 JDBC에 종속적인 `Connection`을 이용한 트랜잭션 코드가     
`UserService`에 등장하면서부터 `UserService`는 `UserDaoJdbc`에 간접적으로 의존하는 코드가 되버렸다.       

`UserService`의 코드가 특정 트랜잭션 방법에 의존적이지 않고 독립적일 수 있게 만들려면 어떻게 해야 할까?      
특정 기술에 의존적인 `Connection`, `UserTransaction`, `Session/Transaction API` 등에 종속되지 않게 할 수 있는 방법이 있다.   







