<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, lecture1.jdbc1.*" %>
<%
String srchText = request.getParameter("srchText");
if (srchText == null) srchText = "";
List<Student> list = StudentDAO2.findByName(srchText);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <style>
      thead th { background-color: #eee; }
      table.table { width: 700px; margin-top: 10px; }
  </style>
</head>
<body>

<!-- 
Q. 처음 실행하면 전체 학생 목록이 조회되는 이유는?
A. 처음 실행하면 name에 null이 전달되는데 빈문자열로("") 넘겨지기 때문에 
목록 조회 조건에는 '%'만 전달되어 전체 학생 목록이 조회되는 것이다.
-->

<div class="container">
<h1>학생목록</h1>

<form class="form-inline">
  <div class="form-group">
    <label>이름</label>
    <input type="text" class="form-control" name="srchText" value="<%= srchText %>" 
           placeholder="검색조건" />
  </div>
  <button type="submit" class="btn btn-primary">조회</button>
</form>

<table class="table table-bordered table-condensed">
    <thead>
        <tr>
            <th>학번</th>
            <th>이름</th>
            <th>학과</th>
            <th>학년</th>
        </tr>
    </thead>
    <tbody>
        <% for (Student student : list) { %>
            <tr>
                <td><%= student.getStudentNumber() %></td>
                <td><%= student.getName() %></td>
                <td><%= student.getDepartmentName() %></td>
                <td><%= student.getYear() %></td>
            </tr>
        <% } %>
    </tbody>
</table>

</div>
</body>
</html>