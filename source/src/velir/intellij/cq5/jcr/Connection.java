package velir.intellij.cq5.jcr;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleServiceManager;
import com.intellij.openapi.project.Project;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.jcr2dav.Jcr2davRepositoryFactory;
import velir.intellij.cq5.module.JCRModuleConfiguration;

import javax.jcr.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to create a new connection to the jcr.
 */
public class Connection {

	private JCRModuleConfiguration.State state;

	private Connection () {
	}

	public static Connection getInstance(Module module) {
		return ModuleServiceManager.getService(module, Connection.class);
	}

	public void setState (JCRModuleConfiguration.State state) {
		this.state = state;
	}

	/**
	 * Will retrieve a repository factory for getting the crx repository.
	 *
	 * @return
	 */
	public RepositoryFactory getRepositoryFactory() {
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
	public Credentials getCredentials() {
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
