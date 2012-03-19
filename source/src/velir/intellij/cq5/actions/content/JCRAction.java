package velir.intellij.cq5.actions.content;

import com.intellij.facet.ProjectFacetManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import velir.intellij.cq5.facet.JCRFacetType;

public abstract class JCRAction extends AnAction {
	protected boolean isJCREvent (AnActionEvent e) {
		final DataContext context = e.getDataContext();
		final Project project = e.getData(PlatformDataKeys.PROJECT);
		Module module = LangDataKeys.MODULE.getData(context);
		return module != null && ProjectFacetManager.getInstance(project).getFacets(JCRFacetType.JCR_TYPE_ID).size() > 0;
	}
}
