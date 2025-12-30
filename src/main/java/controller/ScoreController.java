package controller;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import dao.ScoreMapper;
import entity.ScoreEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

			// 3. データを JSON 文字列に変換
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
			// 親クラスのメソッドでパスワード照合
			if (checkAdminPassword(password)) {
				// 親クラスのメソッドでTRUNCATE実行（IDもリセットされる）
				executeTruncate();
				response.getWriter().write("success");
			} else {
				// パスワード不一致エラー
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("管理者パスワードが正しくありません。");
			}
			return; // 処理終了
		}

		try (SqlSession session = sqlSessionFactory.openSession()) {
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);

			if ("delete".equals(action)) {
				// --- B. 選択行の削除処理（論理削除） ---
				String idStr = request.getParameter("id");
				if (idStr != null) {
					int id = Integer.parseInt(idStr);
					mapper.delete(id); // XML側でフラグを'D'に更新
				}

			} else {
				// --- C. 通常の登録処理 ---
				String subjectName = request.getParameter("subjectName");
				String evaluation = request.getParameter("evaluation");

				// 入力値チェック（NULLによるDB制約違反を防止）
				if (subjectName == null || subjectName.trim().isEmpty()) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("学習項目名を入力してください。");
					return;
				}

				ScoreEntity score = new ScoreEntity();
				score.setSubjectName(subjectName);
				score.setEvaluation(evaluation);

				mapper.insert(score); // MyBatis実行
			}

			// 更新系処理は必ずコミット
			session.commit();
			response.getWriter().write("success");

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました。");
		}
	}

	/**
	 * DataTable用のJSON形式に変換するヘルパーメソッド IDはデータベースの値をそのまま使用
	 */
	private String convertToJson(List<ScoreEntity> scores) {
		StringBuilder sb = new StringBuilder("{\"data\": [");
		for (int i = 0; i < scores.size(); i++) {
			ScoreEntity s = scores.get(i);
			if (i > 0)
				sb.append(",");
			// 1カラム目をIDとして構築
			sb.append(String.format("[\"%d\",\"%s\",\"%s\",\"%s\"]", s.getId(), s.getSubjectName(), s.getEvaluation(),
					s.getUpdateDate()));
		}
		sb.append("]}");
		return sb.toString();
	}
}