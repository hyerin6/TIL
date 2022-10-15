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
</head>
<body>
<%
String cmd = request.getParameter("cmd");

String one_checked = "one".equals(cmd) ? "checked" : "";
String two_checked = "two".equals(cmd) ? "checked" : "";
String three_checked = "three".equals(cmd) ? "checked" : "";
%>
<div class="container">
	<form method="get">
		<div class="form-group">
		<label>select1:</label>
			<select name="cmd" class="form-control">
				<option value="one" <%= "one".equals(cmd) ? "selected" : "" %>>one</option>
				<option value="two" <%= "two".equals(cmd) ? "selected" : "" %>>two</option>
				<option value="three" <%= "three".equals(cmd) ? "selected" : "" %>>three</option>
			</select>
		</div>
		<div class="radio">
			<label><input type="radio" name="cmd" value="one" <%= one_checked %> /> one</label>
			<label><input type="radio" name="cmd" value="two" <%= two_checked %> /> two</label>
			<label><input type="radio" name="cmd" value="three" <%= three_checked %> /> three</label>
		</div>
		<div class="form-group">
			<input type="text" name="cmd" class="form-control" value="<%= cmd %>" />
		</div>
		<div class="form-group">
        	<button type="submit" class="btn btn-primary">확인</button>
      	</div>
	</form>
</div>
</body>
</html>