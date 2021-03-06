package com.shuishou.digitalmenu.common.services;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shuishou.digitalmenu.ConstantValue;
import com.shuishou.digitalmenu.account.models.IUserDataAccessor;
import com.shuishou.digitalmenu.account.models.UserData;
import com.shuishou.digitalmenu.common.models.Configs;
import com.shuishou.digitalmenu.common.models.Desk;
import com.shuishou.digitalmenu.common.models.DiscountTemplate;
import com.shuishou.digitalmenu.common.models.IConfigsDataAccessor;
import com.shuishou.digitalmenu.common.models.IDeskDataAccessor;
import com.shuishou.digitalmenu.common.models.IDiscountTemplateDataAccessor;
import com.shuishou.digitalmenu.common.models.IPayWayDataAccessor;
import com.shuishou.digitalmenu.common.models.IPrinterDataAccessor;
import com.shuishou.digitalmenu.common.models.PayWay;
import com.shuishou.digitalmenu.common.models.Printer;
import com.shuishou.digitalmenu.common.views.GetDeskResult;
import com.shuishou.digitalmenu.common.views.GetDeskWithIndentResult;
import com.shuishou.digitalmenu.indent.models.IIndentDataAccessor;
import com.shuishou.digitalmenu.indent.models.Indent;
import com.shuishou.digitalmenu.indent.models.IndentDetail;
import com.shuishou.digitalmenu.log.models.LogData;
import com.shuishou.digitalmenu.log.services.ILogService;
import com.shuishou.digitalmenu.menu.models.Category2;
import com.shuishou.digitalmenu.menu.models.Category2Printer;
import com.shuishou.digitalmenu.menu.models.ICategory2DataAccessor;
import com.shuishou.digitalmenu.menu.models.ICategory2PrinterDataAccessor;
import com.shuishou.digitalmenu.views.ObjectListResult;
import com.shuishou.digitalmenu.views.ObjectResult;
import com.shuishou.digitalmenu.views.Result;


@Service
public class CommonService implements ICommonService {
	private Logger logger = Logger.getLogger(CommonService.class);
	
	@Autowired
	private IConfigsDataAccessor configsDA;
	
	@Autowired
	private IDeskDataAccessor deskDA;
	
	@Autowired
	private IUserDataAccessor userDA;
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IPrinterDataAccessor printerDA;
	
	@Autowired
	private IDiscountTemplateDataAccessor discountTemplateDA;
	
	@Autowired
	private IIndentDataAccessor indentDA;
	
	@Autowired
	private IPayWayDataAccessor payWayDA;
	
	@Autowired
	private ICategory2DataAccessor category2DA;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ICategory2PrinterDataAccessor category2PrinterDA;
	
	@Override
	@Transactional
	public ObjectResult checkUpgradeApk(){
		File file = new File(request.getSession().getServletContext().getRealPath("/")+"..\\" + ConstantValue.CATEGORY_UPGRADEAPK);
		if (!file.exists()){
			file.mkdir();
			return new ObjectResult(Result.OK, true, null);
		}
		String[] filenames = file.list();
		return new ObjectResult(Result.OK, true, filenames);
	}
	
	@Override
	@Transactional
	public ObjectResult queryConfigMap(){
		List<Configs> configs = configsDA.queryConfigs();
		HashMap<String, String> maps = new HashMap<>();
		if (configs != null){
			for(Configs c : configs){
				maps.put(c.getName(), c.getValue());
			}
		}
		return new ObjectResult(Result.OK, true, maps);
	}
	
	@Override
	@Transactional
	public ObjectResult saveCode(long userId, String oldCode, String code, String key) {
		Configs c = configsDA.getConfigsByName(key);
		if (c == null){
			c = new Configs();
			c.setName(key);
		} else {
			if (!c.getValue().equals(oldCode)){
				return new ObjectResult("old code is wrong", false);
			}
		}
		c.setValue(code);
		configsDA.saveConfigs(c);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_CONFIG.toString(), "User "+ selfUser + " change "+ key + " code " + code);

