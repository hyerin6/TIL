# 스프링 3.1의 DI      

## 자바 언어의 변화와 스프링       
스프링이 제공하는 모든 기술의 기초가 되는 DI의 원리는 변하지 않았지만,                 
DI가 적용된 코드를 작성할 때 사용하는 핵심 도구인 자바 언어에는 그간 적지 않은 변화가 있었다.         
이런 변화들이 DI 프레임워크로서 스프링의 사용 방식에도 여러 가지 영향을 줬다. 대표적인 두 가지 변화를 살펴보자.              

### - 어노테이션의 메타정보 활용             
자바는 소스코드가 컴파일된 후 클래스 파일에 저장됐다가 JVM에 의해 메모리로 로딩되어 실행된다.       
그런데 때로는 자바 코드가 실행되는 것이 목적이 아니라 다른 자바 코드에 의해 데이터처럼 취급되기도 한다.                      

자바 코드의 일부를 리플렉션 API 등을 이용해 어떻게 만들었는지 살펴보고 그에 따라 동작하는 기능이 점점 많이 사용되고 있다.            
원래 리플렉션 API는 자바 코드나 컴포넌트를 작성하는 데 사용되는 툴을 개발할 때 이용하도록 만들어졌는데,        
언제부턴가 본래 목적보다는 자바 코드의 메타정보를 데이터로 활용하는 스타일의 프로그래밍 방식에 더 많이 활용되고 있다.               

이런 프로그래밍 방식의 절정은 자바 5에서 등장한 어노테이션일 것이다.              
자바 클래스나 인터페이스, 필드, 메서드 등은 그 자체로 실행 가능하고, 상속하거나 참조하거나 호출하는 방식 등으로 직접 이용할 수 있다.        
반면에 어노테이션은 기존의 자바 프로그래밍 방식으로는 활용할 수가 없다.           
노테이션은 옵션에 따라 컴파일된 클래스에 존재하거나 어플리케이션이 동작할 때 메모리에 로딩되기도 하지만 자바 코드가 실행되는 데 직접 참여하지 못한다.          
복잡한 리플렉션 API를 이용해 어노테이션의 메타정보를 조회하고, 어노테이션 내에 설정된 값을 가져와 참고하는 방법이 전부다.                

그럼에도 어노테이션의 활용이 늘어난 이유는 무엇일까?           
어노테이션은 어플리케이션 핵심 로직을 담은 자바 코드와 이를 지원하는 IoC 방식의 프레임워크,                
그리고 프레임워크가 참조하는 메타정보라는 세 가지로 구성하는 방식에 잘 어울리기 때문일 것이다.          
어노테이션은 XML이나 여타 외부 파일과 달리 자바 코드의 일부로 사용된다.                
코드의 동작에 직접 영향을 주지는 못하지만 메타정보로서 활용되는 데는 XML에 비해 유리한 점이 많다.                

```  
@Special 
public class MyClass {
    ...
}
```   

위의 코드처럼 어노테이션 하나 추가함으로써 클래스의 패키지, 클래스 이름, 접근 제한자, 상속한 클래스나 구현 인터페이스가 무엇인지 알 수 있다.      
원한다면 클래스의 필드나 메서드 구성도 확인 할 수 있다.        

반면에 동일한 정보를 XML로 표현하려면 모든 내용을 명시적으로 나타내야 한다.  

```  
<x:special target="type" class="com.mycompany.myproject.MyClass" />
```  

어노테이션 하나를 자바 코드에 넣는 것에 비해 작성할 정보의 양이 많다.            
물론 어노테이션에도 단점이 있다. 자바 코드에 존재하므로 변경할 때마다 매번 클래스를 새로 컴파일 해줘야 한다.       

<br />    

### - 정책과 관례를 이용한 프로그래밍        
어노테이션은 작성하는 코드의 양에 비해 부가적으로 얻을 수 있는 정보가 많기 때문에 일정한 패턴을 따르는 경우        
관례를 부여해 명시적인 설정을 최대한 배제하면 애플리케이션의 코드가 매우 간략해진다.       

`@Transactional`을 생각해보면 클래스, 인터페이스 각각의 메서드를 포함해 여러 위치에 적용이 가능하다. 이때 스프링은 우선순위를 가진 대체 정책을 정해놨다.        

스프링은 점차 어노테이션으로 메타정보를 작성하고, 미리 정해진 정책과 관례를 활용해서 간결한 코드에 많은 내용을 담을 수 있는 방식을 적극 도입하고 있다.           

<br />    

### 프로파일 `@Profile` `@ActiveProfiles`         
프로파일이 지정되어 있지 않은 빈 설정은 default 프로파일로 취급되어 항상 적용된다.              
`@Profile`이 붙은 설정 클래스는 `@Import`로 가져오든 `@ContextConfiguration`에 직접 명시하든 상관없이,     
현재 컨테이너의 활동 프로파일 목록에 자신의 프로파일 이름이 들어 있지 않으면 무시된다.  

