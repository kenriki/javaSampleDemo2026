package service;

import org.apache.ibatis.session.SqlSession;
import dao.ScoreMapper;
import entity.UserEntity;

public class UserService {

	/**
	 * ログイン認証
	 */
	public UserEntity login(SqlSession session, String username, String password) {
		if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			return null;
		}

		ScoreMapper mapper = session.getMapper(ScoreMapper.class);
		return mapper.authenticateUser(username, password);
	}

	/**
	 * ユーザー名の重複チェック
	 */
	public boolean isUsernameTaken(SqlSession session, String username) {
		ScoreMapper mapper = session.getMapper(ScoreMapper.class);
		UserEntity user = mapper.findUserByUsername(username);
		return user != null;
	}

	/**
	 * ユーザー登録
	 */
	public boolean registerUser(SqlSession session, UserEntity user) {
		try {
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);
			int result = mapper.insertUser(user);
			if (result > 0) {
				session.commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ユーザー情報取得
	 */
	public UserEntity getUserByUsername(SqlSession session, String username) {
		ScoreMapper mapper = session.getMapper(ScoreMapper.class);
		return mapper.findUserByUsername(username);
	}

	/**
	 * ユーザーID検索
	 */
	public UserEntity getUserById(SqlSession session, int userId) {
		ScoreMapper mapper = session.getMapper(ScoreMapper.class);
		return mapper.findUserById(userId);
	}

	/**
	 * パスワード更新
	 */
	public boolean updatePassword(SqlSession session, int userId, String newPassword) {
		try {
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);
			int result = mapper.updatePassword(userId, newPassword);
			if (result > 0) {
				session.commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
			return false;
		}
	}
}