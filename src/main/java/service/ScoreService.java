package service;

import java.util.*;
import dao.ScoreDAO;
import entity.Score;

public class ScoreService {
	private ScoreDAO dao;

	public ScoreService(Properties props) {
		this.dao = new ScoreDAO(props);
	}

	public List<Score> getAllScores() throws Exception {
		// ここで「判定Aだけ色を変える」などの加工ロジックを挟むことも可能
		return dao.findAll();
	}

	public void registerScore(Score score) throws Exception {
		// ここでバリデーション（空文字チェック等）を入れるのがServiceの役割です
		if (score.getSubjectName() == null || score.getSubjectName().isEmpty()) {
			throw new Exception("学習項目が空です");
		}
		dao.insert(score);
	}
}