package velir.intellij.cq5.ui.jcr.tree.handlers;

import velir.intellij.cq5.ui.jcr.menu.JcrContextMenu;
import velir.intellij.cq5.ui.jcr.tree.JcrTree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class used to handle mouse events against the jcr tree.
 */
public class JcrTreeMouseHandler extends MouseAdapter {
	/**
	 * Event used to handle mouse press events.
	 *
	 * @param evt Moust event.
	 */
	public void mousePressed(MouseEvent evt) {
	}

	/**
	 * Event used to handle mouse release.
	 *
	 * @param evt Mouse event.
	 */
	public void mouseReleased(MouseEvent evt) {
		//if our mouse issued a popup trigger then handle right click
		if (evt.isPopupTrigger()) {
			mouseRightClick(evt);
		}
	}

	private void mouseRightClick(MouseEvent evt) {
		//get our context menu
		JcrContextMenu menu = new JcrContextMenu();
		menu.show((JcrTree) evt.getSource(), evt.getX(), evt.getY());
	}
}
