<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="R" value="/" />
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
        rel="stylesheet" media="screen">
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script src="${R}res/common.js"></script>
  <link rel="stylesheet" href="${R}res/common.css">
</head>
<body>
<div class="container">
  <h1>교수목록</h1>
  <table class="table table-bordered mt5">
    <thead>
      <tr>
        <th>id</th>
        <th>이름</th>
        <th>학과id</th>
        <th>학과id</th>
        <th>학과명</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="professor" items="${ professors }">
        <tr>
          <td>${ professor.id }</td>
          <td>${ professor.professorName }</td>
          <td>${ professor.departmentId }</td>
          <td>${ professor.department.id }</td>
          <td>${ professor.department.departmentName }</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
