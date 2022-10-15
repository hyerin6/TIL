<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>
  input.form-control { width: 200px; }
  select.form-control { width: 70px; }
  table.table { width: 500px; }
  thead tr { background-color: #eee; }
</style>
</head>
<body>

<%  for (int i=1; i <= 10; ++i) { %>
     4 * i = 4 * i
<%  } %>
<br />
<%  for (int i=1; i <= 10; ++i) { %>
     <%= 4 * i %> = <%= 4 * i %>
<%  } %>
<br />
<%  for (int i=1; i <= 10; ++i) { %>
     <%= 4 %> * <%= i %> = <%= 4 * i %>
<%  } %>
<br />
<%  for (int i=1; i <= 10; ++i) { %>
     4 * <%= i %> = <%= 4 * i %>
<%  } %>
<br />
<tr>
<%  for (int i=1; i <= 10; ++i) { %>
     <td>4 * <%= i %> = <%= 4 * i %></td>
<%  } %>
</tr>
<br />
<td>
<%  for (int i=1; i <= 10; ++i) { %>
     4 * <%= i %> = <%= 4 * i %><br />
<%  } %>
</td>

<!-- 위 코드의 실행 결과로 출력되는 내용을 적으시오.
1) 
4 * i = 4 * i ...

2)
4 = 4 8 = 8 12 = 12 ... 

3) 
4 * 1 = 4 4 * 2 = 8 ...

4) 
4 * 1 = 4 4 * 2 = 8 ...

5)
4 * 1 = 4 4 * 2 = 8 ...

6)
4 * 1 = 4
4 * 2 = 8 
.
.
.
4 * 10 = 40
-->
</body>
</html>