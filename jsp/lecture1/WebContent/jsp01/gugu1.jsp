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
<title>구구단을 외자!</title>
</head>
<body>

<table>
<% 
	for(int i = 1; i <= 9; i++){
		out.print("<tr>");
		for(int j = 2; j <= 9; j++){
			out.println("<td>" + j + " × "
		+ i + " = "+ i*j + "</td>");
		}
		out.println("</tr>");
	}
%>
</table>

</body>
</html>