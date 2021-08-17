<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 자세히 보기</title>
<link rel="stylesheet" href="resources/style/detail.css?ver=3">
<style>
	
</style>
<script src="resources/script/jquery-3.6.0.min.js"></script>
<script src="resources/script/replyScript.js?ver=1"></script>

<script>
function logout(){
	if('${sessionScope.loginId}' == null){
		alert("로그인해주세요");
		return;
	}
	else{
		if(confirm("로그아웃 하시겠습니까")){
			let targetPlace = "logout";
			location.href = targetPlace;
			alert("로그아웃 되었습니다.")
		}
		else{
			return;
		}
	}	
}

function boardList() {
	let targetPlace = "listboard";
	location.href = targetPlace;
}
function boardDelete() {
	let boardnum = '${board.boardnum}';
	let userid = '${board.userid}';
	let loginId = $("#userid").val();
	if(userid == loginId){
		let answer = confirm('게시글을 삭제하시겠습니까?');
		
		if(answer) {
			location.href="deleteboard?boardnum=" + boardnum;
		}
	}
	else{
		alert("삭제 권한이 없습니다.");
	}
	
}
function boardUpdate() {
	let boardnum = '${board.boardnum}';
	let userid = '${board.userid}';
	let loginId = $("#userid").val();
	if(userid == loginId){
		location.href="updateboard?boardnum=" + boardnum;
	}
	else {
		alert("수정 권한이 없습니다.");
	}	
}
</script>
</head>
<body>
<div class="outline">
	<div class="wrapper">
		<p align = "right">
			<c:if test="${sessionScope.loginId == null}">		
				<a href="login">로그인</a>
			</c:if>		
			<c:if test="${sessionScope.loginId != null}">
				${sessionScope.loginName}님, 반갑습니다.
				<a href="javascript:logout();">로그아웃</a>
				<a href="update">개인정보 수정</a>
			</c:if>
		</p>
		<h2> 게시판 글보기 </h2>
		<input type="text" id="boardnum" name="boardnum" value="${board.boardnum}" hidden>
		<table id="contentstable">
			<tr>
				<td colspan="2" style="font-weight : bolder">${board.title}</td>
			</tr>
			<tr>				
				<td style="width : 10%">${board.userid}</td>
				<td>${board.regdate}</td>
			</tr>						
			<tr>
				<td colspan="2">
					<pre>${board.text}</pre>
				</td>
			</tr>
			<tr>
				<td >첨부파일</td>
				<td>
					<c:if test="${board.originalfile != null}">
						<a href="download?boardnum=${board.boardnum}">${board.originalfile}</a>
					</c:if>
				</td>				
			</tr>
			<tr><td colspan="2" style="border-bottom: none"></tr>
			<tr>
				<th class="btn" style="text-align:left">
					<input type="button" value="글목록" onclick="boardList();">
				</th>
				<th class="btn" style="text-align:right">
					<!-- 글삭제와 수정은 로그인한 사람과 해당글을 쓴 사람과 같아야 할 수 있다. -->
					<input type="button" value="글삭제" onclick="boardDelete();">
					<input type="button" value="글수정" onclick="boardUpdate();">				
				</th>
			</tr>
		</table>	
		<br>
		<!-- 댓글 입력 : 로그인한 사람만 보여야 함  -->
		<c:if test="${sessionScope.loginId != null }">
			<form action="" method="POST">
				<table id="replyInput" class="reply">
					<tr>
						<td>
							<input type="text" id="userid" name="userid" value="${sessionScope.loginId}" readonly style="background-color: #f7f7f7; width:150px">			
							<input id="replytext"    type="text" name="replytext">
							<input id="insert0" type="button" value="댓글 저장">
						</td>
					</tr>
				</table>
			</form>
		</c:if>
		<br>
		<div id="replyTarget">
		</div>
		
		<!-- 댓글 출력 -->
	</div>
	<br><br>
</div>
</body>
</html>











