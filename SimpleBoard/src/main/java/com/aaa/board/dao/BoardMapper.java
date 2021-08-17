package com.aaa.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.aop.ThrowsAdvice;

import com.aaa.board.vo.Board;

public interface BoardMapper {
	public int insertBoard(Board board) throws Exception; 	// 글 등록
	public int updateBoard(Board board) throws Exception;	// 글 수정
	public int deleteBoard(int boardnum) throws Exception;	// 글 삭제
	public List<Board> selectBoard(Map<String, Object> search) throws Exception;		// 글 전체 목록 조회
	public Board selectOne(int boardnum) throws Exception;	// 글 한 개 조회
	public int updateHitCount(int boardnum) throws Exception; // 조회수 증가
	public int getBoardCount(Map<String, String> search) throws Exception; // 전체 글개수 조회
	public int updateReplyCount(int boardnum); //댓글 수 업데이트
}
