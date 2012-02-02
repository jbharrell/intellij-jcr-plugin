package velir.intellij.cq5.ui.jcr.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;


public class AddNodeMenuItem extends JMenuItem {
	public AddNodeMenuItem(String text){
		super(text);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				String s = e.paramString();
				String test = "";
			}
		});
	}
}
