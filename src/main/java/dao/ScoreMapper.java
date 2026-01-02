package dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import entity.ScoreEntity;
import entity.UserEntity;

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

	// ========== ユーザー認証関連メソッド ==========

	/**
	 * ユーザー名でユーザーを検索
	 * 
	 * @param username 検索するユーザー名
	 * @return ユーザー情報、存在しない場合はnull
	 */
	UserEntity findUserByUsername(@Param("username") String username);

	/**
	 * ユーザー名とパスワードで認証
	 * 
	 * @param username ユーザー名
	 * @param password パスワード
	 * @return 認証成功時はユーザー情報、失敗時はnull
	 */
	UserEntity authenticateUser(@Param("username") String username, @Param("password") String password);

	/**
	 * 新規ユーザー登録
	 * 
	 * @param user 登録するユーザー情報
	 * @return 登録件数
	 */
	int insertUser(UserEntity user);

	/**
	 * ユーザー情報更新
	 * 
	 * @param user 更新するユーザー情報
	 * @return 更新件数
	 */
	int updateUser(UserEntity user);

	/**
	 * ユーザーIDで検索
	 * 
	 * @param userId ユーザーID
	 * @return ユーザー情報、存在しない場合はnull
	 */
	UserEntity findUserById(@Param("userId") int userId);

	/**
	 * ユーザー削除(物理削除)
	 * 
	 * @param userId 削除するユーザーID
	 * @return 削除件数
	 */
	int deleteUser(@Param("userId") int userId);

	/**
	 * 全ユーザー取得
	 * 
	 * @return 全ユーザーのリスト
	 */
	List<UserEntity> findAllUsers();

	/**
	 * ユーザー数をカウント
	 * 
	 * @return 登録されているユーザー数
	 */
	int countUsers();

	/**
	 * メールアドレスで検索
	 * 
	 * @param email 検索するメールアドレス
	 * @return ユーザー情報、存在しない場合はnull
	 */
	UserEntity findUserByEmail(@Param("email") String email);

	/**
	 * パスワード更新
	 * 
	 * @param userId   ユーザーID
	 * @param password 新しいパスワード
	 * @return 更新件数
	 */
	int updatePassword(@Param("userId") int userId, @Param("password") String password);
}