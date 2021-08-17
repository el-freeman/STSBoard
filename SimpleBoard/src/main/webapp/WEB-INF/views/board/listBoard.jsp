<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 글목록</title>
<style>
	body {
		font-family : sans-serif;
		font-size : 0.9em;
	}
	input[type=submit] {
		border : none;
	}
	div.outline{
		padding-left : 80px;
		padding-right: 80px;
	}
	
	div.wrapper{
		width : 900px;
		margin :0 auto;
		text-align : center;
	}
	table{
		width : 900px;
		border-collapse : collapse;
		clear : both;
	}
	
	th {
	/* background : #efefef; */
	border-collapse : collapse;
	border-bottom: 1px solid #444444;
   	padding: 10px;
	}
	
	td{
		border-bottom: 1px solid #dcdfdf;
    	padding: 5px;
	}
	
	.title {
		width : 450px;		
	}
	td.title{
		text-align : left;
	/* 	padding-left : 10px; */	
	}

	.write {
		text-align : right;
	}
	.home {
		text-align : left;
	}
	a {
		display : inline-block;
	}
	a, a:link, a:visited, a:active, a:hover {
		text-decoration:none;
		color : black;
	}
	a:hover {
		font-weight : bolder;
		text-decoration:underline;
		/* color : #f00; */
	}
	#search {
		text-align : right;
	}
	div.home p {
		width : 500px;
		float : left;
		margin : 0px;
	}
	form { 
		float : right:
	}
	img {
		width : 25px;
	}
</style>
<script src="resources/script/jquery-3.6.0.min.js"></script>
<script>
$(function() {
	if(window.performance.getEntriesByType("navigation")[0].type === "back_forward"){
		   location.reload(true);
		}
});

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

function detailBoard(num) {
	let targetPlace = 'detailboard?boardnum=' + num;
	location.href = targetPlace;
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
		<h2> 게시판 </h2>
		<div class="home">
			<p><a href="${pageContext.request.contextPath}/"><img src="resources/images/home.png"></a></p>
			<form id="search" action="listboard" method="GET">
				<select name="searchItem">
					<option value="title" ${searchItem == 'title'?'selected' : '' } >제목</option>
					<option value="userid" ${searchItem == 'userid'?'selected' : '' }>작성자</option>
					<option value="text" ${searchItem == 'text'?'selected' : '' }>글내용</option>
				</select>
				<input type="text" name="searchWord" value="${searchWord}">
				<input class="btn" type="submit" value="검색">
			</form>
		</div>
		
		<!-- 게시글 목록 시작 -->
		<table>
			<tr>
				<th>번호</th>
				<th class="title">제목</th>			
				<th>작성일</th>			
				<th>작성자</th>			
				<th>조회수</th>			
			</tr>
			
			<!-- 게시글 출력 -->
			<c:forEach var="board" items="${list}" varStatus="stat">		
				<tr>
					<td>${board.boardnum} <%-- / ${stat.count } --%></td>
					<td class="title">
						<c:if test= "${board.originalfile != null }">
							<img src="resources/images/clip.png" style= "width:15px">
						</c:if>
							<a href="javascript:detailBoard(${board.boardnum});">${board.title}</a>
						<c:if test="${board.replycount != 0}" >
							<a href="javascript:detailBoard(${board.boardnum});" style="font-size:0.9em; color:red">[${board.replycount}]</a>						
						</c:if>						
					</td>
					<td>${board.regdate}</td>
					<td>${board.userid}</td>
					<td>${board.hitcount }</td>
				</tr>
			</c:forEach>
		</table>
		
		<!--  글쓰기 버튼 -->
		<%-- <c:if test="${sessionScope.loginId != null}"> --%>
			<p></p>
			<div class="write"><a href="writeboard">글쓰기</a></div>
		<%-- </c:if> --%>
		
		<%-- <p>글 개수 : ${totalRecordCount}</p> --%>
		<%-- <p>현재 페이지 : ${currentPage}</p> --%>
		<br>
		<!-- 페이징 출력되는 부분 -->
		<div class="navigator">
		<a href="listboard?currentPage=1&searchItem=${searchItem}&searchWord=${searchWord}">
			처음
			</a>&nbsp;		
			<a href="listboard?currentPage=${navi.startPageGroup-1}&searchItem=${searchItem}&searchWord=${searchWord}">이전</a>&nbsp;&nbsp;
			<c:forEach var="page" begin="${navi.startPageGroup}" end="${navi.endPageGroup}">
				<c:if test="${navi.currentPage == page}">
					<span style="color:blue;font-weight:bolder;font-size:1.3em">${page}</span> &nbsp;
				</c:if>
				
				<c:if test="${navi.currentPage != page}">
					<a href="listboard?currentPage=${page}&searchItem=${searchItem}&searchWord=${searchWord}">${page}</a> &nbsp;
				</c:if>	
			</c:forEach>
			<a href="listboard?currentPage=${navi.endPageGroup+1}&searchItem=${searchItem}&searchWord=${searchWord}">
			다음
			</a>
			&nbsp;			
			<a href="listboard?currentPage=${navi.totalPageCount}&searchItem=${searchItem}&searchWord=${searchWord}">
			끝
			</a>
		</div>
		<br><br>
	</div>
</div>
</body>
</html>








