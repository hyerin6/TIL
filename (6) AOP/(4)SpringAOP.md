# 스프링의 프록시 팩토리 빈         

트랜잭션을 다양한 방법으로 적용하면서 많은 문제점들이 있었는데   
스프링이 이를 어떻게 해결하는지 알아보자.   


## 1. `ProxyFactoryBean`             

스프링은 트랜잭션 기술과 메일 발송 기술에 적용했던 서비스 추상화를 프록시 기술에도 동일하게 적용하고 있다.            
자바에는 JDK에서 제공하는 다이내믹 프록시 외에도 편리하게 프록시를 만들 수 있도록 지원해주는 다양한 기술이 존재한다.     
 
따라서 스프링은 일관된 방법으로 프록시를 만들 수 있게 도와주는 추상 레이어를 제공한다.      
생성된 프록시는 스프링의 빈으로 등록돼야 한다.          
스프링은 프록시 오브젝트를 생성해주는 기술을 추상화한 팩토리 빈을 제공해준다.         
스프링의 `ProxyFactoryBean`은 프록시를 생성해서 빈 오브젝트로 등록하게 해주는 팩토리 빈이다.   
기존에 만들었던 `FactoryBean` 인터페이스를 구현한 `TxProxyFactoryBean`과 달리   
`ProxyFactoryBean`은 순수하게 프록시를 생성하는 작업만을 담당하고 프록시를 통해 제공해줄 부가기능은 별도의 빈에 둘 수 있다.    

`ProxyFactoryBean`이 생성하는 프록시에서 사용할 부가기능은 `MethodInterceptor` 인터페이스를 구현해서 만든다.   
`MethodInterceptor`는 `InvocationHandler`와 비숫하지만 한가지 다른 점이 있다.   
`InvocationHandler`의 `invoke()` 메소드는 타깃 오브젝트에 대한 정보를 제공하지 않는다.   
그래서 타깃은 `InvocationHandler`를 구현한 클래스가 직접 알고 있어야 한다.     
반면에 `MethodInterceptor`의 `invoke()` 메소드는 `ProxyFactoryBean`으로부터 타깃 오브젝트에 대한 정보까지도 함께 제공받는다.        
이 덕분에 `MethodInterceptor`는 **타깃 오브젝트에 상관없이 독립적**으로 만들어질 수 있다.    
따라서 `MethodInterceptor` 오브젝트는 **타깃이 다른 여러 프록시에서 함께 사용**할 수 있고, **싱글톤 빈으로 등록 가능**하다.    


```
public class DynamicProxyTest {
    @Test
    public void simpleProxy() {
        // JDK 다이내믹 프록시 생성 
        Hello proxiedHello = (Hello)Proxy.newProxyInstance(
            getClass().getClassLoader)(,
            new Class[] { Hello.class },
            new UppercaseHandler(new HelloTarget())
        );
    } 

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget()); // 타깃 설정 
        pfBean.addAdvice(new UppercaseAdvice()); // 부가기능을 담은 어드바이스를 추가, 여러 개를 추가할 수도 있다. 

        // FactoryBean이므로 getObject()로 생성된 프록시를 가져온다.  
        Hello proxiedHello = (Hello) pfBean.getObject();

        . . .
    }

    static class UppercaseAdvice implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Thowable { 
            // 리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없다.   
            // MethodInvocation은 메소드 정보와 함께 타깃 오브젝트를 알고 있기 때문인다. 
            String ref = (String)invocation.proceed();
            return ref.toUppercase(); // 부가기능 적용 
        }
    }
}
```         

<br />        

#### 어드바이스: 타깃이 필요 없는 순수한 부가기능          
`ProxyFactoryBean`을 적용한 코드를 기존의 JDK 다이내믹 프록시를 사용했던 코드와 비교해보면 몇 가지 눈에 띄는 차이점이 있다.         
`InvocationHandler`를 구현했을 때와 달리 `MethodInterceptor`를 구현한 `UppercaseAdvice`에 타깃 오브젝트가 등장하지 않는다.            
`MethodInterceptor`로 메소드 정보와 함께 타깃 오브젝트가 담긴 `MethodInvocation` 오브젝트가 전달된다.         
`MethodInvocation`은 타깃 오브젝트의 메소드를 실행할 수 있는 기능이 있기 때문에       
`MethodInterceptor`라는 부가기능을 제공하는 데만 집중할 수 있다.        

