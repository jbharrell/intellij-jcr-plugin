package velir.intellij.cq5.module;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NonNls;
import velir.intellij.cq5.config.JcrSettings;

import javax.swing.*;
import java.util.ArrayList;

public class JCRModuleType extends ModuleType<JCRModuleBuilder> {
	private static final String ID = "JCR_MODULE";

	public JCRModuleType() {
		super(ID);
	}

	public static JCRModuleType getInstance() {
		return (JCRModuleType) ModuleTypeManager.getInstance().findByID(ID);
	}

	@Override
	public JCRModuleBuilder createModuleBuilder() {
		return new JCRModuleBuilder();
	}

	@Override
	public String getName() {
		return "JCR Module";
	}

	@Override
	public String getDescription() {
		return "Allows management of JCR content";
	}

	@Override
	public Icon getBigIcon() {
		return null;  //TODO: implement
	}

	@Override
	public Icon getNodeIcon(boolean b) {
		return null;  //TODO: implement
	}

	private static ModuleWizardStep getJCRConnectionWizard (final JCRModuleBuilder jcrModuleBuilder) {
		return new ModuleWizardStep() {
			JcrSettings jcrSettings = new JcrSettings(jcrModuleBuilder.getState());

			@Override
			public JComponent getComponent() {
				return jcrSettings.createComponent();
			}

			@Override
			public void updateDataModel() {
				//TODO: implement
			}
		};
	}

	@Override
	public ModuleWizardStep[] createWizardSteps(WizardContext wizardContext, JCRModuleBuilder moduleBuilder, ModulesProvider modulesProvider) {
		final ProjectWizardStepFactory stepFactory = ProjectWizardStepFactory.getInstance();
		ArrayList<ModuleWizardStep> steps = new ArrayList<ModuleWizardStep>();
		steps.add(stepFactory.createSourcePathsStep(wizardContext, moduleBuilder, null, "reference.dialogs.new.project.fromScratch.source"));
		steps.add(getJCRConnectionWizard(moduleBuilder));
		final ModuleWizardStep[] wizardSteps = steps.toArray(new ModuleWizardStep[steps.size()]);
		return ArrayUtil.mergeArrays(wizardSteps, super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider), ModuleWizardStep.class);
	}
}
