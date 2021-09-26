<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>
<%
String userid = (String)session.getAttribute("userid");
boolean autologin = (boolean)session.getAttribute("autologin");
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

<h1>로그인 성공</h1>
<hr />

<table class="table table-bordered">
  <tr>
    <td>사용자 아이디</td>
    <td><%= userid %></td>
  </tr>
  <tr>
    <td>자동 로그인</td>
    <td><%= autologin %></td>
  </tr>
</table>

</div>
</body>
</html>