`MethodInvocation`은 일종의 콜백 오브젝트로 `proceed()` 메소드를 실행하면 타깃 오브젝트의 메소드를 내부적으로 실행해주는 기능이 있다.    
`MethodInvocation` 구현 클래스는 일종의 공유 가능한 템플릿처럼 동작하는 것이다.       

`ProxyFactoryBean`에 `MethodInterceptor`을 설정해줄 때는            
일반적인 DI 경우처럼 Setter를 사용하는 대신 `addAdvice()` 메소드를 사용한다는 점도 눈여겨봐야 한다.     
`ProxyFactorybean`에 여러 개의 `MethodInterceptor`를 추가할 수 있고   
`ProxyFactorybean` 하나만으로 여러 개의 부가기능을 제공해주는 프록시를 만들 수 있다는 뜻이다.   

그런데 `MethodInterceptor` 오브젝트를 추가하는 메소드 이름은 `addMethodInterceptor`가 아니라 `addAdvice`다.   
`MethodInterceptor`는 `Advice` 인터페이스를 상속하고 있는 서브인터페이스이기 때문이다.    
`MethodInterceptor` 처럼 타깃 오브젝트에 적용하는 부가기능을 담은 오브젝트를 스프링에서는 어드바이스라고 부른다.    

`ProxyFactoryBean`을 적용한 후에 없어진 것도 있다.           
`ProxyFactoryBean`을 적용한 코드에는 프록시가 구현해야 하는 Hello라는 인터페이스를 제공해주는 부분이 없다.          
다이내믹 프록시를 만들 때 오브젝트의 타입을 결정하기 위해 반드시 제공해야 하는 정보가 타깃 인터페이스였다.            
그런데 `ProxyFactoryBean`은 인터페이스를 굳이 알려주지 않아도 `ProxyFactoryBean`에 있는 인터페이스 자동검출 기능을 사용해         
타깃 오브젝트가 구현하고 있는 인터페이스 정보를 알아낼 수 있다.         

알아낸 인터페이스를 모두 구현하는 프록시를 만들어준다.   
타깃 오브젝트가 구현하는 인터페이스 중에서 일부만 프록시에 적용하기를 원할 때는 인터페이스 정보를 직접 제공해도 된다.   

어드바이스는 타깃 오브젝트에 종속되지 않는 순수한 부가기능을 담은 오브젝트이다.   

<br />     

#### 포인트컷: 부가기능 적용 대상 메소드 선정 방법          
예제에서 메소드 이름을 가지고 부가기능 적용 대상 메소드를 선정했다.   
그런데 `ProxyFactoryBean`과 `MethodInterceptor`에 그 로직을 넣을 수 없다.   
`MethodInterceptor` 오브젝트는 여러 프록시가 공유해서 사용할 수 있고, 메소드 이름 패턴은 프록시마다 다를 수 있기 때문이다.   

`MethodInterceptor`는 `InvocationHandler`와는 다르게 프록시가 클라이언트로부터 받는 요청을 일일이 전달받을 필요가 없다.         
`MethodInterceptor`에는 재사용 가능한 순수한 부가기능 제공 코드만 남기고 부가기능 적용 메소드를 선택하는 기능은 프록시에게 넘기자.       


다음은 JDK 다이내믹 프록시를 이용한 방식의 구조이다.   

