package velir.intellij.cq5.module;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.roots.ui.configuration.DefaultModuleConfigurationEditorFactory;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;

import java.util.ArrayList;
import java.util.List;

public class JCRModuleEditorsProvider implements ModuleConfigurationEditorProvider {
	public ModuleConfigurationEditor[] createEditors(ModuleConfigurationState moduleConfigurationState) {
		final Module module = moduleConfigurationState.getRootModel().getModule();
		if (module.getModuleType() != JCRModuleType.getInstance()) return ModuleConfigurationEditor.EMPTY;

		final DefaultModuleConfigurationEditorFactory editorFactory = DefaultModuleConfigurationEditorFactory.getInstance();
		List<ModuleConfigurationEditor> editors = new ArrayList<ModuleConfigurationEditor>();
		editors.add(editorFactory.createModuleContentRootsEditor(moduleConfigurationState));
		editors.add(new JCRConnectionConfigurationEditor(moduleConfigurationState));
		return editors.toArray(new ModuleConfigurationEditor[editors.size()]);
	}
}
