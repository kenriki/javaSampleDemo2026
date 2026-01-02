<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
body {
	margin: 0;
	padding: 0;
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

header {
	background-color: #333;
	color: white;
	padding: 15px 20px;
	margin-bottom: 20px;
	position: relative;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

header h2 {
	margin: 0;
	display: inline-block;
}

.header-right {
	position: absolute;
	right: 20px;
	top: 50%;
	transform: translateY(-50%);
	display: flex;
	align-items: center;
	gap: 15px;
}

.user-info {
	color: #ecf0f1;
	font-size: 14px;
	font-weight: 500;
}

.logout-btn {
	background-color: #e74c3c;
	color: white;
	border: none;
	padding: 8px 20px;
	border-radius: 4px;
	cursor: pointer;
	text-decoration: none;
	font-size: 14px;
	font-weight: 600;
	transition: background-color 0.3s;
	display: inline-block;
}

.logout-btn:hover {
	background-color: #c0392b;
}

table {
	border-collapse: collapse;
	margin-bottom: 20px;
	width: 100%;
	max-width: 600px;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f4f4f4;
}

.container {
	padding: 20px;
}
</style>
</head>
<body>
	<header>
		<h2>Sample1App Java学習サイト</h2>
		<div class="header-right">
			<%
			if (username != null) {
			%>
			<span class="user-info">ようこそ、<%=username%>さん
			</span> <a href="<%=request.getContextPath()%>/logout" class="logout-btn">ログアウト</a>
			<%
			}
			%>
		</div>
	</header>
	<div class="container">