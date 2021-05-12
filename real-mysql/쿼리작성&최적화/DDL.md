# 쿼리와 연관된 시스템 설정
SQL 작성 규칙은 MySQL 서버의 시스템 설정에 따라 달라진다.

* SQL 모드    
  MySQL 서버의 sql_mode 라는 시스템 설정은 SQL 작성과 결과에 영향을 미친다.      
  예를들어, sql_mode를 STRICT_ALL_TABLES로 설정하면 저장하려는 값이 길이가 칼럼의 길이보다 더 길더라도 초과하는 부분은 버리고 에러가 발생하지 않는다.       
  <br />
* 영문 대소문자 구분    
  MySQL 서버는 설치된 운영체제에 따라 테이블명의 대소문자를 구분한다.        
  구분하지 않게 설정이 가능하지만 초기 DB나 테이블 생성 시 대문자만 혹은 소문자만 통일해서 사용하는 편이 좋다.      
  <br />
* MySQL 예약어    
  예약어는 역따옴표(`)나 쌍따옴표로 감싸는데 테이블 생성시 역따옴표를 사용하면 예약어인지 아닌지 에러나 경고를 보여주지 않는다.        
  역따옴표로 테이블이나 칼럼 이름을 둘러싸지 않아야 에러가 뜨기 때문에 둘러싸지 않아야 한다.

<br />     

# MySQL 연산자와 내장 함수

## 1. 리터럴 표기법
### 문자열
SQL 표준에서 문자열은 항상 홑따옴표(')를 사용해서 표기
```
dept_no = 'd001'
```
문자열에 홑따옴표가 포함된 경우 두 번 연속해서 입력하거나 쌍따옴표를 사용하면 된다.    
<br />

### 숫자
숫자는 따옴표(' 또는 ") 없이 숫자 값만 입력하면 된다.    
MySQL  서버가 문자열 값을 숫자 값으로 자동 변환해준다.

```
WHERE number_col='10001';

```
위 쿼리는 상수값을 숫자로 변환하는데, 이때는 하나만 변환하므로 성능과 관련된 이슈가 발생하지 않는다.


```
WHERE string_col=10001;
```

위 쿼리는 주어진 상수값이 숫자인데 비교되는 칼럼은 문자열 칼럼이다. MySQL은 문자열 칼럼을 숫자로 변환해서 비교한다.       
string_col 칼럼의 모든 문자열 값을 숫자로 변환해서 비교를 수행하기 때문에 **인덱스를 이용하지 못한다.**           
또 string_col 칼럼에 숫자말고 다른 문자가 포함되어 있으면 쿼리 자체가 실패해버린다.       
<br />

### 날짜
MySQL에서는 정해진 형태의 날짜 포맷으로 표기하면 자동으로 DATE나 DATETIME 값으로 변환하기 때문에 함수를 사용하지 않아도 된다.

```
WHERE from_date = '2011-04-29';
WHERE from_date = STR_TO_DATE('2011-04-29', '%Y-%m-%d');
```

두 쿼리의 차이점은 거의 없다.   
첫 번째 쿼리와 같이 비교해도 from_date 타입을 문자열로 변환해서 비교하지 않기 때문에 from_date로 생성된 **인덱스를 이용하는데 문제가 되지 않는다.**

<br />   

## 2. MySQL 연산자

### BETWEEN 연산자
"크거나 같다"와 "작거나 같다"라는 두 개의 연산자를 하나로 합친 연산자다.      
BETWEEN 연산자는 다른 비교 조건과 결합해 하나의 인덱스를 사용할 때 주의해야 할 점이 있다.

```
SELECT * FROM dept_emp
WHERE dept_no='d003' AND emp_no=10001;

SELECT * FROM dept_emp
WHERE dept_no BETWEEN 'd003' AND 'd005' AND emp_no=10001;
```

dept_emp 테이블에는 (dept_no + emp_no) 칼럼으로 인덱스가 생성돼 있다.    
첫 번째 쿼리는 dept_no와 emp_no 조건 모두 인덱스를 이용해 범위를 줄여주는 방법으로 사용할 수 있다.

두 번째 쿼리에서 사용한 BETWEEN은 범위를 읽어야 하는 연산자라서 'd003' 보다 크거나 같고 'd005'보다 작거나 같은 모든 인덱스의 범위를 검색해야만 한다.   
결국 BETWEEN이 사용된 두 번째 쿼리에서 `emp_no=10001` 조건은 비교 범위를 줄이는 역할을 하지 못한다.   
<br />

또한, BETWEEN과 다르게 IN 연산자의 처리 방법은 동등 비교(=) 연산자와 비슷하다.      
IN과 동등 비교 연산자는 같은 형태로 인덱스를 사용하게 된다.

두 번째 쿼리는 'd003'과 'd005' 사이의 인덱스의 상당히 많은 레코드를 읽게 된다.    
이 쿼리를 다음과 같은 형태로 바꾸면 `emp_no=10001` 조건도 작업 범위를 줄이는 용도로 인덱스를 사용할 수 있게 된다.

```
SELECT * FROM dept_emp
WHERE dept_no IN ('d003', 'd004', 'd005') AND emp_no=10001;
```

BETWEEN이 선형으로 인덱스를 검색해야 하는 것과는 달리 IN은 동등 비교를 여러 번 수행하는 것과 같은 효과가 있기 때문에    
dept_emp 테이블의 인덱스 (dept_no + emp_no)를 최적으로 사용할 수 있는 것이다.

여러 칼럼으로 인덱스가 만들어져 있는데 인덱스 앞쪽에 있는 칼럼의 선택도가 떨어질 때는 IN으로 변경하는 방법으로 쿼리의 성능을 개선할 수도 있다.

그러나 IN 연산자에 사용할 상수 값을 가져오기 위해 별도의 SELECT 쿼리를 한번 더 실행해야 할 때도 있다.      
이때 적절한 쿼리를 한 번 더 실행해서 IN으로 변경했을 때 그만큼 효율이 있는지 테스트해보는 것이 좋다.        
그러나 다음과 같은 IN (subquery) 형태로 쿼리를 변경하면 더 나쁜 결과를 가져올 수도 있기 때문에 변경하지 않는 것이 좋다.

```
SELECT * FROM dept_emp
WHERE dept_no IN (
    SELECT dept_no
    FROM departments 
    WHERE dept_no BETWEEN 'd003' AND 'd005') 
  AND emp_no=10001;
```

<br />   


### IN 연산자
MySQL에서 IN 연산자는 사용법에 따라 상당히 비효율적으로 처리될 때도 많다.     
IN 연산자에 상수값을 입력으로 전달하는 경우 최적화해서 수행할 수 있다.      
하지만 입력이 상수가 아니라 서브 쿼리인 경우 서브 쿼리가 아니라 서브 쿼리의 외부가 먼저 실행되고          
IN(subquery)는 체크 조건으로 사용되기 때문에 상당히 느려질 수 있다.

> IN 연산자는 NULL 값을 검색할 수 없다.   
> NULL-Safe dustkswkdls "<=>" 또는 IS NULL 연산자 등을 이용해야 한다.
> NOT IN의 실행 계획은 인덱스 풀 스캔으로 표시되는데 동등이 아닌 부정형 비교라서 인덱스를 이용해 처리 범위를 줄이는 조건으로는 사용할 수 없기 때문이다.


<br />     

# SELECT
## 1. SELECT 각 절의 처리 순서

<img width="1122" alt="스크린샷 2021-05-08 오후 10 48 48" src="https://user-images.githubusercontent.com/33855307/117541603-95f99980-b04f-11eb-8299-069ab338ec86.png">    


첫 번째 그림으로 실행되지 않는 경우는 거의 없으며 SQL에는 ORDER BY나 GROUP BY 절이 있다 하더라도        
인덱스를 이용해 처리할 때는 그 단계 자체가 불필요하므로 생략된다.


두 번째 그림은 ORDER BY가 사용된 쿼리에서 예외적인 순서로 실행되는 경우이다.       
첫 번째 테이블만 읽어서 정렬을 수행한 뒤에 나머지 테이블을 읽는데 주로 GROUP BY 절이 없이 ORDER BY만 사용된 쿼리에서 사용될 수 있는 순서다.

<br />   

## 2. WHERE 절과 GROUP BY 절, ORDER BY 절의 인덱스 사용
### 인덱스를 사용하기 위한 기본 규칙
인덱스를 사용하려면 기본적으로 인덱스된 칼럼의 값 자체를 변환하지 않고 그대로 사용한다는 조건을 만족해야 한다.       
또한, 비교 조건에서 연산자 양쪽의 두 비교 대상 값은 데이터 타입이 일치해야 한다.      
일치하지 않으면 인덱스 레인지 스캔을 사용하지 못하고 인덱스 풀 스캔을 사용한다.

```
SELECT * FROM salaries WHERE salary*10 > 150000;
```

위 쿼리는 다음과 같이 salary 칼럼의 값을 변경하지 않고 검색하도록 유도할 수 있지만   
MySQL 옵티마이저에서 인덱스를 최적으로 이용할 수 있게 표현식을 변환하지는 못한다.

```
SELECT * FROM salaries WHERE salary > 150000/10;
```

위 쿼리는 단순한 예제이며, 칼럼값 여러 개를 곱하거나 더해서 비교해야 하는 복잡한 연산이 있다면      
미리 계산된 값을 저장할 별도의 칼럼을 추가하고 그 칼럼에 인덱스를 생성하는 것이 유일한 해결책이다.

<br />   

### WHERE 절의 인덱스 사용
WHERE 조건이 인덱스를 사용하는 방법
* 범위 제한 조건      
  이는 동등 비교 조건이나 IN으로 구성된 조건이 인덱스를 구성하는 칼럼과 얼마나 좌측부터 일치하는가에 따라 달라진다.
* 체크 조건        
  <br />

**예)** 인덱스 [col1, col2, col3, col4]   
WHERE 조건 [col3 > ?, col1 = ?, col2 = ?, col4 = ?]

위 예제에서 col3의 조건이 동등 조건이 아닌 크다 작다 비교이므로 뒤 칼럼인 col4의 조건은 범위 제한 조건으로 사용되지 못하고 체크 조건으로 사용된 것이다.    
WHERE 절에서 각 조건이 명시된 순서는 중요치 않고, 그 칼럼에 대한 조건이 있는지 없는지가 중요하다.

지금까지 예제의 WHERE 조건은 AND 연산자로 연결되는 경우를 가정한 것이며, 다음과 같이 OR 연산자가 있으면 처리 방법이 바뀐다.

```
SELECT * FROM employees 
WHERE first_name='Kebin' OR last_name='Poly';
```

위 쿼리에서 `first_name='Kebin'` 조건은 인덱스를 이용할 수 있지만 `last_name='Poly'`는 인덱스를 사용할 수 없다.   
만약 이 두 조건이 AND로 연결됐다면 first_name의 인덱스를 이용하겠지만 OR 연산자로 연결됐기 때문에 옵티마이저는 풀 테이블 스캔을 선택할 수 밖에 없다.    
(풀 테이블 스캔) + (인덱스 레인지 스캔)의 작업량보다 풀 테이블 스캔 한 번이 더 빠르다.


first_name과 last_name 칼럼에 각 인덱스가 있다면 index_merge 접근 방법으로 실행할 수 있다.    
이 방법은 풀 테이블 스캔보다는 빠르지만 여전히 제대로된 인덱스 하나를 레인지 스캔하는 것보다는 느리다.

OR로 연결되면 읽어서 비교해야 할 레코드가 더 늘어나기 때문에 WHERE 조건에 OR 연산자가 있다면 주의해야 한다.

<br />  

### GROUP BY 절의 인덱스 사용
GROUP BY 절의 각 칼럼은 비교 연산자를 가지지 않으므로 범위 제한 조건이나 체크 조건과 같이 구분해서 생각할 필요는 없다.    
GROUP BY 절에 명시된 칼럼의 순서가 인덱스를 구성하는 칼럼의 순서와 같으면 GROUP BY 절은 일단 인덱스를 이용할 수 있다.

* GROUP BY 절에 명시된 칼럼이 인덱스 칼럼의 순서와 위치가 같아야 한다.
* 인덱스를 구성하는 칼럼 중에서 뒷쪽에 있는 칼럼은 GROUP BY 절에 명시되지 않아도 인덱스를 사용할 수 있지만   
  인덱스의 앞쪽에 있는 칼럼이 GROUP BY 절에 명시되지 않으면 인덱스를 사용할 수 없다.
* WHERE 조건 절과 달리 GROUP BY 절에 명시된 칼럼이 하나라도 인덱스에 없으면 GROUP BY 절은 전혀 인덱스를 이용하지 못한다.    
  <br />


WHERE 조건절에 col1이나 col2가 동등 비교 조건으로 사용된다면 GROUP BY 절에 col1이나 col2가 빠져도 인덱스를 이용한 GROUP BY가 가능할 때도 있다.      
다음 예제는 인덱스의 앞쪽에 있는 칼럼을 WHERE 절에서 상수로 비교하기 때문에 GROUP BY 절에 해당 칼럼이 명시되지 않아도 인덱스를 이용한 그룹핑이 가능한 예제다.

```
... WHERE col1='상수' ... GROUP BY col2, col3;
... WHERE col1='상수' AND col2='상수' ... GROUP BY col3, col4;
... WHERE col1='상수' AND col2='상수' AND col3='상수' ... GROUP BY col4;
```

WHERE 절과 GROUP BY 절이 혼용된 쿼리가 인덱스를 이용해 WHERE 절과 GROUP BY 절이 모두 처리될 수 있는지는     
WHERE 조건절에서 동등 비교 조건으로 사용된 칼럼을 GROUP BY 절로 옮겨보면 된다.

```
-- // 원본 쿼리 
... WHERE col1='상수' ... GROUP BY col2, col3;

-- // WHERE 조건절의 col1 칼럼을 GROUP BY 절의 앞쪽으로 포함시켜 본 쿼리 
... WHERE col1='상수' ... GROUP BY col1, col2, col3; 
```

<br />   

### ORDER BY 절의 인덱스 사용
MySQL에서 GROUP BY와 ORDER BY는 처리 방법이 상당히 비슷하다.     
ORDER BY는 조건이 하나 더 있는데, 정렬되는 각 칼럼의 오름차순(ASC) 및 내림차순(DESC) 옵션이 인덱스와 같거나 또는 정반대의 경우에만 사용할 수 있다.       
인덱스의 모든 칼럼이 ORDER BY 절에 사용돼야 하는 것은 아니지만 인덱스에 정의된 칼럼의 왼쪽부터 일치해야 하는 것은 변함이 없다.

<br />   

### WHERE 조건과 ORDER BY (또는 GROUP BY) 절의 인덱스 사용
SQL 문장이 WHERE 절과 ORDER BY 절을 가지고 있다고 가정했을 때 WHERE 조건은 A 인덱스를 사용하고 ORDER BY는 B 인덱스를 사용하는 것은 불가능하다.    
WHERE 절과 ORDER BY 절이 같이 사용된 하나의 쿼리 문장은 다음 3가지 중 한 가지 방법으로만 인덱스를 사용한다.    
<br />

* WHERE 절과 ORDER BY 절이 동시에 같은 인덱스를 이용      
  WHERE 절의 칼럼과 ORDER BY 카럼이 모두 하나의 인덱스에 연속해서 포함돼 있는 경우      
  이 방법은 나머지 2가지 방식보다 훨씬 빠른 성능을 보이기 때문에 가능하다면 이 방식으로 처리할 수 있게 쿼리를 튜닝하거나 인덱스를 생성하는 것이 좋다.         
  <br />
* WHERE 절만 인덱스를 이용     
  ORDER BY 절은 인덱스를 이용한 정렬이 불가능하며, 인덱스를 통해 검색된 결과 레코드를 별도의 정렬 처리 과정(Filesort)을 거쳐서 정렬을 수행한다.     
  WHERE 절의 조건에 일치하는 레코드의 건수가 많지 않을 때 효율적인 방식이다.        
  <br />
* ORDER BY 절만 인덱스를 이용    
  ORDER BY 절은 인덱스를 이용해 처리하지만 WHERE 절은 인덱스를 이용하지 못한다.      
  이 방식은 ORDER BY 절의 순서대로 인덱스를 읽으면서 레코드를 한 건씩 WHERE 절의 조건에 일치하는지 비교해 일치하지 않을 때는 버리는 방식으로 처리한다.        
  주로 많은 레코드를 조회해서 정렬해야 할 때는 이런 형태로 튜닝하기도 한다.      
  <br />

WHERE 절에서 동등 비교 조건으로 비교된 칼럼과 ORDER BY 절에 명시된 칼럼이 순서대로 빠짐없이 인덱스 칼럼의 왼쪽부터 일치해야 한다.          
중간에 빠지는 칼럼이 있으면 WHERE 절이나 ORDER BY 절 모두 인덱스를 사용할 수 없다.    
이때는 주로 WHERE 절만 인덱스를 이용할 수 있다.     
<br />

<img width="994" alt="스크린샷 2021-05-10 오후 11 43 02" src="https://user-images.githubusercontent.com/33855307/117677501-7ac29180-b1e9-11eb-8b3a-21c3ebce5236.png">

위 그림은 WHERE 절과 ORDER BY 절이 결합된 두 가지 패턴의 쿼리를 표현한 것이다.    
ORDER BY 절에 해당 칼럼이 사용되고 있다면 WHERE 절에 동등 비교 이외의 연산자로 비교돼도 WHERE 조건과 ORDER BY 조건이 모두 인덱스를 이용할 수 있다.

<br />   

### GROUP BY 절과 ORDER BY 절의 인덱스 사용
GROUP BY와 ORDER BY 절이 사용된 쿼리에서 두 절이 모두 하나의 인덱스를 사용해서 처리되려면    
GROUP BY 절에 명시된 칼럼과 ORDER BY에 명시된 칼럼이 순서와 내용이 모두 같아야 한다.     
GROUP BY와 ORDER BY가 같이 사용된 쿼리에서는 둘 중 하나라도 인덱스를 이용할 수 없을 때는 둘다 인덱스를 사용하지 못한다.

<br />   

### WHERE 조건과 ORDER BY 절, GROUP BY 절의 인덱스 사용
WHERE 절과 GROUP BY 절, ORDER BY 절이 모두 포함된 쿼리가 인덱스를 사용하는지 판단하는 방법은 다음과 같다.

1. WHERE 절이 인덱스를 사용할 수 있는가?
2. GROUP BY 절이 인덱스를 사용할 수 있는가?
3. GROUP BY 절과 ORDER BY 절이 동시에 인덱스를 사용할 수 있는가?       
   <br />

<img width="835" alt="스크린샷 2021-05-11 오전 12 00 11" src="https://user-images.githubusercontent.com/33855307/117680138-de4dbe80-b1eb-11eb-9e6a-2d86a149f2de.png">


<br />      


## 3. WHERE 절의 비교 조건 사용 시 주의사항
### 날짜 비교
DATE나 DATETIME 타입의 값과 문자열을 비교할 때는 문자열 값을 자동으로 DATETIME 타입 값으로 변환해서 비교를 수행한다.    
다음 첫 번째 쿼리와 두 번째 쿼리 모두 인덱스를 효율적으로 이용하기 때문에 성능과 관련된 문제는 고민하지 않아도 된다.

```
SELECT COUNT(*) FROM employees 
WHERE hire_date > STR_TO_DATE('2011-07-23', '%Y-%m-%d');

SELECT COUNT(*) FROM employees 
WHERE hire_date > '2011-07-23';
```

그러나 여기서 hire_date 타입을 강제적으로 문자열로 변경시키면 인덱스를 효율적으로 이용하지 못한다.              
그리고 칼럼의 값을 더하거나 빼는 함수로 변형한 후 비교해도 마찬가지로 인덱스를 이용할 수 없다.              
ex. `WHERE DATE_ADD(hire_date, INTERVAL 1 YEAR) > '2011-07-23'`  > 칼럼을 함수로 변형, 인덱스 사용 못함       
`WHERE hire_date > DATE_SUB('2011-07-23', INTERVAL 1 YEAR)` > 칼럼이 아니라 상수를 변형, 인덱스 사용 가능


<br />     

## 3. DISTINCT
특정 칼럼의 유니크한 값을 조회하려면 SELECT 쿼리에 DISTINCT를 사용한다.    
DISTINCT는 MIN(), MAX(), COUNT()와 같은 집합 함수와 함께 사용하는 경우와 집합 함수 없이 사용하는 경우 두 가지로 구분된다.

집합 함수와 같이 DISTINCT가 사용되는 쿼리의 실행 계획에서 DISTINCT 처리가 인덱스를 사용하지 못할 때는 항상 임시 테이블이 있어야 한다.    
하지만 실행 계획의 Extra 칼럼에는 "Using temporary" 메시지는 출려되지 않는다.    
<br />

### SELECT DISTINCT ...
단순히 유니크한 레코드를 가져가려면 SELECT DISTINCT 형태의 쿼리 문장을 사용한다.    
이 경우 GROUP BY와 거의 같은 방식으로 처리된다. 그러나 인덱스를 이용하지 못하는 경우 SELECT DISTINCT의 경우 정렬이 보장되지 않는다.

DISTINCT를 사용할 때 자주 실수하는 부분은 DISTINCT는 SELECT되는 레코드를 유니크하게 SELECT하는 것이지 칼럼을 유니크하게 조회하는 것이 아니다.    
즉, 다음 쿼리에서 SELECT되는 결과는 first_name만 유니크한 것을 가져오는 것이 아니라 (first_name + last_name) 전체가 유니크한 레코드를 가져오는 것이다.

```
SELECT DISTINCT first_name, last_name FROM employees;
```

<br />   

### 집합 함수와 함께 사용된 DISTINCT
집합 함수 내에서 DISTINCT 키워드가 사용될 때는 일반적으로 SELECT DISTINCT와 다른 형태로 해석된다.    
집합 함수가 없는 SELECT 쿼리에서 DISTINCT는 조회되는 모든 칼럼의 조합 가운데 유일한 값만 가져온다.    
하지만 집합 함수 내에서 사용된 DISTINCT는 그 집합 함수의 인자로 전달된 칼럼 값들 중에서 중복을 제거하고 남은 값만을 가져온다.

```
EXPLAIN
SELECT COUNT(DISTINCT s.salary)
FROM employees e, salaries s
WHERE e.emp_no=s.emp_no
AND e.emp_no BETWEEN 100001 AND 100100;
```

이 쿼리 실행 계획의 Extra 칼럼에는 출력되지 않지만 내부적으로는 `COUNT(DISTINCT s.salary)`의 처리에는 인덱스 대신 임시 테이블을 사용한다.       
하지만 이 쿼리의 실행 계획에는 임시 테이블을 사용한다는 메시지가 표시되지 않는다.

위의 쿼리에서는 employees 테이블과 salaries 테이블을 조인한 결과에서 **salary 칼럼의 값만 저장하기 위한 임시 테이블**을 만들어서 사용한다.     
이때 임시 테이블의 salary 칼럼에는 유니크 인덱스가 생성되기 때문에 레코드 건수가 많아진다면 상당히 느려질 수 있는 형태의 쿼리다.

위 쿼리에 `COUNT(DISTINCT ...)`을 하나 더 추가하면 또 다른 임시 테이블이 필요하기 때문에 전체적으로 2개의 임시 테이블을 사용하게 된다.

<br />  

## 5. LIMIT n
LIMIT는 MySQL에만 존재하는 키워드이며 다음과 같은 순서대로 실행된다.

```
SELECT * FORM employees 
WHERE emp_no BETWEEN 10001 AND 10010
ORDER BY first_name
LIMIT 0, 5;
```

1. employees 테이블에서 WHERE 절의 검색 조건에 일치하는 레코드를 전부 읽어 온다.
2. 1번에서 읽어온 레코드를 first_name 칼럼 값에 따라 정렬한다.
3. 정렬된 결과에서 상위 5건만 사용자에게 반환한다.     
   <br />

MySQL의 LIMIT는 WHERE 조건이 아니기 때문에 항상 쿼리의 가장 마지막에 실행된다.     
LIMIT의 중요한 특성은 LIMIT에서 필요한 레코드 건수만 준비되면 바로 쿼리를 종료시킨다는 것이다.       
즉 모든 레코드의 정렬이 완료되지 않았다 하더라도 상위 5건까지만 정렬이 되면 작업을 멈춘다는 것이다.      
<br />

다음은 GROUP BY 절이나 DISTINCT 등과 같이 LIMIT이 사용되는 경우다.

```
SELECT * FORM employees LIMIT 0, 10;
SELECT * FROM employees GROUP BY first_name LIMIT 0, 10;
SELECT DISTINCT first_name FROM employees LIMIT 0, 10;

SELECT * FROM employees 
WHERE emp_no BETWEEN 10001 AND 11000
ORDER BY first_name LIMIT 0, 10;
```

* 첫 번째 쿼리에서 LIMIT이 없을 때 employees 테이블을 처음부터 끝까지 읽는 풀 테이블 스캔을 실행할 것이다.    
  하지만 LIMIT 옵션이 있기 때문에 풀 테이블 스캔을 실행하면서 MySQL이 스토리지 엔진으로부터 10개의 레코드를 읽어들이는 순간 읽기 작업을 멈춘다.    
  <br />

* 두 번째 쿼리는 GROUP BY가 있기 때문에 GROUP BY 처리가 완료되고 나서야 LIMIT 처리를 수행할 수 있다.     
  인덱스를 사용하지 못하는 GROUP BY는 그룹핑과 정렬의 특성을 모두 가지고 있기 때문에 일단 GROUP BY 작업이 모두 완료돼야만 LIMIT을 수행할 수 있다.      
  결국 실질적인 서버의 적업 내용을 크게 줄여 주지는 못한다.             
  <br />

* 세 번째 쿼리에서 사용한 DISTINCT는 정렬에 대한 요건이 없이 유니크한 그룹만 만들어 내면 된다.           
  풀 테이블 스캔 방식을 이용해 employees 테이블 레코드를 읽어들임과 동시에 DISTINCT를 위한 중복 제거 작업을 (임시 테이블 사용) 진행한다.    
  이 작업을 반복적으로 처리하다가 유니크한 레코드가 LIMIT 건수만큼 채워지면 그 순간 쿼리를 멈춘다.           
  <br />

* 네 번째 쿼리는 employees 테이블로부터 WHERE 조건절에 일치하는 레코드를 읽은 후 first_name 칼럼의 값으로 정렬을 수행한다.   
  정렬을 수행하면서 필요한 10건이 완성되는 순간 작업을 멈추는데 필요한 만큼 정렬되야 쿼리가 완료되기 때문에 크게 작업량을 줄여주지는 못한다.

<br />       

## 6. JOIN
### JOIN의 순서와 인덱스
다음은 인덱스 레인지 스캔으로 레코드를 읽는 작업 순서다.
1. 인덱스에서 조건을 만족하는 값이 저장된 위치를 찾는다. **인덱스 탐색(Index seek)**
2. 1번에서 탐색된 위치부터 필요한 만큼 인덱스를 읽는다. **인덱스 스캔(Index scan)**
3. 2번에서 읽어들인 인덱스 키와 레코드 주소를 이용해 레코드가 저장된 페이지를 가져오고 최종 레코드를 읽어온다.    
   <br />

일반적으로 인덱스 풀 스캔이나 테이블 풀 스캔 작업은 인덱스 탐색 과정이 거의 없지만 실제 인덱스나 테이블의 모든 레코드를 읽기 때문에 부하가 높다.          
인덱스 레인지 스캔에서는 가져오는 레코드의 건수가 소량이기 때문에 인덱스 스캔 과정은 부하가 작지만 **특정 인덱스 키를 찾는 인덱스 탐색 과정이 상대적으로 부하가 높은 편이다.**

조인 작업에서 드라이빙 테이블을 읽을 때는 인덱스 탐색 작업을 단 한 번만 수행하고, 이후 스캔만 실행하면 된다.    
하지만 드리븐 테이블에서는 인덱스 탐색 작업과 스캔 작업을 드라이빙 테이블에서 읽은 레코드 건수만큼 반복한다.    
드라이빙 테이블과 드리븐 테이블이 1:1로 조인되더라도 드리븐 테이블을 읽는 것이 훨씬 큰 부하를 차지하는 것이다.    
그래서 옵티마이저는 항상 드라이빙 테이블이 아니라 드리븐 테이블을 최적으로 읽을 수 있게 실행 계획을 수립한다.    
<br />

```
SELECT * 
FROM employees e, dept_emp de
WHERE e.emp_no=de.emp_no;
```

위 두 테이블의 조인 쿼리에서 employees 테이블의 emp_no 칼럼과 dept_emp 테이블의 emp_no 칼럼에    
각각 인덱스가 있을 때와 없을 때 조인 순서가 어떻게 달라지는지 알아보자.

* 두 칼럼 모두 각각 인덱스가 있는 경우      
  어느 테이블을 드라이빙으로 선택하든 인덱스를 이용해 드리븐 테이블의 검색 작업을 빠르게 처리할 수 있다.      
  각 테이블의 통계 정보에 있는 레코드 건수에 따라 옵티마이저가 최적의 선택을 한다.        
  <br />

* employees.emp_no에만 인덱스가 있는 경우      
  이때 dept_emp 테이블이 드리븐 테이블로 선택된다면 employees 테이블의 레코드 건수만큼 dept_emp 테이블을 풀 스캔한다.       
  그래서 옵티마이저는 항상 dept_emp 테이블을 드라이빙 테이블로 선택하고, employees 테이블을 드리븐 테이블로 선택하게 된다.         
  "e.emp_no=100001"과 같이 employees 테이블을 아주 효율적으로 접근할 수 있는 조건이 있더라도 employees 테이블을 드라이빙 테이블로 선택하지 않을 가능성이 높다.     
  <br />

* dept_emp.emp_no에만 인덱스가 있는 경우    
  위 경우와 반대로 employees 테이블의 반복된 풀 스캔을 막기 위해 employees 테이블을 드라이빙 테이블로 선택하고
  dept_emp 테이블을 드리븐 테이블로 조인을 수행하도록 실행 계획을 수립한다.      
  <br />

* 두 칼럼 모두 인덱스가 없는 경우    
  어느 테이블을 드라이빙으로 선택하더라도 드리븐 테이블의 풀 스캔은 발생하기 때문에 옵티마이저가 적절히 드라이빙 테이블을 선택한다.    
  단 레코드 건수가 적은 테이블을 드리븐 테이블로 선택하는 것이 훨씬 효율적이다.     
  드리븐 테이블을 읽을 때 조인 버퍼가 사용되기 때문에 실행 계획의 Extra 칼럼에 "Using join buffer"가 표시된다.

<br />   

### JOIN 칼럼의 데이터 타입
대표적으로 다음 비교 패턴은 문제가 될 가능성이 높다.

* CHAR 타입과 INT 타입의 비교와 같이 데이터 타입의 종류가 완전히 다른 경우
* 같은 CAHR 타입이더라도 문자집합이나 콜레이션이 다른 경우
* 같은 INT 타입이더라도 부호가 있는지 여부가 다른 경우

<br />       

### OUTER JOIN의 주의사항
OUTER JOIN에서 OUTER로 조인되는 테이블의 칼럼에 대한 조건은 모두 ON 절에 명시해야 한다.

다음과 같이 조건을 ON 절에 명시하지 않고 OUTER 테이블의 칼럼이 WHERE 절에 명시하면 옵티마이저가 INNER JOIN과 같은 방법으로 처리한다.

```
SELECT * FROM employees e
  LEFT JOIN dept_manager mgr ON mgr.emp_no=e.emp_no
WHERE mgr.dept_no='d001';
```

ON 절에 조인 조건은 명시했지만 OUTER로 조인되는 테이블인 dept_manager의 `dept_no='d001'` 조건을 WHERE 절에 명시한 것은 잘못된 조인 방법이다.    
위 LEFT JOIN이 사용된 쿼리는 WHERE 절의 조건 때문에 옵티마이저가 LEFT JOIN을 다음 쿼리와 같이 INNER JOIN으로 변환해버린다.

```
SELECT * FROM employees e
  INNER JOIN dept_manager mgr ON mgr.emp_no=e.emp_no
WHERE mgr.dept_no='d001';
```

OUTER 조인이 되게 만들려면 다음 쿼리와 같이 WHERE 절의 `mgr.dept_no='d001'` 조건을 LEFT JOIN의 ON 절로 옮겨야 한다.


```
SELECT * FROM employees e
  LEFT JOIN dept_manager mgr ON mgr.emp_no=e.emp_no AND mgr.dept_no='d001';
```

<br />      

### OUTER JOIN과 COUNT(*)
페이징 처리를 하기 위해 조건에 일치하는 레코드의 건수를 가져오는 쿼리에서 OUTER JOIN과 COUNT(*)를 자주 함께 사용하곤 한다.    
그런데 주로 테이블의 레코드를 가져오는 쿼리에서 SELECT 절의 내용만 COUNT(*)로 바꿔서     
일치하는 레코드 건수를 조회하는 쿼리를 만들기 때문에 불필요하게 OUTER JOIN으로 연결되는 테이블이 자주 있다.   
만약 건수를 조회하는 쿼리에서 OUTER JOIN으로 조인된 테이블이 다음의 2가지 조건을 만족한다면 해당 테이블은 불필요하게 조인에 포함된 것이다.

* 드라이빙 테이블과 드리븐(OUTER 조인되는) 테이블의 관계가 1:1 또는 M:1 인 경우
* 드리븐(OUTER 조인되는) 테이블에 조인 조건 이외의 별도 조건이 없는 경우               
  <br />

위 두 가지 조건 가운데 첫 번째는 OUTER JOIN으로 연결되는 테이블에 의해 레코드 건수가 더 늘어나지 않아야 한다는 것을 의미한다.    
두 번째 조건은 OUTER JOIN으로 연결되는 테이블에 의해 레코드 건수가 더 줄어들지 않아야 한다는 것을 의미한다.   
<br />

```
SELECT COUNT(*)
FROM dept_emp de LEFT JOIN employees e ON e.emp_no=de.emp_no
WHERE de.dept_no='d001';
```

위 쿼리의 조인엣 드라이빙 테이블(dept_emp)과 드리븐 테이블(employees)은 M:1 관계로 데이터가 저장되며, 드리븐 테이블에 대한 조건은 ON 절의 조인 조건밖에 없다.     
결국 이 쿼리는 다음 쿼리와 같이 조인되지 않고 dept_emp 테이블의 레코드 건수를 새는 것과 같은 결과를 가져온다.   
하지만 성능 측면에서는 조인을 하지 않는 쿼리가 훨씬 빠르다.

```
SELECT COUNT(*)
FORM dept_emp de
WHERE de.dept_no='d001';
```

<br />    

### OUTER JOIN을 이용한 ANTI JOIN
두 개의 테이블에서 한쪽 테이블에는 있지만 다른 한쪽 테이블에는 없는 레코드를 검색할 때 ANTI JOIN을 이용한다.

```
INSERT INTO test1 VALUES (1), (2), (3), (4);
INSERT INTO test2 VALUES (1), (2);
```

이 때 일반적으로 사용하는 방법이 NOT IN이나 NOT EXISTS 사용이다.

```
SELECT t1.id FORM test1 t1
WHERE t1.id NOT IN (SELECT t2.id FROM test2 t2);

SELECT t1.id FROM test1 t1 
WHERE NOT EXISTS
  (SELECT 1 FROM test2 t2 WHERE t2.id=t1.id);
```

MySQL 5.5 포함 서브 쿼리에 대한 최적화가 상당히 부족한 상황이다.          
처리해야 할 레코드 건수가 많다면 OUTER JOIN을 이용한 ANTI JOIN을 사용하는 방법이 좋은 해결책일 것이다.

```
SELECT t1.id 
FROM test1 t1
  LEFT JOIN test2 t2 ON t1.id=t2.id
WHERE t2.id IS NULL;
```

<br />  

### FULL OUTER JOIN 구현
MySQL에서는 FULL OUTER JOIN 기능을 제공하지 않는다. 하지만 두 개의 쿼리 결과를 UNION으로 결합하면 FULL OUTER JOIN의 효과를 얻을 수 있다.    
일반적으로 UNION은 두 집합의 결과에서 중복 제거가 필요하기 때문에 UNION ALL을 사용하면 더 빠르게 처리될 것이다.   
먼저 각 쿼리에서 중복을 제거하고 UNION ALL을 이용해 구현할 수도 있다.


UNION이나 UNION ALL은 모두 내부적인 임시 테이블을 사용하므로 결과가 버퍼링돼야 하고 그로인해 쿼리가 느리게 처리된다.    
버퍼링으로 인한 성능이 걱정된다면 뮤텍스(Mutex) 테이블을 사용하면 된다.

뮤텍스 테이블이란 copy_t라고도 많이 알려져 있는데 단순히 레코드 n개를 복제하는 역할을 하는 테이블이다.   
이 레코드 복제용 테이블은 다른 용도로도 많이 사용되기도 한다.

<br />  

### 조인 순서로 인한 쿼리 실패
MySQL에서는 ANSI 표준인 INNER JOIN이나 LEFT JOIN 구문을 기본 문법으로 제공한다.    
또한 INNER JOIN은 다음과 같이 WHERE 절에 조인 조건을 명시하는 형태도 지원한다.

```
SELECT * 
FROM dept_manager dm, dept_emp de, departments d
WHERE dm.dept_no=de.dept_no AND de.dept_no=d.dept_no;
```

ANSI 표준 조인 문법과 조인 조건을 WHERE 절에 명시하는 문법을 혼용하거나    
ANSI 표준 표기법을 잘못 사용하면 ON 절의 조인 조건에 사용된 칼럼을 인식할 수 없다는 에러가 발생한다.

```
SELECT *  
FROM dept_manager dm
INNER JOIN dept_emp de ON de.dept_no=d.dept_no
INNER JOIN departments d ON d.dept_no=de.dept_No;
-> ERROR 1054 (42S22): Unknown column 'd.depet_no' in 'on clause'

SELECT * FROM dept_manager dm, 
  (dept_emp de INNER JOIN departments d ON d.dept_no=dm.dept_no)
WHERE dm.dept_no=de.dept_no;
-> ERROR 1054 (42S22): Unknown column 'dm.dept_no' in 'on clause'
```

위 두 예제는 모두 칼럼을 인식할 수 없다는 에러 메시지와 함께 쿼리가 실패했다.

ANSI 표준의 JOIN 구문에서는 반드시 JOIN 키워드의 좌우측에 명시된 테이블의 칼럼만 ON 절에 사용될 수 있기 때문이다.      
위와 같은 에러는 간순히 JOIN 구문의 순서나 ON 절의 조건을 조정해서 쉽게 해결할 수 있다.

<br />  

### JOIN과 FOREIGN KEY
FOREIGN KEY는 조인과 아무런 연관이 없다. FOREIGN KEY를 생성하는 주 목적은 데이터의 무결성을 보장하기 위해서다.     
FOREIGN KEY와 연관된 무결성을 참조 무결성이라고 표현한다.

하지만 SQL로 테이블 간의 조인을 수행하는 것은 전혀 무관한 칼럼을 조인 조건으로 사용해도 문법적으로는 문제가 되지 않는다.      
데이터 모델링을 할 때는 각 테이블 간의 관계는 필수적으로 그려 넣어야 한다.     
하지만 그 데이터 모델을 데이터베이스에 생성할 때는 그 테이블 간의 관계는 FOREIGN KEY로 생성하지 않을 때가 더 많다.      
하지만 테이블 간의 조인을 사용하기 위해 FOREIGN KEY가 필요한 것은 아니다.

<br />  

### 지연된 조인 (Delayed Join)
조인을 사용하는 쿼리에서 GROUP BY 또는 ORDER BY를 사용할 때 각 처리 방법에서 인덱스를 사용한다면 이미 최적으로 처리되고 있을 가능성이 높다.    
하지만 그렇지 않다면 MySQL 서버는 우선 모든 조인을 수행하고 난 다음 GROUP BY나 ORDER BY를 처리할 것이다.   
조인을 대체적으로 실행되면 될수록 결과 레코드 건수가 늘어나기 때문에 조인의 결과를 GROUP BY, ORDER BY하면 조인을 실행하기 전보다 더 많은 레코드를 처리해야 한다.      
지연된 조인이란 조인이 실행되기 전에 GROUP BY나 ORDER BY를 처리하는 방식을 의미한다.      
지연된 조인은 주로 LIMIT과 같이 사용된 쿼리에서 더 큰 효과를 얻을 수 있다.

인덱스를 사용하지 못하는 GROUP BY와 ORDER BY 쿼리를 지연된 조인으로 처리해보자.

```
SELECT e.* 
FROM salaries s, employees e
WHERE e.emp_no=s.emp_no
      AND s.emp_no BETWEEN 10001 AND 13000
GROUP BY s.emp_no
ORDER BY SUM(s.salary) DESC
LIMIT 10;
```

위 쿼리의 실행 게획은 employees 테이블을 드라이빙 테이블로 선택해서    
`s.emp_no BETWEEN 10001 AND 13000` 조건을 만족하는 레코드 2999건을 읽고, salaries 테이블을 조인한다.   
조인 결과 11,996건의 레코드를 임시 테이블에 저장하고 GROUP BY 처리를 통해 3000건으로 줄였다.      
그리고 ORDER BY를 처리해서 상위 10건만 최종적으로 가져온다.

EXPLAIN을 통한 Extra 칼럼 결과는 `Using where; Using temporary; Using filesort` 이다.   
<br />

지연된 조인으로 변경한 쿼리를 한번 살펴보자.      
다음 쿼리에서는 salaries 테이블에서 가능한 모든 처리 (WHERE, GROUP BY, ORDER BY, LIMIT)을 한 다음 그 결과를 임시 테이블에 저장했다가     
임시 테이블의 결과를 employees 테이블과 조인하도록 고친 것이다.      
즉 모든 처리를 salaries 테이블에서 수행하고 최종 10건만 employees 테이블과 조인했다.

```
SELECT * 
FROM 
  ( SELECT s.emp_no
    FROM salaries s
    WHERE s.emp_no BETWEEN 10001 AND 13000
    GROUP BY s.emp_no
    ORDER BY SUM(s.salary) DESC
    LIMIT 10 ) x, 
  employees e
WHERE e.emp_no=x.epm_no;
```


지연된 조인으로 변경한 쿼리의 실행 계획은 예상했던 대로 FROM 절에 서브 쿼리가 사용됐기 때문에 이 서브 쿼리의 결과는 파생 테이블로 처리됐다.    
salaries 테이블에서 레코드를 읽어 임시 테이블에 저장하고 GROUP BY 처리를 통해 레코드를 줄이고 ORDER BY를 처리해 상위 10건만 임시 테이블에 저장한다.    
최종적으로 임시 테이블에서 10건을 읽어 employees 테이블과 조인을 10번만 수행해서 결과를 반환한다.

지연된 조인으로 개선된 쿼리는 임시 테이블을 한 번 더 사용하기 때문에 느리다고 생각할 수도 있지만   
임시 테이블에 저장할 레코드가 단 10건밖에 되지 않으므로 메모리를 이용해 빠르게 처리된다.    
하지만 조인의 횟수로 비교해보면 지연된 조인으로 변경된 쿼리의 조인 횟수가 훨씬 적다는 사실을 알 수 있다.    
<br />

다음은 페이징 쿼리에서 지연된 조인을 어떻게 적용할 수 있는지의 예제다.

```
SELECT * 
FROM dept_emp de, employees e
WHERE de.dept_no='d001' AND e.emp_no=de.emp_no
LIMIT 10;
```


이 쿼리는 'd001' 부서에서 일했던 모든 사원의 정보를 조회하는 쿼리이다.   
위 쿼리에서 LIMIT 10은 아무런 문제가 없다. 하지만 두 번째 이후의 페이지로 넘어가면 아래와 같이 쿼리가 바뀌어서 실행될 것이다.

```
SELECT * 
FROM dept_emp de, employees e
WHERE de.dept_no='d001' AND e.emp_no=de.emp_no
LIMIT 100, 10;
```

이 쿼리는 dept_emp 테이블이 드라이빙 테이블이 되고, employees 테이블이 드리븐 테이블이 되어서 조인이 실행될 것이다.     
우선 dept_emp 테이블에서 de.dept_no='d001'인 레코드를 한 건씩 읽으면서 employees 테이블과 조인하면서    
`LIMIT 100, 10` 조건이 만족될 때까지 조인을 수행하게 된다.      
dept_emp 테이블을 110건 읽어서 employees 테이블과 110번 조인하는 것이다.     
그러나 사용자가 필요로 하는 데이터는 마지막 10건이므로 앞에서 읽은 100건은 불필요하게 읽은 데이터이다.      
만약 페이징 처리에서 몇 백 페이지까지 넘어간다면 다음 페이지로 넘어갈수록 느려지게 되는 것이다.       
<br />

위 쿼리를 지연된 조인으로 처리하면 꼭 필요한 10건에 대해서만 employees 테이블과 조인하게 만들 수 있다.   
dept_emp 테이블에서 먼저 꼭 필요한 10개의 레코드만 조회하는 서브 쿼리 (파생 테이블)을 만든다.    
그 결과와 employees 테이블을 조인한다.

```
SELECT * 
FROM ( SELECT * FORM dept_emp WHERE dept_no='d001' LIMIT 100, 10) de, 
       employees e
WHERE e.emp_n=de.emp_no;
```

지연 로딩은 경우에 따라 상당한 성능 향상을 가져올 수 있지만 모든 쿼리를 지연된 조인 형태로 개선할 수 있는 것은 아니다.    
OUTER JOIN과 INNER JOIN에 대해 다음과 같은 조건이 갖춰져야만 지연된 쿼리로 변경해서 사용할 수 있다.

* LEFT (OUTER) JOIN인 경우 드라이빙 테이블과 드리븐 테이블은 1:1 또는 M:1 관계여야 한다.
* INNER JOIN인 경우 드라이빙 테이블과 드리븐 테이블은 1:1 또는 M:1의 관계임과 동시에 드라이빙 테이블에 있는 레코드는        
  드리븐 테이블 모두 존재해야 한다. 두 번째와 세 번째 조건은 드라이빙 테이블을 서브 쿼리로 만들고   
  이 서브 쿼리에 LIMIT를 추가해도 최종 결과의 건수가 변하지 않는다는 보증을 해주는 조건이기 때문에 반드시 정확히 확인한 후 적용해야 한다.

<br />   

### 조인 버퍼 사용으로 인한 정렬 흐트러짐
MySQL에서는 네스티드-루프 방식의 조인만 지원한다.    
네스티드-루프 조인은 알고리즘의 특성상 드라이빙 테이블에서 읽은 레코드의 순서가 다른 테이블이 모두 조인돼도 그대로 유지된다.    
하지만 조인이 실행되기 위해 조인 버퍼가 사용되면 이야기가 달라진다.   
ex. A, B라는 두 테이블이 순서대로 조인될 때, B 테이블을 읽을 때 조인 버퍼가 사용되면 실제 내부적으로 조인은 B 테이블에서 A 테이블로 실행된다.   
그래서 결과가 B 테이블을 기준으로 한 것처럼 일 수도 있다. 하지만 사실 조인 버퍼를 사용하면 결과는 B 테이블을 기준으로 정렬되지 않고 완전히 흐트러진다.

```
SELECT e.emp_no, e.first_name, e.last_name, de.from_date
FROM dept_emp de, employees e
WHERE de.from_date>'2001-10-01' AND e.emp_no<10005;
```


위 쿼리의 실행 계획은 employees 테이블이 조인에서 드라이빙 테이블로 사용했고 dept_emp 테이블로 조인할 때는 조인 버퍼를 사용했다.

|id|select_type|Table|type|key|key_len|ref|rows|Extra|
|---|---|---|---|---|---|---|---|---|
|1|SIMPLE|e|range|PRIMARY|4| |4|Using where|
|1|SIMPLE|de|range|ix_fromdate|3| |2762|Using where; <br /> Using join buffer|     

네스티드 루프 알고리즘만으로 조인을 처리하는 MySQL 서버에서는 조인에서 드라이빙 테이블을 읽은 순서대로 결과가 조회되는 것이 일반적이다.    
즉 employees 테이블의 프라이머리 키인 emp_no 값의 순서대로 조회돼야 한다.   
하지만 이 쿼리의 결과는 emp_no 칼럼으로 정렬돼 있지 않다. 오히려 드리븐 테이블인 dept_emp 테이블을 읽을 때 사용한 ix_fromdate 인덱스 순서대로 정렬된 것처럼 보인다.    
조인 버퍼를 사용한 조인은 드라이빙 테이블과 드리븐 테이블을 읽은 순서와는 거의 무관하게 결과를 출력한다.

<br />   

## 7. GROUP BY
GROUP BY는 특정 칼럼의 값으로 레코드를 그룹핑하고 각 그룹별로 집계된 결과를 하나의 레코드로 조회할 때 사용한다.       
<br />

### GROUP BY 사용 시 주의사항
쿼리에 GROUP BY가 사용되면 그룹 키(GROUP BY 절에 명시된 칼럼)가 아닌 칼럼은 일반적으로 집합 함수를 감싸서 사용해야 한다.      
MySQL에서는 그룹 키가 아닌 칼럼이더라도 쿼리에서 집합 함수 없이 그냥 사용할 수 있다.

```
SELECT first_name FROM employees GROUP BY gender;

SELECT first_name, last_name, COUNT(*)
FROM employees 
GROUP BY first_name ORDER BY last_name;
```

두 쿼리 모두 GROUP BY를 하고 있지만 SELECT 절이나 ORDER BY 절에는 GROUP BY 절에 명시되지 않은     
first_name이나 last_name이라는 칼럼이 집합 함수로 감싸지지 않고 그대로 사용됐다.   
문제는 이 쿼리가 실행되면 first_name이라는 칼럼이 어떤 값을 가져올지 예측할 수 없다는 것이다.

첫 번째 쿼리는 gender로 GROUP BY를 수행했기 때문에 결과 레코드는 2건(남자, 여자)이 반환될 것이다.   
하지만 SELECT하는 칼럼은 gender 칼럼이 아니라 first_name을 조회하고 있다.   
반환되는 first_name은 각각 남녀 성별로 한 건씩만 가져오겠지만 first_name이 제일 큰 값인지 작은지 아니면 중간의 값을 가져온 것인지 알 수 없다.

두 번째 쿼리 또한 first_name 칼럼으로 GROUP BY를 수행해서 결과의 last_name으로 정렬을 수행하고 있다.    
이 결과 또한 first_name이 동일한 여러 사원들 중 어느 사원의 last_name을 가지고 정렬을 수행했는지 보장할 수 없는 쿼리다.

이러한 GROUP BY 사용은 가독성을 떨어뜨린다. 가능하다면 GROUP BY 절에 명시되지 않은 칼럼은 반드시 집합 함수로 감싸서 사용하길 권장한다.    
GROUP BY 절에 명시되지 않은 칼럼은 집합 함수로 감싸서만 사용할 수 있게 하는 것을 "FULL GROUP-BY"라고 한다.

<br />   

## 8. ORDER BY
ORDER BY는 검색된 레코드를 어떤 순서로 정렬할지 결정한다.

ORDER BY절이 사용되지 않는 SELECT 쿼리의 결과는 다음과 같이 정렬된다.
* 인덱스를 사용한 SELECT의 경우 인덱스의 정렬된 순서대로 레코드를 가져온다.
* 인덱스를 사용하지 못하고 풀 테이블 스캔을 실행하는 SELECT를 가정해보자.
    - MyISAM : 테이블에 저장된 순서대로 가져온다. (삭제된 후 그 빈 공간을 INSERT로 다시 채우기 때문에 무조건 INSERT된 순서를 의미하는 것은 아니다.)
    - InnoDB : 프라이머리 키로 클러스터링돼 있기 때문에 풀 테이블 스캔의 경우 기본적으로 프라이머리 키 순서대로 레코드를 가져온다.
* SELECT 쿼리가 임시 테이블을 거쳐서 처리되면 조회되는 순서를 예측하기는 어렵다.      
  <br />

ORDER BY에서 인덱스를 사용하지 못할 때는 추가적인 정렬 작업을 수행하고 쿼리 실행 계획에 있는 Extra 칼럼에 "Using filesort"라는 코멘트가 표시된다.    
"Filesort"라는 단어에 포함된 "File"은 디스크의 파일을 이용해 정렬을 수행한다는 의미가 아니라 쿼리를 수행하는 도중에 MySQL 서버가 퀵 소트 정렬 알고리즘을 수행했다는 의미이다.    
정렬 대상이 많은 경우 여러 부분으로 나눠서 처리하는데 정렬된 결과를 임시로 디스크나 메모리에 저장해둔다. 하지만 실제로 메모리만 이용했는지 디스크 파일을 이용했는지는 알 수 없다.


<br />   

## 9. 서브 쿼리
쿼리를 작성할 때 서브 쿼리를 사용하면 단위 처리별로 쿼리를 독립시킬 수 있다.    
조인처럼 여러 테이블을 섞어두는 형태가 아니라서 가독성이 높아지며, 복잡한 쿼리도 손쉽게 작성할 수 있다.    
하지만 MySQL 서버는 서브 쿼리를 최적으로 실행하지 못할 때가 많다.       
대표적으로 FROM 절에 사용되는 서브 쿼리나 WHERE 절의 IN (subquery) 구문은 MySQL 5.5에서도 그다지 효율적이지 않다.

서브 쿼리는 외부 쿼리에서 정의된 칼럼을 참조하는지 여부에 따라 상관 서브 쿼리와 독립 서브 쿼리로 나눌 수 있다.      
<br />

### 상관 서브 쿼리 (Correlated subquery)
서브 쿼리 **외부에서 정의된 테이블의 칼럼을 참조**해서 검색을 수행할 때 상관 서브 쿼리라고 한다.      
독립적으로 실행되지 못하고 항상 외부 쿼리가 실행된 후 그 결과값이 전달돼야만 하기 때문에 상관 서브 쿼리를 포함하는 비교 조건은 범위 제한 조건이 아니라 체크 조건으로 사용된다.

```
SELECT * FROM employees e
WHERE EXISTS 
     ( SELECT 1 FORM dept_emp de
       WHERE de.emp_no=e.emp_no
             AND de.from_date BETWEEN '2000-01-01' AND '2011-12-30' );
```

<br />


### 독립 서브 쿼리 (Self-Contained subquery)
다음 예제 쿼리와 같이 외부 쿼리의 칼럼을 사용하지 않고 서브 쿼리에서 정의된 칼럼만 참조할 때 독립 서브 쿼리라고 한다.    
외부의 쿼리와 상관없이 항상 같은 결과를 반환하므로 외부 쿼리봗 먼저 실행되어 외부 쿼리의 검색을 위한 상수로 사용되는 것이 일반적이다.    
독립 서브 쿼리가 포함된 비교 조건은 범위 제한 조건으로 사용될 수 있다.    
하지만 MySQL에서는 독립 서브 쿼리라 하더라도 효율적으로 처리되지 못할 때가 많다.

```
SELECT de.dept_no, de.emp_no
FROM dept_emp de
WHERE de.demp_no=( SELECT e.emp_no
                   FROM employees e 
                   WHERE e.first_name='Georgi' AND e.last_name='Facello' LIMIT 1 );
```


<br />    

### 서브 쿼리의 제약 사항
* 서브 쿼리는 대부분 쿼리 문장에서 사용할 수 있지만 LIMIT 절과 LOAD DATA INFILE의 파일명에는 사용할 수 없다.
* 서브 쿼리의 IN 연산자와 함께 사용할 때는 효율적으로 처리되지 못한다.
* IN 연산자 안에서 사용하는 서브 쿼리에는 ORDER BY와 LIMIT을 동시에 사용할 수 없다.
* FROM 절에 사용하는 서브 쿼리는 상관 서브 쿼리 형태로 사용할 수 없다.      
  다음 예제 쿼리에서는 FROM 절의 서브 쿼리가 바깥에서 정의된 departments 테이블의 dept_no를 참조하고 있다.      
  하지만 이런 형태의 쿼리는 칼럼을 인식할 수 없다는 오류 메시지를 발생시킨다.        
  <br />

```
musql> SELECT * 
       FROM departments d, 
         (SELECT * FROM dept_emp de WHERE de.dept_no=d.dept_no) x
       WHERE d.dept_no=x.dept_no LIMIT 10;

ERROR 1054 (42S22): Unknown column 'd.dept_no' in 'where clause'
```

* 서브 쿼리를 이용해 하나의 테이블에 대해 읽고 쓰기를 동시에 할 수 없다.

```
UPDATE departments
  SET dept_name=( SELECT CONCAT(dept_name, '2') FROM departments WHERE dept_no='d009' )
WHERE dept_no='d001';

ERROR 1093 (HY000): You can't specity target table 'departments' for update in FROM clause
```

위 예제는 서브 쿼리를 이용해 departments 테이블을 읽고, 조회된 값을 다시 departments 테이블에 업데이트하는 쿼리다.    
실제 읽는 레코드와 변경하는 레코드는 다르지만 MySQL은 이를 허용하지 않는다.    
이러한 형태의 구문이 꼭 필요하다면 서브 쿼리의 결과를 임시 테이블로 저장하도록 쿼리를 변경하면 된다.    
그러나 그 방법은 데드락의 원인이 되기도 하므로 주의해야 한다.


<br />  

### SELECT 절에 사용된 서브 쿼리
SELECT 절에 사용된 서브 쿼리는 내부적으로 임시 테이블을 만든다거나 쿼리를 비효율적으로 실행하도록 만들지는 않기 때문에      
서브 쿼리가 적절히 인덱스를 사용할 수 있다면 크게 주의할 사항은 없다.

일반적으로 SELECT 절에 서브 쿼리를 사용하면 그 서브 쿼리는 항상 칼럼과 레코드가 하나인 결과를 반환해야 한다.   
NULL이든 아니든 관계없이 레코드가 1건이 존재해야 한다는 것인데, MySQL에서 이 체크 조건이 조금은 느슨하다.    
<br />

**ex1.**
```
SELECT emp_no, (SELECT dept_name FROM departments WHERE dept_name='Sales1')
FROM dept_emp LIMIT 10;
```

서브 쿼리는 항상 결과가 0건이다. 하지만 에러를 발생하지 않고 서브 쿼리의 결과는 NULL로 채워져서 반환한다.   
<br />

**ex2.**
```
SELECT emp_no, (SELECT dept_name FROM departments)
FROM dept_emp LIMIT 10;
```

서브 쿼리가 2건 이상의 레코드를 반환하는 경우에는 에러가 나면서 쿼리가 종료된다.   
<br />

**ex3.**
```
SELECT emp_no, (SELECT dept_no, dept_name FROM departments WHERE dept_name='Sales1')
FROM dept_emp LIMIT 10;
```

SELECT 절에 사용된 서브 쿼리가 2개 이상의 칼럼을 가져오려고 할 때도 에러가 발생한다.       
즉 SELECT 절의 서브 쿼리에는 로우 서브 쿼리를 사용할 수 없고 오로지 스칼라 서브 쿼리만 사용할 수 있다.


<br />  

가끔 조인으로 처리해도 되는 쿼리를 SELECT 절의 서브 쿼리를 사용해서 작성할 때도 있다.    
하지만 서브 쿼리로 실행될 때보다 조인으로 처리할 때가 훨씬 빠르기 때문에 가능하다면 조인으로 쿼리를 작성하는 것이 좋다.

```
SELECT SQL_NO_CACHE
  COUNT(concat(e1.first_name, 
              (SELECT e2.first_name FROM employees e2 WHERE e2.emp_no=e1.emp_no))
       )
FROM employees e1;

SELECT SQL_NO_CACHE
  COUNT(concat(e1.first_name, e2.first_name))
FROM employees e1, employees e2
WHERE e1.emp_no=e2.emp_no;
```

위 두 예제 쿼리 모두 employees 테이블을 두 번씩 프라이머리 키를 이용해 참조하는 쿼리다.            
emp_no은 프라이머리 키라서 조인이나 서브 쿼리 중 어떤 방식을 사용해도 같은 결과를 가져온다.      
(SQL_NO_CACHE는 성능을 비교해보기 위해 쿼리 캐시를 사용하지 않게 하는 힌트이다.)        
첫 번째 쿼리는 0.73, 조인을 사용한 두 번째 쿼리는 0.42초가 걸렸다.      
처리해야 하는 레코드 건수가 많아지면 많아질수록 성능 차이가 커지므로 가능하다면 조인으로 작성하길 권장한다.

<br />   

### WHERE 절에 단순 비교를 위해 사용된 서브 쿼리
서브 쿼리가 WHERE 절에서 사용될 때 어떻게 처리되는지 살펴보자.        
상관 서브 쿼리는 범위 제한 조건으로 사용되지 못하는데 이는 MySQL을 포함한 일반적인 RDBMS에서도 모두 똑같다.    
그리고 독립 서브 쿼리일 때는 서브 쿼리를 먼저 실행한 후 상수로 변환하고 그 조건을 범위 제한 조건으로 사용하는 것이 일반적인 RDBMS의 처리 방식이다.     
하지만 MySQL은 독립 서브 쿼리를 처리하는 방식은 조금 다르다.

```
SELECT * FROM dept_emp de
WHERE de.emp_no=
  (SELECT e.emp_no
   FROM employees e
   WHERE e.first_name='Georgi' AND e.last_name='Facello' LIMIT 1); 
```

이 쿼리는 dept_emp 테이블을 풀 테이블 스캔으로 레코드를 한 건씩 읽으면서 서브 쿼리를 매번 실행해서 서브 쿼리가 포함된 조건이 참인지 비교한다.   


