package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/data")
public class DataServlet extends HttpServlet {
	private Properties dbProps = new Properties();

	@Override
	public void init() throws ServletException {
		// クラスパスからプロパティファイルを読み込む
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
			if (is != null) {
				dbProps.load(is);
			} else {
				throw new ServletException("db.properties not found");
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// プロパティファイルから値を取得
		String url = dbProps.getProperty("db.url");
		String user = dbProps.getProperty("db.user");
		String password = dbProps.getProperty("db.password");

		StringBuilder json = new StringBuilder();
		json.append("{\"data\": [");

		try {
			Class.forName("org.postgresql.Driver");
			try (Connection conn = DriverManager.getConnection(url, user, password);
					// スキーマ名 db1 を指定
					PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM db1.learning_scores ORDER BY id");
					ResultSet rs = pstmt.executeQuery()) {

				boolean first = true;
				while (rs.next()) {
					if (!first)
						json.append(",");
					json.append("[").append("\"").append(rs.getInt("id")).append("\",").append("\"")
							.append(rs.getString("subject_name")).append("\",").append("\"")
							.append(rs.getString("evaluation")).append("\",").append("\"")
							.append(rs.getString("update_date")).append("\"").append("]");
					first = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		json.append("]}");
		out.print(json.toString());
	}
}