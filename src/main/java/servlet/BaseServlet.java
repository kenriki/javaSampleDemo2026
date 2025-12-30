package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.ScoreMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

/**
 * すべてのサーブレットの親となる抽象クラス。 共通の初期化処理（MyBatisやメッセージ設定）および管理者認証、一括削除処理を一括管理する。
 */
public abstract class BaseServlet extends HttpServlet {

	// MyBatisの心臓部。SQL実行の窓口となる工場。
	protected static SqlSessionFactory sqlSessionFactory;

	// 画面に表示するメッセージなどを管理するプロパティ
	protected Properties msgProps = new Properties();

	/**
	 * サーブレット起動時に一度だけ実行される初期化メソッド。
	 */
	@Override
	public void init() throws ServletException {
		if (sqlSessionFactory == null) {
			String resource = "mybatis-config.xml";
			try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource)) {
				if (inputStream != null) {
					sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
					System.out.println("DEBUG: MyBatisの初期化に成功しました。");
				} else {
					throw new ServletException(resource + " が見つかりません。");
				}
			} catch (Exception e) {
				throw new ServletException("MyBatisのビルドに失敗しました。", e);
			}
		}
		loadConfig("messages.properties", msgProps);
	}

	/**
	 * 入力されたパスワードが管理者テーブルのものと一致するかチェックする。 * @param inputPassword 画面から入力されたパスワード文字列
	 * 
	 * @return 一致すればtrue、不一致またはエラー時はfalse
	 */
	protected boolean checkAdminPassword(String inputPassword) {
		if (inputPassword == null || inputPassword.isEmpty()) {
			return false;
		}

		try (SqlSession session = sqlSessionFactory.openSession()) {
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);
			// DBから管理者の正しいパスワードを取得して照合
			String dbPassword = mapper.getAdminPassword();
			return dbPassword != null && dbPassword.equals(inputPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * データベースのテーブルを全件削除し、ID（シーケンス）をリセットする。
	 * このメソッドは事前にcheckAdminPasswordで認証済みであることを前提とする。
	 */
	protected void executeTruncate() {
		try (SqlSession session = sqlSessionFactory.openSession(true)) { // trueでオートコミット有効
			ScoreMapper mapper = session.getMapper(ScoreMapper.class);
			// TRUNCATE TABLE ... RESTART IDENTITY を実行
			mapper.truncateTable();
			System.out.println("DEBUG: テーブルの全件リセットが完了しました。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定されたプロパティファイルを読み込む汎用メソッド。
	 */
	private void loadConfig(String fileName, Properties props) throws ServletException {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
			if (is != null) {
				props.load(is);
			} else {
				System.out.println("Warning: " + fileName + " not found.");
			}
		} catch (IOException e) {
			throw new ServletException(fileName + " の読み込みに失敗しました", e);
		}
	}

	/**
	 * Javaオブジェクト（主にJSON文字列）をブラウザへ送信する共通メソッド。
	 */
	protected void sendAsJson(HttpServletResponse response, String jsonContent) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonContent);
		out.flush();
	}
}