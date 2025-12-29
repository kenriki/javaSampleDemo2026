<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="/layout/header.jsp" />

<%-- ローカルライブラリの読み込み --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery.dataTables.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/sample1.css" />

<h1>Sample1: DataTables 実装</h1>

<div class="table-wrapper">
	<table id="mainTable" class="display" style="width: 100%">
		<thead>
			<tr>
				<th>ID</th>
				<th>学習項目</th>
				<th>評価</th>
				<th>更新日</th>
			</tr>
		</thead>
	</table>
</div>

<%-- JS読み込み（jQueryが先） --%>
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>

<%-- 外部JSにパスを教えるためのグローバル変数 --%>
<script>
	const API_URL = "${pageContext.request.contextPath}/api/data";
</script>
<script src="${pageContext.request.contextPath}/js/sample1.js"></script>

<jsp:include page="/layout/footer.jsp" />