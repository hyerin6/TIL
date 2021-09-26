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
<title>구구단을 외자! 3</title>
</head>
<body>
<table>
<tr>
<% for(int i = 2; i <= 9; i++){ %>
<% if(i == 6){ %>
</tr>
<tr>
<% } %>
<td>
<% for(int j = 1; j <= 9; j++){
	out.println(i + " × " + j + " = " + i*j + "<br />");	
	}
}
%>
</td>
</tr>
</table>

</body>
</html>