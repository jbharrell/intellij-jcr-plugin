package velir.intellij.cq5.ui.jcr.tree;

import com.intellij.ui.treeStructure.Tree;
import velir.intellij.cq5.jcr.LightNode;
import velir.intellij.cq5.ui.jcr.tree.handlers.JcrTreeExpansionHandler;
import velir.intellij.cq5.ui.jcr.tree.handlers.JcrTreeMouseHandler;
import velir.intellij.cq5.ui.jcr.tree.handlers.JcrTreeSelectionHandler;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Represents a jcr tree in the intellij ui.
 */
public class JcrTree extends Tree {
	/**
	 * Will create a new jcr tree with the node passed as the root.
	 *
	 * @param rootNode The node to set as the root.
	 */
	public JcrTree(LightNode rootNode) {
		super();

		//don't display root node for this tree
		this.setShowsRootHandles(true);
		this.setRootVisible(false);

		//set our cell renderer
		this.setCellRenderer(new JcrTreeCellRenderer());

		//create our tree node from the node passed
		JcrTreeNode root = new JcrTreeNode(rootNode);

		//populate our root node
		root.populateChildren(true);

		//set our root node to our tree
		this.setModel(new DefaultTreeModel(root));

		//set our listeners for expand and collapse events
		this.addTreeExpansionListener(new JcrTreeExpansionHandler());

		//set our selection listener for select events.
		this.addTreeSelectionListener(new JcrTreeSelectionHandler());

		//set our mouse listener for mouse events.
		this.addMouseListener(new JcrTreeMouseHandler());
	}

	/**
	 * Will return the jcr path for a TreePath provided.
	 *
	 * @param path The TreePath to get the jcr path for.
	 * @return The jcr path for the TreePath provided.
	 */
	public String getJcrPath(TreePath path) {
		//get our last path component.
		Object lastNode = path.getLastPathComponent();

		//if our last node is a jcr tree node then grab its absolute path
		if (lastNode instanceof JcrTreeNode) {
			//cast our last node into our tree node
			JcrTreeNode treeNode = (JcrTreeNode) lastNode;

			//grab our jcr light node from our tree node
			LightNode jcrLightNode = (LightNode) treeNode.getUserObject();

			//return our jcr path
			return jcrLightNode.getPath();
		}
		return null;
	}

	/**
	 * Will return the expanded children of the provided path.
	 *
	 * @param path The start path to look below for expanded nodes.
	 * @return The expanded nodes.
	 */
	public List<JcrTreeNode> getExpandedChildren(TreePath path) {
		//call our overloaded method
		return this.getExpandedChildren(path, false);
	}

	/**
	 * Will return the expanded children of the provided path.
	 *
	 * @param path    The start path to look below for expanded nodes.
	 * @param descend Whether or not we should recursively look down the tree.
	 * @return The expanded nodes.
	 */
	public List<JcrTreeNode> getExpandedChildren(TreePath path, boolean descend) {
		//initialize our list to hold our expanded children.
		List<JcrTreeNode> expandedChildren = new ArrayList<JcrTreeNode>();

		//get our last path component.
		Object lastNode = path.getLastPathComponent();

		//if our last node is a jcr tree node then go through its children
		//and find the ones that are expanded
		if (lastNode instanceof JcrTreeNode) {
			//cast our last node into our tree node
			JcrTreeNode treeNode = (JcrTreeNode) lastNode;

			//go through each child and if expanded, add to our list
			Enumeration children = treeNode.children();
			while (children.hasMoreElements()) {
				//get our current child
				JcrTreeNode child = (JcrTreeNode) children.nextElement();

				//if our child is expanded then add to our list
				TreePath childPath = child.getTreePath();
				if (this.isExpanded(childPath)) {
					expandedChildren.add(child);

					//if we are looking down the tree, then add all expanded nodes for this child
					if (descend) {
						expandedChildren.addAll(this.getExpandedChildren(childPath, descend));
					}
				}
			}
		}

		//return our expanded children
		return expandedChildren;
	}
}
