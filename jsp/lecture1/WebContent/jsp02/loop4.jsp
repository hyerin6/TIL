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
  <% for (int i=0; i <= 3; ++i) { %>
    <tr>  
    <% for (int j=1; j <= 4; ++j) { %>
        <td><%= i * 4 + j %></td>
    <% } %>
    </tr>
  <% } %>
</table>

<table>
<% 
for (int i=0; i <= 3; ++i) { 
    out.println("<tr>");
    for (int j=1; j <= 4; ++j) {
        out.print("<td>");
        out.print( i * 4 + j );
        out.println("</td>");
    }
    out.println("</tr>");
}
%>
</table>
</body>
</html>