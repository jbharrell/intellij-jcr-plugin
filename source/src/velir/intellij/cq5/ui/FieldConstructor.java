package velir.intellij.cq5.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class FieldConstructor {
	private static final Logger log = LoggerFactory.getLogger(FieldConstructor.class);

	public ValueInput getValueInput(Object initialValue) {
		if (initialValue instanceof Long) {
			return new LongField("" + initialValue);
		} else if (initialValue instanceof Double) {
			return new DoubleField("" + initialValue);
		} else if (initialValue instanceof Boolean) {
			return new BooleanField((Boolean) initialValue);
		} else if (initialValue instanceof Date) {
			return new DateField((Date) initialValue);
		} else if (initialValue instanceof String) {
			return new StringField((String) initialValue);
		} else if (initialValue instanceof Long[]
				|| initialValue instanceof Double[]
				|| initialValue instanceof Boolean[]
				|| initialValue instanceof Date[]
				|| initialValue instanceof String[]) {
			return new MultiField(initialValue);
		} else {
			log.error("could not construct a specific input field for this property");
			return new StringField(initialValue.toString());
		}
	}
}
