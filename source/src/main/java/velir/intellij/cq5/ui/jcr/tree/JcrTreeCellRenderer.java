package velir.intellij.cq5.ui.jcr.tree;

import velir.intellij.cq5.jcr.LightNode;
import velir.intellij.cq5.ui.images.icons.IconFactory;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Custom tree cell render for rendering our jcr tree cells.
 */
public class JcrTreeCellRenderer extends DefaultTreeCellRenderer {
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		//call our superclass functionality
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		//if our value is null then just return
		if (value == null) {
			return this;
		}

		//if we are not of the proper type then just return.
		//extra nodes that are not JcrTreeNode objects are being passed in for some reason.
		if (!(value instanceof JcrTreeNode)) {
			return this;
		}

		//get our node.
		JcrTreeNode node = (JcrTreeNode) value;

		//get our light node.
		LightNode lightNode = (LightNode) node.getUserObject();

		//get the icon for our node
		Icon icon = IconFactory.getIcon(lightNode);

		//if we didn't get an icon then get our default empty icon
		if (icon == null) {
			icon = IconFactory.getIcon("empty.gif");
		}

		//set our icon and return
		setIcon(icon);
		return this;
	}
}
