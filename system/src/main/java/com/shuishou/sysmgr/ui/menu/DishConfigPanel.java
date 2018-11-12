package com.shuishou.sysmgr.ui.menu;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.beans.DishConfig;
import com.shuishou.sysmgr.beans.DishConfigGroup;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialogOperatorIFC;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class DishConfigPanel extends JPanel implements CommonDialogOperatorIFC{
	private final Logger logger = Logger.getLogger(DishConfigPanel.class.getName());
	private MenuMgmtPanel parent;
	private DishConfigGroup configGroup;
	private DishConfig dishConfig;
	private JTextField tfFirstLanguageName= new JTextField(155);
	private JTextField tfSecondLanguageName= new JTextField(155);
	private NumberTextField tfDisplaySeq= new NumberTextField(false);
	private NumberTextField tfAdjustPrice= new NumberTextField(true);
	private Gson gson = new Gson();
	public DishConfigPanel(MenuMgmtPanel parent){
		this.parent = parent;
		initUI();
	}
	
	public DishConfigPanel(MenuMgmtPanel parent, DishConfigGroup group){
		this.parent = parent;
		this.configGroup = group;
		initUI();
	}
	
	private void initUI(){
		JLabel lbUniqueName = new JLabel("Unique Name");
		JLabel lbFirstLanguageName = new JLabel("First Language Name");
		JLabel lbSecondLanguageName = new JLabel("Second Language Name");
		JLabel lbDisplaySeq = new JLabel("Display Sequence");
		JLabel lbAdjustPrice = new JLabel("Adjust Price");
		tfAdjustPrice.setText("0");
		setLayout(new GridBagLayout());
		add(lbFirstLanguageName, 	new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfFirstLanguageName, 	new GridBagConstraints(1, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbSecondLanguageName, 	new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfSecondLanguageName, 	new GridBagConstraints(1, 2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbDisplaySeq, 			new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfDisplaySeq, 			new GridBagConstraints(1, 3, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbAdjustPrice, 			new GridBagConstraints(0, 4, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfAdjustPrice, 			new GridBagConstraints(1, 4, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(new JPanel(), 			new GridBagConstraints(0, 5, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
		tfFirstLanguageName.setMinimumSize(new Dimension(180,25));
		tfSecondLanguageName.setMinimumSize(new Dimension(180,25));
		tfAdjustPrice.setMinimumSize(new Dimension(180, 25));
	}
	
	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("firstLanguageName", tfFirstLanguageName.getText());
		if (tfSecondLanguageName.getText() != null)
			params.put("secondLanguageName", tfSecondLanguageName.getText());
		params.put("sequence", tfDisplaySeq.getText());
		params.put("price", tfAdjustPrice.getText());
		params.put("groupId", configGroup.getId()+"");
		String url = "menu/add_dishconfig";
		if (dishConfig != null){
			url = "menu/update_dishconfig";
			params.put("id", dishConfig.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update dish config. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update dish config. URL = " + url);
			return false;
		}
		HttpResult<DishConfig> result = gson.fromJson(response, new TypeToken<HttpResult<DishConfig>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update dish config. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return false;
		}
		result.data.setGroup(configGroup);//补全信息, 父对象没有从server端带过来
		if (dishConfig == null)
			parent.insertNode(result.data);
		else {
			parent.updateNode(result.data, dishConfig);
		}
		return true;
	}
	
	private boolean doCheckInput(){
		if (tfFirstLanguageName.getText() == null || tfFirstLanguageName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input First Language Name");
			return false;
		}
		if (tfDisplaySeq.getText() == null || tfDisplaySeq.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Display Sequence");
			return false;
		}
		if (tfAdjustPrice.getText() == null || tfAdjustPrice.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Adjust Price");
			return false;
		}
		return true;
	}

	public void setObjectValue(DishConfig config){
		this.dishConfig = config;
		tfFirstLanguageName.setText(config.getFirstLanguageName());
		tfSecondLanguageName.setText(config.getSecondLanguageName());
		tfDisplaySeq.setText(config.getSequence()+"");
		tfAdjustPrice.setText(config.getPrice() + "");
	}
	
	public void setEditable(boolean b){
		tfFirstLanguageName.setEditable(b);
		tfSecondLanguageName.setEditable(b);
		tfDisplaySeq.setEditable(b);
		tfAdjustPrice.setEditable(b);
	}
}
