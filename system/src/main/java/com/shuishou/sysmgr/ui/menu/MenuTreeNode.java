package com.shuishou.sysmgr.ui.menu;

import javax.swing.tree.DefaultMutableTreeNode;

import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.DishConfig;
import com.shuishou.sysmgr.beans.DishConfigGroup;

public class MenuTreeNode extends DefaultMutableTreeNode {
	public MenuTreeNode(Object o){
		super(o);
	}
	
	public String toString(){
		Object o = this.getUserObject();
		if (o instanceof Category1){
			return ((Category1)o).getFirstLanguageName();
		}else if (o instanceof Category2){
			return ((Category2)o).getFirstLanguageName();
		}else if (o instanceof Dish){
			Dish dish = (Dish)o;
			if (!dish.isSoldOut() && !dish.isPromotion())
				return dish.getFirstLanguageName();
			
			String html = "<html>" + dish.getFirstLanguageName();
			if (dish.isPromotion()){
				html += "<font color='red'>[Promotion]</font>";
			} 
			if (dish.isSoldOut()){
				html += "<font color='blue'>[Soldout]</font>";
			}
			html +="</html>";
			return html;
		} else if (o instanceof DishConfigGroup){
			return ((DishConfigGroup)o).getFirstLanguageName();
		} else if (o instanceof DishConfig){
			DishConfig config = (DishConfig)o;
			if (!config.isSoldOut())
				return config.getFirstLanguageName();
			String html = "<html>" + config.getFirstLanguageName();
			if (config.isSoldOut()){
				html += "<font color='blue'>[Soldout]</font>";
			}
			html +="</html>";
			return html;
		}
		return super.toString();
	}
}
