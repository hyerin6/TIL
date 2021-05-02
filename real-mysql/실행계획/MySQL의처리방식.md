# MySQL의 주요 처리 방식    
"풀 테이블 스캔"을 제외한 나머지는 모두 스토리지 엔진이 아니라 MySQL 엔진에서 처리되는 내용이다.   
MySQL 엔진에서 부가적으로 처리하는 작업은 대부분 성능에 미치는 영향력이 큰데 모두 쿼리의 성능을 저하시키는데 한몫하는 작업이다.    

MySQL 엔진에서 처리하는 데 시간이 오래 걸리는 작업의 원리를 알알둔다면 쿼리를 튜닝하는 데 상당히 많은 도움이 될 것이다.   

<br />         

## 1. 풀 테이블 스캔   
풀 테이블 스캔은 인덱스를 사용하지 않고 테이블의 데이터를 처음부터 끝까지 읽어서 요청된 작업을 처리하는 작업을 의미한다.    
MySQL 옵티마이저는 다음과 같은 조건이 일치할 때 주로 풀 테이블 스캔을 선택한다.    

* 테이블의 레코드 건수가 너무 작아서 인덱스를 통해 읽은 것보다 풀 테이블 스캔을 하는 편이 더 빠른 경우   
  (일반적으로 테이블이 페이지 1개로 구성된 경우)        
  
* WHERE 절이나 ON 절에 인덱스를 이용할 수 있는 적절한 조건이 없는 경우    

* 인덱스 레인지 스캔을 사용할 수 있는 쿼리라 하더라도 옵티마이저가 판단한 조건 일치 레코드 건수가 너무 많은 경우    
  (인덱스의 B-Tree를 샘플링해서 조사한 통계 정보 기준)      
  
* 반대로 max_seeks_for_key 변수를 특정 값(N)으로 설정하면 MySQL 옵티마이저는 인덱스의 기수성(Cardinality)이나     
  선택도(Selectivity)를 무시하고, 최대 N건만 읽으면 된다고 판단하게 한다.     
  이 값을 작게 설정할수록 MySQL 서버가 인덱스를 더 사용하도록 유도함.    
<br />      
  
일반적으로 테이블의 전체 크기는 인덱스보다 훨씬 크기 때문에 테이블을 처음부터 끝까지 읽는 작업은 상당히 많은 디스크 읽기가 필요하다.     
대부분의 DBMS는 풀 테이블 스캔을 실행할 때 한꺼번에 여러 개의 블록이나 페이지를 읽어오는 기능이 있으며 그 수를 조절할 수 있다.    
즉 디스크로부터 페이지를 하나씩 읽어 오는 것은 아니다. 근데 페이지 수를 지정하는건 불가능 > 이것은 MyISAM 에 해당하는 이야기   

InnoDB 스토리지 엔진은 특정 테이블의 연속된 데이터 페이지가 얽히면 백그라운드 스레드에 의해 리드 어헤드(Read ahead) 작업이 자동으로 시작된다.    
리드 어헤드란 어떤 영역의 데이터가 앞으로 필요해질 것을 예측해서 요청이 오기 전에 미리 디스크에서 읽어 InnoDB의 버퍼 풀에 가져다 두는 것을 의미한다.    

* 풀 테이블 스캔이 실행하면 처음 몇 개의 데이터 페이지는 포그라운드 스레드가 실행   
* 특정 시점부터는 백그라운드 스레드로 넘겨, 한 번에 최대 64개의 페이지 읽어 버퍼 풀에 저장   

포그라운드 스레드는 버퍼 풀의 데이터를 가져다 사용하기 때문에 쿼리가 상당히 빨리 처리된다.    

<br />  

## 2. ORDER BY 처리 (Using filesort)       
레코드 1~2건을 가져오는 쿼리를 제외하면 대부분 SELECT 쿼리에서 정렬은 필수적으로 사용된다.    
정렬 처리를 위한 방법은 다음과 같다.   

|방법|장점|단점|
|------|---|---|
|인덱스를 이용|INSERT, UPDATE, DELETE 쿼리가 실행될 때 이미 인덱스가 정렬돼 있어서 <br /> 순서대로 읽기만 하면 되므로 매우 빠르다.|INSERT, UPDATE, DELETE 작업 시 부가적인 인덱스 추가/삭제 작업이 필요하므로 느리다. <br /> 인덱스 때문에 디스크 공간이 더 많이 필요하다. <br /> 인덱스가 개수가 늘어날수록 InnoDB의 버퍼 풀이나 MyISAM의 키 캐시용 메모리가 많이 필요하다.|
|Filesort 이용|인덱스를 생성하지 않아도 되므로 인덱스를 이용할 때의 단점이 장점으로 바뀐다. <br /> 정렬애햐 할 레코드가 많지 않으면 메모리에서 Filesort가 처리되므로 충분히 빠르다.|정렬 작업이 쿼리 실행 시 처리되므로 레코드 대상 건수가 많아질수록 쿼리의 응답 속도가 느리다.|   

<br />   
 
항상 Filesort 정렬 작업을 거쳐야 하는 것은 아니며     
다음과 같은 이유로 모든 정렬을 인덱스를 이용하도록 튜닝하기란 거의 불가능하다.  

* 정렬 기준이 너무 많아 요건별로 모두 인덱스를 생성하는 것이 불가능한 경우    
* GROUP BY의 결과 또는 DISTINCT와 같은 처리의 결과를 정렬해야 하는 경우    
* UNION의 결과와 같이 임시 테이블의 결과를 다시 정렬해야 하는 경우    
* 랜덤하게 결과 레코드를 가져와야 하는 경우 (때로는 인덱스를 이용할 수 있도록 개선할 수 있어야 한다.   
<br />     
  
MySQL이 인덱스를 이용하지 않고 별도의 정렬 처리를 수행했는지는 실행 계획의 Extra 칼럼에 "Using filesort"가 표시되는지로 판단할 수 있다.    


<br />     

### 소트 버퍼 (Sort buffer)     
MySQL은 정렬을 위해 별도의 메모리 공간을 할당 받아서 사용하는데 이 메모리 공간을 소트 버퍼라고 한다.    

* 정렬이 필요한 경우에만 할당되며 쿼리 실행이 완료되면 즉시 시스템으로 반납된다.    
* 정렬해야 할 레코드의 건수가 소트 버퍼로 할당된 공간보다 크다면 어떻게 될까?    
    - MySQL은 정렬해야 할 레코드를 여러 조각으로 나눠서 처리한다.    
    이 과정에서 임시 저장을 위해 디스크를 사용한다.    
    
<br />      

### 정렬 알고리즘   
레코드 전체를 소트 버퍼에 담을지 기준 칼럼만 담을지에 따라 구분할 수 있다.    
<br />

#### 1) 싱글 패스(Single pass) 알고리즘    
소트 버퍼에 정렬 기준 칼럼을 포함해 SELECT되는 칼럼 전부를 담아서 정렬하는 방법이다.    


#### 2) 투 패스(Two pass) 알고리즘   
정렬 대상 칼럼과 프라이머리 키값만 소트 버퍼에 담아서 정렬을 수행하고, 정렬된 순서대로 다시 프라이머리 키로 테이블을 읽어서   
SELECT할 칼럼을 가져오는 알고리즘으로 예쩐 버전의 MySQL에서 사용하던 방법이다.    

정렬이 완료되면 결과를 순서대로 테이블에서 한 번 더 읽어서 SELECT 칼럼을 가져온다.  
이 방식은 (같은 레코드) 테이블을 두 번 읽어야 하기 때문에 불합리하다. 하지만 싱글 패스 알고리즘은 더 많은 소트 버퍼 공간이 필요하다.    
  

싱글 패스 알고리즘은 정렬 대상 레코드가 적은 경우 빠른 성능을 보이고,   
투 패스 알고리즘은 정렬 대상 레코드의 크기나 건수가 상당히 많은 경우 효율적이라고 볼 수 있다.      


<br />     

### 정렬의 처리 방식    
쿼리에 ORDER BY가 사용되면 반드시 다음 방식 중 하나로 정렬이 처리된다.    
일반적으로 밑쪽에 있는 방법으로 갈수록 처리가 느려진다.   

|정렬 처리 방법|실행 계획의 Extra 코멘트|
|---|---|
|인덱스 사용한 정렬|별도의 내용 표기 없음|
|드라이빙 테이블만 정렬 <br /> (조인이 없는 경우 포함)|"Using filesort"가 표시됨|
|조인 결과를 임시 테이블로 저장한 후, <br /> 임시 테이블에서 정렬|"Using temporary; Using filesort"가 표시됨|

<br />   

먼저 옵티마이저는 정렬 처리를 위해 인덱스를 이용할 수 있는지 검토할 것이다.     
만약 인덱스를 이용할 수 있다면 별도의 "Filesort" 과정 없이 인덱스를 순서대로 읽어서 결과를 반환한다.     
하지만 인덱스를 이용할 수 없다면 WHERE 조건에 일치하는 레코드를 검색해 정렬 버퍼에 저장하면서 정렬을 처리할 것이다.     

이때 MySQL 옵티마이저는 정렬 대상 레코드를 최소화하기 때문에 다음 두 가지 방법중 하나를 선택한다.   
1. 드라이빙 테이블만 정렬한 다음 조인을 수행    
2. 조인이 끝나고 일치하는 레코드를 모두 가져온 후 정렬을 수행     

일반적으로 조인이 수행되면서 레코드 건수는 거의 배수로 불어나기 때문에 가능하다면 드라이빙 테이블만 정렬한 다음 조인을 수행하는 방법이 효율적이다.    
그래서 두 번째 방법보다는 첫 번째 방법이 더 효율적으로 처리된다.    

<br />      

#### 1) 인덱스를 이용한 정렬     
* 인덱스를 이용한 정렬을 위해 반드시 ORDER BY에 명시된 칼럼이 제일 먼저 읽는 테이블에 속하고 (조인 사용 시 드라이빙 테이블)    
ORDER BY의 순서대로 생성된 인덱스가 있어야 한다.    
  
* WHERE 절에 첫 번째 읽는 테이블의 칼럼에 대한 조건이 있다면 그 조건과 ORDER BY는 같은 인덱스를 사용할 수 있어야 한다.   

* B-Tree 계열의 인덱스가 아닌 해시나 전문 검색 인덱스 등에서는 인덱스를 이용한 정렬을 사용할 수 없다.   

* 인덱스를 이용한 정렬의 경우 MySQL 엔진에서 별도의 정렬을 위한 추가 작업을 수행하지 않는다.   
<br />      

```
SELECT * FROM employees e, salaries s
WHERE s.emp_no=e.emp_no
AND e.emp_no BETWEEN 100002 AND 100020 
ORDER BY e.emp_no;

-- // emp_no 칼럼으로 정렬이 필요한데 인덱스를 사용하면서 자동 정렬이 된다고 
-- // 일부러 ORDER BY emp_no를 제거하는 것은 좋지 않은 선택이다. 
SELECT * FROM employees e, salaries s
WHERE s.emp_no=e.emp_no
AND e.emp_no BETWEEN 100002 AND 100020;
```

위 예제는 ORDER BY가 있건 없건 인덱스를 레인지 스캔해서 결과는 같다.    
ORDER BY 절이 없어도 정렬되는 이유는 employees 테이블의 프라이머리 키를 읽고, 그 다음 salaries 테이블을 조인했기 때문이다.    

즉 B-Tree 인덱스가 키 값으로 정렬돼 있고 조인이 네스티드-루프 방식으로 실행되기 때문에 조인 때문에 드라이빙 테이블의 인덱스 읽기 순서가 흐트러지지 않는다.   
조인이 사용된 쿼리의 실행 계획에 조인 버퍼가 사용되면 순서가 흐트러질 수 있기 때문에 주의해야 한다.   

<br />      

#### 2) 드라이빙 테이블만 정렬    
일반적으로 조인이 수행되면 레코드 수가 증가      
-> 조인 실행 전 드라이빙 테이블 레코드만 정렬하고 조인하자.    
-> 드라이빙 테이블의 칼럼만 ORDER BY 절이 작성돼야 한다.     


**예)**  
```
SELECT * FROM employees e, salaries s 
WHERE s.emp_no=e.emp_no 
  AND e.emp_no BETWEEN 100002 AND 100020
ORDER BY e.last_name;
```

