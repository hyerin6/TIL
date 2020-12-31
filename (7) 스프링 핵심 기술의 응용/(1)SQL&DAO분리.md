# SQL과 DAO의 분리          

반복적인 JDBC 작업 흐름 -> 템플릿 이용하여 해결              
트랜잭션과 예외처리 -> 서비스 추상화, AOP 이용해 해결           

데이터를 가져오고 조작하는 작업의 인터페이스 역할을 하는 것이 DAO다.       
데이터 액세스 로직이 바뀌지 않더라도 DB의 테이블, 필드 이름과 SQL 문장이 바뀔 수 있다.     
어떤 이유든지 SQL 변경이 필요한 상황이 발생하면 SQL을 담고 있는 DAO 코드가 수정될 수밖에 없다.        

SQL을 DAO에서 분리해보자.        


## XML 설정을 이용한 분리      

#### 개별 SQL 프로퍼티 방식   
가장 쉽게 생각해 볼 수 있는 SQL 분리 방법은 SQL을 스프링의 XML 설정파일로 빼는 것이다.     

- 스프링은 설정을 이용해 빈에 (String인 SQL)값을 주입해줄 수 있다.    
즉 스프링에서 String 값을 외부에서 DI해서 사용할 수 있다는 의미      
- 매번 새로운 SQL이 필요할 때마다 프로퍼티를 추가하고 DI를 위한 변수와 setter이 필요하다.   

#### SQL 맵 프로퍼티 방식   
SQL이 많아지면 그때마다 DAO에 DI용 프로퍼티를 추가하기가 귀찮다.     
Map을 이용하여 key 값을 사용해 SQL 문장을 가져오자. 

맵으로 만들어두면 새로운 SQL이 필요할 때 설정에 <entry>만 추가하면 되니 작업량이 적어지고 코드도 간단해진다.     
하지만 메소드에서 SQL을 가져올 때 문자열로 된 키 값을 사용하기 때문에          
오타와 같은 실수가 있어도 해당 메소드 실행 전까지 오류를 확인하기 힘들다.          

<br />               

## SQL 제공 서비스      
스프링의 설정파일 안에 SQL을 두고 이에 DI해서 DAO가 사용하게 하면 손쉽게 SQL을 코드에서 분리할 수 있지만 몇가지 문제점이 있다.       

- SQL과 DI 설정정보가 섞여있으면 지저분해보이고 관리가 어렵다.               
- 스프링의 설정정보로부터 생성된 오브젝트와 정보는 애플리케이션 재시작하지 않고는 변경이 어렵다.             
- 싱글톤인 DAO의 인스턴스 변수에 접근해서 주입된 SQL 맵 오브젝트를 수정하는건 어렵고 동시성 문제를 일으킬 수도 있다.                      

이런 문제점을 해결하려면 **DAO가 사용할 SQL을 제공해주는 기능을 독립**시킬 필요가 있다.       
다음과 같은 기능을 가진 독립적인 SQL 제공 서비스가 필요하다.          


#### SQL 서비스 인터페이스        
이런 경우 클라이언트인 DAO를 SQL 서비스의 구현에서 독립적으로 만들도록 인터페이스를 사용하고, DI로 구현 클래스의 오브젝트를 주입해주어야 한다.               

```  
// SqlService 인터페이스 
public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException; // 런타임 예외    
}

// UserDaoJdbc 
public class UserDaoJdbc implements UserDao {
    private SqlService sqlService;
    
    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    } 

    . . .

}
```


#### 스프링 설정을 사용하는 단순 SQL 서비스        

```
public class SimpleSqlService implements SqlService {
    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) { 
        this.sqlMap = sqlMap; 
    }

    . . . 

}
```  

<br />               


# 인터페이스의 분리와 자기참조 빈      

인터페이스가 하나 있다고 기계적으로 구현 클래스를 만들면 안된다.   
인터페이스로 대표되는 기능을 구현 방법과 확장 가능성에 따라 유연한 방법으로 재구성할 수 있도록 설계해야 한다.              


## XML 파일 매핑       

