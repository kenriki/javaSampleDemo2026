$(document).ready(function() {
	// 1. 最初に変数を定義し、DataTablesを初期化して代入する
	// これを $(document).ready の一番上に書くことで、この中にある全ての処理で table が使えます
	const table = $('#mainTable').DataTable({
		"ajax": API_URL,
		"language": {
			"sEmptyTable": "データは登録されていません",
			"sInfo": "_TOTAL_ 件中 _START_ から _END_ まで表示",
			"sLengthMenu": "_MENU_ 件表示",
			"sSearch": "絞り込み検索:",
			"oPaginate": {
				"sFirst": "先頭", "sLast": "最終", "sNext": "次へ", "sPrevious": "前へ"
			},
			"select": {
				"rows": "（%d 行選択中）"
			}
		},
		"select": true,
		"columnDefs": [
			{ "targets": 0, "visible": true }
		]
	});

	// 2. 追加登録ボタンの処理（この波括弧 { } の中にあるので table が参照できます）
	$('#btnAdd').on('click', function() {
		const data = {
			subjectName: $('#subjectName').val(),
			evaluation: $('#evaluation').val()
		};

		$.ajax({
			url: API_URL,
			type: 'POST',
			data: data,
			success: function() {
				alert('登録が完了しました！');
				$('#subjectName').val('');
				table.ajax.reload(); // 正常に動作します
			}
		});
	});

	// 3. 削除ボタンの処理（ここも $(document).ready の内側に移動しました）
	// これにより、image_a5e86c.png のような「table is not defined」エラーを回避します
	$('#btnDeleteSelected').on('click', function() {
		const selectedRows = table.rows({ selected: true });
		const selectedData = selectedRows.data();

		if (selectedData.length === 0) {
			alert("削除する行を選択してください");
			return;
		}

		if (confirm(selectedData.length + " データを削除しますか？")) {
			// 現在の仕様に合わせて1件目のIDを取得
			const id = selectedData[0][0];

			$.ajax({
				url: API_URL,
				type: 'POST',
				data: {
					action: 'delete',
					id: id
				},
				success: function() {
					alert('削除しました');
					table.ajax.reload(); // 正常に動作します
				},
				error: function() {
					alert('削除に失敗しました。');
				}
			});
		}
	});
});


// 全件削除ボタンのクリックイベント
$('#allDeleteButton').on('click', function() {
	const password = prompt("【警告】全データを削除し、No.1からリセットします。\n管理者パスワードを入力してください：");

	if (password) {
		if (confirm("本当にすべてのデータを削除してよろしいですか？この操作は取り消せません。")) {
			$.ajax({
				url: '/Sample1App/api/score', // サーブレットのURL
				type: 'POST',
				data: {
					action: 'truncate',
					password: password
				},
				success: function() {
					alert("すべてのデータを削除し、IDをリセットしました。");
					table.ajax.reload(); // 画面を更新（0件になる）
				},
				error: function(xhr) {
					alert(xhr.responseText); // 「パスワードが違います」等を表示
				}
			});
		}
	}
});