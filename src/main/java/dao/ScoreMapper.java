package dao;

import java.util.List;
import entity.ScoreEntity;

public interface ScoreMapper {

	/**
	 * 削除フラグが立っていないすべてのスコア情報を取得する
	 */
	List<ScoreEntity> findAll();

	/**
	 * 新しいスコア情報を登録する
	 */
	void insert(ScoreEntity score);

	/**
	 * 指定されたIDのデータの削除フラグを 'D' に更新する（論理削除） ※XML側でUPDATE文として定義
	 */
	void delete(int id);

	/**
	 * 管理者テーブルからパスワードを取得する
	 * 
	 * @return 管理者のパスワード文字列
	 */
	String getAdminPassword();

	/**
	 * テーブルの全データを物理削除し、ID(連番)をリセットする SQL: TRUNCATE TABLE ... RESTART IDENTITY
	 */
	void truncateTable();
}