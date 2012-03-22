package velir.intellij.cq5.actions.content;

import com.intellij.facet.ProjectFacetManager;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import velir.intellij.cq5.config.JCRConfiguration;
import velir.intellij.cq5.config.JCRMountPoint;
import velir.intellij.cq5.facet.JCRFacet;
import velir.intellij.cq5.facet.JCRFacetType;

import java.util.List;

public abstract class JCRAction extends AnAction {
	protected boolean isJCREvent (AnActionEvent e) {
		JCRConfiguration jcrConfiguration = getConfiguration(e);
		return jcrConfiguration != null;
	}

	// get the configuration that is relevant to this event
	protected JCRConfiguration getConfiguration (AnActionEvent e) {
		final DataContext context = e.getDataContext();
		final Project project = e.getData(PlatformDataKeys.PROJECT);
		IdeView ideView = LangDataKeys.IDE_VIEW.getData(context);
		PsiDirectory[] dirs = ideView.getDirectories();

		// get facets
		List<JCRFacet> facets = ProjectFacetManager.getInstance(project).getFacets(JCRFacetType.JCR_TYPE_ID);
		if (facets.size() == 0) return null;

		// iterate through all facets, looking for one with a mount point this event is contains
		for (JCRFacet jcrFacet : facets) {
			JCRConfiguration jcrConfiguration = jcrFacet.getConfiguration();
			for (PsiDirectory directory : dirs) {
				JCRMountPoint jcrMountPoint = jcrConfiguration.getMountPoint(directory.getVirtualFile().getPath());
				if (jcrMountPoint != null) return jcrConfiguration;
			}
		}

		// couldn't find a relevant configuration/mountpoint
		return null;
	}
}
