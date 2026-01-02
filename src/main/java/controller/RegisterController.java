package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import entity.UserEntity;
import service.UserService;
import servlet.BaseServlet;

@WebServlet("/register")
public class RegisterController extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 新規ユーザー登録処理
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("passwordConfirm");

		// 入力チェック
		if (username == null || username.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null
				|| password.trim().isEmpty() || passwordConfirm == null || passwordConfirm.trim().isEmpty()) {
			request.setAttribute("errorMessage", "すべての項目を入力してください");
			request.getRequestDispatcher("/login/index.jsp").forward(request, response);
			return;
		}

		// ユーザー名の長さチェック
		if (username.length() < 3 || username.length() > 50) {
			request.setAttribute("errorMessage", "ユーザー名は3文字以上50文字以内で入力してください");
			request.getRequestDispatcher("/login/index.jsp").forward(request, response);
			return;
		}

		// パスワードの長さチェック
		if (password.length() < 6) {
			request.setAttribute("errorMessage", "パスワードは英数字記号で6文字以上で入力してください");
			request.getRequestDispatcher("/login/index.jsp").forward(request, response);
			return;
		}

		// パスワード一致チェック
		if (!password.equals(passwordConfirm)) {
			request.setAttribute("errorMessage", "パスワードが一致しません");
			request.getRequestDispatcher("/login/index.jsp").forward(request, response);
			return;
		}

		// BaseServletのsqlSessionFactoryを使用
		SqlSession sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();

			UserService userService = new UserService();

			// ユーザー名の重複チェック
			if (userService.isUsernameTaken(sqlSession, username)) {
				request.setAttribute("errorMessage", "このユーザー名は既に使用されています");
				request.getRequestDispatcher("/login/index.jsp").forward(request, response);
				return;
			}

			// ユーザーエンティティ作成
			UserEntity newUser = new UserEntity();
			newUser.setUsername(username);
			newUser.setEmail(email);
			newUser.setPassword(password); // 本番環境ではハッシュ化が必要

			// ユーザー登録
			boolean success = userService.registerUser(sqlSession, newUser);

			if (success) {
				// 登録成功
				request.setAttribute("successMessage", "登録が完了しました。ログインしてください。");
				request.getRequestDispatcher("/login/index.jsp").forward(request, response);
			} else {
				// 登録失敗
				request.setAttribute("errorMessage", "登録に失敗しました。もう一度お試しください。");
				request.getRequestDispatcher("/login/index.jsp").forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "システムエラーが発生しました");
			request.getRequestDispatcher("/login/index.jsp").forward(request, response);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	/**
	 * GETリクエストはログイン画面にリダイレクト
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/login");
	}
}