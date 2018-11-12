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
import com.shuishou.sysmgr.beans.DishConfigGroup;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialogOperatorIFC;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class DishConfigGroupPanel extends JPanel implements CommonDialogOperatorIFC{
	private final Logger logger = Logger.getLogger(DishConfigGroupPanel.class.getName());
	private DishConfigGroupDialog parent;
	private MenuMgmtPanel menuMgmtPanel;
	private DishConfigGroup dcGroup;
	private JCheckBox cbAllowDuplication = new JCheckBox("Allow Duplicate Choose");
	private JTextField tfFirstLanguageName= new JTextField(155);
	private JTextField tfSecondLanguageName= new JTextField(155);
	private JTextField tfUniqueName= new JTextField(155);
	private NumberTextField tfDisplaySeq= new NumberTextField(false);
	private NumberTextField tfRequiredQuantity= new NumberTextField(false);
	private Gson gson = new Gson();
	public DishConfigGroupPanel(DishConfigGroupDialog parent, MenuMgmtPanel menuMgmtPanel){
		this.parent = parent;
		this.menuMgmtPanel = menuMgmtPanel;
		initUI();
		
	}
	
	public DishConfigGroupPanel(MenuMgmtPanel menuMgmtPanel){
		this.menuMgmtPanel = menuMgmtPanel;
		initUI();
		
	}
	
	private void initUI(){
		JLabel lbUniqueName = new JLabel("Unique Name");
		JLabel lbFirstLanguageName = new JLabel("First Language Name");
		JLabel lbSecondLanguageName = new JLabel("Second Language Name");
		JLabel lbDisplaySeq = new JLabel("Display Sequence");
		JLabel lbRequiredQuantity = new JLabel("Required Quantity");
		tfRequiredQuantity.setText("0");
		setLayout(new GridBagLayout());
		add(lbUniqueName, 			new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfUniqueName, 			new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbFirstLanguageName, 	new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfFirstLanguageName, 	new GridBagConstraints(1, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbSecondLanguageName, 	new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfSecondLanguageName, 	new GridBagConstraints(1, 2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbDisplaySeq, 			new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfDisplaySeq, 			new GridBagConstraints(1, 3, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbRequiredQuantity, 	new GridBagConstraints(0, 4, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfRequiredQuantity, 	new GridBagConstraints(1, 4, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(cbAllowDuplication, 	new GridBagConstraints(1, 5, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(new JPanel(), 			new GridBagConstraints(0, 6, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
		tfFirstLanguageName.setMinimumSize(new Dimension(180,25));
		tfSecondLanguageName.setMinimumSize(new Dimension(180,25));
		tfRequiredQuantity.setMinimumSize(new Dimension(180, 25));
		tfUniqueName.setMinimumSize(new Dimension(180, 25));
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
		params.put("requiredQuantity", tfRequiredQuantity.getText());
		params.put("allowDuplicate", String.valueOf(cbAllowDuplication.isSelected()));
		params.put("uniqueName", tfUniqueName.getText());
		String url = "menu/add_dishconfiggroup";
		if (dcGroup != null){
			url = "menu/update_dishconfiggroup";
			params.put("id", dcGroup.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update dish config group. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update dish config group. URL = " + url);
			return false;
		}
		HttpResult<DishConfigGroup> result = gson.fromJson(response, new TypeToken<HttpResult<DishConfigGroup>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update dish config group. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return false;
		}
		if (dcGroup != null){
			if (parent != null)
				parent.updateConfigGroupInList(result.data);
			//修改的ConfigGroup要刷新整棵树
			menuMgmtPanel.updateNode(result.data, dcGroup);
		} else {
			if (parent != null)
				parent.addConfigGroupToList(result.data);
			
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
		if (tfRequiredQuantity.getText() == null || tfRequiredQuantity.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Required Quantity");
			return false;
		}
		if (tfUniqueName.getText() == null || tfUniqueName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Unique Name");
			return false;
		}
		return true;
	}

	public void setObjectValue(DishConfigGroup group){
		this.dcGroup = group;
		tfFirstLanguageName.setText(group.getFirstLanguageName());
		tfSecondLanguageName.setText(group.getSecondLanguageName());
		tfDisplaySeq.setText(group.getSequence()+"");
		tfRequiredQuantity.setText(group.getRequiredQuantity() + "");
		tfUniqueName.setText(group.getUniqueName());
		cbAllowDuplication.setSelected(group.isAllowDuplicate());
	}
	
	public void setEditable(boolean b){
		tfFirstLanguageName.setEditable(b);
		tfSecondLanguageName.setEditable(b);
		tfDisplaySeq.setEditable(b);
		tfUniqueName.setEditable(b);
		tfRequiredQuantity.setEditable(b);
		cbAllowDuplication.setEnabled(b);
	}
}
