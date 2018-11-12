package com.shuishou.sysmgr.ui.material;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Material;
import com.shuishou.sysmgr.beans.MaterialCategory;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialog;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberInputDialog;

public class MaterialMgmtPanel extends JPanel implements TreeSelectionListener, ActionListener{
	private final Logger logger = Logger.getLogger(MaterialMgmtPanel.class.getName());
	private JTabbedPane tabPane;
	private MaterialCategoryPanel pMaterialCategory;
	private MaterialPanel pMaterial;
	private JTree tree;
	private JPopupMenu popupmenuRoot = new JPopupMenu();
	private JMenuItem menuitemAddCategory = new JMenuItem(Messages.getString("MaterialMgmtPanel.AddCategory"));
	private JMenuItem menuitemRefreshTree = new JMenuItem(Messages.getString("MaterialMgmtPanel.RefreshTree"));
	private JPopupMenu popupmenuCategory = new JPopupMenu();
	private JMenuItem menuitemAddMaterial = new JMenuItem(Messages.getString("MaterialMgmtPanel.AddMaterial"));
	private JMenuItem menuitemModifyCategory = new JMenuItem(Messages.getString("MaterialMgmtPanel.Modify"));
	private JMenuItem menuitemDeleteCategory = new JMenuItem(Messages.getString("MaterialMgmtPanel.Delete"));
	private JPopupMenu popupmenuMaterial = new JPopupMenu();
	private JMenuItem menuitemModifyMaterial = new JMenuItem(Messages.getString("MaterialMgmtPanel.Modify"));
	private JMenuItem menuitemDeleteMaterial = new JMenuItem(Messages.getString("MaterialMgmtPanel.Delete"));
	private JMenuItem menuitemMaterialRecord = new JMenuItem("Records");
	private JMenuItem menuitemPurchaseMaterial = new JMenuItem("Purchase");
	private JMenuItem menuitemChangeAmountMaterial = new JMenuItem("Change Amount");
	
	private ArrayList<MaterialCategory> mcs;
	private MainFrame mainFrame;
	
	public MaterialMgmtPanel(MainFrame mainFrame, ArrayList<MaterialCategory> mcs){
		this.mainFrame = mainFrame;
		this.mcs = mcs;
		initUI();
	}
	
