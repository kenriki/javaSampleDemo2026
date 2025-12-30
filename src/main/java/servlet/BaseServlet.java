package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

/**
 * すべてのサーブレットの親となる抽象クラス。 共通の初期化処理（MyBatisやメッセージ設定）をここで一括管理する。
 */
public abstract class BaseServlet extends HttpServlet {

	// MyBatisの心臓部。SQL実行の窓口となる工場。
	// staticにすることで、アプリ全体で1つだけ作成して使い回す。
	protected static SqlSessionFactory sqlSessionFactory;

	// 画面に表示するメッセージなどを管理するプロパティ（これまで通り）
	protected Properties msgProps = new Properties();

	/**
	 * サーブレット起動時に一度だけ実行される初期化メソッド。
	 */
	@Override
	public void init() throws ServletException {
		// --- 1. MyBatisの初期化 (SqlSessionFactoryの作成) ---
		// まだMyBatis設定ファイルが作られていない場合のみ作成する
		if (sqlSessionFactory == null) {
			String resource = "mybatis-config.xml";

			// 重要：クラスローダーを使用してファイルを読み込む。
			// これにより、WARとしてデプロイした際や外部Tomcatでも確実に見つけられる
			try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource)) {
				if (inputStream != null) {
					// 設定ファイルを元にMyBatisの設定
					sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
					System.out.println("DEBUG: MyBatisの初期化に成功しました。");
				} else {
					// ファイル自体が見つからない場合のエラー
					throw new ServletException(resource + " が見つかりません。src/main/resourcesの直下にありますか？");
				}
			} catch (IOException e) {
				// ファイル読み込み失敗時
				throw new ServletException("MyBatisの設定ファイル読み込み中にエラーが発生しました", e);
			} catch (Exception e) {
				// XMLの中身（パスワード間違いなど）に問題がある場合
				throw new ServletException("MyBatisのビルドに失敗しました。XMLの内容を確認してください", e);
			}
		}

		// --- 2. メッセージ情報の読み込み ---
		// messages.propertiesを読み込む（以前の仕組みを継続）
		loadConfig("messages.properties", msgProps);
	}

	/**
	 * 指定されたプロパティファイルを読み込む汎用メソッド。
	 *
	 * @param fileName ファイル名
	 * @param props    格納先のPropertiesオブジェクト
	 */
	private void loadConfig(String fileName, Properties props) throws ServletException {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
			if (is != null) {
				props.load(is);
			} else {
				// ファイルがなくても停止はさせない（警告のみ）
				System.out.println("Warning: " + fileName + " not found.");
			}
		} catch (IOException e) {
			throw new ServletException(fileName + " の読み込みに失敗しました", e);
		}
	}

	/**
	 * Javaオブジェクト（主にJSON文字列）をブラウザへ送信する共通メソッド。
	 *
	 * @param response    レスポンスオブジェクト
	 * @param jsonContent 送信するJSON文字列
	 */
	protected void sendAsJson(HttpServletResponse response, String jsonContent) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonContent);
		out.flush();
	}
}