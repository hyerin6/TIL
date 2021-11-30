<br />

# MySQL   
[mysql architecture](https://github.com/hyerin6/TIL/blob/main/MySQL/real-mysql/architecture/mysql_architecture.md)    

서버 엔진은 클라이언트의 요청을 받아 SQL을 처리하는 DB 자체의 기능적인 역할을 담당한다.   
스토리지 엔진은 서버 엔진이 필요한 데이터를 물리 장치에서 가져오는 역할을 한다.

<br />

### 1) 서버 엔진   
서버 엔진은 사용자가 쿼리를 날렸을 때, DB가 SQL을 이해할 수 있도록 쿼리를 재구성하는 쿼리 파싱과 디스크 메모리 같은    
물리적인 저장장치와 통신하는 스토리지 엔진에 데이터를 요청하는 업무를 담당한다.   

스토리지 엔진에서 받아온 데이터를 사용자 요청에 맞게 처리하거나 접근제어, 쿼리캐시, 옵티마이저 등의 역할을 수행한다.   

<br />


### 2) 스토리지 엔진   
스토리지 엔진은 물리적인 저장장치에서 데이터를 읽어오는 역할을 담당한다.    
MySQL은 다른 DBMS와 다르게 스토리지 엔진이 플러그인 방식으로 동작한다.   

<br />

# MySQL에서 스토리지 엔진이란?  
MySQL은 다양한 스토리지 엔진을 제공한다.   
스토리지 엔진별 특성을 이해하고 적재적소에 적용할 수 있다면 성능, 효율을 높일 수 있다.   

<br />

### [1) MyISAM](https://github.com/hyerin6/TIL/blob/main/MySQL/real-mysql/architecture/InnoDB_architecture.md)   
* 파일 기반 스토리지 엔진이다.   
* 데이터에 대한 키, 즉 인덱스만 메모리에 올려서 처리한다. 데이터는 디스크에서 바로 접근한다.
* 트랜잭션을 지원하지 않고 테이블 단위 잠금으로 데이터 변경을 처리한다.    
    - 따라서 특정 테이블에 여러 세션에서 데이터를 변경하면 성능이 저하된다.


위와 같은 특징이 있으나 MyISAM이 다른 스토리지 엔진에 비해 성능이 떨어진다는 것은 아니다.   

* 풀텍스트 인덱싱 
* 지오메트릭 스파셜 인덱싱 

위와 같은 기능을 제공하기도 하고 다양한 기법으로 데이터 사이즈를 줄이기도 한다.   
그 중 대표적인 방법이 프리픽스 인덱스 압축 기법으로 key 사이즈를 최소화하는 것이다.   

> 프리픽스 인덱스 압축 기법      
> 인덱스 첫 번째 값은 통째로 저장하고 그 이후 변경 값을 차례로 인덱싱하는 방식이다.        
> ex) Perform, Performance          
> (1) Perform 저장   
> (2) 중복된 글자 수 `7` 저장 후 나머지 데이터 `ance` 저장          

프리픽스 인덱스 압축으로 key 사이즈는 줄고 메모리 효율은 높아진다.   
그러나 키 역순으로 데이터를 찾아야 할 때 성능 이슈가 있다.   
원하는 데이터를 찾으려면 이전 단계에 저장된 인덱스 내용이 필요하다.   
로그 수집, 단순 SELECT에는 적합하나 동시 데이터 처리에는 한계가 있다.   

<br />

### [2) InnoDB 스토리지 엔진](https://github.com/hyerin6/TIL/blob/main/MySQL/real-mysql/architecture/MyISAM_architecture.md)      
* 다중 버전 동시성 제어 메커니즘을 제공  
* 행 단위 잠금으로 데이터 변경 작업을 수행
* 인덱스와 데이터를 모두 메모리에 올린다.   
    - 메모리 버퍼 크기가 DB 성능에 큰 영향을 미친다.
* Primary Key는 클러스터 인덱스로 구성된다.

<br />

# MySQL 데이터 처리   

