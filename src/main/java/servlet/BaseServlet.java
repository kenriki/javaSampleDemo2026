package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

public abstract class BaseServlet extends HttpServlet {
	protected Properties dbProps = new Properties();

	@Override
	public void init() throws ServletException {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
			if (is != null)
				dbProps.load(is);
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}

	// JSON 形式にしてクライアントに送る
	protected void sendAsJson(HttpServletResponse response, String jsonContent) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonContent);
		out.flush();
	}
}