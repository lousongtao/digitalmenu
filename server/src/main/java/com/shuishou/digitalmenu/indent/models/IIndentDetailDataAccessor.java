package com.shuishou.digitalmenu.indent.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

public interface IIndentDetailDataAccessor {
	Session getSession();
	
	Serializable save(IndentDetail indentDetail);
	
	void update(IndentDetail indentDetail);
	
	void delete(IndentDetail indentDetail);
	
	IndentDetail getIndentDetailById(int id);
	
	IndentDetail getIndentDetailByParent(int indentId, int dishId);
	
	List<IndentDetail> getIndentDetailByIndentId(int indentId);
	
	List<IndentDetail> getAllIndentDetail();

	List<IndentDetail> getIndentDetailNotReadyByTime(Date time);
}
