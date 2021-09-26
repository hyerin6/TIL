<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
String s1 = "hello";
String s2 = "world";

out.println(s1 + "<br>");
out.println(s2 + "<br>");
out.println(s1 + s2 + "<br>");
out.println("s1" + "<br>");
out.println("s2" + "<br>");
out.println("s1" + "s2" + "<br>");
out.println("s1 + s2" + "<br>");
%>

<%= s1 %> <br>
<%= s2 %> <br>
<%= s1 + s2 %> <br>
<%= "s1" %> <br>
<%= "s2" %> <br>
<%= "s1" + "s2" %> <br>
<%= "s1 + s2" %> <br>

<!-- 위 코드의 실행 결과로 출력되는 내용을 적으시오.
hello
world 
helloworld 
s1
s2
s1s2
s1 + s2
hello
world 
helloworld 
s1
s2
s1s2
s1 + s2
-->
</body>
</html>