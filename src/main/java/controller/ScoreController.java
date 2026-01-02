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
 * 学習記録の取得・登録・削除を制御するコントローラー。 BaseServletを継承し、MyBatisのSqlSessionFactoryを利用する。
 */
public class ScoreController extends BaseServlet {

	private static final long serialVersionUID = -3191452012459400080L;

	/**
	 * 一覧取得処理 (GET)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// MyBatisのセッションを開始
		try (SqlSession session = sqlSessionFactory.openSession()) {
			// 1. Mapperインターフェースを取得
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);

			// 2. MyBatis経由で全件取得を実行（論理削除フラグ 'D' 以外を取得）
			List<ScoreEntity> scores = mapper.findAll();

			// 3. データを JSON 文字列に変換 (ここで列数をJSPに合わせる)
			String json = convertToJson(scores);

			// 4. 親クラス(BaseServlet)のメソッドでレスポンスを返す
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

		// まず「何をするか（action）」を受け取る
		String action = request.getParameter("action");
		String password = request.getParameter("password");

		// --- A. 全件削除（TRUNCATE）処理 ---
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
				// --- B. 選択行の削除処理（論理削除） ---
				String idStr = request.getParameter("id");
				if (idStr != null) {
					int id = Integer.parseInt(idStr);
					mapper.delete(id);
				}

			} else {
				// --- C. 通常の登録処理 ---
				String subjectName = request.getParameter("subjectName");
				String evaluation = request.getParameter("evaluation");

				if (subjectName == null || subjectName.trim().isEmpty()) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("学習項目名を入力してください。");
					return;
				}

				// セッションからログインユーザー名を取得する
				HttpSession httpSession = request.getSession(false);
				String loginUser = "システム"; // デフォルト値
				if (httpSession != null) {
					String username = (String) httpSession.getAttribute("username");
					if (username != null && !username.isEmpty()) {
						loginUser = username;
					}
				}

				ScoreEntity score = new ScoreEntity();
				score.setSubjectName(subjectName);
				score.setEvaluation(evaluation);

				// セッション上のユーザー名を設定する
				score.setCreatedBy(loginUser);

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
	 * DataTable用のJSON形式に変換するヘルパーメソッド JSPの <thead> にある 7項目と順番を一致させる必要があります。
	 */
	private String convertToJson(List<ScoreEntity> scores) {
		StringBuilder sb = new StringBuilder("{\"data\": [");
		for (int i = 0; i < scores.size(); i++) {
			ScoreEntity s = scores.get(i);
			if (i > 0)
				sb.append(",");

			// 配列形式で 7列分作成:
			// [0]ID, [1]学習項目, [2]評価, [3]登録者, [4]更新者, [5]登録日, [6]更新日
			sb.append(String.format("[\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"]", s.getId(),
					escapeJson(s.getSubjectName()), escapeJson(s.getEvaluation()),
					s.getCreatedBy() != null ? escapeJson(s.getCreatedBy()) : "", "", // 更新者 (Entityにフィールドがないため空)
					"", // 登録日 (Entityにフィールドがないため空)
					s.getUpdateDate() != null ? s.getUpdateDate() : ""));
		}
		sb.append("]}");
		return sb.toString();
	}

	/**
	 * JSON文字列内の特殊文字をエスケープする簡易メソッド
	 */
	private String escapeJson(String str) {
		if (str == null)
			return "";
		return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
	}
}