package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

public abstract class BaseServlet extends HttpServlet {
	protected Properties dbProps = new Properties();  // dbアクセス用を追加
	protected Properties msgProps = new Properties(); // メッセージ用を追加

	@Override
	public void init() throws ServletException {
		// DB情報の読み込み
		loadConfig("db.properties", dbProps);
		// メッセージ情報の読み込み
		loadConfig("messages.properties", msgProps);
	}

	private void loadConfig(String fileName, Properties props) throws ServletException {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
			if (is != null) {
				props.load(is);
			} else {
				System.out.println("Warning: " + fileName + " not found.");
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}

	protected void sendAsJson(HttpServletResponse response, String jsonContent) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonContent);
		out.flush();
	}
}