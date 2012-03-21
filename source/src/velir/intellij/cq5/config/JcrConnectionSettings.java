package velir.intellij.cq5.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.awt.*;

public class JCRConnectionSettings implements Configurable {
	private JPanel jPanel;
	private JTextField urlField;
	private JTextField usernameField;
	private JTextField passwordField;
	private JTextField workspaceField;
	private JCRConfiguration.State state;

	public JCRConnectionSettings(JCRConfiguration.State state) {
		this.state = new JCRConfiguration.State(state);
	}

	@Nls
	public String getDisplayName() {
		return "JCR Connection Settings";
	}

	public Icon getIcon() {
		return null;
	}

	public String getHelpTopic() {
		return null;  //TODO: implement
	}

	public JComponent createComponent() {
		jPanel = new JPanel(new VerticalFlowLayout());

		// url
		JPanel urlPanel = new JPanel(new FlowLayout());
		JLabel urlLabel = new JLabel("JCR url:");
		urlPanel.add(urlLabel);
		urlField = new JTextField(state.url);
		urlPanel.add(urlField);
		jPanel.add(urlPanel);

		// username
		JPanel usernamePanel = new JPanel(new FlowLayout());
		JLabel usernameLabel = new JLabel("Username:");
		usernamePanel.add(usernameLabel);
		usernameField = new JTextField(state.username);
		usernamePanel.add(usernameField);
		jPanel.add(usernamePanel);

		// password
		JPanel passwordPanel = new JPanel(new FlowLayout());
		JLabel passwordLabel = new JLabel("Password:");
		passwordPanel.add(passwordLabel);
		passwordField = new JTextField(state.password);
		passwordPanel.add(passwordField);
		jPanel.add(passwordPanel);

		// workspace
		JPanel workspacePanel = new JPanel(new FlowLayout());
		JLabel workspaceLabel = new JLabel("Workspace:");
		workspacePanel.add(workspaceLabel);
		workspaceField = new JTextField(state.workspace);
		workspacePanel.add(workspaceField);
		jPanel.add(workspacePanel);

		return jPanel;
	}

	public boolean isModified() {
		return ! (urlField.getText().equals(state.url)
				&& usernameField.getText().equals(state.username)
				&& passwordField.getText().equals(state.password)
				&& workspaceField.getText().equals(state.workspace));
	}

	public void apply() {
		state.url = urlField.getText();
		state.username = usernameField.getText();
		state.password = passwordField.getText();
		state.workspace = workspaceField.getText();
	}

	public void reset() {
		urlField.setText(state.url);
		usernameField.setText(state.username);
		passwordField.setText(state.password);
		workspaceField.setText(state.workspace);
	}

	public void disposeUIResources() {
	}

	// note that his updates the state with the control values
	public JCRConfiguration.State getState () {
		apply();
		return state;
	}

	// note that this updates the control values to the new state values
	public void setState (JCRConfiguration.State state) {
		this.state = state;
		reset();
	}

}
