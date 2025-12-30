<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- JSTLタグライブラリの宣言 --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<%-- メッセージソースの読み込み（src/main/resources/messages.properties） --%>
<fmt:setBundle basename="messages" />

<jsp:include page="/layout/header.jsp" />

<%-- CSSの読み込み --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery.dataTables.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/sample1.css" />

<div class="container">
	<h1>
		<fmt:message key="title.main" />
	</h1>

	<%-- 入力フォームエリア --%>
	<div class="form-section"
		style="margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px;">
		<input type="text" id="subjectName"
			placeholder="<fmt:message key="label.subject" />"> <select
			id="evaluation">
			<option value="A判定">A判定</option>
			<option value="B判定">B判定</option>
			<option value="C判定">C判定</option>
		</select>
		<button type="button" id="btnAdd" class="btn-primary">追加登録</button>
		<button type="button" id="btnDeleteSelected" class="btn-danger"
			style="background-color: #dc3545; color: white; border: none; padding: 5px 15px; border-radius: 4px; cursor: pointer;">
			選択した行を削除</button>
		<%-- 全件削除ボタン（管理者用） 
		<button type="button" id="allDeleteButton" class="btn-danger"
			style="background-color: #6c757d; color: white; border: none; padding: 5px 15px; border-radius: 4px; cursor: pointer; margin-left: 10px;">
			全件データ削除</button>--%>
	</div>

	<%-- データテーブル --%>
	<div class="table-wrapper">
		<p style="font-size: 0.9em; color: #666;">※行をクリックすると選択（青色反転）できます</p>
		<table id="mainTable" class="display" style="width: 100%">
			<thead>
				<tr>
					<th><fmt:message key="label.id" /></th>
					<th><fmt:message key="label.subject" /></th>
					<th><fmt:message key="label.evaluation" /></th>
					<th><fmt:message key="label.update_date" /></th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</div>

<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>
<script
	src="${pageContext.request.contextPath}/js/dataTables.select.min.js"></script>

<script>
	// JS側で利用するAPIパス
	const API_URL = "${pageContext.request.contextPath}/api/data";
</script>
<script src="${pageContext.request.contextPath}/js/sample1.js"></script>

<jsp:include page="/layout/footer.jsp" />