우선 WHERE 절이 다음 조건을 만족해야 옵티마이저가 employees 테이블을 드라이빙 테이블로 선택할 것이다. 
1. WHERE 절의 검색 조건은 employees 테이블의 프라이머리 키를 이용해 검색하면 작업량을 줄일 수 있다.    
2. 드리븐 테이블(salaries)의 조인 칼럼인 emp_no 칼럼에 인덱스가 있다.    
<br />         
   
검색은 인덱스 레인지 스캔으로 처리할 수 있지만 ORDER BY 절에 명시된 칼럼은      
employees 테이블의 프라이머리 키와 전혀 연관이 없기 때문에 인덱스를 이용한 정렬을 불가능하다.      
그러나 ORDER BY 절의 정렬 기준 칼럼이 드라이빙 테이블에 포함된 칼럼임을 알 수 있기 때문에   
옵티마이저는 드라이빙 테이블만 검색해서 정렬을 먼저 수행하고 그 결과와 salaries 테이블을 조인한 것이다.   

위 SELECT 쿼리 수행 과정은 다음과 같다.   
1. 인덱스를 이용해 WHERE 조건을 만족하는 레코드 검색   
2. 검색 결과를 last_name 칼럼으로 정렬 수행(Filesort)   
3. 정렬된 결과를 순서대로 읽으면서 salaries 테이블과 조인을 수행해서 최종 결과를 가져옴   

<br />   

#### 3) 임시 테이블을 이용한 정렬   
```
SELECT * FROM employees e, salaries s 
WHERE s.emp_no=e.emp_no AND e.emp_no BETWEEN 100002 AND 100020
ORDER BY s.salary;
```

위 쿼리는 ORDER BY 절의 정렬 기준 칼럼이 드라이빙 테이블이 아니라 드리븐 테이블에 있는 칼럼이다.     
즉 정렬이 수행되기 전에 반드시 salaries 테이블을 읽어야 하므로 이 쿼리는 반드시 조인된 데이터를 가지고 정렬할 수 밖에 없다.         

쿼리의 실행 계획을 보면 Extra 칼럼에 "Using temporary; Using filesort" 라는 메시지가 표시된다.      
이는 조인의 결과를 임시 테이블에 저장하고, 그 결과를 다시 정렬 처리했음을 의미한다.       

<br />  

#### 정렬 방식의 성능 비교   
쿼리에서 ORDER BY와 함께 LIMIT이 사용되는 경향이 있다.       
일반적으로 LIMIT은 테이블이나 처리 결과의 일부만 가져오기 때문에 MySQL 서버가 처리해야 할 작업량을 줄이는 역할을 한다.   
하지만 ORDER BY나 GROUP BY와 같은 작업은 WHERE 조건을 만족하는 레코드를 LIMIT 건수만큼만 가져와서는 처리할 수 없다.   
조건에 만족하는 레코드를 모두 가져와 정렬한 후 LIMIT으로 건수 제한을 할 수 있다.   
WHERE 조건이 아무리 인덱스를 잘 활용하도록 튜닝해도 잘못된 ORDER BY나 GROUP BY 때문에 쿼리가 느려지는 경우가 자주 발생한다.    

쿼리에서 인덱스를 사용하지 못하는 정렬이나 그룹핑 작업이 왜 느리게 작동할 수밖에 없는지 한번 살펴보자.    
<br />

* 스트리밍(Streaming) 방식   
  서버 쪽에서 처리해야 할 데이터가 얼마나 될지는 관계없이 조건에 일치하는 레코드가 검색될 때마다 바로바로 클라이언트로 전송해주는 방식을 의미한다.    
  이 방식으로 쿼리를 처리할 경우 클라이언트는 쿼리를 요청하고 곧바로 원했던 첫 번째 레코드를 전달받을 것이다.    
  MySQL 서버가 일치하는 레코드를 찾는 즉시 전달받기 때문에 동시에 데이터 가공 작업을 시작할 수 있다.    
  스트리밍 방식으로 처리되는 쿼리는 쿼리가 얼마나 많은 레코드를 조회하느냐에 상관없이 빠른 응답 시간을 보장해 준다. <br />         
  또한 스트리밍 방식으로 처리되는 쿼리에서 LIMIT와 같이 결과 건수를 제한하는 조건들은 쿼리의 전체 실행 시간을 상당히 줄여줄 수 있다.     
  매우 큰 테이블을 아무런 조건 없이 SELECT만 해 보면 첫 번째 레코드는 아주 빨리 가져올 것이다.    
  이것은 풀 테이블 스캔의 결과가 아무런 버퍼링 처리나 필터링 과정 없이 바로 클라이언트로 스트리밍되기 때문이다.     
  이 쿼리에 LIMIT 조건을 추가하면 전체적으로 가져오는 레코드 건수가 줄어들기 때문에 마지막 레코드를 가져오기까지의 시간을 상당히 줄일 수 있다.
  
<br />       
  
