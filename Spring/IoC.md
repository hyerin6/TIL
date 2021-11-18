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


## Reference
* [용어 정리 참고](https://hyerin6.github.io/2020-01-31/spring-DI-IoC/)
* [전략 패턴 참고](https://hyerin6.github.io/2020-06-13/template/)
* [Spring IoC/DI 란?](https://hyerin6.github.io/2021-10-06/ioc-di/)


<br />
