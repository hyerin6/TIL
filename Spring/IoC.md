<br />  

### Spring IoC Container란?

Spring에서 객체들은 Bean으로 관리하는데 이 Bean은 일반적으로 싱글톤으로 관리된다. 

Spring의 주요 기술인 DI는 어떤 객체가 사용하는 의존 객체를 직접 생성하는 것이 아닌 

IoC Container로 부터 주입받아 사용하는 방법을 말한다. 

<br />
<br />


### BeanFactory, ApplicationContext란?
Spring의 IoC Container Interface이다. 

BeanFactory를 확장한 ApplicationContext을 주로 사용한다. 

<br />
<br />


### ApplicationContext와 다양한 빈 설정 방법 
Spring IoC Container의 역할

* 빈 인스턴스 생성 
* 의존 관계 설정 
* 빈 제공  

<br />
<br />


### ApplicationContext는 두 가지 방법으로 빈을 등록할 수 있다. 
(1) ClassPathXmlApplicationContext - XML 파일에 태그를 통해 빈 객체 직접 등록

(2) AnnotationConfigApplicationContext - `@Bean` 어노테이션 사용 

Spring Boot에서는 어노테이션으로 등록된 클래스에 한해 

ComponentScan을 통해 자동으로 빈을 등록해주기 때문에 위와 같은 빈 등록 방법은 사용하지 않는다. 

<br />
<br />


### Autowired 
* 3가지 DI 기술 중 하나 
* 여러 타입의 빈이 있는 경우 
    - `@Primary`를 사용하여 우선적으로 주입 받을 클래스를 지정할 수 있다. 
    - `@Qulifier`를 사용하여 클래스 이름으로 지정할 수 있다. 
    - List 타입으로 만들면 해당 타입의 빈들은 모두 리스트에 넣어둔다. 

<br />
<br />


### BeanPostProcessor란?

새로 만든 빈 인스턴스를 설정할 수 있는 라이프 사이클 인터페이스이다. 

Bean의 인스턴스를 만든 다음에 Bean에게 부가적인 작업을 수행할 수 있는 인터페이스인 것이다. 

부가적인 작업을 구현하는 방법은 `@PostContruct` 를 이용하는 방법과 

InitializingBean 인터페이스를 구현한 후 `afterPropertiesSet()` 메소드를 구현하는 방법이 있다. 

구현체로는 AutowiredAnnotationBeanPostProcessor가 있는데 이를 BeanPostProcessor가 상속받고 있다. 

AutowiredAnnotationBeanPostProcessor는 `@Autowired`, `@Value`, `@Inject` 어노테이션을 지원하는 어노테이션 처리기이다. 

<br />
<br />


### `@Component` & ComponentScan  
`@Component` 어노테이션으로 등록된 객체들을 찾아서 IoC 컨테이너에 빈으로 등록한다. 

컴포넌트 스캔에서 수정할 수 있는 부분은 다음과 같다. 

* 스캔 위치 설정 
* 필터: 어떤 어노테이션을 스캔할 것인지, 스캔하지 않을 것인지

`@ComponentScan` 어노테이션이 붙어있는 클래스와 같은 패키지 범위 내의 빈을 등록 가능하다. 

당연히 해당 어노테이션이 부착된 어노테이션 범위 밖의 클래스들은 ComponentScan의 범위를 벗어나기 때문에 빈 등록이 불가능하다. 

Singleton Scope의 Bean들은 모두 초기에 생성하므로 초기 구동시간이 오래걸릴 수 있다. 


<br />
<br />


### Functional한 빈 등록 방법  
* Application Context를 만들 때 빈 등록 가능하다.   

* 그러나 모든 빈들을 Functional하게 등록하는 것은 불편하다.  ( = ComponentScan의 등장 이유)

* `@Bean` 어노테이션으로 등록하는 빈의 경우라면 Functional하게 등록하는 것을 고려해볼만하다. 


