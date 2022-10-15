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
<title>목록 선택 1</title>
<style>
	input.form-control { width: 200px; }
	select.form-control { width: 70px; }
</style>
</head>
<body>

<div class="container">
	<form action="text2action.jsp" method="get">
		<h1>select 01</h1>
		
		<div class="form-group">
			<label>number1:</label>
			<input type="text" name="number1" class="form-control" value="34" />
		</div>
		
		<div class="form-group">
			<label>operator:</label>
			<select name="cmd" class="form-control">
				<option value="+">+</option> 
				<option value="-">-</option> 
				<option value="*">*</option> 
				<option value="/">/</option>
			</select>
		</div>
		
		<div class="form-group">
			<label>number2:</label>
			<input type="text" name="number2" class="form-control" value="8" />
		</div>
		
		<div class="form-group">
			<button type="submit" class="btn btn-primary">확인</button>
		</div>
	</form>
</div>
</body>
</html>