```  
@Autowired
DefaultListableBeanFactory bf;

@Test
public void beanTest() throws Exception {
    for (String s : bf.getBeanDefinitionNames()) {
        System.out.println(bf.getBean(s).getClass().getName());
    }
}
```  

스프링 컨테이너는 모두 BeanFactory라는 인터페이스를 구현하고 있다.       
`BeanFactory`의 구현 클래스 중에 `DefaultListableBeanFactory`가 있는데 거의 대부분의 스프링 컨테이너는 이 클래스를 이용해 빈을 등록하고 관리한다.      

    
<br />    

### `@PropertySource`      
스프링 3.1은 빈 설정 작업에 필요한 프로퍼티 정보를 컨테이너가 관리하고 제공해준다.   
스프링 컨테이너가 지정된 정보 소스로부터 프로퍼티 값을 수집하고, 이를 빈 설정 작업 중에 사용할 수 있게 해준다.   
컨테이너가 프로퍼티 값을 가져오는 대상을 프로퍼티 소스라고 한다.  
디폴트로 프로퍼티 정보를 끌어오는 프로퍼티 소스도 있고, 위치를 지정해서 사용되는 프로퍼티 소스도 있다.  

```  
@PropertySource("/database.properties")
public class AppContext {
```    

@PropertySource로 등록한 리소스로부터 가져오는 프로퍼티 값은 컨테이너가 관리하는 Environment 타입의 환경 오브젝트에 저장된다.            
환경 오브젝트는 빈처럼 @Autowired를 통해 필드로 주입받을 수 있다.            
주입받은 Environment 오브젝트의 getProperty() 메서드를 이용하면 프로퍼티 값을 가져올 수 있다.            

```  
@Autowired
Environment env;
   
@Bean
public DataSource dataSource() {
    SimpleDriverDataSource ds = new SimpleDriverDataSource();
    ds.setDriverClass((Class<? extends java.sql.Driver>)Class.forName(env.getProperty("db.driverClass")));
    ds.setUrl(env.getProperty("db.url"));
    ...
}
```  

<br />    

### PropertySourcesPlaceholderConfigurer     
Environment 오브젝트 대신 프로퍼티 값을 직접 DI 받는 방법도 가능하다. `@Value` 어노테이션을 이용하면 된다.     

```    
@PropertySource("/database.properties")
public class AppContext {
    @Value("${db.driverClass}")
    Class<? extends Driver> driverClass;

    @Value("${db.url}")
    String url;

    ...
}
```   

`@Value`와 치환자를 이용해 프로퍼티 값을 필드에 주입하려면 PropertySourcesPlaceholderConfigurer 빈을 선언해줘야 한다.          
빈 팩토리 후처리기로 사용되는 빈을 정의해주는 것인데 이 빈 설정 메서드는 반드시 스태틱 메서드로 선언해야 한다.          


```
@Bean
public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
}
```

<br />    

### Enable* 어노테이션    
`@Component`는 빈 자동등록 대상을 지정할 때 사용하는 어노테이션인데,         
많은 경우 `@Component`를 직접 사용하기보다는 `@Service`, `@Repository`처럼 좀 더 의미있는 이름의 어노테이션을 만들어 사용한다.      
비슷한 방식으로 `@Import`도 다른 이름의 어노테이션으로 대체 가능하다.      

```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TransactionManagementConfigurationSelector.class})
public @interface EnableTransactionManagement {
```

`@EnableTransactionManagement` 어노테이션도 `@Import`를 메타 어노테이션으로 갖고 있다.      
`@EnableTransactionManagement`를 사용한다는 것은 결국 TransactionManagementConfigurationSelector 설정 클래스를 `@Import` 하는 셈이다.      

<br />       

### 설정 메타데이터의 장단점      

| |XML|Annotation|JavaConfig|
|:---|:---|:---|:---|
|장점|사용자가 작성하는 POJO 자바 코드에 침투하지 않음(비침투적 기술)|설정이 적고, 간편함|타입 세이프(type-safe)를 보장함|
|단점|리소스 파일(XML)이 많아짐|- 애노테이션이 붙은 클래스들은 POJO가 아님(침투적인 기술) <br /> - 설정이 분산되어서 제어하기 어려움(컴포넌트 스캐닝을 사용하기 위해서는 반드시 XML 또는 JavaConfig를 통해서 활성화 해야 함) <br /> - 사용자가 만든 클래스만 스프링 빈으로 등록할 수 있음(라이브러리에서 제공하는 클래스를 스프링 빈으로 등록할 수 없음)|일부 기능은 제공하지 않을 수 있음|

 