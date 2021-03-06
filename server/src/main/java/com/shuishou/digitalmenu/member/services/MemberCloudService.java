package com.shuishou.digitalmenu.member.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.digitalmenu.ConstantValue;
import com.shuishou.digitalmenu.DataCheckException;
import com.shuishou.digitalmenu.ServerProperties;
import com.shuishou.digitalmenu.account.models.IUserDataAccessor;
import com.shuishou.digitalmenu.account.models.UserData;
import com.shuishou.digitalmenu.common.models.Configs;
import com.shuishou.digitalmenu.common.models.IConfigsDataAccessor;
import com.shuishou.digitalmenu.log.models.LogData;
import com.shuishou.digitalmenu.log.services.ILogService;
import com.shuishou.digitalmenu.member.models.Member;
import com.shuishou.digitalmenu.member.models.MemberBalance;
import com.shuishou.digitalmenu.member.models.MemberScore;
import com.shuishou.digitalmenu.member.views.MemberBalanceInfo;
import com.shuishou.digitalmenu.member.views.MemberStatInfo;
import com.shuishou.digitalmenu.views.ObjectListResult;
import com.shuishou.digitalmenu.views.ObjectResult;
import com.shuishou.digitalmenu.views.Result;

@Service
public class MemberCloudService implements IMemberCloudService{

	@Autowired
	private IConfigsDataAccessor configDA;
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IUserDataAccessor userDA;
	
	@Override
	@Transactional
	public ObjectResult addMember(int userId, String name, String memberCard, String address, String postCode,
			String telephone, Date birth, double discountRate, String password) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("name", name);
		params.put("memberCard", memberCard);
		params.put("discountRate", String.valueOf(discountRate));
		params.put("telephone", telephone);
		params.put("address", address);
		params.put("postCode", postCode);
		params.put("password", password);
		if (birth != null)
			params.put("birth", ConstantValue.DFYMDHMS.format(birth));
		
