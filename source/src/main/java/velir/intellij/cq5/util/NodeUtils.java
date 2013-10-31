package velir.intellij.cq5.util;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDefinition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for working with Nodes.
 */
public class NodeUtils {
	/**
	 * Hiding default constructor for true static class.
	 */
	private NodeUtils() {

	}

	/**
	 * Returns an array of property names sorted asc.
	 *
	 * @param node The node to get the properties for.
	 * @return
	 */
	public static List<String> getSortedPropertyNames(Node node) throws RepositoryException {
		//get our property names
		List<String> propertyNames = getPropertyNames(node);

		//sort our property names
		Collections.sort(propertyNames);

		//return our sorted ist
		return propertyNames;
	}

	/**
	 * Returns an array of property names sorted asc.  Can also include
	 * the property definitions for the node types of the node passed.
	 *
	 * @param node                       The node to get the properties for.
	 * @param includePropertyDefinitions Whether or not to include properties defined on the node type.
	 * @return
	 */
	public static List<String> getSortedPropertyNames(Node node, boolean includePropertyDefinitions) throws RepositoryException {
		//get our property names
		List<String> propertyNames = getPropertyNames(node, includePropertyDefinitions);

		//sort our property names
		Collections.sort(propertyNames);

		//return our sorted ist
		return propertyNames;
	}

	/**
	 * Returns an array of property names.
	 *
	 * @param node The node to get the properties for.
	 * @return
	 */
	public static List<String> getPropertyNames(Node node) throws RepositoryException {
		return getPropertyNames(node, false);
	}

	/**
	 * Returns an array of property names.  Can also include
	 * the property definitions for the node types of the node passed.
	 *
	 * @param node                       The node to get the properties for.
	 * @param includePropertyDefinitions Whether or not to include properties defined on the node type.
	 * @return
	 */
	public static List<String> getPropertyNames(Node node, boolean includePropertyDefinitions) throws RepositoryException {
		//get our properties iterator from our node
		PropertyIterator pi = node.getProperties();

		//go through each property and add them to our list
		List<String> propertyNames = new ArrayList<String>();
		while (pi.hasNext()) {
			Property property = pi.nextProperty();
			propertyNames.add(property.getName());
		}

		//if we are including property definitions then add them as well
		if (includePropertyDefinitions) {
			propertyNames.addAll(getNodeTypePropertyNames(node));
		}

		//return our property names
		return propertyNames;
	}

	/**
	 * Returns the property names assigned to the node types for the node provided.
	 *
	 * @param node
	 * @return
	 * @throws RepositoryException
	 */
	public static List<String> getNodeTypePropertyNames(Node node) throws RepositoryException {
		//get all our node types
		List<NodeType> nodeTypes = getAllNodeTypes(node);

		//loop through our node types and pull out our property names
		List<String> propertyNames = new ArrayList<String>();
		for (NodeType nodeType : nodeTypes) {
			//add our property names to our list
			for (PropertyDefinition propertyDefinition : nodeType.getPropertyDefinitions()) {
				String propertyName = propertyDefinition.getName();

				if (!propertyNames.contains(propertyName)) {
					propertyNames.add(propertyName);
				}
			}
		}

		//return our property names
		return propertyNames;
	}

	/**
	 * Will get all node types for the node passed.
	 * This includes all mixin node types.
	 *
	 * @param node
	 * @return
	 */
	public static List<NodeType> getAllNodeTypes(Node node) throws RepositoryException {
		//declare a list to hold all of our node types.
		List<NodeType> nodeTypes = new ArrayList<NodeType>();

		//add our primary node type
		nodeTypes.add(node.getPrimaryNodeType());

		//add our mixin node types
		for (NodeType nodeType : node.getMixinNodeTypes()) {
			nodeTypes.add(nodeType);
		}

		//return our node types
		return nodeTypes;
	}
}
