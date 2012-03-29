package velir.intellij.cq5.ui;

import com.intellij.openapi.ui.VerticalFlowLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import velir.intellij.cq5.jcr.model.AbstractProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.CubicCurve2D;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MultiField implements ValueInput {
	private static final Logger log = LoggerFactory.getLogger(MultiField.class);

	private Set<ValueInput> inputs;
	private JPanel outerPanel;
	private JPanel valuesPanel;
	private FieldConstructor fieldConstructor;
	private Object emptyValue;
	private Object defaultValue;

	public MultiField (Object initialValue) {
		fieldConstructor = new FieldConstructor();
		inputs = new HashSet<ValueInput>();
		outerPanel = new JPanel(new VerticalFlowLayout());
		valuesPanel = new JPanel(new VerticalFlowLayout());

		// get default values
		if (initialValue instanceof Long[]) {
			emptyValue = new Long[] {};
			defaultValue = 0L;
		} else if (initialValue instanceof Double[]) {
			emptyValue = new Double[] {};
			defaultValue = 0.0D;
		} else if (initialValue instanceof Boolean[]) {
			emptyValue = new Boolean[] {};
			defaultValue = false;
		} else if (initialValue instanceof String[]) {
			emptyValue = new String[] {};
			defaultValue = "";
		} else if (initialValue instanceof Date[]) {
			emptyValue = new Date[] {};
			defaultValue = new Date();
		} else {
			log.error("invalid property type");
		}

		// add the rest of the components
		for (Object o : (Object[]) initialValue) {
			addValuePanel(o);
		}
		outerPanel.add(valuesPanel);

		JButton jButton = new JButton("Add");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addValuePanel(defaultValue);
				valuesPanel.revalidate();
			}
		});
		outerPanel.add(jButton);
	}

	private void addValuePanel (Object initialValue) {
		final JPanel innerPanel = new JPanel(new FlowLayout());
		final ValueInput valueInput = fieldConstructor.getValueInput(initialValue);
		inputs.add(valueInput);
		innerPanel.add(valueInput.getComponent());

		// remove value button
		JButton jButton = new JButton("X");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputs.remove(valueInput);
				valuesPanel.remove(innerPanel);
				valuesPanel.revalidate();
			}
		});
		innerPanel.add(jButton);

		valuesPanel.add(innerPanel);
	}

	public Object getValue() {
		if (inputs.size() == 0) return emptyValue;
		if (emptyValue instanceof Long[]) {
			Long[] values = new Long[inputs.size()];
			int i = 0;
			for (ValueInput valueInput : inputs) {
				values[i++] = (Long) valueInput.getValue();
			}
			return values;
		} else if (emptyValue instanceof Double[]) {
			Double[] values = new Double[inputs.size()];
			int i = 0;
			for (ValueInput valueInput : inputs) {
				values[i++] = (Double) valueInput.getValue();
			}
			return values;
		} else if (emptyValue instanceof Boolean[]) {
			Boolean[] values = new Boolean[inputs.size()];
			int i = 0;
			for (ValueInput valueInput : inputs) {
				values[i++] = (Boolean) valueInput.getValue();
			}
			return values;
		} else if (emptyValue instanceof String[]) {
			String[] values = new String[inputs.size()];
			int i = 0;
			for (ValueInput valueInput : inputs) {
				values[i++] = (String) valueInput.getValue();
			}
			return values;
		} else if (emptyValue instanceof Date[]) {
			Date[] values = new Date[inputs.size()];
			int i = 0;
			for (ValueInput valueInput : inputs) {
				values[i++] = (Date) valueInput.getValue();
			}
			return values;
		} else {
			log.error("can't get value back from multivalue");
			return null;
		}
	}

	public String getType() {
		if (emptyValue instanceof Long[]) {
			return AbstractProperty.LONG_ARRAY_PREFIX;
		} else if (emptyValue instanceof Double[]) {
			return AbstractProperty.DOUBLE_ARRAY_PREFIX;
		} else if (emptyValue instanceof Boolean[]) {
			return AbstractProperty.BOOLEAN_ARRAY_PREFIX;
		} else if (emptyValue instanceof Date[]) {
			return AbstractProperty.DATE_ARRAY_PREFIX;
		} else if (emptyValue instanceof String[]) {
			return AbstractProperty.STRING_ARRAY_PREFIX;
		} else {
			log.error("somehow the type of multivalue is messed up");
			return null;
		}
	}

	public JComponent getComponent() {
		return outerPanel;
	}
}
