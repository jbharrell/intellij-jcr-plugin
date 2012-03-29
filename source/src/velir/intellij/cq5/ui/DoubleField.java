package velir.intellij.cq5.ui;

import velir.intellij.cq5.jcr.model.AbstractProperty;

import javax.swing.*;
import java.util.regex.Pattern;

public class DoubleField extends RegexTextField implements ValueInput {

	public DoubleField() {
		this("0.0");
	}

	public DoubleField(String init) {
		super(Pattern.compile("[0-9]*\\.?[0-9]*"), init);
	}

	public Object getValue() {
		String s = getText();
		return "".equals(s) ? 0.0D : Double.parseDouble(getText());
	}

	public String getType() {
		return AbstractProperty.DOUBLE_PREFIX;
	}

	public JComponent getComponent() {
		return this;
	}
}
