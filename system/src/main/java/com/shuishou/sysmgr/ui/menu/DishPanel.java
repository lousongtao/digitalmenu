package com.shuishou.sysmgr.ui.menu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.DishChoosePopinfo;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialogOperatorIFC;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class DishPanel extends JPanel implements CommonDialogOperatorIFC, ActionListener{
	private final Logger logger = Logger.getLogger(DishPanel.class.getName());
	private final static String CARDLAYOUT_COMMON = "COMMON";
	private final static String CARDLAYOUT_POPUPMESSAGE = "POPUPMESSAGE";
	private final static int DESCRIPTION_LENGTH_LIMIT = 255;
	private MenuMgmtPanel parent;
	private JTextField tfFirstLanguageName= new JTextField(155);
	private JTextField tfSecondLanguageName= new JTextField(155);
	private NumberTextField tfDisplaySeq= new NumberTextField(false);
	private NumberTextField tfPrice= new NumberTextField(true);
	private JTextField tfAbbre= new JTextField(155);
	private JComboBox cbCategory2 = new JComboBox();
	private JComboBox cbHotLevel = new JComboBox();
	private JCheckBox cbAutoMergeWhileChoose = new JCheckBox(Messages.getString("DishPanel.AutoMergeWhileChoose"));
	private JCheckBox cbAllowFlavor = new JCheckBox(Messages.getString("DishPanel.AllowFlavor"));
	private JLabel lbPicture;
	private JPanel pPicture;
	private JPanel pChooseModeCards;
	private JRadioButton rbChooseModeCommon = new JRadioButton(Messages.getString("DishPanel.Common"));
	private JRadioButton rbChooseModePopmsg = new JRadioButton(Messages.getString("DishPanel.PopupMessage"));
	private JRadioButton rbChooseAfterPopmsg = new JRadioButton(Messages.getString("DishPanel.ChooseDishAfterMessage"));
	private JRadioButton rbNoChooseAfterPopmsg = new JRadioButton(Messages.getString("DishPanel.NotChooseDishOnlyShowMessage"));
	private JRadioButton rbPurchaseTypeUnit = new JRadioButton(Messages.getString("DishPanel.PurchaseTypeUnit"));
	private JRadioButton rbPurchaseTypeWeight = new JRadioButton(Messages.getString("DishPanel.PurchaseTypeWeight"));
	private JTextField tfPopmsg1stName= new JTextField(155);
	private JTextField tfPopmsg2ndName= new JTextField(155);
	private JTextArea tfDescription_1stLang = new JTextArea();
	private JTextArea tfDescription_2ndLang = new JTextArea();
	private Dish dish;
	private Category2 parentCategory2;
	public DishPanel(MenuMgmtPanel parent){
		this.parent = parent;
		initUI();
		initData();
	}
	
	public DishPanel(MenuMgmtPanel parent, Category2 parentCategory2){
		this.parent = parent;
		this.parentCategory2 = parentCategory2;
		initUI();
		initData();
	}
	
	private void initData(){
		ArrayList<Category1> listCategory1 = parent.getMainFrame().getListCategory1s();
		cbCategory2.removeAllItems();
		for(Category1 c1 : listCategory1){
			for(Category2 c2 : c1.getCategory2s()){
				cbCategory2.addItem(c2);
			}
		}
		if (parentCategory2 != null){
			cbCategory2.setSelectedItem(parentCategory2);
		}
	}
	
	private void initUI(){
		JLabel lbFirstLanguageName = new JLabel("First Language Name");
		JLabel lbSecondLanguageName = new JLabel("Second Language Name");
		JLabel lbDescription_1stLang = new JLabel("First Language Description");
		JLabel lbDescription_2ndLang = new JLabel("Second Language Description");
		JLabel lbDisplaySeq = new JLabel("Display Sequence");
		JLabel lbCategory2 = new JLabel("Category2");
		JLabel lbPrice = new JLabel("Price");
		JLabel lbHotLevel = new JLabel("Hot Level");
		JLabel lbAbbre = new JLabel("Abbreviation");
		
		JScrollPane jspDesc_1stLang = new JScrollPane(tfDescription_1stLang, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane jspDesc_2ndLang = new JScrollPane(tfDescription_2ndLang, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jspDesc_1stLang.setMinimumSize(new Dimension(180, 80));
		jspDesc_2ndLang.setMinimumSize(new Dimension(180, 80));
//		cbCategory2.setRenderer(new Category2ListRender());
		
		cbAutoMergeWhileChoose.setSelected(true);
		
		ButtonGroup bgPurchaseType = new ButtonGroup();
		bgPurchaseType.add(rbPurchaseTypeUnit);
		bgPurchaseType.add(rbPurchaseTypeWeight);
		JPanel pPurchaseType = new JPanel();
		pPurchaseType.add(rbPurchaseTypeUnit);
		pPurchaseType.add(rbPurchaseTypeWeight);
		rbPurchaseTypeUnit.setSelected(true);
		pPurchaseType.setBorder(BorderFactory.createTitledBorder(Messages.getString("DishPanel.PurchaseType")));
		
		JPanel pBaseProperty = new JPanel(new GridBagLayout());
		pBaseProperty.setBorder(BorderFactory.createTitledBorder("Base Properties"));
		int row = 0;
		pBaseProperty.add(lbFirstLanguageName, new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfFirstLanguageName, new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbSecondLanguageName, new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfSecondLanguageName, new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbDisplaySeq,  new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfDisplaySeq,  new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbPrice, 		 new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfPrice, 		 new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbHotLevel, 	new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(cbHotLevel, 	new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbAbbre, 		new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfAbbre, 		new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbCategory2, 	new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(cbCategory2, 	new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbDescription_1stLang, new GridBagConstraints(0, row, 2, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(jspDesc_1stLang, 		new GridBagConstraints(0, row, 2, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbDescription_2ndLang, new GridBagConstraints(0, row, 2, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(jspDesc_2ndLang, 		new GridBagConstraints(0, row, 2, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		
		row++;
		pBaseProperty.add(cbAllowFlavor, 	new GridBagConstraints(0, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(cbAutoMergeWhileChoose, 	new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(pPurchaseType, new GridBagConstraints(0, row, 2, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(new JPanel(), new GridBagConstraints(0, row, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,0,0,0), 0, 0));
		
		
		lbPicture = new JLabel();
		Dimension dPicture = lbPicture.getPreferredSize();
		dPicture.width = 120;
		dPicture.height = 120;
		lbPicture.setPreferredSize(dPicture);
		pPicture = new JPanel(new BorderLayout());
		pPicture.add(lbPicture, BorderLayout.CENTER);
		pPicture.setBorder(BorderFactory.createTitledBorder("Picture"));
		
		rbChooseAfterPopmsg.setSelected(true);
		ButtonGroup bgPopmsg = new ButtonGroup();
		bgPopmsg.add(rbChooseAfterPopmsg);
		bgPopmsg.add(rbNoChooseAfterPopmsg);
		JPanel pChooseModePopmsg = new JPanel(new GridBagLayout());
		pChooseModePopmsg.add(rbChooseAfterPopmsg,  new GridBagConstraints(1, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(rbNoChooseAfterPopmsg,new GridBagConstraints(1, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(new JLabel("First Language Name"),new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(tfPopmsg1stName,		new GridBagConstraints(1, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(new JLabel("Second Language Name"),new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(tfPopmsg2ndName,		new GridBagConstraints(1, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		
		
		rbChooseModeCommon.setSelected(true);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rbChooseModeCommon);
		bg.add(rbChooseModePopmsg);
		JPanel pChooseModeRadio = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pChooseModeRadio.add(rbChooseModeCommon);
		pChooseModeRadio.add(rbChooseModePopmsg);
		pChooseModeCards = new JPanel(new CardLayout());
		pChooseModeCards.add(new JPanel(), CARDLAYOUT_COMMON);
		pChooseModeCards.add(pChooseModePopmsg, CARDLAYOUT_POPUPMESSAGE);
		JPanel pChooseMode = new JPanel(new BorderLayout());
		pChooseMode.add(pChooseModeRadio, BorderLayout.NORTH);
		pChooseMode.add(pChooseModeCards, BorderLayout.CENTER);
		pChooseMode.setBorder(BorderFactory.createTitledBorder(Messages.getString("DishPanel.ChooseMode")));
		
		this.setLayout(new GridBagLayout());
		add(pBaseProperty, 	new GridBagConstraints(0, 0, 1, 2,0,0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(10,0,0,0), 0, 0));
		add(pPicture, 		new GridBagConstraints(1, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,0,0,0), 0, 0));
		add(pChooseMode, 	new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,0,0,0), 0, 0));
		
		tfFirstLanguageName.setMinimumSize(new Dimension(180,25));
		tfSecondLanguageName.setMinimumSize(new Dimension(180,25));
		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
		tfPrice.setMinimumSize(new Dimension(180,25));
		cbHotLevel.setMinimumSize(new Dimension(180,25));
		tfAbbre.setMinimumSize(new Dimension(180,25));
		cbCategory2.setMinimumSize(new Dimension(180,25));
		tfPopmsg1stName.setMinimumSize(new Dimension(180,25));
		tfPopmsg2ndName.setMinimumSize(new Dimension(180,25));
		
		cbHotLevel.addItem(0);
		cbHotLevel.addItem(1);
		cbHotLevel.addItem(2);
		cbHotLevel.addItem(3);
		
		rbChooseModeCommon.addActionListener(this);
		rbChooseModePopmsg.addActionListener(this);
	}

	public void setEditable(boolean b){
		tfFirstLanguageName.setEditable(b);
		tfSecondLanguageName.setEditable(b);
		tfDisplaySeq.setEditable(b);
		rbChooseModeCommon.setEnabled(b);
		rbChooseModePopmsg.setEnabled(b);
		rbChooseAfterPopmsg.setEnabled(b);
		rbNoChooseAfterPopmsg.setEnabled(b);
		rbPurchaseTypeUnit.setEnabled(b);
		rbPurchaseTypeWeight.setEnabled(b);
		tfPopmsg1stName.setEditable(b);
		tfPopmsg2ndName.setEditable(b);
		tfDescription_1stLang.setEditable(b);
		tfDescription_2ndLang.setEditable(b);
		tfPrice.setEditable(b);
		tfAbbre.setEditable(b);
		cbAllowFlavor.setEnabled(b);
		cbAutoMergeWhileChoose.setEnabled(b);
		
	}
	
	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		Gson gson = new Gson();
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(MainFrame.getLoginUser().getId()));
		params.put("firstLanguageName", tfFirstLanguageName.getText());
		if (tfSecondLanguageName.getText() != null)
			params.put("secondLanguageName", tfSecondLanguageName.getText());
		params.put("sequence", tfDisplaySeq.getText());
		params.put("price", tfPrice.getText());
		params.put("hotLevel", cbHotLevel.getSelectedItem().toString());
		params.put("abbreviation", tfAbbre.getText()); 
		params.put("category2Id", String.valueOf(((Category2)cbCategory2.getSelectedItem()).getId()));
//		params.put("subitemAmount", tfSubitemAmount.getText());
		params.put("autoMerge", String.valueOf(cbAutoMergeWhileChoose.isSelected()));
		params.put("allowFlavor", String.valueOf(cbAllowFlavor.isSelected()));
		if (this.rbPurchaseTypeUnit.isSelected())
			params.put("purchaseType", String.valueOf(ConstantValue.DISH_PURCHASETYPE_UNIT));
		else if (this.rbPurchaseTypeWeight.isSelected())
			params.put("purchaseType", String.valueOf(ConstantValue.DISH_PURCHASETYPE_WEIGHT));
		if (rbChooseModeCommon.isSelected()){
			params.put("chooseMode", String.valueOf(ConstantValue.DISH_CHOOSEMODE_DEFAULT));
		} else if (rbChooseModePopmsg.isSelected()){
			if (rbChooseAfterPopmsg.isSelected()){
				params.put("chooseMode", String.valueOf(ConstantValue.DISH_CHOOSEMODE_POPINFOCHOOSE));
			} else if (rbNoChooseAfterPopmsg.isSelected()){
				params.put("chooseMode", String.valueOf(ConstantValue.DISH_CHOOSEMODE_POPINFOQUIT));
			}
			DishChoosePopinfo info = new DishChoosePopinfo();
			info.setFirstLanguageName(tfPopmsg1stName.getText());
			info.setSecondLanguageName(tfPopmsg2ndName.getText());
			params.put("sPopInfo", gson.toJson(info));
		} 
		if (tfDescription_1stLang.getText() != null){
			params.put("description_1stlang", tfDescription_1stLang.getText());
		}
		if (tfDescription_2ndLang.getText() != null){
			params.put("description_2ndlang", tfDescription_2ndLang.getText());
		}
		String url = "menu/add_dish";
		if (dish != null){
			url = "menu/update_dish";
			params.put("id", dish.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update dish. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update dish. URL = " + url);
			return false;
		}
		
		HttpResult<Dish> result = gson.fromJson(response, new TypeToken<HttpResult<Dish>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update dish. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return false;
		}
		//the category2 info is null after get from server
		result.data.setCategory2((Category2)cbCategory2.getSelectedItem());
		if (dish == null){
			parent.insertNode(result.data);
		} else {
			parent.updateNode(result.data, dish);
		}
		return true;
	}
	
	private boolean doCheckInput(){
		if (tfFirstLanguageName.getText() == null || tfFirstLanguageName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input First Language Name Name");
			return false;
		}
		if (tfDisplaySeq.getText() == null || tfDisplaySeq.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Display Sequence");
			return false;
		}
		if (tfPrice.getText() == null || tfPrice.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Price");
			return false;
		}
		if (cbHotLevel.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(this, "Please input Hot Level");
			return false;
		}
		if (tfAbbre.getText() == null || tfAbbre.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Abbreviation");
			return false;
		}
		if (cbCategory2.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(this, "Please input Category2");
			return false;
		}
		if (rbChooseModePopmsg.isSelected()){
			if (tfPopmsg1stName.getText() == null || tfPopmsg1stName.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "Please input Popup Message in Choose Mode");
				return false;
			}
		}
		if (cbAutoMergeWhileChoose.isSelected()){
			if (dish != null && dish.getConfigGroups() != null && dish.getConfigGroups().size() > 0){
				JOptionPane.showMessageDialog(this, "There are configs for this dish, cannot set Automerge. ");
				return false;
			}
		}
		if (tfDescription_1stLang.getText() != null && tfDescription_1stLang.getText().length() > DESCRIPTION_LENGTH_LIMIT){
			JOptionPane.showMessageDialog(this, "The length of first language description is too long, please short less "+DESCRIPTION_LENGTH_LIMIT+" letters.");
			return false;
		}
		if (tfDescription_2ndLang.getText() != null && tfDescription_2ndLang.getText().length() > DESCRIPTION_LENGTH_LIMIT){
			JOptionPane.showMessageDialog(this, "The length of second language description is too long, please short less "+DESCRIPTION_LENGTH_LIMIT+" letters.");
			return false;
		}
		
		return true;
	}
	
	public void setObjectValue(Dish dish){
		this.dish = dish;
		tfFirstLanguageName.setText(dish.getFirstLanguageName());
		tfSecondLanguageName.setText(dish.getSecondLanguageName());
		tfDisplaySeq.setText(dish.getSequence()+"");
		tfPrice.setText(dish.getPrice()+"");
		cbHotLevel.setSelectedIndex(dish.getHotLevel());
		tfAbbre.setText(dish.getAbbreviation());
		cbCategory2.setSelectedItem(dish.getCategory2());
		cbAutoMergeWhileChoose.setSelected(dish.isAutoMergeWhileChoose());
		cbAllowFlavor.setSelected(dish.isAllowFlavor());
		this.rbPurchaseTypeUnit.setSelected(dish.getPurchaseType() == ConstantValue.DISH_PURCHASETYPE_UNIT);
		this.rbPurchaseTypeWeight.setSelected(dish.getPurchaseType() == ConstantValue.DISH_PURCHASETYPE_WEIGHT);
		if (dish.getDescription_1stlang() != null){
			tfDescription_1stLang.setText(dish.getDescription_1stlang());
		}else {
			tfDescription_1stLang.setText(null);
		}
		if (dish.getDescription_2ndlang() != null){
			tfDescription_2ndLang.setText(dish.getDescription_2ndlang());
		} else {
			tfDescription_2ndLang.setText(null);
		}
		if (dish.getChooseMode() == ConstantValue.DISH_CHOOSEMODE_DEFAULT){
			rbChooseModeCommon.setSelected(true);
			rbChooseModeCommon.doClick();
		} else if (dish.getChooseMode() == ConstantValue.DISH_CHOOSEMODE_POPINFOCHOOSE){
			rbChooseModePopmsg.setSelected(true);
			rbChooseModePopmsg.doClick();
			rbChooseAfterPopmsg.setSelected(true);
			tfPopmsg1stName.setText(dish.getChoosePopInfo().getFirstLanguageName());
			tfPopmsg2ndName.setText(dish.getChoosePopInfo().getSecondLanguageName());
		} else if (dish.getChooseMode() == ConstantValue.DISH_CHOOSEMODE_POPINFOQUIT){
			rbChooseModePopmsg.setSelected(true);
			rbChooseModePopmsg.doClick();
			rbNoChooseAfterPopmsg.setSelected(true);
			tfPopmsg1stName.setText(dish.getChoosePopInfo().getFirstLanguageName());
			tfPopmsg2ndName.setText(dish.getChoosePopInfo().getSecondLanguageName());
		} 
		//show picture
		if (dish.getPictureName() != null){
			try {
				lbPicture.setIcon(new ImageIcon(ImageIO.read(new URL(MainFrame.SERVER_URL + "../dishimage_big/"+dish.getPictureName()))));
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		} else {
			lbPicture.setIcon(null);
		}
	}
	
	public void refreshCategory2List(){
		ArrayList<Category1> listCategory1 = parent.getMainFrame().getListCategory1s();
		cbCategory2.removeAllItems();
		for(Category1 c1 : listCategory1){
			for(Category2 c2 : c1.getCategory2s()){
				cbCategory2.addItem(c2);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rbChooseModeCommon){
			CardLayout cl = (CardLayout)pChooseModeCards.getLayout();
			cl.show(pChooseModeCards, CARDLAYOUT_COMMON);
		} else if (e.getSource() == rbChooseModePopmsg){
			CardLayout cl = (CardLayout)pChooseModeCards.getLayout();
			cl.show(pChooseModeCards, CARDLAYOUT_POPUPMESSAGE);
		} 
	}

	
	public void showPicturePanel(boolean b){
		pPicture.setVisible(b);
	}
	

	
	
	class Category2ListRender extends JLabel implements ListCellRenderer{
		
		public Category2ListRender(){}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (value != null)
				setText(((Category2)value).getFirstLanguageName());
			return this;
		}
		
	}
	
	
}
