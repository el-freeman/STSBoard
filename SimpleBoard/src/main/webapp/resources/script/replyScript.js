$(function() {
	init();
	$("#insert0").on('click', function(){
		insert(0);
	});
});

let selectedreplynum = -1; //대댓글창 여닫을때 쓰는 플래그
// 데이터 초기화
function init() {
	let boardnum = document.getElementById("boardnum").value;
	//let boardnum = $("#boardnum").val();
	$.ajax({
		url: "selectAll"
		, method: "GET"
		, data : {"boardnum":boardnum}
		, async : false
		, success: output
	});
	selectedreplynum = -1;
}
function output(resp) {
	let result = '';
	let replycount = 0;
	
	$.each(resp, function(index, item) {	
			result += '<table'
			if(item.referencenum > 0){
				result += ' style="background-color:#f4f4f4"'
			}			
			result +='	><tr class="replytr" id="' + item.replynum + '">'						
			if(item.isdeleted != 1){
				replycount++;	
				if(item.referencenum > 0){
				result += '	<td class="blank" style="background-color:#ffffff">'+'</td>'
				}	
				result += '	<td class="userid">' + item.userid + '</td>'	
			}					
			result += '	<td class="replytext">'	
			if(item.referencenum == 0 && item.isdeleted == 0){
				result += '		<a href="javascript:openReplyInsert('+ item.replynum + ');" >' 
				result +=  		 item.replytext + '</a>'
			}
			else{
				result +=  		 item.replytext
			}
			if(item.isdeleted != 1){
				result += '	</td>'			
				result += '	<td class="regdate">' + item.regdate + '</td>'
				result += '	<td>'
				result += '		<input type="button" class="delbtn"    data-num="' + item.replynum + '"  value="삭제">'
				result += '		<input type="button" class="updatebtn" data-num="' + item.replynum + '"  value="수정">'
				result += '	</td>'	
			}		
			else{
				result += '<td></td><td></td>'
			}	
			result += '</tr>'
			result += '<tr class="replyUpdateTarget"></tr>'				
	});
	result += '</table>';
	let temp = $("#replyTarget");
	//let temp = document.getElementById("replyTarget");
	if(replycount == 0){
		//temp.innerHTML = "<b>댓글이 없습니다.</b>";
		temp.html("<b>댓글이 없습니다.</b>");
		//$("#replyTarget").html("<b>댓글이 없습니다.</b>");
	}
	else{
		//temp.innerHTML = result;
		temp.html(result);
		//$("#replyTarget").html(result);
		$(".delbtn").on('click', deleteReply);
		$(".updatebtn").on('click', updateReply);
	}	
}
// 댓글 삭제
function deleteReply() {
	let delno = $(this).attr('data-num');
	let flag = true;
	$.ajax({
		url: 'selectone'
		, method: 'GET'
		, data: { "replynum": delno }
		, success: function(resp) {

			let userid = $("#userid").val();
			if (userid == resp.userid) {
				if(confirm("정말 삭제하시겠습니까?")){					
					let boardnum = resp.boardnum
					$.ajax({
						url:'selectAll'
						, method : 'GET'
						, data : {"boardnum": boardnum}
						, success : function(resp){
							
							$.each(resp, function(index, item){								
								if(item.referencenum == delno ){
									let replyData = {"replynum": delno, "boardnum":boardnum,  "replytext": "삭제된 댓글입니다.", "isdeleted": 1 };
									$.ajax({
										url: 'updatereply'
										, method: 'POST'
										, async:false
										, data: replyData
										, success: function() {
											flag = false;
											alert("댓글이 삭제되었습니다.");
											$("#replytext").val("");
											init();											
										}
									});
									return false;
								}
							})		
							if(flag == true){
								$.ajax({
								url: 'delete'
								, method: 'GET'
								, async:false
								, data: { "replynum": delno }  // "replynum="+delno
								, success: function(resp) {
									alert(resp);
									init();
									}
								});						
							}																
						}												
					});							
				}			
			}
			else{
				alert("삭제 권한이 없습니다.");
			}
		}
	});
}

