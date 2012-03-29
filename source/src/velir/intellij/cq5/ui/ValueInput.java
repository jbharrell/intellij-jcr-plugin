package velir.intellij.cq5.ui;

import velir.intellij.cq5.jcr.model.VProperty;

import javax.swing.*;
import java.awt.*;

public interface ValueInput {
	public Object getValue();
	public String getType();
	public JComponent getComponent();
}
