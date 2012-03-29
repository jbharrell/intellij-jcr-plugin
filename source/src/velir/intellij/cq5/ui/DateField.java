package velir.intellij.cq5.ui;

import com.day.cq.commons.date.DateUtil;
import com.day.cq.commons.date.InvalidDateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import velir.intellij.cq5.jcr.model.AbstractProperty;
import velir.intellij.cq5.util.Anonymous;
import velir.intellij.cq5.util.DefaultingValidator;

import javax.swing.*;
import java.util.Date;

public class DateField extends ValidatingTextField implements ValueInput{
	private static final Logger log = LoggerFactory.getLogger(ValidatingTextField.class);

	public DateField(final Date date) {
		super(new DefaultingValidator<String>(
			DateUtil.getISO8601Date(date),
			new Anonymous<String, Boolean>() {
				public Boolean call(String s) {
					try {
						DateUtil.parseISO8601(s);
						return true;
					} catch (InvalidDateException ide) {
						return false;
					}
				}
			})
		);
	}

	public Object getValue() {
		try {
			return DateUtil.parseISO8601(getText()).getTime();
		} catch (InvalidDateException ide) {
			log.error("this should never happen. Date input should always be valid");
			return null;
		}
	}

	public String getType() {
		return AbstractProperty.DATE_PREFIX;
	}

	public JComponent getComponent() {
		return this;
	}
}