* 버퍼링(Buffering) 방식   
  ORDER BY나 GROUP BY과 같은 처리는 쿼리의 스트리밍되는 것을 불가능하게 한다.   
  WHERE 조건에 일치하는 모든 레코드를 가져온 후 정렬하거나 그룹핑을 해서 차례대로 보내야 하기 때문이다.    
  MySQL 서버에서는 모든 레코드를 검색하고 정렬 작업을 하는 동안 클라이언트는 아무것도 하지 않고 기다려야 하기 때문에 응답속도가 느려지는 것이다.   
  이 방식을 스트리밍의 반대 표현으로 버퍼링이라고 한다. <br />     
  버퍼링 방식으로 처리되는 쿼리는 먼저 결과를 모아서 MySQL 서버에서 일괄 가공해야 하므로 모든 결과를 스토리지 엔진으로부터 가져올 때까지 기다려야 한다.    
  그래서 버퍼링 방식으로 처리되는 쿼리는 LIMIT 처럼 결과 건수를 제한하는 조건이 있어도 성능 향상에 별로 도움이 되지 않는다.    
  네트워크로 전송되는 레코드의 건수를 줄일 수는 있지만 MySQL 서버가 해야 하는 작업량에는 그다지 변화가 없기 때문이다.   


<br />       

앞에서 나왔던 ORDER BY의 3가지 처리 방식 가운데 인덱스를 사용한 정렬 방식만 스트리밍 형태의 처리이며, 나머지는 모두 버퍼링된 후에 정렬된다.    
즉 인덱스를 사용한 정렬 방식은 LIMIT로 제한된 건수만큼만 읽으면서 바로바로 클라이언트로 결과를 전송해줄 수 있다.   
하지만 인덱스를 사용하지 못하는 경우의 처리는 필요한 모든 레코드를 디스크로부터 읽어서 정렬한 후에야    
비로소 LIMIT로 제한된 건수만큼 잘라서 클라이언트로 전송해줄 수 있음을 의미한다.       
<br />    

조인과 함께 ORDER BY 절과 LIMIT 절이 함께 사용될 경우, 정렬의 각 처리 방식별로 어떤 차이가 있는지 살펴보자.   

```
SELECT * 
FROM tb_test1 t1, tb_test2 t2
WHERE t1.col1=t2.col1;
ORDER BY t1.col2
LIMIT 10;
```

tb_test1 테이블의 레코드가 100건이고, tb_test2 테이블의 레코드가 1000건이며   
두 테이블의 조인 결과는 전체 1000건이라고 가정하고 정의 처리 방식별로 읽어야하는 레코드 건수와 정렬을 수행해야 하는 레코드 건수를 비교해보자.   

#### ⦁ tb_test1이 드라이빙이되는 경우   
|정렬 방식|읽어야 할 건수|조인 횟수|정렬해야 할 대상 건수|
|---|---|---|---|
|인덱스 사용|tb_test1 : 1건 <br /> tb_test2 : 10건|1번|0건|
|드라이빙 테이블만 정렬|tb_test1 : 100건 <br /> tb_test2 : 10건|10번|100건 <br /> (tb_test1 테이블의 레코드 건수만큼 정렬 필요)|
|임시 테이블 사용 후 정렬|tb_test1 : 100건 <br /> tb_test2 : 1000건|100번 <br /> (tb_test1 테이블의 레코드 건수만큼 조인 발생)|1000건 <br /> (조인된 결과 레코드 건수를 전부 정렬해야 함)|


<br />   

#### ⦁ tb_test2이 드라이빙이되는 경우   
|정렬 방식|읽어야 할 건수|조인 횟수|정렬해야 할 대상 건수|
|---|---|---|---|
|인덱스 사용|tb_test1 : 10건 <br /> tb_test2 : 10건|10번|0건|
|드라이빙 테이블만 정렬|tb_test1 : 1000건 <br /> tb_test2 : 10건|10번|1000건 <br /> (tb_test2 테이블의 레코드 건수만큼 정렬 필요)|
|임시 테이블 사용 후 정렬|tb_test1 : 1000건 <br /> tb_test2 : 100건|100번 <br /> (tb_test2 테이블의 레코드 건수만큼 조인 발생)|1000건 <br /> (조인된 결과 레코드 건수를 전부 정렬해야 함)|


<br />   

어느 테이블이 먼저 드라이빙되어 조인되는지도 중요하지만 어떤 정렬 방식으로 처리되는지는 더 큰 성능 차이를 만든다.   
가능하면 인덱스를 사용한 정렬로 유도하고 그렇지 못한다면 최소한 드라이빙 테이블만 정렬하도록 유도하는 것도 좋은 튜닝 방법이라고 할 수 있다.    


## 3. GROUP BY 처리    
GROUP BY도 ORDER BY와 같이 쿼리가 스트리밍된 처리를 할 수 없게 하는 요소 중 하나다.    
GROUP BY 절이 있는 쿼리에서는 HAVING 절을 사용할 수 있는데, HAVING 절은 GROUP BY 결과에 대해 필터링 역할을 수행한다.    
일반적으로 GROUP BY 처리 결과는 임시 테이블이나 버퍼에 존재하는 값을 필터링하는 역할을 수행한다.   
GROUP BY에 사용하는 조건은 인덱스를 사용해서 처리할 수 없다.   

* GROUP BY 작업에서 인덱스를 사용하는 경우 : 인덱스 스캔, 루스 인덱스 스캔      
* GROUP BY 작업에서 인덱스를 사용하지 못하는 경우 : 임시 테이블      

<br />    

#### 1) 인덱스 스캔을 이용하는 GROUP BY(타이트 인덱스 스캔)    
ORDER BY의 경우와 마찬가지로 조인의 **드라이빙 테이블에 속한 칼럼만 이용해 그룹핑**할 때 GROUP BY 칼럼으로 이미 **인덱스가 있다면    
그 인덱스를 차례대로 읽으면서 그룹핑 작업을 수행**하고 그 결과로 조인을 처리한다.        
GROUP BY가 인덱스를 사용해서 처리된다고 하더라도 그룹 함수 등의 그룹값을 처리해야 해서 임시 테이블이 필요할 때도 있다.    
GROUP BY가 인덱스를 통해 처리되는 쿼리는 이미 정렬된 인덱스를 읽는 것이기 때문에 추가적인 정렬 작업은 필요하지 않는다.    

<br />  

#### 2) 루스 인덱스 스캔을 이용하는 GROUP BY    
루스 인덱스 스캔 방식은 인덱스의 레코드를 건너뛰면서 필요한 부분을 가져오는 것을 의미하는데    
다음 예제를 통해 GROUP BY에서 어떻게 사용되는지 알아보자.   

```
EXPLAIN 
SELECT emp_no 
FROM salaries 
WHERE from_date='1985-03-01'
GROUP BY emp_no;
```

salaries 테이블의 인덱스는 (emp_no + from_date)로 생성돼 있으므로 위의 쿼리 문장에서 WHERE 조건은 인덱스 레인지 스캔 접근 방식으로 이용할 수 없는 쿼리다.       
하지만 이 쿼리의 실행 계획은 인덱스 레인지 스캔(range 타입)을 이용했으며, Extra 칼럼의 메시지를 보면 GROUP BY 처리까지 인덱스를 사용했다는 것을 알 수 있다.     


|id|select_type|Table|type|key|key_len|ref|rows|Extra|
|---|---|---|---|---|---|---|---|---|
|1|SIMPLE|salaries|range|PRIMARY|7| |568914|Using where; <br /> Using index for group-by|

<br />    

MySQL 서버는 다음과 같은 순서로 위 쿼리를 실행한다.    

1. (emp_no + from_date) 인덱스를 차례대로 스캔하면서, emp_no의 첫 번째 유일한 값(그룹 키) '10001'을 찾아낸다.    

2. (emp_no + from_date) 인덱스에서 emp_no가 '10001'인 것 중에서 from_date 값이 '1985-03-01'인 레코드만 가져온다.      
   이 검색 방법은 1번 단계에서 알아낸 '10001' 값과 쿼리의 WHERE 절에 사용된 `from_date='1985-03-01'` 조건으로    
   (emp_no + from_date) 인덱스를 검색하는 것과 거의 흡사하다.    

3. (emp_no + from_date) 인덱스에서 emp_no의 그 다음 유니크한 (그룹 키) 값을 가져온다.    

4. 3번 단계에서 결과가 더 없으면 처리를 종료하고, 결과가 있으면 2번 과정으로 돌아가서 반복 수행한다.   

<br />    

**MySQL의 루스 인덱스 스캔 방식은 단일 테이블에 대해 수행되는 GROUP BY 처리에만 사용할 수 있다.**             
또한 프리픽스 인덱스(Prefix index, 칼럼 값의 앞쪽 일부만으로 생성된 인덱스)는 루스 인덱스 스캔을 사용할 수 없다.      

인덱스 레인지 스캔에서는 유니크한 값의 수가 많을수록 성능 향상되는 반면 루스 인덱스 스캔에서는 인덱스의 유니크한 값의 수가 적을수록 성능이 향상된다.            
즉, 루스 인덱스 스캔은 분포도가 좋지 않은 인덱스일수록 더 빠른 결과를 만들어낸다.               
루스 인덱스 스캔으로 처리되는 쿼리에서는 별도의 임시 테이블이 필요하지 않다.      

