package com.shuishou.sysmgr.ui.material;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.MaterialCategory;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialogOperatorIFC;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class MaterialCategoryPanel extends JPanel implements CommonDialogOperatorIFC{
	private final Logger logger = Logger.getLogger(MaterialCategoryPanel.class.getName());
	private MaterialMgmtPanel parent;
	private JTextField tfName = new JTextField(155);
	private NumberTextField tfDisplaySeq= new NumberTextField(false);
	private MaterialCategory mc;
	
	public MaterialCategoryPanel(MaterialMgmtPanel parent){
		this.parent = parent;
		initUI();
	}
	
	private void initUI(){
		JLabel lbName = new JLabel(Messages.getString("MaterialCategoryPanel.Name"));
		JLabel lbDisplaySeq = new JLabel(Messages.getString("MaterialCategoryPanel.DisplaySequence"));
		this.setLayout(new GridBagLayout());
		add(lbName, 	new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfName, 	new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbDisplaySeq, new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfDisplaySeq, new GridBagConstraints(1, 2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(new JPanel(), new GridBagConstraints(0, 3, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		tfName.setMinimumSize(new Dimension(180,25));
		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
	}
	
	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("name", tfName.getText());
		params.put("sequence", tfDisplaySeq.getText());
		String url = "material/addmaterialcategory";
		if (mc != null){
			url = "material/updatematerialcategory";
			params.put("id", mc.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update materialcategory. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update materialcategory. URL = " + url);
			return false;
		}
		Gson gson = new Gson();
		HttpResult<MaterialCategory> result = gson.fromJson(response, new TypeToken<HttpResult<MaterialCategory>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update materialcategory. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return false;
		}
		//update parent menu tree
		if (mc == null){
			parent.insertNode(result.data);
		} else {
			parent.updateNode(result.data, mc);
		}
		return true;
	}

	private boolean doCheckInput(){
		if (tfName.getText() == null || tfName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Name");
			return false;
		}
		if (tfDisplaySeq.getText() == null || tfDisplaySeq.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Display Sequence");
			return false;
		}
		return true;
	}
	
	public void setObjectValue(MaterialCategory mc){
		this.mc = mc;
		tfName.setText(mc.getName());
		tfDisplaySeq.setText(mc.getSequence()+"");
	}
	
}
