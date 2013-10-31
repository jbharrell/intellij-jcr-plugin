package velir.intellij.cq5.ui;

import velir.intellij.cq5.jcr.model.AbstractProperty;

import javax.swing.*;
import java.util.regex.Pattern;

public class LongField extends RegexTextField implements ValueInput {

	public LongField() {
		this("0");
	}

	public LongField(String init) {
		super(Pattern.compile("[0-9]*"), init);
	}

	public Object getValue () {
		String s = getText();
		return "".equals(s) ? 0L : Long.parseLong(getText());
	}

	public String getType() {
		return AbstractProperty.LONG_PREFIX;
	}

	public JComponent getComponent() {
		return this;
	}
}