루스 인덱스 스캔이 사용될 수 있을지 없을지 판단하는 것은 WHERE 절의 조건이나 ORDER BY 절이 인덱스를 사용할 수 있을지 없을지 판단하는 것보다는 더 어렵다.    
<br />   

**예) (col1 + col2 + col3) 칼럼으로 생성된 tb_test 테이블을 가정해보자.**   
다음의 쿼리들은 루스 인덱스 스캔을 사용할 수 있는 쿼리다. 쿼리의 패턴을 보고 어떻게 사용 가능한지 생각해보자.    

```
SELECT col1, col2 FROM tb_test GROUP BY col1, col2;
SELECT DISTINCT col1, col2 FROM tb_test;
SELECT col1, MIN(col2), FROM tb_test GROUP BY col1;
SELECT col1, col2 FROM tb_test WHERE col1 < const GROUP BY col1, col2;
SELECT MAX(col3), MIN(col3), col1, col2 FROM tb_test
WHERE col2 > const GROUP BY col1, col2;
SELECT col2 FROM tb_test WHERE col1 < const GROUP BY col1, col2;
SELECT col1, col2 FROM tb_test WHERE col3 = const GROYP BY col1, col2;
```

다음의 쿼리는 루스 인덱스 스캔을 사용할 수 없는 쿼리 패턴이다.   
```
-- // MIN()과 MAX() 이외의 집합 함수가 사용된 경우 루스 인덱스 스캔 사용 불가 
SELECT col1, SUM(col2) FROM tb_test GROUP BY col1;

-- // GROUP BY에 사용된 칼럼이 인덱스 구성 칼럼의 왼쪽부터 일치하지 않기 때문에 사용 불가   
SELECT col1, col2 FROM tb_test GROUP BY col2, col3;

-- // SELECT 절의 칼럼이 GROUP BY와 일치하지 않기 때문에 사용 불가  
SELECT col1, col3 FROM tb_test GROUP BY col1, col2;
```


<br />   

#### 3) 임시 테이블을 사용하는 GROUP BY   
GROUP BY의 기준 칼럼이 드라이빙 테이블에 있든 드리븐 테이블에 있든 관계없이 인덱스를 전혀 사용하지 못할 때는 이 방식으로 처리된다.   

```
EXPLAIN  
SELECT e.last_name, AVG(s.salary)
FROM employees e, salaries s 
WHERE s.emp_no=e.emp_no
GROUP BY e.last_name;
```
  
이 쿼리의 실행 계획에서는 Extra 칼럼에 "Using temporary"와 "Using filesort" 메시지가 표시된다.       
실행 계획에서 임시 테이블 사용된 것은 employees 테이블을 풀 스캔하기 때문이 아니라 인덱스를 전혀 사용할 수 없는 GROUP BY이기 때문이다.      
<br />  

1. Employees 테이블을 풀 테이블 스캔 방식으로 읽는다.   
2. 1번 단계에서 읽은 employees 테이블의 emp_no 값을 이용해 salaries 테이블을 검색한다.    
3. 2번 단계에서 얻은 조인 결과 레코드를 임시 테이블에 저장한다.       
   이 단계에서 사용되는 임시 테이블은 원본 쿼리에서 GROUP BY 절에 사용된 칼럼과 SELECT하는 칼럼만 저장한다.            
   이 임시 테이블에서 중요한 것은 **GROUP BY 절에 사용된 칼럼으로 유니크 키를 생성**한다는 점이다.             
   즉, GROUP BY가 임시 테이블로 처리되는 경우 사용되는 **임시 테이블은 항상 유니크 키를 가진다.**            
4. 1번 단계부터 3번 단계를 조인이 완료될 때까지 반복한다. 조인이 완료되면 임시 테이블의 유니크 키 순서대로 읽어서 클라이언트로 전송된다.      
   만약 쿼리의 ORDER BY 절에 명시된 칼럼과 GROUP BY 절에 명시된 칼럼이 같으면 별도의 정렬 작업을 수행하지 않는다.       
   ORDER BY 절과 GROUP BY 절에 명시된 칼럼이 다르다면 Filesort 과정을 거치면서 다시 한번 정렬 작업을 수행한다.     
<br />       

 
## 4. DISTINCT 처리        
특정 칼럼의 유니크한 값만을 조회하려면 SELECT 쿼리에 DISTINCT를 사용한다.      
DISTINCT는 `MIN()`, `MAX()`, `COUNT()`와 같은 집합 함수와 함께 사용되는 경우와     
집합 함수가 없는 경우, DISTINCT 키워드가 영향을 미치는 범위가 달라진다.          
 
집합 함수와 같이 DISTINCT가 사용되는 쿼리의 실행 계획에서 DISTINCT 처리가 인덱스를 사용하지 못할 때는 항상 임시 테이블이 필요하다.       
하지만 실행 계획의 Extra 칼럼에는 "Using temporary" 메시지가 출력되지 않는다.         
<br />   

#### 1) SELECT DISTINCT ...     
* 단순히 SELECT 되는 레코드 중 유니크한 레코드만 가져오는 경우      
* 이 경우 GROUP BY와 거의 같은 방식으로 처리되지만 정렬 보장되지 않음     
    <details>
    <summary>GROUP BY / SELECT DISTINCT ...</summary>
    <div markdown="1">
    다음 두 쿼리는 정렬 관련 부분만 빼면 내부적으로 같은 작업을 수행한다.   

    ```
    SELECT DISTINCT emp_no FROM salaries;
    SELECT emp_no FROM salaries GROUP BY emp_no;
    ```
    </div>
    </details>

<br />   
DISTINCT를 사용할 때 자주 실수 하는 것이 있다.    
DISTINCT는 SELECT하는 레코드를 유니크하게 SELECT하는 것이지 칼럼을 유니크하게 조회하는 것은 아니다.   

즉, 다음 쿼리에서 SELECT하는 결과는 first_name만 유니크한 것을 가져오는 것이 아니라      
(first_name + last_name) 전체가 유니크한 레코드를 가져오는 것이다.     
```
SELECT DISTINCT first_name, last_name FROM employees; 
```

DISTINCT를 다음과 같이 사용하는 경우도 있다.     
```
SELECT DISTINCT (first_name), last_name FROM employees;
```
MySQL 서버는 DISTINCT 뒤의 괄호를 그냥 의미없이 사용된 괄호로 해석하고 제거해 버린다.     
DISTINCT는 함수가 아니기 때문에 괄호는 아무런 의미가 없다.      


```
SELECT DISTINCT first_name, last_name FROM employees;
```
SELECT 절에 사용된 DISTINCT 키워드는 조회되는 모든 칼럼에 영향을 미친다.   
절대로 일부 칼럼만 유니크하게 조회하는 방법은 없다.   
단, 이어서 설명할 DISTINCT가 집합 함수 내에 사용된 경우는 조금 다르다.    

<br />   

#### 2) 집합 함수와 함께 사용된 DISTINCT   
`COUNT()` 또는 `MIN()`, `MAX()`와 같은 집합 함수 내에서 DISTINCT 키워드가 사용될 수 있는데,         
이 경우에는 일반적으로 SELECT DISTINCT와 다른 형태로 해석된다.        

집합 함수가 없는 SELECT 쿼리에서 DISTINCT는 조회하는 **모든 칼럼의 조합이 유니크한 것**들만 가져온다.    
하지만 집합 함수 내에서 사용된 DISTINCT는 그 **집합 함수의 인자로 전달된 칼럼 값들 중에서 중복을 제거하고 남은 값**만을 가져온다.   

```
EXPLAIN 
SELECT COUNT(DISTINCT s.salary)
FROM employees e, salaries s
WHERE e.emp_no=s.emp_no
AND e.emp_no BETWEEN 100001 AND 100100;
```

이 쿼리는 내부적으로 `COUNT(DISTINCT s.salary)`를 처리하기 위해 **임시 테이블을 사용한다.**    
하지만 **실행 계획에는 임시 테이블을 사용한다는 메시지를 표시되지 않는다.**   

employees 테이블과 salaries 테이블을 조인한 결과에서 salary 칼럼의 값만 저장하기 위한 임시 테이블을 만들어서 사용한다.    
이때 임시 테이블의 salary 칼럼에는 유니크 인덱스가 생성되기 때문에 레코드 건수가 많아진다면 상당히 느려질 수 있는 형태의 쿼리다.    
<br />      

