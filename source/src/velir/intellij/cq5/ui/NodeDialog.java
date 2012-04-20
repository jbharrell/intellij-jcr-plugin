package velir.intellij.cq5.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import velir.intellij.cq5.jcr.model.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class NodeDialog extends DialogWrapper {
	final JPanel propertiesPanel = new JPanel(new VerticalFlowLayout());
	final JPanel rootPanel = new JPanel(new VerticalFlowLayout());

	private boolean canChangeName;
	private boolean canChangeType;
	private String name;
	private Map<String,ValueInput> propertyInputs;
	private FieldConstructor fieldConstructor;

	private interface StringValueFetcher {
		public String get();
	}
	private StringValueFetcher primaryTypeFetcher;
	private StringValueFetcher nameFetcher;

	public NodeDialog(Project project, final VNode vNode, boolean isNew) {
		super(project, false);

		this.canChangeName = isNew;
		this.canChangeType = isNew;
		this.name = vNode.getName();
		propertyInputs = new HashMap<String, ValueInput>();
		fieldConstructor = new FieldConstructor();

		// set fetchers to return initial values
		primaryTypeFetcher = new StringValueFetcher() {
			public String get() {
				return (String) vNode.getProperty(AbstractProperty.JCR_PRIMARYTYPE).getValue();
			}
		};
		final String finalName = name;
		nameFetcher = new StringValueFetcher() {
			public String get() {
				return finalName;
			}
		};

		populatePropertiesPanel(vNode);

		init();
		String title = isNew ? "New Node" : ("Edit " + name);
		setTitle(title);
	}

	private void changeNodeType (String type) {
		VNode vNode = new VNode(name, type);
		populatePropertiesPanel(vNode);
	}

	private void populatePropertiesPanel (VNode vNode) {
		propertiesPanel.removeAll();

		for (String key : vNode.getSortedPropertyNames()) {
			// don't add another primaryType selector
			if (! AbstractProperty.JCR_PRIMARYTYPE.equals(key)) {
				addPropertyPanel(propertiesPanel, key, vNode.getProperty(key).getValue());
			}
		}
		propertiesPanel.revalidate();
	}

	private Object getProperty (String name) {
		return propertyInputs.get(name).getValue();
	}

	private boolean hasProperty (String name) {
		return propertyInputs.containsKey(name);
	}

	private void addPropertyPanel (final JPanel parentPanel, final String name, final Object value) {

		final JPanel jPanel = new JPanel(new GridLayout(1,3));

		// make label
		JLabel jLabel = new JLabel(name);
		jPanel.add(jLabel);

		// add property input panel
		ValueInput valueInput = fieldConstructor.getValueInput(value);
		propertyInputs.put(name, valueInput);
		jPanel.add(valueInput.getComponent());

		// make remove button
		JButton jButton = new JButton("remove");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentPanel.remove(jPanel);
				propertyInputs.remove(name);
				parentPanel.revalidate();
				parentPanel.repaint();
			}
		});
		jPanel.add(jButton);

		parentPanel.add(jPanel);
	}

	@Override
	protected JComponent createCenterPanel() {

		// node name
		JPanel namePanel = new JPanel(new GridLayout(1,2));
		JLabel nameLabel = new JLabel("name");
		namePanel.add(nameLabel);
		final JTextField nameField = new JTextField(name);
		nameField.setEditable(canChangeName);
		namePanel.add(nameField);
		nameFetcher = new StringValueFetcher() {
			public String get() {
				return nameField.getText();
			}
		};
		rootPanel.add(namePanel);

		// if we could not connect to the JCR, display warning
		if (! VNodeDefinition.hasDefinitions()) {
			JLabel jLabel = new JLabel("warning: could not connect to JCR to fetch definitions");
			jLabel.setForeground(Color.RED);
			rootPanel.add(jLabel);
		}

		// node primary type
		JPanel primaryTypePanel = new JPanel(new GridLayout(1,2));
		JLabel primaryTypeLabel = new JLabel("type");
		primaryTypePanel.add(primaryTypeLabel);
		// only allow selecting of node type on node creation
        String jcrType = primaryTypeFetcher.get();
		if (canChangeType) {
			// add selector for primaryType if we could connect to the JCR and built definitions
			if (VNodeDefinition.hasDefinitions()) {
				final JComboBox jComboBox = new JComboBox(VNodeDefinition.getNodeTypeNames());
				jComboBox.setSelectedItem(jcrType);
				jComboBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String newPrimaryType = (String) jComboBox.getSelectedItem();
						changeNodeType(newPrimaryType);
					}
				});
				primaryTypeFetcher = new StringValueFetcher() {
					public String get() {
						return (String) jComboBox.getSelectedItem();
					}
				};
				primaryTypePanel.add(jComboBox);
			}
			// if we couldn't connect to the JCR, just allow the user to put anything in for primary type
			else {
				final JTextField primaryTypeField = new JTextField(jcrType);
				primaryTypeFetcher = new StringValueFetcher() {
					public String get() {
						return primaryTypeField.getText();
					}
				};
				primaryTypePanel.add(primaryTypeField);
			}
		} else {
			JTextField primaryTypeField = new JTextField(jcrType);
			primaryTypeField.setEditable(false);
			primaryTypePanel.add(primaryTypeField);
		}
		rootPanel.add(primaryTypePanel);

		// separator
		rootPanel.add(new JSeparator(JSeparator.HORIZONTAL));

		// properties
		JBScrollPane jbScrollPane = new JBScrollPane(propertiesPanel,
				JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jbScrollPane.setPreferredSize(new Dimension(600, 500));
		rootPanel.add(jbScrollPane);

		// separator
		rootPanel.add(new JSeparator(JSeparator.HORIZONTAL));

		// make add property panel
		JPanel newPropertyPanel = new JPanel(new GridLayout(1,2));
		final JTextField jTextField = new JTextField();
		newPropertyPanel.add(jTextField);
		final JComboBox addPropertyCombo = new JComboBox(AbstractProperty.TYPESTRINGS);
		newPropertyPanel.add(addPropertyCombo);
		final JButton jButton = new JButton("add property");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type = (String) addPropertyCombo.getSelectedItem();
				if (AbstractProperty.BOOLEAN_PREFIX.equals(type)) {
					addPropertyPanel(propertiesPanel, jTextField.getText(), false);
				} else if (AbstractProperty.LONG_PREFIX.equals(type)) {
					addPropertyPanel(propertiesPanel, jTextField.getText(), 0L);
				} else if (AbstractProperty.DOUBLE_PREFIX.equals(type)) {
					addPropertyPanel(propertiesPanel, jTextField.getText(), 0.0D);
				} else if (AbstractProperty.DATE_PREFIX.equals(type)) {
					addPropertyPanel(propertiesPanel, jTextField.getText(), new Date());
				} else if ((AbstractProperty.LONG_PREFIX + "[]").equals(type)) {
					addPropertyPanel(propertiesPanel, jTextField.getText(), new Long[]{0L});
				} else if ((AbstractProperty.DOUBLE_PREFIX + "[]").equals(type)) {
					addPropertyPanel(propertiesPanel, jTextField.getText(), new Double[]{0.0D});
				} else if ((AbstractProperty.BOOLEAN_PREFIX + "[]").equals(type)) {
					addPropertyPanel(propertiesPanel, jTextField.getText(), new Boolean[]{false});
				} else if ("{String}[]".equals(type)) {
					addPropertyPanel(propertiesPanel, jTextField.getText(), new String[]{""});
				} else {
					addPropertyPanel(propertiesPanel, jTextField.getText(), "");
				}
				propertiesPanel.revalidate();
			}
		});
		newPropertyPanel.add(jButton);
		jTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				jButton.setEnabled(! hasProperty(jTextField.getText()));
			}

			public void removeUpdate(DocumentEvent e) {
				jButton.setEnabled(! hasProperty(jTextField.getText()));
			}

			public void changedUpdate(DocumentEvent e) {
				jButton.setEnabled(! hasProperty(jTextField.getText()));
			}
		});
		rootPanel.add(newPropertyPanel);

		return rootPanel;
	}

	public VNode getVNode() {
		VNode vNode = new VNode(nameFetcher.get());

		// set primary type
		vNode.setProperty(AbstractProperty.JCR_PRIMARYTYPE,
				new XMLProperty(AbstractProperty.JCR_PRIMARYTYPE,
						primaryTypeFetcher.get(),
						AbstractProperty.STRING_PREFIX ));

		// set properties
		for (Map.Entry<String,ValueInput> entry : propertyInputs.entrySet()) {
			String pName = entry.getKey();
			String type = entry.getValue().getType();
			Object o = entry.getValue().getValue();
			VProperty vProperty = new XMLProperty(pName, o, type);
			vNode.setProperty(pName, vProperty);
		}
		return vNode;
	}
}
