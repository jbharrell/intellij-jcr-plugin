package velir.intellij.cq5.util;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class NodeXMLOutputter extends XMLOutputter {

	// taken verbatim from jdom XMLOutputter
    private void printNamespace(Writer out, Namespace ns,
                                NamespaceStack namespaces)
                     throws IOException {
        String prefix = ns.getPrefix();
        String uri = ns.getURI();

        // Already printed namespace decl?
        if (uri.equals(namespaces.getURI(prefix))) {
            return;
        }

        out.write(" xmlns");
        if (!prefix.equals("")) {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(escapeAttributeEntities(uri));
        out.write("\"");
        namespaces.push(ns);
    }

	// taken verbatim from jdom XMLOutputter
	private void printQualifiedName(Writer out, Attribute a) throws IOException {
        String prefix = a.getNamespace().getPrefix();
        if ((prefix != null) && (!prefix.equals(""))) {
            out.write(prefix);
            out.write(':');
            out.write(a.getName());
        }
        else {
            out.write(a.getName());
        }
    }

	// taken from jdom XMLOutputter, then modified so that atttributes each get their own line
	protected void printAttributes (Writer out, List attributes, Element parent, NamespaceStack namespaceStack)
			throws IOException {
       for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = (Attribute) attributes.get(i);
            Namespace ns = attribute.getNamespace();
            if ((ns != Namespace.NO_NAMESPACE) &&
                (ns != Namespace.XML_NAMESPACE)) {
                    printNamespace(out, ns, namespaceStack);
            }

            out.write("\n\t");
            printQualifiedName(out, attribute);
            out.write("=");

            out.write("\"");
            out.write(escapeAttributeEntities(attribute.getValue()));
            out.write("\"");
        }
	}
}
