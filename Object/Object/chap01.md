<br />        

# chap01 객체, 설계   

## 소극장 운영

<img width="817" alt="스크린샷 2021-07-14 오후 3 10 56" src="https://user-images.githubusercontent.com/33855307/125571903-690c347d-a5de-4d1b-9355-f402100beefb.png">    

<br />     

* 이벤트에 당첨된 관람객과 그렇지 못한 관람객은 다른 방식으로 입장시켜야 한다.
* 이벤트 당첨자가 아닌 경우에는 티켓을 구매해서 입장한다.
* 코드: <https://github.com/eternity-oop/object/tree/master/chapter01/src/main/java/org/eternity/theater/step01>    
  <br />

이 프로그램은 로직이 간단하고 예상대로 잘 동작하지만      
몇 가지 문제점을 가지고 있다.

<br />           
<br />      

# 무엇이 문제일까?

모듈이란 크기와 상관 없이 클래스나 패키지, 라이브러리와 같이 프로그램을 구성하는 임의의 요소를 의미한다.    
모듈은 다음 세 가지를 만족해야 한다.
* 제대로 동작해야 한다.
* 변경이 용이해야 한다.
* 이해하기 쉬워야 한다.   
  <br />

위 소극장 운영 프로그램은 필요한 기능이 오류 없이 수행되지만   
변경 용이성과 읽는 사람과의 의사소통에 만족하지 못한다.

<br />      
<br />   

### 예상에 빗나가는 코드

```   
(1) 소극장은 관람객의 가방에서 초대장이 있는지 확인       

(2) 가방안에 초대장이 있으면 매표소의 티켓을 관람객의 가방 안에 옮긴다.       

(3) 가방 안에 초대장이 없으면 현금을 꺼내 매표소에 적입한 후     
매표소에 보관돼 있는 티켓을 관람객의 가방 안에 옮긴다.     
```      

문제는 관람객과 판매원이 소극장의 통제를 받는 **수동적인 존재**라는 점이다.

* 소극장이라는 제 3자가 초대장을 확인하기 위해 관람객의 **가방을 마음대로 열어본다.**
* 소극장이 판매원의 허락 없이 매표소에 보관 중인 **티켓과 현금에 마음대로 접근**할 수 있다.
* 위 과정은 평소 상식과 다르기 때문에 코드를 읽는 사람과 제대로 **의사소통하지 못한다.**
* 하나의 클래스나 메서드에 너무 많은 세부사항을 다루기 때문에 코드를 작성하는 사람뿐만 아니라   
  코드를 읽고 이해하는 사람 모두에게 큰 부담을 준다.
* Audience(관람객)와 TicketSeller(판매원)를 변경할 경우 Theater(소극장)도 **함께 변경**해야 한다.


<br />        
<br />        


### 변경에 취약한 코드
관람객이 가방을 들고 있지 않다면?                  
현금이 아니라 신용카드를 이용해 계산한다면?
매표소 밖에서 티켓을 판매한다면?

가방을 들고 있다는 가정만 바뀌어도 Audience 클래스에서 Bag을 제거해야 할 뿐만 아니라      
Audience의 Bag에 직접 접근하는 Theater 역시 수정해야 한다.     
<br />

이것은 **객체 사이의 의존성(dependency)** 과 관련된 문제다.              
문제는 의존성이 **변경**과 관련돼 있다는 점이다.

그렇다고 해서 객체 사이의 의존성을 완전히 없애는 것이 정답은 아니다.    
애플리케이션의 기능을 구성하는 데 필요한 최소한의 의존성만 유지하고 불필요한 의존성을 제거하면 된다.

<br />     


<img width="932" alt="스크린샷 2021-07-14 오후 3 43 45" src="https://user-images.githubusercontent.com/33855307/125575673-0c923aca-cf7b-42f5-98cd-0cad2bdab675.png">     

<br />     

Theater은 너무 많은 클래스에 의존하고 있다.