	private void initUI(){
		MaterialTreeNode root = new MaterialTreeNode("root");
		buildTree(root);
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		JScrollPane jspTree = new JScrollPane(tree);
		Dimension d = jspTree.getPreferredSize();
		d.width = 300;
		jspTree.setPreferredSize(d);
		
		pMaterialCategory = new MaterialCategoryPanel(this);
		pMaterial = new MaterialPanel(this);
		
		tabPane = new JTabbedPane();
		tabPane.add(Messages.getString("MaterialMgmtPanel.MaterialCategory"), pMaterialCategory);
		tabPane.add(Messages.getString("MaterialMgmtPanel.Material"), pMaterial);
		
		this.setLayout(new BorderLayout());
		add(jspTree, BorderLayout.WEST);
		add(tabPane, BorderLayout.CENTER);
		
		popupmenuRoot.add(menuitemAddCategory);
		popupmenuRoot.add(menuitemRefreshTree);
		popupmenuCategory.add(menuitemAddMaterial);
		popupmenuCategory.add(menuitemModifyCategory);
		popupmenuCategory.add(menuitemDeleteCategory);
		popupmenuMaterial.add(menuitemModifyMaterial);
		popupmenuMaterial.add(menuitemDeleteMaterial);
		popupmenuMaterial.add(menuitemMaterialRecord);
		popupmenuMaterial.add(menuitemPurchaseMaterial);
		popupmenuMaterial.add(menuitemChangeAmountMaterial);
		menuitemAddCategory.addActionListener(this);
		menuitemRefreshTree.addActionListener(this);
		menuitemAddMaterial.addActionListener(this);
		menuitemModifyCategory.addActionListener(this);
		menuitemDeleteCategory.addActionListener(this);
		menuitemModifyMaterial.addActionListener(this);
		menuitemDeleteMaterial.addActionListener(this);
		menuitemMaterialRecord.addActionListener(this);
		menuitemPurchaseMaterial.addActionListener(this);
		menuitemChangeAmountMaterial.addActionListener(this);
		tree.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				//show node info is done via valueChanged function, not here
				if (SwingUtilities.isRightMouseButton(e)){
					int row = tree.getClosestRowForLocation(e.getX(), e.getY());
			        tree.setSelectionRow(row);
			        MaterialTreeNode node = (MaterialTreeNode) tree.getLastSelectedPathComponent();
					if (node == null)
						return;
					if (node.toString().equals("root")){
						popupmenuRoot.show(e.getComponent(), e.getX(), e.getY());
					} else if (node.getUserObject() instanceof MaterialCategory){
						popupmenuCategory.show(e.getComponent(), e.getX(), e.getY());
					} else if (node.getUserObject() instanceof Material){
						popupmenuMaterial.show(e.getComponent(), e.getX(), e.getY());
					} 
				}
			}
		});
	}
	
	private void buildTree(MaterialTreeNode root) {
		if (mcs != null){
			for (int i = 0; i < mcs.size(); i++) {
				MaterialCategory mc = mcs.get(i);
				MaterialTreeNode mcnode = new MaterialTreeNode(mc);
				root.add(mcnode);
				
				if (mc.getMaterials() != null){
					for (int j = 0; j < mc.getMaterials().size(); j++) {
						Material m = mc.getMaterials().get(j);
						MaterialTreeNode mnode = new MaterialTreeNode(m);
						mcnode.add(mnode);
					}
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuitemAddCategory){
			MaterialCategoryPanel p = new MaterialCategoryPanel(this);
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MaterialMgmtPanel.AddMaterialCategory"), 300, 300);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemRefreshTree){
			mainFrame.loadListMaterialCategory();
			this.mcs = mainFrame.getListMaterialCategory();
			MaterialTreeNode root = (MaterialTreeNode)tree.getModel().getRoot();
			root.removeAllChildren();
			buildTree(root);
			tree.updateUI();
		} else if (e.getSource() == menuitemAddMaterial){
			MaterialTreeNode node = (MaterialTreeNode)tree.getLastSelectedPathComponent();
			MaterialPanel p = new MaterialPanel(this, (MaterialCategory)node.getUserObject());
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MaterialMgmtPanel.AddMaterial"), 300, 400);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemModifyCategory){
			MaterialCategoryPanel p = new MaterialCategoryPanel(this);
			MaterialTreeNode node = (MaterialTreeNode)tree.getLastSelectedPathComponent();
			p.setObjectValue((MaterialCategory)node.getUserObject());
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MaterialMgmtPanel.ModifyMaterialCategory"), 300, 400);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemDeleteCategory){
			MaterialTreeNode node = (MaterialTreeNode)tree.getLastSelectedPathComponent();
			onDeleteCategory(node);
		} else if (e.getSource() == menuitemModifyMaterial){
			MaterialPanel p = new MaterialPanel(this);
			p.hideLeftAmount();
			MaterialTreeNode node = (MaterialTreeNode)tree.getLastSelectedPathComponent();
			p.setObjectValue((Material)node.getUserObject());
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MaterialMgmtPanel.ModifyMaterial"), 300, 400);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemDeleteMaterial){
			MaterialTreeNode node = (MaterialTreeNode)tree.getLastSelectedPathComponent();
			onDeleteMaterial(node);
		} else if (e.getSource() == menuitemMaterialRecord){
			MaterialTreeNode node = (MaterialTreeNode)tree.getLastSelectedPathComponent();
			MaterialRecordDialog dlg = new MaterialRecordDialog(mainFrame, (Material)node.getUserObject(), 600, 400);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemPurchaseMaterial){
			MaterialTreeNode node = (MaterialTreeNode)tree.getLastSelectedPathComponent();
			onPurchaseMaterial(node);
			
		} else if (e.getSource() == menuitemChangeAmountMaterial){
			MaterialTreeNode node = (MaterialTreeNode)tree.getLastSelectedPathComponent();
			onChangeAmountMaterial(node);
		}
	}
	
	private void onPurchaseMaterial(MaterialTreeNode node){
		Material m = (Material)node.getUserObject();
		NumberInputDialog dlg = new NumberInputDialog(mainFrame, "Input", "Please input the purchase amount.", true);
		dlg.setVisible(true);
		if (!dlg.isConfirm)
			return;
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", m.getId() + "");
		params.put("amount", dlg.inputDouble + "");
		String url = "material/purchasematerial";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for purchase material. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for purchase material. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<Material> result = gson.fromJson(response, new TypeToken<HttpResult<Material>>(){}.getType());
		if (!result.success){
			logger.error("return false while purchase material. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		node.setUserObject(result.data);
		tree.updateUI();
		this.valueChanged(null);//refresh the property panel value
	}
	
	private void onChangeAmountMaterial(MaterialTreeNode node){
		Material m = (Material)node.getUserObject();
		NumberInputDialog dlg = new NumberInputDialog(mainFrame, "Input", "Please input the new amount.", true);
		dlg.setVisible(true);
		if (!dlg.isConfirm)
			return;
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", m.getId() + "");
		params.put("leftAmount", dlg.inputDouble + "");
		String url = "material/updatematerialamount";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for change amount material. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for change amount material. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<Material> result = gson.fromJson(response, new TypeToken<HttpResult<Material>>(){}.getType());
		if (!result.success){
			logger.error("return false while change amount material. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		node.setUserObject(result.data);
		tree.updateUI();
		this.valueChanged(null);//refresh the property panel value
		
	}
	private void onDeleteCategory(MaterialTreeNode node){
		if (tree.getModel().getChildCount(node) > 0){
			JOptionPane.showMessageDialog(this, "There are material node under this node, please delete them first.");
			return;
		}
		MaterialCategory mc = (MaterialCategory)node.getUserObject();
		if (JOptionPane.showConfirmDialog(this, "Do you want to delete this node : "+ mc.getName(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
			return;
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", mc.getId()+"");
		String url = "material/deletematerialcategory";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for delete materialcategory. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for delete materialcategory. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<HashMap<String, String>> result = gson.fromJson(response, new TypeToken<HttpResult<HashMap<String, String>>>(){}.getType());
		if (!result.success){
			logger.error("return false while delete materialcategory. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		((DefaultTreeModel)tree.getModel()).removeNodeFromParent(node);
		// refresh local data
		mainFrame.loadListMaterialCategory();
		pMaterial.initData();
	}
	
	private void onDeleteMaterial(MaterialTreeNode node){
		Material m = (Material)node.getUserObject();
		if (JOptionPane.showConfirmDialog(this, "Do you want to delete this node : "+ m.getName(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
			return;
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", m.getId()+"");
		String url = "material/deletematerial";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for delete material. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for delete material. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<HashMap<String, String>> result = gson.fromJson(response, new TypeToken<HttpResult<HashMap<String, String>>>(){}.getType());
		if (!result.success){
			logger.error("return false while delete material. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		((DefaultTreeModel)tree.getModel()).removeNodeFromParent(node);
		// refresh local data
		mainFrame.loadListMaterialCategory();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		MaterialTreeNode node = (MaterialTreeNode) tree.getLastSelectedPathComponent();
		if (node == null)
			return;
		if (node.getUserObject() instanceof MaterialCategory){
			tabPane.setSelectedIndex(0);
			pMaterialCategory.setObjectValue((MaterialCategory)node.getUserObject());
		} else if (node.getUserObject() instanceof Material){
			tabPane.setSelectedIndex(1);
			pMaterial.setObjectValue((Material)node.getUserObject());
		} 
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	public void insertNode(MaterialCategory c){
		MaterialTreeNode root = (MaterialTreeNode)tree.getModel().getRoot();
		int count = root.getChildCount();
		MaterialTreeNode newnode = new MaterialTreeNode(c);
		if(count == 0){
			root.insert(newnode, 0);
		} else {
			for (int i = 0; i < count; i++) {
				MaterialTreeNode nodei = (MaterialTreeNode)root.getChildAt(i);
				if (((MaterialCategory)nodei.getUserObject()).getSequence() > c.getSequence()){
					root.insert(newnode, i);
					break;//must break, otherwise it will be inserted again for (i == count - 1)
				}
				if (i == count - 1){
					root.insert(newnode, count);
				}
			}
		}
		//refresh UI
		tree.updateUI();
		
		//refresh local data
		mainFrame.loadListMaterialCategory();
		pMaterial.initData();
		this.valueChanged(null);//refresh the property panel value
	}
	
	public void insertNode(Material m){
		MaterialTreeNode root = (MaterialTreeNode)tree.getModel().getRoot();
		TreePath path = findPath(root, m.getMaterialCategory());
		MaterialTreeNode parentnode = (MaterialTreeNode)path.getLastPathComponent();
		int count = parentnode.getChildCount();
		MaterialTreeNode newnode = new MaterialTreeNode(m);
		if(count == 0){
			parentnode.insert(newnode, 0);
		} else {
			for (int i = 0; i < count; i++) {
				MaterialTreeNode nodei = (MaterialTreeNode)parentnode.getChildAt(i);
				if (((Material)nodei.getUserObject()).getSequence() > m.getSequence()){
					parentnode.insert(newnode, i);
					break;//must break, otherwise it will be inserted again for (i == count - 1)
				}
				if (i == count - 1){
					parentnode.insert(newnode, count);
				}
			}
		}
		//refresh UI
		tree.updateUI();
		
		//refresh local data
		mainFrame.loadListMaterialCategory();
		this.valueChanged(null);//refresh the property panel value
	}
	
	public void updateNode(MaterialCategory mc, MaterialCategory originMC){
		MaterialTreeNode root = (MaterialTreeNode)tree.getModel().getRoot();
		TreePath path = findPath(root, mc);
		MaterialTreeNode node = (MaterialTreeNode)path.getLastPathComponent();
		node.setUserObject(mc);
		((DefaultTreeModel)tree.getModel()).reload(node);//without reload, JTree can truncate the name as (...) if it is longer than before
		if (mc.getSequence() != originMC.getSequence()){
			root.remove(node);
			int count = root.getChildCount();
			if (count == 0){
				root.insert(node, 0);
			} else {
				for (int i = 0; i < count; i++) {
					MaterialTreeNode nodei = (MaterialTreeNode)root.getChildAt(i);
					if (((MaterialCategory)nodei.getUserObject()).getSequence() > mc.getSequence()){
						root.insert(node, i);
						break;//must break, otherwise it will be inserted again for (i == count - 1)
					}
					if (i == count - 1){
						root.insert(node, count);
					}
				}
			}
		}
		
		// refresh UI
		tree.updateUI();
		
		// refresh local data
		mainFrame.loadListMaterialCategory();
		pMaterial.initData();
		this.valueChanged(null);//refresh the property panel value
	}
	
	public void updateNode(Material m, Material originM){
		MaterialTreeNode root = (MaterialTreeNode)tree.getModel().getRoot();
		TreePath path = findPath(root, m);
		MaterialTreeNode node = (MaterialTreeNode)path.getLastPathComponent();
		node.setUserObject(m);
		((DefaultTreeModel)tree.getModel()).reload(node);//without reload, JTree can truncate the name as (...) if it is longer than before
		if (m.getSequence() != originM.getSequence() || m.getMaterialCategory().getId() != originM.getMaterialCategory().getId()){
			((MaterialTreeNode)node.getParent()).remove(node);
			path = findPath(root, m.getMaterialCategory());
			MaterialTreeNode parentnode = (MaterialTreeNode)path.getLastPathComponent();
			int count = parentnode.getChildCount();
			if (count == 0){
				parentnode.insert(node,  0);
			} else {
				for (int i = 0; i < count; i++) {
					MaterialTreeNode nodei = (MaterialTreeNode)parentnode.getChildAt(i);
					if (((Material)nodei.getUserObject()).getSequence() > m.getSequence()){
						parentnode.insert(node, i);
						break;//must break, otherwise it will be inserted again for (i == count - 1)
					}
					if (i == count - 1){
						parentnode.insert(node, count);
					}
				}
			}
		}
		
		// refresh UI
		tree.updateUI();
		// refresh local data
		mainFrame.loadListMaterialCategory();
		this.valueChanged(null);//refresh the property panel value
	}
	
	private TreePath findPath(MaterialTreeNode root, Object o) {
	    @SuppressWarnings("unchecked")
	    Enumeration<MaterialTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			MaterialTreeNode node = e.nextElement();
			if (node.getUserObject().getClass().getName().equals(o.getClass().getName())) {
				// since Category1, Category2, Dish use id to do equal, so here can equal them directly
				if (o.equals(node.getUserObject()))
					return new TreePath(node.getPath());
			}
		}
	    return null;
	}
}
