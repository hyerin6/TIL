<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>
<%
SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
String 현재시각 = format.format(new Date());
String userid = "guest";
String password = "";
boolean autologin = true;
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
      input.form-control { width: 200px; }
  </style>
</head>
<body>

<div class="container">

<h1>로그인</h1>
<hr />

<form method="post">
  <div class="form-group">
    <label>사용자 아이디</label>
    <input type="text" class="form-control" name="userid" value="<%= userid %>" />
  </div>
  <div class="form-group">
    <label>비밀번호</label>
    <input type="password" class="form-control" name="password" />
  </div>
  <div class="checkbox">
    <label>
      <input type="checkbox" name="autologin" checked /> 자동 로그인
    </label>
  </div>
  <button type="submit" class="btn btn-primary">
    <i class="glyphicon glyphicon-ok"></i> 로그인
  </button>
  <a href="register1.jsp" class="btn btn-default">
    <i class="glyphicon glyphicon-user"></i> 회원가입
  </a>
</form>

<hr />
<div class="alert alert-info">
  현재시각: <%= 현재시각 %>
</div>

</div>
</body>
</html>