위의 쿼리에 `COUNT(DISTINCT ...)`를 하나 더 추가해서 다음과 같이 변경해보자.    
`COUNT()` 함수가 두 번 사용된 다음 쿼리의 실행 계획은 위의 쿼리와 똑같이 표시된다.     
하지만 s.salary 칼럼의 값을 저장하는 임시 테이블과 e.last_name 칼럼의 값을 저장하는      
또 다른 임시 테이블이 필요하므로 **전체적으로 2개의 임시 테이블을 사용한다.**          

```
SELECT COUNT(DISTINCT s.salary), COUNT(DISTINCT e.last_name)
FROM employees e, salaries s 
WHERE e.emp_no=s.emp_no
AND e.emp_no BETWEEN 100001 AND 100100;
```

위의 쿼리는 DISTINCT 처리를 위해 인덱스를 이용할 수 없어서 임시 테이블이 필요했다.      
<br />      

하지만 다음 쿼리와 같이 인덱스된 칼럼에 대해 DISTINCT 처리를 수행할 때는   
인덱스를 풀 스캔하거나 레인지 스캔하면서 임시 테이블 없이 최적화된 처리를 수행할 수 있다.     

```
SELECT COUNT(DISTINCT emp_no) FROM employees;
SELECT COUNT(DISTINCT emp_no) FROM dept_emp GROUP BY dept_no;
```


<br />   

## 5. 임시 테이블(Using temporary)      
MySQL 엔진이 스토리지 엔진으로부터 받아온 레코드를 정렬하거나 그룹핑할 때는 내부적인 임시 테이블을 사용한다.    
"내부적" 이라는 것은 `CREATE TEMPORARY TABLE`로 만든 임시 테이블과는 다르다는 의미다.    

일반적으로 MySQL 엔진이 사용하는 임시 테이블은 처음에 메모리에 생성됐다가 테이블의 크기가 커지면 디스크로 옮겨진다.      
원본 테이블의 스토리지 엔진과 관계없이 임시 테이블이 메모리를 사용할 때는 MEMORY 스토리지 엔진을 사용하며, 디스크에 저장될 때는 MyISAM 스토리지 엔진을 이용한다.        
<br />          

#### 임시 테이블이 필요한 쿼리      
다음과 같은 패턴의 쿼리는 MySQL 엔진에서 별도의 데이터 가공 작업을 필요로 하므로 대표적으로 내부 임시 테이블을 생성하는 케이스다.    
이 밖에도 인덱스를 사용하지 못할 때 내부 임시 테이블을 생성해야 할 때가 많다.    

1. ORDER BY와 GROUP BY에 명시된 칼럼이 다른 쿼리        
2. ORDER BY나 GROUP BY에 명시된 칼럼이 조인의 순서상 첫 번째 테이블이 아닌 쿼리    
3. DISTINCT와 ORDER BY가 동시에 쿼리에 존재하는 경우 또는 DISTINCT가 인덱스로 처리되지 못하는 쿼리
4. UNION이나 UNION DISTINCT가 사용된 쿼리(select_type 칼럼이 UNION RESULT인 경우)          
5. UNION ALL이 사용된 쿼리(select_type 칼럼이 UNION RESULT인 경우)       
6. 쿼리의 실행 계획에서 select_type이 DERIVED인 쿼리        

실행 계획의 Extra 칼럼에 "Using temporary"가 표시되는지 확인하면 되는데   
4번 ~ 6번 패턴이 "Using temporary"가 표시되지 않는데 임시 테이블을 사용하는 경우이다.     

1번 ~ 4번 패턴은 유니크 인덱스를 가지는 내부 임시 테이블이 만들어진다.    
5번, 6번 패턴은 유니크 인덱스가 없는 내부 임시 테이블이 생성된다.    
일반적으로 유니크 인덱스가 있는 내부 임시 테이블은 그렇지 않은 쿼리보다 상당히 처리 성능이 느리다.    

<br />   

#### 임시 테이블 관련 주의사항    
레코드 건수가 많지 않으면 내부 임시 테이블이 메모리에 생성되고 MySQL의 서버의 부하에 크게 영향을 미치지는 않는다.        
성능상의 이슈가 될만한 부분은 내부 임시 테이블이 MyISAM 테이블로 디스크에 생성되는 경우다.         

```
SELECT * FROM employees GROUP BY last_name ORDER BY first_name;   
```

이 쿼리는 GROUP BY와 ORDER BY 칼럼이 다르고 last_name 칼럼에 인덱스가 없기 때문에       
임시 테이블과 정렬 작업까지 수행해야 하는 가장 골칫거리가 되는 쿼리 형태다.      

실행 계획은 다음과 같다.   

|id|select_type|Table|type|key|key_len|ref|rows|Extra|
|---|---|---|---|---|---|---|---|---|
|1|SIMPLE|employees|ALL| | | |300584|Using temporary; <br /> Using filesort| 

이 쿼리는 대략 처리해야 하는 레코드 건수가 30만 건 정도라는 것을 알 수 있다.    
실행 계획의 내부적인 작업 과정은 다음과 같다.   

1. Employees 테이블의 **모든 칼럼을 포함한 임시 테이블을 생성** (MEMORY 테이블)     
2. Employees 테이블로부터 첫 번째 레코드를 InnoDB 스토리지 엔진으로부터 가져와서   
3. 임시 테이블에 같은 last_name이 있는지 확인    
4. 같은 last_name이 없으면 임시 테이블에 INSERT     
5. 같은 last_name이 있으면 임시 테이블에 UPDATE 또는 무시     
6. 임시 테이블의 크기가 특정 크기보다 커지면 임시 테이블을 MyISAM 테이블로 디스크로 이동    
7. Employees 테이블에서 더 읽을 레코드가 없을 때까지 2~6번 과정 반복 (위 쿼리는 30만 회 반복)     
8. 최종 내부 임시 테이블에 저장된 결과에 대해 정렬 작업을 수행    
9. 클라이언트에 결과 반환    
<br />      
   
여기서 중요한 것은 임시 테이블이 메모리에 있는 경우는 조금 다르겠지만        
디스크에 임시 테이블이 저장된 경우라면 30만 건을 임시 테이블로 저장하려면 적지 않은 부하가 발생하리라는 것이다.      
가능하면 인덱스를 이용해 처리하고, 처음부터 임시 테이블이 필요하지 않게 만드는 것이 가장 좋다.    

<br />   

## 6. 테이블 조인     
MySQL의 모든 버전에서 조인 방식은 네스트드-루프로 알려진 중첩된 루프와 같은 형태만 지원한다.       
<br />      

### 1) 조인의 종류         
크게 다음과 같이 구분한다.     
* INNER JOIN     
* OUTER JOIN   
    - LEFT OUTER JOIN    
    - RIGHT OUTER JOIN   
    - FULL OUTER JOIN    
    <br />    
      

조인의 조건을 어떻게 명시하느냐 따라 다음으로 구분된다.    
* NATURAL JOIN     
* CROSS JOIN(FULL JOIN, CARTESIAN JOIN)           
<br />     
  
조인의 처리에서 어느 테이블을 먼저 읽을지를 결정하는 것은 상당히 중요하며, 그에 따라 처리할 작업량이 상당히 달라진다.    
INNER JOIN은 어느 테이블을 먼저 읽어도 결과가 달라지지 않으므로 MySQL 옵티마이저가 조인의 순서를 조절해서 다양한 방법으로 최적화를 수행할 수 있다.      
하지만 OUTER JOIN은 반드시 OUTER가 되는 테이블을 먼저 읽어야 하기 때문에 조인 순서를 옵티마이저가 선택할 수 없다.    

<br />     

#### JOIN(INNER JOIN)    
MySQL에서 조인은 네스티드-루프 방식만 지원한다. 네스티드-루프란 일반적으로 프로그램을 작성할 때        
두 개의 FOR이나 WHILE과 같은 반복 루프 문장을 실행하는 헝태로 조인이 처리되는 것을 의미한다.       

```
for( record1 IN TABLE1 ) {
    for( record2 IN TABLE2 ) {
        if( record1.join_column == record2.join_column ) {
            join_record_found(record1.*, record2.*);
        } else {
            join_record_notfound();
        }
    }
     
}
```

위 의사 코드에서 알 수 있듯이 조인은 2개의 반복 루프로 두 개의 테이블을 조건에 맞게 연결해주는 작업이다.    
(이 의사 코드의 for 반복문이 풀 테이블 스캔을 의미하는 것은 아니다.)        
 
두 개의 for문에서 바깥쪽을 아우터(OUTER) 테이블이라고 하며, 안쪽을 이너(INNER) 테이블이라고 표현한다.    
OUTER 테이블은 이너 테이블보다 먼저 읽어야 하며, 조인에서 주도적인 역할을 한다고 해서 드라이빙(Driving) 테이블이라고도 한다.    
INNER 테이블은 조인에서 끌려간다는 역할을 한다고 해서 드리븐(Driven) 테이블이라고도 한다.      

