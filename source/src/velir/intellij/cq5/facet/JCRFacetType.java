package velir.intellij.cq5.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import velir.intellij.cq5.config.JCRConfiguration;

public class JCRFacetType extends FacetType<JCRFacet, JCRConfiguration> {
	public static final String JCR_TYPE_STRING = "JCR_FACET_TYPE";
	public static final FacetTypeId<JCRFacet> JCR_TYPE_ID = new FacetTypeId<JCRFacet>(JCR_TYPE_STRING);

	public JCRFacetType() {
		super(JCR_TYPE_ID, JCR_TYPE_STRING, "JCR");
	}

	@Override
	public JCRConfiguration createDefaultConfiguration() {
		return new JCRConfiguration();
	}

	@Override
	public JCRFacet createFacet(@NotNull Module module, String s, @NotNull JCRConfiguration jcrConfiguration, @Nullable Facet facet) {
		return new JCRFacet(module, s, jcrConfiguration);
	}

	@Override
	public boolean isSuitableModuleType(ModuleType moduleType) {
		return moduleType instanceof JavaModuleType;
	}
}
