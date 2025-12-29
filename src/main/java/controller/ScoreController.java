package controller;

import java.io.IOException;
import java.util.List;
import entity.Score;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ScoreService;
import servlet.BaseServlet;

public class ScoreController extends BaseServlet {

	private static final long serialVersionUID = -3191452012459400080L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// 1. Service からデータを取得
			ScoreService service = new ScoreService(dbProps);
			List<Score> scores = service.getAllScores();

			// 2. データを JSON 文字列に変換（ここは本来ライブラリを使うとより綺麗です）
			String json = convertToJson(scores);

			// 3. 親クラスのメソッドでレスポンスを返す
			sendAsJson(response, json);

		} catch (Exception e) {
			// プロパティファイルからエラーメッセージを取得してログに出す
			String errMsg = msgProps.getProperty("error.db.connection");
			System.err.println(errMsg);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errMsg);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. 画面からの入力値を取得
		String subjectName = request.getParameter("subjectName");
		String evaluation = request.getParameter("evaluation");

		// 2. Entity(Score)に詰める
		Score score = new Score();
		score.setSubjectName(subjectName);
		score.setEvaluation(evaluation);

		try {
			// 3. Serviceを実行
			ScoreService service = new ScoreService(dbProps);
			service.registerScore(score);

			// 4. 成功レスポンスを返す
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	// ヘルパー用：内部的な変換ロジック
	private String convertToJson(List<Score> scores) {
		StringBuilder sb = new StringBuilder("{\"data\": [");
		for (int i = 0; i < scores.size(); i++) {
			Score s = scores.get(i);
			if (i > 0)
				sb.append(",");
			sb.append(String.format("[\"%d\",\"%s\",\"%s\",\"%s\"]", s.getId(), s.getSubjectName(), s.getEvaluation(),
					s.getUpdateDate()));
		}
		sb.append("]}");
		return sb.toString();
	}
}