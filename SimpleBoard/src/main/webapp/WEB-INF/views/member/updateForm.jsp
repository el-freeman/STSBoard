<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>
<link rel="stylesheet" href="resources/style/update.css?ver=3">
<script src="resources/script/jquery-3.6.0.min.js"></script>
<script src="resources/script/memberValidation2.js"></script>
</head>
<body>
<div class="wrapper">
	<h2> 회원 정보 수정 </h2>
	<form id="update" action="update" method="POST">
		<table>
			<tr>
				<th>아이디</th>
				<td>
					<input type="text" id="userid" name="userid" value="${member.userid}" readonly style="background-color: #f7f7f7">
				</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td>
					<input type="password" id="userpwd" name="userpwd">
					<span id="pwdresult"></span>
				</td>
			</tr>
			<tr>
				<th>이름</th>
				<td>
					<input type="text" id="username" name ="username" value="${member.username}" disabled>
				</td>
			</tr>
			<tr>
				<th>Email</th>
				<td>
					<input type="email" id="email" name="email" value="${member.email}">
				</td>
			</tr>
			<tr>
				<th>생년월일</th>
				<td>
					<input type="date" id="birth" name="birth" value = "${member.birth}">
				</td>
			</tr>
			<tr>
				<th>성별</th>
				<td>
					<input type="radio" name="gender" value="남자" ${member.gender=='남자' ? 'checked' : ''} disabled>남자
					<input type="radio" name="gender" value="여자" ${member.gender=='여자' ? 'checked' : ''} disabled>여자
				</td>
			</tr>
			<tr>
				<th>전화번호</th>
				<td>			
					<select id="phone1" >
						<c:set var = "phone0" value = "${member.phone}"/>						  
						<option value="010" ${fn:substring(phone0,0,3) == '010' ? 'selected' : '' }>010</option>
						<option value="011" ${fn:substring(phone0,0,3) == '011' ? 'selected' : '' }>011</option>
						<option value="019" ${fn:substring(phone0,0,3) == '019' ? 'selected' : '' }>019</option>
						<option value="018" ${fn:substring(phone0,0,3) == '018' ? 'selected' : '' }>018</option>
					</select>
					<input type="text" id="phone2" placeholder= "${fn:substring(phone0,3,11)}">
					<input type="hidden" id="phone" name="phone" value="" >
				</td>				
			</tr>
			<tr>
				<th colspan="2">
					<input type="submit" value="회원정보수정" onclick="return updateCheck();" >
					<input type="reset"  value="지우기">
				</th>
			</tr>
		</table>
	</form>
</div>
</body>
</html>