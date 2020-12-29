# SQL과 DAO의 분리          

반복적인 JDBC 작업 흐름 -> 템플릿 이용하여 해결              
트랜잭션과 예외처리 -> 서비스 추상화, AOP 이용해 해결           

## XML 설정을 이용한 분리   

#### 개별 SQL 프로퍼티 방식   
가장 쉽게 생각해 볼 수 있는 SQL 분리 방법은 SQL을 스프링의 XML 설정파일로 빼는 것이다.   
스프링은 설정을 이용해 빈에 값을 주입해줄 수 있다.   
스프링에서 스트링 값을 외부에서 DI해서 사용할 수 있기 때문에 쉽게 SQL을 분리했지만   
이 방법은 매번 새로운 SQL이 필요할 때마다 프로퍼티를 추가하고 DI를 위한 변수와 setter를 만들어줘야 한다.   

#### SQL 맵 프로퍼티 방식   
SQL이 많아지면 그때마다 DAO에 DI용 프로퍼티를 추가하기가 귀찮다.     
그래서 SQL을 하나의 컬렉션으로 담아두는 방법을 생각해봤다.     
맵을 이용하여 키 값을 사용해 SQL 문장을 가져오는 것이다.     

맵으로 만들어두면 새로운 SQL이 필요할 때 설정에 <entry>만 추가하면 되니 작업량이 적어지고 코드도 간단해졌지만     
메소드에서 SQL을 가져올 때 문자열로 된 키 값을 사용하기 때문에 오타와 같은 실수가 있어도 해당 메소드 실행 전까지 오류를 확인하기 힘들다.      



<br />               

## SQL 제공 서비스      
스프링의 설정파일 안에 SQL을 두고 이에 DI해서 DAO가 사용하게 하면 손쉽게 SQL을 코드에서 분리할 수 있지만 몇가지 문제점이 있다.       

- SQL과 DI 설정정보가 섞여있으면 지저분해보이고 관리가 어렵다.               
- 스프링의 설정정보로부터 생성된 오브젝트와 정보는 애플리케이션 재시작 전에는 변경이 어렵다.             
- 싱글톤인 DAO의 인스턴스 변수에 접근해서 주입된 SQL 맵 오브젝트를 수정하는건 어렵고              
동시성 문제를 일으킬 수도 있다.                

이런 문제점을 해결하려면 DAO가 사용할 SQL을 제공해주는 기능을 독립시킬 필요가 있다.      
다음과 같은 기능을 가진 독립적인 SQL 제공 서비스가 필요하다.         



#### SQL 서비스 인터페이스        
가장 먼저 할 일은 SQL 서비스의 인터페이스를 설계하는 것이다.   
이런 경우 클라이언트인 DAO를 SQL 서비스의 구현에서 독립적으로 만들도록 인터페이스를 사용하고,   
DI로 구현 클래스의 오브젝트를 주입해주어야 한다.            
DAO가 사용할 SQL 서비스의 기능은 SQL에 대한 키 값을 전달하면 그에 해당하는 SQL을 돌려주는 것이다.      

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

이제 UserDao가 SqlService 타입의 구현 클래스로 정의된 빈을 DI 받도록 설정을 변경해줘야 한다.      



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

SimpleSqlService 클래스를 빈으로 등록하고 UserDao가 DI 받아 사용하도록 설정해준다.   
SQL 정보는 이 빈의 프로퍼티에 <map>을 이용해 등록한다.   

이제 DAO는 SQL을 어디에 저장해두고 가져오는지 신경쓰지 않아도 되고 sqlService 빈에는          
DAO에 전혀 영향을 주지 않은 채로 다양한 방법으로 구현된 SqlService 타입 클래스를 적용할 수 있다.          

<br />               


# 인터페이스의 분리와 자기참조 빈      

인터페이스가 하나 있으니 기계적으로 구현 클래스를 만들면 안된다.   
인터페이스로 대표되는 기능을 구현 방법과 확장 가능성에 따라 유연한 방법으로 재구성할 수 있도록 설계할 필요가 있다.           


## XML 파일 매핑       

스프링의 XML 설정파일에 SQL 정보를 함께 저장해두는 것보다는 SQL을 저장해두는 전용 포맷을 가진 독립적인 파일을 이용하는 것이 바람직하다.          
검색용 키와 SQL 문장 두 가지를 담을 수 있는 간단한 XML 문서를 설계해보고           
이 파일에서 SQL을 읽어뒀다가 DAO에게 제공해주는 SQL 서비스 구현 클래스를 만들어보자.           

#### XML SQL 서비스   
sqlmap.xml에 있는 SQL을 가져와 DAO에 제공해주는 SqlService 인터페이스의 구현 클래스를 만들어보자.   

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
   

SQL 문장을 스프링의 빈 설정에서 완벽하게 분리했다.          
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
스프링은 `@PostConstruct` 어노테이션을 빈 오브젝트의 초기화 메소드를 지정하는 데 사용한다.                                                                    

`@PostConstruct`를 초기화 작업을 수행할 메소드에 부여해주면 스프링은 XmlSqlService 클래스로 등록된 빈의 오브젝트를 생성하고   
DI 작업을 마친 뒤 `@PostConstruct`가 붙은 메소드를 자동으로 실행해준다.   

```
@PostConstruct
public void loadSql() { ... }
```

스프링 컨테이너의 초기 작업 순서는 다음과 같다.       

![스크린샷 2020-12-30 오전 12 01 03](https://user-images.githubusercontent.com/33855307/103293355-1cafc180-4a33-11eb-8656-ebd83964e3ee.png)         


<br />                


