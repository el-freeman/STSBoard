<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댓글 연습</title>
<style>
	input#update {
		display:none;
	}
	div.wrapper {
		width : 1000px;
		margin : 0 auto;
		text-align : center;
	}
	table {
		width : 900px;
		margin : 0 auto;
		border-collapse : collapse;
	}
	td {
		border-bottom : 1px solid black;
		border-top : 1px solid black
	}
	.replynum  { width : 50px; }
	.userid    { width : 100px; }
	.replytext { width : 400px; text-align:left; }
	.regdate   { width : 150px; }
	
</style>
<script src="resources/script/jquery-3.6.0.min.js"></script>
<script>
$(function(){
	init();
	$("#insert").on('click', insert);
});

// 데이터 초기화
function init() {
	$.ajax({
		url : "selectAll"
		, method  : "GET"
		, success : output
	});
}

function output(resp) {
	let result = '';
	
	if(resp.length == 0) {
		$("#replyTarget").html("<b>댓글이 없습니다.</b>");
	} else {
		result += '<table>';
		
		$.each(resp, function(index, item){
			result += '<tr>'
			result += '	<td class="replynum">'+ item.replynum +'</td>'
			result += '	<td class="userid">'+ item.userid +'</td>'
			result += '	<td class="replytext">'+ item.replytext +'</td>'
			result += '	<td class="regdate">'+ item.regdate +'</td>'
			result += '	<td>'
			result += '		<input type="button" class="delbtn"    data-num="'+ item.replynum +'"  value="삭제">'
			result += '		<input type="button" class="updatebtn" data-num="'+ item.replynum +'"  value="수정">'
			result += '	</td>'
			result += '</tr>'
			
		});
		
		result += '</table>';
		$("#replyTarget").html(result);
		$(".delbtn").on('click', deleteReply);
		$(".updatebtn").on('click', updateReply);
	}
}

// 댓글 삭제
function deleteReply() {
	let delno = $(this).attr('data-num');

	$.ajax({
		url : 'delete'
		, method : 'GET'
		, data :{ "replynum" : delno }  // "replynum="+delno
		, success : function(resp) {
			alert(resp);
			init();
		}
	});
}

//댓글 수정
function updateReply() {
	alert("수정");
}

// 댓글 저장
function insert() {
	let userid    = $("#userid").val();
	let replytext = $("#replytext").val();

	if(userid.trim().length == 0 || replytext.trim().length == 0) {
		alert("댓글 내용과 아이디를 입력해주세요");
		return;
	}
	let replyData = {"userid":userid, "replytext":replytext};
	
	$.ajax({
		url : 'insert'
		, method : 'POST'
		, data : replyData
		, success : function() {
			alert("댓글 등록이 완료되었습니다.");
			$("#userid").val("");
			$("#replytext").val("");
			init();
		}
	})
}
</script>
</head>
<body>
<div class="wrapper">
	<h2>[ 댓글 연습 ]</h2>
	<!-- 입력공간 -->
	<div>
		<label>이름 : <input type="text" id="userid" name="userid"></label>
		<label>내용 : <input type="text" id="replytext" name="replytext"></label>
		<input type="button" id="insert" value="댓글 저장">
		<input type="button" id="update" value="댓글 수정">
	</div>
	<br>
	<!-- 출력공간 -->
	<div id="replyUpdateTarget">
	</div>
	<div id="replyTarget">
	</div>
</div>	
</body>
</html>