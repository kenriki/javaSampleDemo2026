<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン - Sample1App</title>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	min-height: 100vh;
	display: flex;
	justify-content: center;
	align-items: center;
}

.login-container {
	background: white;
	border-radius: 10px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
	width: 400px;
	padding: 40px;
}

.login-header {
	text-align: center;
	margin-bottom: 30px;
}

.login-header h1 {
	color: #333;
	font-size: 28px;
	margin-bottom: 10px;
}

.login-header p {
	color: #666;
	font-size: 14px;
}

.form-group {
	margin-bottom: 20px;
}

.form-group label {
	display: block;
	color: #333;
	font-weight: 600;
	margin-bottom: 8px;
	font-size: 14px;
}

.form-group input {
	width: 100%;
	padding: 12px 15px;
	border: 2px solid #e0e0e0;
	border-radius: 5px;
	font-size: 14px;
	transition: border-color 0.3s;
}

.form-group input:focus {
	outline: none;
	border-color: #667eea;
}

.error-message {
	background-color: #fee;
	color: #c33;
	padding: 12px;
	border-radius: 5px;
	margin-bottom: 20px;
	font-size: 14px;
	border-left: 4px solid #c33;
}

.success-message {
	background-color: #d4edda;
	color: #155724;
	padding: 12px;
	border-radius: 5px;
	margin-bottom: 20px;
	font-size: 14px;
	border-left: 4px solid #28a745;
}

.login-button {
	width: 100%;
	padding: 12px;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	color: white;
	border: none;
	border-radius: 5px;
	font-size: 16px;
	font-weight: 600;
	cursor: pointer;
	transition: transform 0.2s, box-shadow 0.2s;
}

