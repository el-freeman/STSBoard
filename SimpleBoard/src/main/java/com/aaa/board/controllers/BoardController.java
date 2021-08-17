package com.aaa.board.controllers;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aaa.board.dao.BoardRepository;
import com.aaa.board.dao.ReplyRepository;
import com.aaa.board.util.FileService;
import com.aaa.board.util.PageNavigator;
import com.aaa.board.vo.Board;

@Controller
public class BoardController {
	private static Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	BoardRepository repository;
	@Autowired
	ReplyRepository replyRepository;

	final String uploadPath = "/boardfile";

	@RequestMapping("/listboard")
	public String listboard(@RequestParam(value = "searchItem", defaultValue = "title") String searchItem,
			@RequestParam(value = "searchWord", defaultValue = "") String searchWord,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage, Model model) {

		logger.info("searchItem : {}", searchItem);
		logger.info("searchWord : {}", searchWord);
		logger.info("요청한페이지 : {}", currentPage);

		// paging
		int totalRecordCount = repository.getBoardCount(searchItem, searchWord);
		logger.info("글 전체 개수 : {}", totalRecordCount);
		
		PageNavigator navi = new PageNavigator(currentPage, totalRecordCount);
		
		int totalPageCount = navi.getTotalPageCount();
		int srow = navi.getSrow();
		int erow = navi.getErow();
		
		List<Board> list = repository.selectAll(srow, erow, searchItem, searchWord);

		model.addAttribute("list", list);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("searchWord", searchWord);
		//model.addAttribute("totalRecordCount", totalRecordCount);
	
		model.addAttribute("currentPage", navi.getCurrentPage());
		model.addAttribute("navi", navi);

		return "board/listBoard";
	}

	/**
	 * 글 등록을 위한 화면 요청
	 * 
	 * @return
	 */
	@RequestMapping("/writeboard")
	public String writeboard() {

		return "board/writeBoard";
	}

	/**
	 * 글 등록 처리를 위한 요청
	 * 
	 * @return
	 */
	@RequestMapping(value = "/writeboard", method = RequestMethod.POST)
	public String writeboard(Board board, MultipartFile upload) {

		if(!upload.isEmpty()) {
			String originalFilename = upload.getOriginalFilename();
			String savedFilename = FileService.saveFile(upload, uploadPath);

			board.setOriginalfile(originalFilename);
			board.setSavedfile(savedFilename);
		}	
		
		int result = repository.insert(board); 
		logger.info("게시글 등록 여부 : {}", result);
		 
		return "redirect:listboard";
	}

	@RequestMapping("/detailboard")
	public String detailboard(int boardnum, Model model) {
		Board board = repository.selectOne(boardnum);
		repository.updateHitCount(boardnum);
		model.addAttribute("board", board);

		return "board/detailBoard";
	}

	@RequestMapping("/deleteboard")
	public String deleteboard(int boardnum, HttpSession session) {
		logger.info("삭제할 글번호 : " + boardnum);
		
		String loginId = (String)session.getAttribute("loginId");
		
		Board b = repository.selectOne(boardnum);
		
		if(loginId!=null && loginId.equals(b.getUserid())) {
			
			String oldfile = b.getSavedfile();
			
			if(oldfile != null) {
				String fullPath = uploadPath + "/" + oldfile;
				FileService.deleteFile(fullPath);
			}						
			// 글 삭제되면 그 글에 달려있는 댓글도 다 삭제			
			Map<String, Object> boardNum = new HashMap<String, Object>();
			boardNum.put("type", "boardnum");
			boardNum.put("value", boardnum);
			replyRepository.delete(boardNum);
		
			repository.delete(boardnum);
		}				
		return "redirect:listboard";
	}

	/**
	 * 수정할 수 있는 화면 요청
	 * 
	 * @param boardnum
	 * @param model
	 * @return
	 */
	@RequestMapping("/updateboard")
	public String updateboard(int boardnum, Model model, HttpSession session) {
		
		String loginId = (String) session.getAttribute("loginId");
		Board board = repository.selectOne(boardnum);
		if(loginId!=null && loginId.equals(board.getUserid())) {
			
			logger.info("수정할 글번호 : " + boardnum);
			model.addAttribute("board", board);

			return "board/updateBoard";
		}
		return "redirect:detailboard?boardnum="+boardnum;
	}
				

	@RequestMapping(value = "/updateboard", method = RequestMethod.POST)
	public String updateboard(Board board, MultipartFile upload) {
		Board b = repository.selectOne(board.getBoardnum());
		String oldfile = b.getSavedfile();
		
		if(oldfile != null) {
			String fullPath = uploadPath + "/" + oldfile;
			FileService.deleteFile(fullPath);
		}
		
		if(!upload.isEmpty()) {
			String originalFilename = upload.getOriginalFilename();
			String savedFilename = FileService.saveFile(upload, uploadPath);

			board.setOriginalfile(originalFilename);
			board.setSavedfile(savedFilename);
		}	
		
		int result = repository.update(board);

		return "redirect:detailboard?boardnum="+board.getBoardnum();
	}
	
	@RequestMapping("/download")
	public String download(int boardnum, HttpServletResponse response, HttpSession session) {
		
		String loginId = (String) session.getAttribute("loginId");
		Board board = repository.selectOne(boardnum);
		if(loginId!=null) {
			String originalFile = board.getOriginalfile();
			try {
				response.setHeader("Content-Disposition", " attachment;filename="+ URLEncoder.encode(originalFile, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			String fullPath = uploadPath + "/" + board.getSavedfile();
			
			FileInputStream filein = null; //서버가 hdd에서 메모리로 파일을 업로드할 때
			ServletOutputStream fileout = null; //클라이언트한테 통신으로 내보낼때 사용하는 객체
			
			try {
				filein = new FileInputStream(fullPath);
				fileout = response.getOutputStream();
				
				FileCopyUtils.copy(filein, fileout);
				
				filein.close();
				fileout.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		return null;
	}	
}