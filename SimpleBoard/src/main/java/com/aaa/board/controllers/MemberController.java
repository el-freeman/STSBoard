package com.aaa.board.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aaa.board.dao.MemberRepository;
import com.aaa.board.vo.Member;

import oracle.net.aso.s;

@Controller
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	MemberRepository repository;

	/**
	 * 회원 가입을 위한 화면을 요청
	 * @return 회원 가입 화면을 보여주는 jsp로 forwarding
	 */
	@RequestMapping("/join")
	public String join() {
		
		return "member/joinForm";
	}
	
	/**
	 * 회원의 정보를 수집하여 DB에 저장
	 * @param member : 수집된 회원정보
	 * @return 
	 */
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(Member member) {
		logger.info("member : {}", member.toString());
		
		int result = repository.join(member);
		logger.info("회원가입 결과 : {}" , result);
		
		return "redirect:/";
	}
	
	/**
	 * 로그인을 위한 화면 요청
	 * @return
	 */
	String referer ="";
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		referer = request.getHeader("Referer");
		return "member/loginForm";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(Member member, Model model, HttpSession session,
			@CookieValue(value = "saveid",defaultValue = "" ) String id,
			boolean saveid,
			HttpServletResponse response) {
		
		if(saveid) {
			Cookie cookie= new Cookie("saveid", member.getUserid());
			cookie.setMaxAge(60*60*24);
			response.addCookie(cookie);
		} 
		else {
			Cookie cookie= new Cookie("saveid", null);
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}		
		Member m = repository.selectMemeber(member); 

		String message = "";
		if(m == null) {
			message = "로그인을 할 수 없습니다.";
			model.addAttribute("message", message);
			
			return "member/loginForm";
			
		} else {
			session.setAttribute("loginId", m.getUserid());
			session.setAttribute("loginName", m.getUsername());
	
			return "redirect:"+referer;
		}
	}
	
	/**
	 * 로그아웃 : HttpSession의 정보를 삭제
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session, HttpServletRequest request) {
		session.invalidate();
		referer = request.getHeader("Referer");
		return "redirect:"+referer;
	}
	
	/**
	 * 회원정보수정을 위한 화면 요청
	 * @return
	 */
	@RequestMapping("/update")
	public String update(HttpSession session, Model model, HttpServletRequest request) {
	
		Object object = session.getAttribute("loginId");
		String userid = (String) object;
		logger.info("member : {}", userid);
	
		Member member =  repository.selectOne(userid);
		model.addAttribute("member", member);
		referer = request.getHeader("Referer");
		return "member/updateForm";
	}	
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(Member member) {
		int result = repository.update(member);
		logger.info("정보 수정 결과 : {}" , result);
		
		return "redirect:"+referer;
	}
	
	@RequestMapping(value = "idCheck", method = RequestMethod.POST)
	@ResponseBody
	public String idCheck(Member member) {
		Member m = repository.selectMemeber(member);
		System.out.println(m);		
		if(m==null) {
			return "success";
		}
		else {
			return "fail";
		}
	}
	
	@RequestMapping("/deletemember")
	public String delete(HttpSession session){
		
		String userid = (String)session.getAttribute("loginId");
		int result = repository.delete(userid);
		session.invalidate();
		
		return "redirect:/";
	}
}