객체 사이의 **의존성이 과한 경우**를 **결합도(coupling)** 가 높다고 말한다.        
결합도는 의존성과 관련돼 있기 때문에 결합도 역시 변경과 관련이 있다.        
두 객체 사이의 결합도가 높으면 높을수록 함께 변경될 확률도 높아지기 때문에 변경하기 어려워진다.         
설계의 목표는 객체 사이의 **결합도를 낮춰 변경이 용이한 설계**를 만드는 것이어야 한다.


<br />       
<br />        


# 설계 개선하기
변경과 의사소통이라는 문제가 서로 엮여있다.    
코드를 이해하기 어려운 이유는 Theater가 관람객의 가방과 판매원의 매표소에 직접 접근하기 때문이다.    
관람객과 판매원이 자신의 일을 스스로 처리해야 한다는 직관에서 벗어난다.

`직접 접근한다는 것 = Theater와 Audience와 TicketSeller에 결합`

해결방법은 간단하다.   
Theater가 가방과 매표소에 대해 세세한 부분까지 알지 못하도록 정보를 차단하면 된다.   
즉 관람객과 판매원을 자율적인 존재로 만들면 되는 것이다.


<br />      
<br />      

### 자율성을 높이려면?
첫 번째 단계는 Theater에서 매표소에 접근하는 모든 코드를 판매원 내부로 숨기는 것이다.

* Theater
```
public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        ticketSeller.sellTo(audience);
    }
}
```

<br />    


* TicketSeller
```
public void sellTo(Audience audience) {
         if (audience.getBag().hasInvitation()) {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().setTicket(ticket);
        } else {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
```


<br />   


이처럼 개념적이나 물리적으로 객체 내부의 세부적인 사항을 감추는 것을 캡슐화라고 부른다.    
**캡슐화의 목적은 변경하기 쉬운 객체를 만드는 것이다.**       
캡슐화를 통해 객체 내부로의 접근을 제한하면 객체와 객체 사이의    
결합도를 낮출 수 있기 때문에 설계를 좀 더 쉽게 변경할 수 있게 된다.


<br />        


Theater는 오직 TicketSeller의 인터페이스에만 의존한다.      
TicketSeller 내부에 TicketOffice 인스턴스를 포함하고 있다는 사실은 구현의 영역에 속한다.

객체를 인터페이스와 구현으로 나눠 인터페이스만 공개하는 것이      
객체 사이의 결합도를 낮추고 변경하기 쉬운 코드를 만드는 가장 기본적인 설계 원칙이다.

<br />        



<img width="863" alt="스크린샷 2021-07-25 오후 3 53 13" src="https://user-images.githubusercontent.com/33855307/126890519-ebd6b332-4028-47ce-b266-e5663e3104f7.png">   

수정 후 클래스 의존성은 위 그림과 같다.    
Theater에서 TicketOffice로의 의존성이 제거됐고 TicketSeller의 내부 구현이 성공적으로 캡슐화되었다.

그러나 Audience는 여전히 자율적인 존재가 아니다.   
Ticketseller가 Audience의 `getBag()` 메서드로 Audience 내부의 Bag 인스턴스에 직접 접근한다.

동일한 방법으로 Audience의 캡슐화를 개선해보자.   
Bag에 접근하는 모든 로직을 Audience 내부로 감춰야 한다.

<br />  

* Audience
```
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Long buy(Ticket ticket) {
        if (bag.hasInvitation()) {
            bag.setTicket(ticket);
            return 0L;
        } else {
            bag.setTicket(ticket);
            bag.minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }
}
```



<br />    


* TicketSeller
```
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        ticketOffice.plusAmount(audience.buy(ticketOffice.getTicket()));
    }
}
```

<br />      

<img width="945" alt="스크린샷 2021-07-25 오후 3 58 36" src="https://user-images.githubusercontent.com/33855307/126890634-67ad3cd7-347e-4d6e-8212-b57c4bd73c4f.png">    

