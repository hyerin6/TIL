# SQL 레벨에서의 접근법         

## 데이터 흐름 

조인은 여러 테이블에서 하나의 결과를 가져올 수 있다는 점에서 편리하다.    
그러나 무분별한 조인은 데이터가 누적됨에 따라 쿼리 성능이 점진적으로 저하된다.   

```
SELECT a.*,
       d.text_field AS text_field1,
       e.text_field AS text_field2
FROM tab01 a
  INNER JOIN tab02 b ON b.tab01_seq = a.seq
  INNER JOIN tab03 c ON c.tab02_seq = b.seq
  LEFT JOIN tab04 d ON b.tab03_seq = c.seq
  LEFT JOIN tab05 e ON b.tab03_seq = c.seq
WHERE a.status IN ('01', '02')
  AND b.status NOT IN('99')
  ORDER BY c.regdate, b.regdate DESC
  LIMIT 0, 20;
```

쿼리 실행 계획을 보면 ORDER BY 구문을 처리하기 위해 Temporary Table(임시 테이블)과 Filesort가 발생한다.   
데이터 수가 적다면 큰 문제가 없겠지만 텍스트 혹은 대용량 데이터 같은 칼럼이 있다면 성능이 급격히 저하된다.   

위에서 데이터를 처리하기 위한 칼럼은 a.status, b.status, b.regdate, c.regdate이다.  
그러나 쿼리를 처리하는 동안 네 개의 칼럼 외에도 내부적으로 칼럼을 가지고 있다.   
text_field와 같은 칼럼은 사용자에게 보여주기 위한 데이터로, 데이터 처리 단계에서는 굳이 필요 없다.   
불필요한 칼럼이 조인 및 정렬에 연관되면 성능이 저하된다.   

다음과 같이 변경해보자. 

```
SELECT aa.*,
       dd.text_field AS text_field1,
       ee.text_field AS text_field2
FROM( 
    SELECT a.seq tab01, c.seq tab03_seq
    FROM tab01 a
    INNER JOIN tab02 b ON b.tab01_seq = a.seq
    INNER JOIN tab03 c ON c.tab02_seq = b.seq
    WHERE a.status IN('01', '02')
    AND b.status NOT IN('99')
    ORDER BY c.regdate, b.regdate DESC
    LIMIT 0, 20
) step01
INNER JOIN tab01 aa ON aa.seq = step01.tab01_seq
LEFT JOIN tab04 dd ON dd.tab03_seq = step01.tab03_seq
LEFT JOIN tab05 ee ON ee.tab03_seq = step01.tab03_seq
```


쿼리 실행 계획은 다음과 같다. 

<img width="650" alt="스크린샷 2021-12-11 오후 3 52 26" src="https://user-images.githubusercontent.com/33855307/145667472-3a0c12e7-86e1-4eaa-bc8d-b1ee681f6869.png">

조인에 필요한 데이터만 서브 쿼리에서 유지하고 결과값 20건만 외부에서 다시 조인하기 때문에   
데이터 연산에 반드시 필요한 데이터만 메모리에서 먼저 연산한다.   

다수의 조인을 피할 수 없다면 필요한 데이터만 가져와서 데이터를 처리하고 다시 조인을 수행하는 것이 성능 향상의 포인트다.  

<br />













































