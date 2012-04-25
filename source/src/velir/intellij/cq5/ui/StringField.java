package velir.intellij.cq5.ui;

import velir.intellij.cq5.jcr.model.AbstractProperty;

import javax.swing.*;

public class StringField extends JTextField implements ValueInput {

	public StringField(String s) {
		super(s);
		setPreferredSize(RegexTextField.GOOD_SIZE);
	}

	public Object getValue() {
		return getText();
	}

	public String getType() {
		return AbstractProperty.STRING_PREFIX;
	}

	public JComponent getComponent() {
		return this;
	}
}