<br />      

캡슐화 새건 결과 Audience와 TicketSeller 내부 구현을 외부에 노출하지 않고 자신의 문제를 스스로 책임지고 해결하도록 자율적인 존재로 만들었다.

<br />      
<br />      

### 무엇이 개선됐는가?
수정된 Audience와 TicketSeller는 자신이 가지고 있는 소지품을 스스로 관리한다.     
더 중요한 점은 Audience나 TicketSeller의 내부 구현을 변경하더라도 Theater를 함께 변경할 필요가 없어졌다는 것이다.

변경 용이성의 측면에서도 확실히 개선되었다.

<br />     
<br />     

### 캡슐화와 응집도
핵심은 객체 내부의 상태를 캡슐화하고 객체 간에 오직 메시지를 통해서만 상호작용하도록 만드는 것이다.    
Theater는 TicketSeller의 내부에 대해서는 전혀 알지 못한다.

밀접하게 연관된 작업만을 수행하고 연관성 없는 작업은 다른 객체에게 위임하는 객체를 가리켜 응집도(cohesion)가 높다고 말한다.    
자신의 데이터를 스스로 처리하는 자율적인 객체를 만들면 결합도를 낮출 수 있을 뿐더러 응집도를 높일 수 있다.

<br />     
<br />       

### 절차지향과 객체지향

* 절차지향   <br />

