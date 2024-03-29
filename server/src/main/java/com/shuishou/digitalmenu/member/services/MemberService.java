package com.shuishou.digitalmenu.member.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shuishou.digitalmenu.ConstantValue;
import com.shuishou.digitalmenu.DataCheckException;
import com.shuishou.digitalmenu.account.models.IUserDataAccessor;
import com.shuishou.digitalmenu.account.models.UserData;
import com.shuishou.digitalmenu.common.models.Configs;
import com.shuishou.digitalmenu.common.models.IConfigsDataAccessor;
import com.shuishou.digitalmenu.log.models.LogData;
import com.shuishou.digitalmenu.log.services.ILogService;
import com.shuishou.digitalmenu.member.models.IMemberBalanceDataAccessor;
import com.shuishou.digitalmenu.member.models.IMemberDataAccessor;
import com.shuishou.digitalmenu.member.models.IMemberScoreDataAccessor;
import com.shuishou.digitalmenu.member.models.Member;
import com.shuishou.digitalmenu.member.models.MemberBalance;
import com.shuishou.digitalmenu.member.models.MemberScore;
import com.shuishou.digitalmenu.member.views.MemberBalanceInfo;
import com.shuishou.digitalmenu.member.views.MemberStatInfo;
import com.shuishou.digitalmenu.views.ObjectListResult;
import com.shuishou.digitalmenu.views.ObjectResult;
import com.shuishou.digitalmenu.views.Result;

@Service
public class MemberService implements IMemberService{

	@Autowired
	private IMemberDataAccessor memberDA;
	
	@Autowired
	private IMemberBalanceDataAccessor memberBalanceDA;
	
	@Autowired
	private IMemberScoreDataAccessor memberScoreDA;
	
	@Autowired
	private IConfigsDataAccessor configsDA;
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IUserDataAccessor userDA;
	
