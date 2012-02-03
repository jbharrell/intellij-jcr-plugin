package velir.intellij.cq5.module;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;

public class JCRModuleConfiguration implements ModuleComponent, PersistentStateComponent<JCRModuleConfiguration.State> {

	public static class State {
		private static final String REPOSITORY_URL = "http://localhost:4502/crx/server";
		private static final String USERNAME = "admin";
		private static final String PASSWORD = "admin";
		private static final String WORKSPACE = "crx.default";

		public String url;
		public String username;
		public String password;
		public String workspace;

		public State () {
			url = REPOSITORY_URL;
			username = USERNAME;
			password = PASSWORD;
			workspace = WORKSPACE;
		}
	}

	private State state;
	private Module module;

	public JCRModuleConfiguration(Module module) {
		this.module = module;

		state = new State();
	}

	public static JCRModuleConfiguration getInstance(Module module) {
		return module.getComponent(JCRModuleConfiguration.class);
	}

	public void initComponent() {
		// TODO: insert component initialization logic here
	}

	public void disposeComponent() {
		// TODO: insert component disposal logic here
	}

	@NotNull
	public String getComponentName() {
		return "JCRPlugin.Properties";
	}

	public void projectOpened() {
		// called when project is opened
	}

	public void projectClosed() {
		// called when project is being closed
	}

	public void moduleAdded() {
		// Invoked when the module corresponding to this component instance has been completely
		// loaded and added to the project.
	}

	public State getState() {
		return state;
	}

	public void loadState(State state) {
		this.state = state;
	}


}