.login-button:hover {
	transform: translateY(-2px);
	box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.login-button:active {
	transform: translateY(0);
}

.login-footer {
	text-align: center;
	margin-top: 20px;
	color: #666;
	font-size: 14px;
}

.demo-info {
	margin-top: 20px;
	padding: 15px;
	background-color: #f8f9fa;
	border-radius: 5px;
	font-size: 13px;
	color: #666;
}

.demo-info strong {
	color: #333;
}

.register-link {
	text-align: center;
	margin-top: 20px;
}

.register-link a {
	color: #667eea;
	text-decoration: none;
	font-weight: 600;
	font-size: 14px;
	cursor: pointer;
}

.register-link a:hover {
	text-decoration: underline;
}

/* モーダルスタイル */
.modal {
	display: none;
	position: fixed;
	z-index: 1000;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	overflow: auto;
	background-color: rgba(0, 0, 0, 0.5);
	animation: fadeIn 0.3s;
}

@
keyframes fadeIn {from { opacity:0;
	
}

to {
	opacity: 1;
}

}
.modal-content {
	background-color: white;
	margin: 5% auto;
	padding: 40px;
	border-radius: 10px;
	width: 90%;
	max-width: 450px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
	animation: slideDown 0.3s;
}

@
keyframes slideDown {from { transform:translateY(-50px);
	opacity: 0;
}

to {
	transform: translateY(0);
	opacity: 1;
}

}
.close {
	color: #aaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
	line-height: 20px;
	cursor: pointer;
}

.close:hover, .close:focus {
	color: #000;
}

.modal-header {
	margin-bottom: 30px;
}

.modal-header h2 {
	color: #333;
	font-size: 24px;
	margin-top: 10px;
}

.register-button {
	width: 100%;
	padding: 12px;
	background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
	color: white;
	border: none;
	border-radius: 5px;
	font-size: 16px;
	font-weight: 600;
	cursor: pointer;
	transition: transform 0.2s, box-shadow 0.2s;
}

.register-button:hover {
	transform: translateY(-2px);
	box-shadow: 0 5px 15px rgba(40, 167, 69, 0.4);
}

.register-button:active {
	transform: translateY(0);
}

.password-hint {
	font-size: 12px;
	color: #666;
	margin-top: 5px;
}
</style>
</head>
<body>
	<div class="login-container">
		<div class="login-header">
			<h1>ログイン</h1>
			<p>Sample1App Java学習サイト</p>
		</div>

		<%
		if (request.getAttribute("errorMessage") != null) {
		%>
		<div class="error-message">
			<%=request.getAttribute("errorMessage")%>
		</div>
		<%
		}
		%>

		<%
		if (request.getAttribute("successMessage") != null) {
		%>
		<div class="success-message">
			<%=request.getAttribute("successMessage")%>
		</div>
		<%
		}
		%>

		<form action="<%=request.getContextPath()%>/login" method="post">
			<div class="form-group">
				<label for="username">ユーザー名</label> <input type="text" id="username"
					name="username" placeholder="ユーザー名を入力"
					value="<%=request.getAttribute("username") != null ? request.getAttribute("username") : ""%>"
					required autofocus>
			</div>

			<div class="form-group">
				<label for="password">パスワード</label> <input type="password"
					id="password" name="password" placeholder="パスワードを入力" required>
			</div>

			<button type="submit" class="login-button">ログイン</button>
		</form>

		<div class="register-link">
			アカウントをお持ちでない方は <a id="openRegisterModal">新規登録</a>
		</div>

<%--		<div class="demo-info">
			<strong>テストアカウント:</strong><br> ユーザー名: testuser<br> パスワード:
			password123
		</div>
 --%>
		<div class="login-footer">© 2026 Sample1App. All rights
			reserved.</div>
	</div>

	<!-- 新規登録モーダル -->
	<div id="registerModal" class="modal">
		<div class="modal-content">
			<span class="close">&times;</span>
			<div class="modal-header">
				<h2>新規ユーザー登録</h2>
			</div>

			<form action="<%=request.getContextPath()%>/register" method="post">
				<div class="form-group">
					<label for="reg-username">ユーザー名</label> <input type="text"
						id="reg-username" name="username" placeholder="ユーザー名を入力" required
						minlength="3" maxlength="50">
					<p class="password-hint">3文字以上、50文字以内</p>
				</div>

				<div class="form-group">
					<label for="reg-email">メールアドレス</label> <input type="email"
						id="reg-email" name="email" placeholder="メールアドレスを入力" required>
				</div>

				<div class="form-group">
					<label for="reg-password">パスワード</label> <input type="password"
						id="reg-password" name="password" placeholder="パスワードを入力" required
						minlength="6" maxlength="255">
					<p class="password-hint">6文字以上</p>
				</div>

				<div class="form-group">
					<label for="reg-password-confirm">パスワード（確認）</label> <input
						type="password" id="reg-password-confirm" name="passwordConfirm"
						placeholder="パスワードを再入力" required minlength="6" maxlength="255">
				</div>

				<button type="submit" class="register-button">登録</button>
			</form>
		</div>
	</div>

	<script>
		// モーダルの要素取得
		const modal = document.getElementById('registerModal');
		const openBtn = document.getElementById('openRegisterModal');
		const closeBtn = document.getElementsByClassName('close')[0];

		// モーダルを開く
		openBtn.onclick = function() {
			modal.style.display = 'block';
		}

		// モーダルを閉じる（×ボタン）
		closeBtn.onclick = function() {
			modal.style.display = 'none';
		}

		// モーダルを閉じる（外側クリック）
		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = 'none';
			}
		}

		// パスワード確認のバリデーション
		const form = document.querySelector('#registerModal form');
		form.onsubmit = function(e) {
			const password = document.getElementById('reg-password').value;
			const passwordConfirm = document
					.getElementById('reg-password-confirm').value;

			if (password !== passwordConfirm) {
				e.preventDefault();
				alert('パスワードが一致しません');
				return false;
			}

			return true;
		}
	</script>
</body>
</html>