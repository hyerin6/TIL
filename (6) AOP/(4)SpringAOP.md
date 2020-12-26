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



<details>  
<summary>ProxyFactoryBean 적용 코드</summary>  
<div markdown="1">  

- 트랜잭션 어드바이스       
```
public class TransactionAdvice implements MethodInceptor {
    PlatformTransactionManager tm;
    
    public void setTransactionManager(PlatformTransactionManager tm) {
        this.tm = tm;
    }

    // 타깃을 호출하는 기능을 가진 콜백 오브젝트를 프록시로부터 받는다.  
    public Object invoke(MethodInvocation incovation) throws Throwable {
        TransactionStatus status = this.tm.getTransaction(new FefaultTransactionDefinition());
        
        try {
            Object ref = invocation.proceed(); // 콜백 호출하여 타깃 메소드 실행 
            this.tm.commit(status);
            return ref;
        // JDK 다이내믹 프록시가 제공하는 Method와 달리 스프링의 MethodInvocation은 예외가 포장되지 않고 타깃에서 보낸 예외 그대로 전달한다.
        } catch(RuntimeException e) {  
            this.tm.rollback(status); 
            throw e;
        }
    }
}
```       

- 트랜잭션 어드바이스 빈 설정            
```       
<bean id="transactionAdvice" class="springbook.user.service.TransactionAdvice">
    <property neme="transactionManager" ref="transactionManager" />
</bean>
```        

- 포인트컷 빈 설정         
```       
<bean id="transactionPointcut" 
    class="org.springframework.aop.support.NameMatchMethodPointcut">
    <property neme="mappedName" value="upgrade*" />
</bean>
```       

- 어드바이저 빈 설정            
```   
<bean id="transactionAdvisor" 
    class="org.springframework.aop.support.DefaultPointcutAdvisor">
    <property neme="advice" ref="transactionAdvice" />
    <property neme="pointcut" ref="transactionPointcut" />
</bean>
```      

- ProxyFactoryBean 설정          
```   
<bean id="userService" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target" ref="userServiceImpl" />
    <property name="interceptorNames">
        <list>
            <value>transactionAdvisor</value>
        </list>
    </property>
</bean>
```   

프로퍼티에 타깃 빈과 어드바이저 빈을 지정해주면 된다.   
어드바이저는 interceptorNames라는 프로퍼티를 통해 넣는다.   
property 태그의 ref 애트리뷰트를 통한 설정 대신 list와 value 태그를 통해 여러 개의 값을 넣을 수 있다.    



- ProxyFactoryBean을 이용한 트랜잭션 테스트    
```
@Test
@DirtiesContext 
public void upgradeAllOrNothing() {
    TestUserService testUserService = new TestUserService(users.get(3).getId());
    testUserService.setUserDao(userDao);
    testUserService.setMailSender(mailSender);

    // userService 빈은 이제 스프링의 ProxyFactoryBean 이다. 
    ProxyFactoryBean txProxyFacrotyBean =   
        context.getBean("&userService", ProxyFactoryBean.class); 
    txProxyFacrotyBean.setTarget(testUserService);
    UserService txUserService = (UserService) txProxyFactoryBean.getObject();

    . . .

}
```

</div>
</details>


<br />          

# 스프링 AOP

## 1. 자동 프록시 생성    
부가기능 적용 과정에서 발견한 대부분의 문제는 제거했다.    
프록시 팩토리 빈 방식의 접근 방법의 한계라고 생각했던 두 문제 중   
부가기능이 타깃 오브젝트마다 만들어지는 문제는 스프링 `ProxyFactoryBean`의 어드바이스를 통해 해결했다.        
남은 문제점은 부가기능의 적용이 필요한 타깃 오브젝트마다 거의 비슷한 내용의 `ProxyFactoryBean` 빈 설정정보를 추가해줘야 한다는 것이다.         

<br />       

#### 중복 문제의 접근 방법      
이전 예제에서 DAO 메소드 마다 반복되는 `try/catch/finally`를 템플릿과 콜백, 클라이언트로 나누는 방법을 통해 분리했다.    
전략패턴과 DI를 적용한 덕분이다.   

이와 다른 방법으로 반복적으로 위임 코드가 필요한 프록시 클래스 코드를 해결한 적이 있다.      
타깃 오브젝트와 부가기능 적용을 위한 코드가 프록시가 구현해야 하는 모든 인티페이스의 메소드마다 반복적으로 필요헀는데     
다이내믹 프록시라는 런타임 코드 자동생성 기법을 이용해 해결했다.      
변하지 않는 타깃으로의 위임과 부가기능 적용 여부 판단 부분은 코드 생성 기법을 시용하는 다이내믹 프록시에 맡기고      
변하는 부가기능 코드 생성은 별도로 만들어서 다이내믹 프록시 생성 팩토리에 DI로 제공하는 방법이다.      

