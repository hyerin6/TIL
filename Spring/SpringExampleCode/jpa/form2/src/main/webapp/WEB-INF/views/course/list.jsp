<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:url var="R" value="/" />
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" 
        rel="stylesheet" media="screen">
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <style>
    td:nth-child(3) { width: 100px; }
  </style>
<body>
<div class="container">
  <h1>강좌목록</h1>
  
  <form method="post">
  
    <table class="table table-bordered mt5">
      <thead>
        <tr>
          <th>id</th>
          <th>강좌명</th>
          <th>학점</th>
          <th>시작일</th>
          <th>개설학과</th>
          <th>담당교수</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="course" items="${ courses }">
          <tr>
            <td>
                ${ course.id }
                <input type="hidden" name="id" value="${ course.id }" />
            </td>
            <td><input type="text" name="courseName" value="${ course.courseName }" class="form-control" /></td>
            <td><input type="text" name="unit" value="${ course.unit }" class="form-control" /></td>
            <td>
                <fmt:formatDate value="${ course.startDate }" pattern="yyyy-MM-dd" var="dt"/>
                <input type="date" name="startDate" value="${ dt }" class="form-control" />
            </td>
            <td>
              <select name="departmentId" class="form-control">
                <c:forEach var="department" items="${ departments }">
                  <c:set var="selected" value="${ department.id == course.departmentId ? 'selected' : '' }" />
                  <option value="${ department.id }" ${ selected }>${ department.departmentName }</option>
                </c:forEach>                
              </select>
            </td>
            <td>
              <select name="professorId" class="form-control">
                <c:forEach var="professor" items="${ professors }">
                  <c:set var="selected" value="${ professor.id == course.professorId ? 'selected' : '' }" />
                  <option value="${ professor.id }" ${ selected }>${ professor.professorName }</option>
                </c:forEach>                
              </select>
            </td>           
          </tr>
        </c:forEach>
      </tbody>
    </table>
    
    <button type="submit" class="btn btn-primary">저장</button>
  </form>
</div>
</body>
</html>

