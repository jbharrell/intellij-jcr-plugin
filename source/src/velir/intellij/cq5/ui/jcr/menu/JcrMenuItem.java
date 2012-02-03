package velir.intellij.cq5.ui.jcr.menu;

import velir.intellij.cq5.ui.jcr.tree.JcrTree;

import javax.swing.*;

/**
 * Menu item for adding to a JcrContextMenu.
 */
public class JcrMenuItem extends JMenuItem {
	private JcrTree tree;

	public JcrMenuItem(JcrTree tree, String text) {
		super(text);
		this.tree = tree;
	}
}
