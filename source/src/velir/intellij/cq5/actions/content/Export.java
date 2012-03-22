package velir.intellij.cq5.actions.content;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import velir.intellij.cq5.config.JCRConfiguration;
import velir.intellij.cq5.jcr.model.VNode;
import velir.intellij.cq5.util.PsiUtils;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.io.*;

public class Export extends JCRAction {
	private static final int BUFFER_SIZE = 1024 * 2;
	private static final Logger log = com.intellij.openapi.diagnostic.Logger.getInstance(Export.class);

	@Override
	public void actionPerformed(AnActionEvent anActionEvent) {
		final DataContext context = anActionEvent.getDataContext();
		final Application application = ApplicationManager.getApplication();
		IdeView ideView = LangDataKeys.IDE_VIEW.getData(context);
		PsiDirectory[] dirs = ideView.getDirectories();
		JCRConfiguration jcrConfiguration = getConfiguration(anActionEvent);

		for (final PsiDirectory directory : dirs) {
			try {
				Node rootNode = jcrConfiguration.getNode(directory.getVirtualFile().getPath());
				final NodeIterator nodeIterator = rootNode.getNodes();
				application.runWriteAction(new Runnable() {
					public void run() {
						while (nodeIterator.hasNext()) {
							Node currentNode = nodeIterator.nextNode();
							try {
								exportR(currentNode, directory);
							} catch (RepositoryException re) {
								log.error("Could not export nodes", re);
							} catch (IOException ioe) {
								log.error("Could not export nodes", ioe);
							}
						}
					}
				});
			} catch (RepositoryException re) {
				log.error("Could not export nodes", re);
			}
		}
	}

	private void exportR (Node node, PsiDirectory parent) throws RepositoryException, IOException {
		// handle file nodes specially
		if (node.getPrimaryNodeType().getName().equals("nt:file")) {
			exportFile(node, parent);
			return;
		}

		// create this node
		VNode vNode = VNode.makeVNode(node);
		PsiUtils.createNode(parent, vNode);

		PsiDirectory insideDir = parent.findSubdirectory(vNode.getName());

		// recurse
		NodeIterator nodeIterator = node.getNodes();
		while (nodeIterator.hasNext()) {
			Node currentNode = nodeIterator.nextNode();
			exportR(currentNode, insideDir);
		}
	}

	/**
	 * treat a file node specially - simply save the binary content
	 * @param node
	 * @param parent
	 * @throws RepositoryException
	 * @throws IOException
	 */
	private void exportFile (Node node, PsiDirectory parent) throws RepositoryException, IOException {
		// get node vars
		Node contentNode = node.getNode("jcr:content");
		Property property = contentNode.getProperty("jcr:data");

		// get psi vars
		PsiFile psiFile = parent.createFile(node.getName());

		// get streams
		InputStream inputStream = property.getBinary().getStream();
		OutputStream outputStream = psiFile.getVirtualFile().getOutputStream(this);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

		// write data
		byte[] b = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = bufferedInputStream.read(b)) != -1) {
			bufferedOutputStream.write(b, 0, read);
		}
		bufferedInputStream.close();
		bufferedOutputStream.close();
		outputStream.close();
		inputStream.close();
	}

	@Override
	public void update(AnActionEvent e) {
		final Presentation presentation = e.getPresentation();
		boolean enabled = isJCREvent(e);

		presentation.setVisible(enabled);
		presentation.setEnabled(enabled);
	}
}
