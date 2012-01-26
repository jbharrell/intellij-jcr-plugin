package velir.intellij.cq5.ui.menu;

import javax.swing.*;

/**
 * A context menu that will appear when the user right clicks a node
 * in the jcr tree.
 */
public class JcrContextMenu extends JPopupMenu {
	public JcrContextMenu(){
		super();

		add(new JMenuItem("test"));
	}
}