```java
@SpringBootApplication
public class ExamApplication {
    @Autowired
    MyService myService;
    
    public static void main(String[] args) {
        // SpringApplication.run(ExamApplication.class, args);
        SpringApplication app = new SpringApplication(ExamApplication.class);
        app.addInitializers(new ApplicationContextInitializer<GenericApplicationContext>() {
            @Override
            public void initialize(GenericApplicationContext ctx) {
                ctx.registerBean(MyService.class);
            }        
        });
        app.run(args);
    }
}
```

functional한 방법이 성능상의 이점이 있긴 하지만 component-scan 방법을 이용하는 것이 

설정 파일을 줄일 수 있으며 일일이 빈을 등록해야 하는 번거로움을 줄여준다. 

<br />
<br />


### Bean Scope 
Bean은 일반적으로 Singleton Scope의 Bean만 사용해왔다.

그러나 때때로 특정한 Scope에 따라 객체를 만들어야할 수 있다.

프로토타입 Scope이 있는데, 매번 새로운 인스턴스를 만들어서 쓰는 Scope이다.

<br />

**프로토타입 빈이 싱글톤 빈을 참조** 

→ 문제 없음 

<br />

**싱글톤 빈이 프로토타입 빈을 참조**

→ 프로토타입 빈이 업데이트 안된다. 

→ 업데이트 하려면 scoped-proxy, Object-Provider, Provider

proxy 모드를 사용하면 클래스 기반의 CGlib를 사용한 Dynamic Proxy를 통해 매번 새로운 프로토타입 인스턴스를 사용할 수 있게 된다.   

<br />
<br />

### 싱글톤 객체 사용시 주의할 점

* 프로퍼티가 공유, Thread-safe한 처리를 해줘야한다. 

* Application Context 초기 구동시 한번 인스턴스 생성된다.

<br />

> [싱글톤으로 적합한 객체]  
> 
> (1) 상태가 없는 공유 객체: 상태를 가지고 있지 않은 객체는 동기화 비용이 없다. 
> 
> 따라서 매번 이 객체를 참조하는 곳에서 새로운 객체를 생성할 이유가 없다.  
> 
> 읽기용으로만 상태를 가진 공유 객체: 1번과 유사하게 상태를 가지고 있으나 읽기 전용이므로 여전히 동기화 비용이 들지 않는다. 
> 
> 매 요청마다 새로운 객체 생성할 필요가 없다.  
> 
> (2) 공유가 필요한 상태를 지닌 공유 객체: 객체 간의 반드시 공유해야 할 상태를 지닌 객체가 하나 있다면, 
> 
> 이 경우에는 해당 상태의 쓰기를 가능한 동기화 할 경우 싱글톤도 적합하다.  
> 
> (3) 쓰기가 가능한 상태를 지니면서도 사용빈도가 매우 높은 객체: 애플리케이션 안에서 정말로 사용빈도가 높다면, 
> 
> 쓰기 접근에 대한 동기화 비용을 감안하고서라도 싱글톤을 고려할만하다.   
> 
> 이 방법은 장시간에 걸쳐 매우 많은 객체가 생성될 때, 해당 객체가 매우 작은 양의 쓰기상태를 가지고 있을 때, 
> 객체 생성비용이 매우 클 때에 유용한 선택이 될 수 있다.
> 
> 
> <br />
>  
> [비싱글톤으로 적합한 객체] 
> 
> (1) 쓰기가 가능한 상태를 지닌 객체: 쓰기가 가능한 상태가 많아서 동기화 비용이 객체 생성 비용보다 크다면 싱글톤으로 적합하지 않다.  
> 
> (2) 상태가 노출되지 않은 객체: 일부 제한적인 경우, 내부 상태를 외부에 노출하지 않는 빈을 참조하여 
> 
> 다른 의존 객체와는 독립적으로 작업을 수행하는 의존 객체가 있다면   
> 
> 싱글톤보다 비싱글톤 객체를 사용하는 것이 더 나을 수 있다.  

<br />
<br />

## Reference
* [용어 정리 참고](https://hyerin6.github.io/2020-01-31/spring-DI-IoC/)
* [전략 패턴 참고](https://hyerin6.github.io/2020-06-13/template/)
* [Spring IoC/DI 란?](https://hyerin6.github.io/2021-10-06/ioc-di/)


<br />
