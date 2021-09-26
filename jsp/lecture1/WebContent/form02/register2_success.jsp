<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="lecture1.form02.*" %>
<%
User user = (User)session.getAttribute("user");
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
      table.table { width: 400px; }
      td:nth-child(1) { background-color: #eee; width: 40%; }
  </style>
</head>
<body>

<div class="container">

<h1>회원가입 성공</h1>
<hr />

<table class="table table-bordered">
  <tr>
    <td>사용자 아이디</td>
    <td><%= user.getUserid() %></td>
  </tr>
  <tr>
    <td>이름</td>
    <td><%= user.getName() %></td>
  </tr>
  <tr>
    <td>이메일</td>
    <td><%= user.getEmail() %></td>
  </tr>
  <tr>
    <td>학과</td>
    <td><%= user.getDepartment() %></td>
  </tr>
</table>

</div>
</body>
</html>