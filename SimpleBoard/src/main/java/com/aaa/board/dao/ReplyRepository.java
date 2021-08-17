package com.aaa.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aaa.board.vo.ReplyVO;

@Repository
public class ReplyRepository {

	@Autowired
	SqlSession session;

	public List<ReplyVO> selectAll(int boardnum) {
		ReplyMapper mapper = session.getMapper(ReplyMapper.class);

		List<ReplyVO> list = null;

		try {
			list = mapper.selectAll(boardnum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public int insert(ReplyVO reply) {
		ReplyMapper mapper = session.getMapper(ReplyMapper.class);
		int result = 0;
		try {
			result = mapper.insert(reply);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int delete(Map<String, Object> idOrReplyNum) {
		ReplyMapper mapper = session.getMapper(ReplyMapper.class);
		int result = 0;
		try {
			result = mapper.delete(idOrReplyNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ReplyVO selectOne(int replynum) {
		ReplyMapper mapper = session.getMapper(ReplyMapper.class);
		ReplyVO result = null;
		try {
			result = mapper.selectOne(replynum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update(ReplyVO replyVO) {
		ReplyMapper mapper = session.getMapper(ReplyMapper.class);
		int result = 0;
		try {
			result = mapper.update(replyVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
