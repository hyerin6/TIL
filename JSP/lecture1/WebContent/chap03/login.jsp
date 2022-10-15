<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String id = request.getParameter("memberID");

/*
- 잘못된 아이디 입니다. 를 출력
http://localhost:8080/lecture1/chap03/login.jsp

- index.jsp로 이동
http://localhost:8080/lecture1/chap03/login.jsp?memberID=hyerin 
*/
if(id != null && id.equals("hyerin")){
	response.sendRedirect("http://localhost:8080/lecture1/chap03/index.jsp");
}else{
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>

잘못된 아이디 입니다. 

</body>
</html>
<% 
	} 
%>