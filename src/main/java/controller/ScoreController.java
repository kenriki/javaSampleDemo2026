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

			// 2. MyBatis経由で全件取得を実行
			List<ScoreEntity> scores = mapper.findAll();

			// 3. データを JSON 文字列に変換
			String json = convertToJson(scores);

			// 4. 親クラス(BaseServlet)のメソッドでレスポンスを返す
			sendAsJson(response, json);

		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = msgProps.getProperty("error.db.connection", "データ取得に失敗しました。");
			System.err.println(errMsg);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errMsg);
		}
	}

	/**
	 * 登録・削除処理 (POST)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 隠しパラメータ等で処理を分岐（例: action=delete なら削除）
		String action = request.getParameter("action");

		try (SqlSession session = sqlSessionFactory.openSession()) {
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);

			if ("delete".equals(action)) {
				// --- 削除処理 ---
				int id = Integer.parseInt(request.getParameter("id"));
				mapper.delete(id); // MyBatis実行

			} else {
				// --- 登録処理 ---
				String subjectName = request.getParameter("subjectName");
				String evaluation = request.getParameter("evaluation");

				ScoreEntity score = new ScoreEntity();
				score.setSubjectName(subjectName);
				score.setEvaluation(evaluation);

				mapper.insert(score); // MyBatis実行
			}

			// 重要：更新系処理は必ずコミットする
			session.commit();
			response.setStatus(HttpServletResponse.SC_OK);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * DataTable用のJSON形式に変換するヘルパーメソッド
	 */
	private String convertToJson(List<ScoreEntity> scores) {
		StringBuilder sb = new StringBuilder("{\"data\": [");
		for (int i = 0; i < scores.size(); i++) {
			ScoreEntity s = scores.get(i);
			if (i > 0)
				sb.append(",");
			// 各行のデータを配列形式で構築
			sb.append(String.format("[\"%d\",\"%s\",\"%s\",\"%s\"]", s.getId(), s.getSubjectName(), s.getEvaluation(),
					s.getUpdateDate()));
		}
		sb.append("]}");
		return sb.toString();
	}
}