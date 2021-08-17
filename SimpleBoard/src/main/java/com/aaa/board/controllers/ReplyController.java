package com.aaa.board.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aaa.board.dao.BoardRepository;
import com.aaa.board.dao.ReplyRepository;
import com.aaa.board.vo.ReplyVO;


@Controller
public class ReplyController {

	@Autowired
	ReplyRepository repository;
	
	@Autowired
	BoardRepository boardRepository;
	
	@RequestMapping("/reply")
	public String replyForm() {
		return "replyForm";
	}
	
	@RequestMapping("/selectAll")
	@ResponseBody
	public List<ReplyVO> selectAll(int boardnum) {
		List<ReplyVO> list = repository.selectAll(boardnum);
		
		return list;
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	@ResponseBody
	public void insert(ReplyVO reply) {

		int result = repository.insert(reply);
		int result2 = boardRepository.replyCountUpdate(reply.getBoardnum());
		return;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(int replynum, HttpSession session) {
		ReplyVO one = selectOne(replynum);
		int result = 0;
		if(one.getUserid().equals((String)session.getAttribute("loginId"))) {
			Map<String, Object> replyNum = new HashMap<String, Object>();
			replyNum.put("type", "replynum");
			replyNum.put("value", replynum);
			result = repository.delete(replyNum);
		}		
		
		if(result == 1) {
			int result2 = boardRepository.replyCountUpdate(one.getBoardnum());
			return "Success";
		}
		return "Fail";
	}
	
	@RequestMapping(value ="/selectone", method = RequestMethod.GET)
	@ResponseBody
	public ReplyVO selectOne(int replynum) {
		ReplyVO result = repository.selectOne(replynum);
		return result;
	}
	
	@RequestMapping(value ="/updatereply", method = RequestMethod.POST)
	@ResponseBody
	public int updateReply(ReplyVO replyVO) {
		
		int result = repository.update(replyVO);
		if(replyVO.getIsdeleted() == 1 ) {
			int result2 = boardRepository.replyCountUpdate(replyVO.getBoardnum());			
		}
		return result;
	}	
}