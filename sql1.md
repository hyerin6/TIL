# SQL

sql은 대소문자를 구별하지 않는다.   
여러 줄로 작성이 가능하다.   
구문은 주로 나누어 작성한다.   
세미콜론(;)으로 문장을 마친다.   


#### [ SQL의 분류 ]
 
- DML (Data Manipulation Language)   
데이터 조작 언어   
데이터를 조작하는 데 사용되는 언어 (선택, 삽입, 수정, 삭제)  
DML 구문이 사용되는 대상은 테이블의 행  
DML 사용하기 위해서는 꼭 그 이전에 테이블이 정의되어 있어야 함  
SQL문 중 SELECT , INSERT, UPDATE, DELETE가 이 구문에 해당  

- DDL (Data Definition Language)  
데이터 정의 언어  
데이터베이스, 테이블, 뷰, 인덱스 등의 데이터베이스 개체를 생성/삭제/변경하는 역할  
CREATE, DROP, ALTER 구문  

- DCL (Data Control Language)    
데이터 제어 언어    
사용자에게 어떤 권한을 부여하거나 빼앗을 때 주로 사용하는 구문    
GRANT/REVOKE/DENY 구문    
  
  
#### [ 데이터베이스 복원/백업 ]  

- 명령어 창에서 복원  
``` mysql -u[user] -p[DB name] [TABLE name] < [FILE name.sql] ```
  
- mysql 명령창에서 복원   
``` source [FILE name.sql] ```
  
- 명령어 창에서 백업  
``` mysqldump -u[user] -p[DB name] [TABLE name] > [FILE name.sql] ``` 







 
