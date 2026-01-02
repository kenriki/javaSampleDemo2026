package controller;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;

import entity.UserEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.UserService;
import servlet.BaseServlet;

public class LoginController extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * ログイン画面表示
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 既にログイン済みの場合はメイン画面へリダイレクト
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("loginUser") != null) {
			response.sendRedirect(request.getContextPath() + "/jsp/sample1/index.jsp");
			return;
		}

		// ログイン画面にフォワード
		request.getRequestDispatcher("/login/index.jsp").forward(request, response);
	}

	/**
	 * ログイン処理
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// 入力チェック
		if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			request.setAttribute("errorMessage", "ユーザー名とパスワードを入力してください");
			request.getRequestDispatcher("/login/index.jsp").forward(request, response);
			return;
		}

		// BaseServletのsqlSessionFactoryを使用
		SqlSession sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();

			UserService userService = new UserService();
			UserEntity user = userService.login(sqlSession, username, password);

			if (user != null) {
				// ログイン成功
				HttpSession session = request.getSession();
				session.setAttribute("loginUser", user);
				session.setAttribute("userId", user.getUserId());
				session.setAttribute("username", user.getUsername());

				// メイン画面へリダイレクト
				response.sendRedirect(request.getContextPath() + "/jsp/sample1/index.jsp");
			} else {
				// ログイン失敗
				request.setAttribute("errorMessage", "ユーザー名またはパスワードが正しくありません");
				request.setAttribute("username", username);
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
}