		String url = "member/addmember";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for add member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while add member. URL = " + url + ", response = "+response, false);
		}
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " add member name : " + name + ", card : " + memberCard);
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	@Transactional
	public ObjectResult updateMember(int userId, int id, String name, String memberCard, String address,
			String postCode, String telephone, Date birth, double discountRate) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("name", name);
		params.put("memberCard", memberCard);
		params.put("discountRate", String.valueOf(discountRate));
		params.put("telephone", telephone);
		params.put("address", address);
		params.put("postCode", postCode);
		if (birth != null)
			params.put("birth", ConstantValue.DFYMDHMS.format(birth));
		params.put("id", String.valueOf(id));
		String url = "member/updatemember";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member. URL = " + url + ", response = "+response, false);
		}
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member info to name : " + name
				+ ", memberCard : " + memberCard + ", address : " + address + ", postCode : "+ postCode + ", telephone : "+ telephone
				+ ", birth" + (birth == null ? "": ConstantValue.DFYMD.format(birth)));
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	@Transactional
	public ObjectResult updateMemberScore(int userId, int id, double newScore) {
		Configs configs = configDA.getConfigsByName(ConstantValue.CONFIGS_BRANCHNAME);
		String branchName = configs == null? "" : configs.getValue();
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("newScore", String.valueOf(newScore));
		params.put("id", String.valueOf(id));
		params.put("branchName", branchName);
		String url = "member/updatememberscore";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member score. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member score. URL = " + url + ", response = "+response, false);
		}
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member score to " + newScore
				+ ". id = " + id + ", name = " + result.data.getName() + ", card = " + result.data.getMemberCard() );
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	@Transactional
	public ObjectResult updateMemberBalance(int userId, int id, double newBalance) {
		Configs configs = configDA.getConfigsByName(ConstantValue.CONFIGS_BRANCHNAME);
		String branchName = configs == null? "" : configs.getValue();
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		params.put("newBalance", String.valueOf(newBalance));
		params.put("branchName", branchName);
		String url = "member/updatememberbalance";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member balance. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member balance. URL = " + url + ", response = "+response, false);
		}
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member balance to " + newBalance
				+ ". id = " + id + ", name = " + result.data.getName() + ", card = " + result.data.getMemberCard() );
		
		return new ObjectResult(Result.OK, true, result.data);
	}
	
	@Override
	@Transactional
	public ObjectResult updateMemberPassword(int userId, int id, String oldPassword, String newPassword) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		params.put("oldPassword", oldPassword);
		params.put("newPassword", newPassword);
		String url = "member/updatememberpassword";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member password. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member password. URL = " + url + ", response = "+response, false);
		}
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member password."
				+ " id = " + id + ", name = " + result.data.getName() + ", card = " + result.data.getMemberCard() );
		return new ObjectResult(Result.OK, true, result.data);
	}
	
	@Override
	@Transactional
	public ObjectResult resetMemberPassword111111(int userId, int id) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		String url = "member/resetmemberpassword111111";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for reset member password 111111. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while reset member password 111111. URL = " + url + ", response = "+response, false);
		}
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member password to 111111"
				+ ". id = " + id + ", name = " + result.data.getName() + ", card = " + result.data.getMemberCard() );
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	@Transactional
	public ObjectResult memberRecharge(int userId, int id, double recharge, String payway) {
		Configs configs = configDA.getConfigsByName(ConstantValue.CONFIGS_BRANCHNAME);
		String branchName = configs == null? "" : configs.getValue();
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		params.put("rechargeValue", String.valueOf(recharge));
		params.put("branchName", branchName);
		params.put("payway", payway);
		String url = "member/memberrecharge";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member balance. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member balance. URL = " + url + ", response = "+response, false);
		}
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " recharge member. id = " + id 
				+ ", name = " + result.data.getName() + ", card = " + result.data.getMemberCard() 
				+ ", recharge amount = " + recharge + " payway = " + payway);
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	@Transactional
	public ObjectResult deleteMember(int userId, int id) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		String url = "member/deletemember";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for delete member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member. URL = " + url + ", response = "+response, false);
		}
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " delete member id : " + id);
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	@Transactional
	public ObjectListResult queryMember(String name, String memberCard, String address, String postCode,
			String telephone) {
		String url = "member/querymember";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		if (name != null && name.length() > 0)
			params.put("name",name);
		if (memberCard != null && memberCard.length() > 0)
			params.put("memberCard", memberCard);
		if (address != null && address.length() > 0)
			params.put("address", address);
		if (postCode != null && postCode.length() > 0)
			params.put("postCode", postCode);
		if (telephone != null && telephone.length() > 0)
			params.put("telephone", telephone);
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<ArrayList<Member>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Member>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}
	
	@Override
	@Transactional
	public ObjectResult queryMemberByCard(String memberCard) {
		String url = "member/querymember";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		if (memberCard != null && memberCard.length() > 0)
			params.put("memberCard", memberCard);
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for query member By Card. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<ArrayList<Member>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Member>>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while query member By Card. URL = " + url + ", response = "+response, false);
		}
		Member m = null;
		if (result.data != null && !result.data.isEmpty()){
			m = result.data.get(0);
		}
		return new ObjectResult(Result.OK, true, m);
	}
	
	@Override
	@Transactional
	public ObjectListResult queryMemberHazily(String key) {
		String url = "member/querymemberhazily";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("key", key);
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member hazily. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<ArrayList<Member>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Member>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member hazily. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}

	@Override
	@Transactional
	public ObjectListResult queryAllMember() {
		String url = "member/queryallmember";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for loading member. URL = " + url, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<ArrayList<Member>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Member>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while loading member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}

	@Override
	@Transactional
	public ObjectResult recordMemberConsumption(String memberCard, String memberPassword, double consumptionPrice) throws DataCheckException {
		boolean byScore = false;
		double scorePerDollar = 0;
		boolean byDeposit = false;
		String branchName = "";
		boolean needPassword = false;
		List<Configs> configs = configDA.queryConfigs();
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
		
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("memberCard",memberCard);
		if (needPassword){
			params.put("memberPassword", memberPassword);
		}
		params.put("consumptionPrice", String.format(ConstantValue.FORMAT_DOUBLE, consumptionPrice));
		params.put("scorePerDollar", String.valueOf(scorePerDollar));
		params.put("byDeposit", String.valueOf(byDeposit));
		params.put("branchName", branchName);
		params.put("byScore", String.valueOf(byScore));
		
		String url = "member/recordconsumption";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for delete member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectResult test10000() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ObjectListResult queryMemberBalance(int memberId){
		String url = "member/querymemberbalance";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("memberId", String.valueOf(memberId));
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member balance. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<ArrayList<MemberBalance>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<MemberBalance>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member balance. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}
	
	@Override
	@Transactional
	public ObjectListResult queryMemberBalance(Date startTime, Date endTime, String type){
		String url = "member/querymemberbalancebytime";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		if (startTime != null){
			params.put("startTime", ConstantValue.DFYMDHMS.format(startTime));
		}
		if (endTime != null){
			params.put("endTime", ConstantValue.DFYMDHMS.format(endTime));
		}
		params.put("type", type);
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member balance. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<ArrayList<MemberBalanceInfo>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<MemberBalanceInfo>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member balance. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}
	
	@Override
	@Transactional
	public ObjectListResult statMemberByTime(Date startTime, Date endTime) {
		String url = "member/statmemberbytime";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		if (startTime != null){
			params.put("startTime", ConstantValue.DFYMDHMS.format(startTime));
		}
		if (endTime != null){
			params.put("endTime", ConstantValue.DFYMDHMS.format(endTime));
		}
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member balance. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<ArrayList<MemberStatInfo>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<MemberStatInfo>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member balance. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}
	
	@Override
	@Transactional
	public ObjectListResult queryMemberScore(int memberId){
		String url = "member/querymemberscore";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("memberId", String.valueOf(memberId));
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member score. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<ArrayList<MemberScore>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<MemberScore>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member score. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}

}
