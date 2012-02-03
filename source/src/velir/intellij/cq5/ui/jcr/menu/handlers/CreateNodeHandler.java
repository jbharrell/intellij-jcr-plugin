package velir.intellij.cq5.ui.jcr.menu.handlers;

import velir.intellij.cq5.jcr.LightNode;
import velir.intellij.cq5.ui.jcr.tree.JcrTree;
import velir.intellij.cq5.ui.jcr.tree.JcrTreeNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handler for creating nodes in the jcr.
 */
public class CreateNodeHandler implements ActionListener {
	private JcrTree tree;

	public CreateNodeHandler(JcrTree tree) {
		this.tree = tree;
	}

	public void actionPerformed(ActionEvent e) {
		//get last selected node
		JcrTreeNode treeNode = (JcrTreeNode) this.tree.getLastSelectedPathComponent();

		//get our light node
		LightNode lightNode = (LightNode) treeNode.getUserObject();

		String name = lightNode.getName();

		String test = "";
	}
}