중첩된 반복 루프에서 최종적으로 선택될 레코드가 안쪽 반복 루프 (INNER 테이블)에 의해 결정되는 경우를 INNER JOIN이라고 한다.      
즉, 두 개의 반복 루프를 실행하면서 TABLE2(INNER 테이블)에 `if( record1.join_column == record2.join_column )` 조건을 만족하는 레코드만 조인의 결과로 가져온다.            

<br />   

#### OUTER JOIN   
```
for( record1 IN TABLE1) {
    for( record2 IN TABLE2 ) {
        if (record1.join_column == record2.join_column ) {
            join_record_found(record1.*, record2.*);
        } else {
            join_record_found(record1.*, NULL);
        }
    }
}
```

TABLE2(INNER 테이블)에 조건을 만족하는 레코드가 없는 경우 TABLE2의 칼럼을 모두 NULL로 채워서 가져온다.      
즉, INNER JOIN에서 일치하는 레코드를 찾지 못했을 때는 TABLE1의 결과를 모두 버리지만 TABLE1의 결과를 버리지 않고 그대로 결과에 포함한다.       

INNER 테이블이 조인의 결과에 전혀 영향을 미치지 않고 OUTER 테이블의 내용에 따라 조인의 결과가 결정되는 것이 OUTER JOIN의 특징이다.   
OUTER 테이블과 INNER 테이블의 관계 (대표적으로 1:M 관계)에 의해 최종 결과 레코드 건수가 늘어날 수는 있지만,    
OUTER 테이블의 결과가 INNTER 테이블에 일치하는 레코드가 없다고 해서 버려지지는 않는다.    
<br />    

OUTER JOIN은 조인의 결과를 결정하는 OUTER 테이블이 조인의 왼쪽에 있는지 오른쪽에 있는지에 따라   
LEFT OUTER JOIN과 RIGHT OUTER JOIN, FULL OUTER JOIN으로 다시 나뉜다.     

```
SELECT * 
FROM employees e
  LEFT OUTUER JOIN salaries s ON s.emp_no = e.emp_no;
  
SELECT * 
FROM salaries s 
  RIGHT OUTER JOIN employees e ON e.emp_no = s.emp_no;
```

* 첫 번째 쿼리는 LEFT OUTER JOIN을 사용했는데 LEFT OUTER JOIN 키워드의 왼쪽에 employees 테이블이 사용됐고   
오른쪽에 salaries 테이블이 사용됐기 때문에 employees가 OUTER 테이블이 된다.    
따라서 최종 결과는 salaries 테이블의 레코드 존재 여부에 관계없이 employees 테이블의 레코드에 의해 결정된다.   
<br />  
* 두 번째 쿼리는 RIGHT OUTER JOIN이 사용됐으며 RIGHT OUTER JOIN 키워드를 기준으로 오른쪽 employees 테이블이 사용됐고   
왼쪽에 salaries 테이블이 사용됐으므로 employees 테이블이 OUTER 테이블이 된다.   
따라서 최종 결과도 salaries 테이블의 레코드의 존재 여부에 관계없이 employees 테이블의 레코드에 의해 결정된다.    


<br />        

두 쿼리는 각각 LEFT OUTER JOIN과 RIGHT OUTER JOIN을 사용했지만 결국 같은 처리 결과를 만들어내는 쿼리다.    
결국 처리 내용이 같으므로 혼동을 막기 위해 LEFT OUTER JOIN으로 통일해서 사용하는 것이 일반적이다.    

JOIN 키워드를 기준으로 왼쪽의 테이블도 OUTER JOIN, 오른쪽의 테이블도 OUTER JOIN을 하고 싶은 경우 사용하는 쿼리가 FULL OUTER JOIN인데    
MySQL에서는 FULL OUTER JOIN을 지원하지 않는다. 하지만 INNER JOIN과 OUTER JOIN을 조금 섞어서 활용하면        
FULL OUTER JOIN과 같은 기능을 수행하도록 쿼리를 작성할 수 있다.        
<br />        


**LEFT OUTER JOIN**에서는 쉽게 실수할 수 있는 부분들이 여러 가지 있다.   
MySQL의 실행 계획은 INNER JOIN을 사용했는지 OUTER JOIN을 사용했는지를 알려주지 않으므로      
OUTER JOIN을 의도한 쿼리가 INNER JOIN으로 실행되지는 않는지 주의해야 한다.

* OUTER JOIN에서 레코드가 없을 수도 있는 쪽의 테이블에 대한 조건은 LEFT JOIN의 ON 절에 모두 명시해야 한다. 
그렇지 않으면 옵티마이저는 OUTER JOIN을 내부적으로 INNER JOIN으로 변형시켜 처리할 수도 있다.   
  
* LEFT OUTER JOIN의 ON 절에 명시되는 조건은 조인되는 레코드가 있을 때만 적용된다.   

* WHERE 절에 명시되는 조건은 OUTER JOIN이나 INNER JOIN에 관계없이 조인된 결과에 대해 모두 적용된다.    
<br />     
  

```
SELECT * 
FROM employees e
  LEFT OUTER JOIN salaries s ON s.emp_no=e.emp_no
WHERE s.salary > 5000;
```

위 쿼리의 LEFT OUTER JOIN 절과 WHERE 절은 서로 충돌되는 방식으로 사용된 것이다.     
OUTER JOIN으로 연결되는 테이블의 칼럼에 대한 조건이 ON 절에 명시되지 않고 WHERE 절에 명시됐기 때문이다.     
그래서 MySQL 서버는 이 쿼리를 최적화 단계에서 다음과 같은 쿼리로 변경한 후 실행한다.    
MySQL 옵티마이저가 쿼리를 변경해버리면 원래 쿼리를 작성했던 사용자의 의도와는 다른 결과를 반환받는다.   

```
SELECT * 
FROM employees e
  INNER JOIN salaries s ON s.emp_no=e.emp_no
WHERE s.salary > 5000;
```

이런 형태의 쿼리는 다음 2가지 중의 한 방식으로 수정해야 쿼리 자체의 의도나 결과를 명확히 할 수 있다.   

```
-- // 순수하게 OUTER JOIN으로 표현한 쿼리   
SELECT * 
FROM employees e
LEFT OUTER JOIN salaries s ON s.emp_no=e.emp_no AND s.salary > 5000;

-- // 순수하게 INNER JOIN으로 표현한 쿼리 
SELECT *
FROM employees e
INNER JOIN salaries s ON s.emp_no=e.emp_no
WHERE s.salary > 5000;
```

LEFT OUTER JOIN이 아닌 쿼리에서는 검색 조건이나 조인 조건을 WHERE 절이나 ON 절 중에서 어느 곳에 명시해도 성능상의 문제나 결과의 차이가 나지 않는다.   

<br />   

  
#### 카테시안 조인      
카테시안 조인은 FULL JOIN 또는 CROSS JOIN이라고도 한다.     
일반적으로 조인을 수행하기 위해 하나의 테이블에서 다른 테이블로 찾아가는 연결 조건이 필요하다.      
하지만 카테시안 조인은 이 조인 조건 자체가 없이 2개 테이블의 모든 레코드 조합을 결과로 가져오는 조인 방식이다.      
레코드 건수가 많아지면 조인의 결과 건수가 기하급수적으로 늘어나므로 MySQL 서버 자체를 응답 불능 상태로 만들어버릴 수도 있다.     

N개 테이블의 조인이 수행되는 쿼리에서는 반드시 조인 조건은 N-1개 (또는 그 이상)가 필요하며 모든 테이블은 반드시 1번 이상 조인 조건에 사용돼야 카테시안 조인을 피할 수 있다.       
조인되는 테이블이 많아지고 조인 조건이 복잡해질수록 의도하지 않은 카테시안 조인이 발생할 가능성이 크기 때문에 주의해야 한다.     

```
SELECT * FROM departments WHERE dept_no='d001';

SELECT * FROM employees WHERE emp_no=1000001;

SELECT * d.*, e.*
FROM department d, employees e
WHERE dept_no = 'd001' AND emp_no=1000001;
```

또한 카테시안 조인은 레코드 한 건만 조회하는 여러 개의 쿼리(전혀 연관이 없는 쿼리)를 하나의 쿼리로 모아서 실행하기 위해 사용되기도 한다.    
위 예제의 첫 번째와 두 번째 쿼리는 각각 레코드 1건씩을 조회하지만 전혀 연관이 없다.    
이 각각의 쿼리를 하나로 묶어서 실행하기 위해 세 번째 쿼리와 같이 하나의 쿼리로 두 테이블을 조인해서 한번에 결과를 가져오고 있다.   
하지만 employees 테이블과 departments 테이블을 연결해주는 조인 조건은 없음을 알 수 있다.    
위와 같이 2개의 쿼리를 하나의 쿼리처럼 빠르게 실행하는 효과를 얻을 수도 있다.    
하지만 카테시안 조인으로 묶은 2개의 단위 쿼리가 반환하는 레코드가 항상 1건이 보장되지 않으면 아무런 결과도 못 가져오거나   
기대했던 것보다 훨씬 많은 결과를 받게 될 수도 있으므로 주의해야 한다.    

