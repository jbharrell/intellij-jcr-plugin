package velir.intellij.cq5.ui.jcr.menu.handlers;

import velir.intellij.cq5.ui.jcr.tree.JcrTree;
import velir.intellij.cq5.ui.jcr.tree.JcrTreeNode;

import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handler for refreshing nodes in the jcr.
 */
public class RefreshHandler implements ActionListener {
	private JcrTree tree;

	public RefreshHandler(JcrTree tree) {
		this.tree = tree;
	}

	public void actionPerformed(ActionEvent e) {
		//get last selected node
		JcrTreeNode treeNode = (JcrTreeNode) this.tree.getLastSelectedPathComponent();

		//refresh our node
		if (treeNode.refresh(true)) {
			//update our node structure.
			((DefaultTreeModel) this.tree.getModel()).nodeStructureChanged(treeNode);
		}
	}
}