//댓글 수정 창 오픈
function updateReply() {
	let replynum = $(this).attr('data-num');	
		
	init();
	let userid = document.getElementById('userid').value;
	
	$.ajax({
		url: 'selectone'
		, method: 'GET'
		, data: { "replynum": replynum }
		, async : false
		, success: function(resp) {		
			//let userid = $("#userid").val();
			if (userid == resp.userid) {				
				let result = '<table>';
				result += '<tr>'
				/*result += '	<td class="replynum">'+resp.replynum +'</td>';*/
				result += '	<td class="userid">' + resp.userid +  '</td>'
				result += '	<td class="replytext">'
				result += '		<input type="text" id=updatereplytext placeholder="'+resp.replytext+'">'
				result += '</td>'
				result += '	<td class="regdate">' + resp.regdate +  '</td>'
				result += '	<td>'
				result += '		<input type="button" class="cancelbtn"    data-num="' + resp.replynum +  '"  value="취소">'
				result += '		<input type="button" class="updateconfirmbtn" data-num="' + resp.replynum +  '"  value="수정">'
				result += '	</td>'
				result += '</tr>'
				result += '</table>';
				let temp = $('#' + replynum); 				
				temp.hide(1);
				temp.next().html(result);
				temp.next().css({ "font-weight":  "bold" });	
				$(".cancelbtn").on('click', init);
				$(".updateconfirmbtn").on('click', updateConfirm);
			}
			else {
				alert("수정 권한이 없습니다.");
			}
		}
	});	
}

function updateConfirm(){
	let replynum = $(this).attr('data-num');
	let replytext = $("#updatereplytext").val() || '';

	if (replytext.trim().length == 0) {
		alert("댓글내용을 입력해주세요");
		return;
	}
	let replyData = {"replynum": replynum, "replytext": replytext };
	$.ajax({
		url: 'updatereply'
		, method: 'POST'
		, data: replyData
		, success: function() {
			alert("댓글 수정이 완료되었습니다.");
			//$("#userid").val("");
			$("#replytext").val("");
			init();
		}
	})	
}
// 댓글 저장
function insert(num) {
	let userid = $("#userid").val();
	let replytext = "";
	let boardnum = $("#boardnum").val();
	let referencenum = num;
	if(num!=0){
		replytext = $("#nestedreplytext").val() || '' ;
	}
	else{
		replytext = $("#replytext").val() ;
	}
	if (replytext.trim().length == 0) {
		alert("댓글내용을 입력해주세요");
		return;
	}
	let replyData = { "boardnum": boardnum, "userid": userid, "replytext": replytext, "referencenum":referencenum };

	$.ajax({
		url: 'insert'
		, method: 'POST'
		, data: replyData
		, success: function() {
			alert("댓글 등록이 완료되었습니다.");
			//$("#userid").val("");
			$("#replytext").val("");
			init();
		}
	})
}

//let selectedreplynum = -1;
function openReplyInsert(replynum){	

	let getreplyupdatetarget = $(".replyUpdateTarget");
	if(selectedreplynum == replynum){
		getreplyupdatetarget.empty();
		selectedreplynum = -1;	
		return;
	}	
	let userid = document.getElementById('userid').value;
	
	$.ajax({
		url: 'selectone'
		, method: 'GET'
		, data: { "replynum": replynum }
		, async : false
		, success: function(resp) {					
			//let userid = $("#userid").val();
			if (userid != null) {				
				let result = '<table>';
				result += '<tr>'
				/*result += '	<td class="replynum">'+resp.replynum +'</td>';*/
				result += ' <td class="userid" align=right>' + userid +  '</td>'
				result += '	<td class="replytext">'
				result += '		<input type="textarea" id=nestedreplytext>'
				result += '</td>'
				result += '	<td class="regdate">' + resp.regdate +  '</td>'
				result += '	<td>'
				result += '		<input type="button" class="cancelbtn"    data-num="' + resp.replynum +  '"  value="취소">'
				result += '		<input type="button" class="insertnestedreply" data-num="' + resp.replynum +  '"  value="댓글등록">'
				result += '	</td>'
				result += '</tr>'
				result += '</table>';	
				getreplyupdatetarget.empty();
				//$(".replyUpdateTarget").empty();			
				let temp = $('#' + replynum); 
				temp.next().html(result);
				temp.next().css({ "font-weight":  "bold" });	
				$(".cancelbtn").on('click', init);
				$(".insertnestedreply").on('click', function(){
					insert(replynum);
				});				
			}
			else {
				alert("로그인 해주세요");
			}
		}
	});		
	getreplyupdatetarget.show(1);	
	$(".replytr").show(1);
	//$(".replyUpdateTarget").show(1);
	selectedreplynum = replynum;	
}