SQL 표준에서 CROSS JOIN은 카테시안 조인과 같은 조인 방식을 의미하고 MySQL에서 CROSS JOIN은 INNER JOIN과 같은 조인 방식을 의미한다.    
MySQL에서 CROSS JOIN을 사용하는 경우 INNER JOIN과 같이 ON 절이나 WHERE 절에 조인 조건을 부여하는 것이 가능하며,     
이렇게 작성된 CROSS JOIN은 INNER JOIN과 같은 방식으로 작동한다.    
그래서 MySQL에서 CROSS JOIN은 카테시안 조인이 될 수도 있고, 아닐 수도 있다.    

다음 두 예제는 같은 결과를 만들어 낸다.    

```
SELECT d.*, e.* 
FROM departments d 
  INNER JOIN employees e ON d.emp_no=e.emp_no;
  
SELECT d.*, e.* 
FROM departments d 
  CROSS JOIN employees e ON d.emp_no=e.emp_no;
```

MySQL에서 카테시안 조인과 이너 조인은 문법으로 구분되는 것이 아니다.     
조인으로 연결되는 조건이 적절히 있다면 이너 조인으로 처리되고, 연결 조건이 없다면 카테시안 조인이 된다.      
그래서 CROSS JOIN이나 INNER JOIN을 특별히 구분해서 사용할 필요는 없다.       

<br />    

#### NATURAL JOIN    
MySQL에서 INNER JOIN의 조건을 명시하는 방법은 여러 가지가 있다.    


```
SELECT * 
FROM employees e, salaries s 
WHERE e.emp_no=s.emp_no;

SELECT * 
FROM employees e
  INNER JOIN salaries s ON s.emp_no=e.emp_no;
  
SELECT * 
FROM employees e 
  INNER JOIN salaries s USING (emp_no);
```


세 쿼리는 표기법의 차이가 있을 뿐, 전부 같은 쿼리다.    
세 번째의 `USING(emp_no)`은 두 번째 쿼리의 `ON s.emp_no=e.emp_no`과 같은 의미로 사용된다.    
USING 키워드는 조인되는 두 테이블의 조인 칼럼이 같은 이름을 가지고 있을 때만 사용할 수 있다.     
<br />    

NATURAL JOIN 또한 INNER JOIN과 같은 결과를 가져오지만 표현 방법이 조금 다른 조인 방법 중 하나다.    

```
SELELCT * 
FROM employees e
  NATURAL JOIN salaries s;
```


위 쿼리도 employees 테이블의 emp_no 칼럼와 salaries 테이블의 emp_no 칼럼을 조인하는 쿼리다.    
NATURAL JOIN은 employees 테이블에 존재하는 칼럼과 salaries 테이블에 존재하는 칼럼 중에서 서로 이름이 같은 칼럼을 모두 조인 조건으로 사용한다.   
employees 테이블과 salaries 테이블에는 이름이 같은 칼럼으로 emp_no만 존재하기 때문에 결국 위 쿼리는   
`INNER JOIN salaries s ON s.emp_no=e.emp_no`와 같은 의미다.   

NATURAL JOIN은 조인 조건을 명시하지 않아도 된다는 편리함이 있지만 각 테이블의 칼럼 이름에 의해 쿼리가 자동으로 변경될 수 있다는 문제가 있다.    
이러한 방식의 조인이 있다는 것만 알아두자.   

<br />   

### 2) Single-sweep multi join     
MySQL의 네스티드-루프 조인을 자주 "Single-sweep multi join"이라고 표현하기도 한다.   
"Single-sweep multi join"의 의미는 조인에 참여하는 테이블의 개수만큼 for나 while과 같은 반복 루프가 중첩되는 것을 말한다.    

```
SELECT d.dept_name, e.first_name
FROM departments d, employees e, dept_emp de 
WHERE de.dept_no=d.dept_no
  AND e.emp_no=de.emp_no;
```

이 쿼리의 실행 계획은 다음과 같다.   


|id|select_type|Table|type|key|key_len|ref|rows|Extra|
|---|---|---|---|---|---|---|---|---|
|1|SIMPLE|d|index|ux_deptname|123|NULL|9|Using index|
|1|SIMPLE|de|ref|PRIMARY|12|employees.d.dept_no|18603|Using index|
|1|SIMPLE|e|eq_ref|PRIMARY|4|employees.de.emp_no|1| | 

실행 계획을 보면, 제일 먼저 d 테이블이 읽히고, 그다음으로 de 테이블, e 테이블이 읽혔다는 것을 알 수 있다.   
for 반복문으로 표시해 보면 다음과 같다.   

```
for(record1 IN departments) {
  for(record2 IN dept_emp && record2.dept_no = record1.dept_no) {
    for(record3 IN employees && record3.emp_no = record2.emp_no) {
      return {record1.dept_name, record3.first_name}
    }
  }
}
```

3번 중첩이 되긴 했지만 전체적으로 반복 루프는 1개다.    
즉 반복 루프를 돌면서 레코드 단위로 모든 조인 대상 테이블을 차례대로 읽는 방식을 "Single-sweep multi join"이라고 한다.              
MySQL 조인의 결과는 드라이빙 테이블을 읽은 순서대로 레코드가 정렬되어 반환되는 것이다.     
조인에서 드리븐 테이블들은 단순히 드라이빙 테이블의 레코드를 읽는 순서대로 검색만 할 뿐이다.      

<br />    

### 3) 조인 버퍼를 이용한 조인(Using join buffer)    
조인은 드라이빙 테이블에서 일치하는 레코드의 건수만큼 드리븐 테이블을 검색하면서 처리된다.    
즉, 드라이빙 테이블은 한 번에 쭉 읽게 되지만 드리븐 테이블은 여러 번 읽는 것을 의미한다.     
그래서 드리븐 테이블을 검색할 때 인덱스를 사용할 수 없는 쿼리는 상당히 느려지며,          
MySQL 옵티마이저는 최대한 드리븐 테이블의 검색이 인덱스를 사용할 수 있게 실행 계획을 수립한다.       

어떤 방식으로도 드리븐 테이블의 풀 테이블 스캔이나 인덱스 풀 스캔을 피할 수 없다면 옵티마이저는 드라이빙 테이블에서 읽은 레코드를 메모리에 캐시한 후   
드리븐 테이블과 이 메모리 캐시를 조인하는 형태로 처리한다.    
이때 사용되는 메모리의 캐시를 조인 버퍼(join buffer)라고 한다.    
조인 버퍼는 조인이 완료되면 바로 해제된다.    

두 테이블이 조인되는 다음 예제에서 각각 테이블에 대한 조건은 WHERE 절에 있지만 두 테이블 간의 연결 고리 역할을 하는 조인 조건은 없다.    
그래서 카테시안 조인을 수행한다.   

```
SELECT * 
FROM dept_emp de, employees e
WHERE de.from_date > '1995-01-01' AND e.emp_no < 109004;
```

이 쿼리가 조인 버퍼 없이 실행되고 dept_emp는 드라이빙 테이블이 되고 employees 테이블이 드리븐 테이블이 되어 조인을 수행한다고 가정하면,             
dept_emp 테이블의 각 레코드에 대해 employees 테이블을 읽을 때 드리븐 테이블에서 가져오는 결과는 매번 같지만 작업을 실행하게 된다.   
<br />        

같은 처리를 조인 버퍼를 사용하게 되면 어떻게 달라질까?    
위 쿼리의 실행 계획을 보면 dept_emp 테이블이 드라이빙 테이블이 되어 조인되고, employees 테이블을 읽을 때는 조인 버퍼를 이용한다는 것을   
Extra 칼럼의 내용으로 알 수 있다.  



|id|select_type|Table|type|key|key_len|ref|rows|Extra|
|---|---|---|---|---|---|---|---|---|
|1|SIMPLE|de|range|ix_fromdate|3| |20550|Using where|
|1|SIMPLE|d|range|PRIMARY|4| |148336|Using where; <br /> Using join buffer| 


단계별로 잘라서 실행 내역을 살펴보자.    

1. dept_emp 테이블의 ix_fromdate 인덱스를 이용해 조건을 만족하는 레코드를 검색한다.   
2. 조인에 필요한 나머지 칼럼을 모두 dept_emp 테이블로부터 읽어서 조인 버퍼에 저장한다.    
3. employees 테이블의 프라이머리 키를 `e.emp_no < 109004` 이용해 조건을 만족하는 레코드를 검색한다.      
4. 3번에서 검색된 결과에 2번의 캐시된 조인 버퍼의 레코드를(dept_emp) 결합해서 반환한다.   
<br />    


