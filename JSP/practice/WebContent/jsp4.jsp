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
    for (int i=1; i <= 10; ++i) {
        out.print("4 x " + i);
        out.print(" = ");
        out.println(4 * i + "<br>");
    }
%>

<!-- 위 코드의 실행 결과로 출력되는 내용을 적으시오.
4 x 1 = 4
4 x 2 = 8
4 x 3 = 12
4 x 4 = 16
4 x 5 = 20
4 x 6 = 24
4 x 7 = 28
4 x 8 = 32
4 x 9 = 36
4 x 10 = 40
-->

</body>
</html>