의미있는 부가기능 로직인 트랜잭션 경계설정은 코드로 만들게 하고     
기계적인 코드인 타깃 인터페이스 구현과 위임, 부가기능 연동 부분은 자동생성하게 한 것이다.      

`ProxyFactoryBean` 설정이 반복되는 문제는 설정 자동 등록 기법이나   
실제 빈 오브젝트는 `ProxyFactoryBean`을 통해 생성되는 프록시 그 자체니까   
프록시가 자동으로 빈으로 생성되게 할 수는 없을까?   
우선 지금까지 알아본 방법으로는 한 번에 여러 개의 빈에 프록시를 적용할 만한 방법은 없었다.      

<br />       

#### 빈 후처리기를 이용한 자동 프록시 생성기             
스프링은 컨테이너로서 제공하는 기능 중에서 변하지 않는 핵심적인 부분외에는 대부분 확장할 수 있도록 확장 포인트를 제공해준다.         
그중에서 관심 가질 만한 확장 포인트는 `BeanPostProcess` 인터페이스를 구현해서 만드는 빈 후처리기다.        
빈 후처리기는 스프링 빈 오브젝트로 만들어지고 난 후에, 빈 오브젝트를 다시 가공할 수 있게 해준다.         

빈 후처리기 중의 하나인 `DefaultAdvisorAutoProxyCreator`를 알아보자.             
`DefaultAdvisorAutoProxyCreator`는 어드바이저를 이용한 자동 프록시 생성기다.            
빈 후처리기를 스프링에 적용하는 방법은 빈 후처리기 자체를 빈으로 등록하는 것이다.          
스프링은 빈 후처리기가 빈으로 등록되어 있으면 빈 오브젝트가 생성될 때마다 빈 후처리기를 보내서 후처리 작업을 요청한다.           

빈 후처리기는 다음과 같은 작업을 할 수 있다.      
- 빈 오브젝트의 프로퍼티를 강제로 수정          
- 빈 오브젝트 별도의 초기화 작업     
- 만들어진 빈 오브젝트를 바꿔치기, 즉 스프링이 설정을 참고해서 만든 오브젝트가 아닌 다른 오브젝트를 빈으로 등록 가능          
   
이를 잘 이용하면 스프링이 생성하는 빈 오브젝트의 일부를 프록시로 포장하고, 프록시를 빈으로 대신 등록할 수도 있다.          
이것을 **자동 프록시 생성 빈 후처리기**라고 한다.         


다음은 빈 후처리기를 이용한 자동 프록시 생성 방법을 그림으로 나타낸 것이다.    

