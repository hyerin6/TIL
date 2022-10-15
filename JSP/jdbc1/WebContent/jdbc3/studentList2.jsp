<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, lecture1.jdbc2.*" %>
<%
int currentPage = 1;
int pageSize = 10;

String pg = request.getParameter("pg");
if (pg != null) currentPage = Integer.parseInt(pg);

List<Student> list = StudentDAO.findAll(currentPage, pageSize);
int recordCount = StudentDAO.count();
int pageCount = (int)Math.ceil((double)recordCount / pageSize); // Math.ceil - 올림
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
      body { font-family: 굴림체; }
      thead th { background-color: #eee; }
      table.table { width: 700px; }
  </style>
</head>
<body>

<div class="container">
<h1>학생목록</h1>

<table class="table table-bordered table-condensed">
    <thead>
        <tr>
            <th>id</th>
            <th>학번</th>
            <th>이름</th>
            <th>학과</th>
            <th>학년</th>
        </tr>
    </thead>
    <tbody>
        <% for (Student student : list) { %>
            <tr>
                <td><%= student.getId() %></td>
                <td><%= student.getStudentNumber() %></td>
                <td><%= student.getName() %></td>
                <td><%= student.getDepartmentName() %></td>
                <td><%= student.getYear() %></td>
            </tr>
        <% } %>
    </tbody>
</table>

<% if (currentPage > 1) { %> <!-- 처음인데 이전 버튼 보일 필요 없음 / &lt; 는 '<' 이다. -->
    <a class="btn btn-default" href="studentList2.jsp?pg=<%= currentPage-1 %>"> &lt; </a>
<% } %>
<% if (currentPage < pageCount) { %> <!-- 마지막인데 다음 버튼 보일 필요 없음 / &gt; 는 '>' 이다. -->
    <a class="btn btn-default" href="studentList2.jsp?pg=<%= currentPage+1 %>"> &gt; </a>
<% } %>

</div>
</body>
</html>