스프링의 XML 설정파일에 SQL 정보를 함께 저장해두는 것보다는 SQL을 저장해두는 전용 포맷을 가진 독립적인 파일을 이용하는 것이 바람직하다.          
검색용 키와 SQL 문장 두 가지를 담을 수 있는 간단한 XML 문서를 설계해보고           
이 파일에서 SQL을 읽어뒀다가 DAO에게 제공해주는 SQL 서비스 구현 클래스를 만들어보자.           


#### XML SQL 서비스   
`sqlmap.xml`에 있는 SQL을 가져와 DAO에 제공해주는 SqlService 인터페이스의 구현 클래스를 만들어보자.   

```  
public class XmlSqlService implements SqlService {
    
    . . .
    
    public XmlSqlService() {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            . . .
        }
    }

}
```    
   
SQL 문장을 스프링의 빈 설정에서 분리했다.          
DAO 로직이나 파라미터가 바뀌지 않는 한 SQL 내용을 변경하더라도 애플리케이션 코드나 DI 설정은 수정할 필요가 없다.          

<br />               


## 빈의 초기화 작업        

XmlSqlService 코드에 몇 가지 개선할 점이 있다.       


1. 생성자에서 예외가 발생할 수도 있는 복잡한 초기화 작업을 다루는 것은 좋지 않다.             
오브젝트를 생성하는 중에 생성자에서 발생하는 예외는 다루기 힘들고 상속하기 불편하며 보안에도 문제가 발생할 수 있다.          

2. 읽어들일 파일의 위치와 이름이 코드에 고정되어 있는게 불편해 보인다.                         
코드의 로직과 여타 이유로 바뀔 가능성이 있는 내용은 외부에서 DI로 설정해줄 수 있게 만들어야 한다.                                    


생성자에서 하던 작업을 `loadSql()` 이라는 이름을 가진 별도의 메소드로 빼보자.    
다음은 `XmlSqlService` 오브젝트의 초기화 방법이다.    

```      
XmlSqlService sqlProvider = new XmlSqlService();
sqlProvider.setSqlmapFile("sqlmap.xml");
sqlProvider.loadSql();
```   

그러나 `XmlSqlService` 오브젝트는 빈이므로 제어권이 스프링에 있다.        
스프링은 빈 오브젝트를 생성하고 DI 작업을 수행해서 프로퍼티를 모두 주입해준 뒤에 미리 지정한 초기화 메소드를 호출해주는 기능을 갖고 있다.    

AOP를 살펴볼 때 스프링의 빈 후처리기에 대해 설명했다.   
빈 후처리기는 스프링 컨테이너가 빈을 생성한 뒤에 부가적인 작업을 수행할 수 있게 해주는 기능이다.   
AOP를 위한 프록시 자동생성기가 대표적인 빈 후처리기다.   
프록시 자동생성기 외에도 여러 빈 후처리기가 존재하고 어노테이션을 이용한 빈 설정을 지원해주는 몇 가지 빈 후처리기가 있다.           


`<context:annotation-config />` 태그에 의해 등록되는 빈 후처리기는 몇 가지 특별한 빈 설정에 사용되는 어노테이션을 제공한다.           
사용할 어노테이션은 `@PostConstruct`다. `@PostConstruct`는 java.lang.annotaion 패키지에 포함된 공통 어노테이션의 한 가지로     
스프링은 `@PostConstruct` 어노테이션을 **빈 오브젝트의 초기화 메소드를 지정**하는 데 사용한다.                                                                      

`@PostConstruct`를 초기화 작업을 수행할 메소드에 부여해주면 스프링은 XmlSqlService 클래스로 등록된 빈의 오브젝트를 생성하고   
DI 작업을 마친 뒤 `@PostConstruct`가 붙은 메소드를 자동으로 실행해준다.   

```
@PostConstruct
public void loadSql() { ... }
```

스프링 컨테이너의 초기 작업 순서는 다음과 같다.       

