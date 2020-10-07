# 의존관계 주입의 응용        

런타임 시에 사용 의존관계를 맺을 오브젝트를 주입해준다는 DI 기술의 장점은 무엇일까?    

코드에는 런타임 클래스에 대한 의존관계가 나타나지 않고, 인터페이스를 통해 결합도가 낮은 코드를 만드므로     
다른 책임을 가진 사용 의존관계에 있는 대상이 바뀌거나 변경되더라도 자신은 영향을 받지 않으며,       
변경을 통한 다양한 확장 방법에는 자유롭다.     


몇 가지 응용 사례를 생각해보자.    


<br />   


### 기능 구현의 교환   
실제 운영에 사용할 데이터베이스 연결 클래스가 `ProductionDBConnectionMaker`이고   
개발 중에 사용하는 로컬 DB를 연결 클래스가 `LocalDBConnectionMaker`인 경우   

DI 방식을 적용해서 만들었을 때, 모든 DAO는 생성 시점에 ConnectionMaker 타입의 오브젝트를 컨테이너로부터 제공받는다.    
구체적인 사용 클래스 이름은 컨테이너가 사용할 설정정보에 들어 있다.      


- 개발용 ConnectionMaker 생성 코드     

```
@Bean 
public ConnectionMaker connectionMaker() {
	return new LocalDBConnectionMaker(); 
}
```


- 운영용 ConnectionMaker 생성 코드       

```
@Bean 
public ConnectionMaker connectionMaker() {
	return new ProductionDBConnectionMaker();   
}
```

DAO가 100개이든 1000개이든 상관없이 위 코드만 수정해주면 된다.   


### 부가기능 추가   
DAO가 DB를 얼마나 많이 연결해서 사용하는지 파악해보자.   
DB 연결횟수를 카운팅하기 위해 모든 DAO의 makeConnection() 메소드를 호출하는 부분에 새로 카운터를 증가시키는 코드를 추가하고 분석 작업이 끝나면 코드를 제거해야 할까?         
그것은 엄청난 낭비이다.   
또한, DB 연결횟수를 세는 것은 DAO의 관심사항은 아니다.   

DI 컨테이너에서 아주 간단한 방법으로 가능하다.     
DAO와 DB 커넥션을 만드는 오브젝트 사이에 연결횟수를 카운팅하는 오브젝트를 하나 더 추가하는 것이다.    
컨테이너가 사용하는 설정정보만 수정해서 런타임 의존관계만 새롭게 정의해주면 된다.   

- 연결횟수 카운팅 기능이 있는 클래스 CountingConnectionMaker        
    
생성자를 보면 CountingConnectionMaker도 DI를 받는 것을 알 수 있다.   
CountingConnectionMaker의 오브젝트가 DI 받을 오브젝트도 역시 ConnectionMaker 인터페이스를 구현한 오브젝트다.  

```  
public Connection makeConnection() throws ClassNotFoundException, SQLException {
	this.counter++;
	return realConnectionMaker.makeConnection();  
}  
```  


- CountingConnectionMaker 의존관계가 추가된 DI 설정용 클래스    

```  
@Bean 
public ConnectionMaker connectionMaker() {
	return new CountingConnectionMaker(realConnectionMaker);
} 
```    

- CountingConnectionMaker에 대한 테스트 클래스     

```  
// DL(의존관계 검색)을 사용하면 이름을 이용해 어떤 빈이든 가져올 수 있다.    
CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);   
System.out.println("CountingConnectionMaker counter: " + ccm.getCounter());   
```  

DAO를 DL 방식으로 가져와 어떤 작업이든 여러 번 실행시킨다.   
그리고 CountingConnectionMaker 빈을 가져온다.   
설정정보에 지정된 이름과 타입만 알면 특정 빈을 가져올 수 있기 때문에 CountingConnectionMaker 빈을 가져올 수 있다.   


<br />     


# 정리     

사용자 정보를 DB에 등록하거나 아이디로 조회하는 기능을 가진 간단한 DAO 코드를 만들고, 그 코드의 문제점을 살펴보고 이를 다양한 방법과 패턴, 원칙, IoC/DI 프레임워크까지 적용해서 개선해봤다.   

그 과정을 정리해보자.   


- 먼저 책임이 다른 코드를 분리해서 두 개의 클래스로 만들었다. (관심사의 분리, 리팩토링)    

- 그중에서 바뀔 수 있는 쪽의 클래스는 인터페이스를 구현하도록 하고, 다른 클래스에서 인터페이스를 통해서만 접근하도록 만들었다. 이렇게 해서 인터페이스를 정의한 쪽의 구현 방법이 달라져 클래스가 바뀌더라도 그 기능을 사용하는 클래스의 코드는 같이 수정할 필요가 없도록 만들었다. (전략 패턴)   

- 이를 통해 자신의 책임 자체가 변경되는 경우 외에는 불필요한 변화가 발생하지 않도록 막아주고, 자신이 사용하는 외부 오브젝트의 기능은 자유롭게 확장하거나 변경할 수 있게 만들었다. (개방 폐쇄 원칙)   

- 결국 한쪽의 기능 변화가 다른 쪽의 변경을 요구하지 않아도 되게 했고(낮은 결합도), 자신의 책임과 관심사에만 순수하게 집중하는(높은 응집도) 깔끔한 코드를 만들 수 있다.   

- 오브젝트가 생성되고 여타 오브젝트와 관계를 맺는 작업의 제어권을 별도의 오브젝트 팩토리를 만들어 넘겼다. 또는 오브젝트 팩토리의 기능을 일반화한 IoC 컨테이너로 넘겨서 오브젝트가 자신이 사용할 대상의 생성이나 선택에 관한 책임으로부터 자유롭게 만들어줬다. (제어의 역전/IoC)    

- 전통적인 싱글톤 패턴 구현 방식의 단점을 살펴봤고 서버에서 사용되는 서비스 오브젝트로서의 장점을 살릴 수 있는 싱글톤을 사용하면서도 싱글톤 패턴의 단점을 극복할 수 있도록 설계된 컨테이너를 활용하는 방법을 알아봤다. (싱글톤 레지스트리)     

- 실제 시점과 코드에는 클래스와 인터페이스 사이의 느슨한 의존관계만 만들어놓고, 런타임 시에 실제 사용할 구체적인 의존 오브젝트를 제3자(DI 컨테이너)의 도움으로 주입받아서 다이내믹한 의존관계를 갖게 해주는 IoC의 특별한 케이스를 알아봤다. (의존관계 주입/DI)    

- 의존 오브젝트를 주입할 때 생성자를 이요하는 방법과 수정자 메소드를 이용하는 방법을 알아봤다. (생성자 주입 / setter 주입)   

- 마지막으로 XML을 이용해 DI 설정정보를 만드는 방법과 의존 오브젝트가 아닌 일반 값을 외부에서 설정해서 런타임 시에 주입하는 방법을 알아봤다.    




