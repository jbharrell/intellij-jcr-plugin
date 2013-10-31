package velir.intellij.cq5.components;

import com.intellij.facet.ProjectFacetManager;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import velir.intellij.cq5.facet.JCRFacet;
import velir.intellij.cq5.facet.JCRFacetType;
import velir.intellij.cq5.jcr.LightNode;
import velir.intellij.cq5.ui.jcr.tree.JcrTree;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * A component that will present the jcr repository to the user.
 */
public class JcrViewComponent extends AbstractProjectComponent {
	private static final Logger log = com.intellij.openapi.diagnostic.Logger.getInstance(AbstractProjectComponent.class);

	Project project;

	/**
	 * Constructor creating a new jcr view component.
	 *
	 * @param project The current project.
	 */
	protected JcrViewComponent(Project project) {
		super(project);
		this.project = project;
	}

	@Override
	public void projectOpened() {
		super.projectOpened();
		setupToolWindow();
	}

	@Override
	public void initComponent() {
		super.initComponent();
	}

	private void setupToolWindow() {
		ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
		JBScrollPane panel = createPanel();

		ToolWindow toolWindow = toolWindowManager.registerToolWindow("CRX Repository", true, ToolWindowAnchor.LEFT);

		ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

		Content content = contentFactory.createContent(panel, "", false);

		toolWindow.getContentManager().addContent(content);
	}

	private JBScrollPane createPanel() {
		//declare our session object.
		Session session = null;

		//declare our root node.
		LightNode rootNode = null;

		//try to get our root node from crx.
		try {
			//get our session
			JCRFacet jcrFacet = ProjectFacetManager.getInstance(project).getFacets(JCRFacetType.JCR_TYPE_ID).get(0);
			session = jcrFacet.getJcrConfiguration().getSession();

			//pull out our root node.
			Node jcrRootNode = session.getRootNode();

			//create our light node from our jcr node
			rootNode = new LightNode(jcrRootNode);
		} catch (RepositoryException rex) {
			log.error("Could not get JCR session", rex);
		} finally {
			//if we were able to retrieve a session then logout
			if (session != null) {
				session.logout();
			}
		}

		//create our jcr tree from our root node
		JcrTree tree = new JcrTree(rootNode);

		//create and return our panel.
		return new JBScrollPane(tree);
	}
}