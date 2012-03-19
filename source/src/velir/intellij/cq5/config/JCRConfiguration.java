package velir.intellij.cq5.config;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ProjectFacetManager;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.jcr2dav.Jcr2davRepositoryFactory;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import velir.intellij.cq5.facet.JCRFacet;
import velir.intellij.cq5.facet.JCRFacetType;
import velir.intellij.cq5.jcr.model.VNodeDefinition;

import javax.jcr.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@State(
		name = "JCRConfiguration",
		storages = {
				@Storage(id = "default", file="$MODULE_FILE$")
		}
)
public class JCRConfiguration implements FacetConfiguration, PersistentStateComponent<JCRConfiguration.State> {
	public static final Logger log = com.intellij.openapi.diagnostic.Logger.getInstance(JCRConfiguration.class);

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

		public State (State state) {
			url = state.url;
			username = state.username;
			password = state.password;
			workspace = state.workspace;
		}

		@Override
		public boolean equals(Object obj) {
			if (! (obj instanceof State)) return false;
			State state = (State) obj;
			return url.equals(state.url)
					&& username.equals(state.username)
					&& password.equals(state.password)
					&& workspace.equals(state.workspace);
		}
	}

	private State state;

	public JCRConfiguration() {
		state = new State();
	}

	public JCRConfiguration(State state) {
		this.state = state;
	}

	/**
	 * gets the first configuration it can find, from any facet from any open project
	 */
	public static JCRConfiguration getAConfiguration () {

		JCRFacet jcrFacet = null;
		for (Project project : ProjectManager.getInstance().getOpenProjects()) {
			List<JCRFacet> facetList = ProjectFacetManager.getInstance(project).getFacets(JCRFacetType.JCR_TYPE_ID);
			if (facetList.size() > 0) return facetList.get(0).getJcrConfiguration();
		}

		// could not find a JCRFacet in any open projects
		return null;
	}

	public void processNewConnectionSettings () {
		try {
			VNodeDefinition.buildDefinitions(getSession());
		} catch (RepositoryException re) {
			log.error("could not build node definitions", re);
		}
	}

	public State getState() {
		return state;
	}

	public void loadState(State state) {
		this.state = state;
	}

	public FacetEditorTab[] createEditorTabs(FacetEditorContext facetEditorContext, FacetValidatorsManager facetValidatorsManager) {
		return new FacetEditorTab[] {
			new FacetEditorTab() {
				JcrSettings jcrSettings = new JcrSettings(state);

				@Nls
				public String getDisplayName() {
					return "Connection";
				}

				public JComponent createComponent() {
					return jcrSettings.createComponent();
				}

				public boolean isModified() {
					return jcrSettings.isModified();
				}

				public void reset() {
					jcrSettings.reset();
				}

				public void disposeUIResources() {
					if (jcrSettings.isModified()) {
						state = new State(jcrSettings.getState());
						processNewConnectionSettings();
					}
					jcrSettings.disposeUIResources();
				}
			}
		};
	}

	public void readExternal(Element element) throws InvalidDataException {
		//deprecated, so will not implement
	}

	public void writeExternal(Element element) throws WriteExternalException {
		//deprecated, so will not implement
	}

	/**
	 * resets to default settings
	 */
	public void reset () {
		state = new State();
	}

	/**
	 * Will retrieve a repository factory for getting the crx repository.
	 *
	 * @return
	 */
	private RepositoryFactory getRepositoryFactory() {
		//return a new jcr2dav repository factory.
		return new Jcr2davRepositoryFactory();
	}

	/**
	 * Will return the crx repository.
	 *
	 * @return
	 */
	public Repository getRepository() throws RepositoryException {
		//get our repository factory
		RepositoryFactory factory = getRepositoryFactory();

		//create our parameters to pass into our factory
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(JcrUtils.REPOSITORY_URI, state.url);

		//get our repository from our factory
		return factory.getRepository(parameters);
	}

	/**
	 * Will return our credentials to login to the repository.
	 *
	 * @return
	 */
	private Credentials getCredentials() {
		return new SimpleCredentials(state.username, state.password.toCharArray());
	}

	/**
	 * Will return a session to the crx repository.
	 *
	 * @return
	 */
	public Session getSession() throws RepositoryException {
		//get our repository
		Repository rep = getRepository();

		// abort if we couldn't get a repository
		if (rep == null) throw new RepositoryException("Could not get repository (velir code)");

		//login to our repository and return our session
		return rep.login(getCredentials(), state.workspace);
	}
}
