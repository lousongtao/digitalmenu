package com.shuishou.digitalmenu.account.models;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.shuishou.digitalmenu.models.BaseDataAccessor;

@Repository
public class UserPermissionDataAccessor extends BaseDataAccessor implements IUserPermissionDataAccessor {

	@Override
	public Serializable save(UserPermission up) {
		return sessionFactory.getCurrentSession().save(up);
	}

	@Override
	public void deleteByUserId(long userId) {
		String sql = "delete from user_permission where user_id = "+userId;
		sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
	}

}
