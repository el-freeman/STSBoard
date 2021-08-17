package com.aaa.board.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aaa.board.vo.Member;

@Repository
public class MemberRepository {

	@Autowired
	SqlSession session;
	
	/**
	 * 회원가입 처리
	 * @param member 사용자가 입력한 사용자 정보
	 * @return
	 */
	public int join(Member member) {
		MemberMapper mapper = session.getMapper(MemberMapper.class);
		
		int result = 0 ;
		
		try {
			result = mapper.insertMember(member);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public Member selectMemeber(Member member) {
		MemberMapper mapper = session.getMapper(MemberMapper.class);
		Member m = null;
		
		try {
			m = mapper.selectMember(member);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}

	public int update(Member member) {
		MemberMapper mapper = session.getMapper(MemberMapper.class);
		int result = 0;
		
		try {
			result = mapper.updateMember(member);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Member selectOne(String userid) {
		MemberMapper mapper = session.getMapper(MemberMapper.class);
		Member m = null;
		
		try {
			m = mapper.selectOne(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}

	public int delete(String userid) {
		MemberMapper mapper = session.getMapper(MemberMapper.class);
		int result = 0;
		
		try {
			result = mapper.deleteMember(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
