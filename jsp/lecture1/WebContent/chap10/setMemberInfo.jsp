<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
session.setAttribute("MEMBERID", "hyerin");
session.setAttribute("NAME", "박혜린");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>세션에 정보 저장</title>
</head>
<body>

세션에 정보를 저장했습니다.

<%
String name = (String)session.getAttribute("NAME");
%>
<hr />
회원명 : <%= name %>

</body>
</html>