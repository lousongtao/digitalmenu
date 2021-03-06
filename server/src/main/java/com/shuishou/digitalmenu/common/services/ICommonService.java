package com.shuishou.digitalmenu.common.services;

import org.springframework.web.multipart.MultipartFile;

import com.shuishou.digitalmenu.common.views.GetDeskResult;
import com.shuishou.digitalmenu.common.views.GetDeskWithIndentResult;
import com.shuishou.digitalmenu.views.ObjectListResult;
import com.shuishou.digitalmenu.views.ObjectResult;

public interface ICommonService {

	ObjectResult checkUpgradeApk();
	ObjectResult saveCode(long userId, String oldCode, String code, String key);
	
	ObjectResult savePrintTicket(long userId, String printTicket);
	
	ObjectResult saveLanguageSet(long userId, int amount, String firstName, String secondName, boolean print2ndLanguage);
	
	GetDeskResult getDesks();
	
	GetDeskWithIndentResult getDesksWithIndents();
	
	ObjectResult saveDesk(long userId, String deskname, int sequence);
	
	ObjectResult updateDesk(long userId, int id, String name, int sequence);
	
	ObjectResult deleteDesk(long userId, int id);
	
	ObjectListResult getPrinters();
	
	ObjectResult savePrinter(long userId, String name, String printerName, int type);
	
	ObjectResult updatePrinter(long userId, int id, String name, String printerName, int type);
	
	ObjectResult deletePrinter(long userId, int id);
	
	ObjectResult testPrinterConnection(int id);
	
	ObjectListResult getDiscountTemplates();
	
	ObjectResult saveDiscountTemplate(long userId, String name, double value, int type);
	
	ObjectResult deleteDiscountTemplate(long userId, int id);
	
	ObjectListResult getPayWays();
	
	ObjectResult savePayWay(long userId, String name);
	
	ObjectResult deletePayWay(long userId, int id);
	
	GetDeskWithIndentResult mergeDesks(int userId, int mainDeskId, String subDesksId);
	
	ObjectResult uploadErrorLog(String machineCode, MultipartFile logfile);
	
	ObjectResult queryConfigMap();
	
	ObjectResult saveBranchName(int userId, String branchName);
	
	ObjectResult saveMemberManagementWay(int userId, boolean byScore, boolean byDeposit, double scorePerDollar, boolean needPassword);
	
}
