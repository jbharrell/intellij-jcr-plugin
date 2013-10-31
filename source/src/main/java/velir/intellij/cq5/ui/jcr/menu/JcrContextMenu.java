package velir.intellij.cq5.ui.jcr.menu;

import velir.intellij.cq5.ui.jcr.menu.handlers.CreateNodeHandler;
import velir.intellij.cq5.ui.jcr.menu.handlers.RefreshHandler;
import velir.intellij.cq5.ui.jcr.tree.JcrTree;

import javax.swing.*;

/**
 * A context menu that will appear when the user right clicks a node
 * in the jcr tree.
 */
public class JcrContextMenu extends JPopupMenu {
	public JcrContextMenu(JcrTree tree) {
		super();

		//create our create add node menu item.
		JcrMenuItem createNode = new JcrMenuItem(tree, "Create Node");
		createNode.addActionListener(new CreateNodeHandler(tree));
		add(createNode);

		//create our refresh menu item.
		JcrMenuItem refresh = new JcrMenuItem(tree, "Refresh");
		refresh.addActionListener(new RefreshHandler(tree));
		add(refresh);
	}
}
