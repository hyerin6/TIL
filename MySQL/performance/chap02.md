<br />

# 쿼리 성능 진단을 최적화의 기초   

## [쿼리 실행 계획](https://github.com/hyerin6/TIL/blob/main/MySQL/real-mysql/%EC%8B%A4%ED%96%89%EA%B3%84%ED%9A%8D/%EC%8B%A4%ED%96%89%EA%B3%84%ED%9A%8D.md)     
DB는 내부적으로 SQL 파싱을 하고 옵티마이징(데이터를 찾는 가장 빠른 방법을 찾아내는 단계)을 거친 후 실제로 데이터를 찾는다.   


* 실행할 쿼리 앞에 EXPLAIN을 붙여주면 된다.   

```
EXPLAIN [EXTENDED]
SELECT .. FROM .. WHERE .. 
```

* 실행 결과 

```
+----+-------------+------------+------+---------------+----------+---------+------+------+--------+  
| id | select_type |   table    | type | possible_keys |   key    | key_len | ref  | rows | Extra  |  
+----+-------------+------------+------+---------------+----------+---------+------+------+--------+  
|1   |SIMPLE       |table_name  |ref   |ix_column      |ix_column |4        |const |1     |        |  
+----+-------------+------------+------+---------------+----------+---------+------+------+--------+  
1 row in set, 1 warning (0.00 sec) 
``` 


EXTENDED는 실제 DB에서 처리되는 최종 SQL 형태를 보여주기 위한 명령어다.   
`EXPLAIN EXTENDED`로 쿼리 실행 계획을 출력하면 위와 같은 형태로 출력되나 warning이 한 개 떠 있다.   

이 상태에서 `SHOW WARNINGS\G` 쿼리를 치면 결과로 다음과 같ㅇ느 쿼리가 출력되는데   
이것이 바로 DB가 내부적으로 이해한 최종적인 형태다.   

```
SHOW WARINIGS\G
******************************** 1. row ********************************
Level1: Note
Code: 1003
Message: select `db`.`tab`.`j` .. from `db`.`tab` where
`db`.`tab`.`k` = 1 
```

<br />

실행 계획 결과를 읽는 순서는 두 가지다.   


![1](https://user-images.githubusercontent.com/33855307/144049755-36866922-23e8-4421-b5a7-b1d3e6aab0dd.jpeg)

<br />

(1) 동일한 ID끼리는 위에서 아래 방향으로 향한다.    
(2) 위에서 아래로 읽는 도중 table 항목에 <drived번호> 항목을 만나면    
번호에 해당하는 ID로 가서 첫 번째 단계를 수행한 후 돌아와 나머지를 수행한다.       

<br />  

각 항목의 의미를 다음과 같이 정리할 수 있다.  

* ID: Select 아이디로 Select를 구분하는 번호    
* Select_type: Select에 대한 타입   
* Table: 참조하는 테이블
* Type: 조인 혹은 조회 타입 
* Possible_keys: 데이터를 조회할 때 DB에서 사용할 수 있는 인덱스 리스트   
* Key: 실제로 사용할 인덱스 
* Key_len: 실제로 사용할 인덱스의 길이 
* Ref: Key 안의 인덱스와 비교하는 칼럼 (상수) 
* Rows: 쿼리 실행 시 조사하는 행 수 
* Extra: 추가 정보   

<br />


![2](https://user-images.githubusercontent.com/33855307/144052425-93b83064-4bef-454f-89c5-1bc4e7cfa545.jpeg)

![3](https://user-images.githubusercontent.com/33855307/144052433-2428134c-e87a-4321-ad8e-a94f17583919.jpeg)


Type에서 `system`에서 `all`로 갈수록 성능이 좋지 않은 것이다.       
단, range에서 조회 데이터 건수가 많지 않다면 성능이 나쁘다고 볼 수 없다.        
초록/파란 상태라면 쿼리가 나쁘지 않은 상태라고 볼 수 있다.        

<br />

`Extra` 항목은 MySQL의 쿼리 실행에 대한 추가적인 정보를 보여준다.   
여러 항목 중 성능과 직접적인 관련이 있는 4가지에 대해 알아보자.   


#### Using Index
커버링 인덱스라고도 하며 인덱스 자료 구조를 이용해서 데이터를 추출한다.   


#### Using Where  
Where 조건으로 데이터를 추출한다.   
Type이 All 혹은 Index 타입과 함께 표현되면 성능이 좋지 않다는 의미다.   



#### Using Filesort 
데이터 정렬이 필요한 경우로 메모리 혹은 디스크 상에서의 정렬을 모두 포함한다.   
결과 데이터가 많은 경우 성능에 직접적인 영향을 미친다.   


#### Using Temporary  
쿼리 처리 시 내부적으로 Temporary Table이 사용되는 경우를 의미한다.   

<br />  

일반적으로 데이터가 많은 경우 `Using Filesort`와 `Using Temporary` 상태는 좋지 않으면 반드시 쿼리 튜닝이 필요하다.   

<br />  

## 쿼리 프로파일링   
쿼리를 처리할 때 내부적으로는 Open Table/Close Table, Optimizing, Sending Data 등을 비롯해 여러 단계를 거치며 최종적으로 데이터를 찾아낸다.   

`EXPLAIN`으로 쿼리 실행 시 DB가 데이터를 찾아가는 일련의 과정을 알아봤다.   

실제 쿼리 실행 시 병목이 되는 부분을 찾아내는 방법을 알아보자.   

<br />

다음과 같이 프로파일링 관련 세션 변수를 활성화한 후 쿼리를 실행하면 프로파일링된 정보를 확인할 수 있다.   

```
SET PROFILING = 1;
SELECT * FROM tab;
SHOW PROFILING;
```

가장 최근에 실행한 쿼리에 대한 프로파일링 정보가 나온다.           
프로파일링 세션 활성화 이후 실행된 쿼리 리스트를 확인하려면 `SHOW PROFILES;` 명령을 실행하면 된다.      

```
+----------------------+----------+
| Status               | Duration |
+----------------------+----------+
| starting             | 0.000037 | (시작)
| checking permissions | 0.000005 | (사용권한확인)
| Opening tables       | 0.000012 | (테이블열기)
| After opening tables | 0.000007 | (테이블을 연 후에)
| System lock          | 0.000004 | (시스템잠금)
| Table lock           | 0.000006 | (테이블잠금)
| init                 | 0.000018 | (초기화)
| optimizing           | 0.000007 | (최적화)
| statistics           | 0.000010 | (통계)
| preparing            | 0.000014 | (준비)
| executing            | 0.000004 | (실행)
| Sending data         | 0.212919 | (데이터보내기)
| end                  | 0.000010 | (끝)
| query end            | 0.000009 | (질의끝)
| closing tables       | 0.000005 | (테이블닫기)
| Unlocking tables     | 0.000012 | (잠금해제테이블)
| freeing items        | 0.000006 | (항목해방)
| updating status      | 0.000029 | (상태업데이트)
| cleaning up          | 0.000003 | (청소)
+----------------------+----------+
```


어느 부분에서 병목이 있는지 대략적으로 알 수 있다.      
프로파일링 결과에서 Sending data(스토리지 엔진에서 DB 엔진으로 쿼리를 보내는 단계) 부분이 오래 걸린 것으로 나왔다면    
스토리지 엔진이 DB 엔진으로 데이터를 적게 보내도록 쿼리를 튜닝한다.     