### 1) MySQL에서 모든 SQL을 단일 코어에서 처리한다.   
MySQL은 SQL을 병렬 처리하지 않는다.       
MySQL 기본적인 스토리지 엔진은 단일 코어로만 데이터를 처리한다.       
따라서 CPU 코어 개수를 늘리는 scale-out 보다는 단위 처리량이 좋은 CPU로 sacle-up 하는 것이 훨씬 좋다.   

<br />

### 2) MySQL은 테이블 조인을 Nested Loop Join 알고리즘만 처리한다.   
Nested Loop Join은 선행 테이블 A의 조건 검색 결과 값 하나하나를 테이블 B와 비교하여 조인하는 방식이다.   

다중 for문과 유사하다.


```java
for( A ) {
    for( B ) { ... }
}
```


처리할 데이터가 적으면 수행 속도가 빠르지만 A나 B중 하나라도 데이터가 많으면 쿼리 효율이 기하급수적으로 떨어진다.   

<br />


### Nested Loop Join   
<https://dev.mysql.com/doc/refman/8.0/en/nested-loop-joins.html>     


t1은 range scan으로 조인 시작 데이터 추출     
t1에서 도출한 데이터는 t2의 인덱스를 통해 연관된 데이터를 가져온다.   
t3에서 데이터를 교체하는데 t3은 적당한 인덱스가 없는 경우에 실행되고, 테이블 풀스캔을 한다.   


```java
for each row in t1 matching range {
  for each row in t2 matching reference key {
    for each row in t3 {
      if row satisfies join conditions, send to client
    }
  }
}
```

Nested Loop Join은 루프문이 세 개 겹친 형태라는 것에 주목하자.     
데이터를 처리하면 매번 데이터 접근 요청을 해야 하기 때문에 상당히 비효율적이다.     

그래서 Nested Loop Join으로만 처리한다고 했는데, DB 내부에서는 이것보다 한 단계 업그레이드된      
Block Nested Loop Join 방식으로 처리한다. (조인 버퍼 개념 도입)        
  

<br />

### Block Nested Loop Join 
<https://dev.mysql.com/doc/refman/8.0/en/nested-loop-joins.html>     

테이블 조인 시 필요한 데이터를 메모리에 일시적으로 저장하여 효율적으로 데이터에 접근한다.   

```java
for each row in t1 matching range {
  for each row in t2 matching reference key {
    store used columns from t1, t2 in join buffer
    if buffer is full {
      for each row in t3 {
        for each t1, t2 combination in join buffer {
          if row satisfies join conditions, send to client
        }
      }
      empty join buffer
    }
  }
}

if buffer is not empty {
  for each row in t3 {
    for each t1, t2 combination in join buffer {
      if row satisfies join conditions, send to client
    }
  }
}
```


Block Nested Loop Join이 다음 쿼리를 어떻게 처리하는지 알아보자.   

```
SELECT a.r1, b.r2
FROM TABLE_A a
INNER JOIN TABLE_B ON a.r1 = b.r2
```

(1) 테이블 A에서 조인할 데이터 대상을 찾는다.             
(2) 테이블 A에 있는 조인할 데이터를 조인 버퍼가 가득 찰 때까지 채운다.                    
(3) 조인 버퍼가 가득 채워지면 테이블 B의 데이터를 스캔하면서 조인 버퍼에 있는 데이터와 매칭되면 결과값을 내보낸다.       

            
테이블 B의 데이터를 스캔할 때는 테이블 풀스캔, 인덱스 풀스캔, 인덱스 범위 스캔 등으로 데이터에 접근한다.   
즉, 테이블의 인덱스 구조 혹은 쿼리 형태에 따라 다양하게 접근한다.   


조인 버퍼 안의 모든 데이터를 비교하는 첫 번째 과정이 종료되면 조인 버퍼를 비우고       
테이블 A 조건에 해당하는 데이터를 모두 처리할 때까지 반복해서 수행한다.         
여기서 테이블 B의 스캔하는 횟수는 `조인 버퍼에 데이터가 적재되는 횟수`와 동일하다.    

<br />   

