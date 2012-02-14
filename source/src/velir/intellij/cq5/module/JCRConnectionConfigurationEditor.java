package velir.intellij.cq5.module;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.jetbrains.annotations.Nls;
import velir.intellij.cq5.config.JcrSettings;
import velir.intellij.cq5.jcr.model.VNodeDefinition;

import javax.swing.*;

public class JCRConnectionConfigurationEditor implements ModuleConfigurationEditor {
	private final Module module;
	private final JCRModuleConfiguration jcrModuleConfiguration;
	private final JcrSettings jcrSettings;

	public JCRConnectionConfigurationEditor(ModuleConfigurationState state) {
		module = state.getRootModel().getModule();
		jcrModuleConfiguration = JCRModuleConfiguration.getInstance(module);
		jcrSettings = new JcrSettings(jcrModuleConfiguration.getState());
	}

	public void saveData() {
		int x = 0;
		//TODO: implement
	}

	public void moduleStateChanged() {
		int x = 0;
		//TODO: implement
	}

	@Nls
	public String getDisplayName() {
		return "JCR Connection";
	}

	public Icon getIcon() {
		return null;  //TODO: implement
	}

	public String getHelpTopic() {
		return null;  //TODO: implement
	}

	public JComponent createComponent() {
		return jcrSettings.createComponent();
	}

	public boolean isModified() {
		return jcrSettings.isModified();
	}

	public void apply() throws ConfigurationException {
		jcrSettings.apply();
		jcrModuleConfiguration.loadState(jcrSettings.getState());
		jcrModuleConfiguration.processNewConnectionSettings();
	}

	public void reset() {
		jcrModuleConfiguration.reset();
	}

	public void disposeUIResources() {
		//TODO: implement
	}
}
