package velir.intellij.cq5.actions.content;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import velir.intellij.cq5.module.JCRModuleType;

public abstract class JCRAction extends AnAction {
	protected boolean isJCREvent (AnActionEvent e) {
		final DataContext context = e.getDataContext();
		final Presentation presentation = e.getPresentation();
		Module module = LangDataKeys.MODULE.getData(context);
		return module != null && module.getModuleType() == JCRModuleType.getInstance();
	}
}
