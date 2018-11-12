package com.shuishou.sysmgr.ui.flavor;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Desk;
import com.shuishou.sysmgr.beans.Flavor;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Permission;
import com.shuishou.sysmgr.beans.UserData;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;


public class FlavorDialog extends JDialog implements ActionListener {

	private final Logger logger = Logger.getLogger(FlavorDialog.class.getName());
	
	private MainFrame mainFrame;
	private FlavorMgmtPanel parent;
	private Flavor flavor;
	private JTextField tfFirstLanguageName = new JTextField();
	private JTextField tfSecondLanguageName = new JTextField();
	private JButton btnSave = new JButton("Save");
	private JButton btnCancel = new JButton("Cancel");
	public FlavorDialog(MainFrame mainFrame, FlavorMgmtPanel parent,String title, Flavor flavor){
		super(mainFrame, title, true);
		this.mainFrame = mainFrame;
		this.parent = parent;
		this.flavor = flavor;
		initUI();
		initData();
	}
	
	private void initUI(){
		JLabel lbFirstLanguageName = new JLabel("First Language Name");
		JLabel lbSecondLanguageName = new JLabel("Second Language Name");
		Container c = this.getContentPane();
		c.setLayout(new GridBagLayout());
		c.add(lbFirstLanguageName, 	new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(tfFirstLanguageName, 	new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		c.add(lbSecondLanguageName, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(tfSecondLanguageName, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		c.add(btnSave, 				new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(btnCancel, 			new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		btnSave.addActionListener(this);
		btnCancel.addActionListener(this);
		
		this.setSize(250,150);
		this.setLocation((int)(mainFrame.getWidth() / 2 - this.getWidth() /2 + mainFrame.getLocation().getX()), 
				(int)(mainFrame.getHeight() / 2 - this.getHeight() / 2 + mainFrame.getLocation().getY()));
	}
	
	private void initData(){
		if (flavor != null){
			tfFirstLanguageName.setText(flavor.getFirstLanguageName());
			tfSecondLanguageName.setText(flavor.getSecondLanguageName());
		}
	}
	
	
	private void doSave(){
		if (tfFirstLanguageName.getText() == null || tfFirstLanguageName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "must input First Language name");
			return;
		}
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		params.put("firstLanguageName", tfFirstLanguageName.getText());
		if (tfSecondLanguageName.getText() != null)
			params.put("secondLanguageName", tfSecondLanguageName.getText());
		
		String url = null;
		if (flavor != null) {
			url = "menu/updateflavor";
			params.put("id", flavor.getId()+"");
		} else {
			url = "menu/addflavor";
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update flavor. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update flavor. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<Flavor> result = gson.fromJson(response, new TypeToken<HttpResult<Flavor>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update flavor. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		parent.refreshData();
		setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel){
			setVisible(false);
		} else if (e.getSource() == btnSave){
			doSave();			
		}
	}
	
}
