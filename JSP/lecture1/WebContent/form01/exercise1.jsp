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
</style>
</head>
<body>
<%
String s = request.getParameter("number");
int number = (s == null) ? 0 : Integer.parseInt(s);

String cmd = request.getParameter("cmd");
int result = 0;
if("++".equals(cmd)) result = number+1;
else if("--".equals(cmd)) result = number-1;
%>

<div class="container">
	<form method="get">
		<div class="form-group">
			<label>number:</label>
			<input type="text" name="number" class="form-control" value="<%= result %>" />
		</div>
		<div class="form-group">
			<input type="submit" name="cmd" value="++" class="btn btn-default">
			<input type="submit" name="cmd" value="--" class="btn btn-default">
		</div>
	</form>
</div>
</body>
</html>