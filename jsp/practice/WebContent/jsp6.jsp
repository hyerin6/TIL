<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<table border=1>
  <tr>
<% 
      for (int i=1; i <= 10; ++i) { 
        out.println("    <td>" + i + "</td>");
      }
%>
  </tr>
</table> 

<!-- 위 코드의 실행 결과로 출력되는 내용을 적으시오.
1 2 3 4 5 6 7 8 9 10 
-->

</body>
</html>