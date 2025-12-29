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
}