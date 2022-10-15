# 1, JDBC 드라이버 
- JDBC(Java DataBase Connectivity)는 자바에서 데이터베이스 연동 프로그램을 개발하려고 만든 기술이다.  
- DBMS(DataBase Management System)은 데이터 베이스 관리 시스템을 말한다.  
자료에 대한 사용자의 다양한 요구를 적절히 처리하고 응답하여 이를 사용할 수 있도록 해주는 시스템이다.  
종류가 다양하며, 그 구조와 특징도 다 다르다.  
- 자바는 모든 DBMS에서 공통으로 사용할 수 있는 인터페이스와 클래스로 구성된 JDBC를 제공한다.  

**JDBC 프로그래밍의 구조**
<img width="1439" alt="2019-03-05 11 59 58" src="https://user-images.githubusercontent.com/33855307/53815004-220d6380-3fa4-11e9-8767-ec677f9e43da.png">  


- JSP를 포함한 자바 어플리케이션에서 데이터베이스를 사용할 때 데이터베이스 종류에 상관없이 JDBC API를 이용해서 데이터베이스에 접근한다.  
- 각각의 DBMS는 자신에게 알맞은 JDBC 드라이버를 제공하고 있다.  


