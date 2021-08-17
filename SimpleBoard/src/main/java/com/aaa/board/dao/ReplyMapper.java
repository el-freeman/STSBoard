package com.aaa.board.dao;

import java.util.List;
import java.util.Map;

import com.aaa.board.vo.ReplyVO;

public interface ReplyMapper {
	// 댓글 저장
	public int insert(ReplyVO reply) throws Exception;
	
	// 댓글 조회(한개 조회)
	public ReplyVO selectOne(int replynum) throws Exception;
	
	// 댓글 삭제
	public int delete(Map<String, Object> idOrReplyNum) throws Exception;
	
	// 댓글 수정
	public int update(ReplyVO reply) throws Exception;
	
	// 댓글 전체 조회
	public List<ReplyVO> selectAll(int boardnum) throws Exception;
}