![스크린샷 2020-12-24 오후 4 06 46](https://user-images.githubusercontent.com/33855307/103069654-25536280-4603-11eb-8138-9c09d2173657.png)   

부가기능 적용 대상 메소드를 선정할 수 있지만 `InvocationHandler`가 타깃과 메소드 선정 알고리즘 코드에 의존하고 있다.           


다음은 스프링의 `ProxyFacrotyBean` 방식이고 두 가지 확장 기능인 부가기능(Advice)와 메소드 선정 알고리즘(Pointcut)을 활용하는 유용한 구조이다.   

![스크린샷 2020-12-24 오후 4 17 06](https://user-images.githubusercontent.com/33855307/103069791-7b280a80-4603-11eb-9a59-ad62ef5e3e3c.png)

메소드 선정 알고리즘을 담고 있는 오브젝트를 포인트컷이라고 부른다.   
어드바이스와 포인트컷은 모두 프록시에 DI로 주입돼서 사용된다.         
두 가지 모두 여러 프록시에서 공유가 가능하도록 만들어지기 때문에 스프링의 싱글톤 빈으로 등록이 가능하다.          
     
<br />     

스프링의 `ProxyFacrotyBean` 방식은 다음과 같은 순서로 진행된다.   

1. 프록시는 클라이언트로부터 요청을 받으면 먼저 포인트컷에게 부가기능을 부여할 메소드인지 확인해달라고 요청한다.       
포인트컷은 `Pointcut` 인터페이스를 구현해서 만들면 된다.   
2. 프록시는 포인트컷으로부터 부가기능을 적용할 대상 메소드인지 확인받으면,        
`MethodInterceptor` 타입의 어드바이스를 호출한다.         
어드바이스는 JDK의 다이내믹 프록시의 `InvocationHandler`와 달리 직접 타깃을 호출하지 않는다.             
공유되서 사용되므로 타깃에 직접 의존하지 않는 템플릿 구조로 설계되어 있다.              
3. 어드바이스가 부가기능을 부여하는 중에 타깃 메소드의 호출이 필요하면           
프록시로부터 전달받은 `MethodInvocation` 타입 콜백 오브젝트의 `proceed()` 메소드를 호출해주기만 하면 된다.          
 
이렇게 재사용 사능한 기능을 만들어두고 바뀌는 부분만 외부에서 주입해서 이를 작업 흐름 중에 사용하도록 하는 것이 전형적인 템플릿/콜백 구조다.                 
어드바이스(`MethodInterceptor`)가 일종의 템플릿이 되고          
타깃 호출 기능을 갖고 있는 `MethodInvocation` 오브젝트가 콜백이 되는 것이다.       

프록시로부터 어드바이스와 포인트컷을 독립시키고 DI를 사용하게 한 것은 전형적인 전략패턴이다.   
덕분에 프록시와 `ProxyFactoryBean` 등의 변경 없이도 기능을 자유롭게 확장할 수 있는 OCP를 충실히 지키는 구조가 되었다.     

<br />     

다음은 포인트컷까지 적용한 `ProxyFactoryBean`이다.    

```
@Test
public void pointcutAdvisor() {
    ProxyFactoryBean pfBean = new ProxyFactoryBaen();
    pfBean.setTarget(new HelloTarget());
    
    NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    pointcut.setMappedName("sayH*");

    // 포인트컷과 어드바이스를 Advisor로 묶어서 한 번에 추가 
    pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvisor()));

    Hello proxiedHello = (Hello) pfBean.getObject();

    . . .
 
}
```

포인트컷과 어드바이스를 묶어서 따로 등록하는 이유는 `ProxyFactoryBean`에는 여러 개의 어드바이스와 포인트컷이 추가될 수 있기 때문이다.            
어떤 어드바이스에 대해 어떤 포인트컷을 적용할지 애매해지지 않게 묶어주는 것이다.           
이렇게 어드바이스와 포인트컷을 묶은 오브젝트를 인터페이스 이름을 따서 어드바이저라고 부른다.         

<br />     

## 2. `ProxyFactoryBean` 적용   

JDK 다이내믹 프록시의 구조를 그대로 이용해서 만들었던 `TxProxyFactoryBean`에 스프링이 제공하는 `ProxyFactoryBean`을 적용해보자.      
`ProxyFactoryBean`, `Advice`, `Pointcut`을 적용한 구조는 다음과 같다.     

![스크린샷 2020-12-24 오후 11 24 33](https://user-images.githubusercontent.com/33855307/103093911-328e4280-463f-11eb-9fc6-f283c4f54afb.png)   

<br />     


