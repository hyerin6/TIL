# 서비스 추상화 적용  

JaxbXmlSqlReader는 더 개선할 점이 있다.         

1. 자바에는 JAXB 외에도 다양한 XML과 자바 오브젝트를 매핑하는 기술이 있다.                    
필요에 따라 다른 기술로 변경할 수 있어야 한다.                          

2. XML 파일을 좀 더 다양한 소스에서 가져올 수 있게 만들자.          
현재는 UserDao 클래스와 같은 클래스패스 안에서만 XML을 읽어올 수 있다.           
임의의 클래스패스나 위치 또는 HTTP 프로토콜을 통해 원격에서 가져오도록 확장하면 좋겠다.        

<br />        

## OXM 서비스 추상화      
XML과 자바 오브젝트를 매핑해서 상호 변환해주는 기술을 OXM이라고 한다.      
스프링은 트랜잭션, 메일 전송처럼 OXM에 대해서도 서비스 추상화 기능을 제공한다.      

#### OXM 서비스 인터페이스   
- 자바 오브젝트 -> XML 변환 : Marshaller       
- XML -> 자바 오브젝트 변환 : Unmarshaller     

SqlReader는 Unmarshaller를 사용하면 된다.     

<br />        

## OXM 서비스 추상화 적용    
스프링의 OXM 추상화 기능을 이용하는 SqlService를 만들어보자. -> OxmSqlService     

SqlRegistry는 DI받고 SqlReader는 스프링의 OXM 언마샬러를 이용하도록 OxmSqlService에 고정시켜야 한다.      
SQL을 읽는 방법을 OXM으로 제한해서 사용성을 극대화시키는게 OxmSqlService의 목적이다.        


#### 멤버 클래스를 참조하는 통합 클래스          
OxmSqlService는 SqlReader 타입의 의존 오브젝트를 사용하되   
이를 **스태틱 멤버 클래스로 내장**하고 자신만이 사용할 수 있도록 만들어보자.
의존 오브젝트를 자신만이 사용하도록 독점하는 구조로 만드는 방법이다.      

- SqlReader 구현을 외부에서 사용하지 못하도록 제한하고 스스로 최적화된 구조가 되었음                     
- 유연성은 조금 손해를 보지만 내부적으로 낮은 결합도를 유지한 채로 응집도가 높은 구현 방법                            
       
SqlReader 구현을 내장하고 있는 OxmSqlService의 구조는 다음과 같다.        
       
