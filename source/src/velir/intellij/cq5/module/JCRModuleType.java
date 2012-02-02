package velir.intellij.cq5.module;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;

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
}
