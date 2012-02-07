package velir.intellij.cq5.jcr.model;

import com.day.cq.commons.date.InvalidDateException;
import com.intellij.openapi.util.JDOMUtil;
import org.jdom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import com.day.cq.commons.date.DateUtil;

public class VNode {

	private static final Logger log = LoggerFactory.getLogger(VNode.class);

	public static Map<String,Namespace> namespaces;

	static {
		namespaces = new HashMap<String, Namespace>();
		namespaces.put("cq", Namespace.getNamespace("cq","http://www.day.com/jcr/cq/1.0"));
		namespaces.put("jcr", Namespace.getNamespace("jcr","http://www.jcp.org/jcr/1.0"));
		namespaces.put("sling", Namespace.getNamespace("sling", "http://sling.apache.org/jcr/sling/1.0"));
		namespaces.put("slingevent", Namespace.getNamespace("slingevent", "http://sling.apache.org/jcr/sling/1.0"));
	}

	private String name;
	private Map<String, VProperty> properties;
	protected boolean canChangeType;

	private VNode (String name) {
		this.name = name;
		properties = new HashMap<String, VProperty>();
		canChangeType = false;
	}

	// constructor for new VNode, allows changing of type
	public VNode (String name, String type) {
		this.name = name;
		// populate with the default fields of this type
		VNodeDefinition vNodeDefinition = VNodeDefinition.getDefinition(type);
		if (vNodeDefinition != null) {
			properties = vNodeDefinition.getPropertiesMap(false);
		} else {
			properties = new HashMap<String, VProperty>();
		}
        //TODO: Replace with VProperty factory
		properties.put(AbstractProperty.JCR_PRIMARYTYPE, new XMLProperty(AbstractProperty.JCR_PRIMARYTYPE, type, AbstractProperty.STRING_PREFIX));
		canChangeType = true;
	}

	public void setProperty (String name, VProperty value) {
		properties.put(name, value);
	}

	public VProperty getProperty (String name) {
		return properties.get(name);
	}

	public void removeProperty (String name) {
		properties.remove(name);
	}

	public boolean hasProperty (String name) {
		return properties.containsKey(name);
	}

	/**
	 * can remove property from node?
	 * @param name
	 * @return
	 */
	public boolean canRemove (String name) {
		return (! AbstractProperty.JCR_PRIMARYTYPE.equals(name));
	}

	/**
	 * can alter property?
	 * @param name
	 * @return
	 */
	public boolean canAlter (String name) {
		return true;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getType () {
        VProperty prop = getProperty(AbstractProperty.JCR_PRIMARYTYPE);
		return (null != prop)? (String) prop.getValue(): null;
	}

	public String[] getSortedPropertyNames () {
		String[] propertyKeys = new String[properties.size()];
		propertyKeys = properties.keySet().toArray(propertyKeys);
		Arrays.sort(propertyKeys);
		return propertyKeys;
	}

	public Element getElement() {
		Element element = new Element("root", namespaces.get("jcr"));
		Set<String> elementNamespaces = new HashSet<String>();

		// properties
		for (Map.Entry<String,VProperty> property : properties.entrySet()) {

			// get namespace from property string, if there
			Namespace propertyNamespace = null;
			String propertyName = property.getKey();
			String[] attributeSections = propertyName.split(":");
			// if namespaced property
			if (attributeSections.length == 2) {
				propertyNamespace = namespaces.get(attributeSections[0]);
				if (propertyNamespace == null) {
					log.error("No namespace definition found for property: " + property.getKey());
				}
				else {
					propertyName = attributeSections[1];
					// add namespace to element if it isn't there already
					if (!elementNamespaces.contains(attributeSections[0])) {
						element.addNamespaceDeclaration(propertyNamespace);
						elementNamespaces.add(attributeSections[0]);
					}
				}
			}

			// prepend string value with property type
			Object value = property.getValue();
			String propertyStringValue = value.toString();

			// set property
			if (propertyNamespace != null) {
				// propertyName cannot have colon, even here
				element.setAttribute(propertyName, propertyStringValue, propertyNamespace);
			}
			else {
				element.setAttribute(propertyName, propertyStringValue);
			}
		}

		return element;
	}

	public static VNode makeVNode (InputStream inputStream, String name) throws JDOMException, IOException {
		VNode vNode = makeVNode(inputStream);
		vNode.setName(name);
		return vNode;
	}

	public static VNode makeVNode (InputStream inputStream) throws JDOMException, IOException {
		Document document = JDOMUtil.loadDocument(inputStream);
		final Element element = document.getRootElement();
		return makeVNode(element);
	}

	public static VNode makeVNode (final Element element) throws JDOMException, IOException {
		String name = element.getName();
		String namespace = element.getNamespacePrefix();
		if (!namespace.equals("")) name = namespace + ":" + name;
		VNode vNode = new VNode(name);

		for (Object o : element.getAttributes()) {
			Attribute attribute = (Attribute) o;

			String propertyName = attribute.getQualifiedName();
			String valueStr = attribute.getValue();

            //TODO: Get Property from a factory
            VProperty prop = XMLProperty.makeFromValueStr(propertyName, valueStr);
            vNode.setProperty(propertyName, prop);
		}

		vNode.canChangeType = false;

		return vNode;
	}

}