		return new ObjectResult(Result.OK, true);
	}
	

	@Override
	@Transactional
	public ObjectResult saveLanguageSet(long userId, int amount, String firstName, String secondName, boolean print2ndLanguage){
		Configs c1 = configsDA.getConfigsByName(ConstantValue.CONFIGS_LANGUAGEAMOUNT);
		if (c1 == null){
			c1 = new Configs();
			c1.setName(ConstantValue.CONFIGS_LANGUAGEAMOUNT);
		} 
		c1.setValue(String.valueOf(amount));
		configsDA.saveConfigs(c1);
		
		Configs c2 = configsDA.getConfigsByName(ConstantValue.CONFIGS_FIRSTLANGUAGENAME);
		if (c2 == null){
			c2 = new Configs();
			c2.setName(ConstantValue.CONFIGS_FIRSTLANGUAGENAME);
		} 
		c2.setValue(firstName);
		configsDA.saveConfigs(c2);
		
		Configs c3 = configsDA.getConfigsByName(ConstantValue.CONFIGS_SECONDLANGUAGENAME);
		if (c3 == null){
			c3 = new Configs();
			c3.setName(ConstantValue.CONFIGS_SECONDLANGUAGENAME);
		} 
		c3.setValue(secondName);
		configsDA.saveConfigs(c3);
		
		Configs c4 = configsDA.getConfigsByName(ConstantValue.CONFIGS_PRINT2NDLANGUAGENAME);
		if (c4 == null){
			c4 = new Configs();
			c4.setName(ConstantValue.CONFIGS_PRINT2NDLANGUAGENAME);
		}
		c4.setValue(String.valueOf(print2ndLanguage));
		configsDA.saveConfigs(c4);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_CONFIG.toString(), "User "+ selfUser + " change language. amount " + amount 
				+ ", first language " + firstName + ", second laguage " + secondName);

		return new ObjectResult(Result.OK, true);
	}
	@Override
	@Transactional
	public GetDeskResult getDesks() {
		List<Desk> desks = deskDA.queryDesks();
		GetDeskResult result = new GetDeskResult(Result.OK, true);
		result.data = new ArrayList<GetDeskResult.Desk>(desks.size());
		for(int i = 0; i<desks.size(); i++){
			GetDeskResult.Desk d = new GetDeskResult.Desk();
			d.id = desks.get(i).getId();
			d.name = desks.get(i).getName();
			d.sequence = desks.get(i).getSequence();
			if (desks.get(i).getMergeTo() != null)
				d.mergeTo = desks.get(i).getMergeTo().getName();
			result.data.add(d);
		}
		return result;
	}

	@Override
	@Transactional
	public ObjectResult saveDesk(long userId, String deskname, int sequence) {
		Desk desk = new Desk();
		desk.setName(deskname);
		desk.setSequence(sequence);
		deskDA.insertDesk(desk);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_DESK.toString(), "User "+ selfUser + " add desk "+ deskname);

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectResult updateDesk(long userId, int id, String name, int sequence) {
		Desk desk = deskDA.getDeskById(id);

		if (desk == null)
			return new ObjectResult("No desk, id = "+ id, false);
        String oldName = desk.getName();
        desk.setName(name);
		desk.setSequence(sequence);
		deskDA.updateDesk(desk);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_DESK.toString(), "User "+ selfUser + " update desk name from "+ oldName + " to "+ name);

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectResult deleteDesk(long userId, int id) {
		Desk desk = deskDA.getDeskById(id);
		if (desk == null)
			return new ObjectResult("No desk found, id = "+ id, false);
		deskDA.deleteDesk(desk);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_DESK.toString(), "User "+ selfUser + " delete desk " + desk.getName());

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectListResult getPrinters() {
		List<Printer> printers = printerDA.queryPrinters();
		return new ObjectListResult(Result.OK, true, printers);
	}

	@Override
	@Transactional
	public ObjectResult savePrinter(long userId, String name, String printerName, int type) {
		Printer p = new Printer();
		p.setName(name);
		p.setPrinterName(printerName);
		p.setType(type);
//		p.setCopy(copy);
//		p.setPrintStyle(printStyle);
		printerDA.insertPrinter(p);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_PRINTER.toString(), "User "+ selfUser + " add printer "+ printerName);

		return new ObjectResult(Result.OK, true);
	}
	
	@Override
	@Transactional
	public ObjectResult updatePrinter(long userId, int id, String name, String printerName, int type) {
		Printer p = printerDA.getPrinterById(id);
		if (p == null)
			return new ObjectResult("cannot find printer by id "+id, false);
		p.setName(name);
		p.setPrinterName(printerName);
		p.setType(type);
//		p.setCopy(copy);
//		p.setPrintStyle(printStyle);
		printerDA.updatePrinter(p);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_PRINTER.toString(), "User "+ selfUser + " update printer "+ printerName);

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectResult deletePrinter(long userId, int id) {
		Printer p = printerDA.getPrinterById(id);
		if (p == null)
			return new ObjectResult("No printer found, id = "+ id, false);
		
		//clear the connection with Category2
		List<Category2> c2s = category2DA.getAllCategory2();
		for(Category2 c2 : c2s ){
			if (c2.getCategory2PrinterList()!= null){
				for(Category2Printer cp : c2.getCategory2PrinterList()){
					if (cp.getPrinter().getId() == id){
						c2.getCategory2PrinterList().remove(cp);
						category2DA.save(c2);
						cp.setCategory2(null);
						cp.setPrinter(null);
						category2PrinterDA.delete(cp);
					}
				}
			}
		}
		printerDA.deletePrinter(p);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_PRINTER.toString(), "User "+ selfUser + " delete printer " + p.getName());

		return new ObjectResult(Result.OK, true);
	}
	
	@Override
	@Transactional
	public ObjectResult testPrinterConnection(int id) {
		Printer p = printerDA.getPrinterById(id);
		if (p == null)
			return new ObjectResult("No printer found, id = "+ id, false);
		PrinterJob pj = PrinterJob.getPrinterJob();

		HashAttributeSet hs = new HashAttributeSet();
		hs.add(new PrinterName(p.getPrinterName(), null));
		// 获取打印服务对象
		PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, hs);
		if (printService.length > 0) {
			PrintService ps = printService[0];
			try {
				pj.setPrintService(ps);
				PageFormat pf = new PageFormat();
				Paper paper = new Paper();
				paper.setImageableArea(0, 0, 200, 200);
				pf.setPaper(paper);
				pj.setPrintable(new Printable(){

					@Override
					public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
						Graphics2D g2 = (Graphics2D) g;
						if (pageIndex > 0)
							return NO_SUCH_PAGE;
						String txt = ConstantValue.DFYMDHMS.format(new Date());
						
						g2.drawString("This is a print test", 9, 20);
						g2.drawString("Current time is " + txt, 9, 40);
						
						return PAGE_EXISTS;
					}
					
				}, pf);
				pj.print();
			} catch (PrinterException e) {
				logger.error(ConstantValue.DFYMDHMS.format(new Date()) + "\n");
				logger.error("", e);
				e.printStackTrace();
				return new ObjectResult(e.getMessage(), false);
			}
		}
		return new ObjectResult(Result.OK, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional
	public GetDeskWithIndentResult getDesksWithIndents() {
		List<Desk> desks = deskDA.queryDesks();
		Collections.sort(desks, new Comparator(){

			@Override
			public int compare(Object o1, Object o2) {
				return ((Desk)o1).getId() - ((Desk)o2).getId();
			}});
		List<Indent> indents = indentDA.getUnpaidIndent();
		List<GetDeskWithIndentResult.DeskWithIndent> deskinfos = new ArrayList<>();
		for (int i = 0; i < desks.size(); i++) {
			Desk desk = desks.get(i);
			GetDeskWithIndentResult.DeskWithIndent deskinfo = new GetDeskWithIndentResult.DeskWithIndent();
			deskinfo.id = desk.getId();
			deskinfo.name = desk.getName();
			if (desk.getMergeTo() != null)
				deskinfo.mergeTo =desk.getMergeTo().getName();
			for(Indent indent : indents){
				if (indent.getDeskName().equals(desk.getName())){
					Hibernate.initialize(indent);
					Hibernate.initialize(indent.getItems());
					deskinfo.indent = indent;
//					deskinfo.price = indent.getTotalPrice();
//					deskinfo.customerAmount = indent.getCustomerAmount();
//					deskinfo.startTime = ConstantValue.DFYMDHMS.format(indent.getStartTime());
					break;
				}
			}
			deskinfos.add(deskinfo);
		}
		return new GetDeskWithIndentResult(Result.OK, true, deskinfos);
	}

	@Override
	@Transactional
	public GetDeskWithIndentResult mergeDesks(int userId, int mainDeskId, String subDesksId) {
		String[] subDeskIds = subDesksId.split("/");
		String subDesksName = "";
		List<Desk> subDesks = new ArrayList<Desk>();
		for(String sid : subDeskIds){
			subDesks.add(deskDA.getDeskById(Integer.parseInt(sid)));
		}
		Desk mainDesk = deskDA.getDeskById(mainDeskId);
		List<Indent> mainIndents = indentDA.getIndents(0, 100, null, null, new Byte[]{ConstantValue.INDENT_STATUS_OPEN}, mainDesk.getName(), null, null);
		Indent mainIndent = null;
		if (!mainIndents.isEmpty()){
			mainIndent = mainIndents.get(0);
		}
		List<Indent> subDesksIndents = new ArrayList<Indent>();
		for(Desk desk : subDesks){
			subDesksIndents.addAll(indentDA.getIndents(0, 100, null, null, new Byte[]{ConstantValue.INDENT_STATUS_OPEN}, desk.getName(), null, null));
		}
		//flag the merge info for sub desks
		for(Desk desk : subDesks){
			if (subDesksName.length() > 0)
				subDesksName += ",";
			subDesksName += desk.getName();
			desk.setMergeTo(mainDesk);
			deskDA.updateDesk(desk);
		}
		//if there are not indents on sub tables, no need to merge indent
		if (subDesksIndents.isEmpty()){
			//do nothing
		} else {
			if (mainIndent == null){
				mainIndent = new Indent();
				mainIndent.setDeskName(mainDesk.getName());
				mainIndent.setStartTime(Calendar.getInstance().getTime());
				mainIndent.setCustomerAmount(0);
				int sequence = indentDA.getMaxSequenceToday() + 1;
				mainIndent.setDailySequence(sequence);
			} 
			double totalprice = mainIndent.getTotalPrice();
			int customers = mainIndent.getCustomerAmount();
			for(Indent subIndent : subDesksIndents){
				List<IndentDetail> details = subIndent.getItems();
				for(IndentDetail detail : details){
					detail.setIndent(mainIndent);
					mainIndent.addItem(detail);
				}
				totalprice += subIndent.getTotalPrice();
				customers += subIndent.getCustomerAmount();
				subIndent.setItems(null);
				indentDA.delete(subIndent);
			}
			mainIndent.setTotalPrice(Double.parseDouble(new DecimalFormat("0.00").format(totalprice)));
			mainIndent.setCustomerAmount(customers);
			indentDA.save(mainIndent);
		}
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.MERGETABLE.toString(), "User "+ selfUser + " merge tables [ " + subDesksName + " ] to table [ "+ mainDesk.getName()+" ]");

		//prepare return data
		List<GetDeskWithIndentResult.DeskWithIndent> deskinfos = new ArrayList<>();
		GetDeskWithIndentResult.DeskWithIndent deskinfo = new GetDeskWithIndentResult.DeskWithIndent();
		deskinfo.id = mainDesk.getId();
		deskinfo.name = mainDesk.getName();
		if (mainIndent != null){
			Hibernate.initialize(mainIndent);
			Hibernate.initialize(mainIndent.getItems());
			deskinfo.indent = mainIndent;
//			deskinfo.price = mainIndent.getTotalPrice();
//			deskinfo.customerAmount = mainIndent.getCustomerAmount();
//			deskinfo.startTime = ConstantValue.DFYMDHMS.format(mainIndent.getStartTime());
		}
		
		deskinfos.add(deskinfo);
		for(Desk desk : subDesks){
			deskinfo = new GetDeskWithIndentResult.DeskWithIndent();
			deskinfo.id = desk.getId();
			deskinfo.name = desk.getName();
			deskinfo.mergeTo = mainDesk.getName();
			deskinfos.add(deskinfo);
		}
		return new GetDeskWithIndentResult(Result.OK, true, deskinfos);
	}

	@Override
	@Transactional
	public ObjectListResult getDiscountTemplates() {
		List<DiscountTemplate> templates = discountTemplateDA.queryDiscountTemplates();
		return new ObjectListResult(Result.OK, true, templates);
	}

	@Override
	@Transactional
	public ObjectResult saveDiscountTemplate(long userId, String name, double value, int type) {
		DiscountTemplate t = new DiscountTemplate();
		t.setName(name);
		t.setValue(value);
		t.setType(type);
		discountTemplateDA.insertDiscountTemplate(t);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_DISCOUNTTEMPLATE.toString(), 
				"User "+ selfUser + " add discount template "+ name);

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectResult deleteDiscountTemplate(long userId, int id) {
		DiscountTemplate t = discountTemplateDA.getDiscountTemplateById(id);
		if (t == null)
			return new ObjectResult("No Discount Template found, id = "+ id, false);
		discountTemplateDA.deleteDiscountTemplate(t);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_DISCOUNTTEMPLATE.toString(), "User "+ selfUser + " delete discount template " + t.getName());

		return new ObjectResult(Result.OK, true);
	}

	@Override
	public ObjectResult uploadErrorLog(String machineCode, MultipartFile logfile) {
		String fileName = logfile.getOriginalFilename();
		String pathName = request.getSession().getServletContext().getRealPath("/")+"../" + ConstantValue.CATEGORY_ERRORLOG;
		File path = new File(pathName);
		if (!path.exists())
			path.mkdirs();
		File f = new File(pathName + "/" + fileName);
		try {
			logfile.transferTo(f);
		} catch (IllegalStateException | IOException e) {
			return new ObjectResult(Result.FAIL, false);
		}
		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectListResult getPayWays() {
		List<PayWay> listPayWay = payWayDA.queryPayWays();
		return new ObjectListResult(Result.OK, true, listPayWay);
	}

	@Override
	@Transactional
	public ObjectResult savePayWay(long userId, String name) {
		PayWay payWay = new PayWay();
		payWay.setName(name);
		payWayDA.insertPayWay(payWay);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_PAYWAY.toString(), 
				"User "+ selfUser + " add pay way "+ name);

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectResult deletePayWay(long userId, int id) {
		PayWay payWay = payWayDA.getPayWayById(id);
		if (payWay == null)
			return new ObjectResult("No Payway found, id = "+ id, false);
		payWayDA.deletePayWay(payWay);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_PAYWAY.toString(), "User "+ selfUser + " delete payway " + payWay.getName());

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectResult saveBranchName(int userId, String branchName) {
		Configs c = configsDA.getConfigsByName(ConstantValue.CONFIGS_BRANCHNAME);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_BRANCHNAME);
		} 
		c.setValue(branchName);
		configsDA.saveConfigs(c);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_CONFIG.toString(), "User "+ selfUser + " change branch name " + branchName);

		return new ObjectResult(Result.OK, true);
	}
	
	@Override
	@Transactional
	public ObjectResult saveMemberManagementWay(int userId, boolean byScore, boolean byDeposit, double scorePerDollar, boolean needPassword) {
		Configs c = configsDA.getConfigsByName(ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT);
		}
		c.setValue(String.valueOf(byDeposit));
		configsDA.saveConfigs(c);
		
		c = configsDA.getConfigsByName(ConstantValue.CONFIGS_MEMBERMGR_BYSCORE);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_MEMBERMGR_BYSCORE);
		}
		c.setValue(String.valueOf(byScore));
		configsDA.saveConfigs(c);
		
		c = configsDA.getConfigsByName(ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR);
		}
		c.setValue(String.valueOf(scorePerDollar));
		configsDA.saveConfigs(c);
		
		c = configsDA.getConfigsByName(ConstantValue.CONFIGS_MEMBERMGR_NEEDPASSWORD);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_MEMBERMGR_NEEDPASSWORD);
		}
		c.setValue(String.valueOf(needPassword));
		configsDA.saveConfigs(c);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_CONFIG.toString(), "User "+ selfUser + " change member management way. byScore = " 
				+ byScore + ", byDeposit = " + byDeposit + ", scorePerDollar = "+ scorePerDollar + ", needPassword = " + needPassword);

		return new ObjectResult(Result.OK, true);
	}

	@Transactional
	@Override
	public ObjectResult savePrintTicket(long userId, String printTicket) {
		Configs c = configsDA.getConfigsByName(ConstantValue.CONFIGS_PRINTTICKET);
		String oldValue = null;
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_PRINTTICKET);
		} else {
			oldValue = c.getValue();
		}
		c.setValue(printTicket);
		configsDA.saveConfigs(c);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_CONFIG.toString(), "User "+ selfUser + " change Print Ticket Way from " + oldValue + " to " + printTicket);

		return new ObjectResult(Result.OK, true);
	}
}
