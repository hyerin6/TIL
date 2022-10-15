<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<c:url var="R" value="/" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="${R}res/common.js"></script>
<link rel="stylesheet" href="${R}res/common.css">
</head>
<body>
<div class="container">
  <h1>책 목록</h1>
  
  <div class="pull-right mb5">
  <a href="create" class="btn btn-info">책 등록</a>
  </div>    
  
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>ID</th>
        <th>제목</th>
        <th>저자</th>
        <th>카테고리</th>
        <th>가격</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="book" items="${ list }">
        <tr data-url="edit?id=${ book.id }&${ pagination.queryString }">
          <td>${ book.id }</td>
          <td>${ book.title }</td>
          <td>${ book.author }</td>
          <td>${ book.category.categoryName }</td>
          <td>${ book.price }</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <my:pagination pageSize="${ pagination.sz }" recordCount="${ pagination.recordCount }" 
                 queryStringName="pg" />
</div>
</body>
</html>
