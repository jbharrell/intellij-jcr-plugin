package velir.intellij.cq5.ui;

import velir.intellij.cq5.util.DefaultingValidator;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ValidatingTextField extends JTextField {
	private final DefaultingValidator<String> validator;

	ValidatingTextField (DefaultingValidator<String> dv) {
		super(dv.getDefaultValue());
		setPreferredSize(RegexTextField.GOOD_SIZE);
		validator = dv;

		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				setText(validator.validateSave(getText()));
			}
		});
	}

	public String getLastValid () {
		return validator.validate(getText());
	}

}
