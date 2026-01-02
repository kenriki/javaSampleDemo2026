package filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ログインチェックフィルター /jsp/ と /layout/ 配下のページにアクセスする際、ログインしていなければログイン画面へリダイレクト
 */
@WebFilter(urlPatterns = { "/jsp/*", "/layout/*" })
public class LoginCheckFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);

		// ログインチェック
		boolean isLoggedIn = (session != null && session.getAttribute("loginUser") != null);

		if (!isLoggedIn) {
			// ログインしていない場合、ログイン画面へリダイレクト
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
		} else {
			// ログイン済みの場合、次のフィルターまたはサーブレットへ
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}
}