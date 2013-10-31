package velir.intellij.cq5.util;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for helping work with Repository objects.
 */
public class RepositoryUtils {
	/**
	 * Hide default constructor for true static class.
	 */
	private RepositoryUtils() {

	}

	public static List<String> getAllNodeTypeNames(Session session) throws RepositoryException {
		//get our child nodeTypes from our root
		NodeIterator nodeTypes = getAllNodeTypes(session);

		//go through each node type and pull out the name
		List<String> nodeTypeNames = new ArrayList<String>();
		while (nodeTypes.hasNext()) {
			Node node = nodeTypes.nextNode();
			nodeTypeNames.add(node.getName());
		}

		//return our node type names
		return nodeTypeNames;
	}

	/**
	 * Will get all the nodes that represent the different node types.
	 *
	 * @param session Repository session to use for pulling out this information.
	 * @return
	 * @throws RepositoryException
	 */
	public static NodeIterator getAllNodeTypes(Session session) throws RepositoryException {
		//get our node types root
		Node nodeTypesRoot = session.getNode("/jcr:system/jcr:nodeTypes");

		//get our child nodeTypes from our root
		return nodeTypesRoot.getNodes();
	}
}