![스크린샷 2020-12-30 오전 12 01 03](https://user-images.githubusercontent.com/33855307/103293355-1cafc180-4a33-11eb-8656-ebd83964e3ee.png)         


<br />                



## 변화를 위한 준비: 인터페이스 분리            

SQL 서비스 기능에는 확장할 영역이 많이 남아있다.   
현재 XmlSqlService는 다음과 같은 상태이다.   
       
- 특정 포맷의 XML에서 SQL 테이블을 가져온다. -> 특정 포맷에 고정됨        
- sql 데이터를 Map에 저장해놓는다. -> Map이 아닌 다른 저장 방법으로 변경한다면?        

코드는 여러 이유에 의해 변경되게 된다. 이는 단일 책임 원칙을 위반한다.       
서로 관심이 다른 코드들을 분리하고 서로 코드에 영향을 주지 않으면서 유연하게 확장이 가능하도록 DI를 적용해보자.        


#### 책임에 따른 인터페이스 정의           
`XmlSqlService`에서 책임을 뽑아보자.   

1. sql 정보 외부 리소스에서 읽어오기      
2. sql 보관해두고 있다가 필요할 때 제공하기 -> 저장소 제공        

여기서 추가로 부가적인 책임을 생각해볼 수 있다.   
3. 한 번 가져온 sql을 필요에 따라 수정할 수 있게 한다.   


기본적으로 SqlService를 구현해서 DAO에 서비스를 제공해주는 오브젝트가 이 책임들을 가진 오브젝트와 협력해서 동작하도록 만들어야 한다.     
변경 가능한 기능은 전략 패턴을 적용해 별도의 오브젝트로 분리해줘야 한다.     

DAO 관점에서는 SqlService 인터페이스를 구현한 오브젝트에만 의존하고   
SqlService의 구현 클래스가 변경 가능한 책임을 가진 SqlReader와 SqlRegistry 두 가지 타입의 오브젝트를 사용하도록 만든다.     

SQL 등록, 조회 기능을 담고있는 SqlRegistry의 일부 인터페이스는 SqlService가 아닌 다른 오브젝트가 사용할 수도 있다.            
대표적으로 SQL을 런타임 시에 변경하도록 요청하는 오브젝트가(SqlUpdater) 필요에 따라 이를 호출해서 SQL을 갱신하도록 요청할 수 있다.       


![스크린샷 2020-12-31 오전 12 02 32](https://user-images.githubusercontent.com/33855307/103363081-a035e500-4afd-11eb-825b-337ccb2f229a.png)                     


SqlReader가 읽어오는 SQL 정보는 다시 SqlRegistry에 전달해서 등록되게 해야 한다.          
기존에 생각했던 Map을 사용한 전달 방식은 전송 타입을 강제하게 되기 때문에 번거로워 보인다.         
SqlReader의 리턴 타입과 SqlRegistry 내부의 데이터 타입이 다르면 전달 과정 중 포맷을 변경해줘야 한다.                

SqlService가 SqlReader에게서 정보를 전달받은 뒤, SqlRegistry에 다시 전달해줘야 할 필요는 없다.             
SqlService에서 정보를 활용하지 않는다면 여기서 SqlService는 빠지고       
SqlReader에게 SqlRegistry 전략을 제공해주면서 이를 이용해 SQL 정보를 SqlRegistry에 저장하라고 요청하는 편이 낫다.     

```
// 변경전 
Map<String, String> sqls = sqlReader.readSql(); // Map이라는 구체적인 전송 타입을 강제 
sqlRegistry.addSqls(sqls);

// 변경 후 
sqlReader.readSql(sqlRegistry); // SQL 저장 대상인 sqlRegistry 오브젝트를 전달한다.   
```

SqlReader는 내부에 갖고 있는 SQL 정보를 형식을 갖춰서 돌려주는 대신,     
협력관계에 있는 의존 오브젝트인 SqlRegistry에게 필요에 따라 등록을 요청할 때만 활용하면 된다.   
여기서 SqlReader가 사용할 SqlRegistry 오브젝트를 제공하는건 SqlService가 담당한다.   

다음은 SqlRegistry에 의존하는 SqlReader의 구조이다.   

![스크린샷 2020-12-31 오후 1 52 49](https://user-images.githubusercontent.com/33855307/103395053-7ae1bf00-4b6f-11eb-9d39-f0089bd77082.png)  


- SqlRegistry가 일종의 콜백 오브젝트처럼 사용된다고 볼 수 있다.      
- SqlReader 입장에서 SqlRegistry 인터페이스를 구현한 오브젝트를 런타임 시에 메소드 파라미터로 제공받아 사용하는 코드에 의한 수동 DI라고 볼 수 있다.       
- SqlRegistry는 SqlService에게 등록된 SQL을 검색해서 돌려주는 기능도 있기 때문에 SqlService에 의존 오브젝트이기도 하다.     


<br />         


## 자기참조 빈으로 시작하기        

#### 다중 인터페이스 구현과 간접 참조      
SqlService의 구현 클래스는 SqlReader와 SqlRegistry 두개의 프로퍼티를 DI 받을 수 있는 구조로 만들어야 한다.    

![스크린샷 2020-12-31 오후 2 22 08](https://user-images.githubusercontent.com/33855307/103395992-27be3b00-4b74-11eb-8ad1-e0fd365f2778.png)    

모든 클래스는 인터페이스에만 의존하고 있다.   
자신이 사용하는 오브젝트의 클래스가 어떤 것인지 알지 못하게 해서 구현 클래스를 바꾸고   
의존 오브젝트를 변경해서 자유롭게 확장할 기회를 제공할 수 있다.   

이 세개의 인터페이스를 하나의 클래스가 모두 구현한다면?   
XmlSqlService 클래스 하나가 세 개의 인터페이스를 구현하도록 만들어도 된다.   

![스크린샷 2020-12-31 오후 2 25 46](https://user-images.githubusercontent.com/33855307/103396085-779d0200-4b74-11eb-9ea0-98ac395b1156.png)     

같은 클래스 코드지만 책임이 다른 코드는 직접 접근하지 않고 인터페이스를 통해 간접적으로 사용하는 코드로 변경해보자.   

<br />         

#### 인터페이스를 이용한 분리     

```   
public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {
    private Map<String, String> sqlMap = new HashMap<>();
    private String sqlmapFile;
    
    // HashMap이라는 저장소를 사용하는 구체적인 구현 방법에서 독립할 수 있도록 인터페이스의 메소드로 접근하게 한다. 
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    // sqlmapFile은 sqlReader 구현의 일부이다. SqlReader 구현 메소드를 통하지 않고 접근하면 안된다.   
    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }
    
    public void read(SqlRegistry sqlRegistry) {
        . . .
    
        // SQL 저장 로직 구현에 독립적인 인터페이스 메소드를 통해 읽은 SQL과 key를 전달한다.   
        sqlRegistry.registerSql(sql.getKey(), sql.getValue()); 
    }

    . . .
    
}
```    

- sqlMap은 SqlRegistry의 인터페이스를 구현의 일부가 됐으므로 SqlRegistry 구현 메소드가 아닌 메소드에서는 직접 사용하면 안 된다.     
독립적인 오브젝트라고 생각하고 SqlRegistry의 메소드를 통해 접근해야 한다.      

- SqlReader를 구현한 코드에서 XmlSqlService 내의 다른 변수와 메소드를 직접 참조하거나 사용하면 안된다.     
필요한 경우 적절한 인터페이스를 통해 접근해야 한다.     


마지막으로 SqlService 인터페이스 구현을 살펴보자.     
`@PostConstruct`가 달린 빈 초기화 메소드와 SqlService 인터페이스에 선언된 메소드인 `getFinder()`를 sqlReader와 sqlRegistry를 이용하도록 변경     

```
public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {
    . . .

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }
}
```  

<br />         

#### 자기참조 빈 설정     
```  
<bean id="sqlService" class="~.XmlSqlService">
    <property name="sqlReader" ref="sqlService" />
    <property name="sqlRegistry" ref="sqlService" />
</bean>
```


SqlService를 구현한 메소드와 초기화 메소드는 외부에서 DI된 오브젝트라고 생각하고 결국 자신의 메소드에 접근한다.        

<br />         

## 디폴트 의존관계      

확장 가능한 인터페이스를 정의하고 인터페이스에 따라 메소드를 구분해서 DI 가능하도록 재구성했다.   
이를 완전히 분리해서 DI로 조합해서 사용하게 만들어보자.   

#### 확장 가능한 기반 클래스    
SqlRegistry와 SqlReader를 이용하는 SqlService 구현 클래스 BaseSqlService를 만들어보자.    
하나의 클래스에 세 가지 인터페이스를 전부 구현하는 게 아니라 클래스를 분리하는 것이기 때문에 각각의 빈 설정이 추가로 필요하다.   

>BaseSqlservice는 상속을 통해 확장해서 사용하기에 적합하다.    
>SqlRegistry와 SqlReader 접근자를 protected로 선언하면 서브 클래스에서 필요한 경우 접근이 가능하다.    

<br />         
 
#### 디폴트 외존관계를 갖는 빈 만들기   
특정 오브젝트가 대부분의 환경에서 거의 디폴트라고 해도 될만큼 기본적으로 사용될 가능성이 있다면   
디폴트 의존관계를 갖는 빈을 만드는 것을 고려해볼 필요가 있다.      

디폴트 의존관계란 외부에서 DI 받지 않아도 기본적으로 자동 적용되는 의존관계를 의미한다.    
다음은 미리 설정된 디폴트 의존 오브젝트를 갖는 DefaultSqlService 클래스다.   

```
public class DefaultSqlService extends BaseSqlService {

    // 생성자에서 디폴트 의존 오브젝트를 직접 만들어서 스스로 DI 해준다.  
    public DefualtSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
```

DI 설정이 없을 경우 디폴트로 적용하고 싶은 의존 오브젝트를 생성자에 넣어줬다.        
3개의 빈을 정의하고 프로퍼티로 연결했던 설정은 다음과 같이 바뀐다.          

```
<bean id="sqlService" class="~.DefaultSqlService" /> 
```

테스트를 돌려보면 디폴트로 생성자에 넣어준 JaxbXmlSqlReader와 HashMapSqlRegistry를 이용해   
DefaultSqlService가 잘 동작할 것 같은데 테스트는 모두 실패한다.   
DefaultSqlService 내부에서 생성하는 JaxbXmlSqlReader의 sqlmapFile 프로퍼티가 비어있기 때문이다.    

<br />         

이 문제를 해결하는 방법은 다음과 같다.   

1. sqlmapFile을 DefaultSqlService의 프로퍼티로 정의          
외부 클래스의 프로퍼티로 정의해서 전달받는 방법 자체는 나쁘지 않지만 DefaultSqlService에 적용하기에는 적합하지 않다.        
JaxbXmlSqlReader는 디폴트 의존 오브젝트에 불과하기 때문이다.     
디폴트라는 건 다른 명시적인 설정이 없는 경우에 기본적으로 사용한다는 의미라 설정이 있으면 디폴트는 무시된다.      
DefaultSqlService는 JaxbXmlSqlReader를 사용하지 않을 수도 있는데     
반드시 필요한 것도 아닌 sqlmapFile을 프로퍼티로 등록해두는 것은 바람직하지 않다.     

2. sqlmapFile도 JaxbXmlSqlReader에 의해 기본적으로 사용될 만한 값을 관례적으로 사용할 만한 이름을 정해서 디폴트로 넣어준다.              
그렇게 하면 DefaultSqlService의 디폴트 외존 오브젝트가 되는 JaxbXmlSqlReader는          
디폴트 sqlmapFile 이름을 갖고 있게 되므로 별다른 설정 없이 사용 가능하다.           

```  
public class JaxbXmlSqlReader implements SqlReader {
    private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";

    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

    // sqlmapFile 프로퍼티를 지정하면 지정된 파일이 사용되고 아니라면 디폴트로 넣은 파일이 사용된다.   
    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }
}
```  

DefaultSqlService는 SqlService를 바로 구현한 것이 아니라 BaseSqlService를 상속했다는 점이 중요하다.   
DefaultSqlService는 BaseSqlService의 sqlReader와 sqlRegistry 프로퍼티를 그대로 갖고 있고   
원한다면 언제든지 변경이 가능하다.   

<br />         

그런데 디폴트 오브젝트를 사용하는 방법에 단점이 한가지 있다.     
설정을 통해 다른 구현 오브젝트트를 사용하더라도 DefaultSqlService는 생성자에서 일단 디폴트 의존 오브젝트를 다 만들어버린다는 점이다.      
바로 설정해 놓은 빈 오브젝트로 바로 대체되긴 하지만 사용되지 않는 오브젝트가 만들어진다는 점이 꺼림직하다.        

디폴트 외존 오브젝트가 만들어지지 않게 `@PostConstruct` 초기화 메소드를 사용해 프로퍼티가 설정됐는지 확인하고     
없는 경우에만 디폴트 오브젝트를 만드는 방법을 사용할 수 있긴하다.       