![KakaoTalk_Photo_2021-07-25-17-08-41 001](https://user-images.githubusercontent.com/33855307/126892296-ee1e5dfe-c23a-42d9-a41f-b72f3333a586.jpeg)

<br />   

맨 처음 작성된 코드에서 Theater의 enter 메서드 안에서       
Audience와 TicketSeller로부터 Bag과 TicketOffice를 가져와 관람객을 임장시키는 절차를 구현했다.         
모든 처리는 Theater의 enter 메서드안에 존재했었다는 점에 주목하자.


이 관점에서 Theater의 enter 메서드는 프로세스(process)이며,   
Audience, TicketSeller, Bag, TicketOffice는 데이터(data)이다.    
이처럼 프로세스와 데이터를 **별도의 모듈**에 위치시키는 방식을 **절차적 프로그래밍(Procedural Programming)** 이라고 한다.

<br />        

* 객체지향   <br />

![KakaoTalk_Photo_2021-07-25-17-08-41 002](https://user-images.githubusercontent.com/33855307/126892293-2ae3d228-e161-4c89-a184-5aaf9e514f2a.jpeg)


<br />      

절차적 프로그래밍은 프로세스가 필요한 모든 데이터에 의존해야 한다는 근본적인 문제점 때문에 변경에 취약할 수밖에 없다.      
해결 방법은 자신의 데이터를 스스로 처리하도록 프로세스의 적절한 단계를 Audience와 TicketSeller로 이동시키는 것이다.    
데이터와 프로세스가 **동일한 모듈 내부**에 위치하도록 프로그래밍하는 방식을
**객체지향 프로그래밍(Object-Oriented Programming)** 이라고 부른다.


<br />      
<br />      


### 더 개선할 부분
아직도 개선의 여지가 있다.    
Audience는 자율적인 존재다.

```
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Long buy(Ticket ticket) {
        if (bag.hasInvitation()) {
            bag.setTicket(ticket);
            return 0L;
        } else {
            bag.setTicket(ticket);
            bag.minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }
}
```


Audience(관람객)는 스스로 티켓을 구매하고 가방 안의 내용물을 직접 관리한다.    
하지만 Bag은 스스로 자기 자신을 책임지지 않고 Audience에 의해 끌려다니는 수동적인 존재다.

Bag도 내부 상태에 접근하는 모든 로직을 갭슐화해서 결합도를 낮추고     
관련된 상태와 행위를 함께 가지는 응집도 높은 클래스로 만들어보자.

```
public class Bag {
    private Long amount;
    private Ticket ticket;
    private Invitation invitation;

    public Long hold(Ticket ticket) {
        if (hasInvitation()) {
            setTicket(ticket);
            return 0L;
        } else {
            setTicket(ticket);
            minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }

    private void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    private boolean hasInvitation() {
        return invitation != null;
    }

    private void minusAmount(Long amount) {
        this.amount -= amount;
    }
}
```


Bag의 구현을 캡슐화시켰다.   
Audience를 Bag의 구현이 아닌 인터페이스에만 의존하도록 수정하자.

```
public class Audience {
    public Long buy(Ticket ticket) {
        return bag.hold(ticket);
    }
}
```


<br />       
<br />       

TicketSeller도 TicketOffice의 자율권을 침해한다.     
TicketSeller는 TicketOffice에 있는 Ticket을 마음대로 꺼내서 자기 멋대로 Audience에게 팔고    
Audience에게 받은 돈을 마음대로 TicketOffice에 넣어버린다.

TicketSeller가 TicketOffice의 구현이 아닌 인터페이스에만 의존하게 만들자.  
<br />

* TicketOffice
```
public class TicketOffice {
    public void sellTicketTo(Audience audience) {
        plusAmount(audience.buy(getTicket()));
    }
}
```


<br />   

* TicketSeller
```
public class TicketSeller {
    public void sellTo(Audience audience) {
        ticketOffice.sellTicketTo(audience);
    }
}
```

<br />    

TicketSeller와 TicketOffice의 변경은 만족스럽지 않다.

변경 전에는 TicketOffice가 Audience에 대해 알지 못했는데   
변경 후 TicketOffice가 Audience에게 직접 티켓을 판매하기 때문에 Audience에 관해 알고 있어야 한다.

<br />    

> 트레이드오프란?                                 
> 트레이드오프란 객체의 어느 한부분의 품질을 높이거나 낮추는게,                  
> 다른 부분의 품질을 높이거나 낮추는데 영향을 끼치는 상황을 이야기한다.                
> 일반적으로 한쪽의 품질을 높이면, 다른쪽의 품질은 떨어지는 방향으로 흐른다.

<br />    

트레이드오프의 시점이다.   
어떤 것을 우선해야 하는가?     
TicketOffice의 자율성보다는 Audience에 대한 결합도를 낮추는 것이 더 중요하다는 결론에 도달했다.

<br />   
<br />    

### 의인화
실생활의 관람객과 판매자가 스스로 자신의 일을 처리하기 때문에 코드에서도 스스로 책임지도록 개선했다.     
그러나 극장, 가방, 판매소는? 이들은 실세계에서는 자율적인 존재가 아니다.

누군가 소극장의 문을 열어줘야하고, 가방에서 돈을 꺼내는 것은 가방이 아니고 관람객이다.   
그럼에도 티켓이나 가방과 같은 무생물을 자율적인 존재로 취급했다.

현실에서는 수동적인 존재하고 하더라도 일단 객체지향에 들어오면 모든 것이 능동적이고 자율적인 존재로 바뀐다.    
이처럼 소프트웨어 객체를 설계하는 원칙을 의인화라고 부른다.


<br />   
<br />   

## 정리 
모든 모듈은 제대로 실행돼야 하고 변경에 용이하고 이해하기 쉬워야 한다.          

결합도를 낮추고 변경에 용이하게 설계해야 한다.       
자율성을 높여라. 객체 내부의 세부적인 사항을 감춰라.      
이를 캡슐화라고 한다.        

핵심은 객체 내부의 상태를 캡슐화하고 객체끼리 메시지를 통해서만 상호작용하도록 만드는 것.      
연관된 작업만 수행, 연관성 없는 작업은 다른 객체에게 위임 = 응집도가 높다.              
자율적인 객체를 만들면 결합도를 낮추고 응집도를 높일 수 있다.              

<br />

