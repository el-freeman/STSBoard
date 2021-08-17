
let idCheck = false;
let pwdCheck = false;


$(function(){
	$("#userid").on('keyup', idcheck);
	$("#userpwd").on('keyup', pwdcheck);
});


function idcheck(){
	let userid = $("#userid").val();
	if(userid.trim().length < 3 || userid.trim().length > 5) {	
		$("#idresult").css("color" , "red");
		$("#idresult").text("아이디는 3~5자로 입력해주세요");
		idCheck = false;
		return;
	}		
	$("#idresult").text("");
	$.ajax({
		url : 'idCheck'
		, method :'POST'
		, data : {"userid" : userid}
		, success : function(resp){
			if(resp =='success') {
				$("#idresult").css("color", "blue");
				$("#idresult").text("사용가능한 아이디입니다.");
				idCheck = true;
			}
			else {
				$("#idresult").css("color" , "red");
				$("#idresult").text("사용 불가능한 아이디입니다.");
				idCheck = false;
			}
		}
	});
}
function pwdcheck(){
	let userpwd = $("#userpwd").val();
	if(userpwd.trim().length < 3 || userpwd.trim().length > 5) {	
		$("#pwdresult").css("color" , "red");
		$("#pwdresult").text("비밀번호는 3~5자로 입력해주세요");
		pwdCheck = false;
		return;
	}
	pwdCheck = true;
	$("#pwdresult").text("");
}

function memberCheck(){
	if(idCheck && pwdCheck){
		alert("회원 가입 성공");
		return true;
	}
	else
	{
		alert("회원 가입 실패");
		return false;
	}	
}

function updateCheck(){
	if(pwdCheck){
		alert("회원 정보 수정 성공");
		return true;
	}
	else{
		alert("회원 정보 수정 실패");
		return false;
	}

}