![스크린샷 2020-12-25 오후 11 52 58](https://user-images.githubusercontent.com/33855307/103137438-58354d80-470c-11eb-9885-48644f8c044a.png)      


1. `DefaultAdvisorAutoProxyCreator` 빈 후처리기가 등록되어 있으면 스프링은 빈 오브젝트를 만들 때마다 후처리기에 빈을 보낸다.   
2. `DefaultAdvisorAutoProxyCreator`는 빈으로 등록된 모든 어드바이저 내의 포인트컷을 이용해 전달받은 빈이 프록시 적용 대상인지 확인한다.  
3. 프록시 적용 대상이면 내장된 프록시 생성기에 현재 빈에 대한 프록시를 만들게 하고, 만들어진 프록시에 어드바이저를 연결한다.     
4. 빈 후처리기는 프록시가 생성되면 원래 컨테이너가 전달해준 빈 오브젝트 대신 프록시 오브젝트를 컨테이너에게 돌려준다.   
5. 컨테이너는 최종적으로 빈 후처리기가 돌려준 오브젝트를 빈으로 등록하고 사용한다.    


적용할 빈을 선정하는 로직이 추가된 포인트컷이 담긴 어드바이저를 등록하고   
빈 후처리기를 사용하면 일일리 `ProxyFactoryBean` 빈을 등록하지 않아도 타깃 오브젝트에 자동으로 프록시가 등록되게 할 수 있다.   
마지막 남은 번거로운 설정 문제를 해결했다.   

<br />   


#### 확장된 포인트컷    
지금까지 포인트컷은 타깃 오브젝트의 메소드 중에서 어떤 메소드에 부가기능을 적용할지 선정해주는 역할을 한다고 했다.                  
그런데 `DefaultAdvisorAutoProxyCreator` 적용 후 포인트컷이 등록된 빈 중에서 어떤 빈에 프록시를 적용할지 선택한다는 식으로 설명했다.                  
포인트컷은 두 가지 기능을 모두 갖고있다. `ClassFilter`와 `MethodMatcher` 두 가지를 리턴하는 메소드를 갖고 있다.                       
            
`ProxyFactoryBean`에서는 굳이 클래스 레벨의 필터는 필요 없었지만, 모든 빈에 대해 프록시 자동 적용 대상을 선별해야 하는                 
빈 후처리기인 `DefaultAdvisorAutoProxyCreator`는 클래스와 메소드 선정 알고리즘이 전부 있는 포인트컷이 필요하다.                   

<br />      


## 2. `DefaultAdvisorAutoProxyCreator` 적용           

#### 어드바이저를 이용한 자동 프록시 생성기 등록   
자동 프록시 생성기는 등록된 빈 중에서 Advisor 인터페이스를 구현한 것을 모두 찾는다.  
그리고 생성되는 모든 빈에 대해 어드바이저의 포인트컷을 적용해보면서 프록시 적용 대상을 선정한다.   
적용 대상이라면 원해 빈 오브젝트는 프록시 뒤에 연결돼서 프록시를 통해서만 접근이 가능하게 바뀐다.          
따라서 타깃 빈에 의존한다고 정의한 다른 빈들은 프록시 오브젝트를 대신 DI 받게 될 것이다.   

#### 어드바이스와 어드바이저   
어드바이저와 어드바이스는 수정할게 없다.   
하지만 이제 `ProxyFactoryBean`으로 등록한 빈에서처럼 어드바이저를 명시적으로 DI하는 빈은 존재하지 않는다.    
대신 자동 프록시 생성기에 의해 자동 수집되고, 프록시 대상 선정 과정에 참여한다.    
자동생성된 프록시에 다이내믹하게 DI돼서 동작하는 어드바이저가 된다.   

#### 자동 프록시 생성기를 사용하는 테스트       
`@Autowired`를 통해 컨텍스트에서 가져오는 `UserService`타입 오브젝트는 `UserServiceImpl` 오브젝트가 아니라 트랜잭션이 적용된 프록시여야 한다.    
지금까지는 `ProxyFactoryBean`이 빈으로 등록되어 있었으므로 이를 가져와 타깃을 테스트용 클래스로 바꿔치기하는 방법을 사용했다.   
하지만 자동 프록시 생성시를 적용하면 더 이상 가져올 `ProxyFactoryBean` 같은 팩토리 빈이 존재하지 않는다.   
자동 프록시 생성기가 알아서 프록시를 만들어줬기 때문에 프록시 오브젝트만 남아 있을 뿐이다.   

그럼 어떻게 테스트해야 할까?                
예외상황에 대한 테스트는 테스트 코드에서 빈을 가져와 수동 DI로 구성을 바꿔서 사용했는데             
자동 프록시 생성기라는 스프링 컨테이너에 종속적인 기법을 사용했기 때문에 예외상황을 위한 테스트 대상도 빈으로 등록해줄 필요가 있다.              
이제는 타깃을 코드에서 바꿔치지할 방법이 없고 자동 프록시 생성기의 적용이 되는지도 빈을 통해 확인할 필요가 있기 때문이다.               



<br />     


## 3. 포인트컷 표현식을 이용한 포인트컷         
지금까지 사용했던 포인트컷은 메소드의 이름과 클래스의 이름 패턴을 각각 클래스 필터와 메소드 매처 오브젝트로 비교해서 선정하는 방식이었다.       
그러나 더 복잡하고 세밀한 기준을 이용해 클래스나 메소드를 선정하게 하려면 어떻게 해야 할까?    

스프링은 아주 간단하고 효과적인 방법으로 포인트컷의 클래스와 메소드를 선정하는 알고리즘을 작성할 수 있는 방법을 제공한다.    
정규식이나 JSP의 EL과 비슷한 일종의 표현식 언어를 사용해서 포인트컷을 작성할 수 있도록 하는 방법이다.   
이것을 포인트컷 표현식이라고 한다.   

#### 포인트컷 표현식 (Pointcut expression)     
포인트컷 표현식을 지원하는 포인트컷을 적용하려면 `AspectJExpressionPointcut` 클래스를 사용하면 된다.                 
`AspectJExpressionPointcut`은 클래스와 메소드의 선정 알고리즘을 포인트컷 표현식을 이용해 한 번에 지정할 수 있게 해준다.         
     
스프링이 사용하는 포인트컷 표현식은 AspectJ라는 프레임워크에서 제공하는 것을 가져와 일부 문법을 확장해서 사용하는 것이다.  
그래서 이를 AspectJ 포인트컷 표현식이라고도 한다.                  

<br />               

# Transaction 적용 정리   

### 1번 방법 : 데코레이터 패턴으로 트랜잭션 적용        
부가기능을 타깃과 같은 인터페이스를 구현하도록 하고 핵심기능은 타깃 오브젝트로 위임한다.    

**문제점**          
- 타깃의 인터페이스를 구현하고 위임하는 코드가 번거롭다.   
부가기능을 적용하지 않아도 되는 메소드까지 전부 구현해야 한다.   
- 부가기능 코드가 중복된다. 특히 트랜잭션 같은 경우 DB 사용 로직에 적용될 가능성이 큰데 일일이 다 구현해야 한다.   


**참고**   
프록시 패턴 - 타깃에 접근하는 방법 제어    
데코레이터 패턴 - 타깃에 부가기능을 부여해주기 위함    



### 2번 방법 : JDK의 다이내믹 프록시 직접 사용     
프록시 팩토리에 의해 런타임 시에 다이내믹하게 다이내믹 프록시 오브젝트를 만든다.   
프록시 팩토리는 `Proxy` 클래스의 `newProxyInstance()` 스태틱 팩토리 메소드로 생성하는 것을 말한다.   

타깃 오브젝트에 위임하는 것과 부가기능 제공은 `InvocationHandler`를 구현한 클래스로 구현한다.   

클라이언트가 프록시 팩토리에 프록시를 요청하면 프록시 팩토리가 다이내믹 프록시 오브젝트를 생성하고   
클라이언트는 그 오브젝트를 (타깃과 같은 타입의) 원하는 기능을 가진 메소드를 호출한다.   

다이내믹 프록시가 원하는 부가기능과 핵심기능의 순서가 적절하게 조합되어 있는 Handler의 메소드에 처리를 요청한다.       
핵심기능을 타깃에 위임하는 Handler의 리턴 값은 다이내믹 프록시 오브젝트가 받는다.       


**문제점**   
- Handler와 다이내믹 프록시를 스프링 DI로 사용하고 싶은데 스프링 빈으로 등록할 수 없다.   
다이내믹 프록시는 `Proxy` 클래스의 `newProxyInstance()` 스태틱 팩토리 메소드로만 생성이 가능하다.   


### 3번 방법 : FactoryBean    
`FactoryBean` 인터페이스를 구현한 클래스는 스프링의 빈으로 등록하면 Spring DI가 가능하다.    

팩토리 빈과 타깃 오브젝트만 스프링 빈으로 등록한다.    
팩토리 빈이 다이내믹 프록시 오브젝트를 생성하는 메소드를 갖고있기 때문에 Spring DI가 가능해졌다.   
 
```      
                 (프로퍼티)
            팩토리 빈 -> 타깃 오브젝트
          (생성)↓    ↘ (생성)
Client -> 다이내믹 프록시 -> Handler
```     


**문제점**       
- Handler 오브젝트가 프록시 빈 개수만큼 생성된다.       
- 하나의 타깃에 여러 개의 부가기능을 적용하려면 설정 파일이 너무 길어지고 비슷한 설정이 반복된다.           
- 한번에 하나의 클래스안에 여러 메소드에 부가기능 적용은 가능하지만 여러 클래스에 부가기능을 적용하는 것은 불가능하다.         



### 4번 방법 : Spring의 ProxyFactoryBean 사용   
부가기능을 부여하는 Advice가 템플릿, 타깃 호출 기능을 갖고있는 `MethodInvocation` 오브젝트가 콜백이 되는 전형적인 템플릿/콜백 구조이다.     
부가기능이 타깃 오브젝트마다 만들어지는 문제는 해결이 되었다.      

**문제점**    
- 부가기능의 적용이 필요한 타깃 오브젝트마다 거의 비슷한 내용의 `ProxyFactoryBean`의 빈 설정정보를 추가해줘야 한다.       


### 5번 방법 : 빈 후처리기를 이용한 자동 프록시 생성기 적용           
`DefaultAdvisorAutoProxyBean`는 어드바이저를 이용한 자동 프록시 생성기다.   
빈 후처리기를 스프링 빈으로 등록해서 사용하면 된다.   

1. 스프링이 빈 오브젝트를 만들 때마다 후처리기에 빈을 보낸다.          
2. `DefaultAdvisorAutoProxyBean`가 포인트컷에 프록시 적용 대상인지 확인하고              
적용 대상이면 해당 빈에 대한 프록시를 생성하고 생성된 프록시에 어드바이저를 연결한다.              
3. 원래 컨테이너가 전달해준 빈 대신 만들어낸 프록시 오브젝트를 컨테이너에 전달한다.              
4. 컨테이너는 최종적으로 빈 후처리기가 돌려준 오브젝트를 빈으로 등록하고 사용한다.            

적용할 빈을 선정하는 로직이 추가된 포인트컷이 담긴 어드바이저를 등록하고 빈 후처리기를 사용하면   
일일이 `ProxyFactoryBean` 빈을 등록하지 않아도 타깃 오브젝트에 자동으로 프록시가 적용되게 할 수 있다.   
번거로운 `ProxyFactoryBean` 설정 문제도 해결했다.  

