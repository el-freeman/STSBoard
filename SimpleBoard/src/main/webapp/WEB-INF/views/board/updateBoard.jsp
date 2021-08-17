<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글수정</title>
<link rel="stylesheet" href="resources/style/detail.css?ver=2">
<script>
function formCheck() {
	let title = document.getElementById("title");
	let text  = document.getElementById("text");
	
	if(title.value.trim().length == 0 || text.value.trim().length == 0) {
		alert('제목과 글 내용을 입력해 주세요');
		return false;
	}
	return true;
}

function detailBoard() {
	let boardnum = ${board.boardnum};
	let target = '${pageContext.request.contextPath}';
	location.href = target + '/detailboard?boardnum='+boardnum;
}

</script>
</head>
<body>
<div class="wrapper">
	<h2> 글수정 </h2>
	<form action="updateboard" method="POST" enctype="multipart/form-data">
		<input type="hidden" name="userid" value="${sessionScope.loginId}">
		<input type="hidden" name="boardnum" value="${board.boardnum}">
		
		<table>		
			<tr>
				<td colspan="2" style="font-weight : bolder"><input type="text" name="title" id="title" value = "${board.title}"></td>
			</tr>	
			<tr>
				<td>${sessionScope.loginName}</td>
				<td>${board.regdate}</td>
			</tr>	
			
			<tr>
				<td colspan="2">
					<textarea rows="15" cols="70" name="text" id="text">${board.text}</textarea>
				</td>
			</tr>
				<tr>		
				<td colspan="2">
					${board.originalfile}<br>
					<input type="file" name="upload" >
				</td>
			</tr>
			<tr>
				<th class="btn" colspan="2">
					<input type="button" value="취소" onclick="detailBoard();">
					<input type="submit" value="글수정" onclick="return formCheck();">
				</th>
			</tr>
		</table>
	</form>
</div>
</body>
</html>