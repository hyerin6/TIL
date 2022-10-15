<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style> 
	table { border-collapse: collapse; } 
	td { padding: 5px; border: solid 1px gray; } 
</style>
<title>loop</title>
</head>
<body>

<table>
	<tr>
<%
	for(int i = 1; i <= 10; ++i)
		out.println("	<td>" + i + "</td>");
%>
	</tr>
	<tr>
<% for (int i=1; i <= 10; ++i) { %>
 	<td><%= i %></td> 
<% } %>
	</tr>
</table>

</body>
</html>