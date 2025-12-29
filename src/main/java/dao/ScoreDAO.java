package dao;

import java.sql.*;
import java.util.*;
import entity.Score;

public class ScoreDAO {
	private String url, user, password;

	public ScoreDAO(Properties props) {
		this.url = props.getProperty("db.url");
		this.user = props.getProperty("db.user");
		this.password = props.getProperty("db.password");
	}

	public List<Score> findAll() throws Exception {
		List<Score> list = new ArrayList<>();
		Class.forName("org.postgresql.Driver");

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM db1.learning_scores ORDER BY id");
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				Score s = new Score();
				s.setId(rs.getInt("id"));
				s.setSubjectName(rs.getString("subject_name"));
				s.setEvaluation(rs.getString("evaluation"));
				s.setUpdateDate(rs.getString("update_date"));
				list.add(s);
			}
		}
		return list;
	}
}