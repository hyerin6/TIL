<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>
  input.form-control { width: 200px; }
  select.form-control { width: 70px; }
  table.table { width: 500px; }
  thead tr { background-color: #eee; }
</style>
<title>Insert title here</title>
</head>
<body>

<%
String s1 = request.getParameter("select1");
boolean one = "one".equals(s1); 
boolean two = "two".equals(s1); 
boolean three = "three".equals(s1);
%>
<div class="container">
	<form method="get">
		<div class="form-group">
		<label>select1:</label>
			<select name="select1" class="form-control">
				<option value="one" <%= one ? "selected" : "" %>>one</option>
				<option value="two" <%= two ? "selected" : "" %>>two</option>
				<option value="three" <%= three? "selected" : "" %>>three</option>
			</select>
		</div>
		<div class="form-group">
			<label><input type="radio" name="radio1" value="one" <%= one ? "checked" : "" %> /> one</label>
			<label><input type="radio" name="radio1" value="two" <%= two ? "checked" : "" %> /> two</label>
			<label><input type="radio" name="radio1" value="three" <%= three ? "checked" : "" %> /> three</label>
		</div>
		<div class="form-group">
			<input type="text" class="form-control" name="text1" value="<%= s1 %>" />
		</div>
		<div class="form-group">
        	<button type="submit" class="btn btn-primary">확인</button>
        </div>
	</form>
</div>
</body>
</html>