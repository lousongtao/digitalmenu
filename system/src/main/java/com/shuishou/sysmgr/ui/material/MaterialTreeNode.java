package com.shuishou.sysmgr.ui.material;

import javax.swing.tree.DefaultMutableTreeNode;

import com.shuishou.sysmgr.beans.Material;
import com.shuishou.sysmgr.beans.MaterialCategory;

public class MaterialTreeNode extends DefaultMutableTreeNode {
	public MaterialTreeNode(Object o){
		super(o);
	}
	
	public String toString(){
		Object o = this.getUserObject();
		if (o instanceof MaterialCategory){
			return ((MaterialCategory)o).getName();
		}else if (o instanceof Material){
			return ((Material)o).getName();
		}
		return super.toString();
	}
}
