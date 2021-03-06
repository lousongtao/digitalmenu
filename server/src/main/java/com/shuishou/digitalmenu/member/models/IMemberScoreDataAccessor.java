package com.shuishou.digitalmenu.member.models;

import java.util.List;

public interface IMemberScoreDataAccessor {

	List<MemberScore> getMemberScoreByMemberId(int memberId);
	
	void save(MemberScore ms);
	
	void delete(MemberScore ms);
	
	void deleteByMember(int memberId);
}
