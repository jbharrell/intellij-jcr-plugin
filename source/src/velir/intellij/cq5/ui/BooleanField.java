package velir.intellij.cq5.ui;

import velir.intellij.cq5.jcr.model.AbstractProperty;

import javax.swing.*;

public class BooleanField extends JCheckBox implements ValueInput {

	public BooleanField(boolean b) {
		super("", b);
	}

	public Object getValue() {
		return isSelected();
	}

	public String getType() {
		return AbstractProperty.BOOLEAN_PREFIX;
	}

	public JComponent getComponent() {
		return this;
	}
}