![스크린샷 2020-12-31 오후 5 08 18](https://user-images.githubusercontent.com/33855307/103401055-ceadd180-4b8a-11eb-8afd-f91ac4bd89d6.png)       


OxmSqlService와 OxmSqlReader는 구조적으로 강하게 결합되어 있지만 논리적으로 명확하게 분리되는 구조다.       
자바의 스태틱 멤버 클래스는 이런 용도로 쓰기에 적합하다.            


```  
public class OxmSqlService implements SqlService {
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    ...

    // private 멤버 클래스로 정의한다. 톱레벨 클래스인 OxmSqlService만이 사용할 수 있다.    
    private class OxmSqlReader implements SqlReader {
        ...
    }
}
```  


- OxmSqlReader는 private 멤버 클래스로 외부에서 접근, 사용이 불가능하다.   
- final로 선언하고 직접 오브젝트를 생성했기 때문에 OxmSqlReader를 DI하거나 변경할 수 없다.   

**Q.** 이렇게 두 클래스를 강하게 결합하고 더 이상의 확장이나 변경을 제한하는 이유는?                 
**A.** OXM을 이용하는 서비스 구조를 최적화하기 위해서이다.                    
하나의 클래스로 만들어두기 때문에 빈의 등록과 설정은 단순해지고 쉽게 사용할 수 있다.            


스프링의 OXM 서비스 추상화를 사용하면 언마샬러를 빈으로 등록해야 한다.        
SqlService를 위해 등록할 빈은 자꾸 늘어난다. 자꾸 늘어나는 빈의 개수와 반복되는 비슷한 DI 구조가 불편하게 느껴질 수 있다.        

빈의 개수를 줄이고 설정을 단순하게 하는 방법에는 BaseSqlService를 확장해서 디폴트 설정해 주는 방법이 있었다.   
이는 디폴트로 내부에서 만드는 오브젝트의 프로퍼티를 외부에서 지정해주기가 힘들다는 단점이 있다.   
프로퍼티를 또 디폴트로 지정할 수 있지만, OXM을 적용하는 경우 언마샬러를 비롯해 설정을 통해 DI 해줄 게 많기 때문에      
SqlReader 클래스를 단순한 디폴트 오브젝트 방식으로는 제공해줄 수 없다.          

이런 경우 하나의 빈 설정만으로 SqlService와 SqlReader의 필요한 프로퍼티 설정이 모두 가능하도록 만들 필요가 있다.        
즉 다음과 같은 방법을 사용하게 한다.        
     
- SqlService의 구현이 SqlReader의 구체적인 구현 클래스가 무엇인지도 알고있게 한다.        
- 자신의 프로퍼티를 통해 설정정보도 넘겨준다.          
- 멤버 클래스로 소유도 하고 있는 강한 결합 구조를 만든다.           


다음은 하나의 빈 설정으로 두 개의 오브젝트를 설정하는 구조이다.   

![스크린샷 2020-12-31 오후 5 38 47](https://user-images.githubusercontent.com/33855307/103402122-0dde2180-4b8f-11eb-8563-695528464286.png)     

OxmSqlService로 등록한 빈의 프로퍼티 일부는 OxmSqlService 내부의 OxmSqlReader 프로퍼티를 설정해주기 위한 창구 역할을 한다.    


```
public class OxmSqlService implements SqlService {
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    ...

    public void setUnmashaller(Unmashaller unmarshaller) { ... } // (1)

    public void setSqlmapFile(String sqlmapFile) { ... } // (2)

    private class OxmSqlReader implements SqlReader {

        private Unmashaller unmarshaller;
        private String sqlmapFile;
    
        // setter 생략 
        
        ...

    }

}
```

(1)과 (2)는 OxmSqlService의 공개된 프로퍼티를 통해 DI 받은 것을 그대로 멤버 클래스의 오브젝트에 전달한다.             
이 setter들은 단일 빈 설정구조를 위한 창구 역할을 할 뿐이다.               

다음은 완성된 OxmSqlservice 클래스다.     

```   
public class OxmSqlService implements SqlService { 
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    // setter 생략   

    // SqlService 인터페이스에 대한 구현 코드는 BaseSqlService와 같다.    
    @PostConstruct 
    public void loadSql() { this.oxmSqlReader.read(this.sqlRegistry); }
    
    private class OxmSqlReader implements SqlReader {
        private Unmashaller unmashaller;
        private final static String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
    
        . . .
    
        pubic void read(SqlRegistry sqlRegistry) {
            . . .
        }        
    }
    
}
```    

<br />       

#### 위임을 이용한 BaseSqlService의 재사용        
OxmSqlService는 SqlReader를 스태틱 멤버 클래스로 고정시켜서 OXM에 특화된 형태로 재구성했기 때문에   
설정은 간결해지고 의도되지 않은 방식으로 확장될 위험이 없다.   

남은 한가지 꺼림직한 부분은 loadSql()과 getSql()이라는 SqlService의 핵심 메소드 구현 코드가 BaseSqlService와 동일하다는 점이다.     

- BaseSqlService 코드를 재사용한다고 상속해서 OxmSqlService를 만들면?              
-> 멤버 클래스로 통합시킨 OxmSqlReader를 생성하는 코드가 애매해진다.             

- 중복 제거를 위해 loadSql()과 getSql() 메소드를 추출해서 슈퍼클래스로 분리하면?          
-> 아직은 간단한 코드라 복잡한 계층구조로 만들기도 부담스럽다.         
     
그래도 미래를 대비해서 의미있는 중복된 코드를 제거할 방법을 찾아보자.              
이런 경우 **위임 구조**를 이용해 코드의 중복을 제거할 수 있다.       
loadSql()과 getSql()의 구현 로직은 BaseSqlService에만 두고,       
OxmSqlService는 일종의 설정과 기본 구성을 변경해주기 위한 어댑터 같은 개념으로 BaseSqlService의 앞에 두는 설계가 가능하다.          

위임구조는 프록시를 만들 때 사용해봤다.          
위임을 위해서는 두 개의 빈을 등록하고 클라이언트의 요청을 직접 받는 빈이         
주요한 내용은 뒤의 빈에게 전달해주는 구조로 반들어야 한다.                   

그러나 OxmSqlService와 BaseSqlService를 위임구조로 만들기 위해 두 개의 빈을 등록하는 것은 불편하다.        
OxmSqlService와 BaseSqlService를 한 클래스로 묶는 방법을 생각해보자.    
OxmSqlService가 OxmSqlReader를 내장하고 있는 것과 마찬가지로   
다음과 같은 구조로 만들면 된다.          

![스크린샷 2021-01-01 오후 4 12 23](https://user-images.githubusercontent.com/33855307/103435003-27916e80-4c4c-11eb-92b2-2a6de93e7e34.png)           

OxmSqlService는 OXM 기술에 특화된 SqlReader를 멤버로 내장하고 있고,   
그에 필요한 설정을 한 번에 지정할 수 있는 확장구조만을 갖고 있다.   

실제 SqlReader와 SqlService를 이용해 SqlService의 기능을 구현하는 일은 내부에 BaseSqlService를 만들어서 위임할 것이다.   

```   
public class OxmSqlService implements SqlService {
    private final BaseSqlService baseSqlService = new BaseSqlService();

    ...

    @PostConstruct 
    public void loadSql(){
        // 실제 작업을 위임할 대상인 baseSqlService에 주입한다. 
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);

        this.baseSqlService.loadSql(); // SQL 등록하는 초기화 작업을 baseSqlService에 위임한다. 
    }

    public String getSql(String key) {
        // SQL을 찾아오는 작업도 baseSqlService에 위임한다.  
        return this.baseSqlService.getSql(key);
    }
}
```  


이렇게 위임구조로 OxmSqlService에 있던 중복 코드를 제거했다.         
SqlReader와 SqlRegistry를 활용해 SqlService를 제공하는 코드는 BaseSqlService에만 유일하게 존재한다.        


## 리소스 추상화         
지금까지 만든 OxmSqlService나 XmlSqlReader에는 공통적인 문제점이 있다.            
SQL 매핑 정보가 담긴 XML 파일 이름을 프로퍼티로 외부에서 지정할 수 있지만 UserDao 클래스와 같은 클래스패스에 존재하는 파일로 제한된다.     

자바에서 다양한 위치에 존재하는 리소스에 대해 단일화된 접근 인터페이스를 제공해주는 클래스가 없다.     
대신 URL을 이용해 웹 상의 리소스에 접근할 때 사용할 수 있는 java.net.URL 클래스가 있다.       

URL 클래스는 http, ftp, file과 같은 접두어(prefix)를 지정할 수 있어서 다양한 원격 리소스에 접근이 가능하다.   
그러나 다음과 같은 단점이 있다.   
- 자바의 클래스패스 안에 존재하는 리소스 또는 임의의 스트링으로 가져올 수 있는 리소스를 지정할 수 없다.    
- 리소스 파일의 존재 여부를 미리 확인할 수 없다.     
- 기존 OxmSqlReader의 구현 코드를 변경해야 한다. 리소스를 가져오려면 최종적으로 InputStream 형태로 변환해서 사용하겠지만,   
리소스 위치와 종류에 따라서 다른 클래스와 메소드를 사용해야 한다는 점이 불편하다.     

역시 목적은 동일하지만 사용법이 각기 다른 여러 가지 기술이 존재하는 것이라고 생각할 수 있고   
OXM과 마찬가지로 서비스 추상화를 적용할 수 있다.   

<br />             

#### 리소스       
스프링은 자바에서 존재하는 일관성 없는 리소스 접근 API를 추상화해서 Resource라는 추상화 인터페이스를 정의했다.    
참고 - <https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/resources.html>          
              
스프링의 거의 모든 API는 외부의 리소스 정보가 필요할 때는 항상 이 Resource 추상화를 이용한다.        

어떻게 임의의 리소스를 Resource 인터페이스 타입의 오브젝트로 가져올 수 있을까?     
다른 서비스 추상화의 오브젝트와는 달리 Resource는 스프링에서 빈이 아니라 값으로 취급된다.    
서비스를 제공해주는 것이 아니라 단순한 정보를 가진 값으로 지정된다.     

그래서 추상화를 적용하는 방법이 문제다. 빈으로 등록되면 구현 클래스를 지정해주면 되는데   
Resource는 빈으로 등록하지 않으니 외부에서 지정하는 방법은 <property>의 value 애트리뷰트에 넣는 방법밖에 없다.       
하지만 value에 넣을 수 있는 값은 문자열뿐이다.    

<br />             

#### 리소스 로더     
그래서 스프링에 URL 클래스와 유사하게 접두어를 이용해 Resource 오브젝트를 선언하는 방법이 있다.                
ResourceLoader는 접두어를 이용해 Resource 오브젝트를 선언한다.           
문자열 안에 리소스 종류, 위치가 함게 표현되어 그 문자열로 정의된 리소스를 실제 Resource 타입 오브젝트로 변환해준다.           

참고 - <https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/ResourceLoader.html>      


ResourceLoader의 대표적인 예는 바로 스프링의 애플리케이션 컨텍스트이다.          
애플리케이션 컨텍스트가 구현해야 하는 인터페이스인 ApplicationContext는 ResourceLoader 인터페이스를 상속하고 있다.          
애플리케이션 컨텍스트가 외부에서 읽어오는 모든 정보는 리소스 로더를 사용하게 되어있다.             

Resource 타입은 빈으로 등록하지 않고 <property> 태그의 value를 사용해 문자열로 값을 넣는데,       
이 문자열로 된 리소스 정보를 Resource 오브젝트로 변환해서 프로퍼티에 주입할 때도 애플리케이션 컨텍스트 자신이 리소스 로더로서 변환과 로딩 기능을 담당한다.          

만약 myFile이라는 이름의 프로퍼티가 Resource 타입이라고 하면, 다음과 같은 리소스 문자열을 사용할 수 있다는 뜻이다.             
순서대로 각각 클래스패스와 파일, 시스템, 웹 서버의 리소스를 지정하는 것이다.          

```     
<property name="myFile" value="classpath:com/epril/myproject/myfile.txt"  /> 
<property name="myFile" value="file:/data/myfile.txt"  /> 
<property name="myFile" value="http://www.myserver.com/test.dat"  /> 
```   


<br />          

#### Resource를 이용해 XML 파일 가져오기          
OxmSqlService에 Resource를 적용해서 SQL 매핑정보가 담긴 파일을 다양한 위치에서 가져오게 해보자.          

```  
public class OxmSqlService implements SqlService {
    public void setSqlmap(Resource sqlmap) { this.oxmSqlReader.setSqlmap(sqlmap); }

    ...

    private class OxmSqlReader implements SqlReader {
        private Resource sqlmap = new ClassPathResource("sqlmap.xml", UserDao.class);

        public void setSqlmap(Resource sqlmap) { this.sqlmap = sqlmap; }

        ...

    }
}
```  
   
Resource를 사용할 때는 Resource 오브젝트가 실제 리소스는 아니라는 점을 주의해야 한다.          

스프링의 리소스 추상화를 이용하면 리소스의 위치와 접근 방법에 독립적인 코드를 쉽게 만들 수 있다.         

       