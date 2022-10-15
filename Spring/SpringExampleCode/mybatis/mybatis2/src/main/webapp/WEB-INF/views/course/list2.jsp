<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
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
  <h1>강좌목록</h1>
  <table class="table table-bordered mt5">
    <thead>
      <tr>
        <th>id</th>
        <th>강좌명</th>
        <th>학점</th>
        <th>시작일</th>
        <th>담당교수id</th>
        <th>담당교수</th>
        <th>개설학과id</th>
        <th>개설학과</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="course" items="${ courses }">
        <tr>
          <td>${ course.id }</td>
          <td>${ course.courseName }</td>
          <td>${ course.unit }</td>
          <td><fmt:formatDate pattern = "yyyy-MM-dd" value = "${ course.startDate }" /></td>
          <td>${ course.professor.id }</td>
          <td>${ course.professor.professorName }</td>
          <td>${ course.department.id }</td>
          <td>${ course.department.departmentName }</td>
        </tr>
        <tr>
          <td>수강생</td>
          <td colspan="7" style="color: gray;">

            <c:forEach var="student" items="${ course.students }">
              ${ student.name }
            </c:forEach>

          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
