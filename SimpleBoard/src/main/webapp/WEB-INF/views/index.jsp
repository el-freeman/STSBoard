<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원게시판</title>
<link rel="stylesheet" href="resources/style/detail.css?ver=2">
<style>
body{
	font-size : 1em;
}
a:hover {
	font-weight : bolder;
	text-decoration:underline;
	/* color : #f00; */
}
</style>
<script src="resources/script/logoutCheck.js?ver=1"></script>
<script>

function deleteMember(){
	if('${sessionScope.loginId}' == null){
		alert("로그인해주세요");
		return;
	}
	else{
		if(confirm("정말 탈퇴하시겠습니까")){
			let targetPlace = "deletemember";
			location.href = targetPlace;
			alert("탈퇴 되었습니다.")
		}
		else{
			return;
		}
	}	
}

</script>
</head>
<body>
<div class="wrapper">
<h2> 회원 게시판 </h2>
	<ul>
		<c:if test="${sessionScope.loginId == null}">
			<li><a href="join">회원가입</a></li>
			<li><a href="login">로그인</a></li>
		</c:if>	
		
		<c:if test="${sessionScope.loginId != null}">
			<li>
				${sessionScope.loginName}님, 
				<a href="javascript:logout();">로그아웃</a>
			</li>
			<li><a href="update">개인정보 수정</a></li>
			<li><a href="javascript:deleteMember();">회원 탈퇴</a></li>
		</c:if>
		
		<li><a href="listboard">게시판</a></li>
	</ul>
</div>
</body>
</html>