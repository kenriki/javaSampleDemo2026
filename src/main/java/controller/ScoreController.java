package controller;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import dao.ScoreMapper;
import entity.ScoreEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.BaseServlet;

/**
 * 学習記録の取得・登録・削除を制御するコントローラー。
 */
public class ScoreController extends BaseServlet {

	private static final long serialVersionUID = -3191452012459400080L;

	/**
	 * 一覧取得処理 (GET)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try (SqlSession session = sqlSessionFactory.openSession()) {
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);
			List<ScoreEntity> scores = mapper.findAll();
			String json = convertToJson(scores);
			sendAsJson(response, json);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = msgProps.getProperty("error.db.connection", "データ取得に失敗しました。");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errMsg);
		}
	}

	/**
	 * 登録・削除処理 (POST)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		String password = request.getParameter("password");

		if ("truncate".equals(action)) {
			if (checkAdminPassword(password)) {
				executeTruncate();
				response.getWriter().write("success");
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("管理者パスワードが正しくありません。");
			}
			return;
		}

		try (SqlSession session = sqlSessionFactory.openSession()) {
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);

			if ("delete".equals(action)) {
				String idStr = request.getParameter("id");
				if (idStr != null) {
					int id = Integer.parseInt(idStr);
					mapper.delete(id);
				}
			} else {
				String subjectName = request.getParameter("subjectName");
				String evaluation = request.getParameter("evaluation");

				if (subjectName == null || subjectName.trim().isEmpty()) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("学習項目名を入力してください。");
					return;
				}

				// セッションからログインユーザー名を取得
				HttpSession httpSession = request.getSession(false);
				String loginUser = "システム";
				if (httpSession != null) {
					String username = (String) httpSession.getAttribute("username");
					if (username != null && !username.isEmpty()) {
						loginUser = username;
					}
				}

				ScoreEntity score = new ScoreEntity();
				score.setSubjectName(subjectName);
				score.setEvaluation(evaluation);
				score.setCreatedBy(loginUser); // ログインユーザーをセット

				mapper.insert(score);
			}

			session.commit();
			response.getWriter().write("success");

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました。");
		}
	}

	/**
	 * DataTable用のJSON形式に変換 インデックス順序を修正: [0]ID, [1]項目, [2]評価, [3]登録者, [4]更新者,
	 * [5]登録日, [6]更新日
	 */
	private String convertToJson(List<ScoreEntity> scores) {
		StringBuilder sb = new StringBuilder("{\"data\": [");
		for (int i = 0; i < scores.size(); i++) {
			ScoreEntity s = scores.get(i);
			if (i > 0)
				sb.append(",");

			// 修正箇所: 5番目(登録日)と6番目(更新日)の値を正しいフィールドに変更
			// Entityに getCreatedDate() 等がない場合は、適切なフィールド名に読み替えてください。
			sb.append(String.format("[\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"]", s.getId(), // [0] ID
					escapeJson(s.getSubjectName()), // [1] 学習項目
					escapeJson(s.getEvaluation()), // [2] 評価
					s.getCreatedBy() != null ? escapeJson(s.getCreatedBy()) : "", // [3] 登録者
					"", // [4] 更新者 (空欄)
					s.getUpdateDate() != null ? s.getUpdateDate() : "", // [5] 登録日 (※一旦既存データが入るUpdateDateを指定)
					"" // [6] 更新日 (空欄にする)
			));
		}
		sb.append("]}");
		return sb.toString();
	}

	private String escapeJson(String str) {
		if (str == null)
			return "";
		return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
	}
}