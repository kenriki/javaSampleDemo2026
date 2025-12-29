<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- ヘッダーの読み込み --%>
<jsp:include page="/layout/header.jsp" />

<h1>Java 配列の宣言・インスタンス化・初期化</h1>

<%
    // 一次元配列：宣言と初期化
    String[] categories = {"一次元配列", "二次元配列", "多次元配列"};

    // 二次元配列：クラス名と点数のイメージ
    String[][] scoreData = {
        {"Java基礎", "A判定"},
        {"Webアプリ", "B判定"},
        {"データベース", "A判定"}
    };
%>

<h3>1. 一次元配列の使用例</h3>
<ul>
    <% for(String cat : categories) { %>
        <li><%= cat %></li>
    <% } %>
</ul>

<h3>2. 二次元配列の使用例</h3>
<table>
    <tr><th>学習項目</th><th>評価</th></tr>
    <% for(int i = 0; i < scoreData.length; i++) { %>
        <tr>
            <td><%= scoreData[i][0] %></td>
            <td><%= scoreData[i][1] %></td>
        </tr>
    <% } %>
</table>

<%-- フッターの読み込み --%>
<jsp:include page="/layout/footer.jsp" />