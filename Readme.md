# サーブレットの復習
> ノンフレームワークで生のDBデータをWEBページにレンダリングまで。

## ①　初期表示について
http://localhost:8080/Sample1App/jsp/sample1/index.jsp
> 初期表示について、JqueryのDataTablesライブラリのものを使って、DB結果をセットしています。
> 追加機能として、入力事項に対して、DataTablesに登録とマスタに反映ができます。

### JSPの値:
API_URL = "${pageContext.request.contextPath}/api/data";

<img src="src/main/webapp/img/img01.png" width="600">

## ② API結果の確認
http://localhost:8080/Sample1App/api/data
> DBから取得したものになります。

<img src="src/main/webapp/img/img02.png" width="600">

## ③ マスタへの反映確認

<img src="src/main/webapp/img/img03.png" width="600">

## ④ 削除するものを選択して「選択した行を削除」をクリックする。  

<img src="src/main/webapp/img/img04.png" width="600">  
<img src="src/main/webapp/img/img05.png" width="600">  
<img src="src/main/webapp/img/img06.png" width="600">  

## ⑤ 削除したあとの画面とマスタの確認  

<img src="src/main/webapp/img/img07.png" width="600">  
<img src="src/main/webapp/img/img08.png" width="600">  