<img width="770" alt="스크린샷 2021-05-02 오후 3 14 46" src="https://user-images.githubusercontent.com/33855307/116804373-28360500-ab59-11eb-8391-3b117fdd7055.png">


위 그림에서 중요한 점은 조인 버퍼가 사용되는 쿼리에서는 조인의 순서가 거꾸로인 것처럼 실행된다는 것이다.    
위에서 설명한 4번 단계가 employees 테이블의 결과를 기준으로 dept_emp 테이블의 결과를 결합한다는 것을 의미한다.    
실제 이 쿼리의 실행 계획상으로는 dept_emp 테이블이 드라이빙 테이블이 되고, employees 테이블이 드리븐 테이블이 된다.     

하지만 실제 드라이빙 테이블의 결과는 조인 버퍼에 담아두고 드리븐 테이블을 먼저 읽고 조인 버퍼에서 일치하는 레코드를 찾는 방식으로 처리된다.    
조인이 수행된 후 가져오는 결과는 드라이빙 테이블의 순서에 의해 결정되지만 조인 버퍼가 사용되는 조인에서는 결과의 정렬 순서가 흐트러질 수 있음을 기억해야 한다.    

<br />    


### 4) 조인 관련 주의사항    
MySQL의 조인 처리에서 특별히 주의해야 할 부분은 **실행 결과의 정렬 순서**, **INNER JOIN과 OUTER JOIN의 선택** 2가지 정도일 것이다.     
<br />   

#### 조인 실행 결과의 정렬 순서      
일반적으로 조인으로 쿼리가 실행되는 경우, 드라이빙 테이블로부터 레코드를 읽는 순서가 전체 쿼리의 결과 순서에 그대로 적용된다.    
이는 네스티드-루프 조인 방식의 특징이기도 하다.   

```
SELECT de.dept_no, e.emp_no, e.first_name
FROM dept_emp de, employees e
WHERE e.emp_no = de.emp_no
  AND de.dept_no='d005';
```

이 쿼리의 실행 계획을 보면 dept_emp 테이블의 프라이머리 키로 먼저 읽었다는 것을 알 수 있다.    
그리고 dept_emp 테이블로부터 읽은 결과를 가지고 employees 테이블의 프라이머리 키를 검색하는 과정으로 처리되었다.    

dept_emp 테이블의 프라이머리 키는 (dept_no + emp_no)로 생성돼 있기 때문에 dept_emp 테이블을 검색한 결과는 dept_no 칼럼 순서대로 정렬되고   
다시 emp_no로 정렬되어 반환된다는 것을 예상할 수 있다.   
그러나 WHERE 조건에 `de.dept_no='d005'`로 고정돼 있으므로 emp_no로 정렬된 것과 같다.    
ORDER BY로 명시하지는 않았지만 emp_no로 정렬된 효과를 얻을 수 있다.    

하지만 결과가 이 순서로 반환된 것은 옵티마이저가 여러 가지 실행 계획 중에서 위의 실행 계획을 선택했기 때문이다.    
만약 옵티마이저가 다른 실행 계획을 선택했다면 결과는 보장되지 않는다.    
정렬되어 결과가 반환되기를 바란다면 ORDER BY 절을 추가해서 정렬이 보장될 수 있게 해야 한다.    

<br />   

#### INNER JOIN과 OUTER JOIN의 선택      
INNER JOIN은 조인의 양쪽 테이블 모두 레코드가 존재하는 경우에만 레코드가 반환된다.     
OUTER JOIN은 OUTER 테이블에 존재하면 레코드가 반환된다.     
쿼리나 테이블의 구조를 살펴보면 OUTER JOIN을 사용하지 않아도 될 때 OUTER JOIN으로 사용할 때가 많다.    
용도가 다르므로 적절한 사용법을 익히고 요구되는 요건에 맞게 사용해야 한다.   

OUTER JOIN과 INNER JOIN은 실제 가져와야 하는 레코드가 같다면 쿼리의 성능은 거의 차이가 발생하지 않는다.                  
다음 두 쿼리는 실제 비교를 수행하는 건수나 최종적으로 가져오는 결과 건수가 같다.                    
(여기서 SQL_NO_CACHE와 STRAIGHT_JOIN은 조건을 같게 만들어주기 위한 힌트다.)      


```
SELECT SQL_NO_CACHE STRAIGHT_JOIN COUNT(*)
FROM dept_emp de
  INNER JOIN employees e ON e.emp_no=de.emp_no;
  
SELECT SQL_NO_CACHE STRAIGHT_JOIN COUNT(*)
FROM dept_emp de
  LEFT JOIN employees e ON e.emp_no=de.emp_no;
```


INNER JOIN과 OUTER JOIN은 성능을 고려해서 선택할 것이 아니라 업무 요건에 따라 선택하는 것이 바람직하다.        
레코드가 결과에 포함되지 않을까 걱정스러운 경우라면 테이블의 구조와 데이터의 특성을 분석해          
INNER JOIN을 사용해야 할지 OUTER JOIN을 사용해야 할지 결정해야 한다.       

<br />    

# 실행 계획 분석 시 주의사항      
## 1. Select_type 칼럼의 주의 대상        

#### * DERIVED       
  DERIVED는 FROM 절에 사용된 서브 쿼리로부터 밟생한 임시 테이블을 의미한다.    
  임시 테이블은 메모리에 저장될 수도 있고 디스크에 저장될 수도 있다.    
  일반적인 경우 크게 성능에 영향을 미치지 않지만 데이터의 크기가 커서 임시 테이블을 디스크에 저장하면 성능이 떨어진다.    
<br />  

#### * UNCACHEABLE SUBQUERY      
  쿼리의 FROM 절 이외의 부분에서 서브 쿼리는 가능하면 MySQL 옵티마이저가 최대한 캐시되어 재사용 될 수 있게 유도한다.   
  하지만 사용자 변수나 일부 함수가 사용된 경우에는 이러한 캐시 기능을 사용할 수 없게 만든다.    
<br />     

#### * DEPENDENT SUBQUERY       
쿼리의 FROM 절 이외의 부분에서 사용하는 서브 쿼리가 자체적으로 실행되지 못하고 외부 쿼리에서 값을 전달받아 실행되는 경우 DEPENDENT SUBQUERY가 표시된다.    
이는 서브 쿼리가 먼저 실행되지 못하고 서브 쿼리가 외부 쿼리의 결과 값에 의존적이기 때문에 전체 쿼리 성능을 느리게 만든다.    
서브 쿼리가 불필요하게 외부 쿼리의 값을 전달받고 있는지 검토해서 가능하면 외부 쿼리와의 의존도를 제거하는 것이 좋다.         

<br />           

## 2. Type 칼럼의 주의 대상      
### ALL, index      
index는 인덱스 풀 스캔을 의미하며, ALL은 풀 테이블 스캔을 의미한다.    
둘 다 대상의 차이만 있지 전체 레코드를 대상으로 하는 작업 방식이라서 빠르게 결과를 가져오기는 어렵다.    
일반적인 OLTP 환경에 적합한 접근 방식은 아니므로 새로운 인덱스를 추가하거나 쿼리의 요건을 변경해서 이러한 접근 방법을 제거하는 것이 좋다.    

<br />   

## 3. Key 칼럼의 주의 대상    
쿼리가 인덱스를 사용하지 못할 때 실행 계획의 Key 칼럼에 아무 값도 표시되지 않는다.    
쿼리가 인덱스를 사용할 수 있게 인덱스를 추가하거나, WHERE 조건을 변경하는 것이 좋다.    

<br />   

## 4. Rows 칼럼의 주의 대상    
* 쿼리가 실제 가져오는 레코드 수보다 훨씬 더 큰 값이 Rows 칼럼에 표시되는 경우에는 쿼리가 인덱스를 정상적으로 사용하고 있는지    
  그리고 그 인덱스가 충분히 작업 범위를 좁혀 줄 수 있는 칼럼으로 구성됐는지 검토해보는 것이 좋다.   
  인덱스가 효율적이지 않다면 충분히 식별성을 가지고 있는 칼럼을 선정해 인덱스를 다시 생성하거나 쿼리의 요건을 변경해보는 것이 좋다.    
<br />  
  
* Rows 칼럼의 수치를 판단할 때 주의해야 할 점은 LIMIT가 포함된 쿼리라 하더라도 LIMIT의 제한은 Rows 칼럼의 고려 대상에서 제외된다는 것이다.    
즉 `LIMIT 1`로 1건만 SELECT 하는 쿼리라 하더라도 Rows 칼럼에는 훨씬 큰 수치가 표현될 수 있으며 성능상 아무런 문제가 없고 최적화된 쿼리일 수도 있다는 것이다.    
  

