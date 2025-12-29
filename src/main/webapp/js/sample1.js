$(document).ready(function() {
	$('#mainTable').DataTable({
		"ajax": API_URL, // JSPで定義した変数を使用
		"language": {
			"sEmptyTable": "データは登録されていません",
			"sInfo": "_TOTAL_ 件中 _START_ から _END_ まで表示",
			"sLengthMenu": "_MENU_ 件表示",
			"sSearch": "絞り込み検索:",
			"oPaginate": {
				"sFirst": "先頭",
				"sLast": "最終",
				"sNext": "次へ",
				"sPrevious": "前へ"
			}
		},
		// 列ごとの微調整（例：ID列の幅を固定）
		"columnDefs": [
			{ "width": "10%", "targets": 0 }
		]
	});
});