	@Override
	@Transactional
	public ObjectResult addMember(int userId, String name, String memberCard, String address, String postCode,
			String telephone, Date birth, double discountRate, String password) {
		Member m = new Member();
		m.setName(name);
		m.setMemberCard(memberCard);
		if (address != null)
			m.setAddress(address);
		if (postCode != null)
			m.setPostCode(postCode);
		if (telephone != null)
			m.setTelephone(telephone);
		if (birth != null)
			m.setBirth(birth);
		m.setDiscountRate(discountRate);
		m.setCreateTime(new Date());
		m.setLastModifyTime(new Date());
		m.setPassword(password);
		memberDA.save(m);
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " add member : " + m);
		return new ObjectResult(Result.OK, true, m);
	}

	@Override
	@Transactional
	public ObjectResult updateMember(int userId, int id, String name, String memberCard, String address,
			String postCode, String telephone, Date birth, double discountRate) {
		Member m = memberDA.getMemberById(id);
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		m.setName(name);
		m.setMemberCard(memberCard);
		m.setAddress(address);
		m.setPostCode(postCode);
		m.setTelephone(telephone);
		m.setBirth(birth);
		m.setDiscountRate(discountRate);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member info to name : " + name
				+ ", memberCard : " + memberCard + ", address : " + address + ", postCode : "+ postCode + ", telephone : "+ telephone
				+ ", birth" + (birth == null ? "": ConstantValue.DFYMD.format(birth)));
		return new ObjectResult(Result.OK, true, m);
	}
	
	@Override
	@Transactional
	public ObjectResult updateMemberScore(int userId, int id, double newScore) {
		Member m = memberDA.getMemberById(id);
		
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		
		double oldScore = m.getScore();
		m.setScore(newScore);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		
		String branchName = "";
		List<Configs> configs = configsDA.queryConfigs();
		for(Configs config : configs){
			if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
				branchName = config.getValue();
			} 
		}
		
		MemberScore ms = new MemberScore();
		ms.setAmount(newScore - oldScore);
		ms.setDate(new Date());
		ms.setMember(m);
		ms.setNewValue(newScore);
		ms.setPlace(branchName);
		ms.setType(ConstantValue.MEMBERSCORE_ADJUST);
		memberScoreDA.save(ms);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member score "+ oldScore +" to " + newScore);
		return new ObjectResult(Result.OK, true, m);
	}
	
	@Override
	@Transactional
	public ObjectResult updateMemberBalance(int userId, int id, double newBalance) {
		Member m = memberDA.getMemberById(id);
		
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		
		double oldBalance = m.getBalanceMoney();
		m.setBalanceMoney(newBalance);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		
		String branchName = "";
		List<Configs> configs = configsDA.queryConfigs();
		for(Configs config : configs){
			if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
				branchName = config.getValue();
			} 
		}
		
		MemberBalance mb = new MemberBalance();
		mb.setAmount(newBalance - oldBalance);
		mb.setDate(new Date());
		mb.setMember(m);
		mb.setNewValue(newBalance);
		mb.setPlace(branchName);
		mb.setType(ConstantValue.MEMBERDEPOSIT_ADJUST);
		memberBalanceDA.save(mb);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member balance "+ oldBalance +" to " + newBalance);
		return new ObjectResult(Result.OK, true, m);
	}
	
	@Override
	@Transactional
	public ObjectResult updateMemberPassword(int userId, int id, String oldPassword, String newPassword){
		Member m = memberDA.getMemberById(id);
		
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		if (!oldPassword.equals(m.getPassword())){
			return new ObjectResult("the old password is wrong", false, null);
		}
		m.setPassword(newPassword);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member password");
		return new ObjectResult(Result.OK, true);
	}
	
	@Override
	@Transactional
	public ObjectResult resetMemberPassword111111(int userId, int id){
		Member m = memberDA.getMemberById(id);
		
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		try {
			m.setPassword(toSHA1("111111".getBytes()));
			m.setLastModifyTime(new Date());
			memberDA.save(m);
			UserData selfUser = userDA.getUserById(userId);
			logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member password to 111111");
			return new ObjectResult(Result.OK, true);
		} catch (NoSuchAlgorithmException e) {
			return new ObjectResult(e.getMessage(), false, null);
		}
	}
	
	@Override
	@Transactional
	public ObjectResult memberRecharge(int userId, int id, double rechargeValue, String payway) {
		Member m = memberDA.getMemberById(id);
		
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		
		double oldBalance = m.getBalanceMoney();
		m.setBalanceMoney(m.getBalanceMoney() + rechargeValue);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		String branchName = "";
		List<Configs> configs = configsDA.queryConfigs();
		for(Configs config : configs){
			if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
				branchName = config.getValue();
			} 
		}
		MemberBalance mb = new MemberBalance();
		mb.setAmount(rechargeValue);
		mb.setDate(new Date());
		mb.setMember(m);
		mb.setNewValue(m.getBalanceMoney());
		mb.setPlace(branchName);
		mb.setType(ConstantValue.MEMBERDEPOSIT_RECHARGE);
		mb.setPayway(payway);
		memberBalanceDA.save(mb);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " recharge member. old balance = "+ oldBalance +", recharge to " + m.getBalanceMoney());
		return new ObjectResult(Result.OK, true, m);
	}

	@Override
	@Transactional
	public ObjectResult deleteMember(int userId, int id) {
		Member m = memberDA.getMemberById(id);
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		memberDA.delete(m);
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " delete member  : " + m.getName());
		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectListResult queryMember(String name, String memberCard, String address, String postCode, String telephone) {
//		int count = memberDA.queryMemberCount(name, memberCard, address, postCode, telephone);
//		if (count >= 300)
//			return new ObjectListResult("Record is over 300, please change the filter", false, null, count);
		List<Member> members = memberDA.queryMember(name, memberCard, address, postCode, telephone);
		return new ObjectListResult(Result.OK, true, members, 0);
	}
	
	@Override
	@Transactional
	public ObjectResult queryMemberByCard(String memberCard) {
		List<Member> members = memberDA.queryMember(null, memberCard, null, null, null);
		Member m = null;
		if (members != null && !members.isEmpty()){
			m = members.get(0);
		}
		return new ObjectResult(Result.OK, true, m);
	}
	
	/**
	 * query member using a key
	 * key maybe is the name, the telephone, or the code
	 * @param key
	 * @return
	 */
	@Override
	@Transactional
	public ObjectListResult queryMemberHazily(String key) {
		List<Member> members = memberDA.queryMemberHazily(key);
		return new ObjectListResult(Result.OK, true, members, 0);
	}
	
	@Override
	@Transactional
	public ObjectListResult queryAllMember() {
		List<Member> members = memberDA.queryAllMember();
		return new ObjectListResult(Result.OK, true, members, members.size());
	}

	@Override
	@Transactional
	public ObjectListResult queryMemberBalance(int memberId) {
		List<MemberBalance> mbs = memberBalanceDA.getMemberBalanceByMemberId(memberId);
		
		return new ObjectListResult(Result.OK, true, mbs, mbs.size());
	}

	@Override
	@Transactional
	public ObjectListResult queryMemberBalance(Date startTime, Date endTime, String type) {
		List<MemberBalance> mbs = memberBalanceDA.queryMemberBalance(startTime, endTime, type);
		List<MemberBalanceInfo> mbis = new ArrayList<>();
		if (mbs != null && !mbs.isEmpty()){
			List<Member> members = memberDA.queryAllMember();
			for (int i = 0; i < mbs.size(); i++) {
				MemberBalance mb = mbs.get(i);
				MemberBalanceInfo mbi = new MemberBalanceInfo();
				mbi.id = mb.getId();
				mbi.amount = mb.getAmount();
				mbi.date = mb.getDate();
				mbi.memberId = mb.getMember().getId();
				mbi.newValue = mb.getNewValue();
				mbi.place = mb.getPlace();
				mbi.type = mb.getType();
				mbi.payway = mb.getPayway();
				for (int j = 0; j < members.size(); j++) {
					if (mbi.memberId == members.get(j).getId()){
						mbi.memberCard = members.get(j).getMemberCard();
						mbi.memberName = members.get(j).getName();
						break;
					}
				}
				mbis.add(mbi);
			}
		}
		
		
		return new ObjectListResult(Result.OK, true, mbis, mbis.size());
	}
	
	@Override
	@Transactional
	public ObjectListResult statMemberByTime(Date startTime, Date endTime) {
		String type = ConstantValue.MEMBERBALANCE_QUERYTYPE_RECHARGE + ConstantValue.MEMBERBALANCE_QUERYTYPE_ADJUST + ConstantValue.MEMBERBALANCE_QUERYTYPE_CONSUME;
		List<MemberBalance> mbs = memberBalanceDA.queryMemberBalance(startTime, endTime, type);
		List<Member> members = memberDA.queryAllMember();
		HashMap<Integer, Member> hmMember = new HashMap<>();//把member数据放入map中, 可快速查询
		for (int i = 0; i < members.size(); i++) {
			Member m = members.get(i);
			hmMember.put(m.getId(), m);
		}
		
		HashMap<Integer, MemberStatInfo> hmStat = new HashMap<>();//key=member.id
		if (mbs != null){
			for (int i = 0; i < mbs.size(); i++) {
				MemberBalance mb = mbs.get(i);
				int memberid = mb.getMember().getId();
				Member m = hmMember.get(memberid);
				if (m == null)
					continue;
				MemberStatInfo ms = hmStat.get(memberid);
				if (ms == null) {
					ms = new MemberStatInfo(m.getMemberCard(), m.getName(), m.getCreateTime());
					hmStat.put(memberid, ms);
				}
				if (mb.getType() == ConstantValue.MEMBERDEPOSIT_RECHARGE) {
					ms.addRecharge(mb.getAmount());
				}
				if (mb.getType() == ConstantValue.MEMBERDEPOSIT_ADJUST) {
					ms.addAdjust(mb.getAmount());
				}
				if (mb.getType() == ConstantValue.MEMBERDEPOSIT_CONSUM) {
					ms.addConsume(mb.getAmount());
				}
			}
		}
		List<MemberStatInfo> msis = new ArrayList<>();
		msis.addAll(hmStat.values());
		
		return new ObjectListResult(Result.OK, true, msis, msis.size());
	}
	
	@Override
	@Transactional
	public ObjectListResult queryMemberScore(int memberId) {
		List<MemberScore> mss = memberScoreDA.getMemberScoreByMemberId(memberId);
		return new ObjectListResult(Result.OK, true, mss, mss.size());
	}

	@Override
	@Transactional(rollbackFor=DataCheckException.class)
	public ObjectResult recordMemberConsumption(String memberCard, String memberPassword, double consumptionPrice) throws DataCheckException {
		Member member = memberDA.getMemberByCard(memberCard);
		if (member == null){
			throw new DataCheckException("cannot find member by card " + memberCard);
		}
		
		Date time = new Date();
		boolean byScore = false;
		double scorePerDollar = 0;
		boolean byDeposit = false;
		boolean needPassword = false;
		String branchName = "";
		List<Configs> configs = configsDA.queryConfigs();
		for(Configs config : configs){
			if (ConstantValue.CONFIGS_MEMBERMGR_BYSCORE.equals(config.getName())){
				byScore = Boolean.valueOf(config.getValue());
			} else if (ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR.equals(config.getName())){
				scorePerDollar = Double.parseDouble(config.getValue());
			} else if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
				branchName = config.getValue();
			} else if (ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT.equals(config.getName())){
				byDeposit = Boolean.valueOf(config.getValue());
			} else if (ConstantValue.CONFIGS_MEMBERMGR_NEEDPASSWORD.equals(config.getName())){
				needPassword = Boolean.valueOf(config.getValue());
			}
		}
		if (byScore && scorePerDollar > 0){
			MemberScore ms = new MemberScore();
			ms.setDate(time);
			ms.setAmount(scorePerDollar * consumptionPrice);
			ms.setPlace(branchName);
			if (consumptionPrice > 0)
				ms.setType(ConstantValue.MEMBERSCORE_CONSUM);
			else 
				ms.setType(ConstantValue.MEMBERSCORE_REFUND);
			
			ms.setMember(member);
			ms.setNewValue(member.getScore() + ms.getAmount());
			memberScoreDA.save(ms);
			member.setScore(member.getScore() + ms.getAmount());
			member.setLastModifyTime(time);
			memberDA.save(member);
		}
		if (byDeposit){
			if (member.getBalanceMoney() < consumptionPrice){
				throw new DataCheckException("Member's balance is not enough to pay");
			}
			if (needPassword){
				if (memberPassword == null || !memberPassword.equals(member.getPassword())){
					throw new DataCheckException("password is wrong");
				}
			}
			MemberBalance mc = new MemberBalance();
			mc.setAmount(consumptionPrice);
			mc.setDate(time);
			mc.setMember(member);
			mc.setPlace(branchName);
			if (consumptionPrice > 0)
				mc.setType(ConstantValue.MEMBERDEPOSIT_CONSUM);
			else 
				mc.setType(ConstantValue.MEMBERDEPOSIT_REFUND);
			mc.setNewValue(member.getBalanceMoney() - consumptionPrice);
			memberBalanceDA.save(mc);
			member.setBalanceMoney(member.getBalanceMoney() - consumptionPrice);
			member.setLastModifyTime(time);
			memberDA.save(member);
		}
		return new ObjectResult(Result.OK, true);
	}

	private String toSHA1(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ex) {
			throw ex;
		}
		return toHex(md.digest(data));
	}

	/**
	 * Bytes array to string.
	 * 
	 * @param digest
	 * @return final string.
	 */
	private String toHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(String.format("%1$02X", b));
		}

		return sb.toString();
	}
	
	@Override
	@Transactional
	public ObjectResult testInsert10000(){
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 10000; i++) {
			memberDA.getMemberById(i);
		}
		return new ObjectResult(null, true);
	}
}
