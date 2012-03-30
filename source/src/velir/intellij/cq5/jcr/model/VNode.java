package velir.intellij.cq5.jcr.model;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.JDOMUtil;
import org.jdom.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.jcr.*;

public class VNode {

	private static final Logger log = com.intellij.openapi.diagnostic.Logger.getInstance(VNode.class);

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

	public VNode (String name) {
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

	public String getPrimaryType () {
		return (String) properties.get(AbstractProperty.JCR_PRIMARYTYPE).getValue();
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

	public static VNode makeVNode (final Node node) throws RepositoryException {
		VNode vNode = new VNode(node.getName());
		PropertyIterator propertyIterator = node.getProperties();
		while (propertyIterator.hasNext()) {
			Property property = propertyIterator.nextProperty();
			AbstractProperty abstractProperty = null;

			// handle multi-valued properties
			if (property.isMultiple()) {
				Value[] values = property.getValues();

				if (property.getType() == PropertyType.BOOLEAN) {
					boolean[] bs = new boolean[values.length];
					for (int i = 0; i < values.length; i++) {
						bs[i] = values[i].getBoolean();
					}
					abstractProperty = new XMLProperty(property.getName(), bs, AbstractProperty.BOOLEAN_ARRAY_PREFIX);
				} else if (property.getType() == PropertyType.DOUBLE) {
					double[] ds = new double[values.length];
					for (int i = 0; i < values.length; i++) {
						ds[i] = values[i].getDouble();
					}
					abstractProperty = new XMLProperty(property.getName(), ds, AbstractProperty.DOUBLE_ARRAY_PREFIX);
				} else if (property.getType() == PropertyType.LONG) {
					long[] ls = new long[values.length];
					for (int i = 0; i < values.length; i++) {
						ls[i] = values[i].getLong();
					}
					abstractProperty = new XMLProperty(property.getName(), ls, AbstractProperty.LONG_ARRAY_PREFIX);
				} else if (property.getType() == PropertyType.STRING) {
					String[] ss = new String[values.length];
					for (int i = 0; i < values.length; i++) {
						ss[i] = values[i].getString();
					}
					abstractProperty = new XMLProperty(property.getName(), ss, AbstractProperty.STRING_ARRAY_PREFIX);
				} else if (property.getType() == PropertyType.DATE) {
					Date[] ds = new Date[values.length];
					for (int i = 0; i < values.length; i++) {
						ds[i] = values[i].getDate().getTime();
					}
					abstractProperty = new XMLProperty(property.getName(), ds, AbstractProperty.DATE_ARRAY_PREFIX);
				} else {
					log.warn("JCR property (multiple) unsupported: " + property.getType());

					// fall back to string
					String[] ss = new String[values.length];
					for (int i = 0; i < values.length; i++) {
						ss[i] = values[i].getString();
					}
					abstractProperty = new XMLProperty(property.getName(), ss, AbstractProperty.STRING_ARRAY_PREFIX);
				}
			}

			// handle single-valued properties
			else {
				if (property.getType() == PropertyType.BOOLEAN) {
					abstractProperty = new XMLProperty(property.getName(), property.getBoolean(),
							AbstractProperty.BOOLEAN_PREFIX);
				} else if (property.getType() == PropertyType.DOUBLE) {
					abstractProperty = new XMLProperty(property.getName(), property.getDouble(),
							AbstractProperty.DOUBLE_PREFIX);
				} else if (property.getType() == PropertyType.LONG) {
					abstractProperty = new XMLProperty(property.getName(), property.getLong(),
							AbstractProperty.LONG_PREFIX);
				} else if (property.getType() == PropertyType.STRING) {
					abstractProperty = new XMLProperty(property.getName(), property.getString(),
							AbstractProperty.STRING_PREFIX);
				} else if (property.getType() == PropertyType.DATE) {
					abstractProperty = new XMLProperty(property.getName(), property.getDate().getTime(),
							AbstractProperty.DATE_PREFIX);
				} else if (property.getType() == PropertyType.NAME) {
					// treat name as string
					abstractProperty = new XMLProperty(property.getName(), property.getString(),
							AbstractProperty.STRING_PREFIX);
				} else { // fall back to string
					log.warn("JCR property unsupported: " + property.getType());
					abstractProperty = new XMLProperty(property.getName(), property.getString(),
							AbstractProperty.STRING_PREFIX);
				}
			}

			vNode.setProperty(property.getName(), abstractProperty);
		}

		return